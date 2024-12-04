package UI.Listeners;

import Common.Exceptions.InvalidFileExtensionException;
import UI.FileCache;
import UI.MainWindow;

import javax.sound.midi.Soundbank;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class SaveProjectActionListener implements ActionListener {
    MainWindow mainWindow;
    public SaveProjectActionListener(MainWindow mainWindow){
        this.mainWindow = mainWindow;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        File lastSave = FileCache.INSTANCE.getLastProjectSave();
        if (lastSave == null){
            new SaveProjectAsActionListener(mainWindow).actionPerformed(e);
        } else {
            try {
                mainWindow.getController().saveProject(lastSave);
            } catch (InvalidFileExtensionException ex) {
                System.out.println("The file extension is invalid");
            } catch (IOException ex) {
                System.out.println("Unable to save file at this location");
            }
        }
    }
}
