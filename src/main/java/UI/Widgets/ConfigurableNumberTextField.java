package UI.Widgets;

import UI.UiUnits;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class ConfigurableNumberTextField extends JPanel {
    NumberTextField numberTextField;
    JComboBox<UiUnits> unitComboBox;

    /**
     * Constructs a NumberTextField with an initial text, enforcing numeric input.
     *
     * @param text          The initial text to display
     * @param onFinishInput A Consumer that takes a Double value from the input field when input is finalized
     */
    public ConfigurableNumberTextField(String text, Consumer<Double> onFinishInput) {
        numberTextField = new NumberTextField(text, onFinishInput);
        unitComboBox = new JComboBox<>(UiUnits.values());
        init();
    }

    public NumberTextField getNumberTextField(){
        return numberTextField;
    }

    void init(){
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        setBorder(BorderFactory.createLineBorder(Color.RED));

        add(numberTextField);
        add(unitComboBox);
    }
}
