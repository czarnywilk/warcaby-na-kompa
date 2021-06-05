package multiplayer.serialized;

import com.google.gson.annotations.SerializedName;

/**
 * Klasa reprezentująca zawodnika.
 */
public class Player {
    /**
     * Nazwa zawodnika.
     */
    @SerializedName("playerName")
    private String playerName;
    /**
     * Identyfikator gry do której przypisany jest gracz.
     */
    @SerializedName("parentGame")
    private Integer gameId = null;
    /**
     * Identyfikator zawodnika.
     */
    private int id;

    /**
     * Konstruktor gracza.
     * @param playerName Nazwa zawodnika.
     * @param gameId Identyfikator gry.
     */
    public Player(String playerName, Integer gameId) {
        this.playerName = playerName;
        this.gameId = gameId;
    }

    /**
     * Getter nazwy zawodnika.
     * @return nazwa zawodnika.
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Getter identyfikatora gry.
     * @return identyfikator gry.
     */
    public Integer getGameId() {
        return gameId;
    }

    /**
     * Getter identyfikatora zawodnika.
     * @return identyfikator zawodnika.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Setter identyfikatora zawodnika.
     * @param id identyfikator zawodnika.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Setter identyfikatra gry.
     * @param gameId nowy identyfikator gry.
     */
    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    /**
     * Setter nazwy zawodnika.
     * @param playerName nowa nazwa zawodnika.
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
