package UI.Listeners;

import Common.DTO.VertexDTO;
import Domain.Controller;
import UI.MainWindow;
import UI.SubWindows.Rendering3DWindow;

import javax.swing.*;
import java.awt.event.*;
import java.security.InvalidKeyException;
import java.security.KeyException;
import java.util.UUID;

public class MeshManipulator implements KeyListener, MouseListener, MouseWheelListener, MouseMotionListener {

    private final Rendering3DWindow rendering3DWindow;
    private final Controller controller;
    private static final double MESH_TRANSLATION = 3;
    private static final double MESH_ROTATION = 1;
    private static final double TIME_BEFORE_DISPLAY = 3;

    private enum MovementType {MANUAL, DISPLAY}

    private UUID selectedMesh;
    private KeyEvent currentEvent = null;
    private MovementType movementType = MovementType.DISPLAY;
    private double clock = 0;
    private double mousePosX = 0;
    private double mousePosY = 0;

    public MeshManipulator(Rendering3DWindow rendering3DWindow, MainWindow mainWindow) {
        this.rendering3DWindow = rendering3DWindow;
        controller = mainWindow.getController();
        startAnimation();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        TransformRequest transform = getMovementForKeyCode(e.getKeyCode(), e.isShiftDown());
        if (transform != null) {
            currentEvent = e;
            movementType = MovementType.MANUAL;
            clock = 0;
        }
    }

    private TransformRequest getMovementForKeyCode(int keycode, boolean isShiftDown) {
        if (isShiftDown) {
            return switch (keycode) {
                case KeyEvent.VK_RIGHT ->
                        new TransformRequest(new VertexDTO(MESH_TRANSLATION, 0, 0), VertexDTO.zero(), 0);
                case KeyEvent.VK_LEFT ->
                        new TransformRequest(new VertexDTO(-MESH_TRANSLATION, 0, 0), VertexDTO.zero(), 0);
                case KeyEvent.VK_UP -> new TransformRequest(new VertexDTO(0, MESH_TRANSLATION, 0), VertexDTO.zero(), 0);
                case KeyEvent.VK_DOWN ->
                        new TransformRequest(new VertexDTO(0, -MESH_TRANSLATION, 0), VertexDTO.zero(), 0);
                default -> null;
            };
        }
        return switch (keycode) {
            case KeyEvent.VK_RIGHT -> new TransformRequest(VertexDTO.zero(), new VertexDTO(0, MESH_ROTATION, 0), 0);
            case KeyEvent.VK_LEFT -> new TransformRequest(VertexDTO.zero(), new VertexDTO(0, -MESH_ROTATION, 0), 0);
            case KeyEvent.VK_UP -> new TransformRequest(VertexDTO.zero(), new VertexDTO(MESH_ROTATION, 0, 0), 0);
            case KeyEvent.VK_DOWN -> new TransformRequest(VertexDTO.zero(), new VertexDTO(-MESH_ROTATION, 0, 0), 0);
            default -> null;
        };

    }

    public void setSelectedMesh(UUID selectedMesh) {
        this.selectedMesh = selectedMesh;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        rendering3DWindow.setMousePos(new VertexDTO(e.getX(), e.getY(), 1));
        rendering3DWindow.repaint();
    }

    public void startAnimation() {
        SwingWorker<Object, Object> sw = new SwingWorker<>() {
            private long lastTimeMillis = System.currentTimeMillis();

            @Override
            protected Object doInBackground() throws Exception {
                while (true) {
                    double delta = (System.currentTimeMillis() - lastTimeMillis) / 1000.0;
                    lastTimeMillis = System.currentTimeMillis();

                    if (!Double.isNaN(clock)) {
                        clock += delta;
                        if (clock > TIME_BEFORE_DISPLAY) {
                            clock = Double.NaN;
                            movementType = MovementType.DISPLAY;
                        }
                    }
                    TransformRequest movement = null;
                    if (currentEvent != null) {
                        movement = getMovementForKeyCode(currentEvent.getKeyCode(), currentEvent.isShiftDown());
                        movementType = MovementType.MANUAL;
                    }
                    if (movementType == MovementType.DISPLAY) {
                        movement = getMovementForKeyCode(KeyEvent.VK_LEFT, false);
                    }
                    if (movement != null) {
                        TransformRequest finalMovement = movement;
                        SwingUtilities.invokeLater(() -> {
                            try {
                                controller.applyTransform(controller.getMeshesOfScene().getFirst(), finalMovement.positionChange, finalMovement.rotationChange.mul(delta), finalMovement.scaleChange);
                            } catch (KeyException e) {
                                e.printStackTrace();
                            }
                            rendering3DWindow.repaint();
                        });


                    }
                    Thread.sleep(33);
                }
            }
        };
        sw.execute();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        try {
            int wheel = e.getWheelRotation();
            if (wheel < 0) {
                for (UUID id : controller.getMeshesOfScene()) {
                    controller.applyTransform(id, new VertexDTO(0, 0, 0), VertexDTO.zero(), 0.01);
                }
            } else if (wheel > 0) {
                for (UUID id : controller.getMeshesOfScene()) {
                    controller.applyTransform(id, new VertexDTO(0, 0, 0), VertexDTO.zero(), -0.01);
                }
            }
            rendering3DWindow.repaint();
        } catch (InvalidKeyException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        double offsetX = -((mousePosX - e.getPoint().x));
        double offsetY = ((mousePosY - e.getPoint().y));
        try {
            controller.applyTransform(selectedMesh, new VertexDTO(offsetX, offsetY, 0), VertexDTO.zero(), 0);
        } catch (InvalidKeyException ex) {
            System.out.println("Nothing selected for movement");
        }
        mousePosX = e.getPoint().x;
        mousePosY = e.getPoint().y;
        rendering3DWindow.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        rendering3DWindow.setMousePos(new VertexDTO(e.getX(), e.getY(), 1));
        rendering3DWindow.repaint();
        mousePosX = e.getPoint().x;
        mousePosY = e.getPoint().y;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (currentEvent != null && currentEvent.getKeyCode() == e.getKeyCode() && getMovementForKeyCode(e.getKeyCode(), false) != null) {
            currentEvent = null;
        }
    }

    record TransformRequest(VertexDTO positionChange, VertexDTO rotationChange, double scaleChange) {
    }

    // Empty classes to fulfill the listener contract

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

}
