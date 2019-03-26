/*  Program Name: Atomas
    Name: Robert Wu
    Course: ICS4U1-01
    Teacher: Ms. Strelkovska
    Assignment: Culminating
    Date: 2017/01/17
    Description: Atom Class (gameplay rules in AtomasPanel.java)
*/
import javax.swing.*;
import java.awt.* ;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.*;
import java.io.*;

class Atom{

    // variables
	private static Color colours[];
	private static String line[], elements[];
    private static int min=1, max=3, travelCount=0;
    private static double angleSpace, startAngle=0;
    private static Scanner sc;
    private boolean explosion=false, fuse;
    private double x, y, a, oldX, oldY, oldA;
    private boolean isNew=false, isFresh=false, isCollapse;
    private int n;

    public static void loadData(){ // loads text files for atom data (not instance method)
    	elements = new String[122];
    	colours = new Color[122];
    	try{
    		sc = new Scanner(new FileReader("AtomData.txt"));
    		for (int i=0; i<122; i++){
    			line = sc.nextLine().split("\t");
                //System.out.println(line[1]+" "+line[3]);
    			elements[i] = line[1].trim();
    			colours[i] = Color.decode(line[3].trim());
    		}
    		sc.close();
    	}
    	catch(Exception e){ // error handling
            e.printStackTrace();
    		System.out.println("Either AtomData.txt is missing or corrupted.");
    	}
    }
    public Atom(double d){ // constructor for double and int
        this.n = (int)d;
        this.x = 300;
        this.y = 300;
        this.isCollapse = (n==0) || (n==-2);
    }

    // get methods
    public static int getMin(){return min;}
    public static int getMax(){return max;}
    public static double getStartAngle(){return startAngle;}
    public static double getAngleSpace(){return angleSpace;}
    public static int getTravelCount(){return travelCount;}
    public int getN(){return n;}
    public double getX(){return x;}
    public double getY(){return y;}
    public double getA(){return a;}
    public double getOA(){return oldA;}
    public boolean getFresh(){return isFresh;}
    public boolean getCollapse(){return isCollapse;}
    public boolean getFuse(){return fuse;}

    // set methods
    public void setN(int n){this.n = n;}
    public void setX(double x){this.x = x;}
    public void setY(double y){this.y = y;}
    public void setA(double a){this.a = a;}
    public void setOX(double x){this.oldX = x;}
    public void setOY(double y){this.oldY = y;}
    public void setOA(double a){this.oldA = a;}
    public void setIsNew(boolean b){this.isNew = b;}
    public void setFresh(boolean b){this.isFresh = b;}
    public void setCollapse(boolean b){this.isCollapse = b;}
    public void setFuse(boolean b){this.fuse = b;}
    public static void setAngleSpace(double aS){angleSpace = aS;}
    public static void setMin(int n){min = Math.max(1,n);}
    public static void setMax(int n){max = Math.min(118,n);}
    public static void setStartAngle(double a){startAngle = a;}
    public static void setTravelCount(int tC){travelCount = tC;}

    public void travel(int index){ // travel for determining new angle and x/y positions
        a = (angleSpace*index+startAngle)%(2*Math.PI);
        if (a-oldA>Math.PI) a-= 2*Math.PI;
        else if (oldA-a>Math.PI) a+= 2*Math.PI;
        if (travelCount<=0){ // when there is no animation
            if (isNew) isNew = false;
            x = 300+(200*Math.cos(a));
            y = 300-(200*Math.sin(a));
            lapAtom();
        }
        else{ // if animation is happening
            if (index==0) travelCount--;
            if (isNew){
                x = 300+((200*((30-(Math.min(travelCount,30)))/30.0))*Math.cos(a));
                y = 300-((200*((30-(Math.min(travelCount,30)))/30.0))*Math.sin(a));
            }
            else if (isFresh){
            	x = 300+((200*(((Math.min(travelCount,30)-30))/30.0))*Math.cos(a));
                y = 300-((200*(((Math.min(travelCount,30)-30))/30.0))*Math.sin(a));
            }
            else{
                x = 300+(200*Math.cos(a-(a-oldA)/30*(Math.min(travelCount,30))));
                y = 300-(200*Math.sin(a-(a-oldA)/30*(Math.min(travelCount,30))));
            }
        }
    }

    public void travel(boolean b){ // overload for dead atom animation
    	if (a-oldA>Math.PI) a-= 2*Math.PI;
        else if (oldA-a>Math.PI) a+= 2*Math.PI;
        if (travelCount>0 && b && a!=-900.0){
            x = 300+(200*Math.cos(a-(a-oldA)/30*(Math.min(travelCount,30))));
            y = 300-(200*Math.sin(a-(a-oldA)/30*(Math.min(travelCount,30))));
        }
        else{
            a = -900;
            x = -300;
            y = -300;
            lapAtom();
        }
    }
    public void lapAtom(){ // saves angle and x/y positions as old
        oldA = a;
        oldX = x;
        oldY = y;
    }
    public void drawAtom(Graphics g, double size, int p, String s){ // overload for debugging   
        g.drawString(s,(int)x,(int)y-50);
        drawAtom(g,size,p);
    }
    public void drawAtom(Graphics g, double size, int p){ // drawing atoms
        drawOrbits(g,(int)size/6,size,p); // change to (g,(int)size/6,-1) for debugging, (g,(int)size/6,p) for final
        g.setColor(colours[n+3]);
        g.fillOval((int)(x-size),(int)(y-size),(int)size*2,(int)size*2); // ball
        g.setColor(Color.WHITE);
        g.setFont(new Font("MONOSPACED", Font.BOLD, 20));
        if (size==100){ // if the atom is on the menu
        	g.setFont(new Font("MONOSPACED", Font.BOLD, 75));
	        g.drawString(elements[n+3],(int)x-22*elements[n+3].length()-(elements[n+3].length()-1),(int)y+10);
	        g.setFont(new Font("MONOSPACED", Font.BOLD, 50));
	        if (n<10) g.drawString(""+n,(int)x-14,(int)y+60);
	        else if (n<100) g.drawString(""+n,(int)x-30,(int)y+60);
	        else if (n<1000) g.drawString(""+n,(int)x-40,(int)y+60);
        	return;
        }
        if (n<1){ // if the atomic number is less than one (not an atom)
        	g.drawString(elements[n+3],(int)x-5*elements[n+3].length()-(elements[n+3].length()-1),(int)y+6);
        	return;
        }
        g.drawString(elements[n+3],(int)x-5*elements[n+3].length()-(elements[n+3].length()-1),(int)y+1);
        g.setFont(new Font("MONOSPACED", Font.BOLD, (int)size-9));
        if (n<10) g.drawString(""+n,(int)x-4,(int)y+17);
        else if (n<100) g.drawString(""+n,(int)x-9,(int)y+17);
        else if (n<1000) g.drawString(""+n,(int)x-14,(int)y+17);
    }
    public void drawOrbits(Graphics g,int size,double radius,int p){ // drawing the orbitting particles
    	if (p==-1){ // debugging
    		g.drawLine(300,300,(int)x,(int)y);
    		return;
    	}
    	g.setColor(Color.WHITE);
        int orbits = n;
        if (n>2) orbits = (n-3)%8+1;
        for (int j=0; j<n; j++) g.fillOval((int)(x-size + (int)(radius*6/5)*Math.cos(p*(Math.PI/60)+j*(2*Math.PI/orbits))), (int)(y-size + (int)(radius*6/5)*Math.sin(p*(Math.PI/60)+j*(2*Math.PI/orbits))), 2*size, 2*size);
    }
}