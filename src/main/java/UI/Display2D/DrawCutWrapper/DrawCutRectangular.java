package UI.Display2D.DrawCutWrapper;

import Common.DTO.CutDTO;
import Common.DTO.RequestCutDTO;
import Common.DTO.VertexDTO;
import Domain.CutType;
import UI.Display2D.Drawing;
import UI.Display2D.Rendering2DWindow;
import UI.MainWindow;
import UI.Widgets.PersoPoint;

import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Rectangular line drawing that inherits from DrawCutWrapper
 * @author Louis-Etienne Messier
 */
public class DrawCutRectangular extends DrawCutWrapper{
    public DrawCutRectangular(CutType type, Rendering2DWindow renderer, MainWindow mainWindow) {
        super(type, renderer, mainWindow);
    }

    public DrawCutRectangular(CutDTO cut, Rendering2DWindow renderer, MainWindow mainWindow) {
        super(cut, renderer, mainWindow);
    }

    @Override
    public void drawWhileChanging(Graphics2D graphics2D, Rendering2DWindow renderer, PersoPoint cursor) {
        this.update(renderer);
        graphics2D.setStroke(stroke);
        graphics2D.setColor(cursor.getColor());


        if(!this.points.isEmpty()){

            PersoPoint p1 = new PersoPoint(points.getFirst());
            PersoPoint p2 = new PersoPoint(points.getFirst());
            p1.movePoint(cursor.getLocationX(), this.points.getFirst().getLocationY());
            p2.movePoint(this.points.getFirst().getLocationX(), cursor.getLocationY());
            points.getFirst().drawLineMM(graphics2D, renderer, p1, this.strokeWidth);
            points.getFirst().drawLineMM(graphics2D, renderer, p2, this.strokeWidth);
            p1.drawLineMM(graphics2D, renderer, cursor, this.strokeWidth);
            p2.drawLineMM(graphics2D, renderer, cursor, this.strokeWidth);

            p1.drawMM(graphics2D, renderer); // drawing the points
            p2.drawMM(graphics2D, renderer); //drawing the points
        }

        for (PersoPoint point : this.points){ // drawing the points
            point.drawMM(graphics2D, renderer);
        }

        if (!refs.isEmpty()){ // drawing the first anchor point
            VertexDTO offset = refs.getFirst().getAbsoluteOffset(mainWindow.getController());
            PersoPoint referenceAnchorPoint = new PersoPoint(offset.getX(), offset.getY(), cursorRadius, true);
            referenceAnchorPoint.setColor(Color.BLACK);
            referenceAnchorPoint.drawMM(graphics2D, renderer);
        }

    }

    @Override
    public boolean addPoint(Drawing drawing, Rendering2DWindow renderer, PersoPoint pointInMM) {
        List<VertexDTO> newPoints = this.cut.getPoints();

        if (refs.isEmpty()){
            VertexDTO p1 = new VertexDTO(pointInMM.getLocationX(), pointInMM.getLocationY(), 0.0f);
            refs = mainWindow.getController().getRefCutsAndBorderOnPoint(p1);

            if(refs.size() >= 2){ // refs must be at least 2 for the rectangle, because it follows the intersection
                drawing.changeRefWrapperById(refs.getFirst().getCut().getId());
            }

        }
        else{
            if(newPoints.isEmpty()){ // premier point a ajouter
                VertexDTO newPoint = new VertexDTO(pointInMM.getLocationX(),pointInMM.getLocationY(),  this.cut.getDepth());
                VertexDTO offset = refs.getFirst().getAbsoluteOffset(mainWindow.getController());
                newPoint = newPoint.sub(offset);
                newPoints.add(newPoint);
            }
            else{
                // Dans le cas contraire, c'est le dernier point, donc ajoute les 4 points finaux:
                //  3 - 2*
                //  |   |
                //  4 - 1
                VertexDTO offset = refs.getFirst().getAbsoluteOffset(mainWindow.getController());
                VertexDTO p1 = newPoints.getFirst().add(offset);
                VertexDTO mouseDTO = new VertexDTO(pointInMM.getLocationX(), pointInMM.getLocationY(), p1.getZ());
                double width =  mouseDTO.sub(p1).getX();
                double height = mouseDTO.sub(p1).getY();

                newPoints = mainWindow.getController().generateRectanglePoints(p1, width, height);
                for(int i =0; i <newPoints.size(); i++){
                    newPoints.set(i, newPoints.get(i).sub(offset)); // Substraction du offset pour retourner en relativeSpace
                }
            }
        }

        this.cut = new CutDTO(this.cut.getId(), this.cut.getDepth(), this.cut.getBitIndex(), this.cut.getCutType(), newPoints, refs);
        return this.cut.getPoints().size() >= 5;
    }

    @Override
    public Optional<UUID> end() {
        return createCut();
    }

    @Override
    public void cursorUpdate(Rendering2DWindow renderer, Drawing drawing){
        PersoPoint p = this.cursorPoint;
        if(refs.isEmpty()){
            p.movePoint(renderer.getMmMousePt().getX(), renderer.getMmMousePt().getY());

            double threshold = renderer.scaleMMToPixel(snapThreshold);
            VertexDTO p1 = new VertexDTO(p.getLocationX(), p.getLocationY(), 0.0f);

            Optional<VertexDTO> closestPoint = mainWindow.getController().getPointNearIntersections(p1, threshold);

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
        else if(this.points.isEmpty()){ // First point after anchor
            p.movePoint(renderer.getMmMousePt().getX(), renderer.getMmMousePt().getY());

            double threshold = renderer.scaleMMToPixel(snapThreshold);
            VertexDTO p1 = new VertexDTO(p.getLocationX(), p.getLocationY(), 0.0f);
            Optional<VertexDTO> closestPoint = mainWindow.getController().getGridPointNearAllBorderAndCuts(p1, threshold);

            if(closestPoint.isPresent()){
                p.movePoint(closestPoint.get().getX(),closestPoint.get().getY());

                drawing.changeRefWrapperById(refs.getFirst().getCut().getId());

                p.setColor(Color.GREEN);
                p.setValid(PersoPoint.Valid.VALID);
            }
        }
        else{ // Rest of the rectangle points
            p.movePoint(renderer.getMmMousePt().getX(), renderer.getMmMousePt().getY());

            // For the snap area
            double threshold = renderer.scaleMMToPixel(snapThreshold);

            // Get the possible closest point
            VertexDTO cursor = new VertexDTO(p.getLocationX(), p.getLocationY(), 0.0f);
            Optional<VertexDTO> closestPoint = mainWindow.getController().getGridPointNearAllBorderAndCuts(cursor, threshold);

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
