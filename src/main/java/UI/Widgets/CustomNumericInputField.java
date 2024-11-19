package UI.Widgets;

import UI.SubWindows.BasicWindow;
import UI.UIConfig;
import UI.UiUnits;
import com.formdev.flatlaf.ui.FlatEmptyBorder;
import com.formdev.flatlaf.ui.FlatRoundBorder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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

    CustomNumericInputField(String nameOfInput, double value){
        super(false);
        this.setBackground(null);
        this.setOpaque(false);
        this.init(nameOfInput, value);
    }

    private void init(String nameOfInput, double value){
        layout = new BoxLayout(this, BoxLayout.X_AXIS);
        this.setLayout(layout);

        this.nameOfInput = new JLabel(nameOfInput);
        this.nameOfInput.setOpaque(true);
        this.nameOfInput.setBackground(UIManager.getColor("SubWindow.lightBackground1"));
        this.nameOfInput.setBorder(new FlatEmptyBorder());
        this.nameOfInput.setHorizontalAlignment(SwingConstants.RIGHT);
        this.nameOfInput.setBorder(new EmptyBorder(0, 0 ,0 , UIConfig.INSTANCE.getDefaultPadding()));

        NumberFormat numberFormat = DecimalFormat.getNumberInstance();
        this.numericInput = new JFormattedTextField(numberFormat);
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
