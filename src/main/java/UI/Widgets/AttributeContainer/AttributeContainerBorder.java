package UI.Widgets.AttributeContainer;

import Common.DTO.CutDTO;
import Common.DTO.VertexDTO;
import UI.Events.ChangeAttributeEvent;
import UI.MainWindow;
import UI.SubWindows.CutListPanel;
import UI.UIConfig;
import UI.Widgets.CutBox;
import UI.Widgets.SingleValueBox;
import UI.Widgets.SingleValueBoxNotEditable;
import com.sun.jdi.event.StepEvent;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class AttributeContainerBorder extends AttributeContainer{

    SingleValueBox widthEdgeEdge;
    SingleValueBox heightEdgeEdge;


    public AttributeContainerBorder(MainWindow mainWindow, CutListPanel cutListPanel, CutDTO cutDTO, CutBox cutBox) {
        super(mainWindow, cutListPanel, cutDTO, cutBox);
        init_attribute(mainWindow, cutDTO);
        init_layout();

    }

    private void init_attribute(MainWindow mainWindow, CutDTO cutDTO){
        super.init_attribute();
        widthEdgeEdge = new SingleValueBox(mainWindow, true, "Largeur interne de la retaille", "Marge",borderEdgeEdgeWidth(), 0, Double.MAX_VALUE);
        heightEdgeEdge = new SingleValueBox(mainWindow, true, "Hauteur interne de la retaille", "Marge", borderEdgeEdgeHeight(), 0, Double.MAX_VALUE);
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
        add(widthEdgeEdge, gc);


        gc.gridx = 0;
        gc.gridy = 1;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(heightEdgeEdge, gc);

        gc.gridx = 0;
        gc.gridy = 2;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(depthBox, gc);

        gc.gridx = 0;
        gc.gridy = 3;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(bitChoiceBox, gc);


    }


    private double borderEdgeEdgeWidth(){
        return cutDTO.getPoints().getFirst().getX() + cutDTO.getPoints().get(1).getX();
    }

    private double borderEdgeEdgeHeight(){
        return cutDTO.getPoints().getFirst().getY() + cutDTO.getPoints().get(1).getY();
    }

    @Override
    public void setupEventListeners() {
        addEventListenerToWidth(widthEdgeEdge);
        addEventListenerToHeight(heightEdgeEdge);
        addEventListenerToBitChoiceBox(bitChoiceBox);
        addEventListenerToDepth(depthBox);
        addEventListenerModifyAnchor(modifyAnchorBox);
    }

    @Override
    public void updatePanel(CutDTO newCutDTO) {
        cutDTO = new CutDTO(newCutDTO);
        widthEdgeEdge.getInput().setValueInMMWithoutTrigerringListeners(borderEdgeEdgeWidth());
        heightEdgeEdge.getInput().setValueInMMWithoutTrigerringListeners(borderEdgeEdgeHeight());
        bitChoiceBox.refresh(mainWindow.getController().getConfiguredBitsMap(), cutDTO.getBitIndex());
        revalidate();
        repaint();
    }


    private void addEventListenerToWidth(SingleValueBox sb) {
        sb.getInput().getNumericInput().addPropertyChangeListener("value", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                CutDTO c = new CutDTO(cutDTO);
                double newWidth = sb.getInput().getMMValue();

                for(int i =0; i < c.getPoints().size(); i++){
                    VertexDTO oldVertex = c.getPoints().get(i);
                    VertexDTO newVertex = new VertexDTO(newWidth/2, oldVertex.getY(), oldVertex.getZ());
                    c.getPoints().set(i, newVertex);
                }
                mainWindow.getController().modifyCut(c);
                cutListPanel.modifiedAttributeEventOccured(new ChangeAttributeEvent(cutBox, cutBox));
            }
        });
    }

    private void addEventListenerToHeight(SingleValueBox sb) {
        sb.getInput().getNumericInput().addPropertyChangeListener("value", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                CutDTO c = new CutDTO(cutDTO);
                double newHeight = sb.getInput().getMMValue();

                for(int i =0; i < c.getPoints().size(); i++){
                    VertexDTO oldVertex = c.getPoints().get(i);
                    VertexDTO newVertex = new VertexDTO(oldVertex.getX(), newHeight/2, oldVertex.getZ());
                    c.getPoints().set(i, newVertex);
                }
                mainWindow.getController().modifyCut(c);
                cutListPanel.modifiedAttributeEventOccured(new ChangeAttributeEvent(cutBox, cutBox));
            }
        });
    }
}
