package userinterface;

import gamelogic.GameComponent;
import gamelogic.GameLoop;
import javafx.application.Application;
import javafx.application.Platform;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * This is the main class of the game. It creates the window in wich the game and all menues is drawn. It also
 * instantiates the game and runs it.
 */
public class GameFrame extends Application
{
    private String windowName;
    //Field is initialized in the 'start' function that is called from the javaFX thread.
    private GameComponent gameComponent;

    //Todo Move values to user interface/launcher
    private static final double GAME_HEIGHT = 600;
    private static final double GAME_WIDTH = 800;

    /**
     * The 'start' function is called to initialize the contents of the 'Application' when the 'launch()' function is called.
     * @param primaryStage The primary 'Stage' in wich to put nodes.
     */
    @Override
    public  void start(Stage primaryStage){
        //Initializing userinterface.GameFrame
        GameLoop gameLoop = new GameLoop(this);
        gameComponent = new GameComponent(GAME_HEIGHT, GAME_WIDTH);

        //Giving 'gameComponent' focus
        Platform.runLater(new Runnable()
        {
            @Override public void run() {
                gameComponent.requestFocus();
            }
        });

        //Initializing GUI this.primaryStage = primaryStage;
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


        //Start gamelogic.GameLoop
        gameLoop.start();
    }

    /**
     * Creates the Vbox instance, and the contents therin, to be placed on the windows top.
     * @return
     */
    private VBox createTopMenues(){
        VBox topContainer = new VBox();
        //Creating objects to put in the container (topContainer)
        MenuBar menuBar = new MenuBar();
        //Adding objects to the topContainer
        topContainer.getChildren().addAll(menuBar);
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

    /**
     * Updates the 'gamelogic.GameComponent'
     * @param nanosScinseLast The time scinse the last update in nanoseconds.
     */
    public void update(float nanosScinseLast){
        gameComponent.update(nanosScinseLast);
    }

    /**
     * Draws the 'gamelogic.GameComponent'
     */
    public void draw(){
        gameComponent.draw();

    }

    public GameFrame(String windowName) {this.windowName = windowName;}

    public GameFrame(){
        this.windowName = "userinterface.GameFrame";
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public synchronized static void main(String[] args) {
        launch(args);
    }
}
