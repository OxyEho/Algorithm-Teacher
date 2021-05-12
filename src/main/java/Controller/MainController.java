package Controller;

import Models.Graph;
import Models.Node;
import View.MainWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MainController {
    private static MainWindow mainWindow;
    private static DrawerController drawerController;
    private static ToMenuButtonListener toMenuButtonListener;
    private static final Graph<String> defaultGraph = new Graph<>(new HashMap<>(){
        {
            put("a", (new Node<>("a", new HashMap<>(){{put("b", 1d);put("e", 1d);}})));
            put("b", (new Node<>("b", new HashMap<>(){{put("a", 1d);put("c", 1d);}})));
            put("c", (new Node<>("c", new HashMap<>(){{put("b", 1d);put("d", 1d);}})));
            put("d", (new Node<>("d", new HashMap<>(){{put("c", 1d);}})));
            put("e", (new Node<>("e", new HashMap<>(){{put("a", 1d);}})));
        }
    }, false, false);

    public static void main(String[] args) {
        toMenuButtonListener = new ToMenuButtonListener();
        mainWindow = new MainWindow(new MenuButtonListener());
        mainWindow.repaint();
    }

    private static class MenuButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton origin = (JButton) e.getSource();
            if (origin.getText().equals("View")){
                mainWindow.dispose();
                drawerController = new DrawerController(toMenuButtonListener, defaultGraph);
            }

        }
    }

    private static class ToMenuButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            mainWindow.setVisible(true);
            drawerController.getGraphDrawer().dispose();
        }
    }
}
