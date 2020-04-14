import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    GUI gui;
    Thread gameThread;

    private void gameLoop(){
        System.out.println("started game loop");
    }

    private void handleMouseClickOnCell(int y) {
        System.out.format("User clicked on %d\n", y);
    }

    @Override
    public void start(Stage primaryStage) {

        // create user interface
        gui = new GUI(primaryStage, this::handleMouseClickOnCell);

        // start game thread
        gameThread = new Thread(this::gameLoop);
        gameThread.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
