package Common.DTO;

import java.util.Optional;
import java.util.UUID;

/**
 * The {@code ClampZoneDTO} class is a read-only {@code ClampZone}
 *
 * @author Adam Côté
 * @version 1.0
 * @since 2024-10-20
 */
public class ClampZoneDTO {
    private final VertexDTO[] zone = new VertexDTO[2];
    private Optional<UUID> clampId = Optional.empty();

    public ClampZoneDTO(VertexDTO first, VertexDTO second, Optional<UUID> clampId){
        zone[0] = first;
        zone[1] = second;
        this.clampId = clampId;
    }

    public VertexDTO[] getZone() {
        return this.zone;
    }

    public Optional<UUID> getClampId() {
        return this.clampId;
    }
}
