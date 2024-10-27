package UI.SubWindows;

import Domain.DTO.CutDTO;
import Domain.CutType;
import Domain.DTO.VertexDTO;
import UI.CutWindow;
import UI.Events.ChangeAttributeEvent;
import UI.Events.ChangeAttributeListener;
import UI.UIConfig;
import UI.Widgets.CutBox;

import javax.swing.*;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.UUID;

/**
 * The {@code CutList} class is a UI class that encapsulates the list of the cuts sub-window
 *
 * @author Louis-Etienne Messier
 * @version 0.1
 * @since 2024-09-21
 */
public class CutListPanel extends BasicWindow implements  ChangeAttributeListener{
    private ArrayList<CutBox> cutBoxes;
    private ArrayList<CutDTO> cuts;
    private BasicWindow panel;
    private BoxLayout layout;
    private JScrollPane scrollPane;
    private ChangeAttributeListener listener;

    /**
     * Constructs a {@code CutList} by initializing all of it's attributes
     */
    public CutListPanel(boolean haveBackground, ChangeAttributeListener listener) {
        super(haveBackground);
        this.listener = listener;
        this.init();
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
            CutBox temp = new CutBox(cut, i, this);
            this.cutBoxes.add(temp);
        }

        panel.removeAll();
        for (CutBox cutBox : this.cutBoxes){
            this.panel.add(cutBox.getPanel());
        }
    }

    /**
     * Deselect all CutBox(es)
     */
    public void refreshSelectedCutBox(){
        for (CutBox cb : cutBoxes){
            cb.deselect();
        }
    }

    /**
     * Initiates all of the {@code CutList} components
     */
    private void init() {
        this.cuts = new ArrayList<CutDTO>();
        cutBoxes = new ArrayList<CutBox>();
        panel = new BasicWindow(false);
        layout = new BoxLayout(panel, BoxLayout.PAGE_AXIS);
        scrollPane = new JScrollPane(panel);
        panel.setOpaque(false);
        panel.setBackground(null);
        panel.setLayout(layout);
        panel.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(UIConfig.INSTANCE.getScrollbarSpeed());
        this.setupHeader("Coupes", scrollPane);
        panel.setAlignmentX(0);
        scrollPane.setAlignmentX(0);

        ArrayList<VertexDTO> tempList = new ArrayList<VertexDTO>();
        tempList.add(new VertexDTO(0, 0, 0));
        tempList.add(new VertexDTO(5.0f/3.14f, 4.00006f, 34.34f));

        ArrayList<VertexDTO> tempList2 = new ArrayList<VertexDTO>();
        tempList2.add(new VertexDTO(45.0f, 12.0f, 0));
        tempList2.add(new VertexDTO(3.3333f, 4.00006f, 34.34f));

        //TEST FOR DRAWING
        cuts.add(new CutDTO(new UUID(100000, 100000),
                0.5f, 5,
                CutType.LINE_HORIZONTAL,
                tempList));

        cuts.add(new CutDTO(new UUID(100000, 100000),
                0.8f, 7,
                CutType.LINE_VERTICAL,
                tempList));

        cuts.add(new CutDTO(new UUID(100000, 100000),
                0.8f, 7,
                CutType.BORDER,
                tempList2));

        cuts.add(new CutDTO(new UUID(100000, 100000),
                0.8f, 7,
                CutType.RECTANGULAR,
                tempList));

        cuts.add(new CutDTO(new UUID(100000, 100000),
                0.8f, 7,
                CutType.L_SHAPE,
                tempList2));

        cuts.add(new CutDTO(new UUID(100000, 100000),
                0.8f, 7,
                CutType.L_SHAPE,
                tempList2));

        cuts.add(new CutDTO(new UUID(100000, 100000),
                0.8f, 7,
                CutType.L_SHAPE,
                tempList));

        updateCutBoxes();
    }
    /**
     * Set the selectedAttributable of the parent window. The point is to simply pass down that information to the parent
     */
    @Override
    public void changeAttributeEventOccurred(ChangeAttributeEvent e) {
        refreshSelectedCutBox();
        ChangeAttributeEvent event = new ChangeAttributeEvent(this, e.getAttribute());
        listener.changeAttributeEventOccurred(event);
    }
}
