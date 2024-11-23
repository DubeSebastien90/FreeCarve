package UI.Widgets.AttributeContainer;

import Common.DTO.CutDTO;
import UI.MainWindow;
import UI.SubWindows.BasicWindow;
import UI.SubWindows.CutListPanel;
import UI.Widgets.CutBox;

public abstract class AttributeContainer extends BasicWindow {

    protected MainWindow mainWindow;
    protected CutListPanel cutListPanel;
    protected CutDTO cutDTO;
    protected CutBox cutBox;


    public AttributeContainer(MainWindow mainWindow, CutListPanel cutListPanel, CutDTO cutDTO, CutBox cutBox) {
        super(true);
        this.mainWindow = mainWindow;
        this.cutListPanel = cutListPanel;
        this.cutDTO = cutDTO;
        this.cutBox = cutBox;
    }

    public abstract void setupEventListeners();
}
