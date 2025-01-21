package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Represents the sun.
 * author: @Hadas
 */
public class Sun {

    // The dimensions of the sun
    private static final Vector2 SUN_DIMENSIONS = new Vector2(100, 100);

    /**
     * Creates a new sun.
     *
     * @param windowDimensions the dimensions of the window
     * @param cycleLength      the length of the day-night cycle
     * @return the sun
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength) {
        GameObject sun = new GameObject(Vector2.ZERO, SUN_DIMENSIONS,
                new OvalRenderable(Color.YELLOW));
        sun.setCoordinateSpace(danogl.components.CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag("Sun");

        Vector2 initialSunCenter = new Vector2(windowDimensions.x() / 2, windowDimensions.y() / 3);
        Vector2 cycleCenter = new Vector2(windowDimensions.x() / 2, windowDimensions.y() * 2 / 3);
        new Transition<>(
                sun,
                (Float angle) -> sun.setCenter(
                        initialSunCenter.subtract(cycleCenter)
                                .rotated(angle)
                                .add(cycleCenter)),
                0f,
                360f,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                cycleLength,
                Transition.TransitionType.TRANSITION_LOOP,
                null
        );
        return sun;
    }
}
