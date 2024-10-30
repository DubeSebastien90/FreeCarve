package UI.Widgets;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * The {@code ChoiceBox} class is a UI class that encapsulate the
 * ChoiceBox editor (choosing an element in a drop down menu)
 *
 * @author Louis-Etienne Messier
 * @version 0.1
 * @since 2024-10-23
 */
public class ChoiceBox extends GenericAttributeBox{

    private JComboBox comboBox;

    public ChoiceBox(boolean haveBackground, String name, ArrayList<JLabel> labels, int index) {
        super(haveBackground, name);
        this.init(name, labels, index);
    }

    private void init(String name, ArrayList<JLabel> labels, int index){
        Integer[] inArray = new Integer[labels.size()];
        for(int i =0; i < inArray.length; i++){
            inArray[i] = i;
        }
        comboBox = new JComboBox(inArray);
        IconComboBoxRenderer renderer = new IconComboBoxRenderer(labels);
        comboBox.setRenderer(renderer);
        comboBox.setSelectedIndex(index);
        GridBagConstraints gc = new GridBagConstraints();

        gc.gridx = 0; gc.gridy = 1;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 1;
        this.add(comboBox, gc);
    }

    public JComboBox getComboBox(){return this.comboBox;}
}
