package UI.SubWindows;

import Common.Interfaces.IPanelObserver;
import Common.Interfaces.IRefreshable;
import UI.LeftBar;
import UI.MainWindow;
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

public class AttributePanel extends BasicWindow implements IPanelObserver {
    private JPanel panel;
    private MainWindow mainWindow;

    /**
     * Constructor of the parent {@code BasicWindow}
     * @param haveBackground
     */
    public AttributePanel(boolean haveBackground, MainWindow mainWindow) {
        super(haveBackground);
        this.mainWindow = mainWindow;
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

            JPanel showErrorsPanel = newAttributable.showErrors();
            if(showErrorsPanel.getComponentCount() > 0){ // Only draw the showErrors panel if there is any errors
                gc.gridx = 0; gc.gridy = 1;
                gc.fill = GridBagConstraints.HORIZONTAL;
                gc.weightx = 1;
                this.panel.add(newAttributable.showErrors(), gc);
            }

            gc.gridx = 0; gc.gridy = 2;
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
        GridBagLayout layout = new GridBagLayout();
        JScrollPane scrollPane = new JScrollPane(panel);
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

        addRefreshListener();

        revalidate();
        repaint();
    }

    /**
     * Adds a refresh listener to the {@code AttributePanel}
     * This is used to update the attributes of the {@Attributable}
     * When the board is updated, we also want to update the attributes
     */
    public void addRefreshListener(){
        this.mainWindow.getController().addRefreshListener(new IRefreshable() {
            @Override
            public void refresh() {
                updateAttribute(null);
            }
        });
    }

    /**
     * Clears the panel
     * Called when the panel is cleared
     */
    @Override
    public void update() {
        this.panel.removeAll();
        this.panel.revalidate();
        this.panel.repaint();
    }

}
