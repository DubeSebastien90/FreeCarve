package UI.Display2D.DrawCutWrapper;

import Common.CutState;
import Common.DTO.CutDTO;
import Common.DTO.VertexDTO;
import Domain.CutType;
import UI.Display2D.Drawing;
import UI.Display2D.Rendering2DWindow;
import UI.Events.ChangeAttributeEvent;
import UI.MainWindow;
import UI.UiUtil;
import UI.Widgets.CutBox;
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
    public double getStrokeWidthBruh(Rendering2DWindow renderer){
        return (CLAMP_STROKE_WIDTH*renderer.getZoom());
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
            points.getFirst().drawLineMM(graphics2D, renderer, p1);
            points.getFirst().drawLineMM(graphics2D, renderer, p2);
            p1.drawLineMM(graphics2D, renderer, cursor);
            p2.drawLineMM(graphics2D, renderer, cursor);

            p1.drawMM(graphics2D, renderer); // drawing the points
            p2.drawMM(graphics2D, renderer); //drawing the points
        }

        for (PersoPoint point : this.points){ // drawing the points
            point.drawMM(graphics2D, renderer);
        }
    }

    @Override
    public void drawDimensions(Graphics2D graphics2D, Rendering2DWindow rendering2DWindow) {
        VertexDTO midBottom = new VertexDTO((cut.getPoints().get(0).getX() + cut.getPoints().get(2).getX()) / 2,
                (cut.getPoints().get(3).getY()) ,
                0.0f);

        VertexDTO centerPoint = new VertexDTO((cut.getPoints().get(0).getX() + cut.getPoints().get(2).getX()) / 2,
                                                (cut.getPoints().get(0).getY() + cut.getPoints().get(2).getY()) / 2,
                                                0.0f);
        double width = Math.abs(cut.getPoints().get(0).getX() - cut.getPoints().get(2).getX());
        double height = Math.abs(cut.getPoints().get(0).getY() - cut.getPoints().get(2).getY());

        double xPos = centerPoint.getX();
        double yPos = centerPoint.getY();

        UiUtil.drawArrowWidthNumber(graphics2D, rendering2DWindow, cut.getPoints().get(2), cut.getPoints().get(1), width, ARROW_COLOR, ARROW_DIMENSION, DIMENSION_COLOR);
        UiUtil.drawArrowWidthNumber(graphics2D, rendering2DWindow, cut.getPoints().get(2), cut.getPoints().get(3), height, ARROW_COLOR,ARROW_DIMENSION, DIMENSION_COLOR);
        UiUtil.drawArrowWidthNumberXY(graphics2D, rendering2DWindow, VertexDTO.zero(), centerPoint, xPos, yPos, ARROW_COLOR, ARROW_DIMENSION, DIMENSION_COLOR);
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

        VertexDTO center = new VertexDTO((p1.getX() + p3.getX()) / 2, (p1.getY() + p3.getY()) / 2, 0.0f);
        double height = Math.abs(p1.getY() - p3.getY());
        double width = Math.abs(p1.getX() - p3.getX());
        List<VertexDTO> pointsWithCenter = mainWindow.getController().generateRectanglePoints(center, width, height);
        this.cut = new CutDTO(this.cut.getId(), this.cut.getDepth(), this.cut.getBitIndex(), this.cut.getCutType(), pointsWithCenter, refs, CutState.VALID);
        return createCut();
    }

    @Override
    public void cursorUpdate(Rendering2DWindow renderer, Drawing drawing) {
        PersoPoint p = this.cursorPoint;
        double threshold;
        if (mainWindow.getController().getGrid().isMagnetic()) {
            threshold = renderer.scalePixelToMM(mainWindow.getController().getGrid().getMagnetPrecision());
        } else {
            threshold = renderer.scalePixelToMM(snapThreshold);
        }
        if(points.isEmpty()){
            p.movePoint(renderer.getMmMousePt().getX(), renderer.getMmMousePt().getY());

            VertexDTO p1 = new VertexDTO(p.getLocationX(), p.getLocationY(), 0.0f);
            Optional<VertexDTO> closestPoint = mainWindow.getController().getGridPointNearAllBorderAndCuts(p1, threshold);
            closestPoint = changeClosestPointIfMagnetic(threshold, closestPoint, true);


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
            VertexDTO p1 = new VertexDTO(p.getLocationX(), p.getLocationY(), 0.0f);
            Optional<VertexDTO> closestPoint = mainWindow.getController().getGridPointNearAllBorderAndCuts(p1, threshold);
            closestPoint = changeClosestPointIfMagnetic(threshold, closestPoint, true);

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

    @Override
    public void moveUpdate(Point2D pixP, Rendering2DWindow renderer, MainWindow mainWindow) {
        List<VertexDTO> listPoints = mainWindow.getController().getAbsolutePointsPosition(getCutDTO());
        Point2D mmE = renderer.pixelTomm(pixP);
        VertexDTO p1;
        VertexDTO p2;

        if (mainWindow.getController().isRoundedCutDTOSegmentHoveredByCursor(cut, new VertexDTO(renderer.getMmMousePt().getX(), renderer.getMmMousePt().getY(), 0), 0, 1)) {
            p1 = new VertexDTO(mmE.getX(), listPoints.get(1).getY(), 0);
            p2 = new VertexDTO(listPoints.get(3).getX(), listPoints.get(3).getY(), 0);
        } else if (mainWindow.getController().isRoundedCutDTOSegmentHoveredByCursor(cut, new VertexDTO(renderer.getMmMousePt().getX(), renderer.getMmMousePt().getY(), 0), 1, 2)) {
            p1 = new VertexDTO(listPoints.get(1).getX(), mmE.getY(), 0);
            p2 = new VertexDTO(listPoints.get(2).getX(), listPoints.get(3).getY(), 0);
        } else if (mainWindow.getController().isRoundedCutDTOSegmentHoveredByCursor(cut, new VertexDTO(renderer.getMmMousePt().getX(), renderer.getMmMousePt().getY(), 0), 2, 3)) {
            p1 = new VertexDTO(listPoints.get(1).getX(), listPoints.get(1).getY(), 0);
            p2 = new VertexDTO(mmE.getX(), listPoints.get(3).getY(), 0);
        } else {
            p1 = new VertexDTO(listPoints.get(1).getX(), listPoints.get(1).getY(), 0);
            p2 = new VertexDTO(listPoints.get(3).getX(), mmE.getY(), 0);
        }
        CutDTO c = getCutDTO();

        List<VertexDTO> relativePts = mainWindow.getController().generateRectanglePoints(p1, p2);
        mainWindow.getController().modifyCut(new CutDTO(c.getId(), c.getDepth(), c.getBitIndex(), c.getCutType(), relativePts, c.getRefsDTO(), c.getState()), false);

        Optional<CutBox> cutBox = mainWindow.getMiddleContent().getCutWindow().getCutListPanel().getCutBoxWithId(getCutDTO().getId());
        mainWindow.getMiddleContent().getCutWindow().modifiedAttributeEventOccured(new ChangeAttributeEvent(cutBox, cutBox.get()));
    }

    @Override
    public void movePoint(Point2D pixP, Rendering2DWindow renderer, MainWindow mainWindow, int indexPoint) {
        List<VertexDTO> listPoints = mainWindow.getController().getAbsolutePointsPosition(getCutDTO());
        Point2D mmE = renderer.pixelTomm(pixP);
        VertexDTO p1;
        VertexDTO p2;

        if (indexPoint == 0) {
            p1 = new VertexDTO(mmE.getX(), listPoints.get(2).getY(), 0);
            p2 = new VertexDTO(listPoints.get(2).getX(), mmE.getY(), 0);
        } else if (indexPoint == 1) {
            p1 = new VertexDTO(mmE.getX(), mmE.getY(), 0);
            p2 = new VertexDTO(listPoints.get(3).getX(), listPoints.get(3).getY(), 0);
        } else if (indexPoint == 2) {
            p1 = new VertexDTO(listPoints.get(0).getX(), mmE.getY(), 0);
            p2 = new VertexDTO(mmE.getX(), listPoints.get(0).getY(), 0);
        } else {
            p1 = new VertexDTO(listPoints.get(1).getX(), listPoints.get(1).getY(), 0);
            p2 = new VertexDTO(mmE.getX(), mmE.getY(), 0);
        }
        CutDTO c = getCutDTO();

        List<VertexDTO> relativePts = mainWindow.getController().generateRectanglePoints(p1, p2);
        mainWindow.getController().modifyCut(new CutDTO(c.getId(), c.getDepth(), c.getBitIndex(), c.getCutType(), relativePts, c.getRefsDTO(), c.getState()), false);

        Optional<CutBox> cutBox = mainWindow.getMiddleContent().getCutWindow().getCutListPanel().getCutBoxWithId(getCutDTO().getId());
        mainWindow.getMiddleContent().getCutWindow().modifiedAttributeEventOccured(new ChangeAttributeEvent(cutBox, cutBox.get()));
    }

    @Override
    public void draw(Graphics2D graphics2D, Rendering2DWindow renderer) {
        this.update(renderer);
        graphics2D.setStroke(new BasicStroke((float) (CLAMP_STROKE_WIDTH*renderer.getZoom())));
        graphics2D.setColor(CLAMP_COLOR);

        Point2D temp1 = renderer.mmTopixel(new Point2D.Double(points.get(0).getLocationX(), points.get(0).getLocationY()));
        Point2D _temp1 = renderer.mmTopixel(new Point2D.Double(points.get(1).getLocationX(), points.get(1).getLocationY()));
        Point2D temp2 = renderer.mmTopixel(new Point2D.Double(points.get(2).getLocationX(), points.get(2).getLocationY()));
        Point2D _temp2 =  renderer.mmTopixel(new Point2D.Double(points.get(3).getLocationX(), points.get(3).getLocationY()));

        int height = (int) Math.abs(temp2.getY() - temp1.getY());
        int width = (int) Math.abs(temp2.getX() - temp1.getX());
        int xMin = (int) temp1.getX();
        int yMin = (int) temp2.getY();

        if(temp2.getX() < _temp1.getX()) {
            xMin -= width;
        }

        if(_temp1.getY() > temp1.getY()){
            yMin -= height;
        }

        graphics2D.fillRect(xMin, yMin, width, height);

        //draw line
        Color c = Color.black;
        for (int i = 0; i < points.size() - 1; i++) {
            if (this.points.get(i).getColor() != HOVER_VIEW_COLOR) {
                c = this.points.get(i).getColor();
            }
        }
        for (int i = 0; i < points.size() - 1; i++) {
            Color c2 = c;
            if (renderer.getDrawing().getState() == Drawing.DrawingState.IDLE && hoveredView && mainWindow.getController().isRoundedCutDTOSegmentHoveredByCursor(cut, new VertexDTO(renderer.getMmMousePt().getX(), renderer.getMmMousePt().getY(), 0), i, i + 1)) {
                c = HOVER_VIEW_COLOR;
            }
            graphics2D.setColor(c);
            this.points.get(i).drawLineMM(graphics2D, renderer, this.points.get(i + 1));
            c = c2;
        }

        //draw points
        for (int i = 0; i < points.size() - 1; i++) {
            if (this.points.get(i).getColor() != HOVER_VIEW_COLOR) {
                c = this.points.get(i).getColor();
            }
        }
        for (VertexDTO point : renderer.getMainWindow().getController().getAbsolutePointsPosition(cut)) {
            graphics2D.setColor(c);
            Point2D temp = renderer.mmTopixel(new Point2D.Double(point.getX(), point.getY()));
            PersoPoint _point = new PersoPoint(point.getX(), point.getY(), this.pointsRadius / renderer.getZoom(), true);
            if (renderer.getDrawing().getState() == Drawing.DrawingState.IDLE && renderer.isPointonPanel() && mainWindow.getController().mouse_on_top(renderer.getMousePt().getX(), renderer.getMousePt().getY(), temp.getX(), temp.getY(), _point.getRadius() * renderer.getZoom())) {
                graphics2D.setColor(HOVER_VIEW_COLOR);
            }
            _point.drawMM(graphics2D, renderer);
        }
    }

    @Override
    public void drawAnchor(Graphics2D graphics2D, Rendering2DWindow renderer) {
        // Opposites points of the rectangle
        VertexDTO p1 = cut.getPoints().getFirst();
        VertexDTO p3 = cut.getPoints().get(2);

        // Anchor points
        VertexDTO center = new VertexDTO((p1.getX() + p3.getX()) / 2, (p1.getY() + p3.getY()) / 2, 0.0f);
        PersoPoint referenceAnchorPoint = new PersoPoint(center.getX(), center.getY(), cursorRadius, true);
        referenceAnchorPoint.setColor(ANCHOR_COLOR);
        //referenceAnchorPoint.drawMM(graphics2D, renderer);
    }
}
