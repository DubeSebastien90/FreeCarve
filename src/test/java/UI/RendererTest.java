package UI;

import Domain.Box;
import Domain.Mesh;
import Domain.Triangle;
import Domain.Vertex;
import Util.Matrix;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class RendererTest {

    private static ArrayList<Mesh> create_mesh_list_for_test() {
        ArrayList<Mesh> meshes = new ArrayList<>();
//        meshes.add(new Mesh(new Vertex(0, 0, 0), Color.RED) {
//            @Override
//            void setTrianglesList() {
//                this.trianglesList = new ArrayList<>(List.of(new Triangle(new Vertex(0, 0, 0), new Vertex(15, 15, 0), new Vertex(15, 0, 0))
//                        , new Triangle(new Vertex(100, 100, 100), new Vertex(15, 15, 0), new Vertex(200, 0, 0))));
//
//            }
//        });
        meshes.get(0).setTrianglesList();
        return meshes;
    }

//    @Test
//    void rotateWorld_validRotationMatrix_rotatesMeshesCorrectly() {
//        // Arrange
//        Renderer r = new Renderer(create_mesh_list_for_test());
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
//        Triangle firstTriangle = r.getMeshes().getFirst().getTrianglesList().getFirst();
//        Triangle secondTriangle = r.getMeshes().getFirst().getTrianglesList().get(1);
//
//        Assertions.assertEquals(firstTriangle.getVertex1(), new Vertex(0, 0, 0));
//        Assertions.assertEquals(firstTriangle.getVertex2(), new Vertex(15 * cos(0.05), 15, 15 * -sin(0.05)));
//        Assertions.assertEquals(firstTriangle.getVertex3(), new Vertex(15 * cos(0.05), 0, 15 * -sin(0.05)));
//
//        Assertions.assertEquals(secondTriangle.getVertex1(), new Vertex(100 * cos(0.05) + 100 * sin(0.05), 100, 100 * -sin(0.05) + 100 * cos(0.05)));
//        Assertions.assertEquals(secondTriangle.getVertex2(), new Vertex(15 * cos(0.05), 15, 15 * -sin(0.05)));
//        Assertions.assertEquals(secondTriangle.getVertex3(), new Vertex(200 * cos(0.05), 0, 200 * -sin(0.05)));
//    }
//
//    @Test
//    void renderer_translation_mesh_test() {
//        //TODO réparer les tests dans la branche à seb
//        Renderer r = new Renderer(create_mesh_list_for_test());
//        Vertex translationVertex = new Vertex(8, 9, 10);
//        Mesh mesh = r.getMeshes().getFirst();
//        r.translationMesh(mesh, translationVertex);
//
//        r = new Renderer(create_mesh_list_for_test());
//        translationVertex = new Vertex(0, 0, 0);
//        mesh = r.getMeshes().getFirst();
//        r.translationMesh(mesh, translationVertex);
//        Assertions.assertEquals(new Vertex(15, 15, 0), mesh.getTrianglesList().getFirst().getVertex2());
//
//        r = new Renderer(create_mesh_list_for_test());
//        translationVertex = new Vertex(-90, -80, -70);
//        mesh = r.getMeshes().getFirst();
//        r.translationMesh(mesh, translationVertex);
//        Assertions.assertEquals(new Vertex(-75, -80, -70), mesh.getTrianglesList().getFirst().getVertex3());
//    }
//
//    @Test
//    void renderer_rotation_mesh_test() {
//        //TODO réparer les tests dans la branche à seb
//        Mesh mesh = new Box(new Vertex(0, 0, 0), 100, 100, 100, Color.BLUE);
//        Renderer r = new Renderer(new ArrayList<>(List.of(mesh)));
//        r.rotationMesh(r.getMeshes().getFirst(), new Vertex(1, 0, 0), 1);
//        Triangle firstTriangle = r.getMeshes().get(0).getTrianglesList().getFirst();
//        Triangle secondTriangle = r.getMeshes().get(0).getTrianglesList().get(1);
//
//        Assertions.assertEquals(firstTriangle.getVertex2(), new Vertex(0, 0.9987502603949663 * 50 + 0.04997916927067833 * 50 + 50, -0.04997916927067833 * 50 + 0.9987502603949663 * 50 + 50));
//        Assertions.assertEquals(secondTriangle.getVertex3(), new Vertex(0, 0.9987502603949663 * 50 + 0.04997916927067833 * 50 + 50, -0.04997916927067833 * 50 + 0.9987502603949663 * 50 + 50));
//
//    }

}
