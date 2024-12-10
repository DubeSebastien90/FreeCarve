package UI.Widgets;

import Annotations.VariableSource;
import Common.Interfaces.IUnitConverter;
import Domain.Controller;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class ImperialFractionalNumberFormatterTest {
    public static Stream<Arguments> stringToValue_HappyPath = Stream.of(
            Arguments.of("1.1", 1.1),
            Arguments.of("banane", 0.0),
            Arguments.of("1'", 12),
            Arguments.of("1'3\"", 15),
            Arguments.of("1'3\"1/2", 15.5),
            Arguments.of("1'3 1/2", 15.5),
            Arguments.of("1'3\"    1/2", 15.5),
            Arguments.of("1'3    1/2", 15.5),
            Arguments.of("1'3\"1/0", 15.0),
            Arguments.of("3\"1/0", 3.0),
            Arguments.of("3\"1/2", 3.5),
            Arguments.of("3 1/2", 3.5),
            Arguments.of("3\"   10/20", 3.5),
            Arguments.of("1", 1));

    @ParameterizedTest
    @VariableSource("stringToValue_HappyPath")
    void stringToValue_HappyPath_ReturnsCorrectValue(String text, double expected) {
        // Arrange
        IUnitConverter converter = Controller.initialize();
        ImperialFractionalNumberFormatter formatter = new ImperialFractionalNumberFormatter(converter);

        // Act
        double result = (double)formatter.stringToValue(text);

        // Assert
        assertEquals(expected, result, 0.001);
    }

    public static Stream<Arguments> stringToValue_NegativeValues = Stream.of(
            Arguments.of("-1.1", -1.1),
            Arguments.of("-banane", 0.0),
            Arguments.of("-1'", -12),
            Arguments.of("-1'3\"", -15),
            Arguments.of("-1'3\"1/2", -15.5),
            Arguments.of("-1'3 1/2", -15.5),
            Arguments.of("-1'3\"    1/2", -15.5),
            Arguments.of("-1'3    1/2", -15.5),
            Arguments.of("-1'3\"1/0", -15.0),
            Arguments.of("-3\"1/0", -3.0),
            Arguments.of("-3\"1/2", -3.5),
            Arguments.of("-3 1/2", -3.5),
            Arguments.of("-3\"   10/20", -3.5),
            Arguments.of("-1", -1));

    @ParameterizedTest
    @VariableSource("stringToValue_NegativeValues")
    void stringToValue_NegativeValues_ReturnsCorrectValue(String text, double expected) {
        // Arrange
        IUnitConverter converter = Controller.initialize();
        ImperialFractionalNumberFormatter formatter = new ImperialFractionalNumberFormatter(converter);

        // Act
        double result = (double)formatter.stringToValue(text);

        // Assert
        assertEquals(expected, result, 0.001);
    }

    @Test
    void stringToValue_WrongValue_ReturnsLastCorrectValue() {
        // Arrange
        IUnitConverter converter = Controller.initialize();
        ImperialFractionalNumberFormatter formatter = new ImperialFractionalNumberFormatter(converter);

        // Act
        double correctValue = (double)formatter.stringToValue("1.1");
        double result = (double)formatter.stringToValue("banane");

        // Assert
        Assertions.assertEquals(correctValue, result, 0.001);
    }

    @Test
    void stringToValue_BiggerThanMax_ReturnsMax() {
        // Arrange
        IUnitConverter converter = Controller.initialize();
        ImperialFractionalNumberFormatter formatter = new ImperialFractionalNumberFormatter(converter);
        double max = 50;
        formatter.setMaximum(max);

        // Act
        double result = (double)formatter.stringToValue(String.valueOf(max+1));

        // Assert
        Assertions.assertEquals(max, result, 0.001);
    }
}