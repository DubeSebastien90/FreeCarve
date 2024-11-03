package UI.Widgets.DrawCutWrapper;

import Domain.CutDTO;
import Domain.CutType;
import Domain.RequestCutDTO;
import Domain.ThirdDimension.VertexDTO;
import UI.MainWindow;
import UI.Display2D.Rendering2DWindow;
import UI.Widgets.PersoPoint;
import com.sun.tools.javac.Main;

import java.awt.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

/**
 * Simple straight line drawing class that inherits from DrawCutWrapper
 * @author Louis-Etienne Messier
 */
public class DrawParrallelCut extends DrawCutWrapper {

    private CutDTO cut;
    private Color strokeColor = Color.RED;
    private float strokeWidth = 3.0f;
    private Stroke stroke;
    private MainWindow mainWindow;

    public DrawParrallelCut(CutType type, Rendering2DWindow renderer, MainWindow mainWindow){
        this.cut = new CutDTO(new UUID(1000, 1000), 0.0f, -1, type, new ArrayList<VertexDTO>());
        this.stroke = new BasicStroke(strokeWidth);
        this.mainWindow = mainWindow;
        this.update(renderer);
    }

    public DrawParrallelCut(CutDTO cut, Rendering2DWindow renderer, MainWindow mainWindow){
        this.cut = cut;
        this.stroke = new BasicStroke(strokeWidth);
        this.mainWindow =mainWindow;
        this.update(renderer);
    }

    private void update(Rendering2DWindow renderer){
        this.points = new ArrayList<>();
        for (VertexDTO point : cut.getPoints()){
            PersoPoint p1 = new PersoPoint(point.getX(), point.getY(), 10.0f, true, strokeColor);
            points.add(p1);
        }
    }

    @Override
    public void draw(Graphics2D graphics2D, Rendering2DWindow renderer)  {
        this.update(renderer);
        graphics2D.setStroke(stroke);
        graphics2D.setColor(this.strokeColor);
        this.points.getFirst().drawLineMM(graphics2D, renderer, this.points.getLast());
    }

    @Override
    public void beingDrawned(Graphics2D graphics2D, Rendering2DWindow renderer, PersoPoint cursor) {
        this.update(renderer);
        graphics2D.setColor(this.strokeColor);
        for (PersoPoint point : this.points){ // drawing the points
            point.drawMM(graphics2D, renderer);
        }

        for (int i =0; i < points.size()-1; i++){
            points.get(i).drawLineMM(graphics2D, renderer, points.get(i+1));
        }

        graphics2D.setColor(cursor.getColor());
        if (!points.isEmpty()){
            points.getLast().drawLineMM(graphics2D, renderer, cursor);
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
    public CutType getCutType() {
        return this.cut.getCutType();
    }


    public Color getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(Color strokeColor) {
        this.strokeColor = strokeColor;
    }


}
