package main;

import handlers.GameManager;
import lobby.Lobby;
import multiplayer.PlaceholderUtility;
import multiplayer.serialized.Player;
import roomList.RoomList;
import table.Table;
import table.TableOnline;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static JFrame frame;
    private static JPanel currentPanel;

    public static void main (String[] args) {

        PlaceholderUtility.initialize();

        createMainFrame(createMainPanel());
    }

    // ==================== METHODS ============================
    static boolean createPlayer(String name) {
        GameManager.setUserPlayer(new Player(name, null));
        return GameManager.createPlayer_sync(GameManager.getUserPlayer());
    }

    private static JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(2, 3, 2, 3));
        JPanel layout = new JPanel(new GridBagLayout());
        layout.setBorder(new EmptyBorder(5, 5, 5, 5));
        JPanel btnPanel = new JPanel(new GridLayout(3, 1, 10, 5));

        JTextField nickname = new JTextField();

        JButton singleplayer = new JButton("Local Game");
        singleplayer.addActionListener(e -> {
            System.out.println("entering local game...");
            Table t = new Table();
            frame.dispose();
        });

        JButton multiplayer = new JButton("Online Game");
        multiplayer.addActionListener(e -> {
            System.out.println("going to roomList...");
            String nick = nickname.getText();
            if (!nick.isEmpty()) {
                if (createPlayer(nick))
                    loadPanel(new RoomList());
            }
        });

        btnPanel.add(nickname);
        btnPanel.add(singleplayer);
        btnPanel.add(multiplayer);

        layout.add(btnPanel);
        panel.add(layout, BorderLayout.CENTER);

        return panel;
    }
    public static JFrame getFrame() {
        return frame;
    }

    public static void loadPanel(JPanel panel) {
        if (currentPanel != null) {
            currentPanel.setVisible(false);
            frame.remove(currentPanel);
        }
        if (frame != null)
            frame.add(currentPanel = panel);
    }
    public static void createMainFrame(JPanel panel) {
        frame = new JFrame("Warcaby");

        frame.add(currentPanel = panel);

        frame.setLocationByPlatform(true);
        frame.setSize(WIDTH, HEIGHT);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                try {
                    System.out.println("Closed");
                    e.getWindow().dispose();

                    RoomList.getTimer().cancel();
                    Lobby.getTimer().cancel();
                    TableOnline.getTimer().cancel();

                    if (GameManager.getUserPlayer() != null)
                        GameManager.quitGame(true);
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
                System.exit(0);
            }
        });
    }
}
