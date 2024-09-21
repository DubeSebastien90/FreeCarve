package UI;
import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
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

    /**
     * Constructs a {@code MainWindow} instance by activating the LaF, initializing
     * it's attributes and setting up all the relevant event listeners functions
     */
    public MainWindow(){
        this.activateFlatLaf();
        this.init();
        this.setupEventListener();
    }

    /**
     * Starts the {@code MainWindow} by making it's {@code JFrame} visible
     */
    public void start() {frame.setVisible(true);}

    private void activateFlatLaf(){
        try{
            UIManager.setLookAndFeel(new FlatDarkLaf());
        }
        catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
    }

    private void init(){
        frame = new JFrame();
        uiConfig = UIConfig.INSTANCE;
        borderLayout = new BorderLayout();

        frame.setTitle(uiConfig.getWindowTitle());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(uiConfig.getDefaultWindowWidth(), uiConfig.getDefaultWindowHeight());
        frame.setLocationRelativeTo(null);

        frame.setLayout(borderLayout);
    }

    private void setupEventListener(){
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {

            }
        });
    }
}
