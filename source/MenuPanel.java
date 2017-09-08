/*  Program Name: Atomas
    Name: Robert Wu
    Course: ICS4U1-01
    Teacher: Ms. Strelkovska
    Assignment: Culminating
    Date: 2017/01/17
    Description: Atomas Menu Panel
*/
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class MenuPanel extends JPanel implements ActionListener, MouseListener, MouseMotionListener{
    private Timer myTimer;
    private int pulse=0;
    private Atom menuAtom;
    
    public MenuPanel(){ // constructor
        menuAtom = new Atom(1); // big clickable atom
        menuAtom.setX(300);
        menuAtom.setY(250);
        myTimer = new Timer(10,this);
        myTimer.start();
    }
    public void actionPerformed(ActionEvent e){
        pulse = (++pulse)%120;
        repaint();
    }
	public void mouseDragged(MouseEvent e){}
    public void mouseMoved(MouseEvent e){ // if mouse hovers over buttons
        if (64>Math.sqrt(Math.pow(e.getX()-150,2)+Math.pow(520-e.getY(),2))) LoadImages.set(4,5);
    	else LoadImages.set(4,3);
        if (64>Math.sqrt(Math.pow(e.getX()-300,2)+Math.pow(520-e.getY(),2))) LoadImages.set(7,8);
        else LoadImages.set(7,6);
        if (64>Math.sqrt(Math.pow(e.getX()-450,2)+Math.pow(520-e.getY(),2))) LoadImages.set(10,11);
        else LoadImages.set(10,9);
    }
    public void mousePressed(MouseEvent e){}
    public void mouseReleased(MouseEvent e){}
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
    public void mouseClicked(MouseEvent e){ // if mouse clicks atom or button
        if (100>Math.sqrt(Math.pow(e.getX()-menuAtom.getX(),2)+Math.pow(menuAtom.getY()-e.getY(),2))) menuAtom.setN((menuAtom.getN()-1)%118+2);
        if (64>Math.sqrt(Math.pow(e.getX()-150,2)+Math.pow(520-e.getY(),2))) AtomasFrame.switchCard("1");
        else if (64>Math.sqrt(Math.pow(e.getX()-300,2)+Math.pow(520-e.getY(),2))) AtomasFrame.switchCard("2");
        else if (64>Math.sqrt(Math.pow(e.getX()-450,2)+Math.pow(520-e.getY(),2))) AtomasFrame.switchCard("3");
    }
    public void paint(Graphics g){ // paint
        super.paint(g);

        // 3 buttons
        g.drawImage(LoadImages.get(4),150-64,520-64,null);
        g.drawImage(LoadImages.get(7),300-64,520-64,null);
        g.drawImage(LoadImages.get(10),450-64,520-64,null);

        g.setColor(Color.WHITE);
        menuAtom.drawAtom(g,100,pulse); // big atom

        //title
        g.setFont(new Font("MONOSPACED", Font.BOLD, 50));
        g.drawString("Atomas: PC",150,80);
        return;
    }
}