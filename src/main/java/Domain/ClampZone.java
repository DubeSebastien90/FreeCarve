package Domain;

import Common.DTO.ClampZoneDTO;
import Common.DTO.VertexDTO;
import Common.Exceptions.ClampZoneException;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

/**
 * The {@code ClamZone} class represent a zone where a {@code PanelCNC} cannot be cut.
 *
 * @author Adam Côté
 * @version 1.0
 * @since 2024-10-20
 */
class ClampZone {
    private final VertexDTO[] zone = new VertexDTO[2];
    private UUID id;

    /**
     * Constructs a new ClampZone
     */
    ClampZone(ClampZoneDTO clampZoneDTO) throws ClampZoneException {
        if (clampZoneDTO.getZone().length != 2 || clampZoneDTO.getZone()[0] == null || clampZoneDTO.getZone()[1] == null) {
            throw new ClampZoneException("La zone interdite doit avoir 2 points.");
        }

        if(clampZoneDTO.getZone()[0].equals(clampZoneDTO.getZone()[1])){
            throw new ClampZoneException("Les deux points de la zone interdite ne peuvent pas être les mêmes.");
        }

        this.zone[0] = clampZoneDTO.getZone()[0];
        this.zone[1] = clampZoneDTO.getZone()[1];
        this.id = UUID.randomUUID();
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
        double minX = Math.min(zone[0].getX(), zone[1].getX());
        double maxX = Math.max(zone[0].getX(), zone[1].getX());
        double minY = Math.min(zone[0].getY(), zone[1].getX());
        double maxY = Math.max(zone[0].getY(), zone[1].getX());

        return (coordinatesmm.getX() >= minX
                && coordinatesmm.getX() <= maxX
                && coordinatesmm.getY() >= minY
                && coordinatesmm.getY() <= maxY);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClampZone clampZone)) return false;
        return Objects.deepEquals(zone, clampZone.zone) && Objects.equals(id, clampZone.id);
    }

    public void modifyClamp(ClampZoneDTO newClamp) throws ClampZoneException {
        if (newClamp.getZone().length != 2 || newClamp.getZone()[0] == null || newClamp.getZone()[1] == null) {
            throw new ClampZoneException("La zone interdite doit avoir 2 points.");
        }

        if(newClamp.getZone()[0].equals(newClamp.getZone()[1])){
            throw new ClampZoneException("Les deux points de la zone interdite ne peuvent pas être les mêmes.");
        }

        this.zone[0] = newClamp.getZone()[0];
        this.zone[1] = newClamp.getZone()[1];
    }

    VertexDTO[] getZone() {
        return this.zone;
    }

    public boolean intersectCut(Cut cut) {
        for(VertexDTO vertex : cut.getAbsolutePointsPosition()){
            if(pointCollision(vertex)){
                return true;
            }
        }
        return false;
    }
}
