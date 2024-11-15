package Domain;

import Common.DTO.VertexDTO;

import java.util.List;

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
        List<Cut> cuts = state.getPanel().getCutList();
        StringBuilder instructions = new StringBuilder();

        //definition of constants
        String rotationSpeed = "S12000"; // rotation speed
        String movementSpeed = "F500"; // movement speed
        final String lineEnd = ";\n";

        //initiate CNC State
        instructions.append("G21" + lineEnd); //mm
        instructions.append("G17" + lineEnd); //xy plane
        instructions.append("G28" + lineEnd); //go to (0,0)

        List<Cut> cutlist = state.getPanel().getCutList();
        for (Cut cut : cutlist) {
            for(VertexDTO vertex : cut.getPoints()){
                instructions.append("");
            }
        }
        //end program
        instructions.append("M05" + lineEnd); // stops the bit rotation
        instructions.append("G28" + lineEnd); // return to (0,0)
        instructions.append("M02" + lineEnd); //end of the program

        return instructions.toString();
    }
}

