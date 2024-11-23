package UI.Widgets.AttributeContainer;

import Common.DTO.CutDTO;
import Domain.CutType;
import UI.MainWindow;
import UI.SubWindows.CutListPanel;
import UI.Widgets.CutBox;

public class AttributeContainerFactory {

    public static AttributeContainer create(MainWindow mainWindow, CutListPanel cutListPanel, CutDTO cutDTO, CutBox cutBox){
        if(cutDTO.getCutType() == CutType.LINE_VERTICAL){
            return new AttributeContainerVertical(mainWindow, cutListPanel, cutDTO, cutBox);
        }
        else if(cutDTO.getCutType() == CutType.LINE_HORIZONTAL){
            return new AttributeContainerHorizontal(mainWindow, cutListPanel, cutDTO, cutBox);
        }
        else{
            return new AttributeContainerVertical(mainWindow, cutListPanel, cutDTO, cutBox);
        }
    }

}
