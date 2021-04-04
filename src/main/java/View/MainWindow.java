package View;

import Controller.MainController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame {
    private static ActionListener buttonActionListener;
    
    public MainWindow(ActionListener buttonActionListener){
        MainWindow.buttonActionListener = buttonActionListener;
        setLayout(null);
        setBounds(0, 0, 800, 800);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initButtons();
    }

    private void initButtons() {
        Container container = getContentPane();
        container.setLayout(new GridLayout());
        JButton drawerButton = new JButton("View");
        drawerButton.addActionListener(buttonActionListener);
        drawerButton.setFont(new Font("TimesRoman", Font.PLAIN, 40));
        container.add(drawerButton);
        JButton anotherButton = new JButton("Constructor");
        anotherButton.addActionListener(buttonActionListener);
        anotherButton.setFont(new Font("TimesRoman", Font.PLAIN, 40));
        container.add(anotherButton);
    }
}
