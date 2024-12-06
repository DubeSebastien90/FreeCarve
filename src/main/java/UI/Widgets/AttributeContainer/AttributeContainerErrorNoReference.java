package UI.Widgets.AttributeContainer;

import Common.DTO.CutDTO;
import UI.MainWindow;
import UI.SubWindows.CutListPanel;
import UI.UIConfig;
import UI.Widgets.CutBox;

import java.awt.*;

public class AttributeContainerErrorNoReference extends  AttributeContainerError{
    public AttributeContainerErrorNoReference(MainWindow mainWindow, CutListPanel cutListPanel, CutDTO cutDTO, CutBox cutBox) {
        super(mainWindow, cutListPanel, cutDTO, cutBox, "Bad reference");
        appendSelectReference();
    }

    private void appendSelectReference(){
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = useGridIndex();
        gc.weightx = 1;
        gc.weighty = 1;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(modifyAnchorBox, gc);
    }
}
