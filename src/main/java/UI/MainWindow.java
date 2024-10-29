package UI;

import Domain.Controller;
import UI.SubWindows.Rendering2DWindow;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.*;

/**
 * This {@code MainWindow} class encapsulates the main frame of the application.
 * To start it, run the {@link #start()} method.
 *
 * <p>This singleton class handles the initialization of UI components,
 * configuration of the Look and Feel (LaF), and manages the main application
 * window's layout and event listeners.</p>
 *
 * @author Louis-Etienne Messier
 * @version 1.1
 * @since 2024-09-21
 */
public enum MainWindow {
    INSTANCE;

    private JFrame frame;
    private final JPanel mainInsidePanel = new JPanel();
    private UIConfig uiConfig;
    private DownBar downBar;
    private TopBar topBar;
    private LeftBar leftBar;
    private MiddleContent middleContent;
    private Controller controller;

    /**
     * Starts the {@code MainWindow} by making its {@code JFrame} visible.
     * <p>This method activates the Look and Feel, initializes all components,
     * sets up event listeners, and finally makes the main window visible.</p>
     */
    public void start() {
        this.activateFlatLaf();
        this.init();
        this.setupEventListener();
        frame.setVisible(true);
    }

    /**
     * Activates the Look and Feel (LaF) for the application.
     * <p>This method sets the LaF to FlatDarkLaf and registers the custom
     * defaults source. If the LaF fails to initialize, an error message is
     * printed to the standard error stream.</p>
     */
    private void activateFlatLaf() {
        FlatLaf.registerCustomDefaultsSource("themes");
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
    }

    /**
     * Initializes all components of the {@code MainWindow}.
     * <p>This method sets up the main frame, configures its attributes,
     * and adds all necessary panels and menus to the frame.</p>
     */
    private void init() {
        this.controller = new Controller();
        ToolTipManager.sharedInstance().setInitialDelay(1000);
        frame = new JFrame();
        uiConfig = UIConfig.INSTANCE;
        BorderLayout borderLayout = new BorderLayout();
        mainInsidePanel.setLayout(borderLayout);
        downBar = new DownBar();
        topBar = new TopBar();
        leftBar = new LeftBar();
        middleContent = new MiddleContent(this);

        frame.setTitle(uiConfig.getWindowTitle());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(uiConfig.getDefaultWindowWidth(), uiConfig.getDefaultWindowHeight());
        frame.setLocationRelativeTo(null);
        frame.setJMenuBar(topBar);

        mainInsidePanel.add(leftBar, BorderLayout.WEST);
        mainInsidePanel.add(middleContent.getPanel(), BorderLayout.CENTER);
        mainInsidePanel.add(downBar.getDownBar(), BorderLayout.SOUTH);
        frame.add(mainInsidePanel);
    }

    /**
     * Returns the controller associated with this {@code MainWindow}.
     *
     * @return the {@link Controller} instance managing application logic.
     */
    public Controller getController() {
        return controller;
    }

    /**
     * Returns the main {@code JFrame} of the application.
     *
     * @return the {@link JFrame} instance for the application.
     */
    public JFrame getFrame() {
        return frame;
    }

    /**
     * Returns the {@code LeftBar} of this {@code MainWindow}.
     *
     * @return the {@link LeftBar} instance of this window.
     */
    public LeftBar getLeftBar() {
        return this.leftBar;
    }

    /**
     * Returns the {@code DownBar} of this {@code MainWindow}.
     *
     * @return the {@link DownBar} instance of this window.
     */
    public DownBar getDownBar() {
        return this.downBar;
    }

    /**
     * Returns the {@code MiddleContent} of this {@code MainWindow}.
     *
     * @return the {@link MiddleContent} instance of this window.
     */
    public MiddleContent getMiddleContent() {
        return this.middleContent;
    }

    /**
     * Displays the options window by replacing the current content pane
     * with an instance of {@link OptionWindow}.
     */
    public void showOptionWindow() {
        frame.setContentPane(new OptionWindow());
        frame.revalidate();
    }

    /**
     * Restores the main content of the application by setting the content
     * pane back to the main inside panel.
     */
    public void showTrueMode() {
        frame.setContentPane(mainInsidePanel);
        frame.revalidate();
    }

    /**
     * Sets up all event listeners related to the {@code MainWindow}.
     * <p>This method currently sets up a component listener to respond to
     * frame resizing events.</p>
     */
    private void setupEventListener() {
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Handle resizing if needed
            }
        });
    }
}
