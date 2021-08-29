import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class GridDemoFrame extends JFrame implements Constants2
{
	GridDemoPanel thePanel;
	JLabel scoreLabel, messageLabel;
	public GridDemoFrame()
	{
		super("Flow");
		
		setSize(ACROSS, DOWN+20);
		
		this.getContentPane().setLayout(new BorderLayout());
		thePanel = new GridDemoPanel(this);
		scoreLabel = new JLabel("Current Highest Score: 0");
		messageLabel = new JLabel("");
		//Box southPanel = Box.createHorizontalBox();
		
		this.getContentPane().add(thePanel,BorderLayout.CENTER);
		//this.getContentPane().add(southPanel, BorderLayout.SOUTH);
		//southPanel.add(Box.createHorizontalStrut(10));
		//southPanel.add(scoreLabel, BorderLayout.SOUTH);
//		southPanel.add(Box.createGlue());
//		southPanel.add(messageLabel, BorderLayout.SOUTH);
//		southPanel.add(Box.createHorizontalStrut(10));
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);	
		thePanel.initiateAnimationLoop(); // uncomment this line if your program uses animation.
	}

}
