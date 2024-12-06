package UI.Display2D.DrawCutWrapper;

import Common.CutState;
import Common.DTO.ClampZoneDTO;
import Common.DTO.CutDTO;
import Common.DTO.VertexDTO;
import Common.Exceptions.ClampZoneException;
import Domain.CutType;
import UI.Display2D.Drawing;
import UI.Display2D.Rendering2DWindow;
import UI.MainWindow;
import UI.Widgets.PersoPoint;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DrawCutClamp extends DrawCutWrapper{

    public DrawCutClamp(CutType type, Rendering2DWindow renderer, MainWindow mainWindow) {
        super(type, renderer, mainWindow);
    }
    public DrawCutClamp(CutDTO cut, Rendering2DWindow renderer, MainWindow mainWindow) {
        super(cut, renderer, mainWindow);
    }

    @Override
    public void drawWhileChanging(Graphics2D graphics2D, Rendering2DWindow renderer, PersoPoint cursor) {
        graphics2D.setStroke(stroke);
        graphics2D.setColor(cursor.getColor());

        if(!this.temporaryCreationPoints.isEmpty()){
            this.points= new ArrayList<>();
            VertexDTO absPos = temporaryCreationPoints.getFirst();
            this.points.add(new PersoPoint(absPos.getX(), absPos.getY(), 10.0f, true, strokeColor));

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
    public boolean addPoint(Drawing drawing, Rendering2DWindow renderer, PersoPoint pointInMM) {
        if (temporaryCreationPoints.isEmpty()){
            temporaryCreationPoints.add(new VertexDTO(
                    pointInMM.getLocationX(),
                    pointInMM.getLocationY(),
                    0.0f
            ));
            return false;
        }
        else {
            temporaryCreationPoints.add(new VertexDTO(
                    pointInMM.getLocationX(),
                    pointInMM.getLocationY(),
                    0.0f
            ));
            return true;
        }
    }

    @Override
    public boolean areRefsValid() {
        return true;
    }

    @Override
    public Optional<UUID> end() {
        VertexDTO p1 = temporaryCreationPoints.getFirst();
        VertexDTO p3 = temporaryCreationPoints.get(1);
        List<VertexDTO> points = generateRectanglePoints(p1, p3);
        this.cut = new CutDTO(this.cut.getId(), this.cut.getDepth(), this.cut.getBitIndex(), this.cut.getCutType(), points, refs, CutState.VALID);
        return createCut();
    }

    @Override
    public void cursorUpdate(Rendering2DWindow renderer, Drawing drawing) {
        PersoPoint p = this.cursorPoint;

        if(points.isEmpty()){
            p.movePoint(renderer.getMmMousePt().getX(), renderer.getMmMousePt().getY());

            double threshold = renderer.scalePixelToMM(snapThreshold);
            VertexDTO p1 = new VertexDTO(p.getLocationX(), p.getLocationY(), 0.0f);
            Optional<VertexDTO> closestPoint = mainWindow.getController().getGridPointNearAllBorderAndCuts(p1, threshold);

            if(closestPoint.isPresent()){
                p.movePoint(closestPoint.get().getX(), closestPoint.get().getY());
                p.setColor(SNAP_COLOR);
                p.setValid(PersoPoint.Valid.VALID);
            }
            else{
                if(renderer.isPointonPanel()){
                    p.setColor(VALID_COLOR);
                    p.setValid(PersoPoint.Valid.VALID);
                }
                else{
                    p.setColor(INVALID_COLOR);
                    p.setValid(PersoPoint.Valid.NOT_VALID);
                }
            }
        }
        else{
            p.movePoint(renderer.getMmMousePt().getX(), renderer.getMmMousePt().getY());

            double threshold = renderer.scalePixelToMM(snapThreshold);
            VertexDTO p1 = new VertexDTO(p.getLocationX(), p.getLocationY(), 0.0f);
            Optional<VertexDTO> closestPoint = mainWindow.getController().getGridPointNearAllBorderAndCuts(p1, threshold);

            if(closestPoint.isPresent()){
                p.movePoint(closestPoint.get().getX(), closestPoint.get().getY());
                p.setColor(SNAP_COLOR);
                p.setValid(PersoPoint.Valid.VALID);
            }
            else{
                if (renderer.isPointonPanel()){
                    p.setColor(VALID_COLOR);
                    p.setValid(PersoPoint.Valid.VALID);
                }
                else {
                    p.setColor(INVALID_COLOR);
                    p.setValid(PersoPoint.Valid.NOT_VALID);
                }
            }
        }

    }

    private List<VertexDTO> generateRectanglePoints(VertexDTO p1, VertexDTO p3) {
        List<VertexDTO> points = new ArrayList<>();

        points.add(new VertexDTO(p3.getX(), p3.getY(), 0.0f));
        points.add(new VertexDTO(p1.getX(), p1.getY(), 0.0f));

        return points;
    }

    @Override
    public void draw(Graphics2D graphics2D, Rendering2DWindow renderer) {
        this.update(renderer);
        graphics2D.setStroke(new BasicStroke((float) CLAMP_STROKE_WIDTH));
        graphics2D.setColor(INVALID_COLOR);

        Point2D temp1 = renderer.mmTopixel(new Point2D.Double(points.get(0).getLocationX(), points.get(0).getLocationY()));
        Point2D temp2 = renderer.mmTopixel(new Point2D.Double(points.get(1).getLocationX(), points.get(1).getLocationY()));

        int x = (int) Math.min(temp1.getX(), temp2.getX());
        int y = (int) Math.min(temp1.getY(), temp2.getY());
        int width = (int) Math.abs(temp1.getX() - temp2.getX());
        int height = (int) Math.abs(temp1.getY() - temp2.getY());

        graphics2D.fillRect(x, y, width, height);
    }
}
