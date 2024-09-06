package Screen;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BoutonRotation implements KeyListener {

    private TheDessinator theDessinator;

    public BoutonRotation(TheDessinator theDessinator) {
        this.theDessinator = theDessinator;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        ExecutorService executors = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        executors.submit(() ->{
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            Matrice rotation = new Matrice(new double[]{
                    Math.cos(0.05), 0, -Math.sin(0.05),
                    0, 1, 0,
                    Math.sin(0.05), 0, Math.cos(0.05)
            });
            rotationCurrentShape(rotation);
        }
        else if(e.getKeyCode() == KeyEvent.VK_LEFT){
            Matrice rotation = new Matrice(new double[]{
                    Math.cos(-0.05), 0, -Math.sin(-0.05),
                    0, 1, 0,
                    Math.sin(-0.05), 0, Math.cos(-0.05)
            });
            rotationCurrentShape(rotation);
        }
        else if(e.getKeyCode() == KeyEvent.VK_UP){
            Matrice rotation = new Matrice(new double[]{
                    1, 0, 0,
                    0, Math.cos(0.05), Math.sin(0.05),
                    0, -Math.sin(0.05), Math.cos(0.05)
            });
            rotationCurrentShape(rotation);
        }
        else if(e.getKeyCode() == KeyEvent.VK_DOWN){
            Matrice rotation = new Matrice(new double[]{
                    1, 0, 0,
                    0, Math.cos(-0.05), Math.sin(-0.05),
                    0, -Math.sin(-0.05), Math.cos(-0.05)
            });

            rotationCurrentShape(rotation);
        }});
        executors.shutdown();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


    public void rotationCurrentShape(Matrice rotationMatrice) {
        for (int i = 0; i < theDessinator.getCurrentShape().size(); i++) {
            Vertex new1 = rotationMatrice.matriceXVertex3x3(theDessinator.getCurrentShape().get(i).getVertex1());
            Vertex new2 = rotationMatrice.matriceXVertex3x3(theDessinator.getCurrentShape().get(i).getVertex2());
            Vertex new3 = rotationMatrice.matriceXVertex3x3(theDessinator.getCurrentShape().get(i).getVertex3());
            theDessinator.getCurrentShape().set(i, new Triangle(new1, new2, new3, new Vertex(0,0,0), theDessinator.getCurrentShape().get(i).getColor()));
        }
        theDessinator.repaint();
    }
}
