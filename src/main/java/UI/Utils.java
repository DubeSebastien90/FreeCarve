package UI;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class Utils {

    public static String chooseFile(String title, String fileDefaultName, Component parent, String fileDescription, String... fileExtensions) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setSelectedFile(new File(fileDefaultName));
        chooser.setDialogTitle(title);
        chooser.addChoosableFileFilter(new FileNameExtensionFilter(fileDescription, fileExtensions));
        chooser.setAcceptAllFileFilterUsed(false);
        if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile().getAbsolutePath();
        } else {
            return null;
        }
    }


}
