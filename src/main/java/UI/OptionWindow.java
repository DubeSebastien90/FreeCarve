package UI;

import javax.swing.*;
import java.awt.*;

public class OptionWindow extends JPanel {
    public OptionWindow() {
        setLayout(new FlowLayout());
        add(new JLabel("This is the options window"));
        JButton gobck = new JButton("GOBACK PLEASE");
        gobck.addActionListener(e -> MainWindow.INSTANCE.showTrueMode());
        add(gobck);
        add(new JButton("Option 2"));
    }

}
