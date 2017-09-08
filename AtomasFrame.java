/*  Program Name: Atomas
    Name: Robert Wu
    Course: ICS4U1-01
    Teacher: Ms. Strelkovska
    Assignment: Culminating
    Date: 2017/01/17
    Description: Atomas Main Frame (gameplay rules in AtomasPanel.java)
*/
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class AtomasFrame extends JFrame implements KeyListener{

    // variables
    private Container c;
    private MenuPanel menu = new MenuPanel();
    private AtomasPanel atomas = new AtomasPanel();
    private InstructionsPanel instructions = new InstructionsPanel();
    private LeaderboardPanel leaderboard = new LeaderboardPanel();
    private LoadImages loadImages = new LoadImages();
    private static AtomasFrame aF;
    private static JPanel cards = new JPanel(new CardLayout()); // referred to in static method
    private static CardLayout cl = (CardLayout) cards.getLayout(); // referred to in static method

    public static void switchCard(String s){cl.show(cards,s);} // method for changing panels; static because called by class in other classes

    public AtomasFrame(){
        super("Atomas: PC");
        cl = (CardLayout) cards.getLayout();
        addKeyListener(this);

        // panels for cards
        menu.setBackground(Color.decode("#471D29"));
        menu.addMouseListener(menu);
        menu.addMouseMotionListener(menu);
        menu.setSize(600, 600);
        atomas.setBackground(Color.decode("#471D29"));
        atomas.addMouseListener(atomas);
        atomas.addMouseMotionListener(atomas);
        atomas.setSize(600, 600);
        instructions.setBackground(Color.decode("#471D29"));
        instructions.addMouseListener(instructions);
        instructions.addMouseMotionListener(instructions);
        instructions.setSize(600, 600);
        leaderboard.setBackground(Color.decode("#471D29"));
    	  leaderboard.addMouseListener(leaderboard);
        leaderboard.addMouseMotionListener(leaderboard);
        leaderboard.setSize(600, 600);

        // add all panels to cards
        cards.add(menu,"0");
        cards.add(atomas,"1");
        cards.add(instructions,"2");
        cards.add(leaderboard,"3");
        c = getContentPane();
        c.add(cards);
    }
    public void keyPressed(KeyEvent e){ // if space or escape is pressed
        atomas.keyboardTyped(e.getKeyCode());
    }
    public void keyReleased(KeyEvent e){}
    public void keyTyped(KeyEvent e){}
    public static void main (String[] args){ // main
        aF = new AtomasFrame();
        aF.setSize(600, 630);
        aF.setVisible(true);
        aF.setResizable(false);
        aF.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
    }
}
