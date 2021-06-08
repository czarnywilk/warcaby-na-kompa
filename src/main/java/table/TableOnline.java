package table;

import handlers.GameManager;
import lobby.Lobby;
import main.Main;
import multiplayer.PlaceholderUtility;
import multiplayer.serialized.Game;
import roomList.RoomList;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
/**
 * Klasa reprezentująca plansze w grze multiplayer.
 */
public class TableOnline extends JFrame {
    /**
     * Zmienna informująca czy został wybrany pion.
     */
    boolean select = false;
    /**
     * Zmienna informująca czy możliwe jest bicie.
     */
    boolean hit = false;
    /**
     * Tablica przycisków umożliwiająca oddziaływanie na piony na  polu planszy
     */
    public JButton[] buttons = new JButton[32];
    /**
     * Tablica z polami planszy
     */
    private static Field[] tablica = new Field[32];
    /**
     * Pole planszy które zostało wybrane.
     */
    private Field selectField = new Field();
    /**
     * Pole planszy na które ma zostać przeniesiony pion.
     */
    private Field targetField = new Field();
    /**
     * Ikona białego piona.
     */
    private ImageIcon pwhite;
    /**
     * Ikona białej damki.
     */
    private ImageIcon qwhite;
    /**
     * Ikona czarnego piona.
     */
    private ImageIcon pblack;
    /**
     * Ikona czarnej damki.
     */
    private ImageIcon qblack;
    /**
     * Kolor planszy jako zmienna znakowa.
     */
    String tableColor = "#03A9F4";
    /**
     * Stan początkowej planszy w postaci ciągu znaków.
     */
    public static String cleanBoard = "11111111000000000000000022222222";
    /**
     * Numer aktualengo gracza
     */
    private int aktualnyGracz;
    /**
     * Klucz gracza
     */
    private int kluczGracza;
    /**
     * Obiekt klasy wywołującej zadany blok instrukcji po odliczeniu podanej ilości sekund.
     */
    private static Timer timer = new Timer();
    /**
     * Metoda ładująca ikony.
     */
    private void loadImages() {
        try {
            BufferedImage pawn_white = ImageIO.read(getClass().getResource("/res/images/pawn_white.png"));
            BufferedImage pawn_black = ImageIO.read(getClass().getResource("/res/images/pawn_black.png"));
            BufferedImage queen_white = ImageIO.read(getClass().getResource("/res/images/white_queen.png"));
            BufferedImage queen_black = ImageIO.read(getClass().getResource("/res/images/black_queen.png"));

            pwhite = new ImageIcon(pawn_white);
            qwhite = new ImageIcon(queen_white);
            pblack = new ImageIcon(pawn_black);
            qblack = new ImageIcon(queen_black);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    /**
     * Konstruktor tworzący planszę w grze online
     */
    public TableOnline() {
        loadImages();

        try {
            int licznik = 0;
            setSize(900, 900);
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 4; j++) {
                    tablica[i * 4 + j] = new Field(j * 2 + ((i % 2)) + 1, i + 1, 0);
                    //else tablica[i*4 + j] = new Field(j*2+((i%2)), i+1, 0);
                    if (i < 2) tablica[i * 4 + j].setPawn(1);
                    else if (i > 5) tablica[i * 4 + j].setPawn(2);
                }
            }
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if ((j % 2 == 0 && i % 2 == 0) || (i % 2 == 1 && j % 2 == 1)) {
                        buttons[licznik] = new JButton();
                        buttons[licznik].setFocusable(false);
                        buttons[licznik].setBounds(100 * j + 40, 100 * (7 - i) + 35, 100, 100);
                        buttons[licznik].setBackground(Color.decode(tableColor));
                        buttons[licznik].setName(Integer.toString(licznik));
                        buttons[licznik].addActionListener(e -> {
                            JButton a = (JButton) e.getSource();
                            System.out.println(a.getName());
                            buttonClicked(Integer.parseInt(a.getName()));
                        });
                        add(buttons[licznik]);
                        licznik++;
                    }
                }
            }
            setBackground(Color.WHITE);
            setLayout(null);
            setVisible(true);

            Integer playerId = GameManager.getUserPlayer().getId();
            Integer whiteId = GameManager.getUserGame().getWhitePlayerId();
            Integer currentId = GameManager.getUserGame().getCurrentPlayerId();
            aktualnyGracz =  currentId.equals(whiteId) ? 1 : 2;
            kluczGracza = playerId.equals(whiteId) ? 1 : 2;

            // refresh every 3 seconds
            if (aktualnyGracz != kluczGracza) {
                waitForTurn();
            }

            wyswietlPlansze();

            addWindowListener(new WindowAdapter()
            {
                @Override
                public void windowClosing(WindowEvent e)
                {
                    try {
                        close(true);
                    }
                    catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            });
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    /**
     * Metoda wyświetlająca planszę.
     */
    private void wyswietlPlansze() {
        for (int i = 0; i < 32; i++){
            if (kluczGracza == 1){
                if (tablica[i].getPawn() == 1) {
                    buttons[i].setIcon(pwhite);
                    buttons[i].setBackground(Color.decode(tableColor));
                }
                else if (tablica[i].getPawn() == 2) {
                    buttons[i].setIcon(pblack);
                    buttons[i].setBackground(Color.decode(tableColor));
                }
                else if (tablica[i].getPawn() == 0) {
                    buttons[i].setForeground(Color.decode(tableColor));
                    buttons[i].setBackground(Color.decode(tableColor));
                    buttons[i].setIcon(null);
                }
                else if (tablica[i].getPawn() == 3) {
                    buttons[i].setIcon(qwhite);
                    buttons[i].setBackground(Color.decode(tableColor));
                }
                else{
                    buttons[i].setIcon(qblack);
                    buttons[i].setBackground(Color.decode(tableColor));
                }
            }
            else{
                if (tablica[i].getPawn() == 1) {
                    buttons[31-i].setIcon(pwhite);
                    buttons[31-i].setBackground(Color.decode(tableColor));
                }
                else if (tablica[i].getPawn() == 2) {
                    buttons[31-i].setIcon(pblack);
                    buttons[31-i].setBackground(Color.decode(tableColor));
                }
                else if (tablica[i].getPawn() == 0) {
                    buttons[31-i].setForeground(Color.decode(tableColor));
                    buttons[31-i].setBackground(Color.decode(tableColor));
                    buttons[31-i].setIcon(null);
                }
                else if (tablica[i].getPawn() == 3) {
                    buttons[31-i].setIcon(qwhite);
                    buttons[31-i].setBackground(Color.decode(tableColor));
                }
                else{
                    buttons[31-i].setIcon(qblack);
                    buttons[31-i].setBackground(Color.decode(tableColor));
                }
            }
        }
    }
    /**
     * Metoda zamykająca okno gry. Przechodzi następnie do listy pokoi.
     * @param leaveRoom Jeśli prawdziwe, gracz opuści obecną grę.
     */
    private void close(boolean leaveRoom) {
        System.out.println("Closed");
        dispose();
        timer.cancel();
        timer = new Timer();

        if (leaveRoom) {
            if (GameManager.getUserPlayer().getGameId() != null)
                GameManager.quitGame(false);
            Main.createMainFrame(new RoomList());
        }
        else
            Main.createMainFrame(new Lobby(GameManager.getUserGame().getGameName()));
    }

    // =========================================================================================

    /**
     * Metoda kończąca turę gracza.
     */
    public void endTurn(){
        wyswietlPlansze();
        sendData();
        if (!canPlayerPlay()){
            System.out.println("Gracz " + GameManager.getUserPlayer().getPlayerName() + " wygrał!");
            timer.cancel();
            timer = new Timer();
            JFrame f = new JFrame();
            JOptionPane.showMessageDialog(
                    f,
                    "Wygrałeś!",
                    "Koniec gry!", JOptionPane.INFORMATION_MESSAGE
            );
            close(true);
        }
    }
    /**
     * Metoda rozpoczynająa turę gracza.
     */
    public void startTurn(){
        wyswietlPlansze();
        if (!canPlayerPlay()){
            System.out.println("Gracz " + GameManager.getSecondPlayer().getPlayerName() + " wygrał!");
            timer.cancel();
            timer = new Timer();
            JFrame f = new JFrame();
            JOptionPane.showMessageDialog(
                    f,
                    "Gracz " + GameManager.getSecondPlayer().getPlayerName() + " wygrał!",
                    "Koniec gry!", JOptionPane.INFORMATION_MESSAGE
            );
            close(true);
        }
        System.out.println("start turn: " + aktualnyGracz);
    }
    /**
     * Metoda pobierające dane z serwera o grze
     */
    public void getData(){
        try {
            GameManager.getGame(GameManager.getUserGame());
            GameManager.setServerCallbackListener(new GameManager.ServerCallbackListener() {
                @Override
                public void onServerResponse(Object obj) {
                    Game game = (Game) obj;

                    if (game != null) {

                        Game oldGame = GameManager.getUserGame();
                        GameManager.setUserGame(game);

                        if (game.getPlayersCount() < 2) {
                            GameManager.setSecondPlayer(null);
                            close(false);
                            return;
                        }

                        if (game.getCurrentPlayerId().equals(GameManager.getUserPlayer().getId())) {

                            aktualnyGracz = oldGame.switchPlayers().equals(game.getWhitePlayerId()) ? 1 : 2;

                            stringToBoard(game.getBoard());

                            timer.cancel();
                            timer = new Timer();

                            startTurn();
                        }
                    }
                }

                @Override
                public void onServerFailed() {
                    System.out.println("Failed to get data from server!");
                }
            });
        }
        catch (ClassCastException cce) {
            System.err.println("Error while casting: " + cce.getMessage());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    /**
     * Metoda wysyłająca dane na serwer.
     */
    public void sendData(){

        // check if someone left before user moved
        Game game = GameManager.getGame_sync(GameManager.getUserGame().getId());
        GameManager.setUserGame(game);
        if (game.getPlayersCount() < 2) {
            close(false);
            return;
        }

        aktualnyGracz = game.switchPlayers().equals(game.getWhitePlayerId()) ? 1 : 2;
        game.setBoard(boardToString());

        GameManager.updateGame(game);
        GameManager.setServerCallbackListener(new GameManager.ServerCallbackListener() {
            @Override
            public void onServerResponse(Object obj) {
                if (canPlayerPlay()) {
                    waitForTurn();
                }
            }

            @Override
            public void onServerFailed() {
                System.out.println("Failed to send data to server!");
            }
        });
    }
    /**
     * Metoda oczekiwania na kolej gracza.
     */
    public void waitForTurn(){
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (PlaceholderUtility.hasInternetAccess())
                    getData();
                System.out.println("Gracz " + GameManager.getUserPlayer().getPlayerName() + " czeka na przeciwnika...");
            }
        }, 0, 3000);
    }
    /**
     * Metoda odtwarzająca stan planszy na podstawie ciągu znaków.
     * @param input ciąg znaków z zakodowaną infomarcja o planszy.
     */
    public static void stringToBoard (String input){
        for (int i = 0; i < 32; i++){
            tablica[i].setPawn(Character.getNumericValue(input.charAt(i)));
        }
    }
    /**
     * Metoda kodująca stan planszy na postać znakową.
     * @return stan planszy w postaci ciągu znaków.
     */
    public static String boardToString(){
        String result = "";
        for (int i = 0; i < 32; i++){
            result += Integer.toString(tablica[i].pawn);
        }
        return result;
    }

    // =========================================================================================

    /**
     * Metoda zwracająca licznik czasu.
     * @return licznik czasu.
     */
    public static Timer getTimer() {
        return timer;
    }
    /**
     * Metoda sprawdzająca czy zawodnik może wykonać ruch.
     * @return Jeśli prawdziwe, gracz może wykonać ruch
     */
    public boolean canPlayerPlay(){
        for (int i=0; i<32; i++){
            if (tablica[i].getPawn()==aktualnyGracz){
                if (pawnCanMove(tablica[i]) || pawnCanHit(tablica[i])) return true;
            }
            if (tablica[i].getPawn()==aktualnyGracz+2){
                if (queenCanMove(tablica[i]) || queenCanHit(tablica[i])) return true;
            }
        }
        return false;
    }
    /**
     * Metoda podświetla możliwe ruchy figury stojącej na wybranym polu - ze szczególnym uwzględnieniem możliwości bicia
     * @param pole pole figury, której możliwe ścieżki podświetlamy
     */
    public void lightPath(Field pole){
        int index = getIndex(pole);
        int x = pole.getX(aktualnyGracz == 1);
        int y = pole.getY(aktualnyGracz == 1);
        buttons[index].setBackground(Color.decode("#00DC29"));
        if (pole.getPawn()==aktualnyGracz){
            if(pawnCanHit(pole)){
                if (x>1){
                    if (getFieldFromAxis(x-1, y+1).getPawn()!=aktualnyGracz && getFieldFromAxis(x-1, y+1).getPawn()!=aktualnyGracz+2 && getFieldFromAxis(x-1,y+1).getPawn()!=0){
                        if (getFieldFromAxis(x-2,y+2).getPawn()==0) buttons[getIndex(getFieldFromAxis(x-2,y+2))].setBackground(Color.decode("#DC0005"));
                    }
                }
                if (x<8){
                    if (getFieldFromAxis(x+1, y+1).getPawn()!=aktualnyGracz && getFieldFromAxis(x+1, y+1).getPawn()!=aktualnyGracz+2 && getFieldFromAxis(x+1,y+1).getPawn()!=0){
                        if (getFieldFromAxis(x+2,y+2).getPawn()==0) buttons[getIndex(getFieldFromAxis(x+2,y+2))].setBackground(Color.decode("#DC0005"));
                    }
                }
            }
            else if (pawnCanMove(pole)){
                if (x>1){
                    if(getFieldFromAxis(x-1, y+1).getPawn()==0){
                        buttons[getIndex(getFieldFromAxis(x-1,y+1))].setBackground(Color.decode("#00DC29"));
                    }
                }
                if (x<8){
                    if(getFieldFromAxis(x+1, y+1).getPawn()==0){
                        buttons[getIndex(getFieldFromAxis(x+1,y+1))].setBackground(Color.decode("#00DC29"));
                    }
                }
            }
        }
        else if (pole.getPawn()==aktualnyGracz+2){
            if (queenCanHit(pole)){
                int iloscPionkowPodRzad = 0;
                Field poleTestowe = new Field();

                if (x>2 && y<7){
                    int x1 = x;
                    int y1 = y;
                    while (x1>1 && y1<8){
                        x1--;
                        y1++;
                        if (getFieldFromAxis(x1,y1).getPawn()!=0){
                            if (getFieldFromAxis(x1,y1).getPawn()==aktualnyGracz || getFieldFromAxis(x1,y1).getPawn()==aktualnyGracz+2) break;
                            else{
                                iloscPionkowPodRzad++;
                                if (iloscPionkowPodRzad>1) break;
                            }
                        }
                        else{
                            if (iloscPionkowPodRzad==1){
                                index = getIndex(getFieldFromAxis(x1,y1));
                                buttons[index].setBackground(Color.decode("#DC0005"));
                            }
                        }
                    }
                }

                if (x>2 && y>2){
                    iloscPionkowPodRzad = 0;
                    int x1 = x;
                    int y1 = y;
                    while (x1>1 && y1>1){
                        x1--;
                        y1--;
                        if (getFieldFromAxis(x1,y1).getPawn()!=0){
                            if (getFieldFromAxis(x1,y1).getPawn()==aktualnyGracz || getFieldFromAxis(x1,y1).getPawn()==aktualnyGracz+2) break;
                            else{
                                iloscPionkowPodRzad++;
                                if (iloscPionkowPodRzad>1) break;
                            }
                        }
                        else{
                            if (iloscPionkowPodRzad==1){
                                index = getIndex(getFieldFromAxis(x1,y1));
                                buttons[index].setBackground(Color.decode("#DC0005"));
                            }
                        }
                    }
                }

                if (x<7 && y<7){
                    iloscPionkowPodRzad = 0;
                    int x1 = x;
                    int y1 = y;
                    while (x1<8 && y1<8){
                        x1++;
                        y1++;
                        if (getFieldFromAxis(x1,y1).getPawn()!=0){
                            if (getFieldFromAxis(x1,y1).getPawn()==aktualnyGracz || getFieldFromAxis(x1,y1).getPawn()==aktualnyGracz+2) break;
                            else{
                                iloscPionkowPodRzad++;
                                if (iloscPionkowPodRzad>1) break;
                            }
                        }
                        else{
                            if (iloscPionkowPodRzad==1){
                                index = getIndex(getFieldFromAxis(x1,y1));
                                buttons[index].setBackground(Color.decode("#DC0005"));
                            }
                        }
                    }
                }

                if (x<7 && y>2){
                    iloscPionkowPodRzad = 0;
                    int x1 = x;
                    int y1 = y;
                    while (x1<8 && y1>1){
                        x1++;
                        y1--;
                        if (getFieldFromAxis(x1,y1).getPawn()!=0){
                            if (getFieldFromAxis(x1,y1).getPawn()==aktualnyGracz || getFieldFromAxis(x1,y1).getPawn()==aktualnyGracz+2) break;
                            else{
                                iloscPionkowPodRzad++;
                                if (iloscPionkowPodRzad>1) break;
                            }
                        }
                        else{
                            if (iloscPionkowPodRzad==1){
                                index = getIndex(getFieldFromAxis(x1,y1));
                                buttons[index].setBackground(Color.decode("#DC0005"));
                            }
                        }
                    }
                }
            }
            else if (queenCanMove(pole)){
                int x1 = x;
                int y1 = y;
                while (x1>1 && y1<8){
                    x1--;
                    y1++;
                    if (getFieldFromAxis(x1, y1).getPawn()==0){
                        index = getIndex(getFieldFromAxis(x1,y1));
                        buttons[index].setBackground(Color.decode("#00DC29"));
                    }
                    else break;
                }
                x1 = x;
                y1 = y;
                while (x1<8 && y1<8){
                    x1++;
                    y1++;
                    if (getFieldFromAxis(x1, y1).getPawn()==0){
                        index = getIndex(getFieldFromAxis(x1,y1));
                        buttons[index].setBackground(Color.decode("#00DC29"));
                    }
                    else break;
                }
                x1 = x;
                y1 = y;
                while (y1>1 && x1<8){
                    x1++;
                    y1--;
                    if (getFieldFromAxis(x1, y1).getPawn()==0){
                        index = getIndex(getFieldFromAxis(x1,y1));
                        buttons[index].setBackground(Color.decode("#00DC29"));
                    }
                    else break;
                }
                x1 = x;
                y1 = y;
                while (x1>1 && y1>1){
                    x1--;
                    y1--;
                    if (getFieldFromAxis(x1, y1).getPawn()==0){
                        index = getIndex(getFieldFromAxis(x1,y1));
                        buttons[index].setBackground(Color.decode("#00DC29"));
                    }
                    else break;
                }
            }
        }
    }
    /**
     * Metoda zwracająca index pola.
     * @param pole którego index ma zostać znaleziony.
     * @return index pola.
     */
    public int getIndex(Field pole){
        for (int i = 0; i<32; i++){
            if (tablica[i] == pole) return (aktualnyGracz==1 ? i : 31-i);
        }
        return 32;
    }
    /**
     * Metoda zwracająca obiekt pola o danym  położeniu.
     * @param x współrzędna x pola ktory ma zostać zwrócone.
     * @param y współrzędna y pola ktory ma zostać zwrócone.
     * @return pole o danych współrzędnych.
     */
    public Field getFieldFromAxis(int x, int y){
        for(var pole: tablica){
            if (pole.getX(aktualnyGracz == 1) == x && pole.getY(aktualnyGracz == 1) == y) return pole;
        }
        return null;
    }
    /**
     * Metoda sprawdzająca czy możliwy jest ruch w dane pole.
     * @param pole które jest sprawdzane
     * @return true jesli jest możliwy ruch, false jeśli nie jest możliwy.
     */
    public boolean pawnCanMove(Field pole){
        int x = pole.getX(aktualnyGracz == 1);
        int y = pole.getY(aktualnyGracz == 1);
        Field polePoLewej = new Field();
        Field polePoPrawej = new Field();
        if (x>1 && y<8){
            polePoLewej = getFieldFromAxis(x-1, y+1);
            if (polePoLewej.getPawn() == 0) return true;
        }
        if (x<8 && y<8){
            polePoPrawej = getFieldFromAxis(x+1, y+1);
            if (polePoPrawej.getPawn() == 0) return true;
        }
        return false;
    }
    /**
     * Metoda sprwadzająca czy możliwe jest bicie pionem
     * @param pole na ktorym stoi pion do sprawdzenia.
     * @return true jeśli jest możliwe bicie, false jeśli nie jest możliwe.
     */
    public boolean pawnCanHit(Field pole){
        int x = pole.getX(aktualnyGracz == 1);
        int y = pole.getY(aktualnyGracz == 1);
        Field polePoLewej = new Field();
        Field polePoPrawej = new Field();

        if (x>2 && y<7){
            polePoLewej = getFieldFromAxis(x-1,y+1);
            if (polePoLewej.getPawn()!=aktualnyGracz && polePoLewej.getPawn()!=aktualnyGracz+2 && polePoLewej.getPawn()!=0){
                int x1 = polePoLewej.getX(aktualnyGracz == 1);
                int y1 = polePoLewej.getY(aktualnyGracz == 1);
                polePoLewej = getFieldFromAxis(x1-1, y1+1);
                if (polePoLewej.getPawn()==0) return true;
            }
        }

        if (x<7 && y<7){
            polePoPrawej = getFieldFromAxis(x+1,y+1);
            if (polePoPrawej.getPawn()!=aktualnyGracz && polePoPrawej.getPawn()!=aktualnyGracz+2 && polePoPrawej.getPawn()!=0){
                int x1 = polePoPrawej.getX(aktualnyGracz == 1);
                int y1 = polePoPrawej.getY(aktualnyGracz == 1);
                polePoPrawej = getFieldFromAxis(x1+1, y1+1);
                if (polePoPrawej.getPawn()==0) return true;
            }
        }

        return false;
    }
    /**
     * Metoda sprwadzająca czy możliwy jest ruch damką
     * @param pole na ktorym stoi damka do sprwadzenia.
     * @return true jeśli jest możliwy ruch, false jeśli nie jest możliwy.
     */
    public boolean queenCanMove(Field pole){
        int x = pole.getX(aktualnyGracz == 1);
        int y = pole.getY(aktualnyGracz == 1);
        Field poleTestowe = new Field();
        if (x>1 && y<8){
            poleTestowe = getFieldFromAxis(x-1, y+1);
            if (poleTestowe.getPawn() == 0) return true;
        }
        if (x<8 && y<8){
            poleTestowe = getFieldFromAxis(x+1, y+1);
            if (poleTestowe.getPawn() == 0) return true;
        }
        if (y>1 && x<8){
            poleTestowe = getFieldFromAxis(x+1, y-1);
            if (poleTestowe.getPawn() == 0) return true;
        }
        if (x>1 && y>1){
            poleTestowe = getFieldFromAxis(x-1, y-1);
            if (poleTestowe.getPawn() == 0) return true;
        }
        return false;
    }
    /**
     * Metoda sprwadzająca czy możliwe jest bicie damką
     * @param pole na ktorym stoi damka do sprwadzenia.
     * @return true jeśli jest możliwe bicie, false jeśli nie jest możliwe.
     */
    public boolean queenCanHit(Field pole){
        int x = pole.getX(aktualnyGracz == 1);
        int y = pole.getY(aktualnyGracz == 1);
        int iloscPionkowPodRzad = 0;
        Field poleTestowe = new Field();

        if (x>2 && y<7){
            int x1 = x;
            int y1 = y;
            while (x1>1 && y1<8){
                x1--;
                y1++;
                if (getFieldFromAxis(x1,y1).getPawn()!=0){
                    if (getFieldFromAxis(x1,y1).getPawn()==aktualnyGracz || getFieldFromAxis(x1,y1).getPawn()==aktualnyGracz+2) break;
                    else{
                        iloscPionkowPodRzad++;
                        if (iloscPionkowPodRzad>1) break;
                    }
                }
                else{
                    if (iloscPionkowPodRzad==1) return true;
                }
            }
        }

        if (x>2 && y>2){
            iloscPionkowPodRzad = 0;
            int x1 = x;
            int y1 = y;
            while (x1>1 && y1>1){
                x1--;
                y1--;
                if (getFieldFromAxis(x1,y1).getPawn()!=0){
                    if (getFieldFromAxis(x1,y1).getPawn()==aktualnyGracz || getFieldFromAxis(x1,y1).getPawn()==aktualnyGracz+2) break;
                    else{
                        iloscPionkowPodRzad++;
                        if (iloscPionkowPodRzad>1) break;
                    }
                }
                else{
                    if (iloscPionkowPodRzad==1) return true;
                }
            }
        }

        if (x<7 && y<7){
            iloscPionkowPodRzad = 0;
            int x1 = x;
            int y1 = y;
            while (x1<8 && y1<8){
                x1++;
                y1++;
                if (getFieldFromAxis(x1,y1).getPawn()!=0){
                    if (getFieldFromAxis(x1,y1).getPawn()==aktualnyGracz || getFieldFromAxis(x1,y1).getPawn()==aktualnyGracz+2) break;
                    else{
                        iloscPionkowPodRzad++;
                        if (iloscPionkowPodRzad>1) break;
                    }
                }
                else{
                    if (iloscPionkowPodRzad==1) return true;
                }
            }
        }

        if (x<7 && y>2){
            iloscPionkowPodRzad = 0;
            int x1 = x;
            int y1 = y;
            while (x1<8 && y1>1){
                x1++;
                y1--;
                if (getFieldFromAxis(x1,y1).getPawn()!=0){
                    if (getFieldFromAxis(x1,y1).getPawn()==aktualnyGracz || getFieldFromAxis(x1,y1).getPawn()==aktualnyGracz+2) break;
                    else{
                        iloscPionkowPodRzad++;
                        if (iloscPionkowPodRzad>1) break;
                    }
                }
                else{
                    if (iloscPionkowPodRzad==1) return true;
                }
            }
        }
        return false;
    }
    /**
     * Metoda sprwadzająca czy możliwu jest ruch z pola startowego na pole końcowe przez piona.
     * @param start pole startowe.
     * @param end pole końcowe.
     * @return true jeśli jest możliwy ruch, false jeśli ruch nie jest możliwy.
     */
    public boolean checkFieldPawn(Field start, Field end){
        if (end.getPawn()!=0) return false;
        int startX = start.getX(aktualnyGracz == 1);
        int startY = start.getY(aktualnyGracz == 1);
        int endX = end.getX(aktualnyGracz == 1);
        int endY = end.getY(aktualnyGracz == 1);
        if ((startX-1 == endX || startX+1 == endX) && startY+1 == endY) return true;
        return false;
    }
    /**
     * Metoda sprwadzająca czy możliwu jest ruch z pola startowego na pole końcowe przez damke.
     * @param start pole startowe.
     * @param end pole końcowe.
     * @return true jeśli jest możliwy ruch, false jeśli ruch nie jest możliwy.
     */
    public boolean checkFieldQueen(Field start, Field end){
        if (end.getPawn()!=0) return false;
        int startX = start.getX(aktualnyGracz == 1);
        int startY = start.getY(aktualnyGracz == 1);
        int endX = end.getX(aktualnyGracz == 1);
        int endY = end.getY(aktualnyGracz == 1);
        if (startX - endX == startY - endY || startX - endX + (startY - endY) == 0){
            int diffX = endX - startX;
            diffX = diffX >= 0 ? 1 : -1;
            int diffY = endY - startY;
            diffY = diffY >= 0 ? 1 : -1;
            while (startX != endX){
                startX += diffX;
                startY += diffY;
                if (getFieldFromAxis(startX, startY).getPawn()!=0) return false;
            }
            return true;
        }
        else return false;
    }
    /**
     * Metoda sprwadzająca czy możliwu jest bicie z pola startowego na pole końcowe przez damke.
     * @param start pole startowe.
     * @param end pole końcowe.
     * @return true jeśli jest możliwe bicie, false jeśli ruch nie jest możliwy.
     */
    public boolean checkHitQueen(Field start, Field end){
        if (end.getPawn()!=0) return false;
        int startX = start.getX(aktualnyGracz == 1);
        int startY = start.getY(aktualnyGracz == 1);
        int endX = end.getX(aktualnyGracz == 1);
        int endY = end.getY(aktualnyGracz == 1);
        int iloscPionkowPodRzad = 0;
        if (startX - endX == startY - endY || startX - endX + (startY - endY) == 0){
            int diffX = endX - startX;
            diffX = diffX >= 0 ? 1 : -1;
            int diffY = endY - startY;
            diffY = diffY >= 0 ? 1 : -1;
            while (startX != endX){
                startX += diffX;
                startY += diffY;
                if (getFieldFromAxis(startX, startY).getPawn()!=0){
                    if (getFieldFromAxis(startX, startY).getPawn()!=aktualnyGracz && getFieldFromAxis(startX, startY).getPawn()!=aktualnyGracz+2){
                        iloscPionkowPodRzad++;
                        if (iloscPionkowPodRzad>1) return false;
                    }
                    else return false;
                }
                else if (iloscPionkowPodRzad==1) return true;
            }
        }
        return false;
    }
    /**
     * Metoda sprwadzająca czy możliwu jest bicie z pola startowego na pole końcowe przez piona.
     * @param start pole startowe.
     * @param end pole końcowe.
     * @return true jeśli jest możliwe bicie, false jeśli ruch nie jest możliwy.
     */
    public boolean checkHitPawn(Field start, Field end){
        if (end.getPawn()!=0) return false;
        int startX = start.getX(aktualnyGracz == 1);
        int startY = start.getY(aktualnyGracz == 1);
        int endX = end.getX(aktualnyGracz == 1);
        int endY = end.getY(aktualnyGracz == 1);
        if (startX-2 == endX && startY+2 == endY){
            Field temp = new Field();
            temp = getFieldFromAxis(startX-1, startY+1);
            if (temp.getPawn() != aktualnyGracz && temp.getPawn() != aktualnyGracz+2 && temp.getPawn() != 0) return true;
        }
        if (startX+2 == endX && startY+2 == endY){
            Field temp = new Field();
            temp = getFieldFromAxis(startX+1, startY+1);
            if (temp.getPawn() != aktualnyGracz && temp.getPawn() != aktualnyGracz+2 && temp.getPawn() != 0) return true;
        }
        return false;
    }

    /**
     * Metoda mająca na celu "zniszczenie" wrogiej figury. Jako parametr przyjmuje dwa pola, między którymi znajduje się niszczony pionek
     * @param start pole początkowe bijącej figury
     * @param end pole docelowe bijącej figury
     */
    public void destroyPawn(Field start, Field end){
        int startX = start.getX(aktualnyGracz == 1);
        int startY = start.getY(aktualnyGracz == 1);
        int endX = end.getX(aktualnyGracz == 1);
        int endY = end.getY(aktualnyGracz == 1);
        int diffY = endY - startY;
        diffY = diffY>=0 ? 1 : -1;
        int diffX = endX - startX;
        diffX = diffX>=0 ? 1 : -1;
        while (startX != endX){
            startX += diffX;
            startY += diffY;
            getFieldFromAxis(startX, startY).setPawn(0);
        }
    }
    /**
     * Metoda sprwadzająca czy zawodnik ma atak do wykonania.
     * @return true jeśli możliwe jest bicie, false jeśli bicie nie jest możliwe.
     */
    public boolean playerCanAttack(){
        for (int i=0; i<32; i++){
            if ((tablica[i].getPawn() == aktualnyGracz && pawnCanHit(tablica[i])) || (tablica[i].getPawn() == aktualnyGracz +2 && queenCanHit(tablica[i]))) return true;
        }
        return false;
    }
    /**
     * Metoda wykonująca się po kliknięciu w dane pole.
     * @param ind index pola.
     */
    public void buttonClicked(int ind){

        if (aktualnyGracz != kluczGracza) {
            System.out.println("Nie moja kolei");
            return;
        }
        System.out.println("Moja kolei");

        int index = -1;

        if (aktualnyGracz == 1){
            index = ind;
            System.out.println("x = " + tablica[index].getX(aktualnyGracz == 1) + " a y = " + tablica[index].getY(aktualnyGracz == 1) + " a pionek = " + tablica[index].getPawn());
            if ((tablica[index].getPawn() == aktualnyGracz || tablica[index].getPawn() == aktualnyGracz+2) && !hit){
                if (((pawnCanMove(tablica[index]) || pawnCanHit(tablica[index])) && tablica[index].getPawn()==aktualnyGracz) || (tablica[index].getPawn() == aktualnyGracz+2 && (queenCanHit(tablica[index]) || queenCanMove(tablica[index])))){
                    wyswietlPlansze();
                    select = true;
                    selectField = tablica[index];
                    lightPath(selectField);
                }
            }
            else if (select){
                targetField = tablica[index];
                if (targetField == selectField && hit){
                    select = false;
                    hit = false;
                    System.out.println("Zakończono mimo możliwości kolejnego bicia!");
                    endTurn();
                }
                if (checkFieldPawn(selectField, targetField) && selectField.getPawn() == aktualnyGracz){
                    if (playerCanAttack()){
                        System.out.println("Możliwe bicie!");
                    }
                    else{
                        targetField.setPawn(aktualnyGracz);
                        selectField.setPawn(0);
                        select = false;
                        hit = false;
                        if (targetField.getY(aktualnyGracz == 1)==8) targetField.setPawn(aktualnyGracz + 2);
                        System.out.println("Wykonano ruch!");
                        endTurn();
                    }
                }
                else if (checkHitPawn(selectField,targetField) && selectField.getPawn() == aktualnyGracz){
                    destroyPawn(selectField,targetField);
                    targetField.setPawn(aktualnyGracz);
                    selectField.setPawn(0);
                    hit = true;
                    if (targetField.getY(aktualnyGracz == 1)==8) targetField.setPawn(aktualnyGracz + 2);
                    if (!pawnCanHit(targetField)) {
                        select = false;
                        hit = false;
                        System.out.println("Kolejne bicie niemożliwe, koniec tury!");
                        endTurn();
                    }
                    else{
                        wyswietlPlansze();
                        System.out.println("Możliwe kolejne bicie!");
                        selectField = targetField;
                        lightPath(selectField);
                    }
                }
                //KRÓLÓWKI!!!
                else if (checkFieldQueen(selectField, targetField) && selectField.getPawn() == aktualnyGracz+2){
                    if (playerCanAttack()){
                        System.out.println("Możliwe bicie!");
                    }
                    else{
                        targetField.setPawn(aktualnyGracz+2);
                        selectField.setPawn(0);
                        select = false;
                        hit = false;
                        System.out.println("Wykonano ruch!");
                        endTurn();
                    }
                }
                else if (checkHitQueen(selectField,targetField) && selectField.getPawn() == aktualnyGracz+2){
                    destroyPawn(selectField,targetField);
                    targetField.setPawn(aktualnyGracz+2);
                    selectField.setPawn(0);
                    hit = true;
                    if (!queenCanHit(targetField)) {
                        select = false;
                        hit = false;
                        System.out.println("Kolejne bicie niemożliwe, koniec tury!");
                        endTurn();
                    }
                    else{
                        wyswietlPlansze();
                        System.out.println("Możliwe kolejne bicie!");
                        selectField = targetField;
                        lightPath(selectField);
                    }
                }
            }
        }

        else{
            index = 31 - ind;

            System.out.println("x = " + tablica[index].getX(aktualnyGracz == 1) + " a y = " + tablica[index].getY(aktualnyGracz == 1) + " a pionek = " + tablica[index].getPawn());

            if ((tablica[index].getPawn() == aktualnyGracz || tablica[index].getPawn() == aktualnyGracz+2) && !hit){
                if (((pawnCanMove(tablica[index]) || pawnCanHit(tablica[index])) && tablica[index].getPawn()==aktualnyGracz) || (tablica[index].getPawn() == aktualnyGracz+2 && (queenCanHit(tablica[index]) || queenCanMove(tablica[index])))){
                    wyswietlPlansze();
                    select = true;
                    selectField = tablica[index];
                    lightPath(selectField);
                }
            }
            else if (select){
                targetField = tablica[index];
                if (targetField == selectField && hit) {
                    select = false;
                    hit = false;
                    endTurn();
                }
                if (checkFieldPawn(selectField, targetField) && selectField.getPawn() == aktualnyGracz){
                    if (playerCanAttack()){
                        System.out.println("Możliwe bicie!");
                    }
                    else{
                        targetField.setPawn(aktualnyGracz);
                        selectField.setPawn(0);
                        if (targetField.getY(aktualnyGracz == 1)==8) targetField.setPawn(aktualnyGracz + 2);
                        endTurn();
                    }
                }
                else if (checkHitPawn(selectField,targetField) && selectField.getPawn() == aktualnyGracz){
                    destroyPawn(selectField,targetField);
                    targetField.setPawn(aktualnyGracz);
                    selectField.setPawn(0);
                    hit = true;
                    if (targetField.getY(aktualnyGracz == 1)==8) targetField.setPawn(aktualnyGracz + 2);
                    if (!pawnCanHit(targetField)) {
                        select = false;
                        hit = false;
                        endTurn();
                    }
                    else {
                        wyswietlPlansze();
                        selectField = targetField;
                        lightPath(selectField);
                    }
                }
                //KRÓLÓWKI!!!
                else if (checkFieldQueen(selectField, targetField) && selectField.getPawn() == aktualnyGracz+2){
                    if (playerCanAttack()){
                        System.out.println("Możliwe bicie!");
                    }
                    else{
                        targetField.setPawn(aktualnyGracz+2);
                        selectField.setPawn(0);
                        select = false;
                        hit = false;
                        System.out.println("Wykonano ruch!");
                        endTurn();
                    }
                }
                else if (checkHitQueen(selectField,targetField) && selectField.getPawn() == aktualnyGracz+2){
                    destroyPawn(selectField,targetField);
                    targetField.setPawn(aktualnyGracz+2);
                    selectField.setPawn(0);
                    hit = true;
                    if (!queenCanHit(targetField)) {
                        select = false;
                        hit = false;
                        System.out.println("Kolejne bicie niemożliwe, koniec tury!");
                        endTurn();
                    }
                    else{
                        wyswietlPlansze();
                        System.out.println("Możliwe kolejne bicie!");
                        selectField = targetField;
                        lightPath(selectField);
                    }
                }
            }
        }

    }
}
