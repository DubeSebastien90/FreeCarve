package UI.Widgets;

import UI.UIConfig;
import UI.UiUnits;
import com.formdev.flatlaf.ui.FlatEmptyBorder;
import com.formdev.flatlaf.ui.FlatRoundBorder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.DecimalFormat;

public class ErrorBox extends GenericAttributeBox{

    JLabel labelError;

    public ErrorBox(boolean haveBackground, String name, String error) {
        super(haveBackground, name);
        this.init(error);
        this.setError();
    }

    private void init(String error){
        GridBagConstraints gc = new GridBagConstraints();
        this.labelError = new JLabel(error);
        gc.gridx = 0;
        gc.gridy = 1;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 1;
        this.add(labelError, gc);
    }
}
