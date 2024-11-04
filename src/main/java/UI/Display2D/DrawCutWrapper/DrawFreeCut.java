package UI.Display2D.DrawCutWrapper;

import Domain.CutDTO;
import Domain.CutType;
import Domain.RequestCutDTO;
import Domain.ThirdDimension.VertexDTO;
import UI.Display2D.Drawing;
import UI.MainWindow;
import UI.Display2D.Rendering2DWindow;
import UI.Widgets.PersoPoint;

import java.awt.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

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
    public void draw(Graphics2D graphics2D, Rendering2DWindow renderer)  {
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
        graphics2D.setColor(this.strokeColor);
        for (PersoPoint point : this.points){ // drawing the points
            point.drawMM(graphics2D, renderer);
        }

        for (int i =0; i < points.size()-1; i++){
            points.get(i).drawLineMM(graphics2D, renderer, points.get(i+1), this.strokeWidth);
        }

        graphics2D.setColor(cursor.getColor());
        if (!points.isEmpty()){
            points.getLast().drawLineMM(graphics2D, renderer, cursor, this.strokeWidth);
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


}
