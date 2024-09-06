import Parser.ParsedSTL;
import Parser.STLParser;
import Screen.MainMenu;
import Screen.TheDessinator;
import Screen.Triangle;
import Screen.Vertex;

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

        DataInputStream dis = null;
        ParsedSTL parsedSTL = null;
        try{
            dis = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
            parsedSTL = STLParser.parse(dis);
        } catch (FileNotFoundException e){
            System.out.println("File not found");
        } catch (EOFException e) {
        } catch (IOException e){
            System.out.println(Arrays.toString(e.getStackTrace()));
        } finally {
            try {
                if (dis != null) {
                    dis.close();
                }
            } catch (IOException e){
                System.out.println(Arrays.toString(e.getStackTrace()));
            }
        }

        frame.add(new TheDessinator(new ArrayList<Triangle>(List.of(Triangle.fromParsedSTL(parsedSTL, Color.RED)))));
        frame.setVisible(true);
    }
}
