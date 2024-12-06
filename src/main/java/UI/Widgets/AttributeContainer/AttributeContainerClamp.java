package UI.Widgets.AttributeContainer;

import Common.DTO.CutDTO;
import Common.DTO.VertexDTO;
import UI.MainWindow;
import UI.SubWindows.CutListPanel;
import UI.UIConfig;
import UI.Widgets.CutBox;
import UI.Widgets.PointsBox;
import UI.Widgets.SingleValueBox;

import java.awt.*;

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
        return cutDTO.getPoints().get(1).getX();
    }

    private double getHeightEdgeEdge(){
        return cutDTO.getPoints().get(1).getY();
    }

    @Override
    public void setupEventListeners() {

    }

    @Override
    public void updatePanel(CutDTO newCutDTO) {

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
}
