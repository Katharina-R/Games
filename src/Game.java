import javafx.util.Pair;

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


    private static boolean isInside(int x, int y){
        return (0 <= x) && (x < N) && (0 <= y) && (y < M);
    }

    private static int kingDistance(int xi, int yi, int xf, int yf){
        return Math.max(abs(xf - xi), abs(yf - yi));
    }

    public int getStone(int x, int y){
        return board[x][y];
    }

    public int getCurrentPlayer(){
        return player;
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
                board[row][col] = getCurrentPlayer();
                break;
            }
        }

        player = -player;
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
}
