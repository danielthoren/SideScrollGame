import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Initiates and creates the window in wich the game is drawn. Also draws a highscorelist using 'HighScoreList'.
 */
public class GameFrame extends JFrame
{
    private JTextArea highScoreArea;
    private GameComponent gameComponent;
    private final static int WINDOW_HEIGHT = 800;
    private final static int WINDOW_WIDTH = 600;

    public GameFrame(String windowName) throws HeadlessException {
	super(windowName);

	//Creating tetriscomponent
	gameComponent = new GameComponent(WINDOW_HEIGHT, WINDOW_WIDTH);

	//Creating Menu
	JMenuBar menuBar = new JMenuBar();
	JMenu menu = new JMenu("File");
	final JToggleButton pause = new JToggleButton("Pause");
	JMenuItem exit = new JMenuItem("Exit");
	exit.setToolTipText("Exit application");

	exit.addActionListener(new ActionListener()
	{
	    @Override public void actionPerformed(ActionEvent event) {
		System.exit(0);
	    }
	});
	pause.addActionListener(new ActionListener()
	{
	    @Override public void actionPerformed(final ActionEvent e) {
		System.out.println("pause");
	    }
	});


	menu.add(exit);
	menuBar.add(menu);
	menuBar.add(pause);
	setJMenuBar(menuBar);

	this.setLayout(new BorderLayout());

	this.pack();
	this.setVisible(true);
    }

    public void update(){
	this.repaint();
    }

}

