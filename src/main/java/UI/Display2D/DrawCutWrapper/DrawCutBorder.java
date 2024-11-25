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
import java.util.Optional;
import java.util.UUID;

public class DrawCutBorder extends DrawCutWrapper{


    public DrawCutBorder(CutType type, Rendering2DWindow renderer, MainWindow mainWindow) {
        super(type, renderer, mainWindow);
    }

    public DrawCutBorder(CutDTO cut, Rendering2DWindow renderer, MainWindow mainWindow) {
        super(cut, renderer, mainWindow);
    }

    @Override
    public void draw(Graphics2D graphics2D, Rendering2DWindow renderer){
        this.update(renderer);
        graphics2D.setStroke(stroke);
        graphics2D.setColor(this.strokeColor);

        for(int i =0; i  < points.size() - 1; i++){
            this.points.get(i).drawLineMM(graphics2D, renderer, this.points.get(i+1), this.strokeWidth);
        }

    }

    @Override
    public void drawWhileChanging(Graphics2D graphics2D, Rendering2DWindow renderer, PersoPoint cursor) {

    }

    @Override
    public boolean addPoint(Drawing drawing, Rendering2DWindow renderer, PersoPoint pointInMM) {

        if(!refs.isEmpty()){
            ArrayList<VertexDTO> newPoints = new ArrayList<>();
            for(int i =0 ; i < refs.size(); i++){
                newPoints.add(mainWindow.getController().getDefaultBorderPointCut());
                newPoints.add(mainWindow.getController().getDefaultBorderPointCut()); // adds two points points size checking
            }
            this.cut = new CutDTO(this.cut.getId(), this.cut.getDepth(), this.cut.getBitIndex(), this.cut.getCutType(), newPoints, refs, this.cut.getState());
            return true;
        }
        return false;
    }

    @Override
    public boolean areRefsValid() {
        return !refs.isEmpty() && !areRefsPointinToItself();
    }

    @Override
    public Optional<UUID> end() {
        return createCut();
    }

    @Override
    public void cursorUpdate(Rendering2DWindow renderer, Drawing drawing) {
        PersoPoint p = this.cursorPoint;

        p.movePoint(renderer.getMmMousePt().getX(), renderer.getMmMousePt().getY());

        double threshold = renderer.scalePixelToMM(snapThreshold);
        VertexDTO p1 = new VertexDTO(p.getLocationX(), p.getLocationY(), 0.0f);
        Optional<VertexDTO> closestPoint = mainWindow.getController().getGridPointNearBorder(p1, threshold);

        if(closestPoint.isPresent()){
            p.movePoint(closestPoint.get().getX(), closestPoint.get().getY());
            VertexDTO snapPoint = new VertexDTO(closestPoint.get().getX(), closestPoint.get().getY(), 0.0f);
            refs = mainWindow.getController().getRefCutsAndBorderOnPoint(snapPoint);

            if(!refs.isEmpty()){
                p.setColor(VALID_COLOR);
                p.setValid(PersoPoint.Valid.VALID);
            }
        }
        else{
            p.setColor(INVALID_COLOR);
            p.setValid(PersoPoint.Valid.NOT_VALID);
        }
    }
}
