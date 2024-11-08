package UI.Listeners;

import Domain.Controller;
import Common.DTO.VertexDTO;
import UI.MainWindow;
import UI.SubWindows.Rendering3DWindow;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.security.KeyException;
import java.util.UUID;

public class MeshManipulator implements KeyListener, MouseListener {

    private final Rendering3DWindow rendering3DWindow;
    private final Controller controller;

    private static final float GIMBAL_ROTATION = 0.1f;
    private static final float MESH_TRANSLATION = 3;
    private static final float MESH_ROTATION = 0.1f;

    private enum MovementType {TRANSLATION, ROTATION, NO_MESH}

    private UUID selectedMesh;
    private MovementType movementType = MovementType.NO_MESH;
    private final MainWindow mainWindow;

    public MeshManipulator(Rendering3DWindow rendering3DWindow, MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.rendering3DWindow = rendering3DWindow;
        this.selectedMesh = null;
        controller = mainWindow.getController();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        try {

            switch (e.getKeyCode()) {
                case KeyEvent.VK_RIGHT:
                    controller.panTransform(rendering3DWindow.getCameraId(), 0, GIMBAL_ROTATION);
                    break;
                case KeyEvent.VK_LEFT:
                    controller.panTransform(rendering3DWindow.getCameraId(), 0, -GIMBAL_ROTATION);
                    break;
                case KeyEvent.VK_UP:
                    controller.panTransform(rendering3DWindow.getCameraId(), GIMBAL_ROTATION, 0);
                    break;
                case KeyEvent.VK_DOWN:
                    controller.panTransform(rendering3DWindow.getCameraId(), -GIMBAL_ROTATION, 0);
                    break;
                case KeyEvent.VK_W:
                    if (movementType == MovementType.TRANSLATION) {
                        controller.applyTransform(selectedMesh, new VertexDTO(0, MESH_TRANSLATION, 0), VertexDTO.zero(), 0);
                    } else if (movementType == MovementType.ROTATION) {
                        controller.applyTransform(selectedMesh, VertexDTO.zero(), new VertexDTO(MESH_ROTATION, 0, 0), 0);
                    }
                    break;
                case KeyEvent.VK_A:
                    if (movementType == MovementType.TRANSLATION) {
                        controller.applyTransform(selectedMesh, new VertexDTO(-MESH_TRANSLATION, 0, 0), VertexDTO.zero(), 0);
                    } else if (movementType == MovementType.ROTATION) {
                        controller.applyTransform(selectedMesh, VertexDTO.zero(), new VertexDTO(0, -MESH_ROTATION, 0), 0);
                    }
                    break;
                case KeyEvent.VK_S:
                    if (movementType == MovementType.TRANSLATION) {
                        controller.applyTransform(selectedMesh, new VertexDTO(0, -MESH_TRANSLATION, 0), VertexDTO.zero(), 0);
                    } else if (movementType == MovementType.ROTATION) {
                        controller.applyTransform(selectedMesh, VertexDTO.zero(), new VertexDTO(-MESH_ROTATION, 0, 0), 0);
                    }
                    break;
                case KeyEvent.VK_D:
                    if (movementType == MovementType.TRANSLATION) {
                        controller.applyTransform(selectedMesh, new VertexDTO(MESH_TRANSLATION, 0, 0), VertexDTO.zero(), 0);
                    } else if (movementType == MovementType.ROTATION) {
                        controller.applyTransform(selectedMesh, VertexDTO.zero(), new VertexDTO(0, MESH_ROTATION, 0), 0);
                    }
                    break;
                case KeyEvent.VK_1:
                    if (movementType != MovementType.NO_MESH) {
                        if (movementType == MovementType.TRANSLATION) {
                            movementType = MovementType.ROTATION;
                        } else {
                            movementType = MovementType.TRANSLATION;
                        }
                    }
                    break;
            }
        } catch (KeyException keyException) {
            keyException.printStackTrace();
        }
        rendering3DWindow.repaint();
    }

    public void setSelectedMesh(UUID selectedMesh) {
        this.selectedMesh = selectedMesh;
        if (movementType == MovementType.NO_MESH) {
            movementType = MovementType.TRANSLATION;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        rendering3DWindow.setMousePos(new VertexDTO(e.getX(), e.getY(), 1));
        rendering3DWindow.repaint();
    }

    // Empty classes to fulfill the listener contract

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

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
}