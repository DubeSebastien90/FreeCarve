package Domain;

import Domain.ThirdDimension.Vertex;

/**
 * The {@code ClampZoneDTO} class is a read-only {@code ClampZone}
 *
 * @author Adam Côté
 * @version 1.0
 * @since 2024-10-20
 */
public class ClampZoneDTO {
    private final Vertex[] zone = new Vertex[2];

    public ClampZoneDTO(Vertex first, Vertex second) {
        zone[0] = first;
        zone[1] = second;
    }

    public Vertex[] getZone() {
        return zone;
    }
}
