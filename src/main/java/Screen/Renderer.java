package Screen;

import javax.swing.*;

import java.awt.*;
import java.util.List;

/**
 * The {@code Renderer} class provides function to render meshes on it, it extends {@code JPanel}.
 *
 * @author Adam Côté
 * @since 2024-09-08
 */
public class Renderer extends JPanel {
    private final Vertex vertexX;
    private final Vertex vertexY;
    private final Vertex vertexZ;
    private List<Mesh> meshes;
    private Vertex mousePos;
    private final BoutonRotation boutonRotation;

    /**
     * @return the {@code List} of {@code Mesh} that need to be rendered
     */
    public List<Mesh> getMeshes() {
        return meshes;
    }

    /**
     * @return the mousePos of this instance of {@code Renderer}
     */
    public Vertex getMousePos() {
        return mousePos;
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
        Mesh mesh = null;
        for (Mesh m : meshes) {
            for (Triangle t : m.getTrianglesList()) {
                if (t.printTriangles(this, graphics2D)) {
                    mesh = m;
                }
            }
        }
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
        Vertex tempVertexX = new Vertex(vertexX);
        tempVertexX.multiplication(translation.getX());
        translationModif.addition(tempVertexX);

        Vertex tempVertexY = new Vertex(vertexY);
        tempVertexY.multiplication(translation.getY());
        translationModif.addition(tempVertexY);

        Vertex tempVertexZ = new Vertex(vertexZ);
        tempVertexZ.multiplication(translation.getZ());
        translationModif.addition(tempVertexZ);
        for (Triangle t : mesh.getTrianglesList()) {
            t.getVertex1().addition(translationModif);
            t.getVertex2().addition(translationModif);
            t.getVertex3().addition(translationModif);
        }
        mesh.setVerticesList();
        mesh.getPosition().addition(translation);
        repaint();
    }

    /**
     * Rotate a specific mesh around it's center
     *
     * @param mesh the mesh to rotate
     * @param axis the axis to turn around
     * @param size the direction of the rotation
     */
    public void rotationMesh(Mesh mesh, Vertex axis, double size) {
        Matrix rotationMatrice = getRotationMatrixAroundVector(axis, size);
        Vertex center = mesh.getCenter();
        for (Triangle t : mesh.getTrianglesList()) {
            t.getVertex1().subtraction(center);
            t.setVertex1(rotationMatrice.matriceXVertex3x3(t.getVertex1()));
            t.getVertex1().addition(center);

            t.getVertex2().subtraction(center);
            t.setVertex2(rotationMatrice.matriceXVertex3x3(t.getVertex2()));
            t.getVertex2().addition(center);

            t.getVertex3().subtraction(center);
            t.setVertex3(rotationMatrice.matriceXVertex3x3(t.getVertex3()));
            t.getVertex3().addition(center);
        }
        mesh.setVerticesList();
        repaint();
    }
//1,0,0,,,0,cos(0.05),-sin(0,05),,,,0,sin(0,05),cos(0,05),

    /**
     * Returns a rotation matrix to apply on a mesh for it to rotate on a certain angle
     *
     * @param v    the axis to rotate around
     * @param size the size of the rotation
     * @return the rotation matrix to apply on the vectors
     */
    private static Matrix getRotationMatrixAroundVector(Vertex v, double size) {
        double ux = v.getX(), uy = v.getY(), uz = v.getZ();
        double c = Math.cos(0.05 * size), s = Math.sin(0.05 * size);
        return new Matrix(new double[]{
                Math.pow(ux, 2) * (1 - c) + c, ux * uy * (1 - c) - uz * s, ux * uz * (1 - c) + uy * s,
                ux * uy * (1 - c) + uz * s, Math.pow(uy, 2) * (1 - c) + c, uy * uz * (1 - c) - ux * s,
                ux * uz * (1 - c) - uy * s, uy * uz * (1 - c) + ux * s, Math.pow(uz, 2) * (1 - c) + c
        });
    }

    /**
     * Getter of the vertexX attribute
     *
     * @return the vertexX attribute
     */
    public Vertex getVertexX() {
        return vertexX;
    }

    /**
     * Getter of the vertexY attribute
     *
     * @return the vertexY attribute
     */
    public Vertex getVertexY() {
        return vertexY;
    }

    /**
     * Getter of the vertexZ attribute
     *
     * @return the vertexZ attribute
     */
    public Vertex getVertexZ() {
        return vertexZ;
    }

}