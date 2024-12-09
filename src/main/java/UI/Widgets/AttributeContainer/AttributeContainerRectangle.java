package UI.Widgets.AttributeContainer;

import Common.DTO.CutDTO;
import Common.DTO.VertexDTO;
import UI.Events.ChangeAttributeEvent;
import UI.MainWindow;
import UI.SubWindows.CutListPanel;
import UI.UIConfig;
import UI.Widgets.CutBox;
import UI.Widgets.PointsBox;
import UI.Widgets.SingleValueBox;
import UI.Widgets.SingleValueBoxNotEditable;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class AttributeContainerRectangle extends AttributeContainer {

    PointsBox offsetOfRectangle;
    SingleValueBox widthOfRectangle;
    SingleValueBox widthOfRectangleCenterCenter;
    SingleValueBox heightOfRectangle;
    SingleValueBox heightOfRectangleCenterCenter;

    public AttributeContainerRectangle(MainWindow mainWindow, CutListPanel cutListPanel, CutDTO cutDTO, CutBox cutBox) {
        super(mainWindow, cutListPanel, cutDTO, cutBox);
        init_attribute(mainWindow, cutDTO);
        init_layout();
    }

    private void init_attribute(MainWindow mainWindow, CutDTO cutDTO) {
        super.init_attribute();

        offsetOfRectangle = new PointsBox(mainWindow, true, "Distance relative du rectangle", getAnchorCenterPoint());
        widthOfRectangle = new SingleValueBox(mainWindow, true, "Largeur interne", "X", getWidthEdgeEdge(), 0, Double.MAX_VALUE);
        heightOfRectangle = new SingleValueBox(mainWindow, true, "Hauteur interne", "Y", getHeightEdgeEdge(), 0, Double.MAX_VALUE);
        widthOfRectangleCenterCenter = new SingleValueBoxNotEditable(mainWindow, true, "Largeur centrale (GCODE)", "X", getWidthCenterCenter());
        heightOfRectangleCenterCenter = new SingleValueBoxNotEditable(mainWindow, true, "Hauteur centrale (GCODE)", "Y", getHeightCenterCenter());
    }

    private VertexDTO getAnchorCenterPoint() {
        return cutDTO.getPoints().getFirst();
    }

    private double getWidthEdgeEdge() {
        return cutDTO.getPoints().get(1).getX();
    }

    private double getHeightEdgeEdge() {
        return cutDTO.getPoints().get(1).getY();
    }

    private double getWidthCenterCenter() {
        double bitDiamter = mainWindow.getController().getBitDiameter(cutDTO.getBitIndex());
        return getWidthEdgeEdge() + bitDiamter;
    }

    private double getHeightCenterCenter() {
        double bitDiamter = mainWindow.getController().getBitDiameter(cutDTO.getBitIndex());
        return getHeightEdgeEdge() + bitDiamter;
    }

    private void init_layout() {
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
        add(widthOfRectangleCenterCenter, gc);

        gc.gridx = 0;
        gc.gridy = 4;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(heightOfRectangleCenterCenter, gc);

        gc.gridx = 0;
        gc.gridy = 5;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(depthBox, gc);

        gc.gridx = 0;
        gc.gridy = 6;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(bitChoiceBox, gc);

        gc.gridx = 0;
        gc.gridy = 7;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(modifyAnchorBox, gc);
    }

    private CutDTO pointsWidthResizeEdgeEdge(double newWidthEdgeEdge) {
        CutDTO c = new CutDTO(cutDTO);
        double height = getHeightEdgeEdge();
        c.getPoints().set(1, new VertexDTO(newWidthEdgeEdge, height, 0));
        return c;
    }

    private CutDTO pointsHeightResizeEdgeEdge(double newHeightEdgeEdge) {
        CutDTO c = new CutDTO(cutDTO);
        double width = getWidthEdgeEdge();
        c.getPoints().set(1, new VertexDTO(width, newHeightEdgeEdge, 0));
        return c;
    }

    private CutDTO moveAllPointsCenterCenter(double newValue, VertexDTO.AXIS axis) {
        CutDTO c = new CutDTO(cutDTO);

        VertexDTO centerAnchorPoint = getAnchorCenterPoint();

        VertexDTO anchor = VertexDTO.zero();
        if (axis == VertexDTO.AXIS.X) {
            anchor = new VertexDTO(newValue, centerAnchorPoint.getY(), centerAnchorPoint.getZ());
        } else if (axis == VertexDTO.AXIS.Y) {
            anchor = new VertexDTO(centerAnchorPoint.getX(), newValue, centerAnchorPoint.getZ());
        } else if (axis == VertexDTO.AXIS.Z) {
            anchor = new VertexDTO(centerAnchorPoint.getX(), centerAnchorPoint.getY(), newValue);
        }
        c.getPoints().set(0, anchor);
        return c;
    }

    @Override
    public void setupEventListeners() {
        addEventListenerToResizeInternalWidth(widthOfRectangle);
        addEventListenerToResizeInternalHeight(heightOfRectangle);
        addEventListenerToMoveOffset(offsetOfRectangle);

        addEventListenerToBitChoiceBox(bitChoiceBox);
        addEventListenerToDepth(depthBox);
        addEventListenerModifyAnchor(modifyAnchorBox);
    }

    @Override
    public void updatePanel(CutDTO newCutDTO) {
        cutDTO = newCutDTO;
        offsetOfRectangle.getxInput().setValueInMMWithoutTrigerringListeners(getAnchorCenterPoint().getX());
        offsetOfRectangle.getyInput().setValueInMMWithoutTrigerringListeners(getAnchorCenterPoint().getY());
        widthOfRectangle.getInput().setValueInMMWithoutTrigerringListeners(getWidthEdgeEdge());
        heightOfRectangle.getInput().setValueInMMWithoutTrigerringListeners(getHeightEdgeEdge());
        widthOfRectangleCenterCenter.getInput().setValueInMMWithoutTrigerringListeners(getWidthCenterCenter());
        heightOfRectangleCenterCenter.getInput().setValueInMMWithoutTrigerringListeners(getHeightCenterCenter());
    }

    private void addEventListenerToResizeInternalWidth(SingleValueBox sb) {
        sb.getInput().getNumericInput().addPropertyChangeListener("value", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                double n = sb.getInput().getMMValue();
                CutDTO c = pointsWidthResizeEdgeEdge(n);
                mainWindow.getController().modifyCut(c);
                cutListPanel.modifiedAttributeEventOccured(new ChangeAttributeEvent(cutBox, cutBox));
            }
        });
    }

    private void addEventListenerToResizeInternalHeight(SingleValueBox sb) {
        sb.getInput().getNumericInput().addPropertyChangeListener("value", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                double n = sb.getInput().getMMValue();
                CutDTO c = pointsHeightResizeEdgeEdge(n);
                mainWindow.getController().modifyCut(c);
                cutListPanel.modifiedAttributeEventOccured(new ChangeAttributeEvent(cutBox, cutBox));
            }
        });
    }

    private void addEventListenerToMoveOffset(PointsBox pb) {
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
    }
}
