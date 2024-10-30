package Screen;

import Parser.STLParser;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class TestHelper {
    public static List<Mesh> createMeshList() {
        ArrayList<Mesh> meshes = new ArrayList<>();
        Mesh mesh = createMesh();
        meshes.add(mesh);
        return meshes;
    }

    public static Mesh createMesh() {
        return new Mesh(new Vertex(0, 0, 0), 1, Color.RED, List.of(
                new Triangle(new Vertex(0, 0, 0), new Vertex(15, 15, 0), new Vertex(15, 0, 0)),
                new Triangle(new Vertex(100, 100, 100), new Vertex(15, 15, 0), new Vertex(200, 0, 0))));
    }

    public static Mesh createMeshFromFile(String localPath) throws IOException {
        String cubeFile = Thread.currentThread().getContextClassLoader().getResource(localPath).getPath();
        InputStream inputStream = new BufferedInputStream(new FileInputStream(cubeFile));
        return new Mesh(Vertex.zero(), 1, Color.RED, Arrays.stream(Triangle.fromParsedSTL(STLParser.parse(inputStream), Color.RED)).toList());
    }
}
