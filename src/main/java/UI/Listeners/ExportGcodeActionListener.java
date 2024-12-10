package UI.Listeners;

import UI.FileCache;
import UI.MainWindow;
import UI.Utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ExportGcodeActionListener implements ActionListener {
    MainWindow mainWindow;

    public ExportGcodeActionListener(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        mainWindow.getMiddleContent().getExportWindow().getRenderer().grabFocus();
        String nomProjet = FileCache.INSTANCE.getLastProjectSave() == null ? "SansNom_" : FileCache.INSTANCE.getLastProjectSave().getName().split("\\.")[0] + "_";
        File file = Utils.chooseFile("Exporter", nomProjet + "gcode.GCODE", mainWindow.getFrame(), FileCache.INSTANCE.getLastGCodeSave(), "Gcode files", "gcode");
        if (file != null) {
            if (!file.getName().contains(".GCODE")) {
                file = new File(file.getParentFile().getPath(), file.getName() + ".GCODE");
            }
            mainWindow.getController().saveGcode(file);
            FileCache.INSTANCE.setLastGCodeSave(file);
        }
    }
}
