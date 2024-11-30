package UI.SubWindows;

import Common.DTO.VertexDTO;
import UI.Listeners.MeshManipulator;
import UI.MainWindow;
import UI.UiUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.UUID;

/**
 * The {@code Rendering3DWindow} class deals with displaying and the interaction with the 3d view, it extends {@code JPanel}.
 *
 * @author Adam Côté
 * @author Sébastien Dubé
 * @author Kamran Charles Nayebi
 * @since 2024-09-08
 */
public class Rendering3DWindow extends JPanel {
    private VertexDTO mousePos;
    private final MeshManipulator meshManipulator;
    private UUID cameraId;
    private final MainWindow mainWindow;
    private final JLabel mouselabel;

    /**
     * @return the mouse position in pixels of the last click on the viewport
     */
    public VertexDTO getMousePos() {
        return mousePos;
    }

    /**
     * Constructs a new {@code Rendering3DWindow} object with a {@code UUID} of a {@code Camera} and sets up the interaction
     *
     * @param cameraID the id of the {@code Camera} which it will show
     */
    public Rendering3DWindow(UUID cameraID, MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        setDoubleBuffered(true);
        setFocusable(true);
        requestFocusInWindow();
        requestFocus();

        setCameraId(cameraID);
        setMousePos(new VertexDTO(0, 0, 0));

        meshManipulator = new MeshManipulator(this, mainWindow);
        addKeyListener(meshManipulator);
        addMouseListener(meshManipulator);
        addMouseWheelListener(meshManipulator);
        addMouseMotionListener(meshManipulator);
        mouselabel = new JLabel("0;0");
        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                mouselabel.setText(e.getX() + " ; " + e.getY());
                Rendering3DWindow.this.remove(mouselabel);
                Rendering3DWindow.this.add(mouselabel);
            }
        });
    }

    /**
     * Sets the mouse position in pixels of the last click on the viewport
     *
     * @param mousePos the mouse position in pixels of the last click on the viewport
     */
    public void setMousePos(VertexDTO mousePos) {
        this.mousePos = mousePos;
    }

    /**
     * Paints the rendered image of the scene from the camera
     *
     * @param graphics a {@code Graphics} object on which the image will be drawn
     */
    @Override
    public void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = ((Graphics2D) graphics);
        this.setBackground(UIManager.getColor("SubWindow.lightBackground1"));
        UiUtil.makeJPanelRoundCorner(this, graphics2D);
        super.paintComponent(graphics2D);

        BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);

        Optional<UUID> selectedMesh = mainWindow.getController().renderImage(img, mousePos);
        selectedMesh.ifPresent(meshManipulator::setSelectedMesh);

        graphics2D.drawImage(img, 0, 0, null);
        setMousePos(new VertexDTO(0, 0, 0));
    }

    /**
     * @return the id of the camera whose view it's displaying
     */
    public UUID getCameraId() {
        return cameraId;
    }

    /**
     * @param cameraId the id of the camera whose view it's displaying
     */
    public void setCameraId(UUID cameraId) {
        this.cameraId = cameraId;
    }
}
