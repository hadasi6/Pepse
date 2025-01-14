package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class RainDrop extends GameObject {
    private static final float GRAVITY = 600;
    private final GameObjectCollection gameObjects;
    private static final Color RAIN_DROP_COLOR = new Color(0, 0, 255);


    public RainDrop(Vector2 topLeftCorner, Vector2 dimensions, GameObjectCollection gameObjects) {
        super(topLeftCorner, dimensions, new RectangleRenderable(RAIN_DROP_COLOR));
        this.gameObjects = gameObjects;
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

    private void removeFromGame() {
        System.out.println("Removing raindrop");
        gameObjects.removeGameObject(this);
    }
}