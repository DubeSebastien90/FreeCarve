import Parser.ParsedSTL;
import Parser.STLParser;
import Screen.*;
import Screen.Box;
import Screen.Renderer;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class App {
    public static void main(String[] args) {
        String file = "teapot.stl";

        JFrame frame = new JFrame("Espresso");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 750);
        frame.setMinimumSize(new Dimension(400, 300));
        frame.setLocationRelativeTo(null);
        frame.setJMenuBar(new MainMenu());

        InputStream inputStream = null;
        ParsedSTL parsedSTL = null;

        try{
            inputStream = new BufferedInputStream(new FileInputStream(file));
            parsedSTL = STLParser.parse(inputStream);
        } catch (FileNotFoundException e){
            System.out.println("File not found");
        } catch (EOFException e) {
        } catch (IOException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                System.out.println(Arrays.toString(e.getStackTrace()));
            }
        }

        Mesh cubeBleu = new Box(new Vertex(200, 200, 200), 100, 100, 30, Color.BLUE);
        Mesh cubeRouge = new Box(new Vertex(0, 0, 0), 75, 75, 75, Color.RED);
        Mesh pyramidVerte = new Pyramid(new Vertex(-110, 110, 110), Color.GREEN);
        Mesh ground = new Plane(new Vertex(0, 0, 0), 1000, Color.white);

        //cubeBleu.setTriangles(new ArrayList<Triangle>(List.of(Triangle.fromParsedSTL(parsedSTL, Color.RED))));

        frame.add(new Renderer(new ArrayList<Mesh>(List.of(cubeRouge, cubeBleu, pyramidVerte, ground))));
        frame.setVisible(true);
    }
}
