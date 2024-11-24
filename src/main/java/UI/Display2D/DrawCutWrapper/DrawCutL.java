package UI.Display2D.DrawCutWrapper;

import Common.DTO.CutDTO;
import Common.DTO.RefCutDTO;
import Common.DTO.RequestCutDTO;
import Common.DTO.VertexDTO;
import Common.Pair;
import Domain.CutType;
import Domain.RefCut;
import UI.Display2D.Drawing;
import UI.Display2D.Rendering2DWindow;
import UI.MainWindow;
import UI.Widgets.PersoPoint;

import javax.swing.text.html.Option;
import java.awt.*;
import java.sql.Ref;
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

        if(!this.points.isEmpty()){
            boolean isPointValid  = setRefsInterpolationToCursor(new VertexDTO(cursor.getLocationX(), cursor.getLocationY(), 0));

            if(isPointValid && this.cursorPoint.getValid() != PersoPoint.Valid.NOT_VALID){
                this.cut = new CutDTO(this.cut.getId(), this.cut.getDepth(), this.cut.getBitIndex(), this.cut.getCutType(), this.cut.getPoints(), refs);
            }
        }

        graphics2D.setStroke(stroke);
        graphics2D.setColor(cursor.getColor());

        for (int i =0; i < points.size()-1; i++){
            points.get(i).drawLineMM(graphics2D, renderer, points.get(i+1), this.strokeWidth);
        }

        for (PersoPoint point : this.points){ // drawing the points
            point.drawMM(graphics2D, renderer);
        }

    }

    @Override
    public boolean addPoint(Drawing drawing, Rendering2DWindow renderer, PersoPoint pointInMM) {
        List<VertexDTO> newPoints = this.cut.getPoints();

        if(newPoints.isEmpty()){
            VertexDTO newPoint = new VertexDTO(pointInMM.getLocationX(),pointInMM.getLocationY(),  this.cut.getDepth());
            newPoints.add(newPoint);
            newPoints.add(new VertexDTO(newPoint));
            newPoints.add(new VertexDTO(newPoint));

            this.cut = new CutDTO(this.cut.getId(), this.cut.getDepth(), this.cut.getBitIndex(), this.cut.getCutType(), newPoints, refs);
            return false;
        }
        else{

            this.cut = new CutDTO(this.cut.getId(), this.cut.getDepth(), this.cut.getBitIndex(), this.cut.getCutType(), newPoints, refs);
            return true;
        }
    }

    private boolean setRefsInterpolationToCursor(VertexDTO cursor){
        List<VertexDTO> out = new ArrayList<>();

        if(refs.size() < 2){
            return false; // can't compute the perpendicularPoints of the ref
        }

        VertexDTO p1a = refs.getFirst().getAbsoluteFirstPoint(mainWindow.getController());
        VertexDTO p1b = refs.getFirst().getAbsoluteSecondPoint(mainWindow.getController());

        VertexDTO p2a = refs.get(1).getAbsoluteFirstPoint(mainWindow.getController());
        VertexDTO p2b = refs.get(1).getAbsoluteSecondPoint(mainWindow.getController());

        Pair<VertexDTO, VertexDTO> pair1 = VertexDTO.perpendicularPointsAroundP1(p1a, p1b);
        Pair<VertexDTO, VertexDTO> pair2= VertexDTO.perpendicularPointsAroundP1(p2a, p2b);

        VertexDTO v1 = pair1.getSecond().sub(pair1.getFirst());
        VertexDTO v2 = pair2.getSecond().sub(pair2.getFirst());


        VertexDTO l1p1 = cursor;
        VertexDTO l1p2 = cursor.add(v1);

        VertexDTO l2p1 = cursor;
        VertexDTO l2p2 = cursor.add(v2);

        Optional<VertexDTO> intersection1 = VertexDTO.isLineIntersectNoLimitation(l1p1, l1p2, p1a, p1b);
        Optional<VertexDTO> intersection2 = VertexDTO.isLineIntersectNoLimitation(l2p1, l2p2, p2a, p2b);


        if(intersection1.isPresent() && intersection2.isPresent()){

            VertexDTO p1 = intersection1.get();
            VertexDTO p2 = intersection2.get();

            VertexDTO numerator1 = p1.sub(p1a);
            VertexDTO numerator2 = p2.sub(p2a);

            VertexDTO denominator1 = p1b.sub(p1a);
            VertexDTO denominator2 = p2b.sub(p2a);

            double t1 = numerator1.getDistance() / denominator1.getDistance();
            double t2 = numerator2.getDistance() / denominator2.getDistance();

            if(t1 >= 0 && t1 <= 1 && t2 >=0 && t2 <= 1){
                RefCutDTO newRef1 = new RefCutDTO(refs.getFirst().getCut(), refs.getFirst().getIndex(), t1);
                RefCutDTO newRef2 = new RefCutDTO(refs.get(1).getCut(), refs.get(1).getIndex(), t2);
                this.refs = new ArrayList<>();
                refs.add(newRef1);
                refs.add(newRef2);
                return true;
            }

        }

        return false;
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

            double threshold = renderer.scaleMMToPixel(snapThreshold);
            VertexDTO p1 = new VertexDTO(p.getLocationX(), p.getLocationY(), 0.0f);
            Optional<VertexDTO> closestPoint = mainWindow.getController().getPointNearIntersections(p1, threshold);

            if(closestPoint.isPresent()){
                p.movePoint(closestPoint.get().getX(),closestPoint.get().getY());
                p1 = new VertexDTO(p.getLocationX(), p.getLocationY(), 0.0f);
                refs = mainWindow.getController().getRefCutsAndBorderOnPoint(p1);

                if (refs.size() >= 2){
                    drawing.changeRefWrapperById(refs.getFirst().getCut().getId());
                    drawing.changeRefWrapperById(refs.get(1).getCut().getId());
                    p.setColor(Color.GREEN);
                    p.setValid(PersoPoint.Valid.VALID);
                }
            }
            else{
                p.setColor(Color.RED);
                p.setValid(PersoPoint.Valid.NOT_VALID);
            }
        }
        else{ // Second L cut point
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
