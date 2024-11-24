package UI.Widgets.AttributeContainer;

import Common.DTO.CutDTO;
import UI.MainWindow;
import UI.SubWindows.CutListPanel;
import UI.UIConfig;
import UI.Widgets.CutBox;
import UI.Widgets.ErrorBox;
import org.w3c.dom.Attr;

import java.awt.*;

public class AttributeContainerError extends AttributeContainer {

    ErrorBox errorBox;

    public AttributeContainerError(MainWindow mainWindow, CutListPanel cutListPanel, CutDTO cutDTO, CutBox cutBox) {
        super(mainWindow, cutListPanel, cutDTO, cutBox);
        init_attribute(mainWindow, cutDTO);
        init_layout();
    }

    private void init_attribute(MainWindow mainWindow, CutDTO cutDTO){
        errorBox = new ErrorBox(true, "Error", "Coupe Invalide");
    }

    private void init_layout(){
        setBackground(null);
        setOpaque(false);
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gc = new GridBagConstraints();
        setLayout(layout);
        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(errorBox, gc);
    }

    @Override
    public void setupEventListeners() {

    }

    @Override
    public void updatePanel(CutDTO newCutDTO) {

    }
}
