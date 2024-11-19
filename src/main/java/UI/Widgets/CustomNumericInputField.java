package UI.Widgets;

import UI.SubWindows.BasicWindow;
import UI.UIConfig;
import UI.UiUnits;
import com.formdev.flatlaf.ui.FlatEmptyBorder;
import com.formdev.flatlaf.ui.FlatRoundBorder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.NumberFormatter;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Small widget class that regroups all the functionalities of a custom numeric input field
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

    CustomNumericInputField(String nameOfInput, double value) {
        this(nameOfInput, value, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
    }

    CustomNumericInputField(String nameOfInput, double value, double minimumValue, double maximumValue) {
        super(false);
        this.setBackground(null);
        this.setOpaque(false);
        this.init(nameOfInput, value);
        this.minNumber = minimumValue;
        this.maxNumber = maximumValue;
    }

    private void init(String nameOfInput, double value){
        layout = new BoxLayout(this, BoxLayout.X_AXIS);
        this.setLayout(layout);

        this.nameOfInput = new JLabel(nameOfInput);
        this.nameOfInput.setOpaque(true);
        this.nameOfInput.setBackground(UIManager.getColor("SubWindow.lightBackground1"));
        this.nameOfInput.setBorder(new FlatEmptyBorder());
        this.nameOfInput.setHorizontalAlignment(SwingConstants.RIGHT);
        this.nameOfInput.setBorder(new EmptyBorder(0, 0, 0 , UIConfig.INSTANCE.getDefaultPadding()));

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

        this.add(this.nameOfInput);
        this.add(this.numericInput);
        this.add(this.unitComboBox);
    }

    public JFormattedTextField getNumericInput(){return this.numericInput;}
}
