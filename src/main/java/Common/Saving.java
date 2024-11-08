package Common;

import javax.swing.*;
import java.io.*;
import java.util.Optional;

public class Saving {
    public static void saveMainScreen(Renderer mainScreen, JFrame frame) {
        Optional<String> response = openFileExplorer(frame, JFileChooser.FILES_AND_DIRECTORIES);
        if (response.isPresent()) {
            File path = new File(response.get());
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(path);
                ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);
                outputStream.writeObject(mainScreen);
                outputStream.close();
            } catch (FileNotFoundException notFoundException) {
                notFoundException.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Optional<String> openFileExplorer(JFrame frame, int selectionMode) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(selectionMode);
        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            return Optional.of(fileChooser.getSelectedFile().getPath());
        }
        return Optional.empty();
    }
}
