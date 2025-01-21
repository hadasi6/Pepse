package pepse.world;

import danogl.GameObject;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import danogl.components.CoordinateSpace;

import java.awt.*;

/**
 * Represents the sky.
 * author: @Hadas
 */
public class Sky {
    private static final Color BASIC_SKY_COLOR = Color.decode("#80C6E5"); // The basic color of the sky

    /**
     * Creates a new sky.
     *
     * @param windowDimensions the dimensions of the window
     * @return the sky
     */
    public static GameObject create(Vector2 windowDimensions) {
        GameObject sky = new GameObject(Vector2.ZERO, windowDimensions,
                new RectangleRenderable(BASIC_SKY_COLOR));
        sky.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sky.setTag("Sky");
        return sky;
    }
}
