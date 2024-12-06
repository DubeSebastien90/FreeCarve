package UI.Widgets;

import UI.MainWindow;
import UI.SubWindows.BasicWindow;
import UI.UIConfig;
import com.formdev.flatlaf.ui.FlatEmptyBorder;
import com.formdev.flatlaf.ui.FlatRoundBorder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.NumberFormatter;
import java.text.NumberFormat;

/**
 * Small widget class that offers an interface for inputting measurements
 *
 * @author Louis-Etienne Messier
 * @author Kamran Charles Nayebi
 * @since 2024-11-21
 */
public class PixelNoUnitInputField extends BasicWindow {
    private JFormattedTextField numericInput;
    private JLabel unitComboBox;

    private double maxDimension;
    private double minDimension;

    public PixelNoUnitInputField(MainWindow mainWindow, String nameOfInput, int value) {
        this(mainWindow, nameOfInput, value, 1, Integer.MAX_VALUE);
    }

    public PixelNoUnitInputField(MainWindow mainWindow, String nameOfInput, int value, int minimumValue, int maximumValue) {
        super(false);
        this.setBackground(null);
        this.setOpaque(false);

        this.minDimension = minimumValue;
        this.maxDimension = maximumValue;
        this.init(nameOfInput, value);
    }


    private void init(String nameOfInput, int value) {
        NumberFormat numberFormat = NumberFormat.getIntegerInstance();
        numberFormat.setGroupingUsed(false);

        NumberFormatter numberFormatter = new NumberFormatter(numberFormat);
        numberFormatter.setValueClass(Double.class);
        numberFormatter.setMinimum(Math.max(minDimension, 1));
        numberFormatter.setMaximum(Math.max(maxDimension, 0));


        BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);
        this.setLayout(layout);

        JLabel nameLabel = new JLabel(nameOfInput);
        nameLabel.setOpaque(false);
        nameLabel.setBorder(new FlatEmptyBorder());
        nameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        nameLabel.setBorder(new EmptyBorder(0, 0, 0, UIConfig.INSTANCE.getDefaultPadding()));


        this.numericInput = new JFormattedTextField(numberFormatter);
        this.numericInput.setColumns(5);
        this.numericInput.setBorder(new FlatRoundBorder());
        this.numericInput.setValue(value);
        this.numericInput.setBackground(UIManager.getColor("SubWindow.lightBackground2"));


        this.add(nameLabel);
        this.add(this.numericInput);
        this.unitComboBox = new JLabel("px");
        this.add(this.unitComboBox);
    }


    public JFormattedTextField getNumericInput() {
        return this.numericInput;
    }


    public double getMaxDimension() {
        return maxDimension;
    }

    public void setMaxDimension(double maxDimension) {
        this.maxDimension = maxDimension;
    }

    public double getMinDimension() {
        return minDimension;
    }

    public void setMinDimension(double minDimension) {
        this.minDimension = minDimension;
    }

    private void setMaxMinListener() {

    }

}
