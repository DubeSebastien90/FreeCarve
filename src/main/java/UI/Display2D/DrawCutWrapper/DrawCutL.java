package UI.Display2D.DrawCutWrapper;

import Common.DTO.CutDTO;
import Common.DTO.VertexDTO;
import Domain.CutType;
import UI.Display2D.Drawing;
import UI.Display2D.Rendering2DWindow;
import UI.MainWindow;
import UI.Widgets.PersoPoint;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * L line drawing that inherits from DrawCutWrapper
 * @author Louis-Etienne Messier
 */
public class DrawCutL extends DrawCutWrapper{

    public DrawCutL(CutType type, Rendering2DWindow renderer, MainWindow mainWindow) {
        super(type, renderer, mainWindow);
    }

    public DrawCutL(CutDTO cut, Rendering2DWindow renderer, MainWindow mainWindow) {
        super(cut, renderer, mainWindow);
    }

    @Override
    public void drawWhileChanging(Graphics2D graphics2D, Rendering2DWindow renderer, PersoPoint cursor) {

        this.update(renderer);

        graphics2D.setStroke(stroke);
        graphics2D.setColor(cursor.getColor());

        if(!this.points.isEmpty()){
            this.cut = new CutDTO(this.cut.getId(), this.cut.getDepth(), this.cut.getBitIndex(), this.cut.getCutType(), compute3Points(cursor), refs, this.cut.getState());
        }

        for (int i =0; i < points.size()-1; i++){
            points.get(i).drawLineMM(graphics2D, renderer, points.get(i+1), this.strokeWidth);
        }

        for (PersoPoint point : this.points){ // drawing the points
            point.drawMM(graphics2D, renderer);
        }

        if (!refs.isEmpty() && !points.isEmpty()){ // drawing the first anchor point
            VertexDTO offset = refs.getFirst().getAbsoluteOffset(mainWindow.getController());
            PersoPoint referenceAnchorPoint = new PersoPoint(offset.getX(), offset.getY(), cursorRadius, true);
            referenceAnchorPoint.setColor(ANCHOR_COLOR);
            referenceAnchorPoint.drawMM(graphics2D, renderer);
        }

    }

    @Override
    public boolean addPoint(Drawing drawing, Rendering2DWindow renderer, PersoPoint pointInMM) {

        if(points.isEmpty()){
            compute3Points(pointInMM);
            this.cut = new CutDTO(this.cut.getId(), this.cut.getDepth(), this.cut.getBitIndex(), this.cut.getCutType(), compute3Points(pointInMM), refs, this.cut.getState());
            return false;
        }
        else if(refs.size() >= 2){
            compute3Points(pointInMM);
            this.cut = new CutDTO(this.cut.getId(), this.cut.getDepth(), this.cut.getBitIndex(), this.cut.getCutType(), compute3Points(pointInMM), refs, this.cut.getState());
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public boolean areRefsValid() {
        return refs.size() >= 2 && !areRefsPointinToItself();
    }

    private List<VertexDTO> compute3Points(PersoPoint cursor){
        List<VertexDTO> newPoints = new ArrayList<>();
        VertexDTO offset = refs.getFirst().getAbsoluteOffset(mainWindow.getController());
        VertexDTO p1 = new VertexDTO(cursor.getLocationX(), cursor.getLocationY(), 0.0f);
        p1 = p1.sub(offset);

        VertexDTO newPoint = new VertexDTO(p1.getX(), p1.getY(),  this.cut.getDepth());
        newPoints.add(newPoint);
        newPoints.add(newPoint);
        newPoints.add(newPoint); // add 3 to prevent amount of points error
        return newPoints;
    }

    @Override
    public Optional<UUID> end() {
        return createCut();
    }

    @Override
    public void cursorUpdate(Rendering2DWindow renderer, Drawing drawing){
        PersoPoint p = this.cursorPoint;

        if(points.isEmpty()){ // First point
            p.movePoint(renderer.getMmMousePt().getX(), renderer.getMmMousePt().getY());

            double threshold = renderer.scalePixelToMM(snapThreshold);
            VertexDTO p1 = new VertexDTO(p.getLocationX(), p.getLocationY(), 0.0f);
            Optional<VertexDTO> closestPoint = mainWindow.getController().getPointNearIntersections(p1, threshold);

            if(closestPoint.isPresent()){
                p.movePoint(closestPoint.get().getX(),closestPoint.get().getY());
                p1 = new VertexDTO(p.getLocationX(), p.getLocationY(), 0.0f);
                refs = mainWindow.getController().getRefCutsAndBorderOnPoint(p1);

                if (refs.size() >= 2){
                    p.setColor(VALID_COLOR);
                    p.setValid(PersoPoint.Valid.VALID);
                }
                else{
                    p.setColor(INVALID_COLOR);
                    p.setValid(PersoPoint.Valid.NOT_VALID);
                }
            }
            else{
                p.setColor(INVALID_COLOR);
                p.setValid(PersoPoint.Valid.NOT_VALID);
            }
        }
        else{ // Second L cut point
            p.movePoint(renderer.getMmMousePt().getX(), renderer.getMmMousePt().getY());
            // For the snap area
            double threshold = renderer.scalePixelToMM(snapThreshold);

            // Get the possible closest point
            VertexDTO cursor = new VertexDTO(p.getLocationX(), p.getLocationY(), 0.0f);
            Optional<VertexDTO> closestPoint = mainWindow.getController().getGridPointNearAllBorderAndCuts(cursor, threshold);

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
