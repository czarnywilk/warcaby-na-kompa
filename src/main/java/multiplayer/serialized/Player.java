package multiplayer.serialized;

import com.google.gson.annotations.SerializedName;

public class Player {

    @SerializedName("playerName")
    private String playerName;
    @SerializedName("parentGame")
    private Integer gameId = null;
    private int id;

    public Player(String playerName, Integer gameId) {
        this.playerName = playerName;
        this.gameId = gameId;
    }

    public String getPlayerName() {
        return playerName;
    }
    public Integer getGameId() {
        return gameId;
    }
    public Integer getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }
    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
