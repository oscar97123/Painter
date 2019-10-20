import javafx.scene.shape.Line;

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
    private int start_point_x;
    private int start_point_y;
    private int end_point_x;
    private int end_point_y;
    private boolean fill_checked = false;
    private Point start_point;
    private Point end_point;
    private Color chosen_color;

    // 用ArrayList記錄 所有繪圖的點
    private ArrayList<Point> points_small_size = new ArrayList<>();
    private ArrayList<Point> points_medium_size = new ArrayList<>();
    private ArrayList<Point> points_big_size = new ArrayList<>();

    private ArrayList<Line> lines_small_size = new ArrayList<>();
    private ArrayList<Line> lines_medium_size = new ArrayList<>();
    private ArrayList<Line> lines_big_size = new ArrayList<>();

    private ArrayList<Line> dotted_lines_small_size = new ArrayList<>();
    private ArrayList<Line> dotted_lines_medium_size = new ArrayList<>();
    private ArrayList<Line> dotted_lines_big_size = new ArrayList<>();

    private ArrayList<Point> circles_noFill_small_size = new ArrayList<>();
    private ArrayList<Point> circles_noFill_medium_size = new ArrayList<>();
    private ArrayList<Point> circles_noFill_big_size = new ArrayList<>();

    private ArrayList<Point> circles_Fill_small_size = new ArrayList<>();
    private ArrayList<Point> circles_Fill_medium_size = new ArrayList<>();
    private ArrayList<Point> circles_Fill_big_size = new ArrayList<>();
    private ArrayList<Integer> circle_distance_x = new ArrayList<>();
    private ArrayList<Integer> circle_distance_y = new ArrayList<>();

    private ArrayList<Point> rectangles_noFill_small_size = new ArrayList<>();
    private ArrayList<Point> rectangles_noFill_medium_size = new ArrayList<>();
    private ArrayList<Point> rectangles_noFill_big_size = new ArrayList<>();

    private ArrayList<Point> rectangles_Fill_small_size = new ArrayList<>();
    private ArrayList<Point> rectangles_Fill_medium_size = new ArrayList<>();
    private ArrayList<Point> rectangles_Fill_big_size = new ArrayList<>();
    private ArrayList<Integer> rectangle_distance_x = new ArrayList<>();
    private ArrayList<Integer> rectangle_distance_y = new ArrayList<>();

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
        eraser = new JButton("橡皮擦");
        brush_color_n_clear_Btn_n_eraser_Onclick(); //按下筆刷顏色 / 清除畫面接鈕後會印出 按下那個button
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

        private int i = 0;
        private int j = 0;

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
//                        points_color.add(chosen_color); //記錄選擇的顏色
                    }
                    //

                    //每畫完一個點 更新JPanel
                    repaint();
                }

                @Override
                public void mouseMoved(MouseEvent e) {

                }
            });

            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {

                }

                @Override
                public void mousePressed(MouseEvent e) {

                    //當用戶按下左鍵時 記錄點擊的位置
                    start_point_x = e.getX();
                    start_point_y = e.getY();

//                    start_point = e.getPoint();
                    //
                }

                @Override
                public void mouseReleased(MouseEvent e) {

                    //用戶放開左鍵時的位置
                    end_point_x = e.getX();
                    end_point_y = e.getY();

//                    end_point = e.getPoint();
                    //

                    //判斷下拉選單選擇了那個繪圖工具
                    if(current_mode.matches("直線")){

                        if (fill_checked){

                            if (size_selected.matches("small")){ // 筆刷小size
                                //記錄從那個點開始畫直線 到完結點
                                lines_small_size.add(new Line(start_point_x, start_point_y, end_point_x, end_point_y));
                                //
                            }else if (size_selected.matches("medium")){ // 筆刷中size

                                lines_medium_size.add(new Line(start_point_x, start_point_y, end_point_x, end_point_y));

                            }else { // 筆刷大size

                                lines_big_size.add(new Line(start_point_x, start_point_y, end_point_x, end_point_y));

                            }

                        }else {

                            //與上面同理，判斷大 中 小 後 記錄在不同的arrayList
                            if (size_selected.matches("small")) {

                                dotted_lines_small_size.add(new Line(start_point_x, start_point_y, end_point_x, end_point_y));

                            }else if (size_selected.matches("medium")) {

                                dotted_lines_medium_size.add(new Line(start_point_x, start_point_y, end_point_x, end_point_y));

                            }else {

                                dotted_lines_big_size.add(new Line(start_point_x, start_point_y, end_point_x, end_point_y));

                            }

                        }

                    }else if (current_mode.matches("橢圓形")){

                        //計算兩點距離 並取 絕對值(防止出現負數)
                        circle_distance_x.add(Math.abs(start_point_x - end_point_x));
                        circle_distance_y.add(Math.abs(start_point_y - end_point_y));
                        //

                        if (fill_checked){

                            //與上面同理，判斷大 中 小 後 記錄在不同的arrayList
                            if (size_selected.matches("small")) {

                                //用min 找出x / y點的最小值 作為畫圖起點，使由下而上的畫圖能正常顯示
                                //再打包成一個"Point" 再記錄在arrayList 中
                                circles_Fill_small_size.add(new Point(Math.min(start_point_x, end_point_x), Math.min(start_point_y, end_point_y)));
                                //

                            }else if (size_selected.matches("medium")) {

                                circles_Fill_medium_size.add(new Point(Math.min(start_point_x, end_point_x), Math.min(start_point_y, end_point_y)));

                            }else {

                                circles_Fill_big_size.add(new Point(Math.min(start_point_x, end_point_x), Math.min(start_point_y, end_point_y)));

                            }

                            }else {

                            //與上面同理，判斷大 中 小 後 記錄在不同的arrayList
                            if (size_selected.matches("small")) {

                                //用min 找出x / y點的最小值 作為畫圖起點，使由下而上的畫圖能正常顯示
                                //再打包成一個"Point" 再記錄在arrayList 中
                                circles_noFill_small_size.add(new Point(Math.min(start_point_x, end_point_x), Math.min(start_point_y, end_point_y)));
                                //

                            }else if (size_selected.matches("medium")) {

                                circles_noFill_medium_size.add(new Point(Math.min(start_point_x, end_point_x), Math.min(start_point_y, end_point_y)));

                            }else {

                                circles_noFill_big_size.add(new Point(Math.min(start_point_x, end_point_x), Math.min(start_point_y, end_point_y)));

                            }
                        }

                    }else if (current_mode.matches("矩形")){

                        //計算兩點距離 並取 絕對值(防止出現負數)
                        rectangle_distance_x.add(Math.abs(start_point_x - end_point_x));
                        rectangle_distance_y.add(Math.abs(start_point_y - end_point_y));
                        //

                        if (fill_checked){

                            //與上面同理，判斷大 中 小 後 記錄在不同的arrayList
                            if (size_selected.matches("small")) {

                                rectangles_Fill_small_size.add(new Point(Math.min(start_point_x, end_point_x), Math.min(start_point_y, end_point_y)));

                            }else if (size_selected.matches("medium")){

                                rectangles_Fill_medium_size.add(new Point(Math.min(start_point_x, end_point_x), Math.min(start_point_y, end_point_y)));

                            }else {

                                rectangles_Fill_big_size.add(new Point(Math.min(start_point_x, end_point_x), Math.min(start_point_y, end_point_y)));

                            }

                        }else {

                            //與上面同理，判斷大 中 小 後 記錄在不同的arrayList
                            if (size_selected.matches("small")) {

                                rectangles_noFill_small_size.add(new Point(Math.min(start_point_x, end_point_x), Math.min(start_point_y, end_point_y)));

                            }else if (size_selected.matches("medium")) {

                                rectangles_noFill_medium_size.add(new Point(Math.min(start_point_x, end_point_x), Math.min(start_point_y, end_point_y)));

                            }else {

                                rectangles_noFill_big_size.add(new Point(Math.min(start_point_x, end_point_x), Math.min(start_point_y, end_point_y)));

                            }

                        }

                    }else {

                    }
                    //


                    i = 0; // reset i = 0  因為會out of bound (每次畫都會把之前的重畫，所以設成0)  用於circle
                    j = 0; // 用於rectangle
                    repaint(); //更新JPanel
                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
        }

        //draw ovals in a 4-by-4 bounding box at specified locations on window
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // 記錄在arrayList 的遊標位置，用for loop 方法畫出來
            for (Point point : points_small_size) {
                g.setColor(chosen_color);
                g.fillOval(point.x, point.y, 4, 4); //小size 筆刷的width, height 各為4
            }

            for (Point point : points_medium_size) {
                g.setColor(chosen_color);
                g.fillOval(point.x, point.y, 6, 6); //中size 筆刷的width, height 各為6
            }

            for (Point point : points_big_size) {
                g.setColor(chosen_color);
                g.fillOval(point.x, point.y, 8, 8); //大size 筆刷的width, height 各為8
            }

            for (Line line : lines_small_size) {
                //creates a copy of the Graphics instance
                Graphics2D g1 = (Graphics2D) g.create();
                g1.setColor(chosen_color);
                g1.setStroke(new BasicStroke(1));
                g1.drawLine((int)line.getStartX(), (int)line.getStartY(), (int)line.getEndX(), (int)line.getEndY());
            }

            for (Line line : lines_medium_size) {
                //creates a copy of the Graphics instance
                Graphics2D g1 = (Graphics2D) g.create();
                g1.setColor(chosen_color);
                g1.setStroke(new BasicStroke(2));
                g1.drawLine((int)line.getStartX(), (int)line.getStartY(), (int)line.getEndX(), (int)line.getEndY());
            }

            for (Line line : lines_big_size) {
                //creates a copy of the Graphics instance
                Graphics2D g1 = (Graphics2D) g.create();
                g1.setColor(chosen_color);
                g1.setStroke(new BasicStroke(3));
                g1.drawLine((int)line.getStartX(), (int)line.getStartY(), (int)line.getEndX(), (int)line.getEndY());

                //gets rid of the copy
                g1.dispose();
            }

            for (Line dotted_line : dotted_lines_small_size) {
                //creates a copy of the Graphics instance
                Graphics2D g1 = (Graphics2D) g.create();

                g1.setColor(chosen_color);
                //set the stroke of the copy, not the original
                Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
                g1.setStroke(dashed);
                g1.drawLine((int)dotted_line.getStartX(), (int)dotted_line.getStartY(), (int)dotted_line.getEndX(), (int)dotted_line.getEndY());

                //gets rid of the copy
                g1.dispose();
            }

            for (Line dotted_line : dotted_lines_medium_size) {
                //creates a copy of the Graphics instance
                Graphics2D g1 = (Graphics2D) g.create();

                g1.setColor(chosen_color);
                //set the stroke of the copy, not the original
                Stroke dashed = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
                g1.setStroke(dashed);
                g1.drawLine((int)dotted_line.getStartX(), (int)dotted_line.getStartY(), (int)dotted_line.getEndX(), (int)dotted_line.getEndY());

                //gets rid of the copy
                g1.dispose();
            }

            for (Line dotted_line : dotted_lines_big_size) {
                //creates a copy of the Graphics instance
                Graphics2D g1 = (Graphics2D) g.create();
                g1.setColor(chosen_color);
                //set the stroke of the copy, not the original
                Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
                g1.setStroke(dashed);
                g1.drawLine((int)dotted_line.getStartX(), (int)dotted_line.getStartY(), (int)dotted_line.getEndX(), (int)dotted_line.getEndY());

                //gets rid of the copy
                g1.dispose();
            }

            for (Point circle_noFIll : circles_noFill_small_size) {
                //creates a copy of the Graphics instance
                Graphics2D g1 = (Graphics2D) g.create();

                g1.setColor(chosen_color);
                if (i < circle_distance_x.size()) { // 防止 out of bound error   (circle_distance_x.size() & circle_distance_y.size() 是一樣的，所以擇一檢查就好)
                    g1.setStroke(new BasicStroke(1));
                    g1.drawOval(circle_noFIll.x, circle_noFIll.y, circle_distance_x.get(i), circle_distance_y.get(i)); //前兩個參數: 開始繪圖的x y點； 後兩個參數：長＆闊
                }
                i++;

                //gets rid of the copy
                g1.dispose();
            }

            for (Point circle_noFIll : circles_noFill_medium_size) {
                //creates a copy of the Graphics instance
                Graphics2D g1 = (Graphics2D) g.create();

                g1.setColor(chosen_color);
                if (i < circle_distance_x.size()) {// 防止 out of bound error   (circle_distance_x.size() & circle_distance_y.size() 是一樣的，所以擇一檢查就好)
                    g1.setStroke(new BasicStroke(2));
                    g1.drawOval(circle_noFIll.x, circle_noFIll.y, circle_distance_x.get(i), circle_distance_y.get(i)); //前兩個參數: 開始繪圖的x y點； 後兩個參數：長＆闊
                }
                i++;

                //gets rid of the copy
                g1.dispose();
            }

            for (Point circle_noFIll : circles_noFill_big_size) {
                //creates a copy of the Graphics instance
                Graphics2D g1 = (Graphics2D) g.create();

                g1.setColor(chosen_color);
                if (i < circle_distance_x.size()) {// 防止 out of bound error   (circle_distance_x.size() & circle_distance_y.size() 是一樣的，所以擇一檢查就好)
                    g1.setStroke(new BasicStroke(3));
                    g1.drawOval(circle_noFIll.x, circle_noFIll.y, circle_distance_x.get(i), circle_distance_y.get(i)); //前兩個參數: 開始繪圖的x y點； 後兩個參數：長＆闊
                }
                i++;

                //gets rid of the copy
                g1.dispose();
            }

            for (Point circle_FIll : circles_Fill_small_size) {
                //creates a copy of the Graphics instance
                Graphics2D g1 = (Graphics2D) g.create();

                g1.setColor(chosen_color);
                if (i < circle_distance_x.size()) { // 防止 out of bound error   (circle_distance_x.size() & circle_distance_y.size() 是一樣的，所以擇一檢查就好)
                    g1.setStroke(new BasicStroke(1));
                    g1.fillOval(circle_FIll.x, circle_FIll.y, circle_distance_x.get(i), circle_distance_y.get(i)); //前兩個參數: 開始繪圖的x y點； 後兩個參數：長＆闊
                }
                i++;

                //gets rid of the copy
                g1.dispose();
            }

            for (Point circle_FIll : circles_Fill_medium_size) {
                //creates a copy of the Graphics instance
                Graphics2D g1 = (Graphics2D) g.create();

                g1.setColor(chosen_color);
                if (i < circle_distance_x.size()) { // 防止 out of bound error   (circle_distance_x.size() & circle_distance_y.size() 是一樣的，所以擇一檢查就好)
                    g1.setStroke(new BasicStroke(2));
                    g1.fillOval(circle_FIll.x, circle_FIll.y, circle_distance_x.get(i), circle_distance_y.get(i)); //前兩個參數: 開始繪圖的x y點； 後兩個參數：長＆闊
                }
                i++;

                //gets rid of the copy
                g1.dispose();
            }

            for (Point circle_FIll : circles_Fill_big_size) {
                //creates a copy of the Graphics instance
                Graphics2D g1 = (Graphics2D) g.create();

                g1.setColor(chosen_color);
                if (i < circle_distance_x.size()) { // 防止 out of bound error   (circle_distance_x.size() & circle_distance_y.size() 是一樣的，所以擇一檢查就好)
                    g1.setStroke(new BasicStroke(3));
                    g1.fillOval(circle_FIll.x, circle_FIll.y, circle_distance_x.get(i), circle_distance_y.get(i)); //前兩個參數: 開始繪圖的x y點； 後兩個參數：長＆闊
                }
                i++;

                //gets rid of the copy
                g1.dispose();
            }

            for (Point rectangle_noFill : rectangles_noFill_small_size) {
                Graphics2D g1 = (Graphics2D) g.create();
                g1.setColor(chosen_color);
                if (j < rectangle_distance_x.size()) { // 防止 out of bound error
                    g1.setStroke(new BasicStroke(1));
                    g1.drawRect(rectangle_noFill.x, rectangle_noFill.y, rectangle_distance_x.get(j), rectangle_distance_y.get(j)); //前兩個參數: 開始繪圖的x y點； 後兩個參數：長＆闊
                }
                j++;

                g1.dispose();
            }

            for (Point rectangle_noFill : rectangles_noFill_medium_size) {
                Graphics2D g1 = (Graphics2D) g.create();
                g1.setColor(chosen_color);
                if (j < rectangle_distance_x.size()) { // 防止 out of bound error
                    g1.setStroke(new BasicStroke(2));
                    g1.drawRect(rectangle_noFill.x, rectangle_noFill.y, rectangle_distance_x.get(j), rectangle_distance_y.get(j)); //前兩個參數: 開始繪圖的x y點； 後兩個參數：長＆闊
                }
                j++;

                g1.dispose();
            }

            for (Point rectangle_noFill : rectangles_noFill_big_size) {
                Graphics2D g1 = (Graphics2D) g.create();
                g1.setColor(chosen_color);
                if (j < rectangle_distance_x.size()) { // 防止 out of bound error
                    g1.setStroke(new BasicStroke(3));
                    g1.drawRect(rectangle_noFill.x, rectangle_noFill.y, rectangle_distance_x.get(j), rectangle_distance_y.get(j)); //前兩個參數: 開始繪圖的x y點； 後兩個參數：長＆闊
                }
                j++;

                g1.dispose();
            }

            for (Point rectangle_Fill : rectangles_Fill_small_size) {
                Graphics2D g1 = (Graphics2D) g.create();
                g1.setColor(chosen_color);
                if (j < rectangle_distance_x.size()) { // 防止 out of bound error
                    g1.setStroke(new BasicStroke(1));
                    g1.fillRect(rectangle_Fill.x, rectangle_Fill.y, rectangle_distance_x.get(j), rectangle_distance_y.get(j)); //前兩個參數: 開始繪圖的x y點； 後兩個參數：長＆闊
                }
                j++;
                g1.dispose();
            }

            for (Point rectangle_Fill : rectangles_Fill_medium_size) {
                Graphics2D g1 = (Graphics2D) g.create();
                g1.setColor(chosen_color);
                if (j < rectangle_distance_x.size()) { // 防止 out of bound error
                    g1.setStroke(new BasicStroke(2));
                    g1.fillRect(rectangle_Fill.x, rectangle_Fill.y, rectangle_distance_x.get(j), rectangle_distance_y.get(j)); //前兩個參數: 開始繪圖的x y點； 後兩個參數：長＆闊
                }
                j++;
                g1.dispose();
            }

            for (Point rectangle_Fill : rectangles_Fill_big_size) {
                Graphics2D g1 = (Graphics2D) g.create();
                g1.setColor(chosen_color);
                if (j < rectangle_distance_x.size()) { // 防止 out of bound error
                    g1.setStroke(new BasicStroke(3));
                    g1.fillRect(rectangle_Fill.x, rectangle_Fill.y, rectangle_distance_x.get(j), rectangle_distance_y.get(j)); //前兩個參數: 開始繪圖的x y點； 後兩個參數：長＆闊
                }
                j++;
                g1.dispose();
            }
            //
        }
    }

    private void brush_color_n_clear_Btn_n_eraser_Onclick(){
        brush_color_Btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("點選 筆刷顏色");

                // 選好的顏色存在chosen_color
                //title 設為"選擇顏色"
                //預設顏色 黑色
                chosen_color = JColorChooser.showDialog(drawingPanel, "選擇顏色", Color.BLACK);
                //
            }
        });

        clear_Btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("點選 清除畫面");

                // 清除所有的繪圖
                drawingPanel.validate();
                remove_all_arrayList_value(); //function內實作 清除ArrayList 所有記錄
                drawingPanel.repaint();
                //
            }
        });

        eraser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("點選 橡皮擦");
            }
        });
    }

    private void remove_all_arrayList_value() {
        // 清除ArrayList 所有記錄
        points_small_size.clear();
        points_medium_size.clear();
        points_big_size.clear();
        lines_small_size.clear();
        lines_medium_size.clear();
        lines_big_size.clear();
        dotted_lines_small_size.clear();
        dotted_lines_medium_size.clear();
        dotted_lines_big_size.clear();
        circles_noFill_small_size.clear();
        circles_noFill_medium_size.clear();
        circles_noFill_big_size.clear();
        circles_Fill_small_size.clear();
        circles_Fill_medium_size.clear();
        circles_Fill_big_size.clear();
        circle_distance_x.clear();
        circle_distance_y.clear();
        rectangles_noFill_small_size.clear();
        rectangles_noFill_medium_size.clear();
        rectangles_noFill_big_size.clear();
        rectangles_Fill_small_size.clear();
        rectangles_Fill_medium_size.clear();
        rectangles_Fill_big_size.clear();
        rectangle_distance_x.clear();
        rectangle_distance_y.clear();
        //
    }

    private void fill_checkBox_OnClick(){
      fill_checkBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (fill_checkBox.isSelected()){
                    System.out.println("選擇 填滿");

                    fill_checked = true;
                }else {
                    System.out.println("取消 填滿");

                    fill_checked = false;
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
