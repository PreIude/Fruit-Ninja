import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

//Purpose: Slice fruits to get score
//By: Jing Xuan long
//Date: 6/16/2017

public class Game {

    public static GameFrame f = new GameFrame();    //game frame
    public static int count, speedIncrease = 0;     //total score and fruit tempo tracker
    public static int life = 3;     //start with 3 lives
    public static HashMap<Integer, HitObjects> fruitQueue = new HashMap<>();    //fruits are stored and created in a hash-map numbered 1-5
    public static boolean playGame = false;
    public static boolean exit = false;
    public static boolean safe = false; //tracks safety power-up
    public static boolean increase = false;     //tracks when to increase tempo
    public static int highScore;
    public static FruitGen gen = new FruitGen();

    public static void main(String[] args) throws FileNotFoundException {
        FruitGen.eh.loop();
        try {   //read highscore text
            FileReader fr = new FileReader("score.txt");
            BufferedReader br = new BufferedReader(fr);
            highScore = Integer.parseInt(br.readLine());
        } catch (IOException e) {
        }
        while (exit == false) {
            System.out.print("");
            if (GameFrame.pressed == true || GamePanel.mPressed == true) {  //wait for user input before beginning game
                playGame = true;
            }
            if (playGame) {
                Game.startGame();
            }
        }
        if (life == 0) {    //lost and user wants to exit
            f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
        }
    }

//start game and main thread
    public static void startGame() {
        life = 3;
        count = 0;
        safe = false;
        HitObjects.resetMaxSpeed();
        fruitQueue.clear();
        for (int i = 1; i <= 5; i++) {  //creates 5 objects
            fruitQueue.put(i, HitObjects.hitGen());
        }
        gen.run();
    }

    public static boolean isIncrease() {
        return increase;
    }

    public static void setIncrease(boolean x) {
        increase = x;
    }

    public static GameFrame getFrame() {
        return f;
    }
}
