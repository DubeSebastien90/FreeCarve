package UI;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.Objects;

public class Utils {

    public static File chooseFile(String title, String fileDefaultName, Component parent, File startingDirectory, String fileDescription, String... fileExtensions) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setCurrentDirectory(Objects.requireNonNullElseGet(startingDirectory, () -> new File(".")));
        chooser.setSelectedFile(new File(fileDefaultName));
        chooser.setDialogTitle(title);
        chooser.addChoosableFileFilter(new FileNameExtensionFilter(fileDescription, fileExtensions));
        chooser.setAcceptAllFileFilterUsed(false);
        if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        } else {
            return null;
        }
    }

    public static File saveFile(String title, String fileDefaultName, Component parent, File startingDirectory, String fileDescription,  String... fileExtensions) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setCurrentDirectory(Objects.requireNonNullElseGet(startingDirectory, () -> new File(".")));
        chooser.setSelectedFile(new File(fileDefaultName));
        chooser.setDialogTitle(title);
        chooser.addChoosableFileFilter(new FileNameExtensionFilter(fileDescription, fileExtensions));
        chooser.setAcceptAllFileFilterUsed(false);
        if (chooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        } else {
            return null;
        }
    }


}
