package UI;

import java.io.Serializable;

public class OptionWrapper implements Serializable {

    private UiUnits units;
    private int rotation;
    private int vitesse;

    public OptionWrapper(OptionWrapper option){
        units = option.units;
        rotation = option.rotation;
        vitesse = option.vitesse;
    }

    public OptionWrapper(){
        this.units = UiUnits.MILLIMETERS;
        this.rotation = 20000;
        this.vitesse = 3;
    }

    @Override
    public String toString() {
        return "Units: " + units + ", Rotation: " + rotation + ", Vitesse: " + vitesse;
    }

    public void setRotation(int rotation){
        this.rotation = rotation;
    }

    public void setVitesse(int vitesse){
        this.vitesse = vitesse;
    }

    public void setUnits(UiUnits units){
        this.units = units;
    }

    public UiUnits getUnits(){
        return units;
    }

    public int getRotation(){
        return rotation;
    }

    public int getVitesse(){
        return vitesse;
    }

}
