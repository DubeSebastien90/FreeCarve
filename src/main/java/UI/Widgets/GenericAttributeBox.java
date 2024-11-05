package UI.Widgets;

import UI.SubWindows.BasicWindow;
import UI.UIConfig;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Generic class for all of the possible fiels in the Attribute tab, gives it the
 * basic background, the name and the appropriate layout
 *
 * @author Louis-Etienne Messier
 * @version 0.1
 * @since 2024-10-30
 */

public class GenericAttributeBox extends BasicWindow {
    protected GridBagLayout layout;
    private JLabel name;

    public GenericAttributeBox(boolean haveBackground, String name) {
        super(haveBackground);
        this.init(name);
    }

    private void init(String name){
        layout = new GridBagLayout();
        GridBagConstraints gc = new GridBagConstraints();
        this.setLayout(layout);
        this.setBorder(new EmptyBorder(UIConfig.INSTANCE.getDefaultPadding(), UIConfig.INSTANCE.getDefaultPadding(),
                UIConfig.INSTANCE.getDefaultPadding(), UIConfig.INSTANCE.getDefaultPadding()));
        this.setBackground(UIManager.getColor("SubWindow.lightBackground1"));
        this.name = new JLabel(name);
        this.name.setBorder(new EmptyBorder(0, 0, UIConfig.INSTANCE.getDefaultPadding(), 0));
        gc.gridx = 0; gc.gridy = 0;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1;
        this.add(this.name, gc);
    }
}
