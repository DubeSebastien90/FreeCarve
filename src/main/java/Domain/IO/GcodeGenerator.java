package Domain.IO;

import Common.CutState;
import Common.DTO.CutDTO;
import Common.DTO.PanelDTO;
import Common.DTO.VertexDTO;
import Domain.Controller;
import Domain.CutType;

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
     * @param controller The Controller which contain the {@code PanelDTO} which needs to be converted into GCode
     * @return The {@code String equivalent of the GCode}
     */
    public static String convertToGCode(Controller controller) {
        StringBuilder instructions = new StringBuilder();
        PanelDTO panelDTO = controller.getPanelDTO();

        //definition of constants
        final String rotationSpeed = "S" + controller.getCNCrotationSpeed(); // rotation speed
        final String movementSpeed = "F" + controller.getCNCCuttingSpeed(); // movement speed
        final String lineEnd = ";\n";

        //initiate CNC State
        instructions.append("G21" + lineEnd); //mm
        instructions.append("G17" + lineEnd); //xy plane
        instructions.append("G28" + lineEnd); //go to (0,0)
        instructions.append("G90" + lineEnd); //Absolute position used for the program
        instructions.append("G92 Z0" + lineEnd);
        instructions.append(movementSpeed).append(lineEnd); //Define speed of the CNC

        List<CutDTO> cutlist = panelDTO.getCutsDTO();
        for (CutDTO cut : cutlist) {
            if (cut.getState() == CutState.VALID && cut.getCutType() != CutType.CLAMP) {
                instructions.append("T").append(cut.getBitIndex() + 1).append(" M06").append(lineEnd); //select the tool
                int cmp = 0;
                for (VertexDTO vertex : controller.getAbsolutePointsPosition(cut)) {
                    double cutX = Math.max(Math.min(vertex.getX(), panelDTO.getPanelDimension().getX()), 0);
                    double cutY = Math.max(Math.min(vertex.getY(), panelDTO.getPanelDimension().getY()), 0);
                    double dept = -cut.getDepth() - 0.5;
                    if (cmp == 0) {
                        instructions.append("G00 X").append(cutX).append(" Y").append(cutY).append(lineEnd); //go to position of first point
                        instructions.append("M03 ").append(rotationSpeed).append(lineEnd); //starts the rotation of the tool
                        instructions.append("G82 X").append(cutX).append(" Y").append(cutY).append(" Z").append(dept).append(lineEnd); //drill the first hole
                        instructions.append("G01 X").append(cutX).append(" Y").append(cutY).append(" Z").append(dept).append(lineEnd); //cut to the point location
                        cmp++;
                    } else {
                        instructions.append("G01 X").append(cutX).append(" Y").append(cutY).append(" Z").append(dept).append(lineEnd); //cut to the point location
                    }
                }
                instructions.append("G01 Z0" + lineEnd); //go to the predefined Z safe spot
                instructions.append("M05" + lineEnd); //stop the tool
            }
        }
        //end program
        instructions.append("G28" + lineEnd); // return to (0,0)
        instructions.append("G00 X0 Y0" + lineEnd);
        instructions.append("M02" + lineEnd); //end of the program

        return instructions.toString();
    }
}
