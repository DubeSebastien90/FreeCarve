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

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class AttributeContainerBorder extends AttributeContainer{

    SingleValueBox marginEdgeToEdge;
    SingleValueBox marginCenterToCenter;

    public AttributeContainerBorder(MainWindow mainWindow, CutListPanel cutListPanel, CutDTO cutDTO, CutBox cutBox) {
        super(mainWindow, cutListPanel, cutDTO, cutBox);
        init_attribute(mainWindow, cutDTO);
        init_layout();

    }

    private void init_attribute(MainWindow mainWindow, CutDTO cutDTO){
        super.init_attribute();
        marginEdgeToEdge = new SingleValueBox(mainWindow, true, "Distance relative de la marge", "Marge",borderEdgeEdge(), UIConfig.INSTANCE.getDefaultUnit());
        marginCenterToCenter = new SingleValueBoxNotEditable(mainWindow, true, "Distances centrales (GCODE)", "Marge", borderCenterCenter(), UIConfig.INSTANCE.getDefaultUnit());
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
        add(marginEdgeToEdge, gc);


        gc.gridx = 0;
        gc.gridy = 1;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(marginCenterToCenter, gc);
    }

    private double borderCenterCenter(){
        return  cutDTO.getPoints().getFirst().getX();
    }

    private double borderEdgeEdge(){

        return centerCenterToEdgeEdge(borderCenterCenter());
    }

    @Override
    public void setupEventListeners() {
        addEventListenerToEdgeEdge(marginEdgeToEdge);

        addEventListenerToBitChoiceBox(bitChoiceBox);
        addEventListenerToDepth(depthBox);
    }

    @Override
    public void updatePanel(CutDTO newCutDTO) {
        cutDTO = newCutDTO;
        marginEdgeToEdge.getInput().setValueInMMWithoutTrigerringListeners(borderEdgeEdge());
        marginCenterToCenter.getInput().setValueInMMWithoutTrigerringListeners(borderCenterCenter());
        revalidate();
        repaint();
    }

    /**
     * Adding the custom event listeners to SingleValueBox objects. The goal is to make
     * the Value attribute react to change events
     *
     * Changes the margin size
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
