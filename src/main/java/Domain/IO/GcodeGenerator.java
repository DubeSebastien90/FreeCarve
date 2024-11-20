package Domain.IO;

import Common.DTO.CutDTO;
import Common.DTO.ProjectStateDTO;
import Common.DTO.VertexDTO;

import java.util.List;

/**
 * The {@code GCodeGenerator} class regroup classes which interact with the Gcode of the project.
 *
 * @author Adam Côté
 * @version 1.0
 * @since 2024-10-20
 */
public class GcodeGenerator {

    /**
     * Converts a {@code ProjectState} into a series of GCode instructions. These instructions can later be saved as a file.
     *
     * @param state The {@code ProjectState} which needs to be converted into GCode
     * @return The {@code String equivalent of the GCode}
     */
    public static String convertToGCode(ProjectStateDTO state) {
        StringBuilder instructions = new StringBuilder();

        //definition of constants
        final String rotationSpeed = "S1200"; // rotation speed
        final String movementSpeed = "F50"; // movement speed
        final String lineEnd = ";\n";

        //initiate CNC State
        instructions.append("G21" + lineEnd); //mm
        instructions.append("G17" + lineEnd); //xy plane
        instructions.append("G28" + lineEnd); //go to (0,0)
        instructions.append("G90" + lineEnd); //Absolute position used for the program
        instructions.append("G92 Z0" + lineEnd);
        instructions.append(movementSpeed + lineEnd); //Define speed of the CNC

        List<CutDTO> cutlist = state.getPanelDTO().getCutsDTO();
        for (CutDTO cut : cutlist) {
            instructions.append("T").append(cut.getBitIndex() + 1).append(" M06").append(lineEnd); //select the tool
            for (VertexDTO vertex : cut.getPoints()) {
                if (vertex == cut.getPoints().get(0)) {
                    instructions.append("G00 X").append(vertex.getX()).append(" Y").append(vertex.getY()).append(lineEnd); //go to position of first point
                    instructions.append("M03 " + rotationSpeed + lineEnd); //starts the rotation of the tool
                    instructions.append("G82 X").append(vertex.getX()).append(" Y").append(vertex.getY()).append(" Z").append(-cut.getDepth()).append(lineEnd); //drill the first hole
                } else {
                    instructions.append("G09 X").append(vertex.getX()).append(" Y").append(vertex.getY()).append(" Z").append(-cut.getDepth()).append(lineEnd); //cut to the point location
                }
            }
            instructions.append("M05" + lineEnd); //stop the tool
            instructions.append("G00 Z0" + lineEnd); //go to the predefined Z safe spot
        }
        //end program
        instructions.append("M05" + lineEnd); // stops the bit rotation
        instructions.append("G28" + lineEnd); // return to (0,0)
        instructions.append("G00 X0 Y0" + lineEnd);
        instructions.append("M02" + lineEnd); //end of the program

        return instructions.toString();
    }
}
