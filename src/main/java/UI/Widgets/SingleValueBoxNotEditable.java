package UI.Widgets;

import UI.MainWindow;
import UI.UiUnits;

public class SingleValueBoxNotEditable extends SingleValueBox{

    public SingleValueBoxNotEditable(MainWindow mainWindow, boolean haveBackground, String name, String inputName, double value) {
        super( mainWindow, haveBackground, name, inputName, value);
        getInput().getNumericInput().setEditable(false);
        setNotModifiable();
    }
}
