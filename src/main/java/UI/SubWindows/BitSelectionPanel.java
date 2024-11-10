package UI.SubWindows;

import Common.BitDTO;
import UI.Events.ChangeAttributeListener;
import UI.MainWindow;
import UI.UIConfig;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.plaf.LabelUI;
import java.util.List;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class BitSelectionPanel extends BasicWindow {
    private MainWindow mainWindow;
    private ChangeAttributeListener listener;
    private BitDTO[] bitDTOList;
    private JScrollPane scrollPane;
    private JPanel panel;
    private ButtonGroup buttonGroup = new ButtonGroup();
    private final JToggleButton[] bitButtonList = new JToggleButton[UIConfig.INSTANCE.getMAX_NB_BITS()];
    private Map<Integer, BitDTO> configuredBitsMap;

    /**
     * The position of the selected bit in the bit list
     * -1 means originally, no bits selected
     */
    private int selectedBit = -1;

    public BitSelectionPanel(boolean haveBackground, ChangeAttributeListener listener, MainWindow mainWindow, Map<Integer, BitDTO> configuredBitsMap) {
        super(haveBackground);
        this.mainWindow = mainWindow;
        this.configuredBitsMap = configuredBitsMap;
        this.listener = listener;
        updateBitList();
        init();
    }

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

        this.setupHeader("Sélection des bits", scrollPane);
        scrollPane.setAlignmentX(0);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        update(gbc);
        setupEventsListeners(gbc);
    }

    public void update(GridBagConstraints gbc){
        panel.removeAll();
        int realLen = getRealListLen();

        if(configuredBitsMap == null || configuredBitsMap.isEmpty()){
            JLabel label = new JLabel("Aucun bit configuré");
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setVerticalAlignment(JLabel.CENTER);
            panel.add(label, gbc);
            return;
        }

        // We want to iterate through configuredBitMap to display the bits in order
        //System.out.println("configuredBitsMap: " + configuredBitsMap);

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

    private void setupEventsListeners(GridBagConstraints gbc){
        panel.addAncestorListener(new AncestorListener() {
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

    private void updateBitList(){
        bitDTOList = mainWindow.getController().getBitsDTO();
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

    private int getRealListLen(){
        int len = 0;
        for (JToggleButton jToggleButton : bitButtonList) {
            if (jToggleButton != null) {
                len++;
            }
        }
        return len;
    }

    public int getSelectedBit() {
        return selectedBit;
    }

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

    public List<String> getCreatedBitsReadable(){
        List<String> createdBits = new ArrayList<>();
        for (BitDTO bitDTO : bitDTOList) {
            if (bitDTO.getDiameter() != 0) {
                createdBits.add(bitDTO.getName() + " - " + bitDTO.getDiameter() + "mm");
            }
        }
        return createdBits;
    }
}
