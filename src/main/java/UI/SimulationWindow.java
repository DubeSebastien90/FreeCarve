package UI;

import UI.SubWindows.BasicWindow;
import UI.SubWindows.Rendering3DWindow;
import UI.Widgets.BigButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimulationWindow extends JPanel {

    private final BigButton nextButton = new BigButton("Suivant");
    private Rendering3DWindow rendering3DWindow;
    private final MainWindow mainWindow;

    public SimulationWindow(MainWindow mainWindow) {
        this.setLayout(new GridBagLayout());
        this.mainWindow = mainWindow;
        init();
        setButtonEventHandler();
    }

    public Rendering3DWindow getRenderer() {
        return rendering3DWindow;
    }

    public void init() {
        GridBagConstraints gbc = new GridBagConstraints();

        rendering3DWindow = (new Rendering3DWindow(mainWindow.getController().getCameraId(), mainWindow));
        mainWindow.getController().setScene();
        gbc.insets = new Insets(0, 0, 0, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weightx = 5;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(rendering3DWindow, gbc);
        gbc.insets = new Insets(0, 0, 0, 5);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.weightx = 3;
        gbc.weighty = 0.80;
        gbc.fill = GridBagConstraints.BOTH;
        add(new BasicWindow(true), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        gbc.weightx = 3;
        gbc.weighty = 0.20;
        gbc.fill = GridBagConstraints.BOTH;
        add(nextButton, gbc);
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        nextButton.revalidate();
    }

    private void setButtonEventHandler() {
        nextButton.getButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainWindow.getMiddleContent().nextWindow();
            }
        });
    }
}
