package UI.Widgets;

import Common.DTO.BitDTO;

import javax.swing.*;
import java.util.Map;
import java.awt.*;

/**
 * The {@code BitChoiceBox} class is a UI class that encapsulate the BitChoiceBox editor (choosing a bit in a drop down menu)
 *  It is used to store the index and the name of the item
 *  So when the user wants to change the bit of a cut, they can choose it in a drop down menu
 *
 * @author Antoine Morin
 * @version 1.0
 */
public class BitChoiceBox extends GenericAttributeBox {

    private JComboBox comboBox;

    /**
     * Constructs a {@code BitChoiceBox} instance initializing all of it's sub-components
     *
     * @param haveBackground boolean to know if the widget have a background
     * @param name the name of the widget
     * @param bitMap a Map of the configured bits
     * @param index the index of the selected bit
     */
    public BitChoiceBox(boolean haveBackground, String name, Map<Integer, BitDTO> bitMap, int index) {
        super(haveBackground, name);
        init(name, bitMap, index);
    }

    /**
     * Initializes the BitChoiceBox
     *
     * @param name the name of the widget
     * @param bitList a Map of the configured bits
     * @param index the index of the selected bit
     */
    private void init(String name, Map<Integer, BitDTO> bitList, int index){
        DefaultComboBoxModel<ComboBitItem> model = new DefaultComboBoxModel<ComboBitItem>();

        for(Map.Entry<Integer, BitDTO> entry : bitList.entrySet()){
            model.addElement(new ComboBitItem(entry.getKey(), entry.getValue().getName()));
        }

        comboBox = new JComboBox(model);
        safeSelect(index, bitList);
        GridBagConstraints gc = new GridBagConstraints();

        gc.gridx = 0; gc.gridy = 1;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 1;
        this.add(comboBox, gc);
    }

    public void refresh(Map<Integer, BitDTO> bitList, int index){
        DefaultComboBoxModel<ComboBitItem> model = new DefaultComboBoxModel<ComboBitItem>();

        for(Map.Entry<Integer, BitDTO> entry : bitList.entrySet()){
            model.addElement(new ComboBitItem(entry.getKey(), entry.getValue().getName()));
        }

        comboBox.setModel(model);
        safeSelect(index, bitList);
    }

    private void safeSelect(int index, Map<Integer, BitDTO> bitList){
        if(bitList.containsKey(index)){
            for(int i =0; i < comboBox.getItemCount(); i++){
                ComboBitItem comboBitItem = (ComboBitItem) comboBox.getItemAt(i);
                if(comboBitItem.getIndex() == index){
                    comboBox.setSelectedIndex(i);
                    comboBox.setBackground(null);
                    return;
                }
            }
        }

        comboBox.setSelectedIndex(-1); // invalid cut
        comboBox.setBackground(Color.RED);

    }

    /**
     * Returns the JComboBox of the BitChoiceBox
     *
     * @return the JComboBox of the BitChoiceBox
     */
    public JComboBox getComboBox(){return this.comboBox;}
}
