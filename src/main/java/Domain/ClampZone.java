package Domain;

import Domain.DTO.VertexDTO;
import Domain.ThirdDimension.Vertex;

import java.util.UUID;

/**
 * The {@code ClamZone} class represent a zone where a {@code PanelCNC} cannot be cut.
 *
 * @author Adam Côté
 * @version 1.0
 * @since 2024-10-20
 */
public class ClampZone {
    private final Vertex[] zone = new Vertex[2];
    private UUID id;

    /**
     * Constructs a new ClampZone
     */
    ClampZone() {
        //todo
    }

    UUID getId() {
        return this.id;
    }

    /**
     * Finds if a coordinate is in the current {@code ClampZone}.
     *
     * @param coordinatesmm The coordinate.
     * @return A boolean indicating if the point is in the ClampZone.
     */
    boolean pointCollision(VertexDTO coordinatesmm) {
        //todo
        return false;
    }
}
