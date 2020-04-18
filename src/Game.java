import javafx.util.Pair;

import java.util.Stack;
import java.util.Vector;

import static java.lang.Math.abs;

public class Game {

    public static final int N = 6; // rows
    public static final int M = 7; // columns

    public static final int YELLOW = -1;
    public static final int RED = 1;
    public static final int EMPTY = 0;
    private int player = YELLOW;

    private int [][] board = new int[N][M]; // initializes with 0 = EMPTY
    private static final int [][] DIR = {{1, 0}, {1, 1}, {0, 1}, {-1, 1}};
    private Stack<Pair<Long, Long>> history = new Stack<>();


    private static boolean isInside(int x, int y){
        return (0 <= x) && (x < N) && (0 <= y) && (y < M);
    }

    private static int kingDistance(int xi, int yi, int xf, int yf){
        return Math.max(abs(xf - xi), abs(yf - yi));
    }

    public static int enemy(int p){
        return -p;
    }

    public int getStone(int x, int y){
        return board[x][y];
    }

    public int getCurPlayer(){
        return player;
    }

    private Pair<Long, Long> convertToState(){
        long yellow = 0, red = 0, pow = 1;

        for(int x = 0; x < N; x++){
            for(int y = 0; y < M; y++){
                if(board[x][y] == YELLOW){
                    yellow |= pow;
                }
                if(board[x][y] == RED){
                    red |= pow;
                }

                pow *= 2;
            }
        }

        return new Pair<>(yellow, red);
    }

    private void loadGame(Pair<Long, Long> state){

        long yellow = state.getKey();
        long red = state.getValue();
        int cy = 0, cr = 0;

        // get board
        for(int x = 0; x < N; x++){
            for(int y = 0; y < M; y++){

                if(yellow % 2 == 1) {
                    board[x][y] = YELLOW;
                    cy++;
                }
                else if(red % 2 == 1) {
                    board[x][y] = RED;
                    cr++;
                }
                else {
                    board[x][y] = EMPTY;
                }

                yellow /= 2;
                red /= 2;
            }
        }

        // get player
        if(cy == cr) player = YELLOW;
        else player = RED;
    }

    public Pair<Long, Long> getCurState(){
        return history.peek();
    }

    public void rollBack(){

         // cannot roll back
        if(history.size() <= 1) return;

        // roll back
        history.pop();
        loadGame(history.peek());
    }

    private boolean isColumnFull(int col){
        return (board[0][col] != EMPTY);
    }

    public boolean isValidMove(int col){
        if(col < 0 || M <= col) return false; // no such column
        return !isColumnFull(col); // column not full
    }

    // assume: isValidMove(col) == true
    public void makeMove(int col){

        // find next free cell
        for(int row = N-1; row >= 0; row--){
            if(board[row][col] == EMPTY) {
                board[row][col] = getCurPlayer();
                break;
            }
        }

        player = -player;
        history.push(convertToState());
    }

    // game is not over -> return: null
    // board is full -> return: empty Vector
    // winner -> return winning stones
    public Vector<Pair<Integer, Integer>> getGameStatus(){

        Vector<Pair<Integer, Integer>> v = new Vector<>();

        // check for winner
        for(int x = 0; x < N; x++) {
            for (int y = 0; y < M; y++) { // each starting position

                // not part of a winning state
                if(board[x][y] == EMPTY) continue;

                for(int[] dir : DIR){ // each direction

                    // have already been checked by neighbour
                    if(isInside(x-dir[0], y - dir[1]) && board[x-dir[0]][y - dir[1]] == board[x][y]) continue;

                    // final positions
                    int xf = x, yf = y;

                    while(isInside(xf, yf) && board[xf][yf] == board[x][y]){
                        xf += dir[0];
                        yf += dir[1];
                    }

                    // check size of segment
                    if(kingDistance(x, y, xf, yf) >= 4) {
                        xf = x;
                        yf = y;

                        // add winning stones
                        while(isInside(xf, yf) && board[xf][yf] == board[x][y]){
                            v.add(new Pair<>(xf, yf));
                            xf += dir[0];
                            yf += dir[1];
                        }
                    }
                }
            }
        }

        // found winner
        if(!v.isEmpty()) return v;

        // check if board is full
        for (int col = 0; col < M; col++) {
            if (!isColumnFull(col)) {
                return null; // possible move
            }
        }

        // game is not over
        return new Vector<>();
    }

    public boolean isOver(){
        return getGameStatus() != null;
    }

    public Vector<Integer> getMoves(){
        // game is over -> no moves
        if(isOver()) return new Vector<>();

        // game is not over
        Vector<Integer> moves = new Vector<>();
        for(int col = 0; col < M; col++){
            if(isValidMove(col)) moves.add(col);
        }
        return moves;
    }

    public Game(){
        history.push(convertToState());
    }
}
