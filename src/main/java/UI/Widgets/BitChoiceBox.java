package UI.Widgets;

import Common.BitDTO;

import javax.swing.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;
import java.awt.*;

public class BitChoiceBox extends GenericAttributeBox {

    private JComboBox comboBox;
    public BitChoiceBox(boolean haveBackground, String name, Map<Integer, BitDTO> bitList, int index) {
        super(haveBackground, name);
        init(name, bitList, index);
    }

    private void init(String name, Map<Integer, BitDTO> bitList, int index){
        DefaultComboBoxModel<ComboBitItem> model = new DefaultComboBoxModel<ComboBitItem>();

        for(Map.Entry<Integer, BitDTO> entry : bitList.entrySet()){
            model.addElement(new ComboBitItem(entry.getKey(), entry.getValue().getName()));
        }

        comboBox = new JComboBox(model);
        comboBox.setSelectedIndex(index);
        GridBagConstraints gc = new GridBagConstraints();

        gc.gridx = 0; gc.gridy = 1;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 1;
        this.add(comboBox, gc);
    }

    public JComboBox getComboBox(){return this.comboBox;}
}
