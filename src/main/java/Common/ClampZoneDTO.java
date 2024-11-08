package Common;

/**
 * The {@code ClampZoneDTO} class is a read-only {@code ClampZone}
 *
 * @author Adam Côté
 * @version 1.0
 * @since 2024-10-20
 */
public class ClampZoneDTO {
    private final VertexDTO[] zone = new VertexDTO[2];

    public ClampZoneDTO(VertexDTO first, VertexDTO second) {
        zone[0] = first;
        zone[1] = second;
    }

    public VertexDTO[] getZone() {
        return zone;
    }
}
