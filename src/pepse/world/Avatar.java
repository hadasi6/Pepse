package pepse.world;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.ImageRenderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

public class Avatar extends GameObject {
    private static final float VELOCITY_X = 400;
    private static final float VELOCITY_Y = -650;
    private static final float GRAVITY = 600;

    private static final int MAX_ENERGY = 100;
    private static final float ENERGY_GAIN_IDLE = 1;
    private static final float ENERGY_LOSS_RUN = 0.5f;
    private static final float ENERGY_LOSS_JUMP = 10;

    private float energy; //todo - change var
    private final UserInputListener inputListener;

    public Avatar(Vector2 topLeftCorner, UserInputListener inputListener, ImageReader imageReader) {
        super(topLeftCorner, Vector2.ONES.mult(50), new ImageRenderable(imageReader.readImage("assets" +
                "/idle_0.png", true).getImage()));
        this.inputListener = inputListener;
        this.energy = MAX_ENERGY;
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float xVel = 0;
//        boolean isRunning = false;

        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT) && inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            // Do nothing if both keys are pressed
            return;
        }else if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y()==0 && energy >= ENERGY_LOSS_JUMP) {
            transform().setVelocityY(VELOCITY_Y);
            this.energy -= ENERGY_LOSS_JUMP;
        } else if (inputListener.isKeyPressed(KeyEvent.VK_LEFT) && energy >= ENERGY_LOSS_RUN) {
            xVel -= VELOCITY_X;// left arrow
            this.energy -= ENERGY_LOSS_RUN;
//            isRunning = true;
        }else if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)){
            xVel += VELOCITY_X;// right arrow
            this.energy -= ENERGY_LOSS_RUN;
        } else {
            this.energy = Math.min(energy+ENERGY_GAIN_IDLE, MAX_ENERGY);
        }
        transform().setVelocityX(xVel);
    }

    public float getEnergy() {
        return energy;
    }
}
