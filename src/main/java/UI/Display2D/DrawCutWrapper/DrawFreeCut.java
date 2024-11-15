package UI.Display2D.DrawCutWrapper;

import Common.DTO.CutDTO;
import Domain.CutType;
import Common.DTO.RequestCutDTO;
import Common.DTO.VertexDTO;
import UI.Display2D.Drawing;
import UI.MainWindow;
import UI.Display2D.Rendering2DWindow;
import UI.Widgets.PersoPoint;

import java.awt.*;
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
    public void drawWhileChanging(Graphics2D graphics2D, Rendering2DWindow renderer, PersoPoint cursor) {
        this.update(renderer);
        graphics2D.setStroke(stroke);
        graphics2D.setColor(this.strokeColor);

        for (int i =0; i < points.size()-1; i++){ // drawing the lines
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
        p.movePoint(renderer.getMmMousePt().getX(), renderer.getMmMousePt().getY());

        // For the snap area
        double threshold = 10;
        threshold = renderer.scaleMMToPixel(threshold);

        // Get the possible closest point
        VertexDTO pointDTO = new VertexDTO(p.getLocationX(), p.getLocationY(), 0.0f);
        Optional<VertexDTO> closestPoint = mainWindow.getController().getGridPointNearAllBorderAndCuts(pointDTO, threshold);

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


}
