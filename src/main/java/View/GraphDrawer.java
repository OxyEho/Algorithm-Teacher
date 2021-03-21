package View;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GraphDrawer extends JFrame {
    private JPanel drawPanel = new JPanel();
    public GraphDrawer() {
        setLayout(null);
        setBounds(600, 200, 800, 800);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public void drawGraph(Integer[] nodes, ArrayList<Integer[]> edges){

            Circle circle = new Circle(nodes[0].toString());
            circle.setBounds(100, 100,100, 100);
            add(circle);
        //рисует
    }

}
