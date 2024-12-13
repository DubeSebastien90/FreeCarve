package UI.Widgets;

import Common.DTO.DimensionDTO;
import Common.Interfaces.IMemorizer;
import Common.Interfaces.IUnitConverter;
import Common.Units;
import UI.MainWindow;
import UI.SubWindows.BasicWindow;
import UI.UIConfig;
import UI.UiUnits;
import com.formdev.flatlaf.ui.FlatEmptyBorder;
import com.formdev.flatlaf.ui.FlatRoundBorder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * Small widget class that offers an interface for inputting measurements
 *
 * @author Louis-Etienne Messier
 * @author Kamran Charles Nayebi
 * @since 2024-11-21
 */
public class MeasurementInputField extends BasicWindow {
    private JFormattedTextField numericInput;
    private JComboBox<UiUnits> unitComboBox;

    private DimensionDTO maxDimension;
    private DimensionDTO minDimension;
    private final IUnitConverter unitConverter;
    private final IMemorizer memorizer;
    private final ImperialFractionalNumberFormatter imperialFractionalNumberFormatter;
    private final CustomNumberFormatter customNumberFormatter;

    private UiUnits currentUnit;

    public MeasurementInputField(MainWindow mainWindow, String nameOfInput, double value, UiUnits unit) {
        this(mainWindow, nameOfInput, value, 0, mainWindow.getController().convertUnit(new DimensionDTO(15, Units.FEET), unit.getUnit()).value(), unit);
    }

    public MeasurementInputField(MainWindow mainWindow, String nameOfInput, double value, double minimumValue, double maximumValue, UiUnits unit) {
        super(false);
        this.setBackground(null);
        this.setOpaque(false);
        this.unitConverter = mainWindow.getController();
        this.memorizer = mainWindow.getController();
        this.currentUnit = unit;
        this.minDimension = new DimensionDTO(minimumValue, unit.getUnit());
        this.maxDimension = new DimensionDTO(maximumValue, unit.getUnit());
        this.imperialFractionalNumberFormatter = new ImperialFractionalNumberFormatter(unitConverter);
        this.customNumberFormatter = new CustomNumberFormatter(new DecimalFormat());
        this.init(nameOfInput, value);
        setCurrentUnit(UIConfig.INSTANCE.getDefaultUnit());
    }

    private void init(String nameOfInput, double value) {
        BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);
        this.setLayout(layout);

        JLabel nameLabel = new JLabel(nameOfInput);
        nameLabel.setOpaque(false);
        nameLabel.setBorder(new FlatEmptyBorder());
        nameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        nameLabel.setBorder(new EmptyBorder(0, 0, 0, UIConfig.INSTANCE.getDefaultPadding()));

        this.numericInput = new JFormattedTextField(new UnitFormatFactory());
        this.numericInput.setColumns(5);
        this.numericInput.setBorder(new FlatRoundBorder());
        this.numericInput.setValue(value);
        this.numericInput.setBackground(UIManager.getColor("SubWindow.lightBackground2"));

        this.add(nameLabel);
        this.add(this.numericInput);
        this.unitComboBox = new JComboBox<>(UiUnits.values());
        this.unitComboBox.setSelectedItem(currentUnit);
        this.add(this.unitComboBox);

    }

    /**
     * Change the value of the numeric input field without triggering the listeners, otherwise i could create and infinite loop
     *
     * @param valueInMM the new value in MM
     */
    public void setValueInMMWithoutTrigerringListeners(double valueInMM) {
        DimensionDTO dValueInMM = new DimensionDTO(valueInMM, Units.MM);
        DimensionDTO valueInGoodUnit = unitConverter.convertUnit(dValueInMM, currentUnit.getUnit());

        PropertyChangeListener[] listeners = numericInput.getPropertyChangeListeners("value");

        // Temporarily remove the listeners
        for (PropertyChangeListener listener : listeners) {
            numericInput.removePropertyChangeListener("value", listener);
        }


        this.numericInput.setValue(valueInGoodUnit.value());

        // Reattach the listeners
        for (PropertyChangeListener listener : listeners) {
            numericInput.addPropertyChangeListener("value", listener);
        }
    }

    public JFormattedTextField getNumericInput() {
        return this.numericInput;
    }

    public double getMMValue() {
        return unitConverter.convertUnit(new DimensionDTO(((Number) numericInput.getValue()).doubleValue(), currentUnit.getUnit()), Units.MM).value();
    }

    public DimensionDTO getMaxDimension() {
        return maxDimension;
    }

    public void setMaxDimension(DimensionDTO maxDimension) {
        JFormattedTextField.AbstractFormatter formatter = numericInput.getFormatter();

        if (formatter instanceof CustomNumberFormatter numberFormatter) {
            numberFormatter.setMaximum(unitConverter.convertUnit(maxDimension, currentUnit.getUnit()).value());
        } else if (formatter instanceof ImperialFractionalNumberFormatter imperialFormatter) {
            imperialFormatter.setMaximum(unitConverter.convertUnit(maxDimension, currentUnit.getUnit()).value());
        }
        this.maxDimension = maxDimension;
    }

    public DimensionDTO getMinDimension() {
        return minDimension;
    }

    public void setMinDimension(DimensionDTO minDimension) {
        JFormattedTextField.AbstractFormatter formatter = numericInput.getFormatter();

        if (formatter instanceof CustomNumberFormatter numberFormatter) {
            numberFormatter.setMinimum(unitConverter.convertUnit(minDimension, currentUnit.getUnit()).value());
        } else if (formatter instanceof ImperialFractionalNumberFormatter imperialFormatter) {
            imperialFormatter.setMinimum(unitConverter.convertUnit(minDimension, currentUnit.getUnit()).value());
        }
        this.minDimension = minDimension;
    }

    public void setCurrentUnit(UiUnits newUnit) {
        double currentValue = ((Number) numericInput.getValue()).doubleValue();

        DimensionDTO result = unitConverter.convertUnit(new DimensionDTO(currentValue, currentUnit.getUnit()), newUnit.getUnit());

        this.currentUnit = newUnit;
        unitComboBox.setSelectedItem(currentUnit);

        numericInput.setValue(result.value());
        setMaxDimension(maxDimension);
        setMinDimension(minDimension);

    }

    private class UnitFormatFactory extends JFormattedTextField.AbstractFormatterFactory {

        @Override
        public JFormattedTextField.AbstractFormatter getFormatter(JFormattedTextField tf) {
            JFormattedTextField.AbstractFormatter factory;
            if (currentUnit == UiUnits.INCHES) {
                factory = imperialFractionalNumberFormatter;
            } else {
                factory = customNumberFormatter;
            }
            // Cache last value in case of bad input
            try {
                factory.stringToValue(tf.getText());
            } catch (ParseException ignored) {
            }
            return factory;
        }
    }

    private class CustomNumberFormatter extends NumberFormatter {
        private double lastValue = 0.0;
        private double min = -Double.MAX_VALUE;
        private double max = Double.MAX_VALUE;
        CustomNumberFormatter(NumberFormat format){
            super(format);
        }
        @Override
        public Object stringToValue(String text) {
            try {
                double newValue = Double.parseDouble(text);
                lastValue = Math.clamp(newValue, min, max);
            } catch (NumberFormatException ignored) {
            }
            return lastValue;
        }

        public void setMinimum(double minimum) {
            this.min = minimum;
            lastValue = Math.clamp(lastValue, min, max);
        }

        public void setMaximum(double maximum) {
            this.max = maximum;
            lastValue = Math.clamp(lastValue, min, max);
        }
    }
}
