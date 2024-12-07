package UI.Widgets.AttributeContainer;

import Common.DTO.CutDTO;
import UI.MainWindow;
import UI.SubWindows.CutListPanel;
import UI.UIConfig;
import UI.Widgets.CutBox;

import java.awt.*;

public class AttributeContainerErrorBadBit extends AttributeContainerError{


    public AttributeContainerErrorBadBit(MainWindow mainWindow, CutListPanel cutListPanel, CutDTO cutDTO, CutBox cutBox) {
        super(mainWindow, cutListPanel, cutDTO, cutBox, "The selected bit is invalid");
        appendBitSelection();
    }

    @Override
    public void setupEventListeners() {
        addEventListenerToBitChoiceBox(bitChoiceBox);
    }

    private void appendBitSelection(){
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = useGridIndex();
        gc.weightx = 1;
        gc.weighty = 1;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(bitChoiceBox, gc);
    }


    @Override
    public void updatePanel(CutDTO newCutDTO) {
        cutDTO = new CutDTO(newCutDTO);
        System.out.println("new bit index" + cutDTO.getBitIndex());
        bitChoiceBox.refresh(mainWindow.getController().getConfiguredBitsMap(), cutDTO.getBitIndex());
    }




}
