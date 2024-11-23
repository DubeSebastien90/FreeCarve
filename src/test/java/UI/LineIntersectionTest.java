package UI;

import Common.DTO.VertexDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class LineIntersectionTest {

    MainWindow mainWindow;

    @BeforeEach
    void SetUp(){
        mainWindow = new MainWindow();
        mainWindow.start();
    }

    @Test
    void line_intersect_board(){
        // Arrange
        VertexDTO p1 = new VertexDTO(0, 50, 0);
        VertexDTO cursor = new VertexDTO(80, 50, 0);
        VertexDTO cursor2 = new VertexDTO(90, 50, 0);
        VertexDTO cursor3 = new VertexDTO(95, 50, 0);
        VertexDTO cursor4 = new VertexDTO(100, 50, 0);
        VertexDTO cursor5 = new VertexDTO(105, 50, 0);
        VertexDTO cursor6 = new VertexDTO(110, 50, 0);
        VertexDTO cursor7 = new VertexDTO(115, 50, 0);
        VertexDTO cursor8 = new VertexDTO(120, 50, 0);

        double threshold = 10;
        threshold =  mainWindow.getMiddleContent().getCutWindow().getRenderer().scaleMMToPixel(threshold);

        // Act
        mainWindow.getController().resizePanel(100, 100);
        Optional<VertexDTO> closestPoint = mainWindow.getController().getGridLineNearAllBorderAndCuts(p1, cursor, threshold);
        Optional<VertexDTO> closestPoint2 = mainWindow.getController().getGridLineNearAllBorderAndCuts(p1, cursor2, threshold);
        Optional<VertexDTO> closestPoint3 = mainWindow.getController().getGridLineNearAllBorderAndCuts(p1, cursor3, threshold);
        Optional<VertexDTO> closestPoint4 = mainWindow.getController().getGridLineNearAllBorderAndCuts(p1, cursor4, threshold);
        Optional<VertexDTO> closestPoint5 = mainWindow.getController().getGridLineNearAllBorderAndCuts(p1, cursor5, threshold);
        Optional<VertexDTO> closestPoint6 = mainWindow.getController().getGridLineNearAllBorderAndCuts(p1, cursor6, threshold);
        Optional<VertexDTO> closestPoint7 = mainWindow.getController().getGridLineNearAllBorderAndCuts(p1, cursor7, threshold);
        Optional<VertexDTO> closestPoint8 = mainWindow.getController().getGridLineNearAllBorderAndCuts(p1, cursor8, threshold);

        // Assert
        VertexDTO dimensions = mainWindow.getController().getPanelDTO().getPanelDimension();

        Assertions.assertEquals(100, dimensions.getX());
        Assertions.assertEquals(100, dimensions.getY());
        Assertions.assertTrue(closestPoint.isEmpty());

        Assertions.assertTrue(closestPoint2.isPresent());
        Assertions.assertEquals(100, closestPoint2.get().getX());
        Assertions.assertEquals(50, closestPoint2.get().getY());
        Assertions.assertEquals(0, closestPoint2.get().getZ());

        Assertions.assertTrue(closestPoint3.isPresent());
        Assertions.assertEquals(100, closestPoint3.get().getX());
        Assertions.assertEquals(50, closestPoint3.get().getY());
        Assertions.assertEquals(0, closestPoint3.get().getZ());

        Assertions.assertTrue(closestPoint4.isPresent());
        Assertions.assertEquals(100, closestPoint4.get().getX());
        Assertions.assertEquals(50, closestPoint4.get().getY());
        Assertions.assertEquals(0, closestPoint4.get().getZ());

        Assertions.assertTrue(closestPoint5.isPresent());
        Assertions.assertEquals(100, closestPoint5.get().getX());
        Assertions.assertEquals(50, closestPoint5.get().getY());
        Assertions.assertEquals(0, closestPoint5.get().getZ());

        Assertions.assertTrue(closestPoint6.isPresent());
        Assertions.assertEquals(100, closestPoint6.get().getX());
        Assertions.assertEquals(50, closestPoint6.get().getY());
        Assertions.assertEquals(0, closestPoint6.get().getZ());

        Assertions.assertTrue(closestPoint7.isEmpty());

        Assertions.assertTrue(closestPoint8.isEmpty());

    }
}
