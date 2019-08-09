import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


@SuppressWarnings("serial")
public class GamePanel extends JPanel implements MouseMotionListener, MouseListener {
    public static int xStart = 600, yStart = 450, xCurrent, yCurrent;   //tracks mouse positions
    public static boolean mPressed = false, woodBack = true, steelBack, skyBack;    //tracks mouse press, tracks background choice
    private AffineTransform[] fruitLocation = new AffineTransform[5];   //used for rotation of objects
    private BufferedImage wood = LoadImage("wood.png");
    private BufferedImage steel = LoadImage("steel.png");
    private BufferedImage sky = LoadImage("sky.png");
    private BufferedImage start = LoadImage("start.png");

    public GamePanel() {
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
        this.setDoubleBuffered(true);
    }

    //Method: LoadImage
    //Purpose: get image location
    //Parameter: file string
    public static BufferedImage LoadImage(String FileName) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(FileName));
        } catch (IOException e) {
        }
        return img;
    }

    //Method: PainComponent
    //Purpose: paints the jpanel based on criteria
    //Parameter: graphics g

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Calibri", Font.BOLD, 25));
        if (Game.playGame) {
            if (GamePanel.woodBack) {   //check for background choice
                g2.drawImage(wood, null, null);
            } else if (GamePanel.steelBack) {
                g2.drawImage(steel, null, null);
            } else if (GamePanel.skyBack) {
                g2.drawImage(sky, null, null);
            }
            g.drawString("Score: " + Game.count + "           Live(s) Remaining: " + Game.life, 800, 50);   //print score and lives
            g.drawString("Bomb Safeguard: " + Game.safe, 800, 100); //print safeguard status
            for (int i = 0; i < 5; i++) {   //loops through 5 objects and prints them if they haven't been sliced
                if (!Game.fruitQueue.get(i + 1).getSliced() == true) {
                    fruitLocation[i] = AffineTransform.getTranslateInstance(Game.fruitQueue.get(i + 1).getCenterX(), Game.fruitQueue.get(i + 1).getCenterY());
                    fruitLocation[i].rotate(Math.toRadians(Game.fruitQueue.get(i + 1).getRotation()), Game.fruitQueue.get(i + 1).getImage().getWidth() / 2, Game.fruitQueue.get(i + 1).getImage().getHeight() / 2);
                    g2.drawImage(Game.fruitQueue.get(i + 1).getImage(), fruitLocation[i], null);
                }

            }
            g.drawLine(xStart, yStart, xCurrent, yCurrent); //draws the slice line
        } else if (Game.playGame == false) {    //if game hasn't started, draw splash screen
            g2.drawImage(start, null, null);
            g.setFont(new Font("ARIAL", Font.BOLD, 30));
            g.drawString("Highscore: " + Game.highScore, 25, 50);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {    //mouse listener methods
        GamePanel.xStart = GamePanel.xCurrent;
        GamePanel.yStart = GamePanel.yCurrent;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        xCurrent = e.getX();
        yCurrent = e.getY();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mPressed = true;
        GamePanel.xStart = GamePanel.xCurrent;
        GamePanel.yStart = GamePanel.yCurrent;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mPressed = false;
    }
}
