package UI.SubWindows;

import Domain.ThirdDimension.*;
import UI.MainWindow;
import UI.Widgets.MeshManipulator;
import Util.UiUtil;

import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.UUID;

/**
 * The {@code Renderer} class provides function to render meshes on it, it extends {@code JPanel}.
 *
 * @author Adam Côté
 * @author Sébastien Dubé
 * @author Kamran Charles Nayebi
 * @since 2024-09-08
 */
public class Renderer extends JPanel {
    private VertexDTO mousePos;
    private final MeshManipulator meshManipulator;
    private UUID cameraId;

    /**
     * @return the mouse position in pixels of the last click on the viewport
     */
    public VertexDTO getMousePos() {
        return mousePos;
    }

    /**
     * Constructs a new {@code Renderer} object with a {@code List} of {@code Mesh} and initialize {@code Renderer}
     *
     * @param cameraID the id of the {@code Camera} which it will show
     */
    public Renderer(UUID cameraID) {
        setDoubleBuffered(true);
        setFocusable(true);
        requestFocusInWindow();
        requestFocus();

        setCameraId(cameraID);
        setMousePos(new VertexDTO(0, 0, 0));

        meshManipulator = new MeshManipulator(this);
        addKeyListener(meshManipulator);
        addMouseListener(meshManipulator);

    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
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
     * PaUUIDs the rendered image of the scene from the camera
     *
     * @param graphics a {@code Graphics} object on which the image will be drawn
     */
    @Override
    public void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = ((Graphics2D) graphics);
        this.setBackground(Color.GRAY);
        UiUtil.makeJPanelRoundCorner(this, graphics2D);
        super.paintComponent(graphics2D);

        BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);

        Optional<UUID> selectedMesh = MainWindow.INSTANCE.getController().renderImage(img, mousePos);
        selectedMesh.ifPresent(meshManipulator::setSelectedMesh);

        graphics2D.drawImage(img, 0, 0, null);
        setMousePos(new VertexDTO(0, 0, 0));
    }

    public UUID getCameraId() {
        return cameraId;
    }

    public void setCameraId(UUID cameraId) {
        this.cameraId = cameraId;
    }
}
