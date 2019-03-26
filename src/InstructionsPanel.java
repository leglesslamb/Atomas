/*  Program Name: Atomas
    Name: Robert Wu
    Course: ICS4U1-01
    Teacher: Ms. Strelkovska
    Assignment: Culminating
    Date: 2017/01/17
    Description: Atomas Instructions (gameplay rules in AtomasPanel.java)
*/
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Scanner;

class InstructionsPanel extends JPanel implements ActionListener, MouseListener, MouseMotionListener{
    private Scanner sc;
    private final String atomDescriptions[] = {  "Plus: Fuses two of the same atom",
                                                        "Minus: Extracts any atom",
                                                        "Dark Plus: Fuses any two atoms",
                                                        "Neutrino: Copies any atom"};
    private Timer myTimer;
    private Atom[] atoms = new Atom[4];
    
    public InstructionsPanel(){
        for (int i=0; i<atoms.length; i++){
            atoms[i] = new Atom(-i);
            atoms[i].setX(100);
            atoms[i].setY(240+i*60);
        }
        myTimer = new Timer(10,this);
        myTimer.start();
    }
    public void actionPerformed(ActionEvent e){
        if (e.getSource()==myTimer) repaint();
    }
	public void mouseDragged(MouseEvent e){}
    public void mouseMoved(MouseEvent e){
        if (64>Math.sqrt(Math.pow(e.getX()-150,2)+Math.pow(520-e.getY(),2))) LoadImages.set(4,5);
        else LoadImages.set(4,3);
        if (64>Math.sqrt(Math.pow(e.getX()-300,2)+Math.pow(520-e.getY(),2))) LoadImages.set(1,2);
        else LoadImages.set(1,0);
        if (64>Math.sqrt(Math.pow(e.getX()-450,2)+Math.pow(520-e.getY(),2))) LoadImages.set(10,11);
        else LoadImages.set(10,9);
    }
    public void mousePressed(MouseEvent e){}
    public void mouseReleased(MouseEvent e){}
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
    public void mouseClicked(MouseEvent e){
        if (64>Math.sqrt(Math.pow(e.getX()-150,2)+Math.pow(520-e.getY(),2))) AtomasFrame.switchCard("1");
        else if (64>Math.sqrt(Math.pow(e.getX()-300,2)+Math.pow(520-e.getY(),2))) AtomasFrame.switchCard("0");
        else if (64>Math.sqrt(Math.pow(e.getX()-450,2)+Math.pow(520-e.getY(),2))) AtomasFrame.switchCard("3");
    }
    public void paint(Graphics g){ // paint
        super.paint(g);
        g.setColor(Color.WHITE);

        // draws guide instructions
        
        for (int i=0; i<atoms.length; i++){
            atoms[i].drawAtom(g,25,0);
            g.drawString("- increase score by fusing atoms",115,130);
            g.drawString("- chain fuses give bonus points",115,160);
            g.drawString("- if you exceed 18 atoms, you lose",115,190);
            g.drawString(atomDescriptions[i],140,240+i*60);
        }

        // title
        g.setFont(new Font("MONOSPACED", Font.BOLD, 50));
        g.drawString("Instructions",120,80);

        // 3 buttons
        g.drawImage(LoadImages.get(4),150-64,520-64,null);
        g.drawImage(LoadImages.get(1),300-64,520-64,null);
        g.drawImage(LoadImages.get(10),450-64,520-64,null);
        return;
    }
}