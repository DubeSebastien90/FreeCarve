package UI.SubWindows;

import UI.Widgets.Header;
import Util.UiUtil;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class BasicWindow extends JPanel {


    public BasicWindow(boolean haveBackground) {
        this.setLayout(new GridBagLayout());
        if (haveBackground) {
            this.setBackground(UIManager.getColor("SubWindow.background"));
        }
    }

    /**
     * Instantiate the header of the subwindow, adding the header and the panel going under it
     * @param headerName name of the header
     * @param underHeaderPanel {@code JPanel} that goes under the header
     */
    public void setupHeader(String headerName, JPanel underHeaderPanel){
        this.setLayout(new BorderLayout());
        Header header = new Header(headerName);
        this.add(header, BorderLayout.NORTH);
        this.add(underHeaderPanel, BorderLayout.CENTER);
    }

    /**
     * Instantiate the header of the subwindow, adding the header and the panel going under it
     * @param headerName name of the header
     * @param underHeaderPanel {@code JPanel} that goes under the header
     */
    public void setupHeader(String headerName, JScrollPane underHeaderPanel){
        this.setBackground(UIManager.getColor("SubWindow.header"));
        this.setLayout(new BorderLayout());
        Header header = new Header(headerName);
        this.add(header, BorderLayout.NORTH);
        this.setBorder(new EmptyBorder(0,0, 10, 0));
        this.add(underHeaderPanel, BorderLayout.CENTER);
    }

    @Override
    public void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        UiUtil.makeJPanelRoundCorner(this, graphics2D);
        super.paintComponent(graphics2D);
    }

}
