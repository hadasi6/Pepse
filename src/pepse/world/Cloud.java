package pepse.world;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Cloud {
    private static final Color BASE_CLOUD_COLOR = new Color(255, 255, 255);

    public static GameObject create(Vector2 topLeftCorner, Vector2 windowDimensions, float cycleLength) {
        List<Block> cloudBlocks = new ArrayList<>();
        List<List<Integer>> cloudPattern = List.of(
                List.of(0, 1, 1, 0, 0, 0),
                List.of(1, 1, 1, 0, 1, 0),
                List.of(1, 1, 1, 1, 1, 1),
                List.of(1, 1, 1, 1, 1, 1),
                List.of(0, 1, 1, 1, 0, 0),
                List.of(0, 0, 0, 0, 0, 0)
        );
        GameObject cloud = new GameObject(topLeftCorner, new Vector2(cloudPattern.get(0).size() * Block.SIZE, cloudPattern.size() * Block.SIZE), null);
        cloud.setTag("Cloud");
        cloud.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

        for (int i = 0; i < cloudPattern.size(); i++) {
            for (int j = 0; j < cloudPattern.get(i).size(); j++) {
                if (cloudPattern.get(i).get(j) == 1) {
                    Vector2 position = topLeftCorner.add(new Vector2(j * Block.SIZE, i * Block.SIZE));
                    RectangleRenderable renderable = new RectangleRenderable(ColorSupplier.approximateMonoColor(BASE_CLOUD_COLOR));
                    Block cloudBlock = new Block(position, renderable);
                    cloudBlock.setTag("Cloud");
                    cloudBlocks.add(cloudBlock);
                }
            }
        }
        new Transition<>(
                cloud,
                cloud.transform()::setCenter,
                topLeftCorner,
                new Vector2(windowDimensions.x() + cloud.getDimensions().x(), topLeftCorner.y()),
                Transition.LINEAR_INTERPOLATOR_VECTOR,
                cycleLength,
                Transition.TransitionType.TRANSITION_LOOP,
                null
        );
        return cloud;
    }
}
