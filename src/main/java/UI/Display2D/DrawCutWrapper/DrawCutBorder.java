package UI.Display2D.DrawCutWrapper;

import Common.DTO.CutDTO;
import Common.DTO.DimensionDTO;
import Common.DTO.VertexDTO;
import Common.Units;
import Domain.CutType;
import UI.Display2D.Drawing;
import UI.Display2D.Rendering2DWindow;
import UI.Events.ChangeAttributeEvent;
import UI.MainWindow;
import UI.UIConfig;
import UI.UiUtil;
import UI.Widgets.CutBox;
import UI.Widgets.PersoPoint;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DrawCutBorder extends DrawCutWrapper {


    public DrawCutBorder(CutType type, Rendering2DWindow renderer, MainWindow mainWindow) {
        super(type, renderer, mainWindow);
    }

    public DrawCutBorder(CutDTO cut, Rendering2DWindow renderer, MainWindow mainWindow) {
        super(cut, renderer, mainWindow);
    }

//    @Override
//    public void draw(Graphics2D graphics2D, Rendering2DWindow renderer) {
//        this.update(renderer);
//        graphics2D.setStroke(stroke);
//        graphics2D.setColor(this.strokeColor);
//
//        for(int i =0; i  < points.size() - 1; i++){
//            this.points.get(i).drawLineMM(graphics2D, renderer, this.points.get(i+1));
//        }
//
//    }


    @Override
    public void drawAnchor(Graphics2D graphics2D, Rendering2DWindow renderer) {
        // No need to draw anchors
    }

    @Override
    public void drawWhileChanging(Graphics2D graphics2D, Rendering2DWindow renderer, PersoPoint cursor) {

    }

    @Override
    public void drawDimensions(Graphics2D graphics2D, Rendering2DWindow rendering2DWindow) {
        List<VertexDTO> absPoints = mainWindow.getController().getAbsolutePointsPosition(cut);
        VertexDTO downLeft = cut.getPoints().get(0);
        VertexDTO upRight = cut.getPoints().get(1);
        double bitDiameter = mainWindow.getController().getBitDiameter(cut.getBitIndex());

        VertexDTO centralPoint = absPoints.get(0).add(absPoints.get(2)).mul(0.5);

        VertexDTO yP1 = new VertexDTO(centralPoint).sub(downLeft);
        VertexDTO yP2 = new VertexDTO(yP1.getX(), yP1.getY() + downLeft.getY() + upRight.getY(), 0);

        VertexDTO xP1 = new VertexDTO(centralPoint).sub(downLeft);
        VertexDTO xP2 = new VertexDTO(xP1.getX() + downLeft.getX() + upRight.getX(), xP1.getY(), 0);

        DimensionDTO widthUnit = mainWindow.getController().convertUnit(new DimensionDTO((downLeft.getX() + upRight.getX()), Units.MM), UIConfig.INSTANCE.getDefaultUnit().getUnit());
        DimensionDTO heightUnit = mainWindow.getController().convertUnit(new DimensionDTO((downLeft.getY() + upRight.getY()), Units.MM), UIConfig.INSTANCE.getDefaultUnit().getUnit());
        UiUtil.drawArrowWidthNumber(graphics2D, rendering2DWindow, xP1, xP2, widthUnit.value(),
                ARROW_COLOR, ARROW_DIMENSION, DIMENSION_COLOR);
        UiUtil.drawArrowWidthNumber(graphics2D, rendering2DWindow, yP1, yP2, heightUnit.value(),
                ARROW_COLOR, ARROW_DIMENSION, DIMENSION_COLOR);
    }

    @Override
    public boolean addPoint(Drawing drawing, Rendering2DWindow renderer, PersoPoint pointInMM) {

        if (!refs.isEmpty()) {
            List<VertexDTO> newPoints = mainWindow.getController().generateBorderPointsRelativeEdgeEdgeFromAbsolute(cut.getBitIndex());
            this.cut = new CutDTO(this.cut.getId(), this.cut.getDepth(), this.cut.getBitIndex(), this.cut.getCutType(), newPoints, refs, this.cut.getState());
            return true;
        }
        return false;
    }


    @Override
    public boolean areRefsValid() {
        return !refs.isEmpty() && areRefsNotCircular();
    }

    @Override
    public Optional<UUID> end() {
        return createCut();
    }

    @Override
    public void cursorUpdate(Rendering2DWindow renderer, Drawing drawing) {
        PersoPoint p = this.cursorPoint;
        double threshold;
        if (mainWindow.getController().getGrid().isMagnetic()) {
            threshold = renderer.scalePixelToMM(mainWindow.getController().getGrid().getMagnetPrecision());
        } else {
            threshold = renderer.scalePixelToMM(snapThreshold);
        }
        p.movePoint(renderer.getMmMousePt().getX(), renderer.getMmMousePt().getY());

        VertexDTO p1 = new VertexDTO(p.getLocationX(), p.getLocationY(), 0.0f);
        Optional<VertexDTO> closestPoint = mainWindow.getController().getGridPointNearBorder(p1, threshold);
        Optional<VertexDTO> otherPoint = changeClosestPointIfMagnetic(threshold, closestPoint, true);
        if (otherPoint.isPresent() && (otherPoint.get().getX() == 0 || otherPoint.get().getX() == mainWindow.getController().getPanelDTO().getPanelDimension().getX() || otherPoint.get().getY() == 0 || otherPoint.get().getY() == mainWindow.getController().getPanelDTO().getPanelDimension().getY())) {
            closestPoint = otherPoint;
        }
        if (closestPoint.isPresent()) {
            p.movePoint(closestPoint.get().getX(), closestPoint.get().getY());
            VertexDTO snapPoint = new VertexDTO(closestPoint.get().getX(), closestPoint.get().getY(), 0.0f);
            refs = mainWindow.getController().getRefCutsAndBorderOnPoint(snapPoint);

            if (!refs.isEmpty()) {
                p.setColor(VALID_COLOR);
                p.setValid(PersoPoint.Valid.VALID);
            }
        } else {
            p.setColor(INVALID_COLOR);
            p.setValid(PersoPoint.Valid.NOT_VALID);
        }
    }

    @Override
    public void moveUpdate(Point2D pixP, Rendering2DWindow renderer, MainWindow mainWindow) {
        this.cursorPoint = new PersoPoint(pixP.getX(), pixP.getY(),1,true);
        double threshold;
        if (mainWindow.getController().getGrid().isMagnetic()) {
            threshold = renderer.scalePixelToMM(mainWindow.getController().getGrid().getMagnetPrecision());
        } else {
            threshold = renderer.scalePixelToMM(snapThreshold);
        }

        List<VertexDTO> listPoints = mainWindow.getController().getAbsolutePointsPosition(getCutDTO());
        Point2D mmE = renderer.pixelTomm(pixP);
        PersoPoint p = new PersoPoint(mmE.getX(), mmE.getY(),1,true);
        Optional<VertexDTO> closestPoint1 = mainWindow.getController().getGridPointNearBorder(new VertexDTO(p.getLocationX(),p.getLocationY(),0), threshold);
        VertexDTO closestPoint = closestPoint1.orElse(new VertexDTO(p.getLocationX(),p.getLocationY(),0));
        Optional<VertexDTO> otherPoint = changeClosestPointIfMagnetic(threshold, closestPoint1, true);
        if (otherPoint.isPresent() && (otherPoint.get().getX() == 0 || otherPoint.get().getX() == mainWindow.getController().getPanelDTO().getPanelDimension().getX() || otherPoint.get().getY() == 0 || otherPoint.get().getY() == mainWindow.getController().getPanelDTO().getPanelDimension().getY())) {
            closestPoint = otherPoint.get();
        }
        VertexDTO p1;

        if (mainWindow.getController().isRoundedCutDTOSegmentHoveredByCursor(cut, new VertexDTO(renderer.getMmMousePt().getX(), renderer.getMmMousePt().getY(), 0), 0, 1)) {
            p1 = new VertexDTO(closestPoint.getX(), listPoints.get(0).getY(), 0);
        } else if (mainWindow.getController().isRoundedCutDTOSegmentHoveredByCursor(cut, new VertexDTO(renderer.getMmMousePt().getX(), renderer.getMmMousePt().getY(), 0), 1, 2)) {
            p1 = new VertexDTO(listPoints.get(1).getX(), closestPoint.getY(), 0);
        } else if (mainWindow.getController().isRoundedCutDTOSegmentHoveredByCursor(cut, new VertexDTO(renderer.getMmMousePt().getX(), renderer.getMmMousePt().getY(), 0), 2, 3)) {
            p1 = new VertexDTO(closestPoint.getX(), listPoints.get(2).getY(), 0);
        } else {
            p1 = new VertexDTO(listPoints.get(3).getX(), closestPoint.getY(), 0);
        }

        CutDTO c = getCutDTO();

        List<VertexDTO> relativePts = mainWindow.getController().generateBorderPointsRelativeEdgeEdgeFromAbsoluteSinglePoint(p1, getCutDTO().getBitIndex());
        mainWindow.getController().modifyCut(new CutDTO(c.getId(), c.getDepth(), c.getBitIndex(), c.getCutType(), relativePts, c.getRefsDTO(), c.getState()), false);

        Optional<CutBox> cutBox = mainWindow.getMiddleContent().getCutWindow().getCutListPanel().getCutBoxWithId(getCutDTO().getId());
        mainWindow.getMiddleContent().getCutWindow().modifiedAttributeEventOccured(new ChangeAttributeEvent(cutBox, cutBox.get()));
    }

    @Override
    public void movePoint(Point2D pixP, Rendering2DWindow renderer, MainWindow mainWindow, int indexPoint) {


        double threshold;
        if (mainWindow.getController().getGrid().isMagnetic()) {
            threshold = renderer.scalePixelToMM(mainWindow.getController().getGrid().getMagnetPrecision());
        } else {
            threshold = renderer.scalePixelToMM(snapThreshold);
        }

        //List<VertexDTO> listPoints = mainWindow.getController().getAbsolutePointsPosition(getCutDTO());
        Point2D mmE = renderer.pixelTomm(pixP);
        this.cursorPoint = new PersoPoint(mmE.getX(), mmE.getY(),1,true);

        PersoPoint p = new PersoPoint(mmE.getX(), mmE.getY(),1,true);
        Optional<VertexDTO> closestPoint1 = mainWindow.getController().getGridPointNearBorder(new VertexDTO(p.getLocationX(),p.getLocationY(),0), threshold);
        VertexDTO closestPoint = closestPoint1.orElse(new VertexDTO(p.getLocationX(),p.getLocationY(),0));
        Optional<VertexDTO> otherPoint = changeClosestPointIfMagnetic(threshold, closestPoint1, true);
        if (otherPoint.isPresent()) {
            closestPoint = otherPoint.get();
        }


        CutDTO c = getCutDTO();

        List<VertexDTO> relativePts = mainWindow.getController().generateBorderPointsRelativeEdgeEdgeFromAbsoluteSinglePoint(closestPoint, getCutDTO().getBitIndex());
        mainWindow.getController().modifyCut(new CutDTO(c.getId(), c.getDepth(), c.getBitIndex(), c.getCutType(), relativePts, c.getRefsDTO(), c.getState()), false);

        Optional<CutBox> cutBox = mainWindow.getMiddleContent().getCutWindow().getCutListPanel().getCutBoxWithId(getCutDTO().getId());
        mainWindow.getMiddleContent().getCutWindow().modifiedAttributeEventOccured(new ChangeAttributeEvent(cutBox, cutBox.get()));
    }

}
