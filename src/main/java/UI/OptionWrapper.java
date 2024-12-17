package UI;

import java.awt.*;
import java.io.Serializable;

public class OptionWrapper implements Serializable {

    private UiUnits units;
    private int rotation;
    private int vitesse;
    private Color panelColor;

    public OptionWrapper(OptionWrapper option){
        units = option.units;
        rotation = option.rotation;
        vitesse = option.vitesse;
    }

    public OptionWrapper(){
        this.units = UiUnits.MILLIMETERS;
        this.rotation = 20000;
        this.vitesse = 3;
        this.panelColor = UIConfig.INSTANCE.getPANEL_COLOR();
    }

    @Override
    public String toString() {
        return "Units: " + units + ", Rotation: " + rotation + ", Vitesse: " + vitesse + ", PanelColor: " + panelColor;
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

    public void setPanelColor(Color color){
        this.panelColor = color;
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

    public Color getPanelColor(){
        return panelColor;
    }

}
