/*  Program Name: Atomas
    Name: Robert Wu
    Course: ICS4U1-01
    Teacher: Ms. Strelkovska
    Assignment: Culminating
    Date: 2017/01/17
    Description: Atomas Images (gameplay rules in AtomasPanel.java)
*/
import java.awt.image.BufferedImage;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;

class LoadImages{
    private static BufferedImage[] images = new BufferedImage[18]; // referred to by class in AtomasFrame
    public static BufferedImage get(int i){return images[i];} // referred to by class in AtomasFrame
    public static void set(int a, int b){images[a] = images[b];} // referred to by class in AtomasFrame
    public LoadImages(){
        try{ // loads all images
            images[0] = ImageIO.read(new File("menuwhite.png"));
            images[1] = images[0];
            images[2] = ImageIO.read(new File("menugreen.png"));
            images[3] = ImageIO.read(new File("playwhite.png"));
            images[4] = images[3];
            images[5] = ImageIO.read(new File("playgreen.png"));
            images[6] = ImageIO.read(new File("instructionswhite.png"));
            images[7] = images[6];
            images[8] = ImageIO.read(new File("instructionsgreen.png"));
            images[9] = ImageIO.read(new File("trophywhite.png"));
            images[10] = images[9];
            images[11] = ImageIO.read(new File("trophygreen.png"));
            images[12] = ImageIO.read(new File("resetwhite.png"));
            images[13] = images[12];
            images[14] = ImageIO.read(new File("resetgreen.png"));
            images[15] = ImageIO.read(new File("savewhite.png"));
            images[16] = images[15];
            images[17] = ImageIO.read(new File("savegreen.png"));

        }
        catch(Exception e){ // if something goes wrong
            for (int i=0; i<6; i++) images[i*3+1] = null;
            e.printStackTrace();
            System.out.println("One or more of your files is missing is missing or corrupted.");
        }
    }
}