package Domain.ThirdDimension;

/**
 * 4D vector that prevents numerical problems like gimbal lock when rotating 3D objects.
 * Replaces matrices for rotation which makes it more space efficient, we don't use the GPU anyway so the matrix boost isn't lost
 *
 * @author Kamran Charles Nayebi
 * @since 2024-10-31
 */
public class Quaternion {
    private double w;
    private double x;
    private double y;
    private double z;

    /**
     * @param w real part
     * @param x imaginary part
     * @param y imaginary part
     * @param z imaginary part
     */
    public Quaternion(double w, double x, double y, double z) {
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Copy constructor
     * @param q Quaternion to copy
     */
    public Quaternion(Quaternion q) {
        this.w = q.w;
        this.x = q.x;
        this.y = q.y;
        this.z = q.z;
    }

    /**
     * Builds a quaternion from a vector using it as the imaginary part
     * @param v Vertex representing the imaginary part
     */
    public Quaternion(Vertex v) {
        this(0, v.getX(), v.getY(), v.getZ());
    }

    public double getW() {
        return w;
    }

    public void setW(double w) {
        this.w = w;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    /**
     * Multiplies self with another quaternion
     * @param q the quaternion to be multiplied by
     * @return self for method call chaining
     */
    public Quaternion multiply(Quaternion q) {
        double newW = w * q.w - x * q.x - y * q.y - z * q.z;
        double newX = w * q.x + x * q.w + y * q.z - z * q.y;
        double newY = w * q.y - x * q.z + y * q.w + z * q.x;
        double newZ = w * q.z + x * q.y - y * q.x + z * q.w;
        setW(newW);
        setX(newX);
        setY(newY);
        setZ(newZ);
        return this;
    }

    /**
     * Multiplies self with a scalar
     * @param s the scalar
     * @return self for method call chaining
     */
    public Quaternion multiply(double s) {
        setW(w*s);
        setX(x*s);
        setY(y*s);
        setZ(z*s);
        return this;
    }

    /**
     * Calculates the norm or length of self
     * @return length of self
     */
    public double length() {
        return Math.sqrt(w*w + x*x + y*y + z*z);
    }

    /**
     * Changes wxyz to have a length of one
     * @return self for method call chaining
     */
    public Quaternion normalize() {
        return multiply(1.0 / length());
    }

    /**
     * Flips the imaginary part of self
     * @return self for method call chaining
     */
    public Quaternion conjugate() {
        setX(-x);
        setY(-y);
        setZ(-z);
        return this;
    }

    /**
     * Creates a quaternion representation of a rotation from a euler angle vector representation of a rotation
     * @param eulerAngles Vector representing the rotation of the object.
     *      The value of X is the rotation around the X axis in radians, the same goes for Y and Z
     * @return a quaternion representation of the rotation
     */
    public static Quaternion fromEulerAngles(Vertex eulerAngles) {
        Quaternion roll = new Quaternion(Math.cos(eulerAngles.getX()/2), Math.sin(eulerAngles.getX()/2), 0, 0);
        Quaternion pitch = new Quaternion(Math.cos(eulerAngles.getY()/2), 0, Math.sin(eulerAngles.getY()/2), 0);
        Quaternion yaw = new Quaternion(Math.cos(eulerAngles.getZ()/2), 0, 0, Math.sin(eulerAngles.getZ()/2));

        return yaw.multiply(pitch).multiply(roll).normalize();
    }
}
