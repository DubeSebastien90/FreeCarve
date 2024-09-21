package UI;

import javax.swing.*;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.ui.FlatButtonBorder;

public class LeftBar extends JScrollPane {

    UIConfig uiConfig = UIConfig.INSTANCE;

    public LeftBar() {
        ToolTipManager.sharedInstance().setInitialDelay(1000);
        setViewportView(new ToolBar());
        setBorder(null);
        setVisible(true);
    }

    public class ToolBar extends JToolBar {

        public ToolBar() {
            super(VERTICAL);
            setVisible(true);
            init();
        }

        private void init() {
            add(createSVGBUtton("save", true, "Enregistrer"));
            addSeparator();
            add(createSVGBUtton("undo", false, "Undo"));
            add(createSVGBUtton("redo", false, "Redo"));
            add(createSVGBUtton("trash", false, "Delete"));
            addSeparator();
            add(createSVGBUtton("grid", false, "Activer grille"));
            add(createSVGBUtton("magnet", false, "Aimanter la grille"));
            add(createSVGBUtton("scale", false, "Change taille panneau"));
            addSeparator();
            add(createSVGBUtton("parallel", false, "Coupe parallèle"));
            add(createSVGBUtton("rectangle", false, "Coupe Rectangle"));
            add(createSVGBUtton("coupeL", false, "Coupe en L"));
            add(createSVGBUtton("retailler", false, "Retaille le panneau"));
            add(createSVGBUtton("modify", false, "Modifier"));
            add(createSVGBUtton("forbidden", false, "Zone interdite"));
            addSeparator();
            add(createSVGBUtton("zoomOut", false, "Rétrécir"));
            add(createSVGBUtton("zoomIn", false, "Agrandir"));
            add(createSVGBUtton("setting", true, "Paramètres"));
        }

        private JButton createSVGBUtton(String iconName, boolean enable, String tooltipMessage) {
            FlatSVGIcon icon = new FlatSVGIcon("UI/" + iconName + ".svg", uiConfig.getToolIconSize(), uiConfig.getToolIconSize());
            FlatSVGIcon.ColorFilter filter = new FlatSVGIcon.ColorFilter(color -> UIManager.getColor("Button.foreground"));
            icon.setColorFilter(filter);
            JButton button = new JButton(icon);
            button.setBorder(new FlatButtonBorder());
            button.setEnabled(enable);
            button.setToolTipText(tooltipMessage);
            return button;
        }

        public void makeGridAvailable() {

        }
    }
}
