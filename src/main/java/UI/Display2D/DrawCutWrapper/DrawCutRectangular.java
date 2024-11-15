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
    public void draw(Graphics2D graphics2D, Rendering2DWindow renderer) {
        this.update(renderer);
        graphics2D.setStroke(stroke);
        graphics2D.setColor(this.strokeColor);

        for(int i =0; i  < points.size() - 1; i++){
            this.points.get(i).drawLineMM(graphics2D, renderer, this.points.get(i+1), this.strokeWidth);
        }
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

    }

    @Override
    public boolean addPoint(Rendering2DWindow renderer, PersoPoint pointInMM) {
        List<VertexDTO> newPoints = this.cut.getPoints();

        if(newPoints.isEmpty()){ // premier point a ajouter
            newPoints.add(new VertexDTO(pointInMM.getLocationX(),pointInMM.getLocationY(),  this.cut.getDepth()));
            // Ajoute les deux autres points d'ancrage de la coupe rectangulaire

        }
        else{
            // Dans le cas contraire, c'est le dernier point, donc ajoute les 4 points finauxx:
            //  3 - 2*
            //  |   |
            //  4 - 1
            newPoints.add(new VertexDTO(pointInMM.getLocationX(),newPoints.getFirst().getY(),  this.cut.getDepth()));
            newPoints.add(new VertexDTO(newPoints.getFirst().getX(),pointInMM.getLocationY(),  this.cut.getDepth()));
            newPoints.add(2, new VertexDTO(pointInMM.getLocationX(),pointInMM.getLocationY(),  this.cut.getDepth()));
            newPoints.add(new VertexDTO(newPoints.getFirst().getX(), newPoints.getFirst().getY(),  this.cut.getDepth()));
        }

        this.cut = new CutDTO(this.cut.getId(), this.cut.getDepth(), this.cut.getBitIndex(), this.cut.getCutType(), newPoints);

        return this.cut.getPoints().size() >= 5;
    }


    /**
     * Create the cut
     * @return {@code Optional<UUID>} UUID if the cut is valid, null if the cut is invalid
     */
    private Optional<UUID> createCut() {
        RequestCutDTO rq = new RequestCutDTO(this.cut.getPoints(), this.cut.getCutType(), this.cut.getBitIndex(), this.cut.getDepth());
        return mainWindow.getController().requestCut(rq);
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
