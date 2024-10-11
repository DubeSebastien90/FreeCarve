package UI;

import javax.swing.*;

import static Util.UiUtil.createSVGButton;

/**
 * The {@code LeftBar} class extends {@code JScrollPane} and is basically a scrollpane with a {@code ToolBar} in it.
 * The {@code ToolBar} is defined in a class inside {@code LeftBar}
 *
 * @author Adam Côté
 * @version 1.0
 * @since 2024-09-21
 */
public class LeftBar extends JScrollPane {

    private final UIConfig uiConfig = UIConfig.INSTANCE;
    private ToolBar toolBar;

    /**
     * Constructs a new {@code LeftBar} object and initialize it with a new {@code ToolBar}
     */
    public LeftBar() {
        this.toolBar = new ToolBar();
        setViewportView(toolBar);
        setBorder(null);
        setVisible(true);
    }

    /**
     * @return The Toolbar that compose this LeftBar
     */
    public ToolBar getToolBar() {
        return this.toolBar;
    }

    /**
     * The {@code ToolBar} class extends {@code JToolBar} and is just a normal JToolbar but with predefined button (Called Tool) in it
     *
     * @author Adam Côté
     * @version 1.0
     * @since 2024-09-21
     */
    public class ToolBar extends JToolBar {

        /**
         * The {@code Tool} enum is useful for accessing the wanted tool inside the {@code ToolBar}.
         * Take note that if you add a tool to the toolbar, you must update the {@code Tool} enum.
         */
        public enum Tool {
            SAVE(0), UNDO(2), REDO(3), TRASH(5), GRID(6), MAGNET(7), SCALE(8), PARALLEL(10), RECTANGLE(11), COUPEL(12), RETAILLER(13), MODIFY(14), FORBIDDEN(15), ZOOMOUT(17), ZOOMIN(18), SETTING(19);

            private final int value;

            /**
             * Construct a new tool
             *
             * @param value The index of the tool inside the {@code ToolBar}
             */
            Tool(int value) {
                this.value = value;
            }

            /**
             * @return The value of the {@code Tool}
             */
            public int getValue() {
                return value;
            }
        }

        /**
         * Constructs a new {@code Toolbar} with certains parameter and some predefined tools
         */
        public ToolBar() {
            super(VERTICAL);
            setVisible(true);
            init();
        }

        /**
         * Initiates the {@code ToolBar} with some predefine tools
         */
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

        /**
         * Enables a certain tool in the {@code ToolBar} given a {@code Tool}
         *
         * @param tool The {@code Tool} that must get enable.
         */
        public void enableTool(Tool tool) {
            getTool(tool).setEnabled(true);
        }

        /**
         * Disables a certain tool in the {@code ToolBar} given a {@code Tool}
         *
         * @param tool The {@code Tool} that must get disable.
         */
        public void disableTool(Tool tool) {
            getTool(tool).setEnabled(false);
        }

        /**
         * Assumes every tool is a JButton.
         *
         * @param tool The tool you want to get
         * @return The tool you want to get
         */
        public JButton getTool(Tool tool) {
            return (JButton) getComponent(tool.value);
        }
    }
}