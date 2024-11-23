package UI.SubWindows;

import Common.DTO.BitDTO;
import UI.Events.ChangeAttributeListener;
import UI.MainWindow;
import UI.UIConfig;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.util.Map;

/**
 * Represents a panel allowing the selection of bits through toggle buttons.
 *
 * @author Antoine Morin
 * @version 1.0
 */
public class BitSelectionPanel extends BasicWindow {
    private MainWindow mainWindow;
    private ChangeAttributeListener listener;
    private JScrollPane scrollPane;
    private JPanel panel;
    private ButtonGroup buttonGroup = new ButtonGroup();
    private final JToggleButton[] bitButtonList = new JToggleButton[UIConfig.INSTANCE.getMAX_NB_BITS()];
    private Map<Integer, BitDTO> configuredBitsMap;

    /**
     * The position of the selected bit in the bit list
     * -1 means originally, no bits selected
     */
    private int selectedBit = 0;

    /**
     * Constructs a BitSelectionPanel with a specified attribute change listener.
     *
     * @param haveBackground true if the panel should have a background, false otherwise
     * @param listener the listener for attribute changes triggered by bit selection.
     * @param mainWindow the main window of the application
     * @param configuredBitsMap the map of configured bits
     */
    public BitSelectionPanel(boolean haveBackground, ChangeAttributeListener listener, MainWindow mainWindow, Map<Integer, BitDTO> configuredBitsMap) {
        super(haveBackground);
        this.mainWindow = mainWindow;
        this.configuredBitsMap = configuredBitsMap;
        this.listener = listener;
        updateBitList();
        init();
    }

    /**
     * Initializes the layout and components of the BitSelectionPanel, such as labels, text areas,
     * and the modify button. Sets up layout constraints and adds components to the panel.
     */
    private void init(){
        buttonGroup = new ButtonGroup();
        panel = new JPanel();
        panel.setBackground(UIManager.getColor("SubWindow.background"));
        panel.setLayout(new GridBagLayout());
        panel.setBorder(null);
        panel.setAlignmentX(0);

        scrollPane = new JScrollPane(panel);
        scrollPane.setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(UIConfig.INSTANCE.getScrollbarSpeed());

        this.setupHeader("Sélection de l'outil", scrollPane);
        scrollPane.setAlignmentX(0);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        update(gbc);
        setupEventsListeners(gbc);
    }

    /**
     * Updates the panel with the latest bits from the controller
     * @param gbc The grid bag constraints to use for the layout
     */
    public void update(GridBagConstraints gbc){
        panel.removeAll();

        if(configuredBitsMap == null || configuredBitsMap.isEmpty()){
            JLabel label = new JLabel("Aucun bit configuré");
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setVerticalAlignment(JLabel.CENTER);
            panel.add(label, gbc);
            return;
        }

        int columns = (configuredBitsMap.size() + 1) / 2; // Calcul du nombre de colonnes pour 2 lignes
        int row = 0;
        int col = 0;

        for (Integer index: configuredBitsMap.keySet()) {
            gbc.gridx = col;
            gbc.gridy = row;

            if (bitButtonList[index] != null) {
                panel.add(bitButtonList[index], gbc);
            }

            col++;
            if (col >= columns) {
                col = 0;
                row++;
            }
        }
    }

    /**
     * Sets up event listeners for the panel, such as the ancestor listener.
     * @param gbc The grid bag constraints to use for the layout
     */
    private void setupEventsListeners(GridBagConstraints gbc){
        panel.addAncestorListener(new AncestorListener() {
            /**
             * Called when enter CutWindow interface
             * @param event The ancestor event
             */
            @Override
            public void ancestorAdded(AncestorEvent event) {
                updateBitList();
                update(gbc);
                enableLastSelectedBit();
                panel.revalidate();
                panel.repaint();
            }

            @Override
            public void ancestorRemoved(AncestorEvent event) {

            }

            @Override
            public void ancestorMoved(AncestorEvent event) {

            }
        });
    }

    /**
     * Updates the bit list with the latest bits from the controller
     */
    private void updateBitList(){
        for(Map.Entry<Integer, BitDTO> entry : configuredBitsMap.entrySet()){
            BitDTO bitDTO = entry.getValue();
            JToggleButton button = new JToggleButton(bitDTO.getName());
            button.setActionCommand(String.valueOf(entry.getKey()));
            buttonGroup.add(button);
            bitButtonList[entry.getKey()] = button;

            button.addActionListener(e -> {
                selectedBit = Integer.parseInt(button.getActionCommand());
            });
        }
    }

    /**
     * Returns the selected bit index of all the configured bits
     * @return The selected bit index
     */
    public int getSelectedBit() {
        return selectedBit;
    }

    /**
     * On window revalidation, this method will highlight the last selected bit for user to know
     * which bit he selected last time
     */
    private void enableLastSelectedBit(){
        if (selectedBit != -1) {
            for (JToggleButton button : bitButtonList) {
                if (button != null && Integer.parseInt(button.getActionCommand()) == selectedBit) {
                    button.setSelected(true);
                    break;
                }
            }
        }
    }
}
