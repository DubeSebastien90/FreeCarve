package UI.Widgets.AttributeContainer;

import Common.CutState;
import Common.DTO.CutDTO;
import Common.InvalidCutState;
import UI.MainWindow;
import UI.SubWindows.CutListPanel;
import UI.UIConfig;
import UI.Widgets.CutBox;
import UI.Widgets.ErrorBox;
import org.w3c.dom.Attr;

import java.util.ArrayList;
import java.util.List;
import java.awt.*;

public class AttributeContainerError extends AttributeContainer {

    List<ErrorBox> errorBoxes;

    public AttributeContainerError(MainWindow mainWindow, CutListPanel cutListPanel, CutDTO cutDTO, CutBox cutBox) {
        super(mainWindow, cutListPanel, cutDTO, cutBox);
        init_attribute(mainWindow, cutDTO);
        init_layout();
    }


    private void init_attribute(MainWindow mainWindow, CutDTO cutDTO){
        errorBoxes = new ArrayList<>();
        List<InvalidCutState> invalidCutState =  mainWindow.getController().getInvalidCutStates(cutDTO.getId());
        for(InvalidCutState state : invalidCutState){
            errorBoxes.add(new ErrorBox(true, "Erreur", generateErrorMessage(state)));
        }
    }

    private String generateErrorMessage(InvalidCutState invalidCutState){
        switch (invalidCutState){
            case CLAMPED -> {
                return "Coupe dans une zone non valide";
            }
            case INVALID_REF -> {
                return "Référence non valide pour la coupe";
            }
            case INVALID_BIT -> {
                return "Index de l'outil sélectionné n'existe pas";
            }
            default -> {
                return "Coupe Invalide";
            }
        }
    }

    private void init_layout(){
        setBackground(null);
        setOpaque(false);
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gc = new GridBagConstraints();
        setLayout(layout);

        for(int i =0; i < errorBoxes.size(); i++){
            gc.gridx = 0;
            gc.gridy = i;
            gc.weightx = 1;
            gc.weighty = 1;
            gc.fill = GridBagConstraints.HORIZONTAL;
            gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
            add(errorBoxes.get(i), gc);
        }

    }

    @Override
    public void setupEventListeners() {

    }

    @Override
    public void updatePanel(CutDTO newCutDTO) {

    }
}
