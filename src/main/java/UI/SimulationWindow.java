package UI;

import Domain.Mesh;
import Domain.Plane;
import Domain.Pyramid;
import Domain.Vertex;
import UI.SubWindows.ConfigWindow;
import UI.SubWindows.CutList;
import UI.SubWindows.Rendering2DWindow;
import UI.Widgets.BigButton;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SimulationWindow extends JPanel {

    public SimulationWindow() {
        this.setLayout(new GridBagLayout());
        init();
    }

    public void init() {
        GridBagConstraints gbc = new GridBagConstraints();

        Mesh cubeBleu = new Domain.Box(new Vertex(200, 200, 200), 100, 100, 30, Color.BLUE);
        Mesh cubeRouge = new Domain.Box(new Vertex(0, 0, 0), 75, 75, 75, Color.RED);
        Mesh pyramidVerte = new Pyramid(new Vertex(-110, 110, 110), Color.GREEN);
        Mesh ground = new Plane(new Vertex(0, 0, 0), 1000, Color.white);
        Renderer rend = (new Renderer(new ArrayList<Mesh>(List.of(cubeRouge, cubeBleu, pyramidVerte, ground))));
        CutList cutList = new CutList();
        gbc.insets = new Insets(0, 0, 0, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weightx = 5;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(rend, gbc);
        gbc.insets = new Insets(0, 0, 0, 5);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.weightx = 3;
        gbc.weighty = 0.80;
        gbc.fill = GridBagConstraints.BOTH;
        add(cutList.getCutList(), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        gbc.weightx = 3;
        gbc.weighty = 0.20;
        gbc.fill = GridBagConstraints.BOTH;
        add(new BigButton("Suivant"), gbc);
    }
}
