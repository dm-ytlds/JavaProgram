package com.demi.myCalendar;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CalendarFrame extends JFrame {

    // 定义需要的组件
    private JComboBox yearComboBox; // 年组合框
    private JComboBox monthComboBox;    // 月组合框
    private JButton returnBtn;  // 返回今日按钮
    private JLabel[] weekLbs;   // 日历表头标签数组
    private JButton[] dayBtns;  // 日历中天数anniu
    private JLabel selectedDateLb;  // 选中日期
    private JLabel chineseYearLb;   // 显示农历年


    public static void main(String[] args) {
        // test
        CalendarFrame calendarFrame = new CalendarFrame();
    }

    public CalendarFrame() throws HeadlessException {
        // 在构造方法中对组件进行初始化
        yearComboBox = new JComboBox();
        monthComboBox = new JComboBox();
        returnBtn = new JButton();
        String[] week = {"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期天"};
        weekLbs = new JLabel[week.length];
        // 给weekLbs数组的每个元素赋值
        for (int i = 0; i < week.length; i++) {
            weekLbs[i] = new JLabel(week[i]);
            // 设置标签内容水平居中
            weekLbs[i].setHorizontalAlignment(JLabel.CENTER);
        }
        // 给显示天数的按钮数组初始化
        dayBtns = new JButton[42];
        for (int i = 0; i < dayBtns.length; i++) {
            dayBtns[i] = new JButton();
        }
        // 选中日期标签初始化
        selectedDateLb = new JLabel("选中日期");
        // 农历年标签初始化
        chineseYearLb = new JLabel("农历年");

        /*-==================--===================-*/
        // 把组件添加到frame框架中
        // 把年组合框，月组合框，返回今日按钮添加到一个JPanel面板中，再把该面板添加到frame框架中
        JPanel yearMonthPanel = new JPanel();
        yearMonthPanel.add(yearComboBox);
        yearMonthPanel.add(new JLabel(" 年 "));
        yearMonthPanel.add(monthComboBox);
        yearMonthPanel.add(new JLabel(" 月 "));
        yearMonthPanel.add(returnBtn);
        // 把面板添加到框架中
        add(yearMonthPanel, BorderLayout.NORTH);

        /*-==================--===================-*/
        // 先创建一个JPanel面板，放星期几以及日历中的天数
        JPanel dayPanel = new JPanel();
        // 设置该面板的布局为网格布局
        dayPanel.setLayout(new GridLayout(7, 7, 8, 8));
        // 设置当前面板的边框
        dayPanel.setBorder(new EmptyBorder(5, 10, 5, 10));
        // 箱dayPanel面板中添加表头
        for (int i = 0; i < weekLbs.length; i++) {
            dayPanel.add(weekLbs[i]);
        }
        // 添加天数按钮
        for (int i = 0; i < dayBtns.length; i++) {
            dayPanel.add(dayBtns[i]);
        }
        // 添加面板到框架中
        add(dayPanel, BorderLayout.CENTER);
        /*-==================--===================-*/
        // 定义Panel面板保存选中日期以及农历年
        JPanel southPanel = new JPanel();
        southPanel.add(selectedDateLb);
        southPanel.add(chineseYearLb);
        // 把面板添加到框架中
        add(southPanel, BorderLayout.SOUTH);
        /*-==================--===================-*/
        // 设置Frame敞口的相关属性
        // 设置标题
        setTitle("Calendar");
        // 设置窗口大小
        setSize(600, 360);
        // 取消窗口大小调整
        setResizable(false);
        // 设置窗口运行后显示在正中间
        setLocationRelativeTo(null);
        // 关闭窗口，退程序
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        // 显示窗口
        setVisible(true);
    }

}
