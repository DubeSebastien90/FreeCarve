package UI.Widgets;

import Common.CutState;
import Common.DTO.CutDTO;
import Common.InvalidCutState;
import Domain.CutType;
import UI.Events.ChangeAttributeEvent;
import UI.Events.ChangeCutEvent;
import UI.Events.ChangeCutListener;
import UI.MainWindow;
import UI.SubWindows.CutListPanel;
import UI.UIConfig;
import UI.UiUtil;
import UI.Widgets.AttributeContainer.*;
import com.formdev.flatlaf.ui.FlatButtonBorder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.UUID;

/**
 * The {@code CutBox} class is a UI class that encapsulates a box containing all the information
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
    private final MainWindow mainWindow;
    private ChangeCutListener cutListener;
    private JButton deleteButton;
    private JButton moveUpButton;
    private JButton moveDownButton;
    private CutBoxState tempState = CutBoxState.NOT_SELECTED;
    private CutBoxState realState = CutBoxState.NOT_SELECTED;
    private CutListPanel cutListPanel;
    private AttributeContainer attributeContainer;
    private AttributeContainerError invalidAttributeContainer;

    public enum CutBoxState {
        SELECTED,
        NOT_SELECTED,
        HOVER,
        NOT_VALID,
    }


    /**
     * Basic constructor of {@code CutBox}, initiates all the UI values and get a reference to the CutList parent
     *
     * @param cutDTO   cut that CutBox will present
     * @param index    index of the cut
     * @param listener reference to the parent listener
     */

    /**
     * Basic constructor of {@code CutBox}, initiates all the UI values and get a reference to the CutList parent
     *
     * @param cutDTO       cut that CutBox will present
     * @param index        index of the cut     *
     * @param cutListener  cutListener, parent of the CutBox
     * @param mainWindow   reference to the mainWindow
     * @param cutListPanel refernce to the cutListPanel
     */
    public CutBox(CutDTO cutDTO, int index, ChangeCutListener cutListener, MainWindow mainWindow, CutListPanel cutListPanel) {
        this.mainWindow = mainWindow;
        this.cut = new CutDTO(cutDTO);
        this.index = index;
        this.cutListener = cutListener;
        this.cutListPanel = cutListPanel;
        this.init();
        this.attributeContainer = AttributeContainerFactory.create(mainWindow, cutListPanel, cutDTO, this);
        this.attributeContainer.setupEventListeners();
        this.setBackgroundToIndex();
        this.updatePanel(this.cut);
        this.setupMouseEvents();

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
        if(this.cut.getState() == CutState.VALID){
            return attributeContainer;
        }
        else{
            this.invalidAttributeContainer = AttributeContainerFactory.createError(mainWindow, cutListPanel, this.cut, this);
            this.invalidAttributeContainer.setupEventListeners();
            return invalidAttributeContainer;
        }
    }

    /**
     * Modify all of the attributes and UI values of the CutBox based on a new CutDTO
     *
     * @param newCutDTO new CutDTO to modify the CutBox with
     */
    public void updatePanel(CutDTO newCutDTO) {
        this.cut = new CutDTO(newCutDTO); // duplicate the cutDTO to modify it's own copy

        // Setting the bit info
        bitnameLabel.setText("Outil : " + (mainWindow.getController().getBitsDTO()[newCutDTO.getBitIndex()].getName()));

        // Setting the index of the cut
        numberLabel.setText(String.valueOf(this.index + 1));

        // Setting the image of the cutbox
        CutType type = this.cut.getCutType();
        String iconName = UiUtil.getIconFileName(type);
        imageLabel.setIcon(UiUtil.getIcon(iconName, UIConfig.INSTANCE.getCutBoxIconSize(),
                UIManager.getColor("button.Foreground")));

        if(this.cut.getState() == CutState.NOT_VALID){
            setState(CutBoxState.NOT_VALID);
        }
    }

    /**
     * Modify all of the attributes and UI values of the AttributeContainer based on a new CutDTO
     *
     * @param newCutDTO new CutDTO to modify the CutBox with
     */
    public void updateAttributeContainerPanel(CutDTO newCutDTO){
        this.attributeContainer.updatePanel(newCutDTO);

        if(this.invalidAttributeContainer != null){
            this.invalidAttributeContainer.updatePanel(newCutDTO);
        }
    }

    /**
     * @return {@code JPanel} of the CutBox
     */
    public JPanel getPanel() {
        return this.panel;
    }

    public AttributeContainer getAttributeContainer(){
        return this.attributeContainer;
    }

    /**
     * Set the CutBox as non-selected
     */
    public void deselect() {
        setState(CutBoxState.NOT_SELECTED);
    }

    /**
     * Select this CutBox
     */
    public void select() {
        setState(CutBoxState.SELECTED);

    }

    /**
     * Go back one previous state
     */
    public void goBackState(){
        tempState = realState;
        setState(realState);
    }

    public void setIndex(int index){
        this.index = index;
    }

    /**
     * Set the state of the CutBox and it's underlying parameters
     *
     * @param state
     */
    public void setState(CutBoxState state) {

        if(state == CutBoxState.SELECTED){
            this.cutListPanel.refreshSelectedCutBox();
            tempState = state;
            realState = state;
            ChangeAttributeEvent event = new ChangeAttributeEvent(CutBox.this, CutBox.this);
            this.cutListPanel.changeAttributeEventOccurred(event);
            panel.setBackground(UIManager.getColor("Button.green"));
        }
        else if(state == CutBoxState.HOVER){
            tempState = state;
            ChangeAttributeEvent event = new ChangeAttributeEvent(CutBox.this, CutBox.this);
            this.cutListPanel.changeAttributeEventOccurred(event);
            panel.setBackground(UIManager.getColor("Button.blue"));
        }
        else if(state == CutBoxState.NOT_SELECTED){

            if(cut.getState() == CutState.NOT_VALID){
                tempState = CutBoxState.NOT_VALID;
                realState = CutBoxState.NOT_VALID;
                panel.setBackground(UIManager.getColor("Button.red"));
            }
            else{
                tempState = state;
                realState = state;
                setBackgroundToIndex();
            }

            ChangeAttributeEvent event = new ChangeAttributeEvent(CutBox.this, CutBox.this);
            this.cutListPanel.changeAttributeEventOccurred(event);
        }
        else if (state == CutBoxState.NOT_VALID){
            if(cut.getState() == CutState.NOT_VALID){

                if(getState() == CutBoxState.SELECTED && getTempState() == CutBoxState.SELECTED) // if selected, stay that way
                {
                    cutListPanel.refreshSelectedCutBox();
                    tempState = CutBoxState.SELECTED;
                    realState = CutBoxState.SELECTED;
                    panel.setBackground(UIManager.getColor("Button.green"));
                }
                else{
                    tempState = CutBoxState.NOT_VALID;
                    realState = CutBoxState.NOT_VALID;
                    panel.setBackground(UIManager.getColor("Button.red"));
                }

                ChangeAttributeEvent event = new ChangeAttributeEvent(CutBox.this, CutBox.this);
                this.cutListPanel.changeAttributeEventOccurred(event);
            }
        }
    }

    private void mouseEnteredPanel(){
        setState(CutBoxState.HOVER);
    }

    private void mouseExitedPanel(){
        goBackState();
    }

    /**
     * @return the current state of the CutBox
     */
    public CutBoxState getState() {
        return this.realState;
    }

    /**
     * returns the temporary state of the cutbox : hover
     * @return the temp state
     */
    public CutBoxState getTempState(){
        return this.tempState;
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
                if (tempState != CutBoxState.SELECTED && realState != CutBoxState.SELECTED) {
                    select();
                } else {
                    deselect();
                }

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                mouseEnteredPanel();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                mouseExitedPanel();
            }
        });
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
                UIConfig.INSTANCE.getDefaultPadding(), UIConfig.INSTANCE.getDefaultPadding()));
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

        MouseAdapter buttonHoverAdapter = new MouseAdapter() { // to transfer events to the CutBox class
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                mouseEnteredPanel();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                mouseExitedPanel();
            }
        };

        deleteButton.addMouseListener(buttonHoverAdapter);
        moveUpButton.addMouseListener(buttonHoverAdapter);
        moveDownButton.addMouseListener(buttonHoverAdapter);
        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.weightx = 0.0;
        gc.anchor = GridBagConstraints.CENTER;
        gc.insets = new Insets(0, 0, 0, UIConfig.INSTANCE.getDefaultPadding());
        panel.add(numberLabel, gc);

        gc.gridx = 0;
        gc.gridy = 1;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.weightx = 0.0;
        gc.insets = new Insets(0, 0, 0, UIConfig.INSTANCE.getDefaultPadding());
        panel.add(imageLabel, gc);

        gc.gridx = 1;
        gc.gridy = 0;
        gc.gridwidth = 1;
        gc.gridheight = 2;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 1.0f;
        gc.insets = new Insets(0, 0, 0, UIConfig.INSTANCE.getDefaultPadding());
        panel.add(bitnameLabel, gc);
//
//
//        gc.gridx = 2;
//        gc.gridy = 0;
//        gc.gridwidth = 1;
//        gc.gridheight = 2;
//        gc.anchor = GridBagConstraints.CENTER;
//        gc.fill = GridBagConstraints.NONE;
//        gc.weightx = 0.0;
//        gc.weighty = 0.0f;
//        gc.insets = new Insets(0, 0, 0, UIConfig.INSTANCE.getDefaultPadding() / 2);
//        panel.add(moveDownButton, gc);
//
//        gc.gridx = 3;
//        gc.gridy = 0;
//        gc.gridwidth = 1;
//        gc.gridheight = 2;
//        gc.anchor = GridBagConstraints.CENTER;
//        gc.fill = GridBagConstraints.NONE;
//        gc.weightx = 0.0;
//        gc.weighty = 0.0f;
//        gc.insets = new Insets(0, 0, 0, UIConfig.INSTANCE.getDefaultPadding() / 2);
//        panel.add(moveUpButton, gc);

        gc.gridx = 2;
        gc.gridy = 0;
        gc.gridwidth = 1;
        gc.gridheight = 2;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 0.0;
        gc.weighty = 0.0f;
        gc.insets = new Insets(0, 0, 0, UIConfig.INSTANCE.getDefaultPadding() / 2);
        panel.add(deleteButton, gc);
    }

}
