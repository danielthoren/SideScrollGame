import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Initiates and creates the window in wich the game is drawn. Also draws a highscorelist using 'HighScoreList'.
 */


public class GameFrame extends Application
{
	private String windowName;
	private Stage primaryStage;

	@Override
	public  void start(Stage primaryStage){
		this.primaryStage = primaryStage;
		primaryStage.setTitle(windowName);
		VBox topContainer = new VBox();
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root,800, 600);
		scene.setFill(Color.BLACK);

		//Creating objects to put in the container (topContainer)
		MenuBar menuBar = new MenuBar();
		//Adding objects to the topContainer
		topContainer.getChildren().add(menuBar);
		//Creating menues and putting them in the menuBar
		Menu file = new Menu("File");
		menuBar.getMenus().addAll(file);
		//Creating MenuItems and putting them in the correct Menu
		MenuItem exit = new MenuItem("Exit");
		file.getItems().addAll(exit);
		//Adding actionevents to the MenuItems
		exit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.exit(1);
			}
		});

		root.setTop(topContainer);

		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public GameFrame(String windowName) {
		this.windowName = windowName;
	}
	public GameFrame(){
		this.windowName = "Game";
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	/**
	 * The main() method is ignored in correctly deployed JavaFX application.
	 * main() serves only as fallback in case the application can not be
	 * launched through deployment artifacts, e.g., in IDEs with limited FX
	 * support. NetBeans ignores main().
	 *
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
}


/*
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

**/