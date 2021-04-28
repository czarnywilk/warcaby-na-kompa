package multiplayer.serialized;

import com.google.gson.annotations.SerializedName;
import table.TableOnline;

public class Game {

    @SerializedName("board")
    private String board;
    @SerializedName("currentPlayerId")
    private Integer currentPlayerId = null;
    @SerializedName("whitePlayerId")
    private Integer whitePlayerId = null;
    @SerializedName("blackPlayerId")
    private Integer blackPlayerId = null;
    @SerializedName("gameName")
    private String gameName;
    @SerializedName("gameStarted")
    private boolean gameStarted = false;
    private int id;


    public Game(String gameName) {
        board = TableOnline.cleanBoard;
        this.gameName = gameName;
    }

    // ------------------------ GETTERS ------------------------
    public String getBoard() {
        return board;
    }
    public Integer getCurrentPlayerId() {
        return currentPlayerId;
    }
    public Integer getWhitePlayerId() {
        return whitePlayerId;
    }
    public Integer getBlackPlayerId() {
        return blackPlayerId;
    }
    public Integer getId() {
        return id;
    }
    public String getGameName() {
        return gameName;
    }
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
    public boolean isGameStarted() {
        return gameStarted;
    }

    // ------------------------ SETTERS ------------------------
    public void setBoard(String board) {
        this.board = board;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setWhitePlayerId(Integer whitePlayerId) {
        this.whitePlayerId = whitePlayerId;
    }
    public void setBlackPlayerId(Integer blackPlayerId) {
        this.blackPlayerId = blackPlayerId;
    }
    public void setCurrentPlayerId(Integer currentPlayerId) {
        this.currentPlayerId = currentPlayerId;
    }
    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    public Integer switchPlayers() {
        if (currentPlayerId.equals(whitePlayerId))
            currentPlayerId = blackPlayerId;
        else
            currentPlayerId = whitePlayerId;

        return currentPlayerId;
    }
}
