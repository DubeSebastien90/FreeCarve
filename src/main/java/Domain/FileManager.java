package Domain;

/**
 * The {@code FileManager} class regroup functions which interact with files on the user's personal computer
 *
 * @author Adam Côté
 * @version 1.0
 * @since 2024-10-20
 */
class FileManager {

    /**
     * Saves the {@code ProjectState passed as an argument as a file on the user's computer}. This function will ask the user the location and the name of the saved file.
     *
     * @param state The {@code ProjectState} which needs to be saved.
     */
    void saveProject(ProjectState state) {
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
    ProjectState openProject() {
        //todo, must call choosePath()
        return null;
    }

    /**
     * Converts a {@code ProjectState} into a series of GCode instructions. These instructions can later be saved as a file.
     *
     * @param state The {@code ProjectState} which needs to be converted into GCode
     * @return The {@code String equivalent of the GCode}
     */
    String convertToGCode(ProjectState state) {
        //todo
        return "";
    }
}
