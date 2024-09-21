package UI;

import javax.swing.*;

public class ProjectWindow extends JPanel {
    private final UIConfig uiConfig = UIConfig.INSTANCE;

    public ProjectWindow() {
        this.setVisible(true);
        init();
    }

    private void init() {
        add(new JButton("Créer projet"));
    }
}
