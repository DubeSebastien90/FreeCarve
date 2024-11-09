package UI.Widgets;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.function.Consumer;

/**
 * NumberTextField is a JTextArea designed for numeric input only. It accepts positive numbers greater than zero
 * and notifies a listener when the user finishes input (by pressing Enter or losing focus).
 *
 * @author Adam Côté
 * @version 1.0
 * @since 2024-11-02
 */
public class NumberTextField extends JTextArea {

    private double maxNumber = Double.POSITIVE_INFINITY;
    private double minNumber = Double.NEGATIVE_INFINITY;

    /**
     * Constructs a NumberTextField that applies a specified function when the user finishes entering a valid number.
     *
     * @param onFinishInput A Consumer that takes a Double value from the input field when input is finalized
     */
    public NumberTextField(Consumer<Double> onFinishInput) {
        setDocument(new NumericDocument());
        addInputFinishListener(onFinishInput);
    }

    /**
     * Constructs a NumberTextField with an initial text, enforcing numeric input.
     *
     * @param text          The initial text to display
     * @param onFinishInput A Consumer that takes a Double value from the input field when input is finalized
     */
    public NumberTextField(String text, Consumer<Double> onFinishInput) {
        super(text);
        setDocument(new NumericDocument());
        this.setText(text);
        addInputFinishListener(onFinishInput);
    }

    public void setMaximumNumber(double maxNumber) {
        this.maxNumber = maxNumber;
    }

    public void setMinimumNumber(double minNumber) {
        this.minNumber = minNumber;
    }

    /**
     * Adds listeners to detect when the user has finished entering input.
     * The function is triggered on Enter key press or focus loss.
     *
     * @param onFinishInput The function to execute with the entered number upon input finalization
     */
    private void addInputFinishListener(Consumer<Double> onFinishInput) {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n' && getText().length() > 0) {
                    NumberTextField.this.getRootPane().requestFocus();
                }
            }
        });

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (getText().length() > 0) {
                    try {
                        double num = Double.parseDouble(getText());
                        num = Math.max(Math.min(num, NumberTextField.this.maxNumber), NumberTextField.this.minNumber);
                        setText("" + num);
                        onFinishInput.accept(num);
                    } catch (NumberFormatException ex) {
                        Toolkit.getDefaultToolkit().beep();
                    }
                }
            }
        });
    }

    /**
     * NumericDocument restricts the input to numbers only, defined by a specific regex pattern.
     */
    static class NumericDocument extends PlainDocument {
        private static final String NUMBER_REGEX = "^(0*([1-9]\\d*|0)\\.?\\d*|0\\.\\d*[1-9]\\d*)$";

        @Override
        public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
            if (str != null) {
                String newText = getText(0, offset) + str + getText(offset, getLength() - offset);
                if (newText.matches(NUMBER_REGEX)) {
                    super.insertString(offset, str, attr);
                } else if (!str.equals("\n")) {
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        }
    }
}
