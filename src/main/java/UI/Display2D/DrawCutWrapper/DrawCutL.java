package UI.Display2D.DrawCutWrapper;

import Common.DTO.CutDTO;
import Common.DTO.RefCutDTO;
import Common.DTO.RequestCutDTO;
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

            PersoPoint p1 = new PersoPoint(points.getFirst());
            PersoPoint p2 = new PersoPoint(points.getFirst());

            p1.movePoint(cursor.getLocationX(), this.points.getFirst().getLocationY());
            p2.movePoint(this.points.getFirst().getLocationX(), cursor.getLocationY());
            p1.drawLineMM(graphics2D, renderer, cursor, this.strokeWidth);
            p2.drawLineMM(graphics2D, renderer, cursor, this.strokeWidth);

            p1.drawMM(graphics2D, renderer); // drawing the points
            p2.drawMM(graphics2D, renderer); //drawing the points
        }
    }

    @Override
    public boolean addPoint(Rendering2DWindow renderer, PersoPoint pointInMM) {
        List<VertexDTO> newPoints = this.cut.getPoints();

        if(newPoints.isEmpty()){ // premier point a ajouter
            VertexDTO newPoint = new VertexDTO(pointInMM.getLocationX(),pointInMM.getLocationY(),  this.cut.getDepth());
            refs = mainWindow.getController().getRefCutsAndBorderOnPoint(newPoint);

            if(refs.size() >= 2){
                newPoints.add(newPoint);
            }
            else{
                System.out.println("TOO ENOUGH REFS IN THE L CUT");
                System.out.println("REF SIZE " + refs.size());
            }
        }
        else{
            // Dans le cas contraire, c'est le dernier point, donc ajoute les 4 points finauxx:
            //  3 - 2*
            //  |   |
            //  4 - 1
            newPoints.add(new VertexDTO(pointInMM.getLocationX(),newPoints.getFirst().getY(),  this.cut.getDepth()));
            newPoints.add(new VertexDTO(newPoints.getFirst().getX(),pointInMM.getLocationY(),  this.cut.getDepth()));
            newPoints.add(2, new VertexDTO(pointInMM.getLocationX(),pointInMM.getLocationY(),  this.cut.getDepth()));
            newPoints.removeFirst();
        }

        this.cut = new CutDTO(this.cut.getId(), this.cut.getDepth(), this.cut.getBitIndex(), this.cut.getCutType(), newPoints);

        return this.cut.getPoints().size() >= 3;
    }


    @Override
    public Optional<UUID> end() {
        return createCut();
    }

    @Override
    public void cursorUpdate(Rendering2DWindow renderer, Drawing drawing){
        PersoPoint p = this.cursorPoint;

        if(this.points.isEmpty()){ // First point
            p.movePoint(renderer.getMmMousePt().getX(), renderer.getMmMousePt().getY());

            double threshold = 10;
            threshold = renderer.scaleMMToPixel(threshold);
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
        else{ // Rest of the rectangle points
            p.movePoint(renderer.getMmMousePt().getX(), renderer.getMmMousePt().getY());

            // Restraining the L cut
            // There is exactly 2 refs
            // The refs are utilised to prevent overflowing L cuts
            double minX = Double.MAX_VALUE;
            double minY = Double.MAX_VALUE;
            double maxX = Double.MIN_VALUE;
            double maxY = Double.MIN_VALUE;

            for (RefCutDTO ref : refs) {
                VertexDTO p1l1 = ref.getFirstPoint();
                VertexDTO p2l1 = ref.getSecondPoint();

                minX = Math.min(p1l1.getX(), minX);
                minX = Math.min(minX, p2l1.getX());

                maxX = Math.max(maxX, p2l1.getX());
                maxX = Math.max(p1l1.getX(), maxX);

                minY = Math.min(minY, p2l1.getY());
                minY = Math.min(p1l1.getY(), minY);

                maxY = Math.max(maxY, p2l1.getY());
                maxY = Math.max(p1l1.getY(), maxY);
            }

            if(p.getLocationX() <= minX){
                p.movePoint(minX, p.getLocationY());
            }
            else if(p.getLocationX() >= maxX){
                p.movePoint(maxX, p.getLocationY());
            }

            if(p.getLocationY() <= minY){
                p.movePoint(p.getLocationX(), minY);
            }
            else if(p.getLocationY() >= maxY){
                p.movePoint(p.getLocationX(), maxY);
            }

            // For the snap area
            double threshold = 10;
            threshold = renderer.scaleMMToPixel(threshold);

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
