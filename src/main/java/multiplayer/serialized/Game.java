package multiplayer.serialized;

import com.google.gson.annotations.SerializedName;
import table.TableOnline;

/**
 * Klasa reprezentująca partie gry w warcaby.
 */
public class Game {
    /**
     * Stan planszy w postaci znakowej
     */
    @SerializedName("board")
    private String board;
    /**
     * Identyfikator aktualnego gracza.
     */
    @SerializedName("currentPlayerId")
    private Integer currentPlayerId = null;
    /**
     * Identyfikator zawodnika grającego białmi pionami.
     */
    @SerializedName("whitePlayerId")
    private Integer whitePlayerId = null;
    /**
     * Identyfikator zawodnika grającego czarnymi pionami.
     */
    @SerializedName("blackPlayerId")
    private Integer blackPlayerId = null;
    /**
     * Nazwa pokoju z grą.
     */
    @SerializedName("gameName")
    private String gameName;
    /**
     * Zmienna informująca czy gra się rozpoczeła.
     */
    @SerializedName("gameStarted")
    private boolean gameStarted = false;
    /**
     * Identyfikator gry.
     */
    private int id;

    /**
     * Konstruktor gry.
     * @param gameName Nazwa partii.
     */
    public Game(String gameName) {
        board = TableOnline.cleanBoard;
        this.gameName = gameName;
    }

    // ------------------------ GETTERS ------------------------

    /**
     * Getter stanu planszy
     * @return stan planszy w postaci Stringa.
     */
    public String getBoard() {
        return board;
    }

    /**
     * Getter identyfikatora aktualengo gracza.
     * @return identyfikator aktualengo gracza.
     */
    public Integer getCurrentPlayerId() {
        return currentPlayerId;
    }

    /**
     * Getter identyfikatora zawodnika grającego białymi pionami.
     * @return identyfikatora zawodnika grającego białymi pionami.
     */
    public Integer getWhitePlayerId() {
        return whitePlayerId;
    }
    /**
     * Getter identyfikatora zawodnika grającego czarnymi pionami.
     * @return identyfikatora zawodnika grającego czarnymi pionami.
     */
    public Integer getBlackPlayerId() {
        return blackPlayerId;
    }
    /**
     * Getter identyfikatora gry.
     * @return identyfikator gry.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Getter nazwy gry.
     * @return nazwa gry
     */
    public String getGameName() {
        return gameName;
    }

    /**
     * Metoda zwracająca liczbę graczy przypisanych do gry.
     * @return liczba graczy przypisanych do gry.
     */
    public Integer getPlayersCount() {
        if (whitePlayerId == null && blackPlayerId == null) {
            return 0;
        }
        else if ((whitePlayerId != null && blackPlayerId == null) ||
                (whitePlayerId == null && blackPlayerId != null)) {
            return 1;
        }
        else
            return 2;
    }

    /**
     * Getter zmiennej informującej czy gra się rozpoczeła
     * @return zmienna informująca czy gra się rozpoczeła
     */
    public boolean isGameStarted() {
        return gameStarted;
    }

    // ------------------------ SETTERS ------------------------
    /**
     * Setter stanu planszy.
     * @param board Nowy stan planszy w postaci Stringa.
     */
    public void setBoard(String board) {
        this.board = board;
    }

    /**
     * Setter identyfikatora gry.
     * @param id nowy identyfikator gry.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Setter identyfikatora zawodnika grającego piałymi pionami.
     * @param whitePlayerId nowy identyfikator zawodnika grającego piałymi pionami
     */
    public void setWhitePlayerId(Integer whitePlayerId) {
        this.whitePlayerId = whitePlayerId;
    }

    /**
     * Setter identyfikatora zawodnika grającego czarnymi pionami.
     * @param blackPlayerId nowy identyfikator zawodnika grającego czarnymi pionami.
     */
    public void setBlackPlayerId(Integer blackPlayerId) {
        this.blackPlayerId = blackPlayerId;
    }

    /**
     * Setter identyfikatora zawodnika aktualnie wykonującego ruch.
     * @param currentPlayerId identyfikator zawodnika aktualnie wykonującego ruch
     */
    public void setCurrentPlayerId(Integer currentPlayerId) {
        this.currentPlayerId = currentPlayerId;
    }

    /**
     * Setter zmiennej informującej czy gra się rozpoczeła.
     * @param gameStarted zmiennea informująca czy gra się rozpoczeła.
     */
    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    /**
     * Metoda zmieniająca identyfikator zawodnika aktualnie wykonującego ruch na identyfikaotr drugiego z graczy.
     * @return Identyfikator gracza aktualnie wykonującego ruch po zmianach.
     */
    public Integer switchPlayers() {
        if (currentPlayerId.equals(whitePlayerId))
            currentPlayerId = blackPlayerId;
        else
            currentPlayerId = whitePlayerId;

        return currentPlayerId;
    }
}
