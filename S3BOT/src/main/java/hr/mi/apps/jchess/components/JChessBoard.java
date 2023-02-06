package hr.mi.apps.jchess.components;

import javax.swing.*;
import java.awt.*;

public class JChessBoard extends JComponent {

    public JChessBoard(){
        this.setLayout(new GridLayout(8, 8));
        for (int i = 0; i < 64; i++){
            addTile(i);
        }
    }

    private void addTile(int index){
        JLabel tile = new JLabel();
        tile.setBackground(index%2 == 0 ? Color.BLACK : Color.white);
        this.add(tile);
    }
}
