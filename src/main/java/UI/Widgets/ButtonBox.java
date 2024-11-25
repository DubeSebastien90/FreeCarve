package UI.Widgets;

import UI.MainWindow;

import javax.swing.*;
import java.awt.*;

public class ButtonBox extends GenericAttributeBox{
    private JButton button;

    public ButtonBox(MainWindow mainWindow, boolean haveBackground, String name, String buttonName){
        super(haveBackground, name);
        this.init(mainWindow, buttonName);
    }

    private void init(MainWindow mainWindow, String buttonName){
        GridBagConstraints gc = new GridBagConstraints();
        this.button = new JButton(buttonName);
        gc.gridx = 0;
        gc.gridy = 1;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 1;
        this.add(button, gc);
    }

    public JButton getInput(){
        return this.button;
    }
}
