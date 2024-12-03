package Domain.IO;

import Common.DTO.PanelDTO;
import Domain.TestHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ProjectFileManagerTest {

    @BeforeAll
    static void setup(){
        new File("test").mkdir();
    }

    @AfterAll
    static void tearDown(){
        File file = new File("test");
        String[] entries = file.list();
        for (String s : entries) {
            File currentFile = new File(file.getPath(), s);
            currentFile.delete();
        }
        file.delete();
    }

    @Test
    void filePath_WriteInto_GoodThingWrote() {
        //Arrange
        String path = "test/TestFile.txt";
        String texte = "Gateau\nau\nBananes";

        //Act
        ProjectFileManager.saveString(path, texte);

        //Assert
        StringBuilder actualText = new StringBuilder();
        try {
            List<String> lines = Files.readAllLines(Paths.get("test/TestFile.txt"));
            for (String line : lines) {
                actualText.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Assertions.assertEquals(texte.replaceAll("\n", ""), actualText.toString());
    }

    @Test
    void saveProject_ValidPath_WritesFile() throws IOException {
        //Arrange
        String path = "test/saveProject_ValidPath.PAN";
        PanelDTO panelDTO = TestHelper.createPanelDTO();

        //Act
        ProjectFileManager.saveProject(path, panelDTO);

        //Assert
        Assertions.assertTrue(new File(path).exists());
    }

    @Test
    void loadProject_ValidPath_ReadsDTOFromFile() throws IOException, ClassNotFoundException {
        //Arrange
        String path = "test/loadProject_ValidPath.PAN";
        PanelDTO panelDTO = TestHelper.createPanelDTO();

        //Act
        ProjectFileManager.saveProject(path, panelDTO);
        PanelDTO result = ProjectFileManager.loadProject(path);

        //Assert
        Assertions.assertEquals(panelDTO, result);
    }

}
