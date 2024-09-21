package UI;

import javax.swing.*;

import static Util.UiUtil.createSVGButton;

/**
 * The 
 */
public class LeftBar extends JScrollPane {

    private final UIConfig uiConfig = UIConfig.INSTANCE;

    public LeftBar() {
        setViewportView(new ToolBar());
        setBorder(null);
        setVisible(true);
    }

    public class ToolBar extends JToolBar {
        public enum Tool {
            SAVE(1), UNDO(2), REDO(3), TRASH(5), GRID(6), MAGNET(7), SCALE(8), PARALLEL(10), RECTANGLE(11), COUPEL(12), RETAILLER(13), MODIFY(14), FORBIDDEN(15), ZOOMOUT(17), ZOOMIN(18), SETTING(19);

            private final int value;

            Tool(int value) {
                this.value = value;
            }

            public int getValue() {
                return value;
            }
        }

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

        public void enableTool(Tool tool) {
            getComponent(tool.value).setEnabled(true);
        }

        public void disableTool(Tool tool) {
            getComponent(tool.value).setEnabled(false);
        }
    }
}
