import java.util.Vector;

public class Minimax {

    public static class Move {
        int move;
        int winner;

        Move (int move_, int winner_){
            move = move_;
            winner = winner_;
        }
    }

    private static Move max(Move m1, Move m2){
        if(m1.winner > m2.winner) return m1;
        return m2;
    }

    private static Move min(Move m1, Move m2){
        if(m1.winner < m2.winner) return m1;
        return m2;
    }

    private static Game game;

    private static Move solve(int height){

        Vector<Integer> moves = game.getMoves();

        // game over
        if(moves.isEmpty()){
            return new Move(-1, game.getWinner());
        }

        if(height < 0) return new Move(-1, 0);

        Move cur;
        Move best = new Move(-1, 2 * Game.enemy(game.getCurPlayer()) );

        for(int move : moves){
            game.makeMove(move);
            cur = solve(height -1);
            game.rollBack();

            cur.move = move;
            if(game.getCurPlayer() == Game.YELLOW) best = max(cur, best);
            else best = min(cur, best);
        }

        return best;
    }

    public static int getMove(Game game_, int max_depth){

        game = game_;
        return solve(max_depth).move;
    }
}
