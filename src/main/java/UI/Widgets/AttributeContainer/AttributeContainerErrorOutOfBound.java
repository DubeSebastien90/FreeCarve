package UI.Widgets.AttributeContainer;

import Common.DTO.CutDTO;
import UI.MainWindow;
import UI.SubWindows.CutListPanel;
import UI.UIConfig;
import UI.Widgets.CutBox;

import java.awt.*;

public class AttributeContainerErrorOutOfBound extends  AttributeContainerError{
    public AttributeContainerErrorOutOfBound(MainWindow mainWindow, CutListPanel cutListPanel, CutDTO cutDTO, CutBox cutBox) {
        super(mainWindow, cutListPanel, cutDTO, cutBox, "Cut is out of bounds");
        appendMove();
    }

    private void appendMove(){

    }
}
