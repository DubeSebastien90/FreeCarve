package UI.Listeners;

import Common.Exceptions.InvalidFileExtensionException;
import UI.FileCache;
import UI.MainWindow;
import UI.Utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class SaveProjectAsActionListener implements ActionListener {
    MainWindow mainWindow;
    public SaveProjectAsActionListener(MainWindow mainWindow){
        this.mainWindow = mainWindow;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        File file = Utils.saveFile("Enregistrer sous", "Projet.PAN", mainWindow.getFrame(), FileCache.INSTANCE.getLastProjectSave(), "Fichier de projet (PAN)", "PAN");
        if (file != null) {
            if (!file.getName().contains(".PAN")){
                file = new File(file.getParentFile().getPath(), file.getName()+".PAN");
            }
            try {
                mainWindow.getController().saveProject(file);
            } catch (InvalidFileExtensionException ex) {
                System.out.println("The file extension is invalid");
            } catch (IOException ex) {
                System.out.println("Unable to save file at this location");
            }
            FileCache.INSTANCE.setLastProjectSave(file);
        }
    }
}
