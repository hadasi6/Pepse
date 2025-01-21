package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Represents the sun halo.
 * author: @Hadas
 */
public class SunHalo {

    private static final Color SUN_HALO_COLOR = new Color(255, 255, 0, 20);

    /**
     * Creates a new sun halo.
     *
     * @param topLeftCorner  the top-left corner of the sun halo
     * @param sunDimenstions the dimensions of the sun halo
     * @return the sun halo
     */
    public static GameObject create(Vector2 topLeftCorner, Vector2 sunDimenstions) {
        GameObject sunHalo = new GameObject(topLeftCorner, sunDimenstions,
                new OvalRenderable(SUN_HALO_COLOR));
        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sunHalo.setTag("SunHalo");
        return sunHalo;
    }
}
