package UI.Widgets;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * The {@code IconComboBoxRenderer} class is a UI class that acts as a
 * custom renderer for icons for the JComboBox (multiple choices UI option)
 *
 * @author Louis-Etienne Messier
 * @version 0.1
 * @since 2024-10-30
 */
public class IconComboBoxRenderer extends JLabel implements ListCellRenderer {
    private ArrayList<JLabel> labels;

    public IconComboBoxRenderer(ArrayList<JLabel> labels){
        setOpaque(true);
        this.labels = labels;
    }


    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        int selectedIndex = ((Integer)value).intValue();

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        setIcon(labels.get(selectedIndex).getIcon());
        setText(labels.get(selectedIndex).getText());
        return this;

    }
}
