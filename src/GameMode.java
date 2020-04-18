public enum GameMode {
    PvP ("Player vs Player"),
    PvC ("Player vs AI"),
    CvP ("AI vs Player"),
    CvC ("AI vs AI");

    String text;

    GameMode(String text){
        this.text = text;
    }

    public String toString(){
        return text;
    }
}
