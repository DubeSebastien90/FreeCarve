package Common.Interfaces;

import Common.DTO.DimensionDTO;
import Common.Units;

/**
 * Object that has unit conversion functionality
 * @author Kamran Charles Nayebi
 * @since 2024-11-19
 */
public interface IUnitConverter {
    DimensionDTO convertUnit(DimensionDTO toConvert, Units targetUnit);
}
