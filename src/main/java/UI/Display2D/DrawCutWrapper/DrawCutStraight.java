package UI.Display2D.DrawCutWrapper;

import Common.DTO.CutDTO;
import Common.DTO.VertexDTO;
import Domain.CutType;
import UI.Display2D.Drawing;
import UI.Display2D.Rendering2DWindow;
import UI.MainWindow;
import UI.Widgets.PersoPoint;

import java.awt.*;
import java.sql.Time;
import java.time.Instant;
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
        this.update(renderer);
        graphics2D.setStroke(stroke);
        graphics2D.setColor(this.strokeColor);

        for (int i =0; i < points.size()-1; i++){
            points.get(i).drawLineMM(graphics2D, renderer, points.get(i+1), this.strokeWidth);
        }

        graphics2D.setColor(cursor.getColor());
        if (!points.isEmpty()){
            points.getLast().drawLineMM(graphics2D, renderer, cursor, this.strokeWidth);
        }

        for (PersoPoint point : this.points){ // drawing the points
            point.drawMM(graphics2D, renderer);
        }

        if (!refs.isEmpty()){ // drawing the first anchor point
            VertexDTO offset = refs.getFirst().getAbsoluteOffset();
            PersoPoint referenceAnchorPoint = new PersoPoint(offset.getX(), offset.getY(), cursorRadius, true);
            referenceAnchorPoint.setColor(Color.BLACK);
            referenceAnchorPoint.drawMM(graphics2D, renderer);
        }

    }

    @Override
    public boolean addPoint(Drawing drawing, Rendering2DWindow renderer, PersoPoint pointInMM) {

        if (refs.isEmpty()){
            VertexDTO p1 = new VertexDTO(pointInMM.getLocationX(), pointInMM.getLocationY(), 0.0f);
            refs = mainWindow.getController().getRefCutsAndBorderOnPoint(p1);
            drawing.changeRefWrapperById(refs.getFirst().getCut().getId());
        }
        else{
            List<VertexDTO> newPoints = this.cut.getPoints();
            VertexDTO newPoint = new VertexDTO(pointInMM.getLocationX(),pointInMM.getLocationY(),  this.cut.getDepth());
            VertexDTO offset = refs.getFirst().getAbsoluteOffset();
            newPoint = newPoint.sub(offset);
            newPoints.add(newPoint);
            this.cut = new CutDTO(this.cut.getId(), this.cut.getDepth(), this.cut.getBitIndex(), this.cut.getCutType(), newPoints, refs);
        }

        return this.cut.getPoints().size() >= 2; // returns true if all the points are added
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

            double threshold = renderer.scaleMMToPixel(snapThreshold);
            VertexDTO p1 = new VertexDTO(p.getLocationX(), p.getLocationY(), 0.0f);

            Optional<VertexDTO> closestPoint = mainWindow.getController().getGridPointNearAllBorderAndCuts(p1, threshold);

            if(closestPoint.isPresent()){
                p.movePoint(closestPoint.get().getX(),closestPoint.get().getY());
                p.setColor(Color.GREEN);
                p.setValid(PersoPoint.Valid.VALID);
            }
            else{
                p.setColor(Color.RED);
                p.setValid(PersoPoint.Valid.NOT_VALID);
            }
        }
        else if(this.points.isEmpty()){ // First point
            p.movePoint(renderer.getMmMousePt().getX(), renderer.getMmMousePt().getY());

            double threshold = renderer.scaleMMToPixel(snapThreshold);
            VertexDTO p1 = new VertexDTO(p.getLocationX(), p.getLocationY(), 0.0f);

            Optional<VertexDTO> closestPoint = mainWindow.getController().getGridPointNearAllBorderAndCuts(p1, threshold);

            if(closestPoint.isPresent()){

                p.movePoint(closestPoint.get().getX(),closestPoint.get().getY());
                p.setColor(Color.GREEN);
                p.setValid(PersoPoint.Valid.VALID);
            }
            else{

                p.setColor(Color.RED);
                p.setValid(PersoPoint.Valid.NOT_VALID);

                if(!refs.isEmpty()){
                    drawing.changeGoBackWrapperById(refs.getFirst().getCut().getId());
                }
            }
        }
        else{ // Second point

            if(this.cut.getCutType() == CutType.LINE_VERTICAL){
                double firstPointX = this.points.getFirst().getLocationX();
                p.movePoint(firstPointX, renderer.getMmMousePt().getY()); // lock to Y axis
            }
            else{
                double firstPointY = this.points.getFirst().getLocationY();
                p.movePoint(renderer.getMmMousePt().getX(), firstPointY); // lock to X axis
            }


            // Get possible snap points
            double threshold = renderer.scaleMMToPixel(snapThreshold);
            VertexDTO cursor = new VertexDTO(p.getLocationX(), p.getLocationY(), 0.0f);
            VertexDTO p1 = new VertexDTO(points.getFirst().getLocationX(), points.getFirst().getLocationY(), 0.0f);
            Optional<VertexDTO> closestPoint = mainWindow.getController().getGridLineNearAllBorderAndCuts(p1,
                    cursor,threshold
            );

            // Snap
            if(closestPoint.isPresent()){
                p.movePoint(closestPoint.get().getX(), closestPoint.get().getY());
                p.setColor(Color.YELLOW);
                p.setValid(PersoPoint.Valid.VALID);
                return;
            }
            // Test if on board
            VertexDTO pointDTO = new VertexDTO(p.getLocationX(), p.getLocationY(), 0.0f);
            if(mainWindow.getController().isPointOnPanel(pointDTO)){
                // Inside of the board
                p.setColor(Color.GREEN);
                p.setValid(PersoPoint.Valid.VALID);
            }
            else{
                // Outside of the board
                p.setColor(Color.RED);
                p.setValid(PersoPoint.Valid.NOT_VALID);
            }
        }
    }
}
