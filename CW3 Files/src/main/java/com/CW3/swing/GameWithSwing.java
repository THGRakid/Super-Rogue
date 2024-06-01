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

    //存储主窗口、按钮、步数标签和移动步数
    private JFrame frame;
    private JButton actionButton;
    private JLabel moveLabel;
    // 创建一个新的JTextArea实例，用于显示文本内容
    private JTextArea textArea;
    private Timer timer;
    // 初始化移动步数为0
    private int move = 0;


    // 构造函数，用于初始化游戏窗口和组件


    public GameWithSwing() {
        createGame();
    }

    public GameWithSwing(In in) {
        super(in);
        createGame();
    }

    public void createGame(){
       layoutDesign();
        // 为按钮添加一个动作监听器，当用户点击按钮时，会执行监听器中的代码
        actionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timer != null && timer.isRunning()) {
                    // 如果Timer已经在运行，则不执行任何操作
                    return;
                }

                timer = new Timer(250, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (move >= N*N) {
                            // 停止Timer
                            timer.stop();
                            JOptionPane.showMessageDialog(frame, "The rogue wins ！ ！ ！");
                            actionButton.setEnabled(false);
                            return;
                        }

                        textArea.setText("");
                        move += 1;
                        moveLabel.setText("Move: " + move);

                        List<Game> games = playForAWT(); // 假设这是你的游戏逻辑方法

                        if (!games.get(0).getStatus()) {
                            // 如果游戏状态变为错误，则代表抓到
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
        /** 创建窗口 */
        // 创建一个新的JFrame实例，并设置标题为"DNF"
        frame = new JFrame("Super-Rogue");
        // 设置窗口关闭时的操作为退出程序
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 750);
        frame.setLayout(new BorderLayout());

        // 创建一个新的JLabel实例，用于显示步数，并设置初始文本
        moveLabel = new JLabel("Move: 0   ");
        moveLabel.setFont(new Font("Arial", Font.BOLD, 24));
        JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // 设置为右对齐
        northPanel.add(moveLabel);
        frame.add(northPanel, BorderLayout.NORTH);

        // 创建一个新的面板，用于放置按钮，并使用FlowLayout
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        // 创建一个新的JButton实例，并设置按钮上的文本为"执行行动"
        actionButton = new JButton("Start Chasing!!!");
        actionButton.setPreferredSize(new Dimension(120, 40));
        buttonPanel.add(actionButton);

        // 如果你想在按钮面板下方添加一些额外的空间，可以使用一个空的Border
        // 这里使用EmptyBorder来添加底部的空间
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0)); // 上, 左, 下, 右
        // 使用BorderLayout将按钮面板添加到窗口的底部
        frame.add(buttonPanel, BorderLayout.SOUTH);
        // 创建一个带有边框的面板
        JPanel contentPanel = new JPanel();
        Border border = BorderFactory.createLineBorder(Color.BLACK); // 创建一个黑色的边框
        contentPanel.setBorder(border); // 给面板设置边框
        // 将带有边框的面板添加到JFrame的内容面板中
        frame.getContentPane().add(contentPanel);


        // 创建一个新的JTextArea实例
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Consolas", Font.PLAIN, 30)); // 设置字体

        // 创建一个新的面板来容纳JTextArea，并使用BorderLayout
        JScrollPane scrollPane = new JScrollPane(textArea);
        // 将centerPanel添加到窗口的中心
        frame.add(scrollPane, BorderLayout.CENTER);


        // 显示窗口
        frame.setVisible(true);

        // 在显示窗口后，设置窗口位于屏幕中央
        frame.setLocationRelativeTo(null);
    }


    // 主方法，程序的入口点
    public static void main(String[] args) {
        // 使用SwingUtilities的invokeLater方法，确保GUI的创建和更新在事件调度线程（EDT）上执行
        // 这样可以避免GUI相关的线程安全问题
        SwingUtilities.invokeLater(new Runnable() {
            // 实现Runnable接口的run方法，用于初始化并运行游戏
            @Override
            public void run() {
                // 创建GameWithSwing类的实例，这会触发GUI的创建和显示
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