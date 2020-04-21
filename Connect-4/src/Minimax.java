import javafx.util.Pair;

import java.util.HashMap;
import java.util.Vector;

public class Minimax {

    public static class Move {
        int move;
        int turn;
        boolean isOptimal;
        int winner;

        Move (int move_, int turn_, boolean isOptimal_, int winner_){
            move = move_;
            turn = turn_;
            isOptimal = isOptimal_;
            winner = winner_;
        }

        @Override
        protected Move clone(){
            return new Move(move, turn, isOptimal, winner);
        }
    }

    private static Move max(Move m1, Move m2){
        if(m1.winner == m2.winner){ // try to extend the time until loss/draw
            if(m1.turn > m2.turn) return m1;
            else return m2;
        }
        if(m1.winner > m2.winner) return m1;
        return m2;
    }

    private static Move min(Move m1, Move m2){
        if(m1.winner == m2.winner){ // try to extend the time until loss/draw
            if(m1.turn > m2.turn) return m1;
            else return m2;
        }
        if(m1.winner < m2.winner) return m1;
        return m2;
    }

    private static Game game;
    private static HashMap<Pair<Long, Long>, Move> dp = new HashMap<>();

    private static Move solve(int height, int alpha, int beta){

        Vector<Integer> moves = game.getMoves();

        // game over
        if(moves.isEmpty()){
            return new Move(-1, game.getTurn(), true, game.getWinner());
        }

        // reached max height
        if(height < 0) return new Move(-1, game.getTurn(),false, game.evaluateBoard());

        // already calculated state
        if (dp.containsKey(game.getCurState())) return dp.get(game.getCurState()).clone();

        // test next moves
        Move cur;
        Move best = new Move(-1, -1,false,2 * Game.enemy(game.getCurPlayer()) );
        int notOptimal = 0;

        for(int move : moves){
            game.makeMove(move);
            cur = solve(height - 1, alpha, beta);
            game.rollBack();

            if(!cur.isOptimal) notOptimal++;

            cur.move = move;
            if(game.getCurPlayer() == Game.YELLOW) {
                best = max(cur, best);
                alpha = Math.max(alpha, best.winner);
            }
            else {
                best = min(cur, best);
                beta = Math.min(beta, best.winner);
            }

            // prune
            if(alpha >= beta || alpha == Game.YELLOW || beta == Game.RED) break;
        }

        // set isOptimal for chosen move
        best.isOptimal = (notOptimal == 0) || (best.winner == game.getCurPlayer());

        dp.put(game.getCurState(), best.clone());
        return best;
    }

    private static void clearDP(){

        // remove uninteresting results
        dp.entrySet().removeIf(e -> !e.getValue().isOptimal);
    }


    public static Pair<Move, Integer> getMove(Game game_, long timeout){

        long start = System.currentTimeMillis();
        int max_depth = -1;
        Move move;
        game = game_;

        do{
            max_depth++;
            move = solve(max_depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
            clearDP();

        } while(!move.isOptimal && (System.currentTimeMillis() - start) < timeout);

        return new Pair<>(move, max_depth);
    }
}
