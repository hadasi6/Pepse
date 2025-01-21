package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Cloud {
    private static final Random random = new Random();
    private static final Color BASE_CLOUD_COLOR = new Color(255, 255, 255);


    public static List<Block> create(Vector2 topLeftCorner, Vector2 windowDimensions, float cycleLength, GameObjectCollection gameObjects) {
        List<Block> cloudBlocks = new ArrayList<>();
        List<List<Integer>> cloudPattern = List.of(
                List.of(0, 1, 1, 0, 0, 0),
                List.of(1, 1, 1, 0, 1, 0),
                List.of(1, 1, 1, 1, 1, 1),
                List.of(1, 1, 1, 1, 1, 1),
                List.of(0, 1, 1, 1, 0, 0),
                List.of(0, 0, 0, 0, 0, 0)
        );

        for (int i = 0; i < cloudPattern.size(); i++) {
            for (int j = 0; j < cloudPattern.get(i).size(); j++) {
                if (cloudPattern.get(i).get(j) == 1) {
                    // Calculate block position
                    Vector2 position = topLeftCorner.add(new Vector2(j * Block.SIZE, i * Block.SIZE));

                    // Create the block
                    RectangleRenderable renderable = new RectangleRenderable(
                            ColorSupplier.approximateMonoColor(BASE_CLOUD_COLOR)
                    );
                    Block cloudBlock = new Block(position, renderable);
                    cloudBlock.setTag("CloudBlock");

                    // Define the start and end points for this block's transition
                    Vector2 start = position;
                    Vector2 end = new Vector2(windowDimensions.x() + j * Block.SIZE, position.y());

                    // Add a transition for this block
                    new Transition<>(
                            cloudBlock,
                            cloudBlock.transform()::setTopLeftCorner,
                            start,
                            end,
                            Transition.LINEAR_INTERPOLATOR_VECTOR,
                            cycleLength,
                            Transition.TransitionType.TRANSITION_LOOP,
                            null
                    );

                    cloudBlocks.add(cloudBlock);
                }
            }
        }

        return cloudBlocks;
    }
    public static void createRain(Vector2 cloudPosition, int numberOfDrops, GameObjectCollection gameObjects) {
        System.out.println("Creating rain...");
        for (int i = 0; i < numberOfDrops; i++) {
            Vector2 dropPosition = cloudPosition.add(new Vector2(random.nextInt(180),
                    random.nextInt(150)));
            RainDrop rainDrop = new RainDrop(dropPosition, new Vector2(5, 10), gameObjects);
            gameObjects.addGameObject(rainDrop, Layer.BACKGROUND+3);
        }
    }
}