package UI.Display2D.DrawCutWrapper;

import Common.DTO.CutDTO;
import Domain.CutType;
import Common.DTO.VertexDTO;
import UI.Display2D.Drawing;
import UI.MainWindow;
import UI.Display2D.Rendering2DWindow;
import UI.UiUtil;
import UI.Widgets.PersoPoint;

import java.awt.*;
import java.util.Optional;
import java.util.UUID;

/**
 * Simple straight line drawing class that inherits from DrawCutWrapper
 * @author Louis-Etienne Messier
 */
public class DrawFreeCut extends DrawCutWrapper {

    public DrawFreeCut(CutType type, Rendering2DWindow renderer, MainWindow mainWindow){
        super(type, renderer, mainWindow);
    }

    public DrawFreeCut(CutDTO cut, Rendering2DWindow renderer, MainWindow mainWindow){
        super(cut, renderer, mainWindow);
    }

    @Override
    public void drawWhileChanging(Graphics2D graphics2D, Rendering2DWindow renderer, PersoPoint cursor) {
        graphics2D.setStroke(stroke);
        graphics2D.setColor(this.strokeColor);

        for (int i =0; i < points.size()-1; i++){ // drawing the lines
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

        VertexDTO anchor = cut.getRefsDTO().getFirst().getAbsoluteOffset(mainWindow.getController());
        VertexDTO relativeP1 = cut.getPoints().getFirst();
        VertexDTO p2 = anchor.add(relativeP1);
        System.out.println(p2);
        UiUtil.drawArrowWidthNumberXY(graphics2D, rendering2DWindow, anchor, p2, relativeP1.getX(), relativeP1.getY(), ARROW_COLOR, ARROW_DIMENSION, DIMENSION_COLOR);
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
