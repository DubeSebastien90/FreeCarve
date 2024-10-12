package UI.Widgets;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * The {@code ImageWidget} class is a simple UI widget class that encapsulates the functionality to draw an a simple
 * static image in a {@code JPanel} with desired width and height
 *
 * @author Louis-Etienne Messier
 * @version 0.1
 * @since 2024-10-12
 */
class ImageWidget extends JPanel {
    private BufferedImage image;

    /**
     * Main public constructor of the ImageWidget, create the image and catch the error if the path is invalid to draw
     * a magenta texture
     * @param imagePath path of the image
     * @param width width of the image
     * @param height height of the image
     */
    public ImageWidget(String imagePath, int width, int height){
        this.setSize(new Dimension(width, height));

        // Loads the image and catch the error if problem
        try{
            image = ImageIO.read(new File(imagePath));
        }catch(IOException ex){
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            for(int x =0; x < width; x++){
                for(int y=0; y<height; y++){
                    image.setRGB(x,y, Color.magenta.getRGB());
                }
            }
        }
    }


    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        if(image != null){
            g.drawImage(image, 0, 0, this);
        }
    }
}
