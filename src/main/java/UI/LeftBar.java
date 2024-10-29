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
    private final ToolBar toolBar;

    /**
     * Constructs a new {@code LeftBar} object and initialize it with a new {@code ToolBar}
     */
    public LeftBar() {
        this.toolBar = new ToolBar();
        setViewportView(toolBar);
        setBorder(null);
        setVisible(true);
        activateAllListener();
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
            SAVE(0), UNDO(2), REDO(3), TRASH(4), GRID(6), MAGNET(7), SCALE(8), PARALLEL(10), RECTANGLE(11), COUPEL(12), RETAILLER(13), MODIFY(14), FORBIDDEN(15), ZOOMOUT(17), ZOOMIN(18), SETTING(19);

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
            JButton save = createSVGButton("save", true, "Enregistrer", uiConfig.getToolIconSize());
            add(save);
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
         * Enables multiples tools in the {@code ToolBar}
         *
         * @param tools A list of tools that needs to be enabled
         */
        public void enableTools(Tool[] tools) {
            for (Tool tool : tools) {
                enableTool(tool);
            }
        }

        /**
         * Disables multiples tools in the {@code ToolBar}
         *
         * @param tools A list of tools that needs to be disabled
         */
        public void disableTools(Tool[] tools) {
            for (Tool tool : tools) {
                disableTool(tool);
            }
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

    /**
     * Sets all the action listener on the different tools in the toolbar.
     */
    private void activateAllListener() {
        saveActionListener();
        zoomActionListener();
        scaleActionListener();
        settingActionListener();
    }

    /**
     * Sets the save button in the toolBar to save the project at the default place on the machine.
     */
    private void saveActionListener() {
        toolBar.getTool(ToolBar.Tool.SAVE).addActionListener(e -> {
            //todo
        });
    }

    /**
     * Sets the two zoom button to zoom or de zoom the 2dRenderer if one is displayed on screen.
     */
    private void zoomActionListener() {
        toolBar.getTool(ToolBar.Tool.ZOOMIN).addActionListener(e -> MainWindow.INSTANCE.getMiddleContent().zoom(-5));

        toolBar.getTool(ToolBar.Tool.ZOOMOUT).addActionListener(e -> MainWindow.INSTANCE.getMiddleContent().zoom(5));
    }

    /**
     * Sets the scale tool to initiate the scaling procedure.
     */
    private void scaleActionListener() {
        toolBar.getTool(ToolBar.Tool.SCALE).addActionListener(e -> {
            MiddleContent middle = MainWindow.INSTANCE.getMiddleContent();
            if (middle.getCurrent() == MiddleContent.MiddleWindowType.CONFIG) {
                middle.getConfigChoiceWindow().getRendering2DWindow().scale();
            }
        });
    }

    /**
     * Sets the Setting tool to change to the option window when clicked.
     */
    private void settingActionListener() {
        toolBar.getTool(ToolBar.Tool.SETTING).addActionListener(e -> MainWindow.INSTANCE.showOptionWindow());
    }
}
