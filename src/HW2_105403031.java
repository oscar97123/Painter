import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.awt.Point;

import javax.swing.*;

/*
 * 資管4A
 * 105403031  莫智堯
 */

public class HW2_105403031 extends JFrame{

    private final JButton eraser;
    private JCheckBox fill_checkBox;
    private JButton brush_color_Btn;
    private JButton clear_Btn;
    private JLabel mouse_position_Text;
    private DrawingPanel drawingPanel;
    private JPanel mouse_event_Panel;
    private JPanel menu;
    private JPanel drawing_tools;
    private JComboBox<String> JComboBox_drawingTools;
    private String[] tools_names = {"筆刷", "直線", "橢圓形", "矩形", "圓角矩形"};
    private JPanel brush_size_small_holder;
    private JRadioButton small_size;
    private JRadioButton medium_size;
    private JRadioButton big_size;
    private JPanel brush_size_medium_holder;
    private JPanel brush_size_big_holder;
    private JPanel fill_panel;
    private JPanel menu_holder;
    private String current_mode = "筆刷";
    private String size_selected = "small"; //記錄用戶選了那一個size 的筆刷, 預設是small

    public HW2_105403031() {
        super("小畫家");

        //顯示Welcome 訊息
        JOptionPane.showMessageDialog(HW2_105403031.this, "Welcome");

        //建立繪圖範圍 + 背景顏色
        drawingPanel = new DrawingPanel();
        //drawingPanel = new JPanel();
        drawingPanel.setBackground(Color.white);
        //

        //建立多個JPanel (後續程式碼會用到)
        menu = new JPanel();
        menu_holder = new JPanel();
        drawing_tools = new JPanel();
        brush_size_small_holder = new JPanel();
        brush_size_medium_holder = new JPanel();
        brush_size_big_holder = new JPanel();
        fill_panel = new JPanel();
        mouse_event_Panel = new JPanel();
        //

        //設定工具列能佔用的size
        //menu_holder <- 最大能佔用的size
        //menu <- 包在menu_holder 內實際佔用的size  (因範例的工具列 左右有留空間)
        menu_holder.setPreferredSize(new Dimension(WIDTH, 50));
        menu.setPreferredSize(new Dimension(550 ,45));


        //分割成GridLayout
        //"繪圖工具" 文字 + 下拉式選單
        drawing_tools.setLayout(new GridLayout(2, 1));
        drawing_tools.add(new JLabel("繪圖工具"));
        drawing_tools.add(JComboBox_drawingTools = new JComboBox<String>(tools_names));
        drawingTools_comboBox_Onclick();  //當comboBox 被選擇時，會印出點選了那個radio button
        //


        //產生 單選按鈕(大 中 小） + 分割成GridLayout
        brush_size_small_holder.setLayout(new GridLayout(2, 1));
        brush_size_medium_holder.setLayout(new GridLayout(2, 1));
        brush_size_big_holder.setLayout(new GridLayout(2, 1));

        small_size = new JRadioButton("小", true);
        medium_size = new JRadioButton("中", false);
        big_size = new JRadioButton("大", false);
        //

        //把三個單選按鈕綁在一起
        ButtonGroup BtnGrp = new ButtonGroup();
        BtnGrp.add(small_size);
        BtnGrp.add(medium_size);
        BtnGrp.add(big_size);

        //放進對應的GridLayout
        brush_size_small_holder.add(new JLabel("筆刷大小"));
        brush_size_small_holder.add(small_size);

        brush_size_medium_holder.add(new JLabel());  //空的JLabel 用作排版(佔位置)
        brush_size_medium_holder.add(medium_size);

        brush_size_big_holder.add(new JLabel());
        brush_size_big_holder.add(big_size);
        //

        //當radio button 被按下時，會印出點選了那個radio button
        brasher_radioBtn_Onclick();


        //填滿功能 ("填滿" 文字label + Checkbox)
        //分割GridLayout
        fill_panel.setLayout(new GridLayout(2, 1));
        fill_panel.add(new JLabel("填滿"));
        fill_checkBox = new JCheckBox();
        fill_panel.add(fill_checkBox);
        fill_checkBox.setEnabled(false); // 程式一打開時會是"筆刷" 模式， "筆刷"模式不開放填滿功能
        fill_checkBox_OnClick(); //當checkbox 勾選時，會印出 有勾選 / 沒有勾選
        //

        //建立三個button
        brush_color_Btn = new JButton("筆刷顏色");
        clear_Btn = new JButton("清除畫面");
        eraser = new JButton("橡皮擦");;
        brush_color_n_clear_Btn_Onclick(); //按下筆刷顏色 / 清除畫面接鈕後會印出 按下那個button
        //


        //BoxLayout 作間隔
        //把全部的elements 加到工具區(menu) 內
        menu.setLayout(new BoxLayout(menu, BoxLayout.X_AXIS));
        menu.add(drawing_tools);
        menu.add(brush_size_small_holder);
        menu.add(brush_size_medium_holder);
        menu.add(brush_size_big_holder);
        menu.add(fill_panel);
        menu.add(brush_color_Btn);
        menu.add(clear_Btn);
        menu.add(eraser);
        menu_holder.add(menu); //menu  包在menu_holder 內 (限制顯示範圍)
        //



        //
        mouse_event_Panel.setLayout(new GridLayout(1, 1));
        mouse_position_Text = new JLabel("游標位置："); // "游標位置：" label   assign 到 mouse_position_Text
        mouse_position_Text.setAlignmentX(Component.LEFT_ALIGNMENT); //"游標位置：" label 靠左
        mouse_position_Text.setForeground(Color.white); //文字顏色
        mouse_event_Panel.add(mouse_position_Text);
        mouse_event_Panel.setBackground(Color.black); //背景顏色
        mouse_function(); //function 內實作顯示實時的 游標位置
        //

        add(menu_holder, BorderLayout.NORTH); //切割layout上方作 工具區
        add(drawingPanel, BorderLayout.CENTER); //切割layout中間作 繪畫區
        add(mouse_event_Panel, BorderLayout.SOUTH); //切割layout下方作 顯示遊標位置 用途


    }

    public class DrawingPanel extends JPanel{
        // 用ArrayList記錄 所有繪圖的點
        private ArrayList<Point> points_small_size = new ArrayList<>();
        private ArrayList<Point> points_medium_size = new ArrayList<>();
        private ArrayList<Point> points_big_size = new ArrayList<>();

        public DrawingPanel(){
            addMouseMotionListener(new MouseMotionListener() {
                @Override
                public void mouseDragged(MouseEvent e) {

                    //判斷下拉選單選擇了那個繪圖工具
                    if (current_mode.matches("筆刷")){

                        // 大 中 小size 筆刷的繪畫記錄(遊標位置) 會記錄在對應的arrayList
                        if (size_selected.matches("small")) {

                            points_small_size.add(e.getPoint());

                        }else if (size_selected.matches("medium")){

                            points_medium_size.add(e.getPoint());

                        }else {

                            points_big_size.add(e.getPoint());

                        }
                        //

                    }else if(current_mode.matches("直線")){

                    }else if (current_mode.matches("橢圓形")){

                    }else if (current_mode.matches("矩形")){

                    }else {

                    }
                    //

                    //每畫完一個點 更新JPanel
                    repaint();
                }

                @Override
                public void mouseMoved(MouseEvent e) {

                }
            });
        }

        //draw ovals in a 4-by-4 bounding box at specified locations on window
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // 記錄在arrayList 的遊標位置，用for loop 方法畫出來
            for (Point point : points_small_size)
                g.fillOval(point.x, point.y, 4, 4); //小size 筆刷的width, height 各為4

            for (Point point : points_medium_size)
                g.fillOval(point.x, point.y, 6, 6); //中size 筆刷的width, height 各為6

            for (Point point : points_big_size)
                g.fillOval(point.x, point.y, 8, 8); //大size 筆刷的width, height 各為8
            //
        }
    }

    private void brush_color_n_clear_Btn_Onclick(){
        brush_color_Btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("點選 筆刷顏色");
            }
        });

        clear_Btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("點選 清除畫面");
            }
        });
    }

    private void fill_checkBox_OnClick(){
      fill_checkBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (fill_checkBox.isSelected()){
                    System.out.println("選擇 填滿");
                }else {
                    System.out.println("取消 填滿");
                }
            }
        });
    }

    private void drawingTools_comboBox_Onclick(){
        JComboBox_drawingTools.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                current_mode = JComboBox_drawingTools.getSelectedItem().toString();

                //筆刷 情況下不能勾選 填滿
                if (current_mode.matches("筆刷")){ //如果 現在是筆刷mode
                    fill_checkBox.setEnabled(false); //填滿checkbox 不開放勾選
                    fill_checkBox.setSelected(false);//checkbox切換回unchecked 的情況
                }else {
                    fill_checkBox.setEnabled(true); //填滿checkbox 開放勾選
                }

                System.out.println("選擇 " + current_mode);
            }
        });
    }

    private void brasher_radioBtn_Onclick(){
        small_size.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (small_size.isSelected()){
                    System.out.println("選擇 小 筆刷");

                    //記錄用戶選了那一個size 的筆刷
                    size_selected = "small";
                }
            }
        });

        medium_size.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (medium_size.isSelected()){
                    System.out.println("選擇 中 筆刷");

                    size_selected = "medium";
                }
            }
        });

        big_size.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (big_size.isSelected()){
                    System.out.println("選擇 大 筆刷");

                    size_selected = "big";
                }
            }
        });
    }

    private void mouse_function(){
        drawingPanel.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseMoved(MouseEvent e) {
                // TODO Auto-generated method stub
                mouse_position_Text.setText("游標位置：" + "(" + String.valueOf(e.getX()) + "," + String.valueOf(e.getY()) + ")");
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                // TODO Auto-generated method stub
                mouse_position_Text.setText("游標位置：" + "(" + String.valueOf(e.getX()) + "," + String.valueOf(e.getY()) + ")");
            }
        });

        drawingPanel.addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mousePressed(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseExited(MouseEvent e) {
                // TODO Auto-generated method stub
                mouse_position_Text.setText("游標位置：( , )");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // TODO Auto-generated method stub

            }
        });
    }
}
