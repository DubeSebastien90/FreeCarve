package UI.Widgets;

import Common.DTO.DimensionDTO;
import Common.Interfaces.IUnitConverter;
import Common.Units;

import javax.swing.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImperialFractionalNumberFormatter extends JFormattedTextField.AbstractFormatter {
    private double min = -Double.MAX_VALUE;
    private double max = Double.MAX_VALUE;
    private IUnitConverter unitConverter;
    private double lastValue = 0.0;

    public ImperialFractionalNumberFormatter(IUnitConverter converter) {
        this.unitConverter = converter;
    }

    @Override
    public Object stringToValue(String text) {
        double value = parseDouble(text);
        if (Double.isNaN(value)) {
            return lastValue;
        }
        if (text.charAt(0) == '-'){
            value *= -1;
        }
        lastValue = value;
        return Math.clamp(value, min, max);
    }

    @Override
    public String valueToString(Object value) {
        DecimalFormat df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return value == null ? "0" : String.valueOf(df.format((double)value));
    }

    private double parseDouble(String text) {
        Pattern feetInchFraction = Pattern.compile("-?([1-9][0-9]*)'\\s*([1-9][0-9]*)[\"\\s]\\s*([0-9]+)/([1-9][0-9]*)");
        Pattern inchFraction = Pattern.compile("-?([1-9][0-9]*)[\"\\s]\\s*([0-9]+)/([1-9][0-9]*)");
        Pattern feetInch = Pattern.compile("-?([1-9][0-9]*)'\\s*([1-9][0-9]*)\"?");
        Pattern fraction = Pattern.compile("-?([0-9]+)/([1-9][0-9]*)");
        Pattern feetOnly = Pattern.compile("-?([1-9][0-9]*)'");
        Pattern inchesOnly = Pattern.compile("-?([1-9][0-9]*)\"");
        Pattern intOnly = Pattern.compile("-?([0-9]+)");
        Pattern doubleOnly = Pattern.compile("-?([0-9]+\\.[0-9]+)");
        Matcher matcher;

        // The order of the matches is important
        matcher = feetInchFraction.matcher(text);
        if (matcher.find()) {
            double pied = Double.parseDouble(matcher.group(1));
            pied = unitConverter.convertUnit(new DimensionDTO(pied, Units.FEET), Units.INCH).value();
            double ratio = Double.parseDouble(matcher.group(3)) / Double.parseDouble(matcher.group(4));
            return pied + Double.parseDouble(matcher.group(2)) + ratio;
        }
        matcher = inchFraction.matcher(text);
        if (matcher.find()) {
            double ratio = Double.parseDouble(matcher.group(2)) / Double.parseDouble(matcher.group(3));
            return Double.parseDouble(matcher.group(1)) + ratio;
        }
        matcher = feetInch.matcher(text);
        if (matcher.find()) {
            double pied = Double.parseDouble(matcher.group(1));
            pied = unitConverter.convertUnit(new DimensionDTO(pied, Units.FEET), Units.INCH).value();
            return Double.parseDouble(matcher.group(2)) + pied;
        }
        matcher = fraction.matcher(text);
        if (matcher.find()) {
            return Double.parseDouble(matcher.group(1)) / Double.parseDouble(matcher.group(2));
        }
        matcher = feetOnly.matcher(text);
        if (matcher.find()) {
            double pied = Double.parseDouble(matcher.group(1));
            pied = unitConverter.convertUnit(new DimensionDTO(pied, Units.FEET), Units.INCH).value();
            return pied;
        }
        matcher = inchesOnly.matcher(text);
        if (matcher.find()) {
            return Double.parseDouble(matcher.group(1));
        }
        matcher = doubleOnly.matcher(text);
        if (matcher.find()) {
            return Double.parseDouble(matcher.group(1));
        }
        matcher = intOnly.matcher(text);
        if (matcher.find()) {
            return Double.parseDouble(matcher.group(1));
        }

        return Double.NaN;
    }

    public void setMinimum(double minimum) {
        this.min = minimum;
    }

    public void setMaximum(double maximum) {
        this.max = maximum;
    }

}
