package UI.SubWindows;

import Common.BitDTO;
import UI.Events.ChangeAttributeListener;
import UI.MainWindow;
import UI.UIConfig;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;

public class BitSelectionPanel extends BasicWindow {
    private MainWindow mainWindow;
    private ChangeAttributeListener listener;
    private BitDTO[] bitDTOList;
    private JScrollPane scrollPane;
    private JPanel panel;
    private ButtonGroup buttonGroup = new ButtonGroup();
    private final JToggleButton[] bitButtonList = new JToggleButton[UIConfig.INSTANCE.getMAX_NB_BITS()];

    /**
     * The position of the selected bit in the bit list
     * -1 means originally, no bits selected
     */
    private int selectedBit = -1;

    public BitSelectionPanel(boolean haveBackground, ChangeAttributeListener listener, MainWindow mainWindow){
        super(haveBackground);
        this.mainWindow = mainWindow;
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
        int index = 0;

        // The list is null at the launch of the App because no bit is created
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < (realLen + 1) / 2; j++) { // We want 2 rows
                gbc.gridx = j;
                gbc.gridy = i;

                while (index < bitButtonList.length && bitButtonList[index] == null) {
                    index++;
                }

                if (index < bitButtonList.length) {
                    panel.add(bitButtonList[index], gbc);
                    index++;
                } else
                    break;
            }
        }
        this.repaint();
    }

    private void setupEventsListeners(GridBagConstraints gbc){
        panel.addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                updateBitList();
                update(gbc);
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
        for (int i = 0; i < bitDTOList.length; i++) {
            if (bitDTOList[i].getDiameter() != 0) {
                JToggleButton button = new JToggleButton(bitDTOList[i].getName());
                bitButtonList[i] = button;
                buttonGroup.add(button);

                button.setActionCommand(String.valueOf(i));
                button.addActionListener(e -> {
                    selectedBit = Integer.parseInt(button.getActionCommand());
                });
            }
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
}
