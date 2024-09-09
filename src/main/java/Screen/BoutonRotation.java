package Screen;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BoutonRotation implements KeyListener, MouseListener {

    private final Renderer renderer;
    private Mesh selectedMesh;
    private int movementType = 0; //0-translation, 1-rotation

    private final Matrix RIGHT_ROTATION = new Matrix(new double[]{
            Math.cos(0.05), 0, -Math.sin(0.05),
            0, 1, 0,
            Math.sin(0.05), 0, Math.cos(0.05)
    });
    private final Matrix LEFT_ROTATION = new Matrix(new double[]{
            Math.cos(-0.05), 0, -Math.sin(-0.05),
            0, 1, 0,
            Math.sin(-0.05), 0, Math.cos(-0.05)
    });
    private final Matrix UP_ROTATION = new Matrix(new double[]{
            1, 0, 0,
            0, Math.cos(0.05), Math.sin(0.05),
            0, -Math.sin(0.05), Math.cos(0.05)
    });
    private final Matrix DOWN_ROTATION = new Matrix(new double[]{
            1, 0, 0,
            0, Math.cos(-0.05), Math.sin(-0.05),
            0, -Math.sin(-0.05), Math.cos(-0.05)
    });

    public BoutonRotation(Renderer renderer) {
        this.renderer = renderer;
        this.selectedMesh = null;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        ExecutorService executors = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        executors.submit(() -> {

            switch (e.getKeyCode()) {
                case KeyEvent.VK_RIGHT:
                    renderer.rotationCurrentShape(RIGHT_ROTATION);
                    break;
                case KeyEvent.VK_LEFT:
                    renderer.rotationCurrentShape(LEFT_ROTATION);
                    break;
                case KeyEvent.VK_UP:
                    renderer.rotationCurrentShape(UP_ROTATION);
                    break;
                case KeyEvent.VK_DOWN:
                    renderer.rotationCurrentShape(DOWN_ROTATION);
                    break;
                case KeyEvent.VK_W:
                    if (movementType == 0) {
                        renderer.translationMesh(selectedMesh, new Vertex(0, -3, 0));
                    } else {
                        renderer.rotationMesh(selectedMesh, UP_ROTATION);
                    }
                    break;
                case KeyEvent.VK_A:
                    if (movementType == 0) {
                        renderer.translationMesh(selectedMesh, new Vertex(-3, 0, 0));
                    } else {
                        renderer.rotationMesh(selectedMesh, LEFT_ROTATION);
                    }
                    break;
                case KeyEvent.VK_S:
                    if (movementType == 0) {
                        renderer.translationMesh(selectedMesh, new Vertex(0, 3, 0));
                    } else {
                        renderer.rotationMesh(selectedMesh, DOWN_ROTATION);
                    }
                    break;
                case KeyEvent.VK_D:
                    if (movementType == 0) {
                        renderer.translationMesh(selectedMesh, new Vertex(3, 0, 0));
                    } else {
                        renderer.rotationMesh(selectedMesh, RIGHT_ROTATION);
                    }
                    break;
                case KeyEvent.VK_SPACE:
                    if (movementType == 0) {
                        renderer.translationMesh(selectedMesh, new Vertex(0, 0, 3));
                    }
                    break;
                case KeyEvent.VK_SHIFT:
                    if (movementType == 0) {
                        renderer.translationMesh(selectedMesh, new Vertex(0, 0, -3));
                    }
                    break;
                case KeyEvent.VK_Q:
                    if (movementType == 0) {
                        movementType = 1;
                    } else {
                        movementType = 0;
                    }
                    break;
            }
        });
        executors.shutdown();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void setSelectedMesh(Mesh selectedMesh) {
        this.selectedMesh = selectedMesh;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        renderer.setMousePos(new Vertex(e.getX(), e.getY(), 1));
        renderer.repaint();
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
