package UI.Widgets.AttributeContainer;

import Common.CutState;
import Common.DTO.CutDTO;
import Common.InvalidCutState;
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
        else{
            return new AttributeContainerVertical(mainWindow, cutListPanel, cutDTO, cutBox);
        }
    }

    public static AttributeContainerError createError(MainWindow mainWindow, CutListPanel cutListPanel, CutDTO cutDTO, CutBox cutBox){

        if(cutDTO.getInvalidCutState() == InvalidCutState.BAD_BIT){
            return new AttributeContainerErrorBadBit(mainWindow, cutListPanel, cutDTO, cutBox);
        }
        else if(cutDTO.getInvalidCutState() == InvalidCutState.NO_REFERENCE){
            return new AttributeContainerErrorNoReference(mainWindow, cutListPanel, cutDTO, cutBox);
        }
        else if(cutDTO.getInvalidCutState() == InvalidCutState.OUT_OF_BOUND){
            return new AttributeContainerErrorOutOfBound(mainWindow, cutListPanel, cutDTO, cutBox);
        }
        return new AttributeContainerError(mainWindow, cutListPanel, cutDTO, cutBox, "Invalid cut");
    }

}
