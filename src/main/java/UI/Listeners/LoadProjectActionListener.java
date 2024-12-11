package UI.Listeners;

import Common.DTO.VertexDTO;
import Common.Exceptions.InvalidFileExtensionException;
import UI.Display2D.Rendering2DWindow;
import UI.Events.ChangeAttributeEvent;
import UI.FileCache;
import UI.MainWindow;
import UI.Utils;
import UI.Widgets.ChooseDimension;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class LoadProjectActionListener implements ActionListener {
    MainWindow mainWindow;

    public LoadProjectActionListener(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        File file = Utils.chooseFile("Charger projet", "Projet.PAN", mainWindow.getFrame(), FileCache.INSTANCE.getLastProjectSave(), "Fichier de projet (PAN)", "PAN");
        if (file != null) {
            try {
                mainWindow.getController().loadProject(file);
            } catch (InvalidFileExtensionException ex) {
                System.out.println("Invalid file extension");
            } catch (IOException ex) {
                System.out.println("Unable to load file");
            } catch (ClassNotFoundException ex) {
                System.out.println("File content is not valid");
            }
            Rendering2DWindow rend = mainWindow.getMiddleContent().getConfigChoiceWindow().getRendering2DWindow();
            rend.getAttributeListener().changeAttributeEventOccurred(new ChangeAttributeEvent(rend, new ChooseDimension(rend, mainWindow.getController().getGrid().isActive())));
            mainWindow.getMiddleContent().getCutWindow().notifyObservers();
            VertexDTO v = mainWindow.getController().getPanelDTO().getPanelDimension();
            mainWindow.getMiddleContent().getConfigChoiceWindow().getRendering2DWindow().resizePanneau(v.getX(), v.getY());
            FileCache.INSTANCE.setLastProjectSave(file);
        }
    }
}
