package UI.Widgets;

import Common.DTO.DimensionDTO;
import Common.Interfaces.IUnitConverter;
import Common.Units;
import UI.SubWindows.BasicWindow;
import UI.UIConfig;
import UI.UiUnits;
import com.formdev.flatlaf.ui.FlatEmptyBorder;
import com.formdev.flatlaf.ui.FlatRoundBorder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.NumberFormatter;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Small widget class that offers an interface for inputting measurements
 *
 * @author Louis-Etienne Messier
 * @author Kamran Charles Nayebi\
 * @since 2024-11-21
 */
public class MeasurementInputField extends BasicWindow {
    private JLabel nameOfInput;
    private JFormattedTextField numericInput;
    private JComboBox<UiUnits> unitComboBox;
    private BoxLayout layout;
    private DimensionDTO maxDimension;
    private DimensionDTO minDimension;
    private IUnitConverter unitConverter;
    private UiUnits currentUnit;

    MeasurementInputField(IUnitConverter unitConverter, String nameOfInput, double value, UiUnits unit) {
        this(unitConverter, nameOfInput, value, 0, unitConverter.convertUnit(new DimensionDTO(15, Units.FEET), unit.getUnit()).value(), unit);
    }

    MeasurementInputField(IUnitConverter unitConverter, String nameOfInput, double value, double minimumValue, double maximumValue, UiUnits unit) {
        super(false);
        this.setBackground(null);
        this.setOpaque(false);
        this.unitConverter = unitConverter;
        this.currentUnit = unit;
        this.minDimension = new DimensionDTO(minimumValue, unit.getUnit());
        this.maxDimension = new DimensionDTO(maximumValue, unit.getUnit());
        this.init(nameOfInput, value);
        setCurrentUnit(unit);
    }

    private void init(String nameOfInput, double value) {
        layout = new BoxLayout(this, BoxLayout.X_AXIS);
        this.setLayout(layout);

        this.nameOfInput = new JLabel(nameOfInput);
        this.nameOfInput.setOpaque(true);
        this.nameOfInput.setBackground(UIManager.getColor("SubWindow.lightBackground1"));
        this.nameOfInput.setBorder(new FlatEmptyBorder());
        this.nameOfInput.setHorizontalAlignment(SwingConstants.RIGHT);
        this.nameOfInput.setBorder(new EmptyBorder(0, 0, 0, UIConfig.INSTANCE.getDefaultPadding()));

        this.unitComboBox = new JComboBox<>(UiUnits.values());
        this.unitComboBox.setSelectedItem(currentUnit);
        this.unitComboBox.addItemListener(new UnitChangeListener());

        this.numericInput = new JFormattedTextField(DecimalFormat.getNumberInstance());
        this.numericInput.setColumns(10);
        this.numericInput.setBorder(new FlatRoundBorder());
        this.numericInput.setValue(value);
        this.numericInput.setBackground(UIManager.getColor("SubWindow.lightBackground2"));

        this.add(this.nameOfInput);
        this.add(this.numericInput);
        this.add(this.unitComboBox);
    }

    public JFormattedTextField getNumericInput() {
        return this.numericInput;
    }

    public double getMMValue() {
        return unitConverter.convertUnit(new DimensionDTO(((Number)numericInput.getValue()).doubleValue(), currentUnit.getUnit()), Units.MM).value();
    }

    private class UnitChangeListener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent event) {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                UiUnits newUnit = (UiUnits) event.getItem();
                setCurrentUnit(newUnit);
            }
        }
    }

    public DimensionDTO getMaxDimension() {
        return maxDimension;
    }

    public void setMaxDimension(DimensionDTO maxDimension) {
        NumberFormatter numberFormatter = (NumberFormatter) numericInput.getFormatter();
        numberFormatter.setMaximum(unitConverter.convertUnit(maxDimension, maxDimension.unit()).value());
        this.maxDimension = maxDimension;
    }

    public DimensionDTO getMinDimension() {
        return minDimension;
    }

    public void setMinDimension(DimensionDTO minDimension) {
        NumberFormatter numberFormatter = (NumberFormatter) numericInput.getFormatter();
        numberFormatter.setMinimum(unitConverter.convertUnit(minDimension, minDimension.unit()).value());
        this.minDimension = minDimension;
    }

    public void setCurrentUnit(UiUnits newUnit) {
        double currentValue = ((Number)numericInput.getValue()).doubleValue();

        DimensionDTO result = unitConverter.convertUnit(new DimensionDTO(currentValue, currentUnit.getUnit()), newUnit.getUnit());

        this.currentUnit = newUnit;
        unitComboBox.setSelectedItem(currentUnit);

        numericInput.setValue(result.value());
        setMaxDimension(maxDimension);
        setMinDimension(minDimension);
    }
}
