package UI.Widgets;

import Common.DTO.VertexDTO;
import Common.Interfaces.IUnitConverter;
import Common.Units;
import UI.MainWindow;
import UI.UiUnits;

import java.awt.*;

/**
 * The {@code PointsBox} class is a UI class that encapsulate the Points editor
 *
 * @author Louis-Etienne Messier
 * @author Kamran Charles Nayebi
 * @since 2024-10-23
 */
public class PointsBox extends GenericAttributeBox {

    private MeasurementInputField xInput;
    private MeasurementInputField yInput;
    private MeasurementInputField zInput;


    public PointsBox(MainWindow mainWindow, boolean hasBackground, String name, VertexDTO vertexDTO) {
        super(hasBackground, name);
        this.init(mainWindow, vertexDTO);
    }

    private void init(MainWindow mainWindow, VertexDTO vertexDTO) {
        GridBagConstraints gc = new GridBagConstraints();

        this.xInput = new MeasurementInputField(mainWindow, "X", vertexDTO.getX(), UiUnits.MILLIMETERS);
        this.yInput = new MeasurementInputField(mainWindow, "Y", vertexDTO.getY(), UiUnits.MILLIMETERS);
        this.zInput = new MeasurementInputField(mainWindow, "Z", vertexDTO.getZ(), UiUnits.MILLIMETERS);

        gc.gridx = 0;
        gc.gridy = 1;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 1;
        this.add(xInput, gc);
        gc.gridx = 0;
        gc.gridy = 2;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 1;
        this.add(yInput, gc);
        gc.gridx = 0;
        gc.gridy = 3;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 1;
        this.add(zInput, gc);
    }

    public MeasurementInputField getxInput() {
        return this.xInput;
    }

    public MeasurementInputField getyInput() {
        return this.yInput;
    }

    public MeasurementInputField getzInput() {
        return this.zInput;
    }
}
