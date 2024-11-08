package UI.Display2D.DrawCutWrapper;

import Common.CutDTO;
import Domain.CutType;
import Common.RequestCutDTO;
import Common.VertexDTO;
import UI.Display2D.Drawing;
import UI.Display2D.Rendering2DWindow;
import UI.MainWindow;
import UI.Widgets.PersoPoint;

import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Simple vertical straight line drawing class that inherits from DrawCutWrapper
 * @author Louis-Etienne Messier
 */
public class DrawCutVertical extends DrawCutWrapper{

    public DrawCutVertical(CutType type, Rendering2DWindow renderer, MainWindow mainWindow) {
        super(type, renderer, mainWindow);
    }

    public DrawCutVertical(CutDTO cut, Rendering2DWindow renderer, MainWindow mainWindow) {
        super(cut, renderer, mainWindow);
    }

    @Override
    public void draw(Graphics2D graphics2D, Rendering2DWindow renderer) {
        this.update(renderer);
        graphics2D.setStroke(stroke);
        graphics2D.setColor(this.strokeColor);
        this.points.getFirst().drawLineMM(graphics2D, renderer, this.points.getLast(), this.strokeWidth);
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

    }

    private Optional<UUID> createCut() {
        RequestCutDTO rq = new RequestCutDTO(this.cut.getPoints(), this.cut.getCutType(), this.cut.getBitIndex(), this.cut.getDepth());
        return mainWindow.getController().requestCut(rq);
    }

    @Override
    public boolean addPoint(Rendering2DWindow renderer, PersoPoint pointInMM) {
        List<VertexDTO> newPoints = this.cut.getPoints();
        newPoints.add(new VertexDTO(pointInMM.getLocationX(),pointInMM.getLocationY(),  this.cut.getDepth()));
        this.cut = new CutDTO(this.cut.getId(), this.cut.getDepth(), this.cut.getBitIndex(), this.cut.getCutType(), newPoints);

        return this.cut.getPoints().size() >= 2; // returns true if all the points are added
    }

    @Override
    public Optional<UUID> end() {
        return createCut();
    }

    @Override
    public void cursorUpdate(Rendering2DWindow renderer, Drawing drawing) {
        PersoPoint p = this.cursorPoint;

        if(this.points.isEmpty()){ // First horizontal point
            p.movePoint(renderer.getMmMousePt().getX(), renderer.getMmMousePt().getY());

            double threshold = 10;
            threshold = renderer.scaleMMToPixel(threshold);
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
        else{ // Second horizontal point
            double firstPointX = this.points.getFirst().getLocationX();
            p.movePoint(firstPointX, renderer.getMmMousePt().getY()); // lock to Y axis

            // Get possible snap points
            double threshold = 10;
            threshold = renderer.scaleMMToPixel(threshold);
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
            if(mainWindow.getController().isPointOnBoard(pointDTO)){
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
