package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Represents the night.
 * author: @Hadas
 */
public class Night {

    private static final float MIDNIGHT_OPACITY = 0.5f; //the opacity of the night

    /**
     * Creates a new night.
     *
     * @param windowDimensions the dimensions of the window
     * @param cycleLength      the length of the day-night cycle
     * @return the night
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength) {
        RectangleRenderable nightRenderer = new RectangleRenderable(Color.BLACK);
        GameObject night = new GameObject(Vector2.ZERO, windowDimensions, nightRenderer);
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        night.setTag("Night");
        new Transition<>(
                night,
                night.renderer()::setOpaqueness,
                0f,
                MIDNIGHT_OPACITY,
                Transition.CUBIC_INTERPOLATOR_FLOAT,// use a cubic interpolator
                cycleLength / 2, // transition fully over half a day
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, // Choose appropriate ENUM value
                null
        );
        return night;
    }
}
