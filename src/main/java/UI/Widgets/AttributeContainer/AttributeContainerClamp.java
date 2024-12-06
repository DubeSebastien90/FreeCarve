package UI.Widgets.AttributeContainer;

import Common.DTO.CutDTO;
import Common.DTO.VertexDTO;
import UI.MainWindow;
import UI.SubWindows.CutListPanel;
import UI.UIConfig;
import UI.Widgets.CutBox;
import UI.Widgets.PointsBox;
import UI.Widgets.SingleValueBox;
import UI.Events.ChangeAttributeEvent;

import java.awt.*;
import java.util.ArrayList;

public class AttributeContainerClamp extends AttributeContainer{
    SingleValueBox widthOfRectangle;
    SingleValueBox heightOfRectangle;
    PointsBox centerPosition;

    public AttributeContainerClamp(MainWindow mainWindow, CutListPanel cutListPanel, CutDTO cutDTO, CutBox cutBox) {
        super(mainWindow, cutListPanel, cutDTO, cutBox);
        init_attribute(mainWindow, cutDTO);
        init_layout();
    }

    private void init_attribute(MainWindow mainWindow, CutDTO cutDTO){
        centerPosition = new PointsBox(mainWindow, true, "Position du centre", getCenterPoint());
        widthOfRectangle = new SingleValueBox(mainWindow, true, "Largeur interne", "X", getWidthEdgeEdge(), UIConfig.INSTANCE.getDefaultUnit(), 0, Double.MAX_VALUE);
        heightOfRectangle = new SingleValueBox(mainWindow, true, "Hauteur interne", "Y", getHeightEdgeEdge(), UIConfig.INSTANCE.getDefaultUnit(), 0 , Double.MAX_VALUE);
    }

    private VertexDTO getCenterPoint(){
        return new VertexDTO((cutDTO.getPoints().get(0).getX() + cutDTO.getPoints().get(2).getX()) / 2,
                (cutDTO.getPoints().get(0).getY() + cutDTO.getPoints().get(2).getY()) / 2,
                (cutDTO.getPoints().get(0).getZ() + cutDTO.getPoints().get(2).getZ()) / 2);
    }

    private double getWidthEdgeEdge(){
        return Math.abs(cutDTO.getPoints().get(0).getX() - cutDTO.getPoints().get(2).getX());
    }

    private double getHeightEdgeEdge(){
        return Math.abs(cutDTO.getPoints().get(0).getY() - cutDTO.getPoints().get(2).getY());
    }



    @Override
    public void setupEventListeners() {
        addCenterEventListener(centerPosition);
        addWidthEventListener(widthOfRectangle);
        addHeightEventListener(heightOfRectangle);
    }

    @Override
    public void updatePanel(CutDTO newCutDTO) {
        cutDTO = newCutDTO;
        centerPosition.getxInput().setValueInMMWithoutTrigerringListeners(getCenterPoint().getX());
        centerPosition.getyInput().setValueInMMWithoutTrigerringListeners(getCenterPoint().getY());
        widthOfRectangle.getInput().setValueInMMWithoutTrigerringListeners(getWidthEdgeEdge());
        heightOfRectangle.getInput().setValueInMMWithoutTrigerringListeners(getHeightEdgeEdge());
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
        add(centerPosition, gc);

        gc.gridx = 0;
        gc.gridy = 1;
        gc.weightx = 1;
        gc.weighty = 1;

        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(widthOfRectangle, gc);

        gc.gridx = 0;
        gc.gridy = 2;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(heightOfRectangle, gc);
    }

    private void addHeightEventListener(SingleValueBox sb){
        sb.getInput().getNumericInput().addPropertyChangeListener("value", evt -> {
            double value = (double) evt.getNewValue();
            CutDTO newClamp = new CutDTO(cutDTO.getId(),
                    cutDTO.getDepth(),
                    cutDTO.getBitIndex(),
                    cutDTO.getCutType(),
                    mainWindow.getController().generateRectanglePoints(getCenterPoint(), getWidthEdgeEdge(), value),
                    new ArrayList<>(), cutDTO.getState());
            mainWindow.getController().modifyCut(newClamp);
            cutListPanel.modifiedAttributeEventOccured(new ChangeAttributeEvent(cutBox, cutBox));
        });
    }

    private void addWidthEventListener(SingleValueBox sb){
        sb.getInput().getNumericInput().addPropertyChangeListener("value", evt -> {
            double value = (double) evt.getNewValue();
            CutDTO newClamp = new CutDTO(cutDTO.getId(),
                    cutDTO.getDepth(),
                    cutDTO.getBitIndex(),
                    cutDTO.getCutType(),
                    mainWindow.getController().generateRectanglePoints(getCenterPoint(), value, getHeightEdgeEdge()),
                    new ArrayList<>(), cutDTO.getState());
            mainWindow.getController().modifyCut(newClamp);
            cutListPanel.modifiedAttributeEventOccured(new ChangeAttributeEvent(cutBox, cutBox));
        });
    }

    private void addCenterEventListener(PointsBox pb){
        pb.getxInput().getNumericInput().addPropertyChangeListener("value", evt -> {
            double value = (double) evt.getNewValue();
            CutDTO newClamp = new CutDTO(cutDTO.getId(),
                    cutDTO.getDepth(),
                    cutDTO.getBitIndex(),
                    cutDTO.getCutType(),
                    mainWindow.getController().generateRectanglePoints(new VertexDTO(value, getCenterPoint().getY(), getCenterPoint().getZ()), getWidthEdgeEdge(), getHeightEdgeEdge()),
                    new ArrayList<>(), cutDTO.getState());
            mainWindow.getController().modifyCut(newClamp);
            cutListPanel.modifiedAttributeEventOccured(new ChangeAttributeEvent(cutBox, cutBox));
        });

        pb.getyInput().getNumericInput().addPropertyChangeListener("value", evt -> {
            double value = (double) evt.getNewValue();
            CutDTO newClamp = new CutDTO(cutDTO.getId(),
                    cutDTO.getDepth(),
                    cutDTO.getBitIndex(),
                    cutDTO.getCutType(),
                    mainWindow.getController().generateRectanglePoints(new VertexDTO(getCenterPoint().getX(), value, getCenterPoint().getZ()), getWidthEdgeEdge(), getHeightEdgeEdge()),
                    new ArrayList<>(), cutDTO.getState());
            mainWindow.getController().modifyCut(newClamp);
            cutListPanel.modifiedAttributeEventOccured(new ChangeAttributeEvent(cutBox, cutBox));

        });
    }
}
