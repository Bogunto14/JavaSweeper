package sweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JavaSweeper extends JFrame {
    private Game game;
    private JPanel panel;
    private  final int IMAGE_SIZE = 50;
    private static InitialDialog dialog;

    public static void main(String[] args) {
        new JavaSweeper();
    }

    public JavaSweeper(){
        dialog = new InitialDialog(this);
        setImages();
        restartGame(dialog.getColsN(), dialog.getRowsN(), dialog.getBombsN());
        showRestartDialog("Начальные параметры");
    }

    static void showRestartDialog(String text) {
        dialog.setText(text);
        dialog.setVisible(true);
    }

    void restartGame(int cols, int rows, int bombs) {
        game = new Game(cols, rows, bombs);
        game.start();
        initPanel();
        initFrame();
    }

    private void initPanel() {
        panel = new JPanel() {

            @Override
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                for (Coord coord : Ranges.getAllCoords()) {
                    g.drawImage((Image) game.getBox(coord).image,
                            coord.x * IMAGE_SIZE,
                            coord.y * IMAGE_SIZE, this);
                }
            }
        };

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX() / IMAGE_SIZE;
                int y = e.getY() / IMAGE_SIZE;

                Coord coord = new Coord(x,y);
                if (!Ranges.inRange(coord)) return;
                if (e.getButton() == MouseEvent.BUTTON1)
                    game.pressLeftButton(coord);
                if (e.getButton() == MouseEvent.BUTTON3)
                    game.pressRightButton(coord);
                if (e.getButton() == MouseEvent.BUTTON2)
                    restartGame(dialog.getColsN(), dialog.getRowsN(), dialog.getBombsN());
                panel.repaint();
            }
        });

        panel.setPreferredSize(new Dimension(
                Ranges.getSize().x * IMAGE_SIZE,
                Ranges.getSize().y * IMAGE_SIZE));
        add (panel);
    }

    public JPanel getPanel() {
        return panel;
    }

    public void drawClickShadow(Coord coord, Color color) {
        Graphics g = panel.getGraphics();
        g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 200));
        g.drawRect(coord.x * IMAGE_SIZE, coord.y * IMAGE_SIZE, IMAGE_SIZE, IMAGE_SIZE);
    }

    private void initFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Сапёр");
        setResizable(false);
        setVisible(true);
        pack();
        setLocationRelativeTo(null);
        setIconImage(getImage("icon"));
    }

    private void setImages(){
        for (Box box : Box.values())
            box.image = getImage(box.name().toLowerCase());
    }

    private Image getImage (String name) {
        String filename = "/img/" + name + ".png";
        ImageIcon icon = new ImageIcon(getClass().getResource(filename));
        return icon.getImage();
    }

    public Game getGame() {
        return game;
    }
}

