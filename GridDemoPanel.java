import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JFrame;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GridDemoPanel extends JPanel implements MouseListener, KeyListener, Constants2
{
	private ArrayList<Particle> gas;
	private boolean start = false;
	private double eCutoff1;
	private double eCutoff2;

	private double deltaE;
	final private JFrame svFrame;
	final private JFrame dvFrame;
	private ArrayList<int []> gc;

	public GridDemoPanel(GridDemoFrame parent)
	{
		super();

		dvFrame = new JFrame("Mole Fraction");
		DistroVisualizer dvPanel = new DistroVisualizer();
		dvFrame.add(dvPanel);
		dvFrame.setSize(AVACROSS,AVDOWN+20);
		dvFrame.setVisible(true);
		dvFrame.setLocation(060,445);
		gc = new ArrayList<int[]>();
		svFrame = new JFrame("Speed Distribution");
		SpeedVisualizer svPanel = new SpeedVisualizer();
		svFrame.add(svPanel);
		svFrame.setSize(AVACROSS,AVDOWN+20);
		svFrame.setVisible(true);
		svFrame.setLocation(660,445);
		setBackground(Color.BLACK);
		gas = new ArrayList<Particle>();
//		gas.add(new Particle(50,50,25,Math.PI/3));
//		gas.add(new Particle(100,140,1,-Math.PI/2));
		for (int i = 0; i < DOWN; i+=DENSITY) {
			for (int j = 0; j < ACROSS; j+=DENSITY) {
			gas.add(new Particle(j,i,0));
		}}
		eCutoff1 = 7055;
		eCutoff2 = 45;
		deltaE = 2000;

	}

	public void paintComponent(Graphics g) {
		start = true;
		//paints both boxes and seeing lines
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		for (int i = 0; i < gas.size(); i++) {
			gas.get(i).drawSelf(g);
		}
	}
	
	/**
	 * the mouse listener has detected a click, and it has happened on the cell in theGrid at row, col
	 * @param row
	 * @param col
	 */
	public void userClickedCell(int row, int col) {}

	//============================ Mouse Listener Overrides ==========================
	@Override
	// mouse was just released within about 1 pixel of where it was pressed.
	public void mouseClicked(MouseEvent e) {
		// mouse location is at e.getX() , e.getY().
		// if you wish to convert to the rows and columns, you can integer-divide by the cell size.

	}

	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	// mouse just entered this window
	public void mouseEntered(MouseEvent e){}
	@Override
	// mouse just left this window
	public void mouseExited(MouseEvent e){}
	//============================ Key Listener Overrides ==========================
	@Override
	/**
	 * user just pressed and released a key. (May also be triggered by autorepeat, if key is held down?
	 * @param e
	 */
	public void keyTyped(KeyEvent e) {}
	@Override
	//not active in final program - remnant of when the program was a playable game of snake
	public void keyPressed(KeyEvent e)
	{
	}

	@Override
	public void keyReleased(KeyEvent e) {}
	// ============================= animation stuff ======================================
	/**
	 * if you wish to have animation, you need to call this method from the GridDemoFrame AFTER you set the window visibility.
	 */
	public void initiateAnimationLoop() {
		Thread aniThread = new Thread( new AnimationThread(0)); // the number here is the number of milliseconds between z.
		aniThread.start(); }
	
	/**
	 * Modify this method to do what you want to have happen periodically.
	 * This method will be called on a regular basis, determined by the delay set in the thread.
	 * Note: By default, this will NOT get called unless you uncomment the code in the GridDemoFrame's constructor
	 * that creates a thread.
	 *
	 */
	public void animationStep() {
		for (int i = 0; i < gas.size(); i++) {
			if (gas.get(i).getX() < 0) {
				gas.get(i).setX(0);
				gas.get(i).setVt(Math.PI-gas.get(i).getVt());}
			else if (gas.get(i).getX() > ACROSS-2*SIZE) {
				gas.get(i).setX(ACROSS-2*SIZE);//X(-gas.get(i).getX()+DOWN-SIZE);
				gas.get(i).setVt(Math.PI-gas.get(i).getVt());}
			if (gas.get(i).getY() < 0) {
				gas.get(i).setY(0);
				gas.get(i).setVt(2*Math.PI-gas.get(i).getVt());}
			else if (gas.get(i).getY() > DOWN-2*SIZE) {
				gas.get(i).setY(DOWN-2*SIZE);
				gas.get(i).setVt(2*Math.PI-gas.get(i).getVt());}
			//TODO questions:
			//how much energy is released and who gets the KE kick when 2 molecules form
			//does the delta E of the reaction or the delta enthalpy matter in this simulation?
			//in unimolecular reactions, do they spontaneously break down on collisions with other molecules or in some other way?
			//is this affected by the fact that it's in 2D?
			//semielasticity?
			for (int j = 0; j < gas.size(); j++) {
				if (i!=j) {
					double d = dist(gas.get(i),gas.get(j));
					double dy = gas.get(j).getY()-gas.get(i).getY();
					double dx = gas.get(j).getX()-gas.get(i).getX();
					if (d<2*SIZE) {
						//test reaction: 	NO2 + NO2 --> NO3 + NO 		slow
						//					NO3 + CO  --> NO2 + CO2		fast
						//		overall		NO2 + CO  --> NO + CO2
						//A <-> B equilibrium
						if (gas.get(i).getKE() > eCutoff1 && gas.get(i).getIdentity() == 0) {
							gas.get(i).setIdentity(1);
							gas.get(i).modKE(deltaE);
						}
						if (gas.get(j).getKE() > eCutoff1 + deltaE && gas.get(j).getIdentity() == 1) {
							gas.get(j).setIdentity(0);
							gas.get(j).modKE(-deltaE);
						}

						double lineAngle = -Math.PI / 2 + Math.atan(dy / dx);
						gas.get(i).modX((2 * SIZE - d + .00001) * Math.cos(lineAngle - Math.PI / 2));
						gas.get(i).modY((2 * SIZE - d + .00001) * Math.sin(lineAngle - Math.PI / 2));
						double stableVR1 = gas.get(i).getVr() * Math.cos(lineAngle - gas.get(i).getVt());
						double stableVR2 = gas.get(j).getVr() * Math.cos(lineAngle - gas.get(j).getVt());
						double oldVR1 = gas.get(i).getVr() * Math.sin(lineAngle - gas.get(i).getVt());
						double oldVR2 = gas.get(j).getVr() * Math.sin(lineAngle - gas.get(j).getVt());
						//						System.out.println(gas.get(i).getVr() + "   " + Math.sqrt(stableVR1*stableVR1+oldVR1*oldVR1));
						//						System.out.println(gas.get(j).getVr() + "   " + Math.sqrt(stableVR2*stableVR2+oldVR2*oldVR2));
						double newVR1 = oldVR2;
						double newVR2 = oldVR1;
						//double newVR1 = (gas.get(i).getMass()-gas.get(j).getMass())*oldVR1/(gas.get(i).getMass()+gas.get(j).getMass())+(2*gas.get(j).getMass())*oldVR2/(gas.get(i).getMass()+gas.get(j).getMass());
						//double newVR2 = (gas.get(i).getMass()-gas.get(j).getMass())*oldVR2/(gas.get(i).getMass()+gas.get(j).getMass())+(2*gas.get(j).getMass())*oldVR1/(gas.get(i).getMass()+gas.get(j).getMass());
						gas.get(i).polarize(Math.cos(lineAngle) * stableVR1 + Math.cos(lineAngle - Math.PI / 2) * newVR1, Math.sin(lineAngle) * stableVR1 + Math.sin(lineAngle - Math.PI / 2) * newVR1);
						gas.get(j).polarize(Math.cos(lineAngle) * stableVR2 + Math.cos(lineAngle - Math.PI / 2) * newVR2, Math.sin(lineAngle) * stableVR2 + Math.sin(lineAngle - Math.PI / 2) * newVR2);

						break;
					}}
			}}
		int a = 0;
		int b = 0;
		for (int i = 0; i < gas.size(); i++) {
			gas.get(i).notch();
			if (gas.get(i).getIdentity() == 0) {a++;}
			else if (gas.get(i).getIdentity() == 1 ){b++;}}
		gc.add(new int[] {a,b});
		repaint();
		//svFrame.repaint();
		dvFrame.repaint();
	}
	public double dist(Particle a, Particle b) {
		return Math.sqrt(Math.pow(a.getY()-b.getY(),2)+Math.pow(a.getX()-b.getX(),2));}

	//displays the full ANN connection and propagated value for a given Snakey with viperPit index as globalRepaintIndex
	//TODO show by species
	public class SpeedVisualizer extends JPanel
	{
		public SpeedVisualizer()
		{super();}
		public void paintComponent(Graphics g)
		{
			if (start) {
				//setBackground(Color.WHITE);
				int boxes = 100;
				int [] vals = new int[boxes];
				g.setColor(Color.RED);
				for (int i = 0; i < gas.size(); i++) {
					vals[(int)(gas.get(i).getVr()*.5)]++;}
				float fFactor = 20;
				for (int i = 0; i < boxes; i ++) {
					g.fillRect(AVACROSS/boxes*i,(int)(AVDOWN-fFactor*vals[i]*AVDOWN/gas.size()),AVACROSS/boxes,(int)(vals[i]*fFactor*AVDOWN/gas.size()));
				}
				g.drawLine((int)(AVACROSS/boxes*eCutoff1),0,(int)(AVACROSS/boxes*eCutoff1),AVDOWN+20);
		}}}

	public class DistroVisualizer extends JPanel
	{
		public DistroVisualizer()
		{super();}
		public void paintComponent(Graphics g)
		{
			if (start) {
				for (int i = 0; i < gc.size(); i++) {
					g.setColor(Color.RED);
					g.fillOval((int)(AVACROSS*i/200),(int)(AVDOWN-AVDOWN*gc.get(i)[0]/MOLEC_NUM),5,5);
					g.setColor(Color.BLUE);
					g.fillOval((int)(AVACROSS*i/200),(int)(AVDOWN-AVDOWN*gc.get(i)[1]/MOLEC_NUM),5,5);
				if (i == 200) {
					System.out.println(gc.get(200)[0] + "   " + gc.get(200)[1]);
				}}
			}
		}}

	// ------------------------------- animation thread - internal class -------------------
	public class AnimationThread implements Runnable
	{
		long start;
		long timestep;
		public AnimationThread(long t) {
			timestep = t;
			start = System.currentTimeMillis();}
		@Override
		public void run() {
			long difference;
			while (true) {
				difference = System.currentTimeMillis() - start;
				if (difference >= timestep) {
					animationStep();
					start = System.currentTimeMillis();}
				try {Thread.sleep(100);}
				catch (InterruptedException iExp) {
					System.out.println(iExp.getMessage());
					break;}}}}




}
