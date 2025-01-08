package UI;

import UI.Listeners.LoadProjectActionListener;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Objects;

import static UI.UiUtil.createSVGButton;

/**
 * This UI class is used to display the first window the user uses when he enters the application.
 * This window give the opportunity to the user to open an existing project or to create a new one.
 *
 * @author Adam Côté
 * @since 2024-10-09
 */
public class FolderWindow extends JPanel {
    private final UIConfig uiConfig = UIConfig.INSTANCE;
    private final JButton newButton = createSpecialWindowButton("newFile", "Nouveau");
    private final JButton openButton = createSpecialWindowButton("openFile", "Ouvrir");
    private final JPanel westPanel = new JPanel(new GridBagLayout());
    private final JPanel eastPanel = new JPanel(new GridBagLayout());
    private final MainWindow mainWindow;
    private int compteur = 0;

    /**
     * Constructs the window with two panel, west and east. The west panel contains the open and new project button.
     * The east panel contains the recent project list.
     */
    public FolderWindow(MainWindow mainWindow) {
        super(new GridBagLayout());
        this.mainWindow = mainWindow;
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.gridy = 2;
        this.add(westPanel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(eastPanel, gbc);

        this.setVisible(true);
        init();
        setButtonActionListener();
    }

    /**
     * This function init the west and the east panel with their default values
     */
    private void init() {
        initButtonLeft();
        initRecentRight();
    }

    /**
     * Initiates the westPanel using a gridBagConstraint.
     */
    private void initButtonLeft() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 20, 40, 0);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;

        JLabel projets = new JLabel("Projets");
        projets.setFont(projets.getFont().deriveFont(40f));
        westPanel.add(projets, gbc);

        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() * 2, 0);
        newButton.setBorder(new EmptyBorder(UIConfig.INSTANCE.getDefaultPadding(), UIConfig.INSTANCE.getDefaultPadding(),
                UIConfig.INSTANCE.getDefaultPadding(), UIConfig.INSTANCE.getDefaultPadding()));
        westPanel.add(newButton, gbc);

        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        openButton.setBorder(new EmptyBorder(UIConfig.INSTANCE.getDefaultPadding(), UIConfig.INSTANCE.getDefaultPadding(),
                UIConfig.INSTANCE.getDefaultPadding(), UIConfig.INSTANCE.getDefaultPadding()));
        openButton.addActionListener(new LoadProjectActionListener(mainWindow));
        westPanel.add(openButton, gbc);
    }

    /**
     * Initiates the eastPanel using a GridBagConstraint
     */
    private void initRecentRight() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel projets = new JLabel("Free Carve", JLabel.CENTER);
        projets.setFont(projets.getFont().deriveFont(40f));

        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        eastPanel.add(projets, gbc);

        gbc.gridy = 2;
        JLabel iconLabel = new JLabel();
        iconLabel.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                compteur++;
                if (compteur == UIConfig.INSTANCE.getNbClicksBeforeMusic()){
                    System.out.println("Enjoy your music!");
                    playSound("secret.wav");
                }
            }
        });
        resizePane(iconLabel);
        iconLabel.setVerticalAlignment(JLabel.TOP);
        iconLabel.setHorizontalAlignment(JLabel.CENTER);
        eastPanel.add(iconLabel, gbc);
        eastPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizePane(iconLabel);
            }
        });
    }

    public static synchronized void playSound(final String url) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    InputStream inputStream = getClass().getResourceAsStream("Sounds/" + url);
                    if (inputStream == null) {
                        throw new IllegalArgumentException("Sound file not found: " + url);
                    }
                    BufferedInputStream bufferedIn = new BufferedInputStream(inputStream);
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
                    clip.open(audioStream);
                    clip.start();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }

    private void resizePane(JLabel jLabel) {
        int min = Math.min(eastPanel.getWidth(), eastPanel.getHeight());
        ImageIcon icon = UiUtil.getIcon("freecarve2", (int) (min/UIConfig.INSTANCE.getMagicIconNumber()));
        jLabel.setIcon(icon);
    }

    /**
     * Transforms a normal JButton into a button that resemble the svg button.
     *
     * @param iconName The name of the icon file in the ressources folder
     * @param text     the text displayed on the button
     * @return The awesome new Jbutton.
     */
    private JButton createSpecialWindowButton(String iconName, String text) {
        JButton button = createSVGButton(iconName, true, uiConfig.getProjectSelectionMenuButtonSize(), UIManager.getColor("Button.secondaryBackground"));
        button.setBorder(null);
        button.putClientProperty("JButton.buttonType", "toolBarButton");
        button.setText(text);
        button.setForeground(UIManager.getColor("Button.secondaryBackground"));
        button.setFont(button.getFont().deriveFont(20f));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        return button;
    }

    /**
     * Sets the event handler for the new project button. When the button is clicked,
     * it triggers the action to proceed to the next window in the main interface.
     */
    private void setButtonActionListener() {
        newButton.addActionListener(e -> mainWindow.showInProjectWindow());
        openButton.addActionListener(e -> mainWindow.showInProjectWindow());
    }
}
