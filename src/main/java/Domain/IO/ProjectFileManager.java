package Domain.IO;

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
     * Saves the {@code ProjectState passed as an argument as a file on the user's computer}. This function will ask the user the location and the name of the saved file.
     *
     * @param state The {@code ProjectState} which needs to be saved.
     */
    void saveProject(ProjectStateDTO state) {
        //todo, must call choosePath()
    }

    /**
     * Asks the user to select a location on its computer.
     *
     * @return The absolute path to go to the chosen location.
     */
    String choosePath() {
        //todo
        return "";
    }

    /**
     * @return A {@code ProjectState} if the user chose a valid file.
     */
    void openProject() {
        //todo, must call choosePath()
    }
}
