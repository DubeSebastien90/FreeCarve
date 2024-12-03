package Domain.IO;

import Common.DTO.PanelDTO;

import java.io.*;

/**
 * The {@code ProjectFileManager} class regroup functions which interact with files on the user's computer
 *
 * @author Adam Côté
 * @author Kamran Charles Nayebi
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
    public static void saveProject(String path, PanelDTO panelDTO) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path))) {
            out.writeObject(panelDTO);
        }
    }

    /**
     * @return A {@code ProjectState} if the user chose a valid file.
     */
    public static PanelDTO loadProject(String path) throws IOException, ClassNotFoundException {
        PanelDTO panelDTO;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(path))) {
            panelDTO = (PanelDTO) in.readObject();
        }
        return panelDTO;
    }
}
