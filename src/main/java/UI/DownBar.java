package UI;



import UI.Widgets.ColoredBox;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * The {@code DownBar} class encapsulates the UI elements of the navigation bar
 * at the bottom of the screen, allowing users to toggle between relevant menus
 *
 * @author Louis-Etienne Messier
 * @version 0.1
 * @since 2024-09-21
 */
public class DownBar {
    private JPanel panel;
    private JScrollPane scrollPane;
    private JPanel container;
    private BoxLayout horizontalLayout;

    private JButton folderButton;
    private JButton canvasButton;
    private JButton bitsButton;
    private JButton cutButton;
    private JButton simulationButton;
    private JButton exportButton;
    ArrayList<JButton> buttons;

    private ColoredBox box1;


    public DownBar(){
        init();
    }

    /**
     * @return the panel container of the {@code DownBar}
     */
    public JScrollPane getDownBar(){
        return scrollPane;
    }

    /**
     * Initiates all of the {@code DownBar} components
     */
    private void init()
    {
        panel = new JPanel();
        scrollPane = new JScrollPane();
        container = new JPanel();
        horizontalLayout = new BoxLayout(container, BoxLayout.X_AXIS);

        folderButton = new JButton("Fichier");
        folderButton.setToolTipText("Menu fichier");
        canvasButton = new JButton("Canvas");
        canvasButton.setToolTipText("Menu canvas");
        bitsButton = new JButton("Outils");
        bitsButton.setToolTipText("Menu outils");
        cutButton = new JButton("Coupes");
        cutButton.setToolTipText("Menu coupes");
        simulationButton = new JButton("Simulation");
        simulationButton.setToolTipText("Menu simulation");
        exportButton = new JButton("Exportation");
        exportButton.setToolTipText("Menu exportation");
        box1 = new ColoredBox(Color.CYAN, 30, 50);

        buttons = new ArrayList<JButton>();
        buttons.add(folderButton);
        buttons.add(canvasButton);
        buttons.add(bitsButton);
        buttons.add(cutButton);
        buttons.add(simulationButton);
        buttons.add(exportButton);

        panel.setBackground(Color.RED);
        container.setBackground(Color.BLUE);
        container.setLayout(horizontalLayout);
        scrollPane.setViewportView(panel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        for (JButton button : buttons) {
            button.setMargin(new Insets(5, 20, 5, 20));
            container.add(button);
        }
        container.add(box1);
        panel.add(container);

    }
}
