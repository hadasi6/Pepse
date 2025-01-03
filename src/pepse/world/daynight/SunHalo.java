package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;

import java.awt.*;

public class SunHalo{

    public static GameObject create(GameObject sun) {
        GameObject sunHalo = new GameObject(sun.getTopLeftCorner(), sun.getDimensions().mult(1.5f), new OvalRenderable(new Color(255, 255, 0, 20)
        ));
        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

        sunHalo.addComponent((float deltaTime) -> {
            sunHalo.setCenter(sun.getCenter());
        });

        sunHalo.setTag("SunHalo");
        return sunHalo;
    }
}
