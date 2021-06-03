package multiplayer;

import retrofit2.Call;
import retrofit2.http.*;
import multiplayer.serialized.Game;
import multiplayer.serialized.Player;

import java.util.List;

/**
 * Interfejs posiadający deklaracje metod HTTP.
 */
public interface JsonPlaceholderAPI {

    /**
     * Metoda pobierająca obiekt gry.
     * @param gameId ID gry do pobrania.
     * @return Obiekt gry.
     */
    @GET("games/{id}")
    Call<Game> getGame(@Path("id") Integer gameId);

    /**
     * Metoda pobierająca liste wszystkich gier.
     * @return Lista wszystkich gier na serwerze.
     */
    @GET("games")
    Call<List<Game>> getListOfRooms();

    /**
     * Metoda pobierająca z serwera wszystkich graczy należących do danej gry.
     * @param gameId ID gry, której gracze mają zostać pobrani.
     * @return Lista graczy.
     */
    @GET("players")
    Call<List<Player>> getPlayersFromGame(@Query("parentGame") Integer gameId);


    /**
     * Metoda tworząca nowy obiekt gry na serwerze.
     * @param game Gra, która ma zostać utworzona.
     * @return Obiekt gry.
     */
    @POST("games")
    Call<Game> createGame(@Body Game game);
    /**
     * Metoda tworząca nowy obiekt gracza na serwerze.
     * @param player Gracz, który ma zostać utworzony.
     * @return Obiekt gracza.
     */
    @POST("players")
    Call<Player> createPlayer(@Body Player player);


    /**
     * Metoda usuwająca obiekt gry z serwera.
     * @param gameId ID gry, która ma zostać usunięta.
     * @return Obiekt usuniętej gry.
     */
    @DELETE("games/{id}")
    Call<Game> deleteGame (@Path("id") Integer gameId);
    /**
     * Metoda usuwająca obiekt gracza z serwera.
     * @param PlayerId ID gracza, który ma zostać usunięty.
     * @return Obiekt usuniętego gracza.
     */
    @DELETE("players/{id}")
    Call<Player> deletePlayer (@Path("id") Integer PlayerId);


    /**
     * Metoda aktualizująca obiekt gry na serwerze.
      * @param gameId ID gry, która ma zostać zaktualizowana.
     * @param game Obiekt gry do zastąpienia.
     * @return Obiekt gry.
     */
    @PATCH("games/{id}")
    Call<Game> editGame (@Path("id") Integer gameId, @Body Game game);
    /**
     * Metoda aktualizująca gracza.
     * @param playerId ID gracza, który ma zostać zaktualizowany.
     * @param player Obiekt gracza do zastąpienia.
     * @return Obiekt gracza.
     */
    @PATCH("players/{id}")
    Call<Player> editPlayer(@Path("id") Integer playerId, @Body Player player);
}

