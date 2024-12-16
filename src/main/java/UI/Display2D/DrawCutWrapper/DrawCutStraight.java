package UI.Display2D.DrawCutWrapper;

import Common.DTO.*;
import Common.Units;
import Domain.CutType;
import UI.Display2D.Drawing;
import UI.Display2D.Rendering2DWindow;
import UI.Events.ChangeAttributeEvent;
import UI.MainWindow;
import UI.UIConfig;
import UI.UiUtil;
import UI.Widgets.CutBox;
import UI.Widgets.PersoPoint;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Vertical/Horizontal line drawing that inherits from DrawCutWrapper
 *
 * @author Louis-Etienne Messier
 */
public class DrawCutStraight extends DrawCutWrapper {
    public DrawCutStraight(CutType type, Rendering2DWindow renderer, MainWindow mainWindow) {
        super(type, renderer, mainWindow);
    }

    @Override
    public void drawAnchor(Graphics2D graphics2D, Rendering2DWindow renderer) {
        if (!cut.getRefsDTO().isEmpty()) { // drawing the first anchor point
            VertexDTO offset = cut.getRefsDTO().getFirst().getAbsoluteOffset(mainWindow.getController());
            PersoPoint referenceAnchorPoint = new PersoPoint(offset.getX(), offset.getY(), cursorRadius, true);
            referenceAnchorPoint.setColor(strokeColor);
            referenceAnchorPoint.drawMM(graphics2D, renderer);
        }
    }

    public DrawCutStraight(CutDTO cut, Rendering2DWindow renderer, MainWindow mainWindow) {
        super(cut, renderer, mainWindow);
    }

    @Override
    public void drawWhileChanging(Graphics2D graphics2D, Rendering2DWindow renderer, PersoPoint cursor) {
        graphics2D.setStroke(stroke);
        graphics2D.setColor(this.strokeColor);
        this.points = new ArrayList<>();
        for (VertexDTO p : temporaryCreationPoints) {
            points.add(new PersoPoint(p.getX(), p.getY(), 10.0f, true, strokeColor));
        }

        for (int i = 0; i < points.size() - 1; i++) {
            points.get(i).drawLineMM(graphics2D, renderer, points.get(i + 1));
        }

        graphics2D.setColor(cursor.getColor());
        if (!points.isEmpty()) {
            points.getLast().drawLineMM(graphics2D, renderer, cursor);
        }

        for (PersoPoint point : this.points) { // drawing the points
            point.drawMM(graphics2D, renderer);
        }

        if (!refs.isEmpty()) { // drawing the first anchor point
            VertexDTO offset = refs.getFirst().getAbsoluteOffset(mainWindow.getController());
            PersoPoint referenceAnchorPoint = new PersoPoint(offset.getX(), offset.getY(), cursorRadius, true);
            referenceAnchorPoint.setColor(ANCHOR_COLOR);
            referenceAnchorPoint.drawMM(graphics2D, renderer);
        }

    }

    @Override
    public void drawDimensions(Graphics2D graphics2D, Rendering2DWindow rendering2DWindow) {
        VertexDTO anchorAbsolutePosition = cut.getRefsDTO().getFirst().getAbsoluteOffset(mainWindow.getController());
        double anchorDiameter = mainWindow.getController().getBitDiameter(cut.getRefsDTO().getFirst().getCut().getBitIndex());
        if (cut.getCutType() == CutType.LINE_HORIZONTAL) {
            double verticalOffset = Math.abs(cut.getPoints().getFirst().getY());
            double absoluteY = mainWindow.getController().getAbsolutePointsPosition(cut).getFirst().getY();
            VertexDTO anchorToCut = new VertexDTO(anchorAbsolutePosition.getX(), absoluteY, 0);
            VertexDTO diffNormalized = anchorToCut.sub(anchorAbsolutePosition).normalize();
            VertexDTO p1 = anchorAbsolutePosition.add(diffNormalized.mul(anchorDiameter / 2));
            VertexDTO p2 = p1.add(diffNormalized.mul(verticalOffset));

            if (verticalOffset == 0) { // when the anchor and the cut is colinear
                p1 = anchorAbsolutePosition;
                p2 = anchorAbsolutePosition;
            }
            DimensionDTO yOffsetUnit = mainWindow.getController().convertUnit(new DimensionDTO(verticalOffset, Units.MM), UIConfig.INSTANCE.getDefaultUnit().getUnit());

            UiUtil.drawArrowWidthNumber(graphics2D, rendering2DWindow, p1, p2, yOffsetUnit.value(), ARROW_COLOR, ARROW_DIMENSION, DIMENSION_COLOR);
        } else if (cut.getCutType() == CutType.LINE_VERTICAL) {
            double horizontalOffset = Math.abs(cut.getPoints().getFirst().getX());
            double absoluteX = mainWindow.getController().getAbsolutePointsPosition(cut).getFirst().getX();
            VertexDTO anchorToCut = new VertexDTO(absoluteX, anchorAbsolutePosition.getY(), 0);
            VertexDTO diffNormalized = anchorToCut.sub(anchorAbsolutePosition).normalize();
            VertexDTO p1 = anchorAbsolutePosition.add(diffNormalized.mul(anchorDiameter / 2));
            VertexDTO p2 = p1.add(diffNormalized.mul(horizontalOffset));

            if (horizontalOffset == 0) {// when the anchor and the cut is colinear
                p1 = anchorAbsolutePosition;
                p2 = anchorAbsolutePosition;
            }

            DimensionDTO xOffsetUnit = mainWindow.getController().convertUnit(new DimensionDTO(horizontalOffset, Units.MM), UIConfig.INSTANCE.getDefaultUnit().getUnit());
            UiUtil.drawArrowWidthNumber(graphics2D, rendering2DWindow, p1, p2, xOffsetUnit.value(), ARROW_COLOR, ARROW_DIMENSION, DIMENSION_COLOR);
        } else if (cut.getCutType() == CutType.LINE_FREE) {
            VertexDTO anchor = cut.getRefsDTO().getFirst().getAbsoluteOffset(mainWindow.getController());
            VertexDTO relativeP1 = cut.getPoints().getFirst();

            VertexDTO p2 = anchor.add(relativeP1);

            DimensionDTO xOffsetUnit = mainWindow.getController().convertUnit(new DimensionDTO(relativeP1.getX(), Units.MM), UIConfig.INSTANCE.getDefaultUnit().getUnit());
            DimensionDTO yOffsetUnit = mainWindow.getController().convertUnit(new DimensionDTO(relativeP1.getY(), Units.MM), UIConfig.INSTANCE.getDefaultUnit().getUnit());

            UiUtil.drawArrowWidthNumberXY(graphics2D, rendering2DWindow, anchor, p2, xOffsetUnit.value(),
                    yOffsetUnit.value(), ARROW_COLOR, ARROW_DIMENSION, DIMENSION_COLOR);
        }

    }

    @Override
    public boolean addPoint(Drawing drawing, Rendering2DWindow renderer, PersoPoint pointInMM) {
        if (refs.isEmpty()) {
            refs = cut.getRefsDTO();
            VertexDTO p1 = new VertexDTO(pointInMM.getLocationX(), pointInMM.getLocationY(), 0.0f);
            RefCutDTO ref = mainWindow.getController().getRefCutsAndBorderOnPoint(p1).getFirst();
            if (refs.size() <= 0) {
                refs.add(ref);
            } else {
                refs.set(0, ref);
            }

        } else {
            VertexDTO newPoint = new VertexDTO(pointInMM.getLocationX(), pointInMM.getLocationY(), 0.0f);
            temporaryCreationPoints.add(newPoint);
            List<RefCutDTO> listRefs = mainWindow.getController().getRefCutsAndBorderOnPoint(newPoint);
            if (!listRefs.isEmpty() && !temporaryCreationPoints.isEmpty()) {
                int refIndex = temporaryCreationPoints.size();
                if (refs.size() > refIndex) {
                    refs.set(refIndex, listRefs.getFirst());
                } else {
                    refs.add(listRefs.getFirst());
                }
            }

        }
        return temporaryCreationPoints.size() >= 2; // returns true if all the points are added
    }

    @Override
    public boolean areRefsValid() {
        return !refs.isEmpty() && areRefsNotCircular();
    }

    @Override
    public Optional<UUID> end() {
        List<VertexDTO> relativeEdgeEdgePoints = new ArrayList<>();
        if (cut.getCutType() == CutType.LINE_VERTICAL) {
            relativeEdgeEdgePoints = mainWindow.getController().generateVerticalPointsRelativeEdgeEdgeFromAbsolute(temporaryCreationPoints.getFirst(), temporaryCreationPoints.get(1), cut.getBitIndex(), refs);
        } else if (cut.getCutType() == CutType.LINE_HORIZONTAL) {
            relativeEdgeEdgePoints = mainWindow.getController().generateHorizontalPointsRelativeEdgeEdgeFromAbsolute(temporaryCreationPoints.getFirst(), temporaryCreationPoints.get(1), cut.getBitIndex(), refs);
        } else if (cut.getCutType() == CutType.LINE_FREE) {
            relativeEdgeEdgePoints = mainWindow.getController().generateFreeCutPointsRelativeEdgeEdgeFromAbsolute(temporaryCreationPoints.getFirst(), temporaryCreationPoints.get(1), cut.getBitIndex(), refs);
        }
        this.cut = new CutDTO(this.cut.getId(), this.cut.getDepth(), this.cut.getBitIndex(), this.cut.getCutType(), relativeEdgeEdgePoints, refs, this.cut.getState());
        return createCut();
    }


    @Override
    public void moveUpdate(Point2D pixP, Rendering2DWindow renderer, MainWindow mainWindow) {
        List<VertexDTO> listPoints = mainWindow.getController().getAbsolutePointsPosition(getCutDTO());
        double threshold = getThresholdForMagnet(renderer);
        Point2D mmE = renderer.pixelTomm(pixP);
        this.cursorPoint = new PersoPoint(mmE.getX(), mmE.getY(), 1, true);
        PersoPoint p = new PersoPoint(mmE.getX(), mmE.getY(), 1, true);
        Optional<VertexDTO> closestPoint1 = mainWindow.getController().getGridPointNearBorder(new VertexDTO(p.getLocationX(), p.getLocationY(), 0), threshold);
        VertexDTO closestPoint = closestPoint1.orElse(new VertexDTO(p.getLocationX(), p.getLocationY(), 0));
        List<VertexDTO> relativePts;
        if (getCutType() == CutType.LINE_VERTICAL) {
            closestPoint = Optional.ofNullable(changeClosestLineMaybe(closestPoint1, threshold, false)).orElse(closestPoint);
            VertexDTO p1 = new VertexDTO(closestPoint.getX(), listPoints.get(0).getY(), 0);
            VertexDTO p2 = new VertexDTO(closestPoint.getX(), listPoints.get(1).getY(), 0);
            relativePts = mainWindow.getController().generateVerticalPointsRelativeEdgeEdgeFromAbsolute(p1, p2, getCutDTO().getBitIndex(), getCutDTO().getRefsDTO());
        } else if (getCutType() == CutType.LINE_HORIZONTAL) {
            closestPoint = Optional.ofNullable(changeClosestLineMaybe(closestPoint1, threshold, true)).orElse(closestPoint);
            VertexDTO p1 = new VertexDTO(listPoints.get(0).getX(), closestPoint.getY(), 0);
            VertexDTO p2 = new VertexDTO(listPoints.get(1).getX(), closestPoint.getY(), 0);
            relativePts = mainWindow.getController().generateHorizontalPointsRelativeEdgeEdgeFromAbsolute(p1, p2, getCutDTO().getBitIndex(), getCutDTO().getRefsDTO());
        } else {
            VertexDTO p1 = new VertexDTO(renderer.getDrawing().getPrevPts().get(0).getX() + closestPoint.getX() - pointDepart.getX(), renderer.getDrawing().getPrevPts().get(0).getY() + closestPoint.getY() - pointDepart.getY(), 0);
            VertexDTO p2 = new VertexDTO(renderer.getDrawing().getPrevPts().get(1).getX() + closestPoint.getX() - pointDepart.getX(), renderer.getDrawing().getPrevPts().get(1).getY() + closestPoint.getY() - pointDepart.getY(), 0);
            relativePts = mainWindow.getController().generateFreeCutPointsRelativeEdgeEdgeFromAbsolute(p1, p2, getCutDTO().getBitIndex(), getCutDTO().getRefsDTO());
        }
        CutDTO c = getCutDTO();
        CutDTO newCut = new CutDTO(c.getId(), c.getDepth(), c.getBitIndex(), c.getCutType(), relativePts, c.getRefsDTO(), c.getState());
        cut = new CutDTO(newCut);
        mainWindow.getController().modifyCut(newCut, false);
        //update board
        Optional<CutBox> cutBox = mainWindow.getMiddleContent().getCutWindow().getCutListPanel().getCutBoxWithId(getCutDTO().getId());
        mainWindow.getMiddleContent().getCutWindow().modifiedAttributeEventOccured(new ChangeAttributeEvent(cutBox, cutBox.get()));
    }

    @Override
    public void movePoint(Point2D pixP, Rendering2DWindow renderer, MainWindow mainWindow, int indexPoint) {
        List<VertexDTO> listPoints = mainWindow.getController().getAbsolutePointsPosition(getCutDTO());
        int nbRefs = mainWindow.getController().getCutDTOById(cut.getId()).getRefsDTO().size();
        double threshold = getThresholdForMagnet(renderer);
        List<VertexDTO> v = mainWindow.getController().getAbsolutePointsPosition(cut);
        Point2D mmE = renderer.pixelTomm(pixP);
        PersoPoint p = new PersoPoint(mmE.getX(), mmE.getY(), 1, true);

        if (getCutType() == CutType.LINE_VERTICAL) {
            this.cursorPoint = new PersoPoint(listPoints.get(indexPoint).getX(), mmE.getY(), 1, true);
            Optional<VertexDTO> closestPoint1 = mainWindow.getController().getGridPointNearBorder(new VertexDTO(cursorPoint.getLocationX(), cursorPoint.getLocationY(), 0), threshold);
            VertexDTO closestPoint = closestPoint1.orElse(new VertexDTO(p.getLocationX(), p.getLocationY(), 0));
            closestPoint = Optional.ofNullable(changeClosestLineMaybe(closestPoint1, threshold, true)).orElse(closestPoint);
            closestPoint = Optional.ofNullable(changeClosestPointMaybe(threshold, closestPoint1, true)).orElse(closestPoint);

            VertexDTO p1;
            VertexDTO p2;
            if (indexPoint == 0) {
                if (nbRefs <= 1) {
                    p1 = new VertexDTO(listPoints.get(0).getX(), closestPoint.getY(), 0);
                    p2 = new VertexDTO(listPoints.get(1).getX(), listPoints.get(1).getY(), 0);
                } else {
                    p1 = new VertexDTO(closestPoint.getX(), listPoints.get(0).getY(), 0);
                    p2 = new VertexDTO(closestPoint.getX(), listPoints.get(1).getY(), 0);
                }
            } else {
                if (nbRefs <= 2) {
                    p1 = new VertexDTO(listPoints.get(0).getX(), listPoints.get(0).getY(), 0);
                    p2 = new VertexDTO(listPoints.get(1).getX(), closestPoint.getY(), 0);
                } else {
                    p1 = new VertexDTO(closestPoint.getX(), listPoints.get(0).getY(), 0);
                    p2 = new VertexDTO(closestPoint.getX(), listPoints.get(1).getY(), 0);
                }
            }
            CutDTO c = getCutDTO();

            List<VertexDTO> relativePts = mainWindow.getController().generateVerticalPointsRelativeEdgeEdgeFromAbsolute(p1, p2, getCutDTO().getBitIndex(), getCutDTO().getRefsDTO());
            CutDTO newCut = new CutDTO(c.getId(), c.getDepth(), c.getBitIndex(), c.getCutType(), relativePts, c.getRefsDTO(), c.getState());
            cut = new CutDTO(newCut);
            mainWindow.getController().modifyCut(newCut, false);
        } else if (getCutType() == CutType.LINE_HORIZONTAL) {
            this.cursorPoint = new PersoPoint(mmE.getX(), listPoints.get(indexPoint).getY(), 1, true);
            Optional<VertexDTO> closestPoint1 = mainWindow.getController().getGridPointNearBorder(new VertexDTO(cursorPoint.getLocationX(), cursorPoint.getLocationY(), 0), threshold);
            VertexDTO closestPoint = closestPoint1.orElse(new VertexDTO(p.getLocationX(), p.getLocationY(), 0));
            closestPoint = Optional.ofNullable(changeClosestLineMaybe(closestPoint1, threshold, false)).orElse(closestPoint);
            closestPoint = Optional.ofNullable(changeClosestPointMaybe(threshold, closestPoint1, true)).orElse(closestPoint);
            VertexDTO p1;
            VertexDTO p2;
            if (indexPoint == 0) {
                if (nbRefs <= 1) {
                    p1 = new VertexDTO(closestPoint.getX(), listPoints.get(0).getY(), 0);
                    p2 = new VertexDTO(listPoints.get(1).getX(), listPoints.get(1).getY(), 0);
                } else {
                    p1 = new VertexDTO(listPoints.get(0).getX(), closestPoint.getY(), 0);
                    p2 = new VertexDTO(listPoints.get(1).getX(), closestPoint.getY(), 0);
                }
            } else {
                if (nbRefs <= 2) {
                    p1 = new VertexDTO(listPoints.get(0).getX(), listPoints.get(0).getY(), 0);
                    p2 = new VertexDTO(closestPoint.getX(), listPoints.get(1).getY(), 0);
                } else {
                    p1 = new VertexDTO(listPoints.get(0).getX(), closestPoint.getY(), 0);
                    p2 = new VertexDTO(listPoints.get(1).getX(), closestPoint.getY(), 0);
                }
            }
            CutDTO c = getCutDTO();

            List<VertexDTO> relativePts = mainWindow.getController().generateHorizontalPointsRelativeEdgeEdgeFromAbsolute(p1, p2, getCutDTO().getBitIndex(), getCutDTO().getRefsDTO());
            CutDTO newCut = new CutDTO(c.getId(), c.getDepth(), c.getBitIndex(), c.getCutType(), relativePts, c.getRefsDTO(), c.getState());
            cut = new CutDTO(newCut);
            mainWindow.getController().modifyCut(newCut, false);
        } else if (getCutType() == CutType.LINE_FREE) {
            this.cursorPoint = new PersoPoint(mmE.getX(), mmE.getY(), 1, true);
            Optional<VertexDTO> closestPoint1 = mainWindow.getController().getGridPointNearBorder(new VertexDTO(p.getLocationX(), p.getLocationY(), 0), threshold);
            VertexDTO closestPoint = closestPoint1.orElse(new VertexDTO(p.getLocationX(), p.getLocationY(), 0));
            closestPoint = Optional.ofNullable(changeClosestLineMaybe(closestPoint1, threshold, false)).orElse(closestPoint);
            closestPoint = Optional.ofNullable(changeClosestLineMaybe(closestPoint1, threshold, true)).orElse(closestPoint);
            closestPoint = Optional.ofNullable(changeClosestPointMaybe(threshold, closestPoint1, true)).orElse(closestPoint);
            VertexDTO p1;
            VertexDTO p2;
            if (indexPoint == 0) {
                p1 = new VertexDTO(closestPoint.getX(), closestPoint.getY(), 0);
                p2 = new VertexDTO(listPoints.get(1).getX(), listPoints.get(1).getY(), 0);
            } else {
                p1 = new VertexDTO(listPoints.get(0).getX(), listPoints.get(0).getY(), 0);
                p2 = new VertexDTO(closestPoint.getX(), closestPoint.getY(), 0);
            }
            CutDTO c = getCutDTO();

            List<VertexDTO> relativePts = mainWindow.getController().generateFreeCutPointsRelativeEdgeEdgeFromAbsolute(p1, p2, getCutDTO().getBitIndex(), getCutDTO().getRefsDTO());
            CutDTO newCut = new CutDTO(c.getId(), c.getDepth(), c.getBitIndex(), c.getCutType(), relativePts, c.getRefsDTO(), c.getState());
            cut = new CutDTO(newCut);
            mainWindow.getController().modifyCut(newCut, false);
        }
        Optional<CutBox> cutBox = mainWindow.getMiddleContent().getCutWindow().getCutListPanel().getCutBoxWithId(getCutDTO().getId());
        mainWindow.getMiddleContent().getCutWindow().modifiedAttributeEventOccured(new ChangeAttributeEvent(cutBox, cutBox.get()));
    }

    @Override
    public void cursorUpdate(Rendering2DWindow renderer, Drawing drawing) {
        PersoPoint p = this.cursorPoint;
        double tolerance = 0.000001;
        double threshold = getThresholdForMagnet(renderer);
        if (refs.isEmpty()) {
            p.movePoint(renderer.getMmMousePt().getX(), renderer.getMmMousePt().getY());

            VertexDTO p1 = new VertexDTO(p.getLocationX(), p.getLocationY(), 0.0f);

            Optional<VertexDTO> closestPoint = mainWindow.getController().getGridPointNearAllBorderAndCuts(p1, threshold);
            boolean b = true;
            if (closestPoint.isPresent()) {
                for (CutDTO cut : mainWindow.getController().getPanelDTO().getCutsDTO()) {
                    if (cut.getCutType() == CutType.LINE_FREE) {
                        List<VertexDTO> points = mainWindow.getController().getAbsolutePointsPosition(cut);
                        if (mainWindow.getController().isPointNearLine(closestPoint.get(), points.get(0), points.get(1), tolerance).isPresent()) {
                            b = false;
                        }
                    }
                }
            }
            Optional<VertexDTO> otherPoint = Optional.empty();
            Optional<VertexDTO> ve = Optional.empty();
            Optional<VertexDTO> ho = Optional.empty();
            if (b) {
                otherPoint = Optional.ofNullable(changeClosestLineMaybe(closestPoint, threshold, false))
                        .or(() -> closestPoint);
                ve = otherPoint;
                otherPoint = Optional.ofNullable(changeClosestLineMaybe(otherPoint, threshold, true))
                        .or(() -> closestPoint);
                ho = otherPoint;
            }
            otherPoint = Optional.ofNullable(changeClosestPointMaybe(threshold, otherPoint, b))
                    .or(() -> closestPoint);

            if (otherPoint.isPresent()) {
                p.movePoint(otherPoint.get().getX(), otherPoint.get().getY());

                if (ve.equals(otherPoint) && closestPoint.isPresent()) {
                    otherPoint = Optional.of(new VertexDTO(otherPoint.get().getX(), closestPoint.get().getY(), 0));
                    p.movePoint(otherPoint.get().getX(), otherPoint.get().getY());
                }
                if (ho.equals(otherPoint) && closestPoint.isPresent()) {
                    otherPoint = Optional.of(new VertexDTO(closestPoint.get().getX(), otherPoint.get().getY(), 0));
                    p.movePoint(otherPoint.get().getX(), otherPoint.get().getY());
                }
                if (closestPoint.isPresent() && (Math.abs(otherPoint.get().getX() - closestPoint.get().getX()) < tolerance || Math.abs(otherPoint.get().getY() - closestPoint.get().getY()) < tolerance)) {
                    p.setColor(ANCHOR_COLOR);
                    p.setValid(PersoPoint.Valid.VALID);
                } else {
                    p.setColor(INVALID_COLOR);
                    p.setValid(PersoPoint.Valid.NOT_VALID);
                }
            } else {
                p.setColor(INVALID_COLOR);
                p.setValid(PersoPoint.Valid.NOT_VALID);
            }
        } else if (this.points.isEmpty()) { // First point
            p.movePoint(renderer.getMmMousePt().getX(), renderer.getMmMousePt().getY());

            VertexDTO p1 = new VertexDTO(p.getLocationX(), p.getLocationY(), 0.0f);

            Optional<VertexDTO> closestPoint = mainWindow.getController().getGridPointNearAllBorderAndCuts(p1, threshold);
            Optional<VertexDTO> otherPoint = Optional.ofNullable(changeClosestLineMaybe(closestPoint, threshold, false))
                    .or(() -> closestPoint);
            Optional<VertexDTO> ve = otherPoint;
            otherPoint = Optional.ofNullable(changeClosestLineMaybe(otherPoint, threshold, true))
                    .or(() -> closestPoint);
            Optional<VertexDTO> ho = otherPoint;
            otherPoint = Optional.ofNullable(changeClosestPointMaybe(threshold, otherPoint, true))
                    .or(() -> closestPoint);

            if (otherPoint.isPresent()) {
                p.movePoint(otherPoint.get().getX(), otherPoint.get().getY());
                if (ve.equals(otherPoint) && closestPoint.isPresent()) {
                    otherPoint = Optional.of(new VertexDTO(otherPoint.get().getX(), closestPoint.get().getY(), 0));
                    p.movePoint(otherPoint.get().getX(), otherPoint.get().getY());
                }
                if (ho.equals(otherPoint) && closestPoint.isPresent()) {
                    otherPoint = Optional.of(new VertexDTO(closestPoint.get().getX(), otherPoint.get().getY(), 0));
                    p.movePoint(otherPoint.get().getX(), otherPoint.get().getY());
                }
                if (closestPoint.isPresent() && (Math.abs(otherPoint.get().getX() - closestPoint.get().getX()) < tolerance || Math.abs(otherPoint.get().getY() - closestPoint.get().getY()) < tolerance)) {
                    p.setColor(ANCHOR_COLOR);
                    p.setValid(PersoPoint.Valid.VALID);
                } else {
                    p.setColor(INVALID_COLOR);
                    p.setValid(PersoPoint.Valid.NOT_VALID);
                }
            } else {
                p.setColor(INVALID_COLOR);
                p.setValid(PersoPoint.Valid.NOT_VALID);
            }
        } else { // Second point

            if (this.cut.getCutType() == CutType.LINE_VERTICAL) {
                double firstPointX = this.points.getFirst().getLocationX();
                p.movePoint(firstPointX, renderer.getMmMousePt().getY()); // lock to Y axis
            } else if (this.cut.getCutType() == CutType.LINE_HORIZONTAL) {
                double firstPointY = this.points.getFirst().getLocationY();
                p.movePoint(renderer.getMmMousePt().getX(), firstPointY); // lock to X axis
            } else {
                p.movePoint(renderer.getMmMousePt().getX(), renderer.getMmMousePt().getY()); // Free Cut
            }


            // Get possible snap points
            VertexDTO cursor = new VertexDTO(p.getLocationX(), p.getLocationY(), 0.0f);
            VertexDTO p1 = new VertexDTO(points.getFirst().getLocationX(), points.getFirst().getLocationY(), 0.0f);
            Optional<VertexDTO> closestPoint = mainWindow.getController().getGridLineNearAllBorderAndCuts(p1,
                    cursor, threshold
            );
            Optional<VertexDTO> otherPoint = Optional.empty();
            PanelDTO board = mainWindow.getController().getPanelDTO();
            Optional<VertexDTO> finalClosestPoint = closestPoint;
            otherPoint = Optional.ofNullable(changeClosestLineMaybe(closestPoint, threshold, false))
                    .or(() -> finalClosestPoint);
            otherPoint = Optional.ofNullable(changeClosestLineMaybe(otherPoint, threshold, true))
                    .or(() -> finalClosestPoint);
            otherPoint = Optional.ofNullable(changeClosestPointMaybe(threshold, otherPoint, true))
                    .or(() -> finalClosestPoint);

            if (otherPoint.isPresent()) {
                if (otherPoint.isPresent() && (otherPoint.get().getX() == points.getFirst().getLocationX() || otherPoint.get().getY() == points.getFirst().getLocationY())) {
                    closestPoint = otherPoint;
                }
            }
            if (this.cut.getCutType() == CutType.LINE_FREE) {
                closestPoint = otherPoint;
            }

            // Snap
            if (closestPoint.isPresent()) {
                p.movePoint(closestPoint.get().getX(), closestPoint.get().getY());
                p.setColor(SNAP_COLOR);
                p.setValid(PersoPoint.Valid.VALID);
                return;
            }
            // Test if on board
            VertexDTO pointDTO = new VertexDTO(p.getLocationX(), p.getLocationY(), 0.0f);
            if (mainWindow.getController().isPointOnPanel(pointDTO)) {
                // Inside of the board
                p.setColor(VALID_COLOR);
                p.setValid(PersoPoint.Valid.VALID);
            } else {
                // Outside of the board
                p.setColor(INVALID_COLOR);
                p.setValid(PersoPoint.Valid.NOT_VALID);
            }
        }
    }
}
