package UI.Widgets;

import Common.DTO.DimensionDTO;
import Common.Interfaces.IUnitConverter;
import Common.Units;
import UI.MainWindow;
import UI.SubWindows.BasicWindow;
import UI.UIConfig;
import UI.UiUnits;
import com.formdev.flatlaf.ui.FlatEmptyBorder;
import com.formdev.flatlaf.ui.FlatRoundBorder;
import com.sun.tools.javac.Main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.NumberFormatter;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Small widget class that regroups all the functionalities of a custom numeric input field
 *
 * @author Louis-Etienne Messier
 * @version 0.1
 * @since 2024-10-27
 */
public class CustomNumericInputField extends BasicWindow {
    private JLabel nameOfInput;
    private JFormattedTextField numericInput;
    private JComboBox<UiUnits> unitComboBox;
    private BoxLayout layout;
    private double maxNumber;
    private double minNumber;
    private IUnitConverter unitConverter;
    private UiUnits currentUnit;

    CustomNumericInputField(IUnitConverter unitConverter, String nameOfInput, double value) {
        this(unitConverter, nameOfInput, value, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
    }

    CustomNumericInputField(IUnitConverter unitConverter, String nameOfInput, double value, double minimumValue, double maximumValue) {
        super(false);
        this.setBackground(null);
        this.setOpaque(false);
        this.minNumber = minimumValue;
        this.maxNumber = maximumValue;
        this.init(nameOfInput, value);
        this.unitConverter = unitConverter;
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

        NumberFormat numberFormat = DecimalFormat.getNumberInstance();
        NumberFormatter numberFormatter = new NumberFormatter(numberFormat);
        numberFormatter.setMaximum(maxNumber);
        numberFormatter.setMinimum(minNumber);
        this.numericInput = new JFormattedTextField(numberFormatter);
        this.numericInput.setColumns(10);
        this.numericInput.setBorder(new FlatRoundBorder());
        this.numericInput.setValue(value);
        this.numericInput.setBackground(UIManager.getColor("SubWindow.lightBackground2"));

        this.unitComboBox = new JComboBox<>(UiUnits.values());
        this.unitComboBox.addItemListener(new UnitChangeListener());
        currentUnit = (UiUnits) unitComboBox.getSelectedItem();

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
                double currentValue = ((Number)numericInput.getValue()).doubleValue();

                DimensionDTO result = unitConverter.convertUnit(new DimensionDTO(currentValue, currentUnit.getUnit()), newUnit.getUnit());

                currentUnit = newUnit;
                numericInput.setValue(result.value());
            }
        }
    }
}
