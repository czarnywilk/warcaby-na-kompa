package lobby;

import handlers.GameManager;
import main.Main;
import multiplayer.PlaceholderUtility;
import multiplayer.serialized.Game;
import multiplayer.serialized.Player;
import table.TableOnline;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Lobby extends JPanel {

    private static java.util.Timer timer = new Timer();
    private static JTextField player1;
    private static JTextField player2;

    private static void refresh() {
        System.out.println("getting list of players...");

        Game game = GameManager.getGame_sync(GameManager.getUserGame().getId());
        if (game == null) {
            System.out.println("User game is null!");
            return;
        }

        try {
            GameManager.getPlayersFromGame(game);
            GameManager.setServerCallbackListener(new GameManager.ServerCallbackListener() {
                @Override
                public void onServerResponse(Object obj) {
                    if(obj!=null){
                        ArrayList<Player> players = (ArrayList)obj;

                        if (GameManager.getSecondPlayer() == null) {
                            for (Player p : players) {
                                if (!p.getId().equals(GameManager.getUserPlayer().getId())) {
                                    GameManager.setSecondPlayer(p);
                                    if (game.getWhitePlayerId() == null) {
                                        game.setWhitePlayerId(p.getId());
                                        game.setCurrentPlayerId(p.getId());
                                    }
                                    else if (game.getBlackPlayerId() == null) {
                                        game.setBlackPlayerId(p.getId());
                                    }
                                    break;
                                }
                            }
                        }

                        player2.setText(GameManager.getSecondPlayer() == null ?
                                "-- puste --" : GameManager.getSecondPlayer().getPlayerName());

                        // refresh frame
                        Main.getFrame().invalidate();
                        Main.getFrame().validate();
                        Main.getFrame().repaint();

                        GameManager.setUserGame(game);
                        if (game.isGameStarted() && game.getPlayersCount() > 1) {
                            new TableOnline();
                            Main.getFrame().dispose();
                            timer.cancel();
                            timer = new Timer();
                        }
                    }
                }

                @Override
                public void onServerFailed() {
                    System.err.println("Failed to get players!");
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

    public static Timer getTimer() {
        return timer;
    }

    public Lobby(String room){
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(5,5,5,5));

        System.out.println("in lobby: " + room);

        JPanel holder = new JPanel(new GridBagLayout());
        holder.setBorder(new EmptyBorder(5,5,5,5));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(400, 250));
        panel.setBorder(BorderFactory.createTitledBorder(room));

        // ========================= PLAYERS =============================
        player1 = new JTextField(GameManager.getUserPlayer().getPlayerName());
        player1.setBackground(Color.LIGHT_GRAY);
        player1.setHorizontalAlignment(JTextField.CENTER);
        player1.setEditable(false);

        player2 = new JTextField(GameManager.getSecondPlayer() == null ?
                "-- puste --" : GameManager.getSecondPlayer().getPlayerName());
        player2.setBackground(Color.LIGHT_GRAY);
        player2.setHorizontalAlignment(JTextField.CENTER);
        player2.setEditable(false);
        // ===============================================================

        JButton startGame = new JButton("Zacznij grę");
        startGame.setBackground(Color.ORANGE);
        startGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        startGame.addActionListener(e -> {
            //TODO start online game
            if (GameManager.getSecondPlayer() != null) {
                GameManager.getUserGame().setGameStarted(true);
                var updatedGame = GameManager.updateGame_sync(GameManager.getUserGame());
                if (updatedGame != null) {
                    GameManager.setUserGame(updatedGame);
                    new TableOnline();
                    Main.getFrame().dispose();
                    timer.cancel();
                    timer = new Timer();
                }
                else {
                    System.err.println("Failed to start game!");
                }
            }
            else {
                System.err.println("Not enough players!");
            }
        });

        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(player1);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(player2);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(startGame);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        holder.add(panel);
        add(holder, BorderLayout.CENTER);

        setVisible(true);

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (PlaceholderUtility.hasInternetAccess())
                    refresh();
            }
        }, 0, 5000);
    }
}
