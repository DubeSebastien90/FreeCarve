package UI.SubWindows;

import Common.DTO.BitDTO;
import Common.Exceptions.InvalidFileExtensionException;
import UI.Events.ChangeAttributeEvent;
import UI.Events.ChangeAttributeListener;
import UI.*;
import UI.Listeners.SaveToolsActionListener;
import UI.Widgets.BitInfoDisplay;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Represents a panel allowing the selection of bits through toggle buttons.
 * The panel supports up to a maximum of 12 bits and communicates bit selection
 * changes to a specified listener.
 *
 * @author Antoine Morin
 * @version 1.1
 * @since 2024-11-01
 */
public class BitConfigurationPanel extends BasicWindow {
    private final JToggleButton[] bitList = new JToggleButton[UIConfig.INSTANCE.getMAX_NB_BITS()];
    private BitDTO[] bitDTOList;
    private final ChangeAttributeListener listener;
    private int selectedBit = 0;
    private final MainWindow mainWindow;

    private final JButton saveToolsButton = UiUtil.createSVGButton("upload", true, UIConfig.INSTANCE.getToolIconSize());
    private final JButton loadToolsButton = UiUtil.createSVGButton("download", true, UIConfig.INSTANCE.getToolIconSize());

    /**
     * Constructs a BitSelectionPanel with a specified attribute change listener.
     *
     * @param listener the listener for attribute changes triggered by bit selection.
     */
    public BitConfigurationPanel(ChangeAttributeListener listener, MainWindow mainWindow) {
        super(true);
        this.mainWindow = mainWindow;
        this.listener = listener;
        bitDTOList = mainWindow.getController().getBitsDTO();
        for (int i = 0; i < bitList.length; i++) {
            bitList[i] = new JToggleButton(bitDTOList[i].getName());
        }
        init();
        setButtonEvent();
    }

    /**
     * Returns the index of the currently selected bit.
     *
     * @return the index of the selected bit as an integer.
     */
    public int getSelectedBit() {
        return selectedBit;
    }

    /**
     * Returns the list of toggle buttons representing each bit.
     *
     * @return an array of JToggleButton, where each button represents a bit.
     */
    public JToggleButton[] getBitList() {
        return bitList;
    }

    /**
     * Initializes the layout of the bit selection panel and adds the toggle buttons
     * to the panel using a grid layout.
     */
    private void init() {
        JPanel bigPanel = new JPanel(new GridBagLayout());
        JPanel panel = new JPanel();
        panel.setBackground(null);

        GridBagLayout layout = new GridBagLayout();
        JScrollPane scrollPane = new JScrollPane(bigPanel);
        this.setupHeader("Outils", scrollPane);
        panel.setLayout(layout);
        panel.setBorder(new EmptyBorder(UIConfig.INSTANCE.getDefaultPadding(), UIConfig.INSTANCE.getDefaultPadding(),
                UIConfig.INSTANCE.getDefaultPadding(), UIConfig.INSTANCE.getDefaultPadding()));
        bigPanel.setBorder(new EmptyBorder(UIConfig.INSTANCE.getDefaultPadding(), UIConfig.INSTANCE.getDefaultPadding(),
                UIConfig.INSTANCE.getDefaultPadding(), UIConfig.INSTANCE.getDefaultPadding()));
        bigPanel.setBackground(UIManager.getColor("SubWindow.background"));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(UIConfig.INSTANCE.getScrollbarSpeed());
        panel.setAlignmentX(0);
        bigPanel.setAlignmentX(0);
        scrollPane.setAlignmentX(0);

        GridBagConstraints gbc = new GridBagConstraints();

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0)); // Small gap between buttons
        buttonPanel.add(loadToolsButton);
        buttonPanel.add(saveToolsButton);
        buttonPanel.setBackground(null);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.add(buttonPanel);
        panel.setLayout(layout);
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        for (int i = 0; i < UIConfig.INSTANCE.getMAX_NB_BITS() / 6; i++) {
            for (int j = 1; j <= 6; j++) {
                gbc.gridy = j;
                gbc.gridx = i;
                panel.add(bitList[(i) + (UIConfig.INSTANCE.getMAX_NB_BITS() / 6) * (j - 1)], gbc);
            }
        }

        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.gridx = 0;
        gbc1.gridy = 0;
        gbc1.weightx = 1;
        gbc1.weighty = 0;
        gbc1.fill = GridBagConstraints.BOTH;
        bigPanel.add(buttonPanel, gbc1);
        gbc1.gridy = 1;
        gbc1.gridx = 0;
        gbc1.weightx = 1;
        gbc1.weighty = 1;
        bigPanel.add(panel, gbc1);

        revalidate();
        repaint();
    }

    /**
     * Refreshes the labels of the bit toggle buttons to reflect the latest bit names.
     */
    public void refresh() {
        bitDTOList = mainWindow.getController().getBitsDTO();
        for (int i = 0; i < bitList.length; i++) {
            bitList[i].setText(bitDTOList[i].getName());
        }
    }

    /**
     * Sets up the action listeners for each toggle button. When a button is selected,
     * it triggers an attribute change event, updates the selected bit index, and ensures
     * only the selected button is toggled.
     */
    public void setButtonEvent() {
        for (int i = 0; i < getBitList().length; i++) {
            JToggleButton bit = getBitList()[i];
            int finalI = i;
            bit.addActionListener(e -> {
                ChangeAttributeEvent event = new ChangeAttributeEvent(
                        this,
                        new BitInfoDisplay(
                                mainWindow.getController().getBitsDTO()[finalI],
                                true,
                                BitConfigurationPanel.this, mainWindow
                        )
                );
                listener.changeAttributeEventOccurred(event);
                for (int j = 0; j < getBitList().length; j++) {
                    getBitList()[j].setSelected(finalI == j);
                }
                selectedBit = finalI;
            });
        }


        saveToolsButton.addActionListener(new SaveToolsActionListener(mainWindow));

        loadToolsButton.addActionListener(e -> {
            mainWindow.getLeftBar().getToolBar().disableTool(LeftBar.ToolBar.Tool.TRASH);
            File file = Utils.chooseFile("Charger outils", "defaut.CNC", mainWindow.getFrame(), FileCache.INSTANCE.getLastToolSave(), "Fichier outils (CNC)", "CNC");
            if (file != null) {
                try {
                    mainWindow.getController().loadTools(file);
                } catch (InvalidFileExtensionException ex) {
                    System.out.println("Invalid file extension");
                } catch (IOException ex) {
                    System.out.println("Unable to load file");
                } catch (ClassNotFoundException ex) {
                    System.out.println("File content is not valid");
                }
                mainWindow.getMiddleContent().getConfigChoiceWindow().getBitWindow().refresh();
                mainWindow.getMiddleContent().getConfigChoiceWindow().getAttributePanel().update();
                mainWindow.getMiddleContent().getCutWindow().notifyObservers();
                FileCache.INSTANCE.setLastToolSave(file);
            }
        });
    }
}
