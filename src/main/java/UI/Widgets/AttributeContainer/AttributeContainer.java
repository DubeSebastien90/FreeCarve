package UI.Widgets.AttributeContainer;

import Common.CutState;
import Common.DTO.BitDTO;
import Common.DTO.CutDTO;
import Domain.CutType;
import UI.Display2D.Drawing;
import UI.Events.ChangeAttributeEvent;
import UI.MainWindow;
import UI.SubWindows.BasicWindow;
import UI.SubWindows.CutListPanel;
import UI.UIConfig;
import UI.UiUtil;
import UI.Widgets.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Map;

public abstract class AttributeContainer extends BasicWindow {

    protected MainWindow mainWindow;
    protected CutListPanel cutListPanel;
    protected CutDTO cutDTO;
    protected CutBox cutBox;

    SingleValueBox depthBox;
    BitChoiceBox bitChoiceBox;
    ButtonBox modifyAnchorBox;


    public AttributeContainer(MainWindow mainWindow, CutListPanel cutListPanel, CutDTO cutDTO, CutBox cutBox) {
        super(true);
        this.mainWindow = mainWindow;
        this.cutListPanel = cutListPanel;
        this.cutDTO = cutDTO;
        this.cutBox = cutBox;
    }

    public abstract void setupEventListeners();
    public abstract void updatePanel(CutDTO newCutDTO);


    protected void init_attribute(){

        depthBox = new SingleValueBox(mainWindow, true, "Profondeur", "Profondeur", cutDTO.getDepth());

        Map<Integer, BitDTO> configuredBitsMap = mainWindow.getController().getConfiguredBitsMap();


        bitChoiceBox = new BitChoiceBox(true, "Outil", configuredBitsMap, cutDTO.getBitIndex());

        ArrayList<JLabel> labelList = new ArrayList<>();
        for (CutType t : CutType.values()) {
            JLabel l = new JLabel(UiUtil.getIcon(UiUtil.getIconFileName(t), UIConfig.INSTANCE.getCutBoxIconSize(),
                    UIManager.getColor("button.Foreground")));
            l.setText(UiUtil.getIconName(t));
            labelList.add(l);
        }

        modifyAnchorBox = new ButtonBox(mainWindow, true, "Modifier le point de référence", "Sélectionner le nouveau point");
    }

    /**
     * Adding the custom event listeners to SingleValueBox objects. The goal is to make
     * the Value attribute react to change events
     *
     * @param sb {@code SingleValueBox object}
     */
    protected void addEventListenerToDepth(SingleValueBox sb) {
        sb.getInput().getNumericInput().addPropertyChangeListener("value", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                CutDTO c = new CutDTO(cutDTO);
                c = new CutDTO(c.getId(), sb.getInput().getMMValue(), c.getBitIndex(), c.getCutType(), c.getPoints(), c.getRefsDTO(), c.getState());
                mainWindow.getController().modifyCut(c, true);
                cutListPanel.modifiedAttributeEventOccured(new ChangeAttributeEvent(cutBox, cutBox));
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
    protected void addEventListenerToBitChoiceBox(BitChoiceBox cb) {
        cb.getComboBox().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox comboBox = (JComboBox) e.getSource();

                CutDTO c = new CutDTO(cutDTO);
                ComboBitItem chosenBit = (ComboBitItem) comboBox.getModel().getSelectedItem();
                c = new CutDTO(c.getId(), c.getDepth(), chosenBit.getIndex(), c.getCutType(), c.getPoints(), c.getRefsDTO(), c.getState());
                mainWindow.getController().modifyCut(c, true);
                cutListPanel.modifiedAttributeEventOccured(new ChangeAttributeEvent(cutDTO, cutBox));
            }
        });
    }

    protected  void addEventListenerModifyAnchor(ButtonBox bb){
        bb.getInput().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainWindow.getController().computeGridIntersections();
                mainWindow.getMiddleContent().getCutWindow().getRendering2DWindow().getDrawing().initModifyAnchor(cutDTO);
            }
        });
    }
}
