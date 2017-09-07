/*  Program Name: Atomas
    Name: Robert Wu
    Course: ICS4U1-01
    Teacher: Ms. Strelkovska
    Assignment: Culminating
    Date: 2017/01/17
    Description: Atomas Panel (game)
    Rules for Gameplay:
    	1: use pluses or dark pluses to fuse atoms
    	2: symmetrical arrangements (chain fuses) get fused together for bonus points
    	3: minus atoms extract atoms to the centre; click on extracted atoms ("fresh" atoms to change them to plus)
    	4: neutrino atoms copy atoms from the circle
    	5: if the player reaches 19 atoms with no more fuses possible, they lose
    	6: a plus spawns at least every six atoms
    	7: a minus spawns at least every 36 atoms
    	8: 0.1% for neutrino, 0.4% for dark plus, 1% for minus, 10% for plus, always
    	9: primary goal: increase score; secondary goal: increase atomic number
*/
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.util.Scanner;
import javax.imageio.*;
import java.io.*;

class AtomasPanel extends JPanel implements ActionListener, MouseListener, MouseMotionListener{
    private Scanner sc = new Scanner(System.in);
    private String name;
    private Timer myTimer;
    private int score, pulse=0, newAN=-999, plusCounter=0, minusCounter=0, loseCount;
    private ArrayList<Atom> atoms;
    private Atom nextAtom, beforeAtom, afterAtom;
    private boolean lose=false, scoreChecked=false;
    public AtomasPanel(){
        reset();
        Atom.loadData();
        myTimer = new Timer(10,this);
        myTimer.start();
    }
    public void actionPerformed(ActionEvent e){
        pulse = (++pulse)%120; // for orbiting particles
        if (e.getSource()==myTimer){
            if (lose && Atom.getTravelCount()==0){ // when player has lost and animation has stopped
                if (loseCount<240) loseCount+=3; // animation over overlay
                repaint();
                return;
            }
            for (int i=0; i<atoms.size(); i++) atoms.get(i).travel(i); // updates animation
            beforeAtom.travel(true);
            afterAtom.travel(true);
            if (Atom.getTravelCount()==0){
                for (int i=atoms.size()-1; i>=0; i--){
                    if (atoms.get(i).getCollapse()){ // if atom has fusing capabilities
                    	int before = (i+atoms.size() -1)%(atoms.size());
                    	int after = (i+atoms.size() +1)%(atoms.size());
                    	if (atoms.get(i).getFuse()){ // if atom is set to fuse
                    		if (newAN!=-999){ // new score has been chosen
                    			atoms.get(i).setN(newAN);
                            	score += newAN;
                            	Atom.setMin(Math.max(1,(int)(Math.log(score)/Math.log(10))));
        						Atom.setMax(Math.max(3,2+(int)(Math.log(score)/Math.log(4))));
                            	newAN = -999; // bogus score
                           	}
                        }
                        if ((getNewAN(before,i,after)>0)&& atoms.size()>2){ // if there are at least three atoms and 3 of them can fuse
                        	atoms.get(i).setFuse(true);
                            newAN = getNewAN(before,i,after);
                            for (int j=0; j<atoms.size(); j++) atoms.get(j).lapAtom();
                            beforeAtom = atoms.get(before);
                            beforeAtom.lapAtom();
                            afterAtom = atoms.get(after);
                            afterAtom.lapAtom();
                            if (after>before) atoms.remove(after);
                            atoms.remove(before);
                            if (before>=after) atoms.remove(after);
                            Atom.setAngleSpace((2*Math.PI)/atoms.size());
                            atoms.get(0).travel(0);
                            Atom.setTravelCount(45);
                            for (int j=0; j<atoms.size(); j++) atoms.get(j).lapAtom();
                            for (int j=0; j<atoms.size(); j++) atoms.get(j).travel(j);

                            // adjusts index
                            if (before<i && i>after) i = (i-2+atoms.size())%atoms.size();
                        	else if (before<i && i<after) i = (i-1+atoms.size())%atoms.size();

                        	// dead atoms for animation
                            beforeAtom.setA(atoms.get((i+atoms.size())%atoms.size()).getA());
                            afterAtom.setA(atoms.get((i+atoms.size())%atoms.size()).getA());
                            beforeAtom.travel(true);
                            afterAtom.travel(true);

                            // adjusts index
                            i-=1;
                        }
                        else if (atoms.get(i).getN()!=0 && atoms.get(i).getN()!=-2){ // if atom is not plus or dark plus, set not collapse or fuse
                        	atoms.get(i).setCollapse(false);
                        	atoms.get(i).setFuse(false);
                        }
                    }
                }
            }
            repaint();
        }
        repaint();
    }
    public void mouseDragged(MouseEvent e){}
    public void mouseMoved(MouseEvent e){ // hover over buttons
        if (64>Math.sqrt(Math.pow(e.getX()-520,2)+Math.pow(520-e.getY(),2))) LoadImages.set(13,14);
        else LoadImages.set(13,12);
        if (64>Math.sqrt(Math.pow(e.getX()-75,2)+Math.pow(520-e.getY(),2))) LoadImages.set(1,2);
        else LoadImages.set(1,0);
        if (64>Math.sqrt(Math.pow(e.getX()-520,2)+Math.pow(75-e.getY(),2))) LoadImages.set(7,8);
        else LoadImages.set(7,6);
        if (64>Math.sqrt(Math.pow(e.getX()-300,2)+Math.pow(450-e.getY(),2))) LoadImages.set(16,17);
        else LoadImages.set(16,15);
        repaint();
    }
    public void mousePressed(MouseEvent e){}
    public void mouseReleased(MouseEvent e){}
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
    public void mouseClicked(MouseEvent e){

    	// 4 buttons
        if (64>Math.sqrt(Math.pow(e.getX()-520,2)+Math.pow(520-e.getY(),2))) reset();
        if (64>Math.sqrt(Math.pow(e.getX()-75,2)+Math.pow(520-e.getY(),2))) AtomasFrame.switchCard("0");
        if (64>Math.sqrt(Math.pow(e.getX()-520,2)+Math.pow(75-e.getY(),2))) AtomasFrame.switchCard("2");
        if (64>Math.sqrt(Math.pow(e.getX()-300,2)+Math.pow(450-e.getY(),2)) && lose && !scoreChecked) scoreChecked = LeaderboardPanel.checkScore(name,score);

        // if clicked outside circle, return
        if (225<Math.sqrt(Math.pow(e.getX()-300,2)+Math.pow(300-e.getY(),2)) || Atom.getTravelCount()!=0 || lose) return;

        // if fresh atom is clicked, change it to a plus, return
        if (25>Math.sqrt(Math.pow(e.getX()-300,2)+Math.pow(300-e.getY(),2)) && nextAtom.getFresh() && nextAtom.getN()>0){ nextAtom = new Atom(0); return;}
        beforeAtom.travel(false);
        afterAtom.travel(false);
        double angle = ((Math.atan2(300-e.getY(),e.getX()-300)+2*Math.PI)%(2*Math.PI));
        int newIndex = -1;
        if (nextAtom.getN()==-1 || nextAtom.getN()==-3){ // minus or neutrino
        	for (int i=0; i<atoms.size(); i++){ // checks if any circle atom is clicked
                if (50>Math.sqrt(Math.pow(e.getX()-atoms.get(i).getX(),2)+Math.pow(atoms.get(i).getY()-e.getY(),2))) newIndex = i;
            }
            if (newIndex==-1) return; // if not updated, return
            if (nextAtom.getN()==-3){ // if neutrino
            	nextAtom.setN(atoms.get(newIndex).getN());
            	return;
            }
            nextAtom = new Atom(atoms.get(newIndex).getN());
            nextAtom.lapAtom(); // sets current position of next atom
            nextAtom.setX(300);
            nextAtom.setY(300);
            nextAtom.setFresh(true);
            Atom.setStartAngle(atoms.get(newIndex).getA());
            for (int i=0; i<atoms.size(); i++) atoms.get(i).lapAtom();
            Atom.setStartAngle(angle);
            atoms.remove(newIndex);
            reorder(newIndex);
            for (int i=0; i<atoms.size(); i++) atoms.get(i).travel(i);
            beforeAtom.travel(false);
            afterAtom.travel(false);
            Atom.setAngleSpace((2*Math.PI)/atoms.size());
        }
        else{ // if it's a plus, dark plus, or regular atom
        	newIndex = 1+(int)( (angle-Atom.getStartAngle()+2*Math.PI)%(2*Math.PI)/Atom.getAngleSpace() );
            nextAtom.setA(angle);
            nextAtom.setOA(angle);
            nextAtom.setIsNew(true);
            nextAtom.setFresh(false);
            atoms.add(newIndex,nextAtom);
            Atom.setAngleSpace((2*Math.PI)/atoms.size());
            for (int i=0; i<atoms.size(); i++) atoms.get(i).lapAtom();
            Atom.setStartAngle(angle);
            reorder(newIndex); // sets newly placed atom as origin
            Atom.setTravelCount(30);
            for (int i=0; i<atoms.size(); i++) atoms.get(i).travel(i);
            beforeAtom.travel(false); // animation for dead atoms
            afterAtom.travel(false);
            nextAtom = new Atom(getNewAN());
        }
        if (atoms.size()>18){ // when there's 19 atoms, check if collapses can still happen
			boolean moreCollapse = false;
			int before, after;
			for (int i=0; i<atoms.size(); i++){
				before = (i+atoms.size() -1)%(atoms.size());
				after = (i+atoms.size() +1)%(atoms.size());
				if ((getNewAN(before,i,after)>0 && atoms.get(i).getN()==0) || atoms.get(i).getN()==-2){
					moreCollapse = true;
					break;
				}
			}
			if (!moreCollapse) lose = true; // if no collapse can be made, player has lost
		}
        repaint();
    }
    public int getNewAN(){ // returns atomic number for new atom for next atom
        plusCounter = (++plusCounter)%6; // to ensure there is always a plus at least every 6 atoms
        minusCounter = (++minusCounter)%36; // to ensure there is always a minus at least every 36 atoms
        if (minusCounter==35) return -1;
        if (plusCounter==5) return 0;
        int newN = (int)(Atom.getMin()+Math.random()*(1+Atom.getMax()-Atom.getMin()));
        int randomN = (int)(Math.random()*1000);
        if (randomN<1) return -3; // 1/1000 chance for neutrino
        else if (randomN<5) return -2; // 4/1000 chance for dark plus
        else if (randomN<10){ // 1/100 chance for minus
        	minusCounter = 0;
        	return -1;
        }
        else if (randomN<100){ // 1/10 chance for plus
            plusCounter = 0;
            return 0;
        }
        return newN;
    }
    public int getNewAN(int before,int index,int after){ // returns atomic number for new atom for created atom
        if ((atoms.get(before).getN()!=atoms.get(after).getN() && atoms.get(index).getN()!=-2) || after==before) return -9; // returns bogus atomic number when no fuse should occur
        int newN = Math.max(atoms.get(index).getN(),Math.max(atoms.get(before).getN(),atoms.get(after).getN())); // determines new atomic number
        if (atoms.get(index).getN()==0) newN++; // normal plus
        else if (atoms.get(index).getN()==-2) newN+=3; // if dark plus
        else if (atoms.get(index).getN()>0 && atoms.get(index).getN()<=atoms.get(after).getN()) newN+=2; // if not plus and collapse atom is less than what it's in between
        else newN++;
        return newN;
    }
    public void reset(){ // resets game with default values
    	scoreChecked=false;
    	loseCount = 0;
        score = 0;
        plusCounter = 0;
        minusCounter = 0;
        lose = false;
        name = "";
        Atom.setMin(1);
        Atom.setMax(3);
        Atom.setStartAngle(0);
        atoms = new ArrayList<Atom>();
        // for (int i=0; i<6; i++) atoms.add(new Atom(i+1)); // debugging line; replace with one below
        for (int i=0; i<6; i++) atoms.add(new Atom((int)(Atom.getMin()+Math.random()*Atom.getMax())));
        Atom.setAngleSpace((2*Math.PI)/atoms.size());
        for (int i=0; i<atoms.size(); i++) atoms.get(i).travel(i);
        nextAtom = new Atom((int)(Atom.getMin()+Math.random()*Atom.getMax()));
        beforeAtom = new Atom(0);
        afterAtom = new Atom(0);
        repaint();
    }
    public void reorder(int n){ // recursion
    	if (n==0){
    		Atom.setAngleSpace((2*Math.PI)/atoms.size());
    		Atom.setTravelCount(30);
    		return;
    	}
    	atoms.add(atoms.get(0)); // move first element to the end
    	atoms.remove(0);
    	reorder(n-1); // recursive call
    }
    public void keyboardTyped(int n){ // whenever the player presses a key
    	if (n==27) System.exit(0);
    	if (n>47 && n<52){// cheats
    		if (n==48 || n==50) nextAtom.setCollapse(true);
    		nextAtom.setN(-n+48);
    	}
    	if (!lose) return; // ensures player has lost
        if (n==8 && name.length()>0) name = name.substring(0,name.length()-1);
        else if ((n>64 && n<91 || n==46) && name.length()<12) name += (char)n;
    }
    public void paint(Graphics g){ // paint
        super.paint(g);
        g.drawImage(LoadImages.get(1),75-64,520-64,null); // 3 buttons
        g.drawImage(LoadImages.get(13),520-64,520-64,null);
        g.drawImage(LoadImages.get(7),520-64,75-64,null);

        g.setColor(Color.WHITE);
		if (atoms.size()==16){ // warning counter
			g.setColor(Color.GREEN);
			for (int i=0; i<3; i++) g.fillOval(275+20*i,40,10,10);
		}
		else if (atoms.size()==17){ // warning counter
			g.setColor(Color.ORANGE);
			for (int i=0; i<2; i++) g.fillOval(285+20*i,40,10,10);
		}
		else if (atoms.size()==18){ // warning counter
			g.setColor(Color.RED);
			g.fillOval(295,40,10,10);
		}
		g.setColor(Color.WHITE);
        g.setFont(new Font("MONOSPACED", Font.BOLD, 30));
        g.drawString("Score: "+score,15,40);
        beforeAtom.drawAtom(g,25,pulse); // animation for dead atoms
        afterAtom.drawAtom(g,25,pulse);
        for (int i=0; i<atoms.size(); i++) atoms.get(i).drawAtom(g,25,pulse); // draws circle atoms
        nextAtom.drawAtom(g,25,pulse); // draws centre atoms
    	g.setColor(Color.BLACK);
    	if (lose){ // when the player has lost (exceeded 18 atoms without any collapse)
    		g.fillOval(300-loseCount,300-loseCount,2*loseCount,2*loseCount); // the black hole with animation
    		if (loseCount>=225){ // if the player has lost
    			g.drawImage(LoadImages.get(16),300-64,450-64,null); // overlay
    			g.setFont(new Font("MONOSPACED", Font.BOLD, 40));
    			g.setColor(Color.WHITE);
    			g.drawString("GAME OVER",192,175);
    			g.drawString("SCORE: "+score,300-12*("SCORE: "+score).length(),290);
    			g.drawString("NAME: "+name,300-12*("NAME: "+name).length(),340);
    		}
    	}
    }
}
