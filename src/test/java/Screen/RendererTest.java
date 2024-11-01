package Screen;

import Domain.ThirdDimension.Mesh;
import Domain.ThirdDimension.Triangle;
import Domain.ThirdDimension.Vertex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class RendererTest {

//    @Test
//    void rotateWorld_validRotationMatrix_rotatesMeshesCorrectly() {
//        // Arrange
//        Renderer r = new Renderer(TestHelper.createMeshList());
//        Matrix m1 = new Matrix(new double[]{
//                cos(0.05), 0, -sin(0.05),
//                0, 1, 0,
//                sin(0.05), 0, cos(0.05)
//        });
//
//        // Act
//        r.rotateWorld(m1);
//
//        // Assert
//        Triangle firstTriangle = r.getMeshes().getFirst().getLocalTriangles().getFirst();
//        Triangle secondTriangle = r.getMeshes().getFirst().getLocalTriangles().get(1);
//
//        Assertions.assertEquals(firstTriangle.getVertex(0), new Vertex(0, 0, 0));
//        Assertions.assertEquals(firstTriangle.getVertex(1), new Vertex(15 * cos(0.05), 15, 15 * -sin(0.05)));
//        Assertions.assertEquals(firstTriangle.getVertex(2), new Vertex(15 * cos(0.05), 0, 15 * -sin(0.05)));
//
//        Assertions.assertEquals(secondTriangle.getVertex(0), new Vertex(100 * cos(0.05) + 100 * sin(0.05), 100, 100 * -sin(0.05) + 100 * cos(0.05)));
//        Assertions.assertEquals(secondTriangle.getVertex(1), new Vertex(15 * cos(0.05), 15, 15 * -sin(0.05)));
//        Assertions.assertEquals(secondTriangle.getVertex(2), new Vertex(200 * cos(0.05), 0, 200 * -sin(0.05)));
//    }

}
