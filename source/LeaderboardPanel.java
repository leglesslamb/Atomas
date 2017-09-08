/*  Program Name: Atomas
    Name: Robert Wu
    Course: ICS4U1-01
    Teacher: Ms. Strelkovska
    Assignment: Culminating
    Date: 2017/01/17
    Description: Atomas Leaderboard For Storing Data of Top Players (gameplay rules in AtomasPanel.java)
*/
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

class LeaderboardPanel extends JPanel implements ActionListener, MouseListener, MouseMotionListener{
    private Scanner sc;
    private Timer myTimer;
    private String line[];
    private static ArrayList<String> playerNames; // static because it's used in a static method
    private static ArrayList<Integer> playerScores;

    public LeaderboardPanel(){
        playerNames = new ArrayList<String>();
        playerScores = new ArrayList<Integer>();
        try{ // try reading file of highscores
            sc = new Scanner(new FileReader("HighScores.txt"));
            for (int i=0; i<10; i++){ // top 10
                line = sc.nextLine().split(" ");
                playerNames.add(line[0].trim()); // name
                playerScores.add(Integer.parseInt(line[1])); // score
            }
            sc.close();
        }
        catch(Exception e){ // errors
            e.printStackTrace();
            System.out.println("Either HighScores.txt is missing or corrupted.");
        }
        myTimer = new Timer(10,this);
        myTimer.start();
    }
    public void actionPerformed(ActionEvent e){if (e.getSource()==myTimer) repaint(); }
    public void mouseDragged(MouseEvent e){}
    public void mouseMoved(MouseEvent e){ // if mouse overs over a button
        if (64>Math.sqrt(Math.pow(e.getX()-150,2)+Math.pow(520-e.getY(),2))) LoadImages.set(4,5);
        else LoadImages.set(4,3);
        if (64>Math.sqrt(Math.pow(e.getX()-300,2)+Math.pow(520-e.getY(),2))) LoadImages.set(1,2);
        else LoadImages.set(1,0);
        if (64>Math.sqrt(Math.pow(e.getX()-450,2)+Math.pow(520-e.getY(),2))) LoadImages.set(7,8);
        else LoadImages.set(7,6);
    }
    public void mousePressed(MouseEvent e){}
    public void mouseReleased(MouseEvent e){}
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
    public void mouseClicked(MouseEvent e){ // checks if a button is clicked and switches cards accordingly
        if (64>Math.sqrt(Math.pow(e.getX()-150,2)+Math.pow(520-e.getY(),2))) AtomasFrame.switchCard("1");
        else if (64>Math.sqrt(Math.pow(e.getX()-300,2)+Math.pow(520-e.getY(),2))) AtomasFrame.switchCard("0");
        else if (64>Math.sqrt(Math.pow(e.getX()-450,2)+Math.pow(520-e.getY(),2))) AtomasFrame.switchCard("2");
    }

    public static boolean checkScore(String name, int score){ // static because AtomasPanel does not have a instance of LeaderboardPanel
        for (int i=0; i<10; i++){ // if score exceeds top 10
            if (score>=playerScores.get(i)){
                playerNames.add(i,name);
                playerScores.add(i,score);
                try{ // writes new scores to file
                    PrintWriter pw = new PrintWriter("HighScores.txt");
                    pw.close();
                    FileWriter fstream = new FileWriter("HighScores.txt",true);
                    BufferedWriter wr = new BufferedWriter(fstream);
                    for (int j=0; j<10; j++){
                        wr.write(playerNames.get(j)+" "+playerScores.get(j));
                        wr.newLine();
                    }
                    wr.close();
                }
                catch(Exception e){ e.printStackTrace();}
                break;
            }
        }
        return true;
    }
    public void paint(Graphics g){ // paint
        super.paint(g);

        //3 buttons
        g.drawImage(LoadImages.get(4),150-64,520-64,null);
        g.drawImage(LoadImages.get(1),300-64,520-64,null);
        g.drawImage(LoadImages.get(7),450-64,520-64,null);

        // title
        g.setFont(new Font("MONOSPACED", Font.BOLD, 50));
        g.setColor(Color.WHITE);
        g.drawString("Leaderboard",125,80);
        g.setFont(new Font("MONOSPACED", Font.BOLD, 25));

        for (int i=0; i<10; i++){ // prints all scores
            g.drawString(""+(i+1),100,150+25*i);
            g.drawString(playerNames.get(i),150,150+25*i);
            g.drawString(""+playerScores.get(i),350,150+25*i);
        }
        return;
    }
}