package UI.Widgets;

import Domain.DTO.CutDTO;
import Domain.Cut;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.LabelUI;
import java.awt.*;

/**
 * The {@code CutBox} class is a UI class that encapsulates a box containing all the informations
 * about a specific cut
 *
 * @author Louis-Etienne Messier
 * @version 0.1
 * @since 2024-09-21
 */
public class CutBox {
    private CutDTO cut;
    private JPanel panel;
    private GridBagLayout layout;
    private JLabel bitnameLabel;
    private JLabel numberLabel;
    private JLabel coordinateLabel;
    private ImageWidget imageWidget;

    public CutBox(CutDTO cutDTO){
        this.cut = cutDTO;
        this.init();
        this.updatePanel(this.cut);
    }

    private void init(){

        layout = new GridBagLayout();
        panel = new JPanel(layout);

        panel.setBorder(new EmptyBorder(5,5,5,5));
        GridBagConstraints gc = new GridBagConstraints();
        bitnameLabel = new JLabel("Bitname placeholder");
        numberLabel = new JLabel("Number placeholder");
        coordinateLabel = new JLabel("(Coordinates label) - (Placeholder)");
        imageWidget = new ImageWidget(" l", 100, 100);

        gc.gridx = 0; gc.gridy = 0;
        gc.gridwidth =1; gc.gridheight=1;
        panel.add(numberLabel, gc);

        gc.gridx = 0; gc.gridy = 1;
        gc.gridwidth =1; gc.gridheight=1;
        panel.add(imageWidget, gc);

        gc.gridx = 1; gc.gridy = 0;
        gc.gridwidth =3; gc.gridheight=1;
        panel.add(bitnameLabel, gc);

        gc.gridx = 1; gc.gridy = 1;
        gc.gridwidth =3; gc.gridheight=1;
        panel.add(coordinateLabel, gc);
    }

    public void updatePanel(CutDTO newCutDTO){
        this.cut = newCutDTO;

    }

    public JPanel getPanel(){
        return this.panel;
    }

}
