package Domain.ThirdDimension;

public abstract class Transform {
    private Vertex position;
    private float scale;
    private Vertex rotationEuler;
    private Quaternion rotationQuaternion;

    Transform(Vertex position, float scale, Vertex rotationEuler) {
        this.position = position;
        this.scale = scale;
       setRotationEuler(rotationEuler);
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
}
