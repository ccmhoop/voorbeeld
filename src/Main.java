import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Main {

    private ArrayList<BufferedImage> facingRightAnimation = new ArrayList<>();
    private ArrayList<BufferedImage> facingLeftAnimation = new ArrayList<>();
    public static ArrayList<Integer> tileYAxisCollision = new ArrayList<>();
    private String playerAction;
    public int playerPositionXAxis,playerPositionYAxis,playerSpeed=2;
    public int animationIndex;
    private final int defaultGravity = 5;
    private int timer,timeCycle,animationTimer;
    private int playerCollisionY,collisionPredictYAxis;
    public boolean jumpActivated,toggleGravity,faceRight;

    //--------------------------------------------------------------------------------------\\
    /*
     *-Loads the needed pngs for the players animations
     * -------------------------------------------------
     *-setAnimationArray(); can be modified to accept different animations.
     *-animations from different folders can be used. By adding directorNumber++; after current animation are loaded
     *-add a switch case for each directory++';
     *-use the animationPngPath(); method to in the ARRAY.add to change the path to the new folder;
     */

    public void setAnimationArray(int pngLoadAmount, int directoryNumber) {
        for (int i = 1; i <= pngLoadAmount; i++) {
            try {
                facingLeftAnimation.add(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(animationPngPath(directoryNumber, i, "Left")))));
                facingRightAnimation.add(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(animationPngPath(directoryNumber, i, "Right")))));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String animationPngPath(int numberDirectory, int fileNumber, String directoryName) {
        return "/res/playerAnimation/" + numberDirectory + directoryName + "/" + directoryName + " " + "(" + fileNumber + ").png";
    }

    private void playerAnimationDrawer(Graphics2D g2) {
        if (faceRight) {
            g2.drawImage(facingRightAnimation.get(animationIndex), playerPositionXAxis, playerPositionYAxis, 128, 128, null);
        } else {
            g2.drawImage(facingLeftAnimation.get(animationIndex), playerPositionXAxis, playerPositionYAxis, 128, 128, null);
        }
    }

    //--------------------------------------------------------------------------------------\\

    public int timeCycles(int timerSpeed, int timerCycles) {
        timer++;
        if (timer == timerSpeed) {
            timeCycle++;
        }
        if (timer > timerSpeed) {
            timer = 0;
        }
        if (timeCycle > timerCycles) {
            timeCycle = 0;
            timer = 0;
        }
        return timeCycle;
    }

    public void animationArrayIndexCycles(int speed, int indexStart, int indexEnd) {
        animationTimer++;
        if (animationTimer == speed) {
            animationIndex++;
        }
        if (animationIndex > indexEnd || animationIndex < indexStart) {
            animationIndex = indexStart;
        }
        if (animationTimer > speed) {
            animationTimer = 0;
        }
    }

    //--------------------------------------------------------------------------------------\\

    private void playerInput() {

        playerAction = KeyboardInputs.direction;

        if (keyBoard.leftPressed) {
            playerPositionXAxis -= playerSpeed;
            faceRight = false;
        } else if (keyBoard.rightPressed) {
            playerPositionXAxis += playerSpeed;
            faceRight = true;
        } else {
            playerAction = "idle";
        }
        if (keyBoard.spaceBar) {
            if (toggleGravity) {
                jumpActivated = true;
            }
        }
    }

    private void jump(int jumpSpeed,int jumpStages) {
        if (jumpActivated) {
            switch (timeCycles(jumpSpeed, jumpStages)) {
                //windup\\
                case 3, 4 -> playerPositionYAxis -= 5;
                case 5, 6 -> playerPositionYAxis -= 2;
                case 7, 8 -> playerPositionYAxis -= 1;
                case 9 -> jumpActivated = false;
            }
        }
    }

    //--------------------------------------------------------------------------------------\\

    public int gravity(int entityPositionYAxis) {
        if (toggleGravity) {
            return entityPositionYAxis;
        } else {
            return entityPositionYAxis + defaultGravity;
        }
    }

    private void collisionPrediction() {
        for (int i = 10; 0 < i; i--) {
            collisionPredictYAxis = playerCollisionY - i;
            if (tileYAxisCollision.contains(collisionPredictYAxis)) {
            }
        }
    }

    private boolean setTileCollision(int tileNumber) {
        switch (tileNumber) {
            case 2, 6, 5 -> {
                return true;
            }
        }
        return false;
    }

    //--------------------------------------------------------------------------------------\\
    /*
    *-Separate timer method to split up The timer and the cycleUpdate
    */

    public int timeCycles(int timerCycleSpeed, int cycleAmount) {
        timer++;
        if (timer == timerCycleSpeed) {
            timeCycle++;
        }
        if (timer > timerCycleSpeed) {
            timer = 0;
        }
        return timeCycle = timeCycleUpdate(cycleAmount);
    }

    private int timeCycleUpdate(int cycleAmount) {
        if (timeCycle > cycleAmount) {
            timeCycle = 0;
            timer = 0;
        }
        return timeCycle;
    }

    //--------------------------------------------------------------------------------------\\

    /*
     *-Separates animationCycler and animationUpdater method;
     *-Controls Animation Speed and cycles through array to get the requested animations
     *-For more information read animationArrayPosition.txt in "/res/playerAnimation"
     */

    public void animationPngCycles(int speed, int indexCycleStart, int indexCycleEnd) {
        animationTimer++;
        if (animationIndex > indexCycleEnd || animationIndex < indexCycleStart) {
            animationIndex = indexCycleStart;
        }
        if (animationTimer > speed) {
            animationTimer = 0;
        }
        animationIndexUpdater(speed, animationTimer);
    }

    private void animationIndexUpdater(int speed, int timer) {
        if (timer == speed) {
            animationIndex++;
        }
    }

}