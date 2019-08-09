import javax.swing.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import static java.lang.Math.sqrt;

public class FruitGen extends Thread {
    public static AudioClip power = Applet.newAudioClip(getCompleteURL("power.wav"));
    public static AudioClip eh = Applet.newAudioClip(getCompleteURL("eh.wav"));
    public static AudioClip give = Applet.newAudioClip(getCompleteURL("give.wav"));
    private static AudioClip sliced = Applet.newAudioClip(getCompleteURL("sliced.wav"));
    private static AudioClip explosion = Applet.newAudioClip(getCompleteURL("explosion.wav"));
    private static AudioClip life = Applet.newAudioClip(getCompleteURL("life.wav"));
    private static AudioClip safe = Applet.newAudioClip(getCompleteURL("safe.wav"));
    private static AudioClip heart = Applet.newAudioClip(getCompleteURL("heart.wav"));
    private static AudioClip fell = Applet.newAudioClip(getCompleteURL("fell.wav"));


    //Method: getCompleteURL
    //Purpose: grabs file location of specific file
    //Parameters: filename
    public static URL getCompleteURL(String fileName) {
        try {
            return new URL("file:" + System.getProperty("user.dir") + "/" + fileName);
        } catch (MalformedURLException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }
    //Method: LineCheck
    //Purpose: Check if line intersects with both sides of circle object
    //Parameters: Object center x/y, Radius, line start, line end
    public static boolean lineCheck(double centerX, double centerY, double radius, double x1, double y1, double x2, double y2) {
        double deltaX = x2 - x1;
        double deltaY = y2 - y1;
        double a = deltaX * deltaX + deltaY * deltaY;
        double b = 2 * (deltaX * (x1 - centerX) + deltaY * (y1 - centerY));
        double c = (x1 - centerX) * (x1 - centerX) + (y1 - centerY) * (y1 - centerY) - radius * radius;
        double d = b * b - 4 * a * c;
        if (d < 0)  //root is imaginary which means no intersections
            return false;
        double quad1 = (-b + sqrt(d)) / (2 * a);    //use quadratic equation for both
        double quad2 = (-b - sqrt(d)) / (2 * a);
        if (quad1 >= 0 && quad1 <= 1) {     //if both are true then condition has been met
            if (quad2 >= 0 && quad2 <= 1) {
                return true;
            }
        }
        return false;
    }


    //Method: FruitCheck
    //Purpose: First checks for line intersection then controls what happens when things are sliced/not sliced
    //Parameter: Object number
    public static void fruitCheck(int x) {
        if (lineCheck(Game.fruitQueue.get(x).xPos, Game.fruitQueue.get(x).yPos, Game.fruitQueue.get(x).radius / 2, GamePanel.xStart, GamePanel.yStart, GamePanel.xCurrent, GamePanel.yCurrent)) {   //check for intersection
            if (!Game.fruitQueue.get(x).getType().equals("bomb") && !Game.fruitQueue.get(x).getType().equals("life") && !Game.fruitQueue.get(x).getType().equals("safe")) {     //slice is successful
                sliced.play();
                Game.fruitQueue.get(x).setSliced(true);     //set object as sliced for disposal
                Game.fruitQueue.put(x, HitObjects.hitGen());      //create a new object to take it's place
                Game.count++;   //add to score
                Game.speedIncrease++;
                if (Game.speedIncrease >= 5) {  //if 5 fruits have been sliced, increase tempo
                    Game.speedIncrease = 0;
                    Game.setIncrease(true);
                }
            } else if (Game.fruitQueue.get(x).getType().equals("bomb")) {   //sliced or touched a bomb
                if (Game.safe == true) {    //if safeguard is on, live through bomb
                    life.play();
                    Game.fruitQueue.get(x).setSliced(true);
                    Game.fruitQueue.put(x, HitObjects.hitGen());
                    Game.safe = false;
                } else if (Game.safe == false) {    //else you lost
                    explosion.play();
                    Game.life = 0;
                }
            } else if (Game.fruitQueue.get(x).getType().equals("life")) {   //sliced a life
                heart.play();
                Game.fruitQueue.get(x).setSliced(true);
                Game.fruitQueue.put(x, HitObjects.hitGen());
                Game.life++;    //gain a life
            } else if (Game.fruitQueue.get(x).getType().equals("safe")) {   //sliced a safeguard
                safe.play();
                Game.fruitQueue.get(x).setSliced(true);
                Game.fruitQueue.put(x, HitObjects.hitGen());
                Game.safe = true;   //gain safeguard from bomb
            }
        }
        Game.fruitQueue.get(x).fall();  //make object fall
        if (Game.fruitQueue.get(x).getYPos() > 900) {   //checks if object has gone off screen
            if (!Game.fruitQueue.get(x).isBomb() && !Game.fruitQueue.get(x).getType().equals("life") && !Game.fruitQueue.get(x).getType().equals("safe") && Game.fruitQueue.get(x).getSliced() == false) {  //if not a bomb or power  lose a life
                fell.play();
                Game.life--;
            }
            Game.fruitQueue.put(x, HitObjects.hitGen());    //replace object
        }
    }

    public void run() {
        while (Game.life > 0) {     //runs while not dead
            if (Game.isIncrease()) {    //checks if tempo can be increased
                HitObjects.setMaxSpeed();
                Game.setIncrease(false);
            }
            Game.getFrame().repaint();
            for (int i = 1; i < 6; i++) {   //checks each fruit for line intersection
                fruitCheck(i);
            }
            try {   //small delay
                Thread.sleep(5);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
        Game.highScore = Math.max(Game.count, Game.highScore);  //compares scores
        if (Game.count > Game.highScore) {     //if score is greater than highscore replace it and put into txt
            try {
                try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream("score.txt"), "utf-8"))) {
                    writer.write(Game.highScore);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileWriter in = null;
        try {
            in = new FileWriter(new File("score.txt"));
            String s = "" + Game.highScore;
            in.write(s);
            in.close();
        } catch (IOException e) {
            try {
                new FileOutputStream("score.txt", true).close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        int reply = JOptionPane.showConfirmDialog(null, "Play Again?",  //ask if user wants to play again
                "Highscore: " + Game.highScore, JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            Game.playGame = false;
            Game.f.repaint();
        } else {    //if not exit game
            Game.exit = true;
        }
    }
}


