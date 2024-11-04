package UI.Display2D.DrawCutWrapper;

import Domain.CutDTO;
import Domain.CutType;
import Domain.RequestCutDTO;
import Domain.ThirdDimension.VertexDTO;
import UI.Display2D.Drawing;
import UI.Display2D.Rendering2DWindow;
import UI.MainWindow;
import UI.Widgets.PersoPoint;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Simple straight horizontal line drawing class that inherits from DrawCutWrapper
 * @author Louis-Etienne Messier
 */
public class DrawCutHorizontal extends  DrawCutWrapper{

    public DrawCutHorizontal(CutType type, Rendering2DWindow renderer, MainWindow mainWindow) {
        super(type, renderer, mainWindow);
    }

    public DrawCutHorizontal(CutDTO cut, Rendering2DWindow renderer, MainWindow mainWindow) {
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
    public void beingDrawned(Graphics2D graphics2D, Rendering2DWindow renderer, PersoPoint cursor) {
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

    /**
     * Create the cut
     * @return {@code Optional<UUID>} UUID if the cut is valid, null if the cut is invalid
     */
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
            Optional<PersoPoint> closestPoint = drawing.getPointNearAllLine(p);
            if(closestPoint.isPresent()){
                p.movePoint(closestPoint.get().getLocationX(),closestPoint.get().getLocationY());
                p.setColor(Color.GREEN);
                p.setValid(PersoPoint.Valid.VALID);
            }
            else{
                p.setColor(Color.RED);
                p.setValid(PersoPoint.Valid.NOT_VALID);
            }
        }
        else{ // Second horizontal point
            double firstPointY = this.points.getFirst().getLocationY();
            p.movePoint(renderer.getMmMousePt().getX(), firstPointY); // lock to Y axis

            Optional<PersoPoint> closestPoint = drawing.getLineNearAllLine(points.getFirst(), p);
            closestPoint.ifPresent(persoPoint -> p.movePoint(persoPoint.getLocationX(), persoPoint.getLocationY()));

            Rectangle2D boundaries = renderer.getBoard();
            if(p.getLocationX() <= boundaries.getWidth() + boundaries.getX() &&
                    p.getLocationX() >= boundaries.getX() &&
                    p.getLocationY() <= boundaries.getHeight() + boundaries.getY() &&
                    p.getLocationY() >= boundaries.getY()){
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
