import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class GUI {

    private final int PADDING = 15;
    private final int CELL_PADDING = 10;
    private final int MENU_SPACING = 100;
    private final int GAME_SPACING = 10;

    private final int RADIUS = 30;
    private final int CELL_SIZE = 2 * RADIUS + 2 * CELL_PADDING;
    private final int BOARD_SIZE = Game.M * CELL_SIZE;
    private final int MENU_WIDTH = BOARD_SIZE;
    private final int MENU_HEIGHT = 40;
    private final int GAME_WIDTH = BOARD_SIZE + 2 * PADDING;
    private final int GAME_HEIGHT = BOARD_SIZE + MENU_HEIGHT + 2 * PADDING + GAME_SPACING;
    private final int GAME_INFO_WIDTH = GAME_WIDTH;
    private final int GAME_INFO_HEIGHT = 100;
    private final int WINDOW_WIDTH = GAME_WIDTH;
    private final int WINDOW_HEIGHT = GAME_INFO_HEIGHT + GAME_HEIGHT;

    private Circle[][] stone = new Circle[Game.N + 1][Game.M];
    private Consumer<Integer> handleMouseClickOnCell;

    // force the size of panes
    private void setSize(Pane pane, int width, int height) {
        pane.setMinSize(width, height);
        pane.setMaxSize(width, height);
    }

    private VBox getGameInfoUI(){
        // TODO set text
        VBox gameInfo = new VBox();
        setSize(gameInfo, GAME_INFO_WIDTH, GAME_INFO_HEIGHT);
        gameInfo.setStyle(GUIColour.GAME_INFO);
        return gameInfo;
    }

    private Button getButton(String text){
        return new Button(text);
    }

    private HBox getMenuUI(){
        HBox menuUI = new HBox(
                getButton("New Game"),
                getButton("Change Game Mode"),
                getButton("Undo"));
        menuUI.setAlignment(Pos.CENTER);
        setSize(menuUI, MENU_WIDTH, MENU_HEIGHT);
        menuUI.setSpacing(MENU_SPACING);
        return menuUI;
    }

    private VBox getStone(int x, int y){
        stone[x][y] = new Circle(RADIUS);
        stone[x][y].setFill(Color.valueOf(GUIColour.UI_COLOUR));

        VBox cellUI = new VBox(stone[x][y]);
        cellUI.setPadding(new Insets(CELL_PADDING));
        setSize(cellUI, CELL_SIZE, CELL_SIZE);

        if (x == 0) {
            stone[x][y].setStroke(Color.BLACK);
            stone[x][y].setStrokeWidth(1.5);
            stone[x][y].getStrokeDashArray().addAll(6.7);
            cellUI.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> handleMouseClickOnCell.accept(y));
        }
        else {
            stone[x][y].setStroke(Color.DARKBLUE);
            stone[x][y].setStrokeWidth(1.75);
            cellUI.setStyle(GUIColour.CELL);
        }

        return cellUI;
    }

    private GridPane getBoardUI(){
        GridPane boardUI = new GridPane();
        boardUI.setPadding(new Insets(0));
        setSize(boardUI, BOARD_SIZE, BOARD_SIZE);

        for (int i = 0; i < Game.N + 1; i++) {
            for (int j = 0; j < Game.M; j++) {
                boardUI.add(getStone(i, j), j, i);
            }
        }

        return boardUI;
    }

    private VBox getGameUI(){
        VBox gameUI = new VBox(getMenuUI(), getBoardUI());
        gameUI.setPadding(new Insets(PADDING));
        gameUI.setSpacing(GAME_SPACING);
        setSize(gameUI, GAME_WIDTH, GAME_HEIGHT);
        return gameUI;
    }

    private VBox getUI(){
        VBox ui = new VBox(getGameInfoUI(), getGameUI());
        ui.setPadding(new Insets(0));
        setSize(ui, WINDOW_WIDTH, WINDOW_HEIGHT);
        ui.setStyle(GUIColour.UI);
        return ui;
    }

    public GUI(Stage primaryStage, Consumer<Integer> handleMouseClickOnCell_){
        handleMouseClickOnCell = handleMouseClickOnCell_;

        primaryStage.setTitle("Connect 4");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(getUI(), WINDOW_WIDTH, WINDOW_HEIGHT));
        primaryStage.setWidth(WINDOW_WIDTH + 5);
        primaryStage.setHeight(WINDOW_HEIGHT + 25);
        primaryStage.show();
    }
}
