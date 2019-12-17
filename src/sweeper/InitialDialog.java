package sweeper;

        import javax.swing.*;
        import java.awt.*;
        import java.awt.event.ActionEvent;

class InitialDialog extends JDialog{
    private final static int BOMBS_N = 10;
    private final static int COLS_N = 9;
    private final static int ROWS_N = 9;

    private JLabel label;
    private JSpinner bombs;
    private JSpinner cols;
    private JSpinner rows;

    private JPanel panel;
    private JavaSweeper parent;
    private Font font;

    InitialDialog(JavaSweeper parent) {
        super(parent, "Новая игра", true);
        this.parent = parent;
        font = new Font(Font.SERIF, Font.PLAIN, 20);
        setFont(font);
        label = new JLabel("", SwingConstants.LEFT);
        label.setHorizontalTextPosition(SwingConstants.LEFT);
        label.setFont(font);
        bombs = new JSpinner(new SpinnerNumberModel(BOMBS_N, 1, 1000, 1));
        bombs.setFont(font);
        cols = new JSpinner(new SpinnerNumberModel(COLS_N, 1, 100, 1));
        cols.setFont(font);
        rows = new JSpinner(new SpinnerNumberModel(ROWS_N, 1, 100, 1));
        rows.setFont(font);
        initPanel();
        getContentPane().add(panel);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        pack();
        setSize(400, 220);
        setLocationRelativeTo(null);

    }

    void setText(String text) {
        label.setText(text);
    }

    private void initPanel() {
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(label);
        JPanel subPanel = new JPanel(new GridLayout(3, 2));
        JLabel tmp = new JLabel("Количество бомб");
        tmp.setFont(font);
        subPanel.add(tmp);
        subPanel.add(bombs);
        tmp = new JLabel("Количество столбцов");
        tmp.setFont(font);
        subPanel.add(tmp);
        subPanel.add(cols);
        tmp = new JLabel("Количество строк");
        tmp.setFont(font);
        subPanel.add(tmp);
        subPanel.add(rows);
        panel.add(subPanel);
        JButton redeem = new JButton("ОК");
        redeem.setFont(font);
        redeem.addActionListener(this::redeemClicked);
        panel.add(redeem);
    }

    private void redeemClicked(ActionEvent e) {
        int bombsN = getBombsN();
        int colsN = getColsN();
        int rowsN = getRowsN();
        if (bombsN > colsN * rowsN) {
            label.setText("Слишком много бомб!");
            return;
        }
        parent.restartGame(getColsN(), getRowsN(), getBombsN());
        setVisible(false);
        label.setText("Начальные параметры");
    }

    int getBombsN() {
        return (int) bombs.getValue();
    }

    int getColsN() {
        return (int) cols.getValue();
    }

    int getRowsN() {
        return (int) rows.getValue();
    }
}