package UI;

import static Util.UiUtil.createSVGButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class FolderWindow extends JPanel {
    private final UIConfig uiConfig = UIConfig.INSTANCE;
    private final Project.ProjectFolderWindow thisState = Project.INSTANCE.getProjectFolderWindow();
    private final JPanel westPanel = new JPanel(new GridBagLayout());
    private final JPanel eastPanel = new JPanel(new GridBagLayout());
    private final JScrollPane scrollRecentProject = new JScrollPane();
    private final Box recentProject = Box.createVerticalBox();

    public FolderWindow() {
        super(new BorderLayout());

        this.add(westPanel, BorderLayout.WEST);
        this.add(eastPanel, BorderLayout.EAST);
        this.setVisible(true);
        init();
    }

    private void init() {
        initButtonLeft();
        initRecentRight();
    }

    private void initButtonLeft() {
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.weightx = 1;
        gbc.weighty = 0;

        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 150, 40, 0);
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel projets = new JLabel("Projets");
        projets.setFont(projets.getFont().deriveFont(40f));
        westPanel.add(projets, gbc);


        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 150, 20, 0);  // Espacement de 15 pixels en bas
        westPanel.add(createSpecialWindowButton("newFile", "Nouveau"), gbc);

        gbc.gridy = 3;
        westPanel.add(createSpecialWindowButton("openFile", "ouvrir"), gbc);
    }

    private void initRecentRight() {

        scrollRecentProject.setViewportView(recentProject);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weightx = 1.0;
        gbc.gridwidth = 2;

        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.BOTH;
        //TODO mettre des mesures pas dememe
        gbc.insets = new Insets(25, 0, 20, 200);

        JLabel projets = new JLabel("Récents");
        projets.setSize(50, 50);
        projets.setFont(projets.getFont().deriveFont(40f));
        eastPanel.add(projets, gbc);

        gbc.insets = new Insets(0, 0, 0, 200);
        gbc.weighty = 1.0;
        eastPanel.add(scrollRecentProject, gbc);
        addRecentProject("no Recent Project");

//TODO pas mettre des valeurs fixes
        scrollRecentProject.setSize(100, 500);
        scrollRecentProject.setMaximumSize(new Dimension(100, 500));

        gbc.insets = new Insets(0, 0, 50, 200);
        eastPanel.add(Box.createVerticalGlue(), gbc);
    }

    public void addRecentProject(String projectName) {
        JLabel project = new JLabel(projectName);
        project.setBorder(new EmptyBorder(5, 0, 5, 0));
        recentProject.add(project);
    }

    public void removeRecentProject(int index) {
        recentProject.remove(index);
    }

    private JButton createSpecialWindowButton(String iconName, String text) {
        JButton button = createSVGButton(iconName, true, uiConfig.getProjectSelectionMenuButtonSize(), UIManager.getColor("Button.secondaryBackground"));
        button.setBorder(null);
        button.putClientProperty("JButton.buttonType", "toolBarButton");
        button.setText(text);
        button.setForeground(UIManager.getColor("Button.secondaryBackground"));
        button.setFont(button.getFont().deriveFont(20f));
        return button;
    }
}
