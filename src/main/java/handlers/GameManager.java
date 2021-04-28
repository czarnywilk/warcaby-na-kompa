package handlers;
//import lobby.Lobby;
import multiplayer.PlaceholderUtility;
import multiplayer.serialized.Game;
import multiplayer.serialized.Player;
//import roomlist.RoomList;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GameManager {

    // -------------------- SERVER LISTENER ----------------
    public interface ServerCallbackListener {
        void onServerResponse(Object obj);
        void onServerFailed();
    }
    public static ServerCallbackListener listener;
    public static void setServerCallbackListener(ServerCallbackListener listener){
        GameManager.listener = listener;
    }
    // -----------------------------------------------------

    private static Player userPlayer;
    private static Player secondPlayer;
    private static Game userGame;

    /*private static Context mContext; // memory leak :(
    public static void setContext(Context context) {
        mContext = context;
        listener = new ServerCallbackListener() {
            @Override
            public void onServerResponse(Object obj) {

            }

            @Override
            public void onServerFailed() {

            }
        };
    }*/

    // ------------------- GETTERS -----------------------
    public static void getGame (Game game) {
        Call<Game> call = PlaceholderUtility.getPlaceholderInstance().getGame(game.getId());

        call.enqueue(new Callback<Game>() {
            @Override
            public void onResponse(Call<Game> call, Response<Game> response) {
                if (!response.isSuccessful()) {
                    //Toast.makeText(mContext,"Connection unsuccessful!", Toast.LENGTH_SHORT).show();
                    return;
                }

                listener.onServerResponse(response.body());
            }

            @Override
            public void onFailure(Call<Game> call, Throwable t) {
                //Toast.makeText(mContext,"Connection failed!", Toast.LENGTH_SHORT).show();
                listener.onServerFailed();
            }
        });
    }
    public static void getGame (Integer gameId) {
        Call<Game> call = PlaceholderUtility.getPlaceholderInstance().getGame(gameId);

        call.enqueue(new Callback<Game>() {
            @Override
            public void onResponse(Call<Game> call, Response<Game> response) {
                if (!response.isSuccessful()) {
                    //Toast.makeText(mContext,"Connection unsuccessful!", Toast.LENGTH_SHORT).show();
                    return;
                }

                listener.onServerResponse(response.body());
            }

            @Override
            public void onFailure(Call<Game> call, Throwable t) {
                //Toast.makeText(mContext,"Connection failed!", Toast.LENGTH_SHORT).show();
                listener.onServerFailed();
            }
        });
    }
    public static void getGames () {
        Call<List<Game>> call = PlaceholderUtility.getPlaceholderInstance().getListOfRooms();

        call.enqueue(new Callback<List<Game>>() {
            @Override
            public void onResponse(Call<List<Game>> call, Response<List<Game>> response) {
                if (!response.isSuccessful()) {
                    //Toast.makeText(mContext,"Connection unsuccessful!", Toast.LENGTH_SHORT).show();
                    return;
                }
                listener.onServerResponse(response.body());
            }

            @Override
            public void onFailure(Call<List<Game>> call, Throwable t) {
                //Toast.makeText(mContext,"Connection failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public static void getPlayersFromGame (Game game) {
        Call<List<Player>> call = PlaceholderUtility.getPlaceholderInstance().getPlayersFromGame(game.getId());
        call.enqueue(new Callback<List<Player>>() {
            @Override
            public void onResponse(Call<List<Player>> call, Response<List<Player>> response) {
                if (!response.isSuccessful()) {
                    //Toast.makeText(mContext,"Connection unsuccessful!", Toast.LENGTH_SHORT).show();
                    return;
                }
                listener.onServerResponse(response.body());
            }

            @Override
            public void onFailure(Call<List<Player>> call, Throwable t) {
                //Toast.makeText(mContext,"Connection failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static Player getUserPlayer() {
        return userPlayer;
    }
    public static Player getSecondPlayer() {
        return secondPlayer;
    }
    public static Game getUserGame() {
        return userGame;
    }
    /*public static Context getContext() {
        return mContext;
    }*/

    // ------------------- SETTERS -----------------------
    public static void setUserPlayer(Player userPlayer) {
        GameManager.userPlayer = userPlayer;
    }
    public static void setUserGame(Game userGame) {
        GameManager.userGame = userGame;
    }
    public static void setSecondPlayer(Player secondPlayer) {
        GameManager.secondPlayer = secondPlayer;
    }

    // -------------------- EDIT -------------------------
    public static void updateGame (Game game) {
        Call<Game> call = PlaceholderUtility.getPlaceholderInstance().
                editGame(game.getId(), game);

        call.enqueue(new Callback<Game>() {
            @Override
            public void onResponse(Call<Game> call, Response<Game> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                //listener.onServerResponse(null);
            }

            @Override
            public void onFailure(Call<Game> call, Throwable t) {
                //listener.onServerFailed();
            }
        });
    }
    public static void joinGame (Game game) {
        Call<Game> call = PlaceholderUtility.getPlaceholderInstance().
                editGame(game.getId(), game);

        call.enqueue(new Callback<Game>() {
            @Override
            public void onResponse(Call<Game> call, Response<Game> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                //Toast.makeText(mContext,"Joined Game!", Toast.LENGTH_SHORT).show();

                Game gameResponse = response.body();
                assert gameResponse != null;

                GameManager.setUserGame(gameResponse);
                GameManager.userPlayer.setGameId(gameResponse.getId());
                GameManager.updatePlayer(userPlayer, false);

                //TODO !!!!!!!!!!!!!!!!
                //RoomList.removeRefreshCallbacks();
                //mContext.startActivity(new Intent(mContext, Lobby.class));

                //listener.onServerResponse(null);
            }

            @Override
            public void onFailure(Call<Game> call, Throwable t) {
                //listener.onServerFailed();
            }
        });
    }
    public static void updatePlayer (Player updatedPlayer, boolean setResponse) {
        Call<Player> call = PlaceholderUtility.getPlaceholderInstance().
                editPlayer(updatedPlayer.getId(), updatedPlayer);

        call.enqueue(new Callback<Player>() {
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

    // ------------------- CREATE ------------------------
    public static void createGame (Game game) {
        Call<Game> call = PlaceholderUtility.getPlaceholderInstance().createGame(game);

        call.enqueue(new Callback<Game>() {
            @Override
            public void onResponse(Call<Game> call, Response<Game> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                //Toast.makeText(mContext,"Game created!", Toast.LENGTH_SHORT).show();

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
    public static void createPlayer(Player player) {
        Call<Game> call = PlaceholderUtility.getPlaceholderInstance().createPlayer(player);
        call.enqueue(new Callback<Game>() {
            @Override
            public void onResponse(Call<Game> call, Response<Game> response) {

                if (!response.isSuccessful()) {
                    return;
                }
                //Toast.makeText(mContext,"Player created!", Toast.LENGTH_SHORT).show();
                player.setId(response.body().getId());
                listener.onServerResponse(null);
            }

            @Override
            public void onFailure(Call<Game> call, Throwable t) {
                listener.onServerFailed();
            }
        });
    }

    //-------------------- DELETE ------------------------
    public static void deletePlayer (Integer deletePlayerId) {

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
    public static void deleteGame (Integer gameId) {

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
    public static void quitGame(boolean deletePlayerOnQuit) {
        Game game = getUserGame();
        Integer playerId = getUserPlayer().getId();

        if (game != null) {
            if (getSecondPlayer() == null) {
                if (deletePlayerOnQuit) {
                    System.out.println("1");
                    deleteGame(game.getId());
                    System.out.println("2");
                    deletePlayer(playerId);
                    System.out.println("3");
                }
                else {
                    System.out.println("4");
                    deleteGame(game.getId());
                    System.out.println("5");
                }
            }
            else {
                if (deletePlayerOnQuit)
                    deletePlayer(playerId);
                else {
                    getUserPlayer().setGameId(null);
                    updatePlayer(getUserPlayer(), false);
                }

                // edit game: set null(s) in game
                if (game.getWhitePlayerId().equals(playerId)) {
                    game.setWhitePlayerId(null);
                }
                else if (game.getBlackPlayerId().equals(playerId)) {
                    game.setBlackPlayerId(null);
                }

                if (game.getCurrentPlayerId().equals(playerId)) {
                    game.setCurrentPlayerId(null);
                }

                game.setGameStarted(false);
                updateGame(game);
            }
        }
        else if (deletePlayerOnQuit) {
            System.out.println("6");
            deletePlayer(playerId);
            System.out.println("7");
        }
    }
}


