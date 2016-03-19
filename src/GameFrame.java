import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
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
	private GameComponent gameComponent;
	private GameLoop gameLoop;

	private long lastFpsTime = 0;
	private long lastLoopTime;
	private int fps = 0;
	private boolean gameRunning;
	private int targetFPS;

	private double gameHeight = 600;
	private double gameWidth = 800;

	@Override
	public  void start(Stage primaryStage){
		//Initializing GameFrame
		targetFPS = 1;
		gameRunning = true;
		gameLoop = new GameLoop(this);
		gameComponent = new GameComponent(gameHeight, gameWidth);

		//Initializing GUI
		this.primaryStage = primaryStage;
		primaryStage.setTitle(windowName);
		Group root = new Group();
		BorderPane mainBorderPane = new BorderPane();
		root.getChildren().add(mainBorderPane);
		Scene scene = new Scene(root);
		scene.setFill(Color.BLACK);

		mainBorderPane.setTop(createTopMenues());
		mainBorderPane.setCenter(gameComponent);

		primaryStage.setScene(scene);
		primaryStage.show();


		//Start GameLoop
		gameLoop.start();
	}

	private VBox createTopMenues(){
		VBox topContainer = new VBox();
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

		return topContainer;
	}

	public void update(float timeScinseLast){
		gameComponent.update(timeScinseLast);
	}

	public void draw(){
		gameComponent.draw();

	}

	public GameFrame(String windowName) {
		this.windowName = windowName;
	}

	public GameFrame(){
		this.windowName = "GameFrame";
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
