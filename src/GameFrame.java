import javax.swing.*;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;

@SuppressWarnings("serial")
public class GameFrame extends JFrame implements AWTEventListener {
    public static boolean pressed;
    JMenu changeBack = new JMenu("Background");     //jmenu options
    JMenuItem one = new JMenuItem("Wooden");
    JMenuItem two = new JMenuItem("Steel");
    JMenuItem three = new JMenuItem("Sky");
    JMenu changeMusic = new JMenu("Music");
    JMenuItem eh = new JMenuItem("Eh");
    JMenuItem power = new JMenuItem("Power");
    JMenuItem give = new JMenuItem("Spice Girls");
    JMenuItem instructions = new JMenuItem("Instructions");

    public GameFrame() {
        super("Fruit Ninja");
        this.getToolkit().addAWTEventListener(this, AWTEvent.KEY_EVENT_MASK);
        this.setSize(1200, 900);    //size
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JMenuBar bar = new JMenuBar();
        this.setJMenuBar(bar);
        GamePanel panel = new GamePanel(); //give each jmenu option a listener
        one.addActionListener(ev -> {     //background options
            GamePanel.skyBack = false;
            GamePanel.woodBack = true;
            GamePanel.steelBack = false;
        });

        two.addActionListener(ev -> {
            GamePanel.skyBack = false;
            GamePanel.woodBack = false;
            GamePanel.steelBack = true;
        });
        three.addActionListener(ev -> {
            GamePanel.skyBack = true;
            GamePanel.woodBack = false;
            GamePanel.steelBack = false;
        });
        instructions.addActionListener(ev -> JOptionPane.showMessageDialog(null, "1. Click mouse or press any key to create a line\n2. Move mouse to move the line's end point\n3. Touch a bomb and you lose\n4. Miss a fruit and you lose a life\n5. A shield gives you a one use protection from bomb\n6. A life gives you an additional life\n7. Don't lose"));
        eh.addActionListener(ev -> {
            FruitGen.power.stop();  //music options
            FruitGen.give.stop();
            FruitGen.eh.loop();
        });
        power.addActionListener(ev -> {
            FruitGen.eh.stop();
            FruitGen.give.stop();
            FruitGen.power.loop();

        });

        give.addActionListener(ev -> {
            FruitGen.power.stop();
            FruitGen.eh.stop();
            FruitGen.give.loop();
        });
        changeBack.add(one);    //add each item to menu
        changeBack.add(two);
        changeBack.add(three);
        changeMusic.add(eh);
        changeMusic.add(power);
        changeMusic.add(give);
        bar.add(changeBack);
        bar.add(changeMusic);
        bar.add(instructions);
        this.add(panel);
        this.setVisible(true);
    }

    @Override
    public void eventDispatched(AWTEvent event) {   //key listener
        if (event instanceof KeyEvent) {
            KeyEvent key = (KeyEvent) event;
            if (key.getID() == KeyEvent.KEY_PRESSED) { //Handle key presses
                GameFrame.pressed = true;
                GamePanel.xStart = GamePanel.xCurrent;
                GamePanel.yStart = GamePanel.yCurrent;
                key.consume();
            }
            if (key.getID() == KeyEvent.KEY_RELEASED) { //Handle key releases
                GameFrame.pressed = false;
            }
        }
    }
}




