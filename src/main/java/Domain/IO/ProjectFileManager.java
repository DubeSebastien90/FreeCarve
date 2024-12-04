package Domain.IO;

import Common.DTO.BitDTO;
import Common.DTO.PanelDTO;
import Common.Exceptions.InvalidFileExtensionException;

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
     * @param location File where the text will be written.
     * @param text The text that needs to be written.
     */
    public static void saveString(File location, String text) {
        try {
            FileWriter myWriter = new FileWriter(location);
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
     * @param location Valid writable file location
     */
    public static void saveProject(File location, PanelDTO panelDTO) throws IOException, InvalidFileExtensionException {
        if (!location.getName().contains(".PAN"))
            throw new InvalidFileExtensionException("File " + location.getName() + " does not have the PAN extension");

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(location))) {
            out.writeObject(panelDTO);
        }
    }

    /**
     * Loads the project from a file
     *
     * @param location Valid readable file location
     * @return A {@code ProjectState} if the user chose a valid file.
     */
    public static PanelDTO loadProject(File location) throws IOException, ClassNotFoundException, InvalidFileExtensionException {
        if (!location.getName().contains(".PAN"))
            throw new InvalidFileExtensionException("File " + location.getName() + " does not have the PAN extension");

        PanelDTO panelDTO;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(location))) {
            panelDTO = (PanelDTO) in.readObject();
        }

        return panelDTO;
    }

    /**
     * Saves the bits of the machine to the specified file location
     *
     * @param location valid writable file location
     * @param bits bits to save
     * @throws IOException if unable to write to location
     */
    public static void saveBits(File location, BitDTO[] bits) throws IOException, InvalidFileExtensionException {
        if (!location.getName().contains(".CNC"))
            throw new InvalidFileExtensionException("File " + location.getName() + " does not have the CNC extension");

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(location))) {
            out.writeObject(bits);
        }
    }

    /**
     * Loads the bits from the specified file location
     *
     * @param location readable file that is of the right format
     * @return list of bitDTOs that was stored
     * @throws IOException if unable to read file
     * @throws InvalidFileExtensionException if the file has the wrong extension
     * @throws ClassNotFoundException if the file is in the wrong structure
     */
    public static BitDTO[] loadBits(File location) throws IOException, ClassNotFoundException, InvalidFileExtensionException {
        if (!location.getName().contains(".CNC"))
            throw new InvalidFileExtensionException("File " + location.getName() + " does not have the CNC extension");

        BitDTO[] bitDTOS;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(location))) {
            bitDTOS = (BitDTO[]) in.readObject();
        }
        return bitDTOS;
    }
}
