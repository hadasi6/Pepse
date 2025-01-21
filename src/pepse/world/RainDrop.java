package pepse.world;

import danogl.GameObject;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;
import java.util.function.Consumer;

/**
 * Represents a raindrop.
 * author: @Hadas
 */
public class RainDrop extends GameObject {
    private static final float GRAVITY = 600; // The acceleration due to gravity
    private final Consumer<GameObject> removeRainDrop; //removes the raindrop from the game
    private static final Color RAIN_DROP_COLOR = new Color(0, 0, 255); //The color of the raindrop.


    /**
     * Creates a new raindrop.
     *
     * @param topLeftCorner  the top left corner of the raindrop
     * @param dimensions     the dimensions of the raindrop
     * @param removeRainDrop the game objects collection
     */
    public RainDrop(Vector2 topLeftCorner, Vector2 dimensions,
                    Consumer<GameObject> removeRainDrop) {
        super(topLeftCorner, dimensions, new RectangleRenderable(RAIN_DROP_COLOR));
        this.removeRainDrop = removeRainDrop;
        transform().setAccelerationY(GRAVITY);
        setTag("RainDrop");

        new Transition<>(
                this,
                renderer()::setOpaqueness,
                1f,
                0f,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                2f,
                Transition.TransitionType.TRANSITION_ONCE,
                this::removeFromGame
        );
    }

    /**
     * Removes the raindrop from the game.
     */
    private void removeFromGame() {
        System.out.println("Removing raindrop");
        removeRainDrop.accept(this);
    }
}