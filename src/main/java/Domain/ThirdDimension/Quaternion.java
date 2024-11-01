package Domain.ThirdDimension;

public class Quaternion {
    private double w;
    private double x;
    private double y;
    private double z;

    public Quaternion(double w, double x, double y, double z) {
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Quaternion(Quaternion q) {
        this.w = q.w;
        this.x = q.x;
        this.y = q.y;
        this.z = q.z;
    }

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

    public Quaternion multiply(double s) {
        setW(w*s);
        setX(x*s);
        setY(y*s);
        setZ(z*s);
        return this;
    }

    public double length() {
        return Math.sqrt(w*w + x*x + y*y + z*z);
    }
    
    public Quaternion normalize() {
        return multiply(1.0 / length());
    }

    public Quaternion congugate() {
        setX(-x);
        setY(-y);
        setZ(-z);
        return this;
    }

    public static Quaternion fromEulerAngles(Vertex eulerAngles) {
        Quaternion roll = new Quaternion(Math.cos(eulerAngles.getX()/2), Math.sin(eulerAngles.getX()/2), 0, 0);
        Quaternion pitch = new Quaternion(Math.cos(eulerAngles.getY()/2), 0, Math.sin(eulerAngles.getY()/2), 0);
        Quaternion yaw = new Quaternion(Math.cos(eulerAngles.getZ()/2), 0, 0, Math.sin(eulerAngles.getZ()/2));

        return yaw.multiply(pitch).multiply(roll).normalize();
    }
}
