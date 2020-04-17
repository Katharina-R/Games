import javafx.application.Application;
import javafx.scene.paint.Paint;
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

    private int getPlayerMove() throws InterruptedException{
        int move;

        do{
            getPlayerEvent(0);

            if(playerEvent != PlayerEvent.MOVE) return -1;

            move = lastClicked;

        } while(!game.isValidMove(move));

        return move;
    }

    private void updateGUI(){

        for(int x = 0; x < Game.N; x++){
            for(int y = 0; y < Game.M; y++){
                if(game.getStone(x, y) == Game.YELLOW) gui.setStone(x + 1, y, Paint.valueOf(Style.YELLOW));
                else if(game.getStone(x, y) == Game.RED) gui.setStone(x + 1, y, Paint.valueOf(Style.RED));
                else gui.setStone(x + 1, y, Paint.valueOf(Style.EMPTY));
            }
        }
    }

    private void gameLoop(){
        System.out.println("started game loop");

        // init game
        game = new Game();

        int move;

        while(!game.isOver()){

            // TODO: mark possible moves

            if(isPlayerMove()){
                try {
                    move = getPlayerMove();
                } catch (InterruptedException e) {
                    return;
                }

                if(playerEvent == PlayerEvent.NEW_GAME){
                    //TODO: new game
                    break;
                }
                else if(playerEvent == PlayerEvent.CHANGE_GAME_MODE){
                    // TODO: change game mode
                    continue;
                }
                else if(playerEvent == PlayerEvent.UNDO){
                    // TODO: undo
                    continue;
                }

                game.makeMove(move);
            }
            else{

                // TODO: get ai move
                //TODO: set stone
            }

            updateGUI();
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
