package UI.SubWindows;

import UI.UIConfig;
import UI.Widgets.Attributable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * The {@code CutList} class is a UI class that encapsulates the
 * versatile modification of attributes panel
 * @author Louis-Etienne Messier
 * @version 0.1
 * @since 2024-10-23
 */

public class AttributePanel extends BasicWindow{
    private JPanel panel;
    private JScrollPane scrollPane;
    private GridBagLayout layout;

    /**
     * Constructor of the parent {@code BasicWindow}
     * @param haveBackground
     */
    public AttributePanel(boolean haveBackground){
        super(haveBackground);
        this.init();
    }

    /**
     * Update the current attributes of the {@AttributePanel} this means
     * it updates the name and the custom overriden attributes of the {@Attributable}
     * @param newAttributable {@Attributable} that wants to be showned
     */
    public void updateAttribute(Attributable newAttributable){
        this.panel.removeAll();
        GridBagConstraints gc = new GridBagConstraints();

        if(newAttributable != null){
            gc.anchor = GridBagConstraints.NORTH;
            gc.gridx = 0; gc.gridy = 0;
            gc.fill = GridBagConstraints.HORIZONTAL;
            gc.weightx = 1;
            this.panel.add(newAttributable.showName(), gc);

            gc.gridx = 0; gc.gridy = 1;
            gc.fill = GridBagConstraints.HORIZONTAL;
            gc.weightx = 1;
            this.panel.add(newAttributable.showAttribute(), gc);

            gc.gridx = 0; gc.gridy = 2;
            gc.insets = new Insets(0, 0, 0,  0);
            gc.weighty = 1;
            gc.fill = GridBagConstraints.VERTICAL;
            JPanel dummy = new JPanel();
            dummy.setBackground(null); dummy.setOpaque(false);
            this.panel.add(dummy, gc); // dummy container to fill out vertical space
        }

        this.panel.revalidate();
        this.panel.repaint();
    }

    /**
     * Inits the component
     */
    private void init(){
        panel = new JPanel();
        layout = new GridBagLayout();
        scrollPane = new JScrollPane(panel);
        this.setupHeader("Attributs", scrollPane);
        panel.setLayout(layout);
        panel.setBorder(new EmptyBorder(UIConfig.INSTANCE.getDefaultPadding(), UIConfig.INSTANCE.getDefaultPadding(),
                UIConfig.INSTANCE.getDefaultPadding(), UIConfig.INSTANCE.getDefaultPadding()));
        panel.setBackground(UIManager.getColor("SubWindow.background"));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(UIConfig.INSTANCE.getScrollbarSpeed());
        panel.setAlignmentX(0);
        scrollPane.setAlignmentX(0);
        revalidate();
        repaint();
    }

}
