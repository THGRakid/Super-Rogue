package com.CW3.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameWithSwing {

    //存储主窗口、按钮、步数标签和移动步数
    private JFrame frame;
    private JButton actionButton;
    private JLabel moveLabel;
    // 初始化移动步数为0
    private int move = 0;


    // 构造函数，用于初始化游戏窗口和组件
    public GameWithSwing() {
        // 创建一个新的JFrame实例，并设置标题为"DNF"
        frame = new JFrame("DNF");
        // 设置窗口关闭时的操作为退出程序
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 设置窗口的大小为400x300像素
        frame.setSize(1000, 750);
        // 设置窗口的布局管理器为FlowLayout，组件将按照它们在添加时的顺序从左到右、从上到下排列
        frame.setLayout(new FlowLayout());

        // 创建一个新的JLabel实例，用于显示分数，并设置初始文本
        moveLabel = new JLabel("Move: " + move);
        // 将分数标签添加到窗口中
        frame.add(moveLabel);

        // 创建一个新的JButton实例，并设置按钮上的文本为"执行行动"
        actionButton = new JButton("执行行动");
        // 为按钮添加一个动作监听器，当用户点击按钮时，会执行监听器中的代码
        actionButton.addActionListener(new ActionListener() {
            // 重写ActionListener接口的actionPerformed方法
            @Override
            public void actionPerformed(ActionEvent e) {
                // 当按钮被点击时，分数增加10
                move += 10;
                // 更新分数标签的文本，显示新的分数
                moveLabel.setText("分数: " + move);

                // 检查分数是否大于100
                if (move > 100) {
                    // 如果分数大于100，显示一个对话框，告知用户获胜
                    JOptionPane.showMessageDialog(frame, "恭喜，你赢了！");
                    // 禁用按钮，防止用户再次点击
                    actionButton.setEnabled(false);
                }
            }
        });
        // 将按钮添加到窗口中
        frame.add(actionButton);

        // 设置窗口为可见状态，使其显示在屏幕上
        frame.setVisible(true);
    }

    // 主方法，程序的入口点
    public static void main(String[] args) {
        // 使用SwingUtilities的invokeLater方法，确保GUI的创建和更新在事件调度线程（EDT）上执行
        // 这样可以避免GUI相关的线程安全问题
        SwingUtilities.invokeLater(new Runnable() {
            // 实现Runnable接口的run方法，用于初始化并运行游戏
            @Override
            public void run() {
                new GameWithSwing(); // 创建GameWithSwing类的实例，这会触发GUI的创建和显示
            }
        });
    }

}