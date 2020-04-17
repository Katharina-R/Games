import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private GameMode gameMode = GameMode.PvP;
    private Game game;
    private GUI gui;
    private Thread gameThread;

    private boolean userInputEnabled = false;
    private PlayerEvent playerEvent = PlayerEvent.NONE;
    private int lastClicked;

    private boolean isPlayerMove(){
        if(game.getCurrentPlayer() == Game.YELLOW){
            return (gameMode == GameMode.PvP) || (gameMode == GameMode.PvC);
        }
        else{
            return (gameMode == GameMode.PvP) || (gameMode == GameMode.CvP);
        }
    }

    private synchronized void getPlayerEvent(int timeout) throws InterruptedException {
        userInputEnabled = true;
        System.out.println("wait");
        wait(timeout);
        System.out.println("stopped waiting");
        userInputEnabled = false;
    }

    private synchronized void handleMouseClickOnCell(int y) {
        System.out.format("User clicked on %d\n", y);

        if(!userInputEnabled) return;

        playerEvent = PlayerEvent.MOVE;
        lastClicked = y;

        notify(); // notify gameThread in getPlayerEvent
    }

    private synchronized void handleMouseClickOnMenu(PlayerEvent e) {
        System.out.println("User clicked on " + e.getText());

        if(!userInputEnabled) return;

        playerEvent = e;

        notify(); // notify gameThread in getPlayerEvent
    }

    private void gameLoop(){
        System.out.println("started game loop");

        // init game
        game = new Game();

        while(!game.isOver()){

            if(isPlayerMove()){

                try {
                    getPlayerEvent(0);
                } catch (InterruptedException e) {
                    System.out.println("Stopping Game Thread");
                    return;
                }

                if(playerEvent == PlayerEvent.MOVE){
                    //TODO: move
                }
                else if(playerEvent == PlayerEvent.NEW_GAME){
                    //TODO: new game
                }
                else if(playerEvent == PlayerEvent.CHANGE_GAME_MODE){
                    // TODO: change game mode
                }
                else if(playerEvent == PlayerEvent.UNDO){
                    // TODO: undo
                }
            }
            else{

                // TODO: get ai move
                //TODO: set stone
            }
        }
    }

    @Override
    public void start(Stage primaryStage) {

        // create user interface
        gui = new GUI(primaryStage, this::handleMouseClickOnCell, this::handleMouseClickOnMenu);

        // start game thread
        gameThread = new Thread(this::gameLoop);
        gameThread.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
