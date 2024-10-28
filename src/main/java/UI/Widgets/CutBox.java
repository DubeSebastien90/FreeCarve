package UI.Widgets;

import Domain.CutDTO;
import Domain.CutType;
import Domain.ThirdDimension.VertexDTO;
import UI.Events.ChangeAttributeEvent;
import UI.Events.ChangeAttributeListener;
import UI.SubWindows.BasicWindow;
import UI.UIConfig;
import Util.UiUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

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
    private RoundedJLabel coordinateLabel;
    private JLabel imageLabel;
    private int index;
    private boolean selected;
    private ChangeAttributeListener listener;
    /**
     * Basic constructor of {@code CutBox}, initiates all of the UI values and get a reference to the CutList parent
     * @param cutDTO cut that CutBox will present
     * @param index index of the cut
     * @param listener reference to the parent listener
     */
    public CutBox(CutDTO cutDTO, int index, ChangeAttributeListener listener){
        this.cut = cutDTO;
        this.index = index;
        this.listener = listener;
        this.init();
        this.setBackgroundToIndex();
        this.updatePanel(this.cut);
        this.setupMouseEvents();
    }

    @Override
    public JLabel showName(){
        JLabel label = new JLabel(this.imageLabel.getIcon());
        label.setText("Coupe " + this.index);
        label.setBackground(Color.YELLOW);
        label.setBorder(new EmptyBorder(0,0,UIConfig.INSTANCE.getDefaultPadding(), 0));
        return label;
    }

    /**
     * Function override of the Attributable interface
     * @return {@code JPanel} of the attribute modification of the CutBox
     */
    @Override
    public JPanel showAttribute() {
        BasicWindow container = new BasicWindow(true);
        container.setBackground(null);
        container.setOpaque(false);
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gc = new GridBagConstraints();
        container.setLayout(layout);
        PointsBox pointsBox1 = new PointsBox(true, "Point1", this.cut.getPoints().get(0));
        PointsBox pointsBox2 = new PointsBox(true, "Point2", this.cut.getPoints().get(1));
        gc.gridx = 0; gc.gridy = 0;
        gc.weightx = 1; gc.weighty = 1;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding()/3, 0);
        container.add(pointsBox1, gc);

        gc.gridx = 0; gc.gridy = 1;
        gc.insets = new Insets(0, 0, 0,  0);
        container.add(pointsBox2, gc);

        return container;
    }

    /**
     * Modify all of the attributes and UI values of the CutBox based on a new CutDTO
     * @param newCutDTO new CutDTO to modify the CutBox with
     */
    public void updatePanel(CutDTO newCutDTO){
        this.cut = newCutDTO;

        // Setting the bit info
        bitnameLabel.setText((String.valueOf(newCutDTO.getBitIndex())));

        // Setting the index of the cut
        numberLabel.setText(String.valueOf(this.index));

        // Setting the image of the cutbox
        CutType type = this.cut.getCutType();
        String iconName = "";
        switch(type){
            case BORDER -> iconName = "forbidden";
            case L_SHAPE -> iconName = "coupeL";
            case RECTANGULAR -> iconName = "rectangle";
            case LINE_HORIZONTAL -> iconName = "parallel";
            case LINE_VERTICAL -> iconName = "parallel";
            default -> iconName = "forbidden"; // default in case of bad name of icon
        }
        imageLabel.setIcon(UiUtil.getIcon(iconName, UIConfig.INSTANCE.getCutBoxIconSize(),
                UIManager.getColor("button.Foreground")));

        // Setting the coordiante value of the Cut
        List<VertexDTO> points = newCutDTO.getPoints();
        String coordinateText = points.getFirst().format2D() + " - " + points.getLast().format2D();
        coordinateLabel.setText(coordinateText);
    }

    /**
     * @return {@code JPanel} of the CutBox
     */
    public JPanel getPanel(){
        return this.panel;
    }

    /**
     * Set the CutBox as non-selected
     */
    public void deselect(){
        this.selected = false;
        setBackgroundToIndex();

    }

    /**
     * Set the background of the CutBox according to it's index
     */
    private void setBackgroundToIndex(){
        if (this.index % 2 == 0){
            this.panel.setBackground(UIManager.getColor("SubWindow.darkBackground1"));
        }
        else{
            this.panel.setBackground(UIManager.getColor("SubWindow.darkBackground2"));
        }
    }

    /**
     * Setup the custom mouse events of the CutBox
     */
    private void setupMouseEvents(){
        this.panel.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                ChangeAttributeEvent event = new ChangeAttributeEvent(CutBox.this, CutBox.this);
                listener.changeAttributeEventOccurred(event);
                selected = true;
                panel.setBackground(UIManager.getColor("Button.green"));
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if(!selected) panel.setBackground(UIManager.getColor("Button.blue"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if(!selected) setBackgroundToIndex();
            }
        });
    }


    /**
     * Initialize the UI components
     */
    private void init(){

        layout = new GridBagLayout();
        panel = new JPanel(layout);
        panel.setAlignmentX(0);
        panel.setBorder(new EmptyBorder(UIConfig.INSTANCE.getDefaultPadding(), UIConfig.INSTANCE.getDefaultPadding(),
                UIConfig.INSTANCE.getDefaultPadding(), UIConfig.INSTANCE.getDefaultPadding()));
        GridBagConstraints gc = new GridBagConstraints();
        bitnameLabel = new RoundedJLabel("Bitname placeholder", 15);
        numberLabel = new JLabel("Number placeholder");
        coordinateLabel = new RoundedJLabel("(Coordinates label) - (Placeholder)", 15);
        imageLabel = new JLabel(UiUtil.getIcon("coupeL", UIConfig.INSTANCE.getCutBoxIconSize(),
                UIManager.getColor("button.Foreground")));
        numberLabel.setBackground(Color.RED);
        bitnameLabel.setBackground(UIManager.getColor("SubWindow.background"));
        bitnameLabel.setHorizontalAlignment(JLabel.CENTER);
        coordinateLabel.setHorizontalAlignment(JLabel.CENTER);
        coordinateLabel.setBackground(UIManager.getColor("SubWindow.background"));
        numberLabel.putClientProperty("FlatLaf.style", "font: bold $h3.regular.font");
        coordinateLabel.setMinimumSize(new Dimension(100, 100));


        gc.gridx = 0; gc.gridy = 0;
        gc.gridwidth =1; gc.gridheight=1;
        gc.weightx = 0.0;
        gc.anchor = GridBagConstraints.CENTER;
        gc.insets = new Insets(0,0,0,5);
        panel.add(numberLabel, gc);

        gc.gridx = 0; gc.gridy = 1;
        gc.gridwidth =1; gc.gridheight=1;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.weightx = 0.0;
        panel.add(imageLabel, gc);

        gc.gridx = 1; gc.gridy = 0;
        gc.gridwidth =3; gc.gridheight=1;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0;
        gc.insets = new Insets(5,0,5,0);
        panel.add(bitnameLabel, gc);

        gc.gridx = 1; gc.gridy = 1;
        gc.gridwidth =3; gc.gridheight=1;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0;
        panel.add(coordinateLabel, gc);
    }


}
