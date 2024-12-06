package Common;

/**
 * Defines conversions between units
 * @author Kamran Charles Nayebi
 * @since 2024-11-16
 */
public enum Units {
    MM(1), CM(10), M(1000), INCH(25.4), FEET(304.8);

    final double ratio;

    Units(double ratioToMM){
        ratio = ratioToMM;
    }

    public double getRatio(){
        return ratio;
    }

    public double getInverseRatio(){
        return 1/ratio;
    }
}