package UI.Display2D.DrawCutWrapper;

import Common.DTO.CutDTO;
import Common.DTO.DimensionDTO;
import Common.DTO.RefCutDTO;
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
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * L line drawing that inherits from DrawCutWrapper
 *
 * @author Louis-Etienne Messier
 */
public class DrawCutL extends DrawCutWrapper {

    public DrawCutL(CutType type, Rendering2DWindow renderer, MainWindow mainWindow) {
        super(type, renderer, mainWindow);
    }

    @Override
    public void drawAnchor(Graphics2D graphics2D, Rendering2DWindow renderer) {
        VertexDTO p1 = cut.getRefsDTO().get(0).getAbsoluteFirstPoint(mainWindow.getController());
        VertexDTO p2 = cut.getRefsDTO().get(0).getAbsoluteSecondPoint(mainWindow.getController());
        VertexDTO p3 = cut.getRefsDTO().get(1).getAbsoluteFirstPoint(mainWindow.getController());
        VertexDTO p4 = cut.getRefsDTO().get(1).getAbsoluteSecondPoint(mainWindow.getController());

        Optional<VertexDTO> anchor = VertexDTO.isLineIntersectNoLimitation(p1, p2, p3, p4);


        if (anchor.isPresent()) {
            PersoPoint referenceAnchorPoint = new PersoPoint(anchor.get().getX(), anchor.get().getY(), cursorRadius, true);
            referenceAnchorPoint.setColor(strokeColor);
            referenceAnchorPoint.drawMM(graphics2D, renderer);
        }

    }

    public DrawCutL(CutDTO cut, Rendering2DWindow renderer, MainWindow mainWindow) {
        super(cut, renderer, mainWindow);
    }

    @Override
    public void drawWhileChanging(Graphics2D graphics2D, Rendering2DWindow renderer, PersoPoint cursor) {

        if(!refs.isEmpty()){
            VertexDTO p1 = new VertexDTO(cursor.getLocationX(), cursor.getLocationY(), 0.0f);
            List<VertexDTO> relativeEdgeEdgePoints = new ArrayList<>();
            relativeEdgeEdgePoints = mainWindow.getController().generateLPointsRelativeEdgeEdgeFromAbsolute(p1, cut.getBitIndex(), refs);
            this.cut = new CutDTO(this.cut.getId(), this.cut.getDepth(), this.cut.getBitIndex(), this.cut.getCutType(), relativeEdgeEdgePoints, refs, this.cut.getState());
        }

        this.update(renderer);

        graphics2D.setStroke(stroke);
        graphics2D.setColor(cursor.getColor());


        for (int i =0; i < points.size()-1; i++){
            points.get(i).drawLineMM(graphics2D, renderer, points.get(i+1));
        }

        for (PersoPoint point : this.points){ // drawing the points
            point.drawMM(graphics2D, renderer);

        }

        if (!refs.isEmpty() && !points.isEmpty()) { // drawing the first anchor point
            VertexDTO offset = refs.getFirst().getAbsoluteOffset(mainWindow.getController());
            PersoPoint referenceAnchorPoint = new PersoPoint(offset.getX(), offset.getY(), cursorRadius, true);
            referenceAnchorPoint.setColor(ANCHOR_COLOR);
            referenceAnchorPoint.drawMM(graphics2D, renderer);
        }

    }

    @Override
    public void drawDimensions(Graphics2D graphics2D, Rendering2DWindow rendering2DWindow) {
        List<VertexDTO> absPoints = mainWindow.getController().getAbsolutePointsPosition(cut);
        VertexDTO relativeOffset = cut.getPoints().getFirst();
        double bitDiameter = mainWindow.getController().getBitDiameter(cut.getBitIndex());

        int index2 = 1;
        VertexDTO dirOpposite= absPoints.get(index2);
        VertexDTO offset = new VertexDTO(Math.signum(relativeOffset.getX()), Math.signum(relativeOffset.getY()), 0).mul(-bitDiameter/2);

        VertexDTO p1Width = dirOpposite;
        VertexDTO p2Width = dirOpposite.sub(new VertexDTO(relativeOffset.getX(), 0, 0));
        VertexDTO p1Height = dirOpposite;
        VertexDTO p2Height = dirOpposite.sub(new VertexDTO(0, relativeOffset.getY(), 0));

        p1Width = p1Width.add(offset);
        p2Width = p2Width.add(offset);
        p1Height = p1Height.add(offset);
        p2Height = p2Height.add(offset);

        DimensionDTO widthUnit = mainWindow.getController().convertUnit(new DimensionDTO(Math.abs(relativeOffset.getX()), Units.MM), UIConfig.INSTANCE.getDefaultUnit().getUnit());
        DimensionDTO heightUnit = mainWindow.getController().convertUnit(new DimensionDTO(Math.abs(relativeOffset.getY()), Units.MM), UIConfig.INSTANCE.getDefaultUnit().getUnit());

        UiUtil.drawArrowWidthNumber(graphics2D, rendering2DWindow,  p1Width, p2Width, widthUnit.value() ,  ARROW_COLOR, ARROW_DIMENSION,  DIMENSION_COLOR);
        UiUtil.drawArrowWidthNumber(graphics2D, rendering2DWindow,  p1Height, p2Height, heightUnit.value(), ARROW_COLOR, ARROW_DIMENSION, DIMENSION_COLOR);
    }

    @Override
    public boolean addPoint(Drawing drawing, Rendering2DWindow renderer, PersoPoint pointInMM) {

        if(refs.isEmpty()){
            VertexDTO p1 = new VertexDTO(pointInMM.getLocationX(), pointInMM.getLocationY(), 0.0f);
            refs = mainWindow.getController().getRefCutsAndBorderOnPoint(p1);
            return false;
        }
        else if(refs.size() >= 2){
            temporaryCreationPoints.add(new VertexDTO(pointInMM.getLocationX(), pointInMM.getLocationY(), 0.0f));

            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean areRefsValid() {
        return refs.size() >= 2 && areRefsNotCircular();
    }

    @Override
    public Optional<UUID> end() {
        List<VertexDTO> relativeEdgeEdgePoints = new ArrayList<>();
        relativeEdgeEdgePoints = mainWindow.getController().generateLPointsRelativeEdgeEdgeFromAbsolute(temporaryCreationPoints.getFirst(), cut.getBitIndex(), refs);
        this.cut = new CutDTO(this.cut.getId(), this.cut.getDepth(), this.cut.getBitIndex(), this.cut.getCutType(), relativeEdgeEdgePoints , refs, this.cut.getState());
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
        if (points.isEmpty()) { // First point
            p.movePoint(renderer.getMmMousePt().getX(), renderer.getMmMousePt().getY());

            VertexDTO p1 = new VertexDTO(p.getLocationX(), p.getLocationY(), 0.0f);
            Optional<VertexDTO> closestPoint = mainWindow.getController().getPointNearIntersections(p1, threshold);
            closestPoint = changeClosestPointIfMagnetic(threshold, closestPoint, false);
            if (closestPoint.isPresent()) {
                p.movePoint(closestPoint.get().getX(), closestPoint.get().getY());
                p1 = new VertexDTO(p.getLocationX(), p.getLocationY(), 0.0f);
                List<RefCutDTO> testRefs = mainWindow.getController().getRefCutsAndBorderOnPoint(p1);
                if (testRefs.size() >= 2){
                    p.setColor(VALID_COLOR);
                    p.setValid(PersoPoint.Valid.VALID);
                } else {
                    p.setColor(INVALID_COLOR);
                    p.setValid(PersoPoint.Valid.NOT_VALID);
                }
            } else {
                p.setColor(INVALID_COLOR);
                p.setValid(PersoPoint.Valid.NOT_VALID);
            }
        } else { // Second L cut point
            p.movePoint(renderer.getMmMousePt().getX(), renderer.getMmMousePt().getY());
            // For the snap area

            // Get the possible closest point
            VertexDTO cursor = new VertexDTO(p.getLocationX(), p.getLocationY(), 0.0f);
            Optional<VertexDTO> closestPoint = mainWindow.getController().getGridPointNearAllBorderAndCuts(cursor, threshold);
            closestPoint = changeClosestPointIfMagnetic(threshold, closestPoint, true);
            // Snap
            if (closestPoint.isPresent()) {
                p.movePoint(closestPoint.get().getX(), closestPoint.get().getY());
                p.setColor(SNAP_COLOR);
                p.setValid(PersoPoint.Valid.VALID);
                return;
            }

            // Test if on board
            VertexDTO pointDTO = new VertexDTO(p.getLocationX(), p.getLocationY(), 0.0f);
            if (mainWindow.getController().isPointOnPanel(pointDTO)) {
                // Inside of the board
                p.setColor(VALID_COLOR);
                p.setValid(PersoPoint.Valid.VALID);
            } else {
                // Outside of the board
                p.setColor(INVALID_COLOR);
                p.setValid(PersoPoint.Valid.NOT_VALID);
            }
        }
    }

    @Override
    public void moveUpdate(Point2D pixP, Rendering2DWindow renderer, MainWindow mainWindow) {

    }

    @Override
    public void movePoint(Point2D pixP, Rendering2DWindow renderer, MainWindow mainWindow, int indexPoint) {
        List<VertexDTO> listPoints = mainWindow.getController().getAbsolutePointsPosition(getCutDTO());
        Point2D mmE = renderer.pixelTomm(pixP);
        VertexDTO p1;

        if (indexPoint == 1){
            p1 = new VertexDTO(mmE.getX(), mmE.getY(), 0);
        } else if (indexPoint == 0){
            p1 = new VertexDTO(listPoints.get(1).getX(), mmE.getY(), 0);
        } else{
            p1 = new VertexDTO(mmE.getX(), listPoints.get(1).getY(), 0);
        }
        CutDTO c = getCutDTO();

        List<VertexDTO> relativePts = mainWindow.getController().generateLPointsRelativeEdgeEdgeFromAbsolute(p1, getCutDTO().getBitIndex(), getCutDTO().getRefsDTO());
        mainWindow.getController().modifyCut(new CutDTO(c.getId(), c.getDepth(), c.getBitIndex(), c.getCutType(), relativePts, c.getRefsDTO(), c.getState()));

        Optional<CutBox> cutBox = mainWindow.getMiddleContent().getCutWindow().getCutListPanel().getCutBoxWithId(getCutDTO().getId());
        mainWindow.getMiddleContent().getCutWindow().modifiedAttributeEventOccured(new ChangeAttributeEvent(cutBox, cutBox.get()));
    }

}
