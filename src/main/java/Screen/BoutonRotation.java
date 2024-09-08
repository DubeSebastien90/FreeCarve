package Screen;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BoutonRotation implements KeyListener {

    private final TheDessinator theDessinator;

    private final Matrice RIGHT_ROTATION = new Matrice(new double[]{
            Math.cos(0.05), 0, -Math.sin(0.05),
            0, 1, 0,
            Math.sin(0.05), 0, Math.cos(0.05)
    });
    private final Matrice LEFT_ROTATION = new Matrice(new double[]{
            Math.cos(-0.05), 0, -Math.sin(-0.05),
            0, 1, 0,
            Math.sin(-0.05), 0, Math.cos(-0.05)
    });
    private final Matrice UP_ROTATION = new Matrice(new double[]{
            1, 0, 0,
            0, Math.cos(0.05), Math.sin(0.05),
            0, -Math.sin(0.05), Math.cos(0.05)
    });
    private final Matrice DOWN_ROTATION = new Matrice(new double[]{
            1, 0, 0,
            0, Math.cos(-0.05), Math.sin(-0.05),
            0, -Math.sin(-0.05), Math.cos(-0.05)
    });

    public BoutonRotation(TheDessinator theDessinator) {
        this.theDessinator = theDessinator;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        ExecutorService executors = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        executors.submit(() -> {
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                rotationCurrentShape(RIGHT_ROTATION);
            } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                rotationCurrentShape(LEFT_ROTATION);
            } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                rotationCurrentShape(UP_ROTATION);
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                rotationCurrentShape(DOWN_ROTATION);
            } else if (e.getKeyCode() == KeyEvent.VK_W){
                System.out.println("hey");
            }
        });
        executors.shutdown();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void translationMesh(Mesh mesh) {

    }

    public void rotationCurrentShape(Matrice rotationMatrice) {
        for (Mesh m : theDessinator.getMeshes()) {
            for (Triangle t : m.getTrianglesList()) {
                Vertex new1 = rotationMatrice.matriceXVertex3x3(t.getVertex1());
                Vertex new2 = rotationMatrice.matriceXVertex3x3(t.getVertex2());
                Vertex new3 = rotationMatrice.matriceXVertex3x3(t.getVertex3());
                t.setVertex1(new1);
                t.setVertex2(new2);
                t.setVertex3(new3);
            }
        }
        theDessinator.repaint();
    }
}
