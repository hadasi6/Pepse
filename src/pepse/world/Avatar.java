package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.ImageRenderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * The avatar of the game.
 * author: @Hadas
 */
public class Avatar extends GameObject {
    // Constants
    private static final float VELOCITY_X = 400;
    private static final float VELOCITY_Y = -650;
    private static final float GRAVITY = 600;
    private static final float AMOUNT = 10;

    private static final int MAX_ENERGY = 100;
    private static final float ENERGY_GAIN_IDLE = 1;
    private static final float ENERGY_LOSS_RUN = 0.5f;
    private static final float ENERGY_LOSS_JUMP = 10;

    // Variables
    private float energy;
    private final UserInputListener inputListener;
    // Observers
    private final List<EnergyObserver> observers = new ArrayList<>();
    private final List<JumpListener> jumpListeners = new ArrayList<>();

    // Animations
    private final AnimationRenderable idleAnimation;
    private final AnimationRenderable runAnimation;
    private final AnimationRenderable jumpAnimation;

    /**
     * Creates a new avatar.
     *
     * @param topLeftCorner the top left corner of the avatar
     * @param inputListener the input listener
     * @param imageReader   the image reader
     */
    public Avatar(Vector2 topLeftCorner, UserInputListener inputListener, ImageReader imageReader) {
        super(topLeftCorner, Vector2.ONES.mult(50), new ImageRenderable(imageReader.readImage("assets" +
                "/idle_0.png", true).getImage()));
        this.inputListener = inputListener;
        this.energy = MAX_ENERGY;
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);
        setTag("avatar");
        // Load animations
        idleAnimation = new AnimationRenderable(new ImageRenderable[]{
                new ImageRenderable(imageReader.readImage("assets/idle_0.png", true).getImage()),
                new ImageRenderable(imageReader.readImage("assets/idle_1.png", true).getImage()),
                new ImageRenderable(imageReader.readImage("assets/idle_2.png", true).getImage()),
                new ImageRenderable(imageReader.readImage("assets/idle_3.png", true).getImage())
        }, 0.2f);

        runAnimation = new AnimationRenderable(new ImageRenderable[]{
                new ImageRenderable(imageReader.readImage("assets/run_0.png", true).getImage()),
                new ImageRenderable(imageReader.readImage("assets/run_1.png", true).getImage()),
                new ImageRenderable(imageReader.readImage("assets/run_2.png", true).getImage()),
                new ImageRenderable(imageReader.readImage("assets/run_3.png", true).getImage()),
                new ImageRenderable(imageReader.readImage("assets/run_4.png", true).getImage()),
                new ImageRenderable(imageReader.readImage("assets/run_5.png", true).getImage())
        }, 0.1f);

        jumpAnimation = new AnimationRenderable(new ImageRenderable[]{
                new ImageRenderable(imageReader.readImage("assets/jump_0.png", true).getImage()),
                new ImageRenderable(imageReader.readImage("assets/jump_1.png", true).getImage()),
                new ImageRenderable(imageReader.readImage("assets/jump_2.png", true).getImage()),
                new ImageRenderable(imageReader.readImage("assets/jump_3.png", true).getImage())
        }, 0.15f);
    }

    /**
     * Adds a jump listener.
     *
     * @param listener the listener
     */
    public void addJumpListener(JumpListener listener) {
        jumpListeners.add(listener);
    }

    /**
     * Notifies all jump listeners.
     */
    private void notifyJumpListeners() {
        for (JumpListener listener : jumpListeners) {
            listener.onJump();
        }
    }


    /**
     * Updates the avatar.
     *
     * @param deltaTime the time since the last update
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float xVel = 0;

        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT) && inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            // Do nothing if both keys are pressed
            return;
        } else if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) &&
                getVelocity().y() == 0 && energy >= ENERGY_LOSS_JUMP) {
            transform().setVelocityY(VELOCITY_Y);
            this.energy -= ENERGY_LOSS_JUMP;
            renderer().setRenderable(jumpAnimation);
            notifyObservers();
            notifyJumpListeners();
            renderer().setRenderable(jumpAnimation);
        } else if (inputListener.isKeyPressed(KeyEvent.VK_LEFT) && energy >= ENERGY_LOSS_RUN) {
            xVel -= VELOCITY_X; // left arrow
            this.energy -= ENERGY_LOSS_RUN;
            renderer().setRenderable(runAnimation);
            renderer().setIsFlippedHorizontally(true);
        } else if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT) && energy >= ENERGY_LOSS_RUN) {
            xVel += VELOCITY_X; // right arrow
            this.energy -= ENERGY_LOSS_RUN;
            renderer().setRenderable(runAnimation);
            renderer().setIsFlippedHorizontally(false);
        } else {
            this.energy = Math.min(energy + ENERGY_GAIN_IDLE, MAX_ENERGY);
            renderer().setRenderable(idleAnimation);
        }

        transform().setVelocityX(xVel);
        notifyObservers();
    }

    /**
     * Gets the energy of the avatar.
     *
     * @return the energy
     */
    public float getEnergy() {

        return energy;
    }

    /**
     * Adds an observer.
     *
     * @param observer the observer
     */
    public void addObserver(EnergyObserver observer) {
        observers.add(observer);
    }

    /**
     * Notifies all observers.
     */
    public void notifyObservers() {
        for (EnergyObserver observer : observers) {
            observer.updateEnergy(energy);
        }
    }

    /**
     * Handles collisions.
     *
     * @param other     the other game object
     * @param collision the collision
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other.getTag().equals("fruit")) {
            addEnergy();
            other.setDimensions(Vector2.ZERO); // Hide the fruit
        }
    }

    /**
     * Adds energy to the avatar.
     */
    private void addEnergy() {
        this.energy = Math.min(this.energy + AMOUNT, MAX_ENERGY);
        notifyObservers();
    }

}
