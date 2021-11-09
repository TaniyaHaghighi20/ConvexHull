import java.awt.EventQueue;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Color;

public class MainFrame extends JFrame  {

    private JPanel contentPane;
    private JRadioButton blindSearch_btn;
    private JRadioButton grahamSearch_btn;
    private JRadioButton quickHull_btn;
    private JRadioButton drawLine;
    private JCheckBox chckbxShowXAnd;
    private JPanel drawingPanel;

    private ButtonGroup buttonGroup = new ButtonGroup();
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainFrame frame = new MainFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public MainFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1404, 775);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        drawingPanel = new JPanel();
        drawingPanel.setBackground(Color.WHITE);
        drawingPanel.setForeground(Color.WHITE);

        drawingPanel.setBounds(12, 13, 1191, 702);
        contentPane.add(drawingPanel);

        blindSearch_btn = new JRadioButton("BlindSearch");
        blindSearch_btn.setBounds(1235, 51, 127, 25);
        contentPane.add(blindSearch_btn);

        grahamSearch_btn = new JRadioButton("Graham Scan");
        grahamSearch_btn.setBounds(1235, 120, 127, 25);
        contentPane.add(grahamSearch_btn);

        quickHull_btn = new JRadioButton("Quick hull");
        quickHull_btn.setBounds(1235, 200, 127, 25);
        contentPane.add(quickHull_btn);

        drawLine = new JRadioButton("Draw line");
        drawLine.setBounds(1235, 280, 127, 25);
        contentPane.add(drawLine);

        chckbxShowXAnd = new JCheckBox("show x and y");
        chckbxShowXAnd.setBounds(1235, 360, 127, 25);
        contentPane.add(chckbxShowXAnd);

        buttonGroup.add(blindSearch_btn);
        buttonGroup.add(grahamSearch_btn);
        buttonGroup.add(quickHull_btn);
        buttonGroup.add(drawLine);
    }


    @Override
    public JPanel getContentPane() {
        return contentPane;
    }

    public JRadioButton getBlindSearch_btn() {
        return blindSearch_btn;
    }

    public JRadioButton getGrahamSearch_btn() {
        return grahamSearch_btn;
    }

    public JRadioButton getQuickHull_btn() {
        return quickHull_btn;
    }

    public JCheckBox getChckbxShowXAnd() {
        return chckbxShowXAnd;
    }

    public JPanel getDrawingPanel() {
        return drawingPanel;
    }

    public JRadioButton getDrawLine() {
        return drawLine;
    }
}


