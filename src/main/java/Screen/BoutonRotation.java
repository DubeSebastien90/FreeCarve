package Screen;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BoutonRotation implements KeyListener {

    private final TheDessinator theDessinator;
    private Vertex vertexX = new Vertex(1,0,0);
    private Vertex vertexY = new Vertex(0,1,0);
    private Vertex vertexZ = new Vertex(0,0,1);

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
                translationMesh(theDessinator.getMeshes().get(0), new Vertex(1,0,0));
            }
        });
        executors.shutdown();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void translationMesh(Mesh mesh, Vertex translation) {
        for(Triangle t : mesh.getTrianglesList()){
            t.setVertex1(t.getVertex1().addition(translation));
            t.setVertex2(t.getVertex2().addition(translation));
            t.setVertex3(t.getVertex3().addition(translation));
        }
        theDessinator.repaint();
    }

    public void rotationCurrentShape(Matrice rotationMatrice) {
        for (Mesh m : theDessinator.getMeshes()) {
            for (Triangle t : m.getTrianglesList()) {
                t.setVertex1(rotationMatrice.matriceXVertex3x3(t.getVertex1()));
                t.setVertex2(rotationMatrice.matriceXVertex3x3(t.getVertex2()));
                t.setVertex3(rotationMatrice.matriceXVertex3x3(t.getVertex3()));
            }
        }
        System.out.println(theDessinator.getMeshes().get(0).getTrianglesList().get(0).getVertex2());
        theDessinator.repaint();
    }
}
