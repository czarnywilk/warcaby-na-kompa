package multiplayer;

import retrofit2.Call;
import retrofit2.http.*;
import multiplayer.serialized.Game;
import multiplayer.serialized.Player;

import java.util.List;

public interface JsonPlaceholderAPI {

    @GET("games/{id}")
    Call<Game> getGame(@Path("id") Integer gameId);
    @GET("games")
    Call<List<Game>> getListOfRooms();
    @GET("players")
    Call<List<Player>> getPlayersFromGame(@Query("parentGame") Integer gameId);


    @POST("games")
    Call<Game> createGame(@Body Game game);
    @POST("players")
    Call<Player> createPlayer(@Body Player player);


    @DELETE("games/{id}")
    Call<Game> deleteGame (@Path("id") Integer gameId);
    @DELETE("players/{id}")
    Call<Player> deletePlayer (@Path("id") Integer PlayerId);


    @PATCH("games/{id}")
    Call<Game> editGame (@Path("id") Integer gameId, @Body Game game);
    @PATCH("players/{id}")
    Call<Player> editPlayer(@Path("id") Integer playerId, @Body Player player);
}

