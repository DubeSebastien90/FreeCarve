package UI.Widgets;

import Domain.DTO.CutDTO;
import Domain.CutType;
import UI.UIConfig;
import Domain.Cut;
import Util.UiUtil;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.LabelUI;
import java.awt.*;

/**
 * The {@code CutBox} class is a UI class that encapsulates a box containing all the informations
 * about a specific cut
 *
 * @author Louis-Etienne Messier
 * @version 0.1
 * @since 2024-09-21
 */
public class CutBox {
    private CutDTO cut;
    private JPanel panel;
    private GridBagLayout layout;
    private RoundedJLabel bitnameLabel;
    private JLabel numberLabel;
    private RoundedJLabel coordinateLabel;
    private JLabel imageLabel;

    public CutBox(CutDTO cutDTO, int index){
        this.cut = cutDTO;
        this.init();
        this.updatePanel(this.cut, index);
    }

    private void init(){

        layout = new GridBagLayout();
        panel = new JPanel(layout);
        panel.setAlignmentX(0);
        panel.setBorder(new EmptyBorder(5,5,5,5));
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

    public void updatePanel(CutDTO newCutDTO, int index){
        this.cut = newCutDTO;
        bitnameLabel.setText((String.valueOf(newCutDTO.getBitIndex())));
        numberLabel.setText(String.valueOf(index));

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
        //        coordinateLabel.setText();
        imageLabel.setIcon(UiUtil.getIcon(iconName, UIConfig.INSTANCE.getCutBoxIconSize(),
                UIManager.getColor("button.Foreground")));
    }

    public JPanel getPanel(){
        return this.panel;
    }

}
