package View;

import Models.Graph;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GraphDrawer extends JFrame {
    JFrame frame;

    public GraphDrawer() {
        super("TestTest");
        setSize(400,500);
        setLayout(null);
        setBounds(200, 200, 1000, 1000);
        setVisible(true);
        // dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                dispose();
            }
        });
    }

//    private void dragGraph(Graph<Integer> graph){
//        graph.getNodeList().size();
//    }
}
