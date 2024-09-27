package UI;

import javax.swing.*;
import java.awt.*;

public enum Project {
    INSTANCE;
    private final ProjectMiddleWindow projectMiddleWindow = new ProjectMiddleWindow();
    private final ProjectFolderWindow projectFolderWindow = new ProjectFolderWindow();

    public ProjectFolderWindow getProjectFolderWindow() {
        return projectFolderWindow;
    }

    public ProjectMiddleWindow getProjectMiddleWindow() {
        return projectMiddleWindow;
    }



    public static class ProjectMiddleWindow {
        private int windowAt = 0;

        public int getWindowAt() {
            return windowAt;
        }

        public void setWindowAt(int windowAt) {
            //TODO rajouter action changer de fenetre
            this.windowAt = windowAt;
        }
    }

    public static class ProjectFolderWindow {

    }

    public static class ProjectCutWindow {

    }

    public static class ProjectSimulationWindow {

    }

    public static class ProjectParamWindow {

    }

    public static class ProjectExportWindow {

    }
}
