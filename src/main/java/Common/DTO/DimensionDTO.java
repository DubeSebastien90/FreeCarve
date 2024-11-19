package Common.DTO;

import Common.Units;

/**
 * Tuple that represents a dimension with numeric value and a unit
 *
 * @author Kamran Charles Nayebi
 * @since 2024-11-19
 */
public record DimensionDTO(double value, Units unit) {
}