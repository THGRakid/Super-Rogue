package com.CW3.swing;

import com.CW3.game.Game;
import com.CW3.game.In;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Scanner;

public class GameWithSwing extends Game {


    private JFrame frame;
    private JButton actionButton;
    private JLabel moveLabel;
    private JTextArea textArea;
    private Timer timer;
    private int move = 0;


    // Constructor, which initializes the game window and components


    public GameWithSwing() {
        createGame();
    }

    public GameWithSwing(In in) {
        super(in);
        createGame();
    }

    public void createGame(){
       layoutDesign();
        // Add an action listener to the button, and when the user clicks the button, the code in the listener executes
        actionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timer != null && timer.isRunning()) {
                    // If the Timer is already running, no operation is performed
                    return;
                }

                timer = new Timer(250, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (move >= N*N) {
                            // Stop Timer
                            timer.stop();
                            JOptionPane.showMessageDialog(frame, "The rogue wins ！ ！ ！");
                            actionButton.setEnabled(false);
                            return;
                        }

                        textArea.setText("");
                        move += 1;
                        moveLabel.setText("Move: " + move);

                        List<Game> games = playForAWT();

                        if (!games.get(0).getStatus()) {
                            // If the game status changes to error, it means caught
                            timer.stop();
                            JOptionPane.showMessageDialog(frame, "The monster wins ！ ！ ！");
                            actionButton.setEnabled(false);
                            return;
                        }

                        //  Update textArea
                        textArea.append(games.get(0).toString());

                    }
                });


                timer.start();
            }
        });
    }


    public void layoutDesign(){
        /** Create window */
        // Create a new JFrame instance and set the title to "DNF"
        frame = new JFrame("Super-Rogue");
        // Set the operation when the window closes to exit the program
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 750);
        frame.setLayout(new BorderLayout());

        // Creates a new JLabel instance that displays the number of steps and sets the initial text
        moveLabel = new JLabel("Move: 0   ");
        moveLabel.setFont(new Font("Arial", Font.BOLD, 24));
        JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        northPanel.add(moveLabel);
        frame.add(northPanel, BorderLayout.NORTH);

        // Create a new panel to place the buttons and use FlowLayout
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        // Create a new instance of JButton and set the text on the button to "Perform Action"
        actionButton = new JButton("Start Chasing!!!");
        actionButton.setPreferredSize(new Dimension(120, 40));
        buttonPanel.add(actionButton);


        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        frame.add(buttonPanel, BorderLayout.SOUTH);
        JPanel contentPanel = new JPanel();
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        contentPanel.setBorder(border);
        frame.getContentPane().add(contentPanel);


        // Create a new JTextArea instance
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Consolas", Font.PLAIN, 30));
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, BorderLayout.CENTER);


        // Display window
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }


    // The main method, the entry point of the program
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            // Implements the run method of the Runnable interface to initialize and run the game
            @Override
            public void run() {
                // Create an instance of the GameWithSwing class, which triggers the creation and display of the GUI
                System.out.println("Please enter the capital letters A-U to determine the map you want to select:");
                Scanner scanner = new Scanner(System.in);
                String s = scanner.nextLine();
                String File = "dungeon" + s + ".txt";
                In stdin = new In(File);
                new GameWithSwing(stdin);

            }
        });
    }

}