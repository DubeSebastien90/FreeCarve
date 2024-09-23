package UI;

import com.formdev.flatlaf.FlatDarkLaf;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;

import javax.swing.*;
import javax.swing.border.Border;

/**
 * This {@code MainWindow} class encapsulates the main frame of the application.
 * To start it, run the start method.
 *
 * @author Louis-Etienne Messier
 * @version 0.1
 * @since 2024-09-21
 */
public class MainWindow {
    private JFrame frame;
    private UIConfig uiConfig;
    private BorderLayout borderLayout;
    private DownBar downBar;
    private TopBar topBar;
    private LeftBar leftBar;
    private MiddleContent middleContent;

    /**
     * Constructs a {@code MainWindow} instance by activating the LaF, initializing
     * it's attributes and setting up all the relevant event listeners functions
     */
    public MainWindow() {
        this.activateFlatLaf();
        this.init();
        this.setupEventListener();
    }

    /**
     * Starts the {@code MainWindow} by making it's {@code JFrame} visible
     */
    public void start() {
        frame.setVisible(true);
    }

    /**
     * Activates the LaF
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
     * Initiates all of the {@code MainWindow} components
     */
    private void init() {
        ToolTipManager.sharedInstance().setInitialDelay(1000);
        frame = new JFrame();
        uiConfig = UIConfig.INSTANCE;
        borderLayout = new BorderLayout();
        downBar = new DownBar();
        topBar = new TopBar();
        leftBar = new LeftBar();
        middleContent = new MiddleContent();

        frame.setTitle(uiConfig.getWindowTitle());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(uiConfig.getDefaultWindowWidth(), uiConfig.getDefaultWindowHeight());
        frame.setLocationRelativeTo(null);

        frame.setLayout(borderLayout);
        frame.setJMenuBar(topBar);
        frame.add(leftBar, BorderLayout.WEST);
        frame.add(middleContent.getMiddleContent(), BorderLayout.CENTER);
        frame.add(downBar.getDownBar(), BorderLayout.SOUTH);
    }

    /**
     * @return The leftBar of this mainWindow
     */
    public LeftBar getLeftBar() {
        return this.leftBar;
    }

    /**
     * Setup of all event listeners relating to the {@code MainWindow}
     */
    private void setupEventListener() {
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {

            }
        });
    }
}
