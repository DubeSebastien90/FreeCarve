import UI.*;

public class App {
    public static void main(String[] args) {

        MainWindow mainWindow = new MainWindow();
        mainWindow.start();
//        String file = "teapot.stl";
//        InputStream inputStream = null;
//        ParsedSTL parsedSTL = null;
//        try {
//            inputStream = new BufferedInputStream(new FileInputStream(file));
//            parsedSTL = STLParser.parse(inputStream);
//
//        } catch (FileNotFoundException e) {
//            System.out.println("File not found");
//        } catch (EOFException e) {
//        } catch (IOException e) {
//            System.out.println(Arrays.toString(e.getStackTrace()));
//        } finally {
//            try {
//                if (inputStream != null) {
//                    inputStream.close();
//                }
//            } catch (IOException e) {
//                System.out.println(Arrays.toString(e.getStackTrace()));
//            }
//        }
//        Mesh cubeBleu = new Box(new Vertex(200, 200, 200), 100, 100, 30, Color.BLUE);
//        Mesh cubeRouge = new Box(new Vertex(0, 0, 0), 75, 75, 75, Color.RED);
//        Mesh pyramidVerte = new Pyramid(new Vertex(-110, 110, 110), Color.GREEN);
//        Mesh ground = new Plane(new Vertex(0, 0, 0), 1000, Color.white);
//        frame.add(new Renderer(new ArrayList<Mesh>(List.of(cubeRouge, cubeBleu, pyramidVerte, ground))));
    }
}
