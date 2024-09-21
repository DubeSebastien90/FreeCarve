import UI.*;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.formdev.flatlaf.FlatDarkLaf;

public class App {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel( new FlatDarkLaf() );
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }

        MainWindow mainWindow = MainWindow.INSTANCE;
        mainWindow.start();
    }
}
