package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class Night {

    private static final float MIDNIGHT_OPACITY = 0.8f; //todo -

    public static GameObject create(Vector2 windowDimensions, float cycleLength) {
        RectangleRenderable nightRenderer = new RectangleRenderable(Color.BLACK);
        GameObject night = new GameObject(Vector2.ZERO, windowDimensions, nightRenderer);
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        night.setTag("Night");

        //todo - i deleted <FLOAT>
        new Transition<>(
                night,
                night.renderer()::setOpaqueness,
                0f,
                MIDNIGHT_OPACITY,
                Transition.CUBIC_INTERPOLATOR_FLOAT,// use a cubic interpolator
                cycleLength / 2, // transition fully over half a day
        Transition.TransitionType.TRANSITION_BACK_AND_FORTH, // Choose appropriate ENUM value
        null
);// nothing further to execute upon reaching final value


        return night;
    }
}
