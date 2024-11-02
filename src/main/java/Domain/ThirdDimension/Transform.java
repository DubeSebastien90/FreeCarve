package Domain.ThirdDimension;

import java.security.InvalidKeyException;
import java.util.UUID;

/**
 * Parent of all classes that have a transform, which is a position, rotation and scale
 *
 * @author Kamran Charles Nayebi
 * @since 2024-10-31
 */
public abstract class Transform {
    private Vertex position;
    private float scale;
    private Vertex rotationEuler;
    private Quaternion rotationQuaternion;
    private UUID id;

    Transform(Vertex position, float scale, Vertex rotationEuler) {
        this.position = position;
        this.scale = scale;
       setRotationEuler(rotationEuler);
       id = UUID.randomUUID();
    }

    public Vertex getRotationEuler() {
        return rotationEuler;
    }

    public void setRotationEuler(Vertex rotationEuler) {
        if(rotationEuler.getX() >= 2*Math.PI)
            rotationEuler.setX(Math.asin(Math.sin(rotationEuler.getX())));
        if(rotationEuler.getY() >= 2*Math.PI)
            rotationEuler.setY(Math.asin(Math.sin(rotationEuler.getY())));
        if(rotationEuler.getZ() >= 2*Math.PI)
            rotationEuler.setZ(Math.asin(Math.sin(rotationEuler.getZ())));
        this.rotationEuler = rotationEuler;
        this.rotationQuaternion = Quaternion.fromEulerAngles(this.rotationEuler);
    }

    public Vertex getPosition() {
        return position;
    }

    public void setPosition(Vertex position) {
        this.position = position;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    /**
     * Returns a new rotated translated and scaled version of the triangle
     * @param triangle original triangle
     * @return new rotated translated and scaled triangle
     */
    public Triangle getTransformedTriangle(Triangle triangle) {
        Vertex[] vertices = new Vertex[triangle.getVertices().length];
        for(int i = 0; i < vertices.length; i++){
            Vertex v = new Vertex(triangle.getVertices()[i]);
            v.rotate(rotationQuaternion);
            v.add(position);
            v.multiply(scale);
            vertices[i] = v;
        }
        Vertex normal = new Vertex(triangle.getNormal()).rotate(rotationQuaternion);
        return new Triangle(vertices[0], vertices[1], vertices[2], normal, triangle.getColor());
    }

    /**
     * Rotates the {@code Transform} around the origin as if it were on a gimbal.
     *
     * @param xAxisRotation The amount of rotation in rad to apply around the X axis
     * @param yAxisRotation The amount of rotation in rad to apply around the Y axis
     */
    public void pan(float xAxisRotation, float yAxisRotation){
        position.rotate(Quaternion.fromEulerAngles(new Vertex(xAxisRotation, yAxisRotation, 0)));
        rotationEuler.add(new Vertex(xAxisRotation, yAxisRotation, 0));
        rotationQuaternion = Quaternion.fromEulerAngles(rotationEuler);
    }

    public UUID getId() {
        return id;
    }
}
