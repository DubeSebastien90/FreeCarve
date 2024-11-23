package UI.Widgets.AttributeContainer;

import Common.DTO.CutDTO;
import UI.Events.ChangeAttributeEvent;
import UI.MainWindow;
import UI.SubWindows.BasicWindow;
import UI.SubWindows.CutListPanel;
import UI.Widgets.BitChoiceBox;
import UI.Widgets.ComboBitItem;
import UI.Widgets.CutBox;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class AttributeContainer extends BasicWindow {

    protected MainWindow mainWindow;
    protected CutListPanel cutListPanel;
    protected CutDTO cutDTO;
    protected CutBox cutBox;


    public AttributeContainer(MainWindow mainWindow, CutListPanel cutListPanel, CutDTO cutDTO, CutBox cutBox) {
        super(true);
        this.mainWindow = mainWindow;
        this.cutListPanel = cutListPanel;
        this.cutDTO = cutDTO;
        this.cutBox = cutBox;
    }

    public abstract void setupEventListeners();
    public abstract void updatePanel(CutDTO newCutDTO);

    protected double edgeEdgeX(){
        int currentBitIndex = cutDTO.getBitIndex();
        int refBitIndex = cutDTO.getRefsDTO().getFirst().getCut().getBitIndex();

        double currentBitDiameter = mainWindow.getController().getBitDiameter(currentBitIndex);
        double refBitDiameter = mainWindow.getController().getBitDiameter(refBitIndex);

        double centerDistance = cutDTO.getPoints().getFirst().getX();
        double edgeToEdgeDistance = centerDistance - currentBitDiameter - refBitDiameter;

        if (centerDistance < 0){
            edgeToEdgeDistance = centerDistance + currentBitDiameter + refBitDiameter;
        }

        return edgeToEdgeDistance;
    }

    protected double edgeEdgeY(){
        int currentBitIndex = cutDTO.getBitIndex();
        int refBitIndex = cutDTO.getRefsDTO().getFirst().getCut().getBitIndex();

        double currentBitDiameter = mainWindow.getController().getBitDiameter(currentBitIndex);
        double refBitDiameter = mainWindow.getController().getBitDiameter(refBitIndex);

        double centerDistance = cutDTO.getPoints().getFirst().getY();
        double edgeToEdgeDistance = centerDistance - currentBitDiameter - refBitDiameter;

        if (centerDistance < 0){
            edgeToEdgeDistance = centerDistance + currentBitDiameter + refBitDiameter;
        }

        return edgeToEdgeDistance;
    }

    protected double centerCenterX(){
        return cutDTO.getPoints().getFirst().getX();
    }

    protected double centerCenterY(){
        return cutDTO.getPoints().getFirst().getY();
    }

    protected double edgeEdgeToCenterCenter(double edgeEdge){
        int currentBitIndex = mainWindow.getMiddleContent().getCutWindow().getBitSelectionPanel().getSelectedBit();
        int refBitIndex = cutDTO.getRefsDTO().getFirst().getCut().getBitIndex();
        return mainWindow.getController().edgeEdgeToCenterCenter(edgeEdge, currentBitIndex, refBitIndex);
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
                c = new CutDTO(c.getId(), c.getDepth(), chosenBit.getIndex(), c.getCutType(), c.getPoints(), c.getRefsDTO());
                mainWindow.getController().modifyCut(c);
                cutListPanel.modifiedAttributeEventOccured(new ChangeAttributeEvent(cutDTO, cutBox));
            }
        });
    }
}
