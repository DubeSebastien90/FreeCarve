package Domain.IO;

import Common.DTO.ProjectStateDTO;

import java.io.FileWriter;
import java.io.IOException;

public class ProjectFileManager {

    /**
     * Saves a string representing a gcode to the specified path file.
     *
     * @param path  The path of the file where the gcode will be written.
     * @param gcode The GCode that needs to be written.
     */
    public static void saveGcode(String path, String gcode) {
        try {
            FileWriter myWriter = new FileWriter(path);
            myWriter.write(gcode);
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
