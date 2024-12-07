package UI.Display2D;

import UI.Events.ChangeAttributeEvent;
import UI.Widgets.ChooseDimension;
import UI.Widgets.PersoPoint;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;

public class Scaling {
    private MouseMotionListener scaleListener;
    private final Rendering2DWindow rend;
    private ChooseDimension chooseDimension;
    private double radius = 30;

    /**
     * Construcs a new Scaling object
     *
     * @param rend The {@code Rendering2DWindow} on which the scaling is happening
     */
    public Scaling(Rendering2DWindow rend) {
        this.rend = rend;
        addMouseMotionListener();
        initScalePointMouseListener();
    }

    public MouseMotionListener getScaleListener() {
        return scaleListener;
    }

    /**
     * Initializes the points and behavior for resizing the board.
     * This includes setting up the necessary components and defining how
     * the board responds to resize events.
     */
    void initiateScaling() {
        chooseDimension = new ChooseDimension(rend, rend.getMainWindow().getController().getGrid().isActive());
        double locationX = (rend.getOffsetX() + rend.getBoard().getX() + rend.getBoard().getWidth()) * rend.getZoom() - radius / 2;
        double locationY = rend.getHeight() - (rend.getBoard().getY() + rend.getOffsetY() + rend.getBoard().getHeight()) * rend.getZoom() - radius / 2;
        PersoPoint p = new PersoPoint(locationX, locationY, radius, false);
        PersoPoint p1 = new PersoPoint(locationX - rend.getBoard().getWidth() * rend.getZoom(), locationY, radius, true);
        PersoPoint p2 = new PersoPoint(locationX, locationY + rend.getBoard().getHeight() * rend.getZoom(), radius, true);
        rend.getPoints().add(p1);
        rend.getPoints().add(p);
        rend.getPoints().add(p2);
        rend.getAttributeListener().changeAttributeEventOccurred(new ChangeAttributeEvent(rend, chooseDimension));
    }

    /**
     * Sets up a mouse motion listener to handle resizing the board when a
     * point is being dragged.
     */
    private void initScalePointMouseListener() {
        scaleListener = new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                final double zoom = rend.getZoom();
                final double offsetX = rend.getOffsetX();
                final double offsetY = rend.getOffsetY();
                if (rend.isDraggingAPoint()) {
                    super.mouseDragged(e);
                    PersoPoint p = rend.getPoints().get(1);
                    double ratio = 1 / zoom;
                    double newWidth = Math.max((e.getX()*ratio - offsetX), 0);
                    double newHeight = Math.max((rend.getHeight() - e.getY() - offsetY * zoom) * ratio , 0);

                    p.movePoint(Math.min(p.getLocationX()-p.getRadius()/2, rend.getMainWindow().getController().getPanelDTO().getMaxMMWidth()-p.getRadius()/2), p.getLocationY()-p.getRadius()/2);
                    rend.getPoints().get(0).movePoint(offsetX * zoom - p.getRadius() / 2, p.getLocationY());
                    rend.getPoints().get(2).movePoint(p.getLocationX(), rend.getHeight() - offsetY * zoom - p.getRadius() / 2);
                    rend.resizePanneau(newWidth, newHeight);
                    double displayWidth = Math.round(rend.getBoard().getWidth() * 100);
                    displayWidth = displayWidth / 100;
                    double displayHeight = Math.round(rend.getBoard().getHeight() * 100);
                    displayHeight = displayHeight / 100;
                    chooseDimension.getxTextField().getNumericInput().setText("" + displayWidth);
                    chooseDimension.getyTextField().getNumericInput().setText("" + displayHeight);
                    rend.repaint();
                }
            }
        };
    }

    private void addMouseMotionListener() {
        rend.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (rend.isDraggingAPoint()) {
                    super.mouseDragged(e);
                    for (PersoPoint point : rend.getPoints()) {
                        point.movePoint((rend.getOffsetX() + rend.getBoard().getWidth()) * rend.getZoom(), (rend.getHeight() - (rend.getOffsetY() + rend.getBoard().getHeight()) * rend.getZoom()));
                        rend.repaint();
                    }
                }
            }
        });
    }

}
