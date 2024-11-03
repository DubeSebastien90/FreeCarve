import UI.*;

import javax.swing.*;

import com.formdev.flatlaf.FlatDarkLaf;


public class App {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }

        MainWindow mainWindow = new MainWindow();
        mainWindow.start();
    }
}
