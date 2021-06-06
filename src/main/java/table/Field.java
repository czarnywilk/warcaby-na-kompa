package table;

/**
 * Klasa reprezentująca pole na planszy.
 */
class Field{
    /**
     * Zmienne reprezentujące położenie pola oraz rodzaj piona.
     */
    protected int x,y,pawn;

    /**
     * Konstruktor towrzący nowe pole.
     * @param x położenie w osi x.
     * @param y położenie w osi y.
     * @param pawn rodzaj piona.
     */
    public Field(int x, int y, int pawn){
        this.x = x;
        this.y = y;
        this.pawn = pawn;
    }

    /**
     * Konstruktor domyślny.
     */
    public Field(){
        this.x = 0;
        this.y = 0;
        this.pawn = 5;
    }

    /**
     * Setter rodzaju piona na polu.
     * @param pawn nowy rodzaj poiona.
     */
    public void setPawn(int pawn){
        this.pawn = pawn;
    }

    /**
     * Getter rodzaju piona.
     * @return rodzaj piona.
     */
    public int getPawn(){
        return this.pawn;
    }

    /**
     * Metoda zwracająca współrzędną x pola
     * @param whitePlayer parametr informujący o perspektywie gry - jeżeli ma wartość true zwracany jest wynik z perspektywy białego gracza.
     * @return ułożenie danego pola w osi x
     */
    public int getX(boolean whitePlayer){
        if (whitePlayer) return this.x;
        else return 9-this.x;
    }
    /**
     * Metoda zwracająca współrzędną y pola .
     * @param whitePlayer parametr informujący o perspektywie gry - jeżeli ma wartość true zwracany jest wynik z perspektywy białego gracza.
     * @return ułożenie danego pola w osi y.
     */
    public int getY(boolean whitePlayer){
        if (whitePlayer) return this.y;
        else return 9-this.y;
    }
}
