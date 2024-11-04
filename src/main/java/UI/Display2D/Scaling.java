package UI.Display2D;

import UI.Events.ChangeAttributeEvent;
import UI.Widgets.ChooseDimension;
import UI.Widgets.PersoPoint;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class Scaling {
    private MouseMotionListener scaleListener;
    private final Rendering2DWindow rend;
    private final ChooseDimension chooseDimension;

    /**
     * Construcs a new Scaling object
     *
     * @param rend The {@code Rendering2DWindow} on which the scaling is happening
     */
    public Scaling(Rendering2DWindow rend) {
        this.rend = rend;
        chooseDimension = new ChooseDimension(rend);
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
        double radius = 5;
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
                    double newWidth = Math.max((e.getX() - offsetX * zoom) * ratio + p.getRadius() / 2, 0);
                    double newHeight = Math.max((rend.getHeight() - e.getY() - offsetY * zoom) * ratio - p.getRadius() / 2, 0);
                    rend.getPoints().get(0).movePoint(offsetX * zoom - p.getRadius() / 2, p.getLocationY());
                    rend.getPoints().get(2).movePoint(p.getLocationX(), rend.getHeight() - offsetY * zoom - p.getRadius() / 2);
                    rend.resizePanneau(newWidth, newHeight);
                    double displayWidth = Math.round(newWidth * 100);
                    displayWidth = displayWidth / 100;
                    double displayHeight = Math.round(newHeight * 100);
                    displayHeight = displayHeight / 100;
                    chooseDimension.getxTextField().setText("" + displayWidth);
                    chooseDimension.getyTextField().setText("" + displayHeight);
                    rend.repaint();
                }
            }
        };
    }
}
