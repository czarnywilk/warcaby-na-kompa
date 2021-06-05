package handlers;
import multiplayer.PlaceholderUtility;
import multiplayer.serialized.Game;
import multiplayer.serialized.Player;
//import roomlist.RoomList;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Klasa obsługująca komunikacje oraz synchronizacje danych z serwerem.
 */
public class GameManager {

    // -------------------- SERVER LISTENER ----------------
    /**
     * Interfejs nasłuchujący odpowiedzi od serwera.
     */
    public interface ServerCallbackListener {
        void onServerResponse(Object obj);
        void onServerFailed();
    }
    /**
     * Obiekt nasłuchiwacza serwera.
     */
    public static ServerCallbackListener listener;
    /**
     * Setter nasłuchiwacza serwera.
     * @param listener Obiekt nasłuchiwacza serwera.
     */
    public static void setServerCallbackListener(ServerCallbackListener listener){
        GameManager.listener = listener;
    }
    // -----------------------------------------------------

    /**
     * Obiekt gracza sterowany przez lokalnego użytkownika.
     */
    private static Player userPlayer;
    /**
     * Obiekt gracza sterowany przez przeciwnika.
     */
    private static Player secondPlayer;
    /**
     * Obiekt gry, w której uczestniczy gracz
     */
    private static Game userGame;

    // ------------------- GETTERS -----------------------
    /**
     * Metoda pobierająca obiekt gry z serwera w sposób asynchroniczny.
     * @param game Gra, która ma zostać pobrana.
     */
    public static void getGame (Game game) {
        Call<Game> call = PlaceholderUtility.getPlaceholderInstance().getGame(game.getId());

        call.enqueue(new Callback<Game>() {
            @Override
            public void onResponse(Call<Game> call, Response<Game> response) {
                if (!response.isSuccessful()) {
                    listener.onServerFailed();
                    return;
                }

                listener.onServerResponse(response.body());
            }

            @Override
            public void onFailure(Call<Game> call, Throwable t) {
                listener.onServerFailed();
            }
        });
    }
    /**
     * Metoda pobierająca obiekt gry z serwera.
     * @param gameId ID gry, która ma zostać pobrana.
     */
    public static Game getGame_sync (Integer gameId) {
        Call<Game> call = PlaceholderUtility.getPlaceholderInstance().getGame(gameId);

        final Game[] _game = new Game[1];

        try {
            Runnable runnable = () -> {
                try {
                    Response<Game> response = call.execute();

                    _game[0] = response.body();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            };
            Thread thread = new Thread(runnable);
            thread.start(); // spawn thread
            thread.join(); // wait for thread to finish
        }
        catch (InterruptedException ie) {
            ie.printStackTrace();
        }

        return _game[0];
    }
    /**
     * Metoda pobierająca wszytskie gry z serwera w sposób asynchroniczny.
     */
    public static void getGames () {
        Call<List<Game>> call = PlaceholderUtility.getPlaceholderInstance().getListOfRooms();

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Game>> call, Response<List<Game>> response) {
                if (!response.isSuccessful()) {
                    listener.onServerFailed();
                    return;
                }
                listener.onServerResponse(response.body());
            }

            @Override
            public void onFailure(Call<List<Game>> call, Throwable t) {
                listener.onServerFailed();
            }
        });
    }
    /**
     * Metoda pobierająca z serwera w sposób asynchroniczny wszystkich graczy należących do danej gry.
     * @param game Gra, której gracze mają zostać pobrani.
     */
    public static void getPlayersFromGame (Game game) {
        Call<List<Player>> call = PlaceholderUtility.getPlaceholderInstance().getPlayersFromGame(game.getId());
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Player>> call, Response<List<Player>> response) {
                if (!response.isSuccessful()) {
                    listener.onServerFailed();
                    return;
                }
                listener.onServerResponse(response.body());
            }

            @Override
            public void onFailure(Call<List<Player>> call, Throwable t) {
                listener.onServerFailed();
            }
        });
    }

    /**
     * Getter lokalnego gracza.
     * @return Obiekt gracza.
     */
    public static Player getUserPlayer() {
        return userPlayer;
    }
    /**
     * Getter przeciwnika.
     * @return Obiekt gracza.
     */
    public static Player getSecondPlayer() {
        return secondPlayer;
    }
    /**
     * Getter obecnej gry.
     * @return Obiekt gry.
     */
    public static Game getUserGame() {
        return userGame;
    }

    // ------------------- SETTERS -----------------------
    /**
     * Setter lokanego gracza.
     * @param userPlayer Obiekt gracza.
     */
    public static void setUserPlayer(Player userPlayer) {
        GameManager.userPlayer = userPlayer;
    }
    /**
     * Setter obecnej gry.
     * @param userGame Obiekt gry.
     */
    public static void setUserGame(Game userGame) {
        GameManager.userGame = userGame;
    }
    /**
     * Setter przeciwnika.
     * @param secondPlayer Obiekt gracza.
     */
    public static void setSecondPlayer(Player secondPlayer) {
        GameManager.secondPlayer = secondPlayer;
    }

    // -------------------- EDIT -------------------------
    /**
     * Metoda aktualizująca obiekt gry na serwerze w sposób asynchroniczny.
     * @param game Obiekt gry do zastąpienia.
     */
    public static void updateGame (Game game) {
        Call<Game> call = PlaceholderUtility.getPlaceholderInstance().
                editGame(game.getId(), game);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Game> call, Response<Game> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                listener.onServerResponse(null);
            }

            @Override
            public void onFailure(Call<Game> call, Throwable t) {
                listener.onServerFailed();
            }
        });
    }
    /**
     * Metoda aktualizująca obiekt gry na serwerze.
     * @param game Obiekt gry do zastąpienia.
     * @return Obiekt zaktualizowanej gry.
     */
    public static Game updateGame_sync (Game game) {
        Call<Game> call = PlaceholderUtility.getPlaceholderInstance().
                editGame(game.getId(), game);

        final Game[] games = {null};

        try {
            Runnable runnable = () -> {
                try {
                    Response<Game> response = call.execute();
                    games[0] = response.body();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            };
            Thread thread = new Thread(runnable);
            thread.start(); // spawn thread
            thread.join(); // wait for thread to finish
        }
        catch (InterruptedException ie) {
            ie.printStackTrace();
        }

        return games[0];
    }
    /**
     * Metoda dołączająca lokalnego użytkownika do gry w sposób asynchroniczny.
     * @param game Gra, do której chcemy dołączyć.
     */
    public static void joinGame (Game game) {
        Call<Game> call = PlaceholderUtility.getPlaceholderInstance().
                editGame(game.getId(), game);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Game> call, Response<Game> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                Game gameResponse = response.body();
                assert gameResponse != null;

                GameManager.setUserGame(gameResponse);
                GameManager.userPlayer.setGameId(gameResponse.getId());
                GameManager.updatePlayer(userPlayer, false);
            }

            @Override
            public void onFailure(Call<Game> call, Throwable t) {
            }
        });
    }
    /**
     * Metoda dołączająca lokalnego użytkownika do gry.
     * @param game Gra, do której chcemy dołączyć.
     */
    public static void joinGame_sync(Game game) {
        Call<Game> call = PlaceholderUtility.getPlaceholderInstance().
                editGame(game.getId(), game);

        try {
            Runnable runnable = () -> {
                try {
                    Response<Game> response = call.execute();

                    Game gameResponse = response.body();
                    assert gameResponse != null;

                    setUserGame(gameResponse);
                    userPlayer.setGameId(gameResponse.getId());
                    updatePlayer_sync(userPlayer);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            };
            Thread thread = new Thread(runnable);
            thread.start(); // spawn thread
            thread.join(); // wait for thread to finish
        }
        catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }
    /**
     * Metoda aktualizująca gracza w sposób asynchroniczny.
     * @param updatedPlayer Obiekt gracza do zastąpienia.
     * @param setResponse Jeśli prawdziwe, nasłuchiwacz wywoła metodę
     *                    informującą o sukcesie odebrania odpowiedzi od serwera
     */
    public static void updatePlayer (Player updatedPlayer, boolean setResponse) {
        Call<Player> call = PlaceholderUtility.getPlaceholderInstance().
                editPlayer(updatedPlayer.getId(), updatedPlayer);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Player> call, Response<Player> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                if (setResponse)
                    listener.onServerResponse(null);
            }

            @Override
            public void onFailure(Call<Player> call, Throwable t) {
                //listener.onServerFailed();
            }
        });
    }
    /**
     * Metoda aktualizująca gracza.
     * @param updatedPlayer Obiekt gracza do zastąpienia.
     */
    public static void updatePlayer_sync (Player updatedPlayer) {
        Call<Player> call = PlaceholderUtility.getPlaceholderInstance().
                editPlayer(updatedPlayer.getId(), updatedPlayer);

        try {
            Runnable runnable = () -> {
                try {
                    call.execute();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            };
            Thread thread = new Thread(runnable);
            thread.start(); // spawn thread
            thread.join(); // wait for thread to finish
        }
        catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    // ------------------- CREATE ------------------------
    /**
     * Metoda tworząca nowy obiekt gry na serwerze w sposób asynchroniczny.
     * @param game Gra, która ma zostać utworzona.
     */
    public static void createGame (Game game) {
        Call<Game> call = PlaceholderUtility.getPlaceholderInstance().createGame(game);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Game> call, Response<Game> response) {
                if (!response.isSuccessful()) {
                    listener.onServerFailed();
                    return;
                }

                Game gameResponse = response.body();
                assert gameResponse != null;

                game.setId(gameResponse.getId());
                game.setWhitePlayerId(gameResponse.getWhitePlayerId());
                game.setBlackPlayerId(gameResponse.getBlackPlayerId());

                GameManager.setUserGame(gameResponse);
                GameManager.userPlayer.setGameId(gameResponse.getId());
                GameManager.updatePlayer(userPlayer, false);

                listener.onServerResponse(null);
            }

            @Override
            public void onFailure(Call<Game> call, Throwable t) {
                listener.onServerFailed();
            }
        });
    }
    /**
     * Metoda tworząca nowy obiekt gry na serwerze.
     * @param game Gra, która ma zostać utworzona.
     */
    public static void createGame_sync (Game game) {
        Call<Game> call = PlaceholderUtility.getPlaceholderInstance().createGame(game);

        try {
            Runnable runnable = () -> {
                try {
                    Response<Game> response = call.execute();

                    Game gameResponse = response.body();
                    assert gameResponse != null;

                    game.setId(gameResponse.getId());
                    game.setWhitePlayerId(gameResponse.getWhitePlayerId());
                    game.setBlackPlayerId(gameResponse.getBlackPlayerId());

                    GameManager.setUserGame(gameResponse);
                    GameManager.userPlayer.setGameId(gameResponse.getId());
                    GameManager.updatePlayer(userPlayer, false);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            };
            Thread thread = new Thread(runnable);
            thread.start(); // spawn thread
            thread.join(); // wait for thread to finish
        }
        catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }
    /**
     * Metoda tworząca nowy obiekt gracza na serwerze w sposób asynchroniczny.
     * @param player Gracz, który ma zostać utworzony.
     */
    public static void createPlayer(Player player) {
        Call<Player> call = PlaceholderUtility.getPlaceholderInstance().createPlayer(player);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Player> call, Response<Player> response) {
                if (!response.isSuccessful()) {
                    listener.onServerFailed();
                    return;
                }
                Player playerResponse = response.body();
                assert playerResponse != null;

                player.setId(playerResponse.getId());
                listener.onServerResponse(null);
            }

            @Override
            public void onFailure(Call<Player> call, Throwable t) {
                listener.onServerFailed();
            }
        });
    }
    /**
     * Metoda tworząca nowy obiekt gracza na serwerze.
     * @param player Gracz, który ma zostać utworzony.
     */
    public static boolean createPlayer_sync(Player player) {
        Call<Player> call = PlaceholderUtility.getPlaceholderInstance().createPlayer(player);
        final boolean[] success = {false};
        try {
            Runnable runnable = () -> {
                try {
                    Response<Player> response = call.execute();

                    Player playerResponse = response.body();
                    if (playerResponse != null) {
                        player.setId(playerResponse.getId());
                        success[0] = true;
                    }
                    else
                        System.out.println("Failed to create player!!");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            };
            Thread thread = new Thread(runnable);
            thread.start(); // spawn thread
            thread.join(); // wait for thread to finish
        }
        catch (InterruptedException ie) {
            ie.printStackTrace();
        }

        return success[0];
    }

    //-------------------- DELETE ------------------------
    /**
     * Metoda usuwająca obiekt gracza z serwera.
     * @param deletePlayerId ID gracza, który ma zostać usunięty.
     */
    public static void deletePlayer_sync (Integer deletePlayerId) {

        Call<Player> call = PlaceholderUtility.getPlaceholderInstance().
                deletePlayer(deletePlayerId);


        try {
            Runnable runnable = () -> {
                try {
                    call.execute();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            };
            Thread thread = new Thread(runnable);
            thread.start(); // spawn thread
            thread.join(); // wait for thread to finish
        }
        catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }
    /**
     * Metoda usuwająca obiekt gry z serwera.
     * @param gameId ID gry, która ma zostać usunięta.
     */
    public static void deleteGame_sync (Integer gameId) {

        Call<Game> call = PlaceholderUtility.getPlaceholderInstance().deleteGame(gameId);

        try {
            Runnable runnable = () -> {
                try {
                    call.execute();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            };
            Thread thread = new Thread(runnable);
            thread.start(); // spawn thread
            thread.join(); // wait for thread to finish
        }
        catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    // -------------------- QUIT -------------------------
    /**
     * Metoda obsługująca wyjście lokalnego gracza z obecnej gry.
     * @param deletePlayerOnQuit Jeśli prawdziwe, lokalny gracz zostanie
     *                           usunięty z serwera (np. gry gracz wyłączy aplikację)
     */
    public static void quitGame(boolean deletePlayerOnQuit) {
        try {
            Game game = getGame_sync(getUserGame().getId());
            Integer playerId = getUserPlayer().getId();

            if (game != null) {
                if (game.getPlayersCount() < 2) {
                    if (deletePlayerOnQuit)
                        deletePlayer_sync(playerId);
                    else {
                        getUserPlayer().setGameId(null);
                        updatePlayer_sync(getUserPlayer());
                    }
                    deleteGame_sync(game.getId());
                } else {
                    if (deletePlayerOnQuit)
                        deletePlayer_sync(playerId);
                    else {
                        getUserPlayer().setGameId(null);
                        updatePlayer_sync(getUserPlayer());
                    }

                    // edit game: set null(s) in game
                    if (game.getWhitePlayerId() != null &&
                            game.getWhitePlayerId().equals(playerId)) {
                        game.setWhitePlayerId(null);
                    } else if (game.getBlackPlayerId() != null &&
                               game.getBlackPlayerId().equals(playerId)) {
                        game.setBlackPlayerId(null);
                    }

                    if (game.getCurrentPlayerId() != null &&
                            game.getCurrentPlayerId().equals(playerId)) {
                        game.setCurrentPlayerId(null);
                    }

                    game.setGameStarted(false);
                    updateGame_sync(game);
                }
            } else if (deletePlayerOnQuit) {
                deletePlayer_sync(playerId);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}


