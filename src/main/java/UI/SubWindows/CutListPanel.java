package UI.SubWindows;

import Common.CutDTO;
import UI.Events.ChangeAttributeEvent;
import UI.Events.ChangeAttributeListener;
import UI.Events.ChangeCutListener;
import UI.MainWindow;
import UI.UIConfig;
import UI.Widgets.Attributable;
import UI.Widgets.CutBox;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The {@code CutList} class is a UI class that encapsulates the list of the cuts sub-window
 *
 * @author Louis-Etienne Messier
 * @version 0.1
 * @since 2024-09-21
 */
public class CutListPanel extends BasicWindow implements ChangeAttributeListener {
    private List<CutBox> cutBoxes;
    private List<CutDTO> cuts;
    private JPanel panel;
    private BoxLayout layout;
    private JScrollPane scrollPane;
    private ChangeAttributeListener listener;
    private final MainWindow mainWindow;
    private ChangeCutListener cutListener;

    /**
     * Constructs a {@code CutList} by initializing all of it's attributes
     */
    public CutListPanel(boolean haveBackground, ChangeAttributeListener listener, ChangeCutListener changeCutListener, MainWindow mainWindow) {
        super(haveBackground);
        this.mainWindow = mainWindow;
        this.listener = listener;
        this.cutListener = changeCutListener;
        this.init();
        update();
    }

    /**
     * Updates the UI of the CutList based on the stored CutDTO
     */
    public void update(){
        this.cuts = mainWindow.getController().getCutListDTO();
        this.cutBoxes = new ArrayList<CutBox>();
        for (int i = 0; i < cuts.size(); i++) {
            CutDTO cut = cuts.get(i);
            CutBox temp = new CutBox(cut, i, this, this.cutListener, mainWindow);
            this.cutBoxes.add(temp);
        }
        panel.removeAll();
        for (CutBox cutBox : this.cutBoxes) {
            this.panel.add(cutBox.getPanel());
        }

        this.revalidate();
        this.repaint();
    }

    /**
     * Deselect all CutBox(es)
     */
    public void refreshSelectedCutBox() {
        for (CutBox cb : cutBoxes) {
            cb.deselect();
        }
    }

    /**
     * Initiates all of the {@code CutList} components
     */
    private void init() {
        this.cuts = new ArrayList<CutDTO>();
        cutBoxes = new ArrayList<CutBox>();
        panel = new JPanel();
        layout = new BoxLayout(panel, BoxLayout.PAGE_AXIS);
        scrollPane = new JScrollPane(panel);
        scrollPane.setOpaque(false);
        panel.setBackground(UIManager.getColor("SubWindow.background"));
        panel.setLayout(layout);
        panel.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(UIConfig.INSTANCE.getScrollbarSpeed());
        this.setupHeader("Coupes", scrollPane);
        panel.setAlignmentX(0);
        scrollPane.setAlignmentX(0);
        update();
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

    /**
     * Modify the CutBoxes in the CutList after one of them had a successful modification accepted by the DOMAIN
     */
    @Override
    public void modifiedAttributeEventOccured(ChangeAttributeEvent event) {
        Attributable att = event.getAttribute();
        CutBox c = (CutBox) att;

        Optional<CutDTO> cut = mainWindow.getController().findSpecificCut(c.getCutUUID());
        cut.ifPresent(c::updatePanel);

        listener.modifiedAttributeEventOccured(event);
    }

    /**
     * Find a cutbox with a specific ID
     * @param id id to search with
     * @return Optional<CutBox> : CutBox if found, null if not found
     */
    public Optional<CutBox> getCutBoxWithId(UUID id){
        for (CutBox c : this.cutBoxes){
            if(c.getCutUUID() == id){
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }
}
