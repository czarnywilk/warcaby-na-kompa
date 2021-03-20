import javax.accessibility.Accessible;
import javax.swing.*;
import javax.swing.plaf.ScrollPaneUI;
import java.awt.*;

public class OnlineBox extends Frame {
    OnlineBox(){
        Panel panel=new Panel();
        panel.setBounds(50,100,400,800);
        panel.setBackground(Color.gray);
        add(panel);
        Button[] tablica = new Button[100];
        for (int i = 0; i < 100; i++){
            tablica[i] = new Button(Integer.toString(i));
            tablica[i].setSize(400,100);
            panel.add(tablica[i]);
        }
        setSize(500,1000);
        setLayout(null);
        setVisible(true);
    }

    public static void main(String[] args){
        OnlineBox o = new OnlineBox();
    }
}
