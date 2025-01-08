package UI;

import Domain.Controller;
import UI.Listeners.SaveDispatcher;
import UI.Listeners.UndoRedoDispatcher;
import UI.Widgets.AttributionRefactorWindow;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;

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
public class MainWindow {

    private JFrame frame;
    private final JPanel mainInsidePanel = new JPanel();
    private UIConfig uiConfig;
    private DownBar downBar;
    private TopBar topBar;
    private LeftBar leftBar;
    private MiddleContent middleContent;
    private Controller controller;
    private boolean inProject = false;

    /**
     * Starts the {@code MainWindow} by making it's {@code JFrame} visible
     */
    public void start() {
        this.activateFlatLaf();
        this.init();
        this.setupEventListener();
        frame.setVisible(true);
        try {
            OptionWindow.loadOptions(this);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
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
        this.controller = Controller.initialize();

        ToolTipManager.sharedInstance().setInitialDelay(1000);
        frame = new JFrame();
        uiConfig = UIConfig.INSTANCE;
        BorderLayout borderLayout = new BorderLayout();
        mainInsidePanel.setLayout(borderLayout);
        downBar = new DownBar(this);
        topBar = new TopBar(this);
        leftBar = new LeftBar(this);
        middleContent = new MiddleContent(this);

        frame.setTitle(uiConfig.getWindowTitle());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(uiConfig.getDefaultWindowWidth(), uiConfig.getDefaultWindowHeight());
        frame.setLocationRelativeTo(null);
        frame.setJMenuBar(topBar);
        ImageIcon image = UiUtil.getIcon("freecarve2", 300);
        frame.setIconImage(image.getImage());

        mainInsidePanel.add(leftBar, BorderLayout.WEST);
        mainInsidePanel.add(middleContent.getPanel(), BorderLayout.CENTER);
        mainInsidePanel.add(downBar.getDownBar(), BorderLayout.SOUTH);
        frame.add(mainInsidePanel);
        frame.setFocusable(true);
        showFileSelectionWindow();
        controller.flushAllUndoRedo();
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

    public Controller getController() {
        return this.controller;
    }

    /**
     * Displays the options window by replacing the current content pane
     * with an instance of {@link OptionWindow}.
     */
    public void showOptionWindow() {
        frame.setContentPane(new OptionWindow(this));
        frame.revalidate();
    }

    public void showAttributionWindow() {
        frame.setContentPane(new AttributionRefactorWindow(this));
        frame.revalidate();
    }

    public void showHelp(){
        frame.setContentPane(new HelpWindow(this));
        frame.revalidate();
    }

    /**
     * Displays the File selection/ project selection window with an instance of {@link FolderWindow }
     */
    public void showFileSelectionWindow() {
        inProject = false;
        frame.setSize(700, 700);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(new FolderWindow(this));
        getController().resetPanelCNC();
        getMiddleContent().getCutWindow().notifyObservers();
        frame.revalidate();
    }

    /**
     * Restores the main content of the application by setting the content
     * pane back to the main inside panel.
     */
    public void showTrueMode() {
        if (inProject) {
            showInProjectWindow();
        } else {
            showFileSelectionWindow();
        }
    }

    public void showInProjectWindow() {
        inProject = true;
        frame.setContentPane(mainInsidePanel);
        frame.setSize(uiConfig.getDefaultWindowWidth(), uiConfig.getDefaultWindowHeight());
        frame.setLocationRelativeTo(null);
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

        DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new UndoRedoDispatcher(this,getController()));
        DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new SaveDispatcher(this));
    }
}
