package UI.Widgets.AttributeContainer;

import Common.DTO.BitDTO;
import Common.DTO.CutDTO;
import Common.DTO.VertexDTO;
import Domain.CutType;
import UI.Events.ChangeAttributeEvent;
import UI.MainWindow;
import UI.SubWindows.CutListPanel;
import UI.UIConfig;
import UI.UiUnits;
import UI.Widgets.*;
import UI.UiUtil;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Map;

public class AttributeContainerVertical extends AttributeContainer {
    SingleValueBox distanceFromEdgeToEdge;
    SingleValueBox absoluteDistanceFromEdgeToEdge;
    SingleValueBox distanceCenterToCenter;
    SingleValueBox depthBox;
    BitChoiceBox bitChoiceBox;

    public AttributeContainerVertical(MainWindow mainWindow, CutListPanel cutListPanel, CutDTO cutDTO, CutBox cutBox) {
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

        distanceFromEdgeToEdge = new SingleValueBox(mainWindow, true, "Distance relative de la sous-pièce", "X", edgeEdgeX(), UIConfig.INSTANCE.getDefaultUnit());
        absoluteDistanceFromEdgeToEdge = new SingleValueBoxNotEditable(mainWindow, true, "Taille de la sous-pièce", "X", Math.abs(edgeEdgeX()), UIConfig.INSTANCE.getDefaultUnit());
        distanceCenterToCenter = new SingleValueBoxNotEditable(mainWindow, true, "Distance des coupes centrales (GCODE)", "X", centerCenterX(), UIConfig.INSTANCE.getDefaultUnit());
        depthBox = new SingleValueBox(mainWindow, true, "Profondeur", "Profondeur", cutDTO.getDepth(), UIConfig.INSTANCE.getDefaultUnit());

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

    @Override
    public void updatePanel(CutDTO newCutDTO) {
        cutDTO = newCutDTO;
        distanceFromEdgeToEdge.getInput().setValueInMM(edgeEdgeX());
        absoluteDistanceFromEdgeToEdge.getInput().setValueInMM(Math.abs(edgeEdgeX()));
        distanceCenterToCenter.getInput().setValueInMM( centerCenterX());
        depthBox.getInput().setValueInMM(cutDTO.getDepth());
        revalidate();
        repaint();
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
                c = new CutDTO(c.getId(), sb.getInput().getMMValue(), c.getBitIndex(), c.getCutType(), c.getPoints(), c.getRefsDTO());
                mainWindow.getController().modifyCut(c);
                cutListPanel.modifiedAttributeEventOccured(new ChangeAttributeEvent(cutBox, cutBox));
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
                double centerCenterN = edgeEdgeToCenterCenter(sb.getInput().getMMValue());
                for(int i =0; i < c.getPoints().size(); i++){
                    VertexDTO oldVertex = c.getPoints().get(i);
                    VertexDTO newVertex = new VertexDTO(centerCenterN, oldVertex.getY(), oldVertex.getZ());
                    c.getPoints().set(i, newVertex);
                }
                mainWindow.getController().modifyCut(c);
                cutListPanel.modifiedAttributeEventOccured(new ChangeAttributeEvent(cutBox, cutBox));
            }
        });
    }

}
