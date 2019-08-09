import java.awt.image.BufferedImage;


public class HitObjects {


    private static double maxSpeed; //max tempo of falling speed
    public int rotation = 0;
    public String type;
    public double xPos, yPos, centerX, centerY;     //positions of object
    public BufferedImage x; //image file associated with it's type
    public int radius;  //Diameter of each object
    double yVelocity = 0;   //starts at 0 per object
    private double gravity = 0.05;
    private boolean sliced = false;     //tracks status of object
    private boolean isBomb = false;

    public HitObjects(String t, int r, BufferedImage i) {
        this.radius = r;
        this.x = i;
        this.type = t;
    }

    public static HitObjects hitGen() {
        double random = Math.random() * 100;    //random number 0-100
        HitObjects hitOb;
        if (random <= 15) {     //create object based on number
            hitOb = new HitObjects("apple", 75, GamePanel.LoadImage("apple.png"));
        } else if (random <= 30) {
            hitOb = new HitObjects("watermelon", 200, GamePanel.LoadImage("watermelon.png"));
        } else if (random <= 45) {
            hitOb = new HitObjects("orange", 100, GamePanel.LoadImage("orange.png"));
        } else if (random <= 60) {
            hitOb = new HitObjects("coconut", 150, GamePanel.LoadImage("coconut.png"));
        } else if (random <= 63) {
            hitOb = new HitObjects("life", 100, GamePanel.LoadImage("life.png"));
        } else if (random <= 66) {
            hitOb = new HitObjects("safe", 100, GamePanel.LoadImage("safe.png"));
        } else {
            hitOb = new HitObjects("bomb", 175, GamePanel.LoadImage("bomb.png"));
            hitOb.setBomb(true);
        }
        double randomX = (Math.random() * 1000) + 100;  //random x spawn point
        double randomY = -(Math.random() * 5000) - 500; //random y spawn point
        hitOb.setXPos(randomX);
        hitOb.setYPos(randomY);
        hitOb.centerX = hitOb.xPos - (hitOb.radius / 2);
        hitOb.centerY = hitOb.yPos - (hitOb.radius / 2);
        return hitOb;
    }

    //Method: Fall
    //Purpose: Controls the falling portion of each object
    //Parameters: Void
    public void fall() {
        yPos += yVelocity;
        centerY = yPos;
        if (!this.sliced) {
            yVelocity += gravity;
            if (yVelocity > maxSpeed) {
                yVelocity = maxSpeed;
            }
        }
    }

    public int getRotation() {
        this.rotation++;
        return rotation;
    }

    public BufferedImage getImage() {
        return x;
    }

    public double getYPos() {
        return yPos;
    }

    public void setYPos(double y) {
        this.yPos = y;
    }

    public void setXPos(double x) {
        this.xPos = x;
    }

    public boolean getSliced() {
        return sliced;
    }

    public void setSliced(boolean sliced) {
        this.sliced = sliced;
    }

    public boolean isBomb() {
        return isBomb;
    }

    public void setBomb(boolean isBomb) {
        this.isBomb = isBomb;
    }

    public String getType() {
        return type;
    }

    public double getCenterX() {
        return centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    public static void setMaxSpeed() {
        HitObjects.maxSpeed += 0.25;
    }

    public static void resetMaxSpeed() {
        HitObjects.maxSpeed = 5;
    }
}




