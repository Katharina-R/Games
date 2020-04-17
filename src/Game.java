public class Game {

    public static final int N = 6; // rows
    public static final int M = 7; // columns

    public static final int YELLOW = -1;
    public static final int RED = 1;

    private int player = YELLOW;

    public int getCurrentPlayer(){
        return player;
    }

    public boolean isOver(){
        //TODO: implement isOver
        return false;
    }
}
