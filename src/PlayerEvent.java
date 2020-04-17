public enum PlayerEvent{
    NEW_GAME ("New Game"),
    CHANGE_GAME_MODE ("Change Game Mode"),
    UNDO ("Undo"),
    MOVE (""),
    NONE ("");

    String text;

    PlayerEvent(String text){
        this.text = text;
    }

    String getText(){
        return text;
    }
}