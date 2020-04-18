import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.Vector;

public class Main extends Application {

    private GameMode gameMode = GameMode.PvP;
    private Game game;
    private GUI gui;
    private Thread gameThread;

    private boolean userInputEnabled = false;
    private PlayerEvent playerEvent = PlayerEvent.NONE;
    private int lastClicked;

    private boolean isPlayerMove(){
        if(game.getCurPlayer() == Game.YELLOW){
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
                int finalX = x, finalY = y;

                if(game.getStone(x, y) == Game.YELLOW) {
                    Platform.runLater(() -> gui.setStone(finalX, finalY, Paint.valueOf(Style.YELLOW)));
                }
                else if(game.getStone(x, y) == Game.RED) {
                    Platform.runLater(() -> gui.setStone(finalX, finalY, Paint.valueOf(Style.RED)));
                }
                else {
                    Platform.runLater(() -> gui.setStone(finalX, finalY, Paint.valueOf(Style.EMPTY)));
                }
            }
        }
    }

    private String playerText(int p){
        if(p == Game.YELLOW) return "Yellow";
        if (p == Game.RED) return "Red";
        return "Empty";
    }

    private void greyOutGUI(){
        for(int x = 0; x < Game.N; x++){
            for(int y = 0; y < Game.M; y++){
                int finalX = x, finalY = y;

                if(game.getStone(x, y) == Game.YELLOW) {
                    Platform.runLater(() -> gui.setStone(finalX, finalY, Paint.valueOf(Style.YELLOW_LIGHT)));
                }
                else if(game.getStone(x, y) == Game.RED) {
                    Platform.runLater(() -> gui.setStone(finalX, finalY, Paint.valueOf(Style.RED_LIGHT)));
                }
            }
        }
    }

    private void markWinningStones(Vector<Pair<Integer, Integer>> stones){
        for(Pair<Integer, Integer> stone: stones){
            if(game.getStone(stone.getKey(), stone.getValue()) == Game.YELLOW){
                Platform.runLater(() -> gui.setStone(stone.getKey(), stone.getValue(), Paint.valueOf(Style.YELLOW)));
            }
            else {
                Platform.runLater(() -> gui.setStone(stone.getKey(), stone.getValue(), Paint.valueOf(Style.RED)));
            }
        }
    }

    private boolean handleMenuButton(){
        if (playerEvent == PlayerEvent.NEW_GAME) {
            game = new Game();
            return true;
        }
        if (playerEvent == PlayerEvent.CHANGE_GAME_MODE) {
            // TODO: change game mode
            return true;
        }
        if (playerEvent == PlayerEvent.UNDO) {
            game.rollBack();
            return true;
        }

        return false;
    }

    private void gameLoop(){
        System.out.println("started game loop");

        // init game
        game = new Game();

        int move;

        do {
            while (!game.isOver()) {
                updateGUI();
                // TODO: mark possible moves

                Platform.runLater(() -> gui.setGameInfo(playerText(game.getCurPlayer()) + "'s turn!"));

                if (isPlayerMove()) {
                    try {
                        move = getPlayerMove();
                    } catch (InterruptedException e) {
                        return;
                    }

                    if(handleMenuButton()) continue;

                } else {
                    move = Minimax.getMove(game, 10);
                }

                game.makeMove(move);
            }

            // make last move
            updateGUI();

            // get winner
            Vector<Pair<Integer, Integer>> winningStones = game.getGameStatus();

            if (winningStones.isEmpty()) {
                Platform.runLater(() -> gui.setGameInfo("Draw!"));
            } else {
                Platform.runLater(() -> gui.setGameInfo(playerText(Game.enemy(game.getCurPlayer())) + " won!"));

                // mark winning pieces
                greyOutGUI();
                markWinningStones(winningStones);
            }

            // user didn't undo / start a new game
            do{
                // get input from menu buttons
                try {
                    getPlayerEvent(0);
                } catch (InterruptedException e) {
                    return;
                }

                handleMenuButton();
            } while(playerEvent != PlayerEvent.UNDO && playerEvent != PlayerEvent.NEW_GAME);
        } while(true);
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
