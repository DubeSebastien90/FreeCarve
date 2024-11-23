package UI.Widgets;

public class SingleValueBoxNotEditable extends SingleValueBox{

    public SingleValueBoxNotEditable(boolean haveBackground, String name, String inputName, double value) {
        super(haveBackground, name, inputName, value);
        getInput().getNumericInput().setEditable(false);
        setNotModifiable();
    }
}
