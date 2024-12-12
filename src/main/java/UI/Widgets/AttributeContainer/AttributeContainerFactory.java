package UI.Widgets.AttributeContainer;

import Common.CutState;
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
        else if(cutDTO.getCutType() == CutType.RECTANGULAR){
            return  new AttributeContainerRectangle(mainWindow, cutListPanel, cutDTO, cutBox);
        }
        else if(cutDTO.getCutType() == CutType.RETAILLER){
            return new AttributeContainerBorder(mainWindow, cutListPanel, cutDTO, cutBox);
        }
        else if(cutDTO.getCutType() == CutType.L_SHAPE){
            return new AttributeContainerCutL(mainWindow, cutListPanel, cutDTO, cutBox);
        }
        else if(cutDTO.getCutType() == CutType.LINE_FREE){
            return new AttributeContainerFree(mainWindow, cutListPanel, cutDTO, cutBox);
        }
        else if(cutDTO.getCutType() == CutType.CLAMP){
            return new AttributeContainerClamp(mainWindow, cutListPanel, cutDTO, cutBox);
        }
        else{
            return new AttributeContainerVertical(mainWindow, cutListPanel, cutDTO, cutBox);
        }
    }

}
