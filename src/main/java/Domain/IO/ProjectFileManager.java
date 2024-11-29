package Domain.IO;

import Common.DTO.PanelDTO;
import Common.DTO.ProjectStateDTO;

import java.io.FileWriter;
import java.io.IOException;

/**
 * The {@code ProjectFileManager} class regroup functions which interact with files on the user's personal computer
 *
 * @author Adam Côté
 * @version 1.0
 * @since 2024-10-20
 */
public class ProjectFileManager {

    /**
     * Saves a string representing a random text to the specified path file.
     *
     * @param path The path of the file where the text will be written.
     * @param text The text that needs to be written.
     */
    public static void saveString(String path, String text) {
        try {
            FileWriter myWriter = new FileWriter(path);
            myWriter.write(text);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Saves the {@code PanelCNC} passed as an argument as a file on the user's computer.
     *
     * @param panelDTO The {@code PanelCNC} which needs to be saved.
     */
    void saveProject(PanelDTO panelDTO) {

    }

    /**
     * @return A {@code ProjectState} if the user chose a valid file.
     */
    void openProject() {

    }
}
