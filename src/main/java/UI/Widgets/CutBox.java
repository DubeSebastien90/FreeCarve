package UI.Widgets;

import Common.DTO.BitDTO;
import Common.DTO.CutDTO;
import Domain.CutType;
import Common.DTO.VertexDTO;
import UI.Events.ChangeAttributeEvent;
import UI.Events.ChangeAttributeListener;
import UI.Events.ChangeCutEvent;
import UI.Events.ChangeCutListener;
import UI.MainWindow;
import UI.SubWindows.BasicWindow;
import UI.UIConfig;
import Common.UiUtil;
import com.formdev.flatlaf.ui.FlatButtonBorder;
import com.sun.tools.javac.Main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * The {@code CutBox} class is a UI class that encapsulates a box containing all the informations
 * about a specific cut
 *
 * @author Louis-Etienne Messier
 * @version 0.1
 * @since 2024-09-21
 */
public class CutBox implements Attributable {
    private CutDTO cut;
    private JPanel panel;
    private GridBagLayout layout;
    private RoundedJLabel bitnameLabel;
    private JLabel numberLabel;
    private JLabel imageLabel;
    private int index;
    private boolean selected;
    private ChangeAttributeListener listener;
    private final MainWindow mainWindow;
    private ChangeCutListener cutListener;
    private JButton deleteButton;
    private JButton moveUpButton;
    private JButton moveDownButton;

    // Attributes variables
    BasicWindow attributeContainer;
    PointsBox pointsBox1;
    PointsBox pointsBox2;
    SingleValueBox depthBox;
    BitChoiceBox bitChoiceBox;
    ChoiceBox cuttypeBox;

    /**
     * Basic constructor of {@code CutBox}, initiates all of the UI values and get a reference to the CutList parent
     *
     * @param cutDTO   cut that CutBox will present
     * @param index    index of the cut
     * @param listener reference to the parent listener
     */
    public CutBox(CutDTO cutDTO, int index, ChangeAttributeListener listener, ChangeCutListener cutListener, MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.cut = cutDTO;
        this.index = index;
        this.listener = listener;
        this.cutListener = cutListener;
        this.init();
        this.init_attribute();
        this.setBackgroundToIndex();
        this.updatePanel(this.cut);
        this.setupMouseEvents();
        this.setupAttributeChangeEvents();
    }

    /**
     * Function override of the Attributable interface
     *
     * @return
     */
    @Override
    public JLabel showName() {
        JLabel label = new JLabel(this.imageLabel.getIcon());
        label.setText("Coupe " + this.index);
        label.setBackground(Color.YELLOW);
        label.setBorder(new EmptyBorder(0, 0, UIConfig.INSTANCE.getDefaultPadding(), 0));
        return label;
    }

    /**
     * Function override of the Attributable interface
     *
     * @return {@code JPanel} of the attribute modification of the CutBox
     */
    @Override
    public JPanel showAttribute() {
        attributeContainer = new BasicWindow(true);
        attributeContainer.setBackground(null);
        attributeContainer.setOpaque(false);
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gc = new GridBagConstraints();
        attributeContainer.setLayout(layout);
        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        attributeContainer.add(pointsBox1, gc);
        gc.gridx = 0;
        gc.gridy = 1;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        attributeContainer.add(pointsBox2, gc);

        gc.gridx = 0;
        gc.gridy = 2;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        attributeContainer.add(depthBox, gc);

        gc.gridx = 0;
        gc.gridy = 3;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        attributeContainer.add(bitChoiceBox, gc);

        gc.gridx = 0;
        gc.gridy = 4;
        gc.insets = new Insets(0, 0, 0, 0);
        attributeContainer.add(cuttypeBox, gc);

        return attributeContainer;
    }

    /**
     * Modify all of the attributes and UI values of the CutBox based on a new CutDTO
     *
     * @param newCutDTO new CutDTO to modify the CutBox with
     */
    public void updatePanel(CutDTO newCutDTO) {
        this.cut = newCutDTO;

        // Setting the bit info
        bitnameLabel.setText("Outil : " + (mainWindow.getController().getBitsDTO()[newCutDTO.getBitIndex()].getName()));

        // Setting the index of the cut
        numberLabel.setText(String.valueOf(this.index));

        // Setting the image of the cutbox
        CutType type = this.cut.getCutType();
        String iconName = UiUtil.getIconFileName(type);
        imageLabel.setIcon(UiUtil.getIcon(iconName, UIConfig.INSTANCE.getCutBoxIconSize(),
                UIManager.getColor("button.Foreground")));
    }

    /**
     * @return {@code JPanel} of the CutBox
     */
    public JPanel getPanel() {
        return this.panel;
    }

    /**
     * Select this CutBox
     */
    public void select(){
        selected = true;
        panel.setBackground(UIManager.getColor("Button.green"));
    }

    /**
     * Set the CutBox as non-selected
     */
    public void deselect() {
        this.selected = false;
        setBackgroundToIndex();

    }

    /**
     * Get the CutBox UUID
     *
     * @return the id of the CutBox
     */
    public UUID getCutUUID() {
        return this.cut.getId();
    }

    /**
     * Set the background of the CutBox according to it's index
     */
    private void setBackgroundToIndex() {
        if (this.index % 2 == 0) {
            this.panel.setBackground(UIManager.getColor("SubWindow.darkBackground1"));
        } else {
            this.panel.setBackground(UIManager.getColor("SubWindow.darkBackground2"));
        }
    }

    /**
     * Setup the custom mouse events of the CutBox
     */
    private void setupMouseEvents() {
        this.panel.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                ChangeAttributeEvent event = new ChangeAttributeEvent(CutBox.this, CutBox.this);
                listener.changeAttributeEventOccurred(event);
                select();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!selected) panel.setBackground(UIManager.getColor("Button.blue"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!selected) setBackgroundToIndex();
            }
        });
    }

    /**
     * Initialize the events of the Attributable properties that can be changed
     */
    private void setupAttributeChangeEvents() {
        addEventListenerToPointBox(pointsBox1, 0);
        addEventListenerToPointBox(pointsBox2, 1);
        addEventListenerToSingleValue(depthBox);
        addEventListenerToBitChoiceBox(bitChoiceBox);
        addEventListenerToChoiceBox(cuttypeBox);
    }

    /**
     * Initialize the UI components
     */
    private void init() {

        layout = new GridBagLayout();
        panel = new JPanel(layout);
        panel.setAlignmentX(0);
        panel.setBorder(new EmptyBorder(UIConfig.INSTANCE.getDefaultPadding(), UIConfig.INSTANCE.getDefaultPadding(),
                UIConfig.INSTANCE.getDefaultPadding(), UIConfig.INSTANCE.getDefaultPadding()));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        panel.setMinimumSize(new Dimension(Integer.MIN_VALUE, 60));
        GridBagConstraints gc = new GridBagConstraints();
        bitnameLabel = new RoundedJLabel("Bitname placeholder", 15);
        numberLabel = new JLabel("Number placeholder");
        imageLabel = new JLabel(UiUtil.getIcon("coupeL", UIConfig.INSTANCE.getCutBoxIconSize(),
                UIManager.getColor("button.Foreground")));
        numberLabel.setBackground(Color.RED);
        bitnameLabel.setBackground(UIManager.getColor("SubWindow.background"));
        bitnameLabel.setHorizontalAlignment(JLabel.CENTER);
        bitnameLabel.setBorder(new EmptyBorder(UIConfig.INSTANCE.getDefaultPadding(), UIConfig.INSTANCE.getDefaultPadding(),
                UIConfig.INSTANCE.getDefaultPadding(),  UIConfig.INSTANCE.getDefaultPadding()));
        numberLabel.putClientProperty("FlatLaf.style", "font: bold $h3.regular.font");
        deleteButton = UiUtil.createSVGButton("trash", true, UIConfig.INSTANCE.getToolIconSize(), Color.RED);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cutListener.deleteCutEventOccured(new ChangeCutEvent(CutBox.this, cut.getId()));
            }
        });
        moveUpButton = UiUtil.createSVGButton("upArrow", true, UIConfig.INSTANCE.getToolIconSize());
        moveDownButton = UiUtil.createSVGButton("downArrow", true, UIConfig.INSTANCE.getToolIconSize());
        deleteButton.setBorder(new FlatButtonBorder());
        moveUpButton.setBorder(new FlatButtonBorder());
        moveDownButton.setBorder(new FlatButtonBorder());

        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.weightx = 0.0;
        gc.anchor = GridBagConstraints.CENTER;
        gc.insets = new Insets(0,0,0,UIConfig.INSTANCE.getDefaultPadding());
        panel.add(numberLabel, gc);

        gc.gridx = 0;
        gc.gridy = 1;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.weightx = 0.0;
        gc.insets = new Insets(0,0,0,UIConfig.INSTANCE.getDefaultPadding());
        panel.add(imageLabel, gc);

        gc.gridx = 1; gc.gridy = 0;
        gc.gridwidth =1; gc.gridheight=2;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 1.0f;
        gc.insets = new Insets(0, 0, 0, UIConfig.INSTANCE.getDefaultPadding());
        panel.add(bitnameLabel, gc);


        gc.gridx = 2; gc.gridy = 0;
        gc.gridwidth = 1; gc.gridheight=2;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 0.0;
        gc.weighty = 0.0f;
        gc.insets = new Insets(0,0,0,UIConfig.INSTANCE.getDefaultPadding()/2);
        panel.add(moveDownButton, gc);

        gc.gridx = 3; gc.gridy = 0;
        gc.gridwidth = 1; gc.gridheight=2;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 0.0;
        gc.weighty = 0.0f;
        gc.insets = new Insets(0,0,0,UIConfig.INSTANCE.getDefaultPadding()/2);
        panel.add(moveUpButton, gc);

        gc.gridx = 4; gc.gridy = 0;
        gc.gridwidth = 1; gc.gridheight=2;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 0.0;
        gc.weighty = 0.0f;
        gc.insets = new Insets(0,0,0, UIConfig.INSTANCE.getDefaultPadding()/2);
        panel.add(deleteButton, gc);


    }

    /**
     * Adding the custom event listeners to PointBox objects. The goal is to make
     * the Point attribute react to change events
     *
     * @param pb    {@code PointBox object}
     * @param index index of the PointBox
     */
    private void addEventListenerToPointBox(PointsBox pb, int index) {

        // Listen for changes in the X field
        pb.getxInput().getNumericInput().addPropertyChangeListener("value", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                CutDTO c = CutBox.this.cut;
                VertexDTO oldVertex = c.getPoints().get(index);
                Number n = (Number) evt.getNewValue();
                VertexDTO newVertex = new VertexDTO(n.doubleValue(), oldVertex.getY(), oldVertex.getZ());
                c.getPoints().set(index, newVertex);
                mainWindow.getController().modifyCut(c);
                listener.modifiedAttributeEventOccured(new ChangeAttributeEvent(this, CutBox.this));
            }
        });
        // Listen for changes in the Y field
        pb.getyInput().getNumericInput().addPropertyChangeListener("value", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                CutDTO c = CutBox.this.cut;
                VertexDTO oldVertex = c.getPoints().get(index);
                Number n = (Number) evt.getNewValue();
                VertexDTO newVertex = new VertexDTO(oldVertex.getX(), n.doubleValue(), oldVertex.getZ());
                c.getPoints().set(index, newVertex);
                mainWindow.getController().modifyCut(c);
                listener.modifiedAttributeEventOccured(new ChangeAttributeEvent(this, CutBox.this));
            }
        });
    }

    /**
     * Adding the custom event listeners to SingleValueBox objects. The goal is to make
     * the Value attribute react to change events
     *
     * @param sb {@code SingleValueBox object}
     */
    private void addEventListenerToSingleValue(SingleValueBox sb) {
        sb.getInput().getNumericInput().addPropertyChangeListener("value", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                CutDTO c = CutBox.this.cut;
                Number n = (Number) evt.getNewValue();
                c = new CutDTO(c.getId(), n.doubleValue(), c.getBitIndex(), c.getCutType(), c.getPoints());
                mainWindow.getController().modifyCut(c);
                listener.modifiedAttributeEventOccured(new ChangeAttributeEvent(this, CutBox.this));
            }
        });
    }

    /**
     * Adding the custom event listeners to ChoiceBox objects. The goal is to make
     * the ComboBox attribute react to change events
     *
     * @param cb {@code ChoiceBox object}
     */
    private void addEventListenerToChoiceBox(ChoiceBox cb) {
        cb.getComboBox().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox comboBox = (JComboBox) e.getSource();
                CutDTO c = CutBox.this.cut;
                CutType chosenCutType = CutType.values()[comboBox.getSelectedIndex()];
                c = new CutDTO(c.getId(), c.getDepth(), c.getBitIndex(), chosenCutType, c.getPoints());
                mainWindow.getController().modifyCut(c);
                listener.modifiedAttributeEventOccured(new ChangeAttributeEvent(this, CutBox.this));
            }
        });
    }

    /**
     * Adding the custom event listeners to BitChoiceBox objects. The goal is to make
     * the ComboBox attribute react to change events
     *
     * Called when the user selects a new bit in the BitChoiceBox
     *
     * @param cb {@code BitChoiceBox object} The combo box containing the bits informations
     */
    private void addEventListenerToBitChoiceBox(BitChoiceBox cb) {
        cb.getComboBox().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox comboBox = (JComboBox) e.getSource();
                CutDTO c = CutBox.this.cut;
                ComboBitItem chosenBit = (ComboBitItem) comboBox.getModel().getSelectedItem();
                c = new CutDTO(c.getId(), c.getDepth(), chosenBit.getIndex(), c.getCutType(), c.getPoints());
                mainWindow.getController().modifyCut(c);
                listener.modifiedAttributeEventOccured(new ChangeAttributeEvent(this, CutBox.this));
            }
        });
    }

    /**
     * Initialize variables relevant to the Attribute Panel
     */
    private void init_attribute() {
        pointsBox1 = new PointsBox(true, "Point1", this.cut.getPoints().get(0));
        pointsBox2 = new PointsBox(true, "Point2", this.cut.getPoints().get(1));
        depthBox = new SingleValueBox(true, "Profondeur", this.cut.getDepth());

        Map<Integer, BitDTO> configuredBitsMap = mainWindow.getMiddleContent().getConfiguredBitsMap();

        int index = 0;
        for(Map.Entry<Integer, BitDTO> entry : configuredBitsMap.entrySet()){
            if(entry.getKey().equals(this.cut.getBitIndex())){
                break;
            }
            index++;
        }
        bitChoiceBox = new BitChoiceBox(true, "Outil", configuredBitsMap, index);

        ArrayList<JLabel> labelList = new ArrayList<>();
        for (CutType t : CutType.values()) {
            JLabel l = new JLabel(UiUtil.getIcon(UiUtil.getIconFileName(t), UIConfig.INSTANCE.getCutBoxIconSize(),
                    UIManager.getColor("button.Foreground")));
            l.setText(UiUtil.getIconName(t));
            labelList.add(l);
        }
        cuttypeBox = new ChoiceBox(true, "Type de coupe", labelList, this.cut.getCutType().ordinal());
    }


}
