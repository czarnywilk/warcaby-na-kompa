package roomList;

import handlers.GameManager;
import main.Main;
import lobby.Lobby;
import multiplayer.PlaceholderUtility;
import multiplayer.serialized.Game;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class RoomList extends JPanel {

    private static ArrayList<Game> roomList = new ArrayList<>();
    private static JPanel games = new JPanel();
    private static Timer timer = new Timer();

    private static void refresh(){
        System.out.println("getting list of rooms...");

        try {
            roomList.clear();
            GameManager.getGames();
            GameManager.setServerCallbackListener(new GameManager.ServerCallbackListener() {
                @Override
                public void onServerResponse(Object obj) {
                    if(obj!=null){
                        roomList.addAll((ArrayList)obj);
                        setRoomButtons();

                        // refresh frame
                        Main.getFrame().invalidate();
                        Main.getFrame().validate();
                        Main.getFrame().repaint();
                    }
                }

                @Override
                public void onServerFailed() {
                    System.err.println("Failed to refresh room list!");
                }
            });
        }
        catch (ClassCastException cce) {
            System.err.println("Error while casting: " + cce.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public RoomList() {
        super();

        setLayout(new BorderLayout());

        JLabel label = new JLabel("Lista pokoi", SwingConstants.CENTER);
        add(label, BorderLayout.PAGE_START);

        // ============================= ROOMS =================================
        games.setLayout(new BoxLayout(games, BoxLayout.Y_AXIS));

        setRoomButtons();
        JScrollPane scroll = new JScrollPane(games);
        add(scroll, BorderLayout.CENTER);


        // ============================= BOTTOM =================================
        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));

        JTextField roomName = new JTextField();
        roomName.setPreferredSize(new Dimension(200, 20));
        roomName.setHorizontalAlignment(JTextField.CENTER);
        bottom.add(roomName);

        JButton addRoom = new JButton("Stwórz grę");
        addRoom.setAlignmentX(Component.CENTER_ALIGNMENT);
        addRoom.addActionListener(e -> {
            System.out.println("add room: " + roomName.getText());
            String _roomName = roomName.getText();
            if (!_roomName.isEmpty()) {
                createGame(_roomName);
            }
        });
        bottom.add(addRoom);

        add(bottom, BorderLayout.PAGE_END);
        setVisible(true);

        // refresh every 5 sec
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (PlaceholderUtility.hasInternetAccess())
                    refresh();
            }
        }, 0, 5000);
    }

    public static Timer getTimer() {
        return timer;
    }

    private static void setRoomButtons() {

        games.removeAll();

        for (Game g: roomList){
            JButton button = new JButton(g.getGameName());
            button.setSize(new Dimension(200,50));
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.addActionListener(e -> {
                System.out.println("going to room: " + button.getText());
                joinGame(g.getId());
            });
            games.add(button);
        }
    }

    private static void createGame(String name) {
        try {
            Game game = new Game(name);

            int random = new Random().nextInt(100);
            if (random > 50) {
                game.setWhitePlayerId(GameManager.getUserPlayer().getId());
                game.setCurrentPlayerId(GameManager.getUserPlayer().getId());
            } else
                game.setBlackPlayerId(GameManager.getUserPlayer().getId());

            GameManager.createGame_sync(game);
            timer.cancel();
            timer = new Timer();
            Main.loadPanel(new Lobby(game.getGameName()));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void joinGame(Integer gameId) {
        Game _game = GameManager.getGame_sync(gameId);
        if (_game.getPlayersCount() < 2) {
            Integer playerId = GameManager.getUserPlayer().getId();
            if (_game.getWhitePlayerId() == null) {
                _game.setWhitePlayerId(playerId);
                _game.setCurrentPlayerId(playerId);
            } else {
                _game.setBlackPlayerId(playerId);
            }
            GameManager.joinGame_sync(_game);
            Main.loadPanel(new Lobby(_game.getGameName()));
            timer.cancel();
            timer = new Timer();
        }
    }
}
