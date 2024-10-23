package UI.SubWindows;

import UI.CutWindow;
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
    private BasicWindow panel;
    private JScrollPane scrollPane;
    private BoxLayout layout;
    private CutWindow parent;

    public AttributePanel(boolean haveBackground, CutWindow parent){
        super(haveBackground);
        this.init();
        this.parent = parent;
    }

    public void updateAttribute(Attributable newAttributable){
        this.panel.removeAll();
        this.panel.add(newAttributable.showName());
        this.panel.add(newAttributable.showAttribute());
        this.panel.revalidate();
        this.panel.repaint();
    }

    private void init(){
        panel = new BasicWindow(false);
        layout = new BoxLayout(panel, BoxLayout.PAGE_AXIS);
        scrollPane = new JScrollPane(panel);
        this.setupHeader("Attributs", scrollPane);
        panel.setOpaque(false);
        panel.setBackground(null);
        panel.setLayout(layout);
        panel.setBorder(new EmptyBorder(10,10,10,10));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(UIConfig.INSTANCE.getScrollbarSpeed());
        panel.setAlignmentX(0);
        scrollPane.setAlignmentX(0);
    }
}
