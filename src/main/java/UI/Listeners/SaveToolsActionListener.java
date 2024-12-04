package UI.Listeners;

import Common.Exceptions.InvalidFileExtensionException;
import UI.FileCache;
import UI.MainWindow;
import UI.Utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class SaveToolsActionListener implements ActionListener {
    MainWindow mainWindow;
    public SaveToolsActionListener(MainWindow mainWindow){
        this.mainWindow = mainWindow;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String nomProjet = FileCache.INSTANCE.getLastProjectSave() == null ? "SansNom_" : FileCache.INSTANCE.getLastProjectSave().getName().split("\\.")[0] + "_";
        File file = Utils.saveFile("Exporter", nomProjet + "tools.CNC", mainWindow.getFrame(), FileCache.INSTANCE.getLastToolSave(), "Fichier outils (CNC)", "CNC");
        if (file != null) {
            if (!file.getName().contains(".CNC")){
                file = new File(file.getParentFile().getPath(), file.getName()+".CNC");
            }
            try {
                mainWindow.getController().saveTools(file);
            } catch (InvalidFileExtensionException ex) {
                System.out.println("The file extension is invalid");
            } catch (IOException ex) {
                System.out.println("Unable to save file at this location");
            }
            FileCache.INSTANCE.setLastToolSave(file);
        }
    }

    public void silentSave() {
        File file = FileCache.INSTANCE.getLastToolSave();
        if (file == null){
            file = new File("defaut.CNC");
        }
        try {
            mainWindow.getController().saveTools(file);
        } catch (InvalidFileExtensionException ex) {
            System.out.println("The file extension is invalid");
        } catch (IOException ex) {
            System.out.println("Unable to save file at this location");
        }
        FileCache.INSTANCE.setLastToolSave(file);
    }
}
