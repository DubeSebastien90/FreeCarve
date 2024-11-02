package UI;

import Domain.ThirdDimension.Mesh;
import Domain.ThirdDimension.Vertex;
import UI.SubWindows.BasicWindow;
import UI.Widgets.BigButton;
import UI.SubWindows.Renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class SimulationWindow extends JPanel {

    private final BigButton nextButton = new BigButton("Suivant");
    private Renderer renderer;

    public SimulationWindow() {
        this.setLayout(new GridBagLayout());
        init();
        setButtonEventHandler();
    }

    public Renderer getRenderer() {
        return renderer;
    }

    public void init() {
        GridBagConstraints gbc = new GridBagConstraints();

        renderer = (new Renderer(MainWindow.INSTANCE.getController().getCameraId()));
        gbc.insets = new Insets(0, 0, 0, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weightx = 5;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(renderer, gbc);
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
                MainWindow.INSTANCE.getMiddleContent().nextWindow();
            }
        });
    }
}
