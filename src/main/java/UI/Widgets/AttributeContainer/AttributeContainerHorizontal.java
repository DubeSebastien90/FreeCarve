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

public class AttributeContainerHorizontal extends AttributeContainer {
    SingleValueBox distanceFromEdgeToEdge;
    SingleValueBox absoluteDistanceFromEdgeToEdge;
    SingleValueBox distanceCenterToCenter;
    SingleValueBox depthBox;
    BitChoiceBox bitChoiceBox;

    public AttributeContainerHorizontal(MainWindow mainWindow, CutListPanel cutListPanel, CutDTO cutDTO, CutBox cutBox) {
        super(mainWindow, cutListPanel, cutDTO, cutBox);
        init_attribute(mainWindow, cutDTO);
        init_layout();

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
        add(distanceFromEdgeToEdge, gc);

        gc.gridx = 0;
        gc.gridy = 1;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(absoluteDistanceFromEdgeToEdge, gc);

        gc.gridx = 0;
        gc.gridy = 2;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(distanceCenterToCenter, gc);

        gc.gridx = 0;
        gc.gridy = 3;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(depthBox, gc);

        gc.gridx = 0;
        gc.gridy = 4;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(bitChoiceBox, gc);

    }

    /**
     * Initialize variables relevant to the Attribute Panel
     */
    private void init_attribute(MainWindow mainWindow, CutDTO cutDTO) {

        distanceFromEdgeToEdge = new SingleValueBox(true, "Distance relative de la sous-pièce", "Y", edgeEdgeY());
        absoluteDistanceFromEdgeToEdge = new SingleValueBoxNotEditable(true, "Taille de la sous-pièce", "Y", Math.abs(edgeEdgeY()));
        distanceCenterToCenter = new SingleValueBoxNotEditable(true, "Distance des coupes centrales (GCODE)", "Y", centerCenterY());
        depthBox = new SingleValueBox(true, "Profondeur", "Profondeur", cutDTO.getDepth());

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
    }

    @Override
    public void setupEventListeners() {
        addEventListenerToEdgeEdge(distanceFromEdgeToEdge);
        addEventListenerToDepth(depthBox);
        addEventListenerToBitChoiceBox(bitChoiceBox);
    }


    /**
     * Adding the custom event listeners to SingleValueBox objects. The goal is to make
     * the Value attribute react to change events
     *
     * @param sb {@code SingleValueBox object}
     */
    private void addEventListenerToDepth(SingleValueBox sb) {
        sb.getInput().getNumericInput().addPropertyChangeListener("value", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                CutDTO c = new CutDTO(cutDTO);
                Number n = (Number) evt.getNewValue();
                c = new CutDTO(c.getId(), n.doubleValue(), c.getBitIndex(), c.getCutType(), c.getPoints(), c.getRefsDTO());
                mainWindow.getController().modifyCut(c);
                cutListPanel.modifiedAttributeEventOccured(new ChangeAttributeEvent(this, cutBox));
            }
        });
    }

    /**
     * Adding the custom event listeners to SingleValueBox objects. The goal is to make
     * the Value attribute react to change events
     *
     * Changes the X offset of the vertical cut
     *
     * @param sb {@code SingleValueBox object}
     */
    private void addEventListenerToEdgeEdge(SingleValueBox sb) {
        sb.getInput().getNumericInput().addPropertyChangeListener("value", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                CutDTO c = new CutDTO(cutDTO);
                Number n = (Number) evt.getNewValue();
                double centerCenterN = edgeEdgeToCenterCenter(n.doubleValue());
                for(int i =0; i < c.getPoints().size(); i++){
                    VertexDTO oldVertex = c.getPoints().get(i);
                    VertexDTO newVertex = new VertexDTO(oldVertex.getX(), centerCenterN, oldVertex.getZ());
                    c.getPoints().set(i, newVertex);
                }
                mainWindow.getController().modifyCut(c);
                cutListPanel.modifiedAttributeEventOccured(new ChangeAttributeEvent(this, cutBox));
            }
        });
    }




}

