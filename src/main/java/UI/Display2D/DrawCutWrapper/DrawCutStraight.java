package UI.Display2D.DrawCutWrapper;

import Common.DTO.CutDTO;
import Common.DTO.VertexDTO;
import Domain.CutType;
import UI.Display2D.Drawing;
import UI.Display2D.Rendering2DWindow;
import UI.MainWindow;
import UI.UiUtil;
import UI.Widgets.PersoPoint;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Vertical/Horizontal line drawing that inherits from DrawCutWrapper
 * @author Louis-Etienne Messier
 */
public class DrawCutStraight extends DrawCutWrapper{
    public DrawCutStraight(CutType type, Rendering2DWindow renderer, MainWindow mainWindow) {
        super(type, renderer, mainWindow);
    }

    public DrawCutStraight(CutDTO cut, Rendering2DWindow renderer, MainWindow mainWindow) {
        super(cut, renderer, mainWindow);
    }

    @Override
    public void drawWhileChanging(Graphics2D graphics2D, Rendering2DWindow renderer, PersoPoint cursor) {
        graphics2D.setStroke(stroke);
        graphics2D.setColor(this.strokeColor);

        this.points = new ArrayList<>();
        for(VertexDTO p : temporaryCreationPoints){
            points.add(new PersoPoint(p.getX(), p.getY(), 10.0f, true, strokeColor));
        }

        for (int i =0; i < points.size()-1; i++){
            points.get(i).drawLineMM(graphics2D, renderer, points.get(i+1), false);
        }

        graphics2D.setColor(cursor.getColor());
        if (!points.isEmpty()){
            points.getLast().drawLineMM(graphics2D, renderer, cursor, false);
        }

        for (PersoPoint point : this.points){ // drawing the points
            point.drawMM(graphics2D, renderer, false);
        }

        if (!refs.isEmpty()){ // drawing the first anchor point
            VertexDTO offset = refs.getFirst().getAbsoluteOffset(mainWindow.getController());
            PersoPoint referenceAnchorPoint = new PersoPoint(offset.getX(), offset.getY(), cursorRadius, true);
            referenceAnchorPoint.setColor(ANCHOR_COLOR);
            referenceAnchorPoint.drawMM(graphics2D, renderer, false);
        }

    }

    @Override
    public void drawDimensions(Graphics2D graphics2D, Rendering2DWindow rendering2DWindow) {
        VertexDTO anchorAbsolutePosition = cut.getRefsDTO().getFirst().getAbsoluteOffset(mainWindow.getController());
        double anchorDiameter = mainWindow.getController().getBitDiameter(cut.getRefsDTO().getFirst().getCut().getBitIndex());
        if(cut.getCutType() == CutType.LINE_HORIZONTAL){
            double verticalOffset = Math.abs(cut.getPoints().getFirst().getY());
            double absoluteY = mainWindow.getController().getAbsolutePointsPosition(cut).getFirst().getY();
            VertexDTO anchorToCut = new VertexDTO(anchorAbsolutePosition.getX(), absoluteY, 0);
            VertexDTO diffNormalized = anchorToCut.sub(anchorAbsolutePosition).normalize();
            VertexDTO p1 = anchorAbsolutePosition.add(diffNormalized.mul(anchorDiameter/2));
            VertexDTO p2 = p1.add(diffNormalized.mul(verticalOffset));

            if(verticalOffset == 0){ // when the anchor and the cut is colinear
                p1 = anchorAbsolutePosition;
                p2 = anchorAbsolutePosition;
            }
            UiUtil.drawArrowWidthNumber(graphics2D, rendering2DWindow, p1, p2, verticalOffset, ARROW_COLOR, ARROW_DIMENSION, DIMENSION_COLOR);
        }
        else if(cut.getCutType() == CutType.LINE_VERTICAL){
            double horizontalOffset = Math.abs(cut.getPoints().getFirst().getX());
            double absoluteX = mainWindow.getController().getAbsolutePointsPosition(cut).getFirst().getX();
            VertexDTO anchorToCut = new VertexDTO(absoluteX, anchorAbsolutePosition.getY(), 0);
            VertexDTO diffNormalized = anchorToCut.sub(anchorAbsolutePosition).normalize();
            VertexDTO p1 = anchorAbsolutePosition.add(diffNormalized.mul(anchorDiameter/2));
            VertexDTO p2 = p1.add(diffNormalized.mul(horizontalOffset));

            if(horizontalOffset == 0){// when the anchor and the cut is colinear
                p1 = anchorAbsolutePosition;
                p2 = anchorAbsolutePosition;
            }
            UiUtil.drawArrowWidthNumber(graphics2D, rendering2DWindow, p1, p2, horizontalOffset, ARROW_COLOR, ARROW_DIMENSION, DIMENSION_COLOR);
        }
        else if(cut.getCutType() == CutType.LINE_FREE){
            VertexDTO anchor = cut.getRefsDTO().getFirst().getAbsoluteOffset(mainWindow.getController());
            VertexDTO relativeP1 = cut.getPoints().getFirst();

            VertexDTO p2 = anchor.add(relativeP1);

            UiUtil.drawArrowWidthNumberXY(graphics2D, rendering2DWindow, anchor, p2, relativeP1.getX(), relativeP1.getY(), ARROW_COLOR, ARROW_DIMENSION, DIMENSION_COLOR);
        }

    }

    @Override
    public boolean addPoint(Drawing drawing, Rendering2DWindow renderer, PersoPoint pointInMM) {
        if (refs.isEmpty()){
            VertexDTO p1 = new VertexDTO(pointInMM.getLocationX(), pointInMM.getLocationY(), 0.0f);
            refs = mainWindow.getController().getRefCutsAndBorderOnPoint(p1);
        }
        else{
            VertexDTO newPoint = new VertexDTO(pointInMM.getLocationX(),pointInMM.getLocationY(),  0.0f);
            temporaryCreationPoints.add(newPoint);

        }
        return temporaryCreationPoints.size() >= 2; // returns true if all the points are added
    }

    @Override
    public boolean areRefsValid() {
        return !refs.isEmpty() && areRefsNotCircular();
    }

    @Override
    public Optional<UUID> end() {
        List<VertexDTO> relativeEdgeEdgePoints = new ArrayList<>();
        if(cut.getCutType() == CutType.LINE_VERTICAL){
            relativeEdgeEdgePoints = mainWindow.getController().generateVerticalPointsRelativeEdgeEdgeFromAbsolute(temporaryCreationPoints.getFirst(), temporaryCreationPoints.get(1), cut.getBitIndex(), refs);
        }
        else if(cut.getCutType() == CutType.LINE_HORIZONTAL){
            relativeEdgeEdgePoints = mainWindow.getController().generateHorizontalPointsRelativeEdgeEdgeFromAbsolute(temporaryCreationPoints.getFirst(), temporaryCreationPoints.get(1), cut.getBitIndex(), refs);
        }
        else if(cut.getCutType() == CutType.LINE_FREE){
            relativeEdgeEdgePoints = mainWindow.getController().generateFreeCutPointsRelativeEdgeEdgeFromAbsolute(temporaryCreationPoints.getFirst(), temporaryCreationPoints.get(1), cut.getBitIndex(), refs);
        }
        this.cut = new CutDTO(this.cut.getId(), this.cut.getDepth(), this.cut.getBitIndex(), this.cut.getCutType(), relativeEdgeEdgePoints , refs, this.cut.getState());
        return createCut();
    }


    @Override
    public void cursorUpdate(Rendering2DWindow renderer, Drawing drawing) {
        PersoPoint p = this.cursorPoint;

        if(refs.isEmpty()){ // Get the reference point
            p.movePoint(renderer.getMmMousePt().getX(), renderer.getMmMousePt().getY());

            double threshold = renderer.scalePixelToMM(snapThreshold);
            VertexDTO p1 = new VertexDTO(p.getLocationX(), p.getLocationY(), 0.0f);

            Optional<VertexDTO> closestPoint = mainWindow.getController().getGridPointNearAllBorderAndCuts(p1, threshold);

            if(closestPoint.isPresent()){
                p.movePoint(closestPoint.get().getX(),closestPoint.get().getY());
                p.setColor(ANCHOR_COLOR);
                p.setValid(PersoPoint.Valid.VALID);
            }
            else{
                p.setColor(INVALID_COLOR);
                p.setValid(PersoPoint.Valid.NOT_VALID);
            }
        }
        else if(this.points.isEmpty()){ // First point
            p.movePoint(renderer.getMmMousePt().getX(), renderer.getMmMousePt().getY());

            double threshold = renderer.scalePixelToMM(snapThreshold);
            VertexDTO p1 = new VertexDTO(p.getLocationX(), p.getLocationY(), 0.0f);

            Optional<VertexDTO> closestPoint = mainWindow.getController().getGridPointNearAllBorderAndCuts(p1, threshold);

            if(closestPoint.isPresent()){

                p.movePoint(closestPoint.get().getX(),closestPoint.get().getY());
                p.setColor(VALID_COLOR);
                p.setValid(PersoPoint.Valid.VALID);
            }
            else{
                p.setColor(INVALID_COLOR);
                p.setValid(PersoPoint.Valid.NOT_VALID);
            }
        }
        else{ // Second point

            if(this.cut.getCutType() == CutType.LINE_VERTICAL){
                double firstPointX = this.points.getFirst().getLocationX();
                p.movePoint(firstPointX, renderer.getMmMousePt().getY()); // lock to Y axis
            }
            else if(this.cut.getCutType() == CutType.LINE_HORIZONTAL){
                double firstPointY = this.points.getFirst().getLocationY();
                p.movePoint(renderer.getMmMousePt().getX(), firstPointY); // lock to X axis
            }
            else{
                p.movePoint(renderer.getMmMousePt().getX(), renderer.getMmMousePt().getY()); // Free Cut
            }


            // Get possible snap points
            double threshold = renderer.scalePixelToMM(snapThreshold);
            VertexDTO cursor = new VertexDTO(p.getLocationX(), p.getLocationY(), 0.0f);
            VertexDTO p1 = new VertexDTO(points.getFirst().getLocationX(), points.getFirst().getLocationY(), 0.0f);
            Optional<VertexDTO> closestPoint = mainWindow.getController().getGridLineNearAllBorderAndCuts(p1,
                    cursor,threshold
            );

            // Snap
            if(closestPoint.isPresent()){
                p.movePoint(closestPoint.get().getX(), closestPoint.get().getY());
                p.setColor(SNAP_COLOR);
                p.setValid(PersoPoint.Valid.VALID);
                return;
            }
            // Test if on board
            VertexDTO pointDTO = new VertexDTO(p.getLocationX(), p.getLocationY(), 0.0f);
            if(mainWindow.getController().isPointOnPanel(pointDTO)){
                // Inside of the board
                p.setColor(VALID_COLOR);
                p.setValid(PersoPoint.Valid.VALID);
            }
            else{
                // Outside of the board
                p.setColor(INVALID_COLOR);
                p.setValid(PersoPoint.Valid.NOT_VALID);
            }
        }
    }
}
