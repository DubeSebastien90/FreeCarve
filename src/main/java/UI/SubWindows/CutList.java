package UI.SubWindows;

import Domain.DTO.CutDTO;
import Domain.CutType;
import UI.UIConfig;
import UI.Widgets.CutBox;
import Domain.Cut;
import Util.UiUtil;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.UUID;

/**
 * The {@code CutList} class is a UI class that encapsulates the list of the cuts sub-window
 *
 * @author Louis-Etienne Messier
 * @version 0.1
 * @since 2024-09-21
 */
public class CutList extends BasicWindow{
    private ArrayList<CutBox> cutBoxes;
    private ArrayList<CutDTO> cuts;
    private BasicWindow scrollablePanel;
    private BoxLayout layout;
    private JScrollPane scrollPane;
    /**
     * Constructs a {@code CutList} by initializing all of it's attributes
     */
    public CutList(boolean haveBackground) {
        super(haveBackground);
        this.init();
    }


    /**
     * Initiates all of the {@code CutList} components
     */
    private void init() {
        this.cuts = new ArrayList<CutDTO>();

        cutBoxes = new ArrayList<CutBox>();
        scrollablePanel = new BasicWindow(true);
        layout = new BoxLayout(scrollablePanel, BoxLayout.PAGE_AXIS);
        scrollPane = new JScrollPane(scrollablePanel);
        scrollablePanel.setLayout(layout);
        scrollablePanel.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(UIConfig.INSTANCE.getScrollbarSpeed());
        this.setupHeader("Coupes", scrollPane);
        scrollablePanel.setAlignmentX(0);
        scrollPane.setAlignmentX(0);

        //TEST FOR DRAWING
        cuts.add(new CutDTO(new UUID(100000, 100000),
                            0.5f, 5,
                            CutType.LINE_HORIZONTAL));

        cuts.add(new CutDTO(new UUID(100000, 100000),
                0.8f, 7,
                CutType.LINE_VERTICAL));

        cuts.add(new CutDTO(new UUID(100000, 100000),
                0.8f, 7,
                CutType.BORDER));

        cuts.add(new CutDTO(new UUID(100000, 100000),
                0.8f, 7,
                CutType.RECTANGULAR));

        cuts.add(new CutDTO(new UUID(100000, 100000),
                0.8f, 7,
                CutType.L_SHAPE));

        cuts.add(new CutDTO(new UUID(100000, 100000),
                0.8f, 7,
                CutType.L_SHAPE));

        cuts.add(new CutDTO(new UUID(100000, 100000),
                0.8f, 7,
                CutType.L_SHAPE));
        updateCutBoxes();
    }

    /**
     * Change the cutList, with a new list of CutDTO, and updates the UI
     * @param newCuts list of new CutDTO
     */
    public void setCutList(ArrayList<CutDTO> newCuts){
        this.cuts = newCuts;
        updateCutBoxes();
    }

    /**
     * Updates the UI of the CutList based on the stored CutDTO
     */
    private void updateCutBoxes(){
        this.cutBoxes = new ArrayList<CutBox>();
        for (int i =0; i < cuts.size(); i++){
            CutDTO cut = cuts.get(i);
            CutBox temp = new CutBox(cut, i);
            if (this.cutBoxes.size() % 2 == 0){
                temp.getPanel().setBackground(UIManager.getColor("SubWindow.darkBackground1"));
            }
            else{
                temp.getPanel().setBackground(UIManager.getColor("SubWindow.darkBackground2"));
            }
            this.cutBoxes.add(temp);
        }

        scrollablePanel.removeAll();
        for (CutBox cutBox : this.cutBoxes){
            this.scrollablePanel.add(cutBox.getPanel());
        }
    }
}
