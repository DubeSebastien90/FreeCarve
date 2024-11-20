package Domain.IO;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ProjectFileManagerTest {

    @Test
    void filePath_WriteInto_GoodThingWrote() {
        String path = "TestFile.txt";
        String texte = "Gateau\nau\nBananes";
        ProjectFileManager.saveString(path, texte);
        StringBuilder actualText = new StringBuilder();
        try {
            List<String> lines = Files.readAllLines(Paths.get("TestFile.txt"));
            for (String line : lines) {
                actualText.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Assertions.assertEquals(texte.replaceAll("\n", ""), actualText.toString());
    }
}
