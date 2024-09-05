import Parser.ParsedSTL;
import Parser.STLParser;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main(String[] args) {
        String file = "cube.stl";
        DataInputStream dis = null;
        try{
            dis = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
            ParsedSTL parsedSTL = STLParser.parse(dis);
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
    }
}