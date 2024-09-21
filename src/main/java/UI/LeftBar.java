package UI;

import javax.swing.*;

import static Util.UiUtil.createSVGButton;

public class LeftBar extends JScrollPane {

    UIConfig uiConfig = UIConfig.INSTANCE;

    public LeftBar() {
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
            add(createSVGButton("save", true, "Enregistrer", uiConfig.getToolIconSize()));
            addSeparator();
            add(createSVGButton("undo", false, "Undo", uiConfig.getToolIconSize()));
            add(createSVGButton("redo", false, "Redo", uiConfig.getToolIconSize()));
            add(createSVGButton("trash", false, "Delete", uiConfig.getToolIconSize()));
            addSeparator();
            add(createSVGButton("grid", false, "Activer grille", uiConfig.getToolIconSize()));
            add(createSVGButton("magnet", false, "Aimanter la grille", uiConfig.getToolIconSize()));
            add(createSVGButton("scale", false, "Change taille panneau", uiConfig.getToolIconSize()));
            addSeparator();
            add(createSVGButton("parallel", false, "Coupe parallèle", uiConfig.getToolIconSize()));
            add(createSVGButton("rectangle", false, "Coupe Rectangle", uiConfig.getToolIconSize()));
            add(createSVGButton("coupeL", false, "Coupe en L", uiConfig.getToolIconSize()));
            add(createSVGButton("retailler", false, "Retaille le panneau", uiConfig.getToolIconSize()));
            add(createSVGButton("modify", false, "Modifier", uiConfig.getToolIconSize()));
            add(createSVGButton("forbidden", false, "Zone interdite", uiConfig.getToolIconSize()));
            addSeparator();
            add(createSVGButton("zoomOut", false, "Rétrécir", uiConfig.getToolIconSize()));
            add(createSVGButton("zoomIn", false, "Agrandir", uiConfig.getToolIconSize()));
            add(createSVGButton("setting", true, "Paramètres", uiConfig.getToolIconSize()));
        }

        public void makeGridAvailable() {

        }
    }
}
