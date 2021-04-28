import handlers.GameManager;
import multiplayer.PlaceholderUtility;
import multiplayer.serialized.Game;
import multiplayer.serialized.Player;
import table.Table;
import table.TableOnline;

import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main (String[] args) {

        PlaceholderUtility.initialize();

        Scanner s = new Scanner(System.in);
        System.out.print("Player name: ");
        String playerName = s.nextLine();

        createPlayer(playerName);

        System.out.println("Create game (y/n) ");
        String cg = s.nextLine();
        if (cg.equals("y") || cg.equals("Y")) {
            String gameName = s.nextLine();
            createGame(gameName);
        }
        else {
            System.out.println("Game ID: ");
            Integer gameId = s.nextInt();
            joinGame(gameId);
        }

        String start = "";
        while (!start.equals("start")) {
            System.out.println("Type \"start\" to start game: ");
            start = s.nextLine();
        }


        TableOnline table = new TableOnline();

    }

    // ================== FOR TESTING ============================
    static void createPlayer(String name) {
        GameManager.setUserPlayer(new Player(name, null));
        GameManager.createPlayer(GameManager.getUserPlayer());
        GameManager.setServerCallbackListener(new GameManager.ServerCallbackListener() {
            @Override
            public void onServerResponse(Object obj) {
                System.out.println("Player created!");
            }

            @Override
            public void onServerFailed() {
                System.err.println("Connection failed!");
            }
        });
    }

    static void createGame(String name) {
        Game game = new Game(name);

        /*int random = new Random().nextInt(100);
        if(random > 50) {
            game.setWhitePlayerId(GameManager.getUserPlayer().getId());
            game.setCurrentPlayerId(GameManager.getUserPlayer().getId());
        }
        else*/
            game.setBlackPlayerId(GameManager.getUserPlayer().getId());

        GameManager.createGame(game);
        GameManager.setServerCallbackListener(new GameManager.ServerCallbackListener() {
            @Override
            public void onServerResponse(Object obj) {
                System.out.println("Game created!");
            }

            @Override
            public void onServerFailed() {
                System.err.println("Connection failed!");
            }
        });
    }

    static void joinGame(Integer gameId) {
        GameManager.getGame(gameId);
        GameManager.setServerCallbackListener(new GameManager.ServerCallbackListener() {
            @Override
            public void onServerResponse(Object obj) {
                try {
                    Game _game = (Game) obj;
                    if (_game.getPlayersCount() < 2) {
                        if (_game.getCurrentPlayerId() == null) {
                            _game.setWhitePlayerId(GameManager.getUserPlayer().getId());
                            _game.setCurrentPlayerId(GameManager.getUserPlayer().getId());
                        } else {
                            _game.setBlackPlayerId(GameManager.getUserPlayer().getId());
                        }
                        GameManager.joinGame(_game);
                    }
                }
                catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
            @Override
            public void onServerFailed() {
                System.err.println("Connection failed!");
            }
        });
    }

}
