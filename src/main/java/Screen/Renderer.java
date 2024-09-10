package Screen;

import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The {@code Renderer} class provides function to render meshes on it, it extends {@code JPanel}.
 *
 * @author Adam Côté
 * @since 2024-09-08
 */
public class Renderer extends JPanel {
    private Vertex vertexX;
    private Vertex vertexY;
    private Vertex vertexZ;
    private List<Mesh> meshes;
    private Vertex mousePos;
    private BoutonRotation boutonRotation;

    /**
     * @return the {@code List} of {@code Mesh} that need to be rendered
     */
    public List<Mesh> getMeshes() {
        return meshes;
    }

    /**
     * Sets the {@code List} of {@code Mesh} that needs to be rendered to a new one
     *
     * @param meshes the new {@code List} of {@code Mesh}
     */
    public void setMeshes(List<Mesh> meshes) {
        this.meshes = meshes;
    }

    /**
     * Constructs a new {@code Renderer} object with a {@code List} of {@code Mesh} and initialize {@code Renderer}
     *
     * @param meshes the {@code List} of {@code Mesh}
     */
    public Renderer(List<Mesh> meshes) {
        setDoubleBuffered(true);
        setFocusable(true);
        requestFocusInWindow();
        requestFocus();
        this.vertexX = new Vertex(1, 0, 0);
        this.vertexY = new Vertex(0, 1, 0);
        this.vertexZ = new Vertex(0, 0, 1);
        setMeshes(meshes);
        boutonRotation = new BoutonRotation(this);
        addKeyListener(boutonRotation);
        addMouseListener(boutonRotation);
        this.mousePos = new Vertex(0, 0, 0);
    }

    /**
     * Sets the mouse position variable at a specific {@code Vertex}
     *
     * @param mousePos the position of the mouse
     */
    public void setMousePos(Vertex mousePos) {
        this.mousePos = mousePos;
    }

    /**
     * Paint the {@code List} of {@code Mesh} on a {@code Graphics2D} and then draw it on the {@code Renderer}
     *
     * @param graphics a {@code Graphics} object on which the meshes will be drawn
     */
    @Override
    public void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = ((Graphics2D) graphics);
        this.setBackground(Color.GRAY);
        super.paintComponent(graphics2D);
        Mesh mesh = Triangle.printTriangles(this, graphics2D, meshes, mousePos);
        if (mousePos.getZ() == 1) {
            boutonRotation.setSelectedMesh(mesh);
        }
        setMousePos(new Vertex(0, 0, 0));
    }

    /**
     * Rotate all of the meshes around the (0,0,0) point
     *
     * @param rotationMatrice - the rotation matrix
     */
    public void rotationCurrentShape(Matrix rotationMatrice) {
        for (Mesh m : meshes) {
            for (Triangle t : m.getTrianglesList()) {
                t.setVertex1(rotationMatrice.matriceXVertex3x3(t.getVertex1()));
                t.setVertex2(rotationMatrice.matriceXVertex3x3(t.getVertex2()));
                t.setVertex3(rotationMatrice.matriceXVertex3x3(t.getVertex3()));
                t.setNormal(rotationMatrice.matriceXVertex3x3(t.getNormal()));
            }
            m.setVerticesList();
        }
        vertexX.setVertex(rotationMatrice.matriceXVertex3x3(vertexX));
        vertexY.setVertex(rotationMatrice.matriceXVertex3x3(vertexY));
        vertexZ.setVertex(rotationMatrice.matriceXVertex3x3(vertexZ));
        repaint();
    }

    /**
     * Move a specific mesh in the scene
     *
     * @param mesh        - the mesh to move
     * @param translation - the movement vector
     */
    public void translationMesh(Mesh mesh, Vertex translation) {
        Vertex translationModif = new Vertex(0, 0, 0);
        translationModif = translationModif.addition(vertexX.multiplication(translation.getX()));
        translationModif = translationModif.addition(vertexY.multiplication(translation.getY()));
        translationModif = translationModif.addition(vertexZ.multiplication(translation.getZ()));
        for (Triangle t : mesh.getTrianglesList()) {
            t.setVertex1(t.getVertex1().addition(translationModif));
            t.setVertex2(t.getVertex2().addition(translationModif));
            t.setVertex3(t.getVertex3().addition(translationModif));
        }
        mesh.setVerticesList();
        mesh.setPosition(mesh.getPosition().addition(translation));
        repaint();
    }

    /**
     * Rotate a specific mesh around it's center
     *
     * @param mesh            - the mesh to rotate
     * @param rotationMatrice - the rotation matrix
     */
    public void rotationMesh(Mesh mesh, Matrix rotationMatrice) {
        Vertex center = mesh.getCenter();
        for (Triangle t : mesh.getTrianglesList()) {
            t.setVertex1(rotationMatrice.matriceXVertex3x3(t.getVertex1().substraction(center)).addition(center));
            t.setVertex2(rotationMatrice.matriceXVertex3x3(t.getVertex2().substraction(center)).addition(center));
            t.setVertex3(rotationMatrice.matriceXVertex3x3(t.getVertex3().substraction(center)).addition(center));
            t.setNormal(rotationMatrice.matriceXVertex3x3(t.getNormal().substraction(center)).addition(center));
        }
        mesh.setVerticesList();
        repaint();
    }

}
