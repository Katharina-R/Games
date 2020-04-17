public enum GameMode {
    PvP ("Player vs Player"),
    PvC ("Player vs Computer"),
    CvP ("Computer vs Player"),
    CvC ("Computer vs Computer");

    String text;

    GameMode(String text){
        this.text = text;
    }

    public String getText(){
        return text;
    }
}
