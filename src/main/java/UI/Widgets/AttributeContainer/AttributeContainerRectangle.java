package UI.Widgets.AttributeContainer;

import Common.DTO.CutDTO;
import Common.DTO.VertexDTO;
import UI.Events.ChangeAttributeEvent;
import UI.MainWindow;
import UI.SubWindows.CutListPanel;
import UI.UIConfig;
import UI.Widgets.*;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class AttributeContainerRectangle extends AttributeContainer{

    PointsBox offsetOfRectangle;
    SingleValueBox widthOfRectangle;
    SingleValueBox absoluteWidthOfRectangle;
    SingleValueBox heightOfRectangle;
    SingleValueBox absoluteHeightOfRectangle;

    public AttributeContainerRectangle(MainWindow mainWindow, CutListPanel cutListPanel, CutDTO cutDTO, CutBox cutBox) {
        super(mainWindow, cutListPanel, cutDTO, cutBox);
        init_attribute(mainWindow, cutDTO);
        init_layout();
    }

    private void init_attribute(MainWindow mainWindow, CutDTO cutDTO){
        super.init_attribute();
        offsetOfRectangle = new PointsBox(mainWindow, true, "Distance relative du rectangle", cutDTO.getPoints().getFirst());
        widthOfRectangle = new SingleValueBox(mainWindow, true, "Largeur interne relative", "X", getRectangleEdgeEdgeX(), UIConfig.INSTANCE.getDefaultUnit());
        heightOfRectangle = new SingleValueBox(mainWindow, true, "Hauteur interne relative", "Y", getRectangleEdgeEdgeY(), UIConfig.INSTANCE.getDefaultUnit());
        absoluteWidthOfRectangle = new SingleValueBoxNotEditable(mainWindow, true, "Largeur interne absolue", "X", Math.abs(getRectangleEdgeEdgeX()), UIConfig.INSTANCE.getDefaultUnit());
        absoluteHeightOfRectangle = new SingleValueBoxNotEditable(mainWindow, true, "Hauteur interne absolue", "Y", Math.abs(getRectangleEdgeEdgeY()), UIConfig.INSTANCE.getDefaultUnit());
    }

    private double getRectangleCenterCenterX(){
        VertexDTO p1 = cutDTO.getPoints().getFirst();
        VertexDTO p3 = cutDTO.getPoints().get(2);
        VertexDTO diff = p3.sub(p1);
        return diff.getX();
    }

    private double getRectangleCenterCenterY(){
        VertexDTO p1 = cutDTO.getPoints().getFirst();
        VertexDTO p3 = cutDTO.getPoints().get(2);
        VertexDTO diff = p3.sub(p1);
        return diff.getY();
    }

    private double getRectangleEdgeEdgeX(){
        double diameter = mainWindow.getController().getBitDiameter(cutDTO.getBitIndex());
        return getRectangleCenterCenterX() - diameter;
    }

    private double getRectangleEdgeEdgeY(){
        double diameter = mainWindow.getController().getBitDiameter(cutDTO.getBitIndex());
        return getRectangleCenterCenterY() - diameter;
    }

    private double getRectangleEdgeEdgeToCenterCenter(double edgeEdgeValue){
        double diameter = mainWindow.getController().getBitDiameter(cutDTO.getBitIndex());
        return edgeEdgeValue + diameter;
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
        add(offsetOfRectangle, gc);

        gc.gridx = 0;
        gc.gridy = 1;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(widthOfRectangle, gc);

        gc.gridx = 0;
        gc.gridy = 2;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(heightOfRectangle, gc);

        gc.gridx = 0;
        gc.gridy = 3;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(absoluteWidthOfRectangle, gc);

        gc.gridx = 0;
        gc.gridy = 4;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(absoluteHeightOfRectangle, gc);

        gc.gridx = 0;
        gc.gridy = 5;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(depthBox, gc);

        gc.gridx = 0;
        gc.gridy = 6;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(bitChoiceBox, gc);
    }

    private CutDTO pointsWidthResizeCenterCenter(double newWidth){
        CutDTO c = new CutDTO(cutDTO);

        VertexDTO p1 = c.getPoints().getFirst();
        VertexDTO p3 = c.getPoints().get(2);

        double width = newWidth;
        double height = p3.sub(p1).getY();

        List<VertexDTO> newPoints = mainWindow.getController().generateRectanglePoints(p1, width, height);

        for(int i=0; i < c.getPoints().size(); i++){
            c.getPoints().set(i, newPoints.get(i));
        }

        return c;
    }

    private CutDTO pointsHeightResizeCenterCenter(double newHeight){
        CutDTO c = new CutDTO(cutDTO);

        VertexDTO p1 = c.getPoints().getFirst();
        VertexDTO p3 = c.getPoints().get(2);

        double width = p3.sub(p1).getX();;
        double height = newHeight;

        List<VertexDTO> newPoints = mainWindow.getController().generateRectanglePoints(p1, width, height);

        for(int i=0; i < c.getPoints().size(); i++){
            c.getPoints().set(i, newPoints.get(i));
        }

        return c;
    }

    private CutDTO moveAllPointsCenterCenter(double newValue, VertexDTO.AXIS axis){
        CutDTO c = new CutDTO(cutDTO);

        VertexDTO p1 = c.getPoints().getFirst();
        VertexDTO p3 = c.getPoints().get(2);

        double width = p3.sub(p1).getX();;
        double height = p3.sub(p1).getY();

        VertexDTO anchor = VertexDTO.zero();
        if(axis == VertexDTO.AXIS.X){
            anchor = new VertexDTO(newValue, p1.getY(), p1.getZ());
        }
        else if(axis == VertexDTO.AXIS.Y){
            anchor = new VertexDTO(p1.getX(), newValue, p1.getZ());
        }
        else if(axis == VertexDTO.AXIS.Z){
            anchor = new VertexDTO(p1.getX(), p1.getY(), newValue);
        }
        List<VertexDTO> newPoints = mainWindow.getController().generateRectanglePoints(anchor, width, height);

        for(int i=0; i < c.getPoints().size(); i++){
            c.getPoints().set(i, newPoints.get(i));
        }

        return c;
    }

    @Override
    public void setupEventListeners() {
        addEventListenerToResizeInternalWidth(widthOfRectangle);
        addEventListenerToResizeInternalHeight(heightOfRectangle);
        addEventListenerToMoveOffset(offsetOfRectangle);
    }

    @Override
    public void updatePanel(CutDTO newCutDTO) {
        cutDTO = newCutDTO;
        offsetOfRectangle.getxInput().setValueInMMWithoutTrigerringListeners(cutDTO.getPoints().getFirst().getX());
        offsetOfRectangle.getyInput().setValueInMMWithoutTrigerringListeners(cutDTO.getPoints().getFirst().getY());
        offsetOfRectangle.getzInput().setValueInMMWithoutTrigerringListeners(cutDTO.getPoints().getFirst().getZ());
        widthOfRectangle.getInput().setValueInMMWithoutTrigerringListeners(getRectangleEdgeEdgeX());
        heightOfRectangle.getInput().setValueInMMWithoutTrigerringListeners(getRectangleEdgeEdgeY());
        absoluteWidthOfRectangle.getInput().setValueInMMWithoutTrigerringListeners(Math.abs(getRectangleEdgeEdgeX()));
        absoluteHeightOfRectangle.getInput().setValueInMMWithoutTrigerringListeners(Math.abs(getRectangleEdgeEdgeY()));
    }

    private void addEventListenerToResizeInternalWidth(SingleValueBox sb){
        sb.getInput().getNumericInput().addPropertyChangeListener("value", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                double centerCenterN = getRectangleEdgeEdgeToCenterCenter(sb.getInput().getMMValue());
                CutDTO c = pointsWidthResizeCenterCenter(centerCenterN);
                mainWindow.getController().modifyCut(c);
                cutListPanel.modifiedAttributeEventOccured(new ChangeAttributeEvent(cutBox, cutBox));
            }
        });
    }

    private void addEventListenerToResizeInternalHeight(SingleValueBox sb){
        sb.getInput().getNumericInput().addPropertyChangeListener("value", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                double centerCenterN = getRectangleEdgeEdgeToCenterCenter(sb.getInput().getMMValue());
                CutDTO c = pointsHeightResizeCenterCenter(centerCenterN);
                mainWindow.getController().modifyCut(c);
                cutListPanel.modifiedAttributeEventOccured(new ChangeAttributeEvent(cutBox, cutBox));
            }
        });
    }

    private void addEventListenerToMoveOffset(PointsBox pb){
        pb.getxInput().getNumericInput().addPropertyChangeListener("value", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                CutDTO c = moveAllPointsCenterCenter(pb.getxInput().getMMValue(), VertexDTO.AXIS.X);
                mainWindow.getController().modifyCut(c);
                cutListPanel.modifiedAttributeEventOccured(new ChangeAttributeEvent(cutBox, cutBox));
            }
        });

        pb.getyInput().getNumericInput().addPropertyChangeListener("value", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                CutDTO c = moveAllPointsCenterCenter(pb.getyInput().getMMValue(), VertexDTO.AXIS.Y);
                mainWindow.getController().modifyCut(c);
                cutListPanel.modifiedAttributeEventOccured(new ChangeAttributeEvent(cutBox, cutBox));
            }
        });

        pb.getzInput().getNumericInput().addPropertyChangeListener("value", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                CutDTO c = moveAllPointsCenterCenter(pb.getzInput().getMMValue(), VertexDTO.AXIS.Z);
                mainWindow.getController().modifyCut(c);
                cutListPanel.modifiedAttributeEventOccured(new ChangeAttributeEvent(cutBox, cutBox));
            }
        });
    }
}
