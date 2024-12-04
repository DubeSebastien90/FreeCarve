package UI;

import java.io.File;

public enum FileCache {
    INSTANCE;

    private File lastProjectSave = null;
    private File lastToolSave = null;
    private File lastGCodeSave = null;

    public File getLastProjectSave() {
        return lastProjectSave;
    }

    public void setLastProjectSave(File lastProjectSave) {
        this.lastProjectSave = lastProjectSave;
    }

    public File getLastToolSave() {
        return lastToolSave;
    }

    public void setLastToolSave(File lastToolSave) {
        this.lastToolSave = lastToolSave;
    }

    public File getLastGCodeSave() {
        return lastGCodeSave;
    }

    public void setLastGCodeSave(File lastGCodeSave) {
        this.lastGCodeSave = lastGCodeSave;
    }
}
