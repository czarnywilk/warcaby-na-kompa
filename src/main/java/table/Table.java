package table;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Table extends java.awt.Frame {

    int nrGracza = 1;
    boolean select = false;
    boolean hit = false;

    public JButton buttons[] = new JButton[32];
    Field tablica[] = new Field[32];
    Field selectField = new Field();
    Field targetField = new Field();

    BufferedImage pawn_white;
    {
        try {
            pawn_white = ImageIO.read(new File("src\\main\\resources\\pawn_white.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    BufferedImage pawn_black;
    {
        try {
            pawn_black = ImageIO.read(new File("src\\main\\resources\\pawn_black.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    BufferedImage queen_white;
    {
        try {
            queen_white = ImageIO.read(new File("src\\main\\resources\\white_queen.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    BufferedImage queen_black;
    {
        try {
            queen_black = ImageIO.read(new File("src\\main\\resources\\black_queen.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    ImageIcon pwhite = new ImageIcon(pawn_white);
    ImageIcon qwhite = new ImageIcon(queen_white);
    ImageIcon pblack = new ImageIcon(pawn_black);
    ImageIcon qblack = new ImageIcon(queen_black);

    String tableColor = "#03A9F4";

    public Table(){
        int licznik = 0;
        setSize(900,900);
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 4; j++){
                tablica[i*4 + j] = new Field(j*2+((i%2))+1, i+1, 0);
                //else tablica[i*4 + j] = new Field(j*2+((i%2)), i+1, 0);
                if (i<2) tablica[i*4 + j].setPawn(1);
                else if (i>5) tablica[i*4 + j].setPawn(2);
            }
        }
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                if ((j % 2 == 0 && i % 2 == 0) || (i%2 == 1 && j % 2 == 1)){
                    buttons[licznik] = new JButton();
                    buttons[licznik].setFocusable(false);
                    buttons[licznik].setBounds(100 * j + 50,100 * (7-i) + 50,100,100);
                    buttons[licznik].setBackground(Color.decode(tableColor));
                    buttons[licznik].setName(Integer.toString(licznik));
                    buttons[licznik].addMouseListener(new MouseListener() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            JButton a = (JButton) e.getSource();
                            System.out.println(a.getName());
                            buttonClicked(Integer.parseInt(a.getName()));
                        }

                        @Override
                        public void mousePressed(MouseEvent e) {

                        }

                        @Override
                        public void mouseReleased(MouseEvent e) {

                        }

                        @Override
                        public void mouseEntered(MouseEvent e) {

                        }

                        @Override
                        public void mouseExited(MouseEvent e) {

                        }
                    });
                    add(buttons[licznik]);
                    licznik++;
                }
            }
        }
        setBackground(Color.WHITE);
        setLayout(null);
        setVisible(true);
        wyswietlPlansze();
    }

    private void wyswietlPlansze() {
        for (int i = 0; i < 32; i++){
            if (nrGracza == 1){
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

    public void endTurn(){
        if (nrGracza == 1) nrGracza=2;
        else nrGracza = 1;
        if (!canPlayerPlay()){
            System.out.println("Gracz nr " + nrGracza + " przegrał!");
        }
        wyswietlPlansze();
    }

    public boolean canPlayerPlay(){
        for (int i=0; i<32; i++){
            if (tablica[i].getPawn()==nrGracza){
                if (pawnCanMove(tablica[i]) || pawnCanHit(tablica[i])) return true;
            }
            if (tablica[i].getPawn()==nrGracza+2){
                if (queenCanMove(tablica[i]) || queenCanHit(tablica[i])) return true;
            }
        }
        return false;
    }

    public void lightPath(Field pole){
        int index = getIndex(pole);
        int x = pole.getX(nrGracza == 1);
        int y = pole.getY(nrGracza == 1);
        buttons[index].setBackground(Color.decode("#00DC29"));
        if (pole.getPawn()==nrGracza){
            if(pawnCanHit(pole)){
                if (x>1){
                    if (getFieldFromAxis(x-1, y+1).getPawn()!=nrGracza && getFieldFromAxis(x-1, y+1).getPawn()!=nrGracza+2 && getFieldFromAxis(x-1,y+1).getPawn()!=0){
                        if (getFieldFromAxis(x-2,y+2).getPawn()==0) buttons[getIndex(getFieldFromAxis(x-2,y+2))].setBackground(Color.decode("#DC0005"));
                    }
                }
                if (x<8){
                    if (getFieldFromAxis(x+1, y+1).getPawn()!=nrGracza && getFieldFromAxis(x+1, y+1).getPawn()!=nrGracza+2 && getFieldFromAxis(x+1,y+1).getPawn()!=0){
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
        else if (pole.getPawn()==nrGracza+2){
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
                            if (getFieldFromAxis(x1,y1).getPawn()==nrGracza || getFieldFromAxis(x1,y1).getPawn()==nrGracza+2) break;
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
                            if (getFieldFromAxis(x1,y1).getPawn()==nrGracza || getFieldFromAxis(x1,y1).getPawn()==nrGracza+2) break;
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
                            if (getFieldFromAxis(x1,y1).getPawn()==nrGracza || getFieldFromAxis(x1,y1).getPawn()==nrGracza+2) break;
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
                            if (getFieldFromAxis(x1,y1).getPawn()==nrGracza || getFieldFromAxis(x1,y1).getPawn()==nrGracza+2) break;
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

    public int getIndex(Field pole){
        for (int i = 0; i<32; i++){
            if (tablica[i] == pole) return (nrGracza==1 ? i : 31-i);
        }
        return 32;
    }

    public Field getFieldFromAxis(int x, int y){
        for(Field pole: tablica){
            if (pole.getX(nrGracza == 1) == x && pole.getY(nrGracza == 1) == y) return pole;
        }
        return null;
    }

    public boolean pawnCanMove(Field pole){
        int x = pole.getX(nrGracza == 1);
        int y = pole.getY(nrGracza == 1);
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

    public boolean pawnCanHit(Field pole){
        int x = pole.getX(nrGracza == 1);
        int y = pole.getY(nrGracza == 1);
        Field polePoLewej = new Field();
        Field polePoPrawej = new Field();

        if (x>2 && y<7){
            polePoLewej = getFieldFromAxis(x-1,y+1);
            if (polePoLewej.getPawn()!=nrGracza && polePoLewej.getPawn()!=nrGracza+2 && polePoLewej.getPawn()!=0){
                int x1 = polePoLewej.getX(nrGracza == 1);
                int y1 = polePoLewej.getY(nrGracza == 1);
                polePoLewej = getFieldFromAxis(x1-1, y1+1);
                if (polePoLewej.getPawn()==0) return true;
            }
        }

        if (x<7 && y<7){
            polePoPrawej = getFieldFromAxis(x+1,y+1);
            if (polePoPrawej.getPawn()!=nrGracza && polePoPrawej.getPawn()!=nrGracza+2 && polePoPrawej.getPawn()!=0){
                int x1 = polePoPrawej.getX(nrGracza == 1);
                int y1 = polePoPrawej.getY(nrGracza == 1);
                polePoPrawej = getFieldFromAxis(x1+1, y1+1);
                if (polePoPrawej.getPawn()==0) return true;
            }
        }

        return false;
    }

    public boolean queenCanMove(Field pole){
        int x = pole.getX(nrGracza == 1);
        int y = pole.getY(nrGracza == 1);
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

    public boolean queenCanHit(Field pole){
        int x = pole.getX(nrGracza == 1);
        int y = pole.getY(nrGracza == 1);
        int iloscPionkowPodRzad = 0;
        Field poleTestowe = new Field();

        if (x>2 && y<7){
            int x1 = x;
            int y1 = y;
            while (x1>1 && y1<8){
                x1--;
                y1++;
                if (getFieldFromAxis(x1,y1).getPawn()!=0){
                    if (getFieldFromAxis(x1,y1).getPawn()==nrGracza || getFieldFromAxis(x1,y1).getPawn()==nrGracza+2) break;
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
                    if (getFieldFromAxis(x1,y1).getPawn()==nrGracza || getFieldFromAxis(x1,y1).getPawn()==nrGracza+2) break;
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
                    if (getFieldFromAxis(x1,y1).getPawn()==nrGracza || getFieldFromAxis(x1,y1).getPawn()==nrGracza+2) break;
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
                    if (getFieldFromAxis(x1,y1).getPawn()==nrGracza || getFieldFromAxis(x1,y1).getPawn()==nrGracza+2) break;
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

    public boolean checkFieldPawn(Field start, Field end){
        if (end.getPawn()!=0) return false;
        int startX = start.getX(nrGracza == 1);
        int startY = start.getY(nrGracza == 1);
        int endX = end.getX(nrGracza == 1);
        int endY = end.getY(nrGracza == 1);
        if ((startX-1 == endX || startX+1 == endX) && startY+1 == endY) return true;
        return false;
    }

    public boolean checkFieldQueen(Field start, Field end){
        if (end.getPawn()!=0) return false;
        int startX = start.getX(nrGracza == 1);
        int startY = start.getY(nrGracza == 1);
        int endX = end.getX(nrGracza == 1);
        int endY = end.getY(nrGracza == 1);
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

    public boolean checkHitQueen(Field start, Field end){
        if (end.getPawn()!=0) return false;
        int startX = start.getX(nrGracza == 1);
        int startY = start.getY(nrGracza == 1);
        int endX = end.getX(nrGracza == 1);
        int endY = end.getY(nrGracza == 1);
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
                    if (getFieldFromAxis(startX, startY).getPawn()!=nrGracza && getFieldFromAxis(startX, startY).getPawn()!=nrGracza+2){
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

    public boolean checkHitPawn(Field start, Field end){
        if (end.getPawn()!=0) return false;
        int startX = start.getX(nrGracza == 1);
        int startY = start.getY(nrGracza == 1);
        int endX = end.getX(nrGracza == 1);
        int endY = end.getY(nrGracza == 1);
        if (startX-2 == endX && startY+2 == endY){
            Field temp = new Field();
            temp = getFieldFromAxis(startX-1, startY+1);
            if (temp.getPawn() != nrGracza && temp.getPawn() != nrGracza+2 && temp.getPawn() != 0) return true;
        }
        if (startX+2 == endX && startY+2 == endY){
            Field temp = new Field();
            temp = getFieldFromAxis(startX+1, startY+1);
            if (temp.getPawn() != nrGracza && temp.getPawn() != nrGracza+2 && temp.getPawn() != 0) return true;
        }
        return false;
    }

    public void destroyPawn(Field start, Field end){
        int startX = start.getX(nrGracza == 1);
        int startY = start.getY(nrGracza == 1);
        int endX = end.getX(nrGracza == 1);
        int endY = end.getY(nrGracza == 1);
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

    public boolean playerCanAttack(){
        for (int i=0; i<32; i++){
            if ((tablica[i].getPawn() == nrGracza && pawnCanHit(tablica[i])) || (tablica[i].getPawn() == nrGracza +2 && queenCanHit(tablica[i]))) return true;
        }
        return false;
    }

    public void buttonClicked(int ind){

        int index = -1;

        if (nrGracza == 1){
            index = ind;
            System.out.println("x = " + tablica[index].getX(nrGracza == 1) + " a y = " + tablica[index].getY(nrGracza == 1) + " a pionek = " + tablica[index].getPawn());
            if ((tablica[index].getPawn() == nrGracza || tablica[index].getPawn() == nrGracza+2) && !hit){
                if (((pawnCanMove(tablica[index]) || pawnCanHit(tablica[index])) && tablica[index].getPawn()==nrGracza) || (tablica[index].getPawn() == nrGracza+2 && (queenCanHit(tablica[index]) || queenCanMove(tablica[index])))){
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
                if (checkFieldPawn(selectField, targetField) && selectField.getPawn() == nrGracza){
                    if (playerCanAttack()){
                        System.out.println("Możliwe bicie!");
                    }
                    else{
                        targetField.setPawn(nrGracza);
                        selectField.setPawn(0);
                        select = false;
                        hit = false;
                        if (targetField.getY(nrGracza == 1)==8) targetField.setPawn(nrGracza + 2);
                        System.out.println("Wykonano ruch!");
                        endTurn();
                    }
                }
                else if (checkHitPawn(selectField,targetField) && selectField.getPawn() == nrGracza){
                    destroyPawn(selectField,targetField);
                    targetField.setPawn(nrGracza);
                    selectField.setPawn(0);
                    hit = true;
                    if (targetField.getY(nrGracza == 1)==8) targetField.setPawn(nrGracza + 2);
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
                else if (checkFieldQueen(selectField, targetField) && selectField.getPawn() == nrGracza+2){
                    if (playerCanAttack()){
                        System.out.println("Możliwe bicie!");
                    }
                    else{
                        targetField.setPawn(nrGracza+2);
                        selectField.setPawn(0);
                        select = false;
                        hit = false;
                        System.out.println("Wykonano ruch!");
                        endTurn();
                    }
                }
                else if (checkHitQueen(selectField,targetField) && selectField.getPawn() == nrGracza+2){
                    destroyPawn(selectField,targetField);
                    targetField.setPawn(nrGracza+2);
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

            System.out.println("x = " + tablica[index].getX(nrGracza == 1) + " a y = " + tablica[index].getY(nrGracza == 1) + " a pionek = " + tablica[index].getPawn());

            if ((tablica[index].getPawn() == nrGracza || tablica[index].getPawn() == nrGracza+2) && !hit){
                if (((pawnCanMove(tablica[index]) || pawnCanHit(tablica[index])) && tablica[index].getPawn()==nrGracza) || (tablica[index].getPawn() == nrGracza+2 && (queenCanHit(tablica[index]) || queenCanMove(tablica[index])))){
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
                if (checkFieldPawn(selectField, targetField) && selectField.getPawn() == nrGracza){
                    if (playerCanAttack()){
                        System.out.println("Możliwe bicie!");
                    }
                    else{
                        targetField.setPawn(nrGracza);
                        selectField.setPawn(0);
                        if (targetField.getY(nrGracza == 1)==8) targetField.setPawn(nrGracza + 2);
                        endTurn();
                    }
                }
                else if (checkHitPawn(selectField,targetField) && selectField.getPawn() == nrGracza){
                    destroyPawn(selectField,targetField);
                    targetField.setPawn(nrGracza);
                    selectField.setPawn(0);
                    hit = true;
                    if (targetField.getY(nrGracza == 1)==8) targetField.setPawn(nrGracza + 2);
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
                else if (checkFieldQueen(selectField, targetField) && selectField.getPawn() == nrGracza+2){
                    if (playerCanAttack()){
                        System.out.println("Możliwe bicie!");
                    }
                    else{
                        targetField.setPawn(nrGracza+2);
                        selectField.setPawn(0);
                        select = false;
                        hit = false;
                        System.out.println("Wykonano ruch!");
                        endTurn();
                    }
                }
                else if (checkHitQueen(selectField,targetField) && selectField.getPawn() == nrGracza+2){
                    destroyPawn(selectField,targetField);
                    targetField.setPawn(nrGracza+2);
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