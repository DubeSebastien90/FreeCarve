package UI.Widgets.AttributeContainer;

import Common.DTO.BitDTO;
import Common.DTO.CutDTO;
import Common.DTO.VertexDTO;
import Common.UiUtil;
import Domain.CutType;
import UI.Events.ChangeAttributeEvent;
import UI.MainWindow;
import UI.SubWindows.CutListPanel;
import UI.UIConfig;
import UI.Widgets.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Map;

public class AttributeContainerVertical extends AttributeContainer {
    PointsBox pointsBox1;
    PointsBox pointsBox2;
    SingleValueBox depthBox;
    BitChoiceBox bitChoiceBox;
    ChoiceBox cuttypeBox;

    public AttributeContainerVertical(MainWindow mainWindow, CutListPanel cutListPanel, CutDTO cutDTO, CutBox cutBox) {
        super(mainWindow, cutListPanel, cutDTO, cutBox);
        init_layout();
        init_attribute(mainWindow, cutDTO);

    }

    private void init_layout(){
        setBackground(null);
        setOpaque(false);
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gc = new GridBagConstraints();
        setLayout(layout);
        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(pointsBox1, gc);

        gc.gridx = 0;
        gc.gridy = 1;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(pointsBox2, gc);

        gc.gridx = 0;
        gc.gridy = 2;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(depthBox, gc);

        gc.gridx = 0;
        gc.gridy = 3;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(bitChoiceBox, gc);

        gc.gridx = 0;
        gc.gridy = 4;
        gc.insets = new Insets(0, 0, 0, 0);
        add(cuttypeBox, gc);
    }

    /**
     * Initialize variables relevant to the Attribute Panel
     */
    private void init_attribute(MainWindow mainWindow, CutDTO cutDTO) {
        pointsBox1 = new PointsBox(true, "Point1", cutDTO.getPoints().get(0));
        pointsBox2 = new PointsBox(true, "Point2", cutDTO.getPoints().get(1));
        depthBox = new SingleValueBox(true, "Profondeur", cutDTO.getDepth());

        Map<Integer, BitDTO> configuredBitsMap = mainWindow.getMiddleContent().getConfiguredBitsMap();

        int index = 0;
        for(Map.Entry<Integer, BitDTO> entry : configuredBitsMap.entrySet()){
            if(entry.getKey().equals(cutDTO.getBitIndex())){
                break;
            }
            index++;
        }
        bitChoiceBox = new BitChoiceBox(true, "Outil", configuredBitsMap, index);

        ArrayList<JLabel> labelList = new ArrayList<>();
        for (CutType t : CutType.values()) {
            JLabel l = new JLabel(UiUtil.getIcon(UiUtil.getIconFileName(t), UIConfig.INSTANCE.getCutBoxIconSize(),
                    UIManager.getColor("button.Foreground")));
            l.setText(UiUtil.getIconName(t));
            labelList.add(l);
        }
        cuttypeBox = new ChoiceBox(true, "Type de coupe", labelList, cutDTO.getCutType().ordinal());
    }

    @Override
    public void setupEventListeners() {
        addEventListenerToPointBox(pointsBox1, 0);
        addEventListenerToPointBox(pointsBox2, 1);
        addEventListenerToSingleValue(depthBox);
        addEventListenerToBitChoiceBox(bitChoiceBox);
        addEventListenerToChoiceBox(cuttypeBox);
    }

    /**
     * Adding the custom event listeners to PointBox objects. The goal is to make
     * the Point attribute react to change events
     *
     * @param pb    {@code PointBox object}
     * @param index index of the PointBox
     */
    private void addEventListenerToPointBox(PointsBox pb, int index) {

        // Listen for changes in the X field
        pb.getxInput().getNumericInput().addPropertyChangeListener("value", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                CutDTO c = cutDTO;
                VertexDTO oldVertex = c.getPoints().get(index);
                Number n = (Number) evt.getNewValue();
                VertexDTO newVertex = new VertexDTO(n.doubleValue(), oldVertex.getY(), oldVertex.getZ());
                c.getPoints().set(index, newVertex);
                mainWindow.getController().modifyCut(c);
                cutListPanel.modifiedAttributeEventOccured(new ChangeAttributeEvent(this, cutBox));
            }
        });
        // Listen for changes in the Y field
        pb.getyInput().getNumericInput().addPropertyChangeListener("value", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                CutDTO c = cutDTO;
                VertexDTO oldVertex = c.getPoints().get(index);
                Number n = (Number) evt.getNewValue();
                VertexDTO newVertex = new VertexDTO(oldVertex.getX(), n.doubleValue(), oldVertex.getZ());
                c.getPoints().set(index, newVertex);
                mainWindow.getController().modifyCut(c);
                cutListPanel.modifiedAttributeEventOccured(new ChangeAttributeEvent(this, cutBox));
            }
        });
    }

    /**
     * Adding the custom event listeners to SingleValueBox objects. The goal is to make
     * the Value attribute react to change events
     *
     * @param sb {@code SingleValueBox object}
     */
    private void addEventListenerToSingleValue(SingleValueBox sb) {
        sb.getInput().getNumericInput().addPropertyChangeListener("value", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                CutDTO c = cutDTO;
                Number n = (Number) evt.getNewValue();
                c = new CutDTO(c.getId(), n.doubleValue(), c.getBitIndex(), c.getCutType(), c.getPoints(), c.getRefsDTO());
                mainWindow.getController().modifyCut(c);
                cutListPanel.modifiedAttributeEventOccured(new ChangeAttributeEvent(this, cutBox));
            }
        });
    }

    /**
     * Adding the custom event listeners to ChoiceBox objects. The goal is to make
     * the ComboBox attribute react to change events
     *
     * @param cb {@code ChoiceBox object}
     */
    private void addEventListenerToChoiceBox(ChoiceBox cb) {
        cb.getComboBox().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox comboBox = (JComboBox) e.getSource();
                CutDTO c = cutDTO;
                CutType chosenCutType = CutType.values()[comboBox.getSelectedIndex()];
                c = new CutDTO(c.getId(), c.getDepth(), c.getBitIndex(), chosenCutType, c.getPoints(), c.getRefsDTO());
                mainWindow.getController().modifyCut(c);
                cutListPanel.modifiedAttributeEventOccured(new ChangeAttributeEvent(this, cutBox));
            }
        });
    }



    /**
     * Adding the custom event listeners to BitChoiceBox objects. The goal is to make
     * the ComboBox attribute react to change events
     *
     * Called when the user selects a new bit in the BitChoiceBox
     *
     * @param cb {@code BitChoiceBox object} The combo box containing the bits informations
     */
    private void addEventListenerToBitChoiceBox(BitChoiceBox cb) {
        cb.getComboBox().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox comboBox = (JComboBox) e.getSource();
                CutDTO c = cutDTO;
                ComboBitItem chosenBit = (ComboBitItem) comboBox.getModel().getSelectedItem();
                c = new CutDTO(c.getId(), c.getDepth(), chosenBit.getIndex(), c.getCutType(), c.getPoints(), c.getRefsDTO());
                mainWindow.getController().modifyCut(c);
                cutListPanel.modifiedAttributeEventOccured(new ChangeAttributeEvent(this, cutBox));
            }
        });
    }

}
