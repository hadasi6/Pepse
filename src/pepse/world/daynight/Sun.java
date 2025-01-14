package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class Sun {

    public static GameObject create(Vector2 windowDimensions, float cycleLength) {
        GameObject sun = new GameObject(Vector2.ZERO, new Vector2(100, 100),
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
