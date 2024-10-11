package UI;

import Annotations.VariableSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.stream.Stream;

public class LeftBarTest {

    public static Stream<Arguments> enableTool_toolExist = Stream.of(
            Arguments.of(LeftBar.ToolBar.Tool.SAVE),
            Arguments.of(LeftBar.ToolBar.Tool.SETTING));


//    @ParameterizedTest
//    @VariableSource("enableTool_toolExist")
//    void enableTool_toolExist_enableIt(LeftBar.ToolBar.Tool tool) {
//        MainWindow mainWindow = new MainWindow();
//        LeftBar bar = mainWindow.getLeftBar();
//        LeftBar.ToolBar toolbar = bar.getToolBar();
//        toolbar.enableTool(tool);
//        Assertions.assertTrue(toolbar.getTool(tool).isEnabled());
//    }

    public static Stream<Arguments> disableTool_toolExist = Stream.of(
            Arguments.of(LeftBar.ToolBar.Tool.SAVE),
            Arguments.of(LeftBar.ToolBar.Tool.SETTING));

//    @ParameterizedTest
//    @VariableSource("disableTool_toolExist")
//    void disableTool_toolExist_disableIt(LeftBar.ToolBar.Tool tool) {
//        MainWindow mainWindow = new MainWindow();
//        LeftBar bar = mainWindow.getLeftBar();
//        LeftBar.ToolBar toolbar = bar.getToolBar();
//        toolbar.disableTool(tool);
//        Assertions.assertFalse(toolbar.getTool(tool).isEnabled());
//    }
}
