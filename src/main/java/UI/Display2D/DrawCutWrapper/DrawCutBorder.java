package UI.Display2D.DrawCutWrapper;

import Common.DTO.CutDTO;
import Common.DTO.DimensionDTO;
import Common.DTO.VertexDTO;
import Common.Units;
import Domain.CutType;
import UI.Display2D.Drawing;
import UI.Display2D.Rendering2DWindow;
import UI.MainWindow;
import UI.UIConfig;
import UI.UiUtil;
import UI.Widgets.PersoPoint;

import java.awt.*;

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

    @Override
    public void draw(Graphics2D graphics2D, Rendering2DWindow renderer) {
        this.update(renderer);
        graphics2D.setStroke(stroke);
        graphics2D.setColor(this.strokeColor);

        for(int i =0; i  < points.size() - 1; i++){
            this.points.get(i).drawLineMM(graphics2D, renderer, this.points.get(i+1));

        }

    }

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
        VertexDTO xP2 = new VertexDTO(xP1.getX() + downLeft.getX() + upRight.getX(),  xP1.getY(), 0);

        DimensionDTO widthUnit = mainWindow.getController().convertUnit(new DimensionDTO((downLeft.getX() + upRight.getX()), Units.MM), UIConfig.INSTANCE.getDefaultUnit().getUnit());
        DimensionDTO heightUnit = mainWindow.getController().convertUnit(new DimensionDTO((downLeft.getY()+ upRight.getY()) , Units.MM), UIConfig.INSTANCE.getDefaultUnit().getUnit());
        UiUtil.drawArrowWidthNumber(graphics2D, rendering2DWindow,  xP1, xP2, widthUnit.value(),
                ARROW_COLOR,ARROW_DIMENSION, DIMENSION_COLOR);
        UiUtil.drawArrowWidthNumber(graphics2D, rendering2DWindow,  yP1, yP2, heightUnit.value(),
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
}
