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
import java.util.Random;
import java.util.function.Consumer;

/**
 * A class that represents a cloud in the game world.
 * author: @Hadas
 */
public class Cloud {
    // The base color of the cloud
    private static final Color BASE_CLOUD_COLOR = new Color(255, 255, 255);
    private static final Random random = new Random();

    /**
     * Creates a cloud in the game world.
     *
     * @param topLeftCorner    the top-left corner of the cloud
     * @param windowDimensions the dimensions of the window
     * @param cycleLength      the length of the cloud's cycle
     * @param seed             the seed for the random number generator
     * @return the blocks that make up the cloud
     */
    public static List<Block> create(Vector2 topLeftCorner, Vector2 windowDimensions, float cycleLength,
                                     int seed) {
        random.setSeed(seed);
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
                    RectangleRenderable renderable =
                            new RectangleRenderable(ColorSupplier.approximateMonoColor(BASE_CLOUD_COLOR));
                    Block cloudBlock = new Block(position, renderable);
                    cloudBlock.setTag("CloudBlock");
                    // Define the start and end points for this block's transition
                    Vector2 end = new Vector2(windowDimensions.x() + j * Block.SIZE, position.y());
                    // Add a transition for this block
                    new Transition<>(
                            cloudBlock,
                            cloudBlock.transform()::setTopLeftCorner,
                            position,
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


    /**
     * Creates rain in the game world.
     *
     * @param cloudPosition    the position of the cloud
     * @param numberOfDrops    the number of raindrops to create
     * @param addGameObject    the function to add a game object to the game
     * @param removeGameObject the function to remove a game object from the game
     */
    public static void createRain(Vector2 cloudPosition, int numberOfDrops,
                                  Consumer<GameObject> addGameObject,
                                  Consumer<GameObject> removeGameObject) {
        for (int i = 0; i < numberOfDrops; i++) {
            Vector2 dropPosition = cloudPosition.add(new Vector2(random.nextInt(180),
                    random.nextInt(150)));
            RainDrop rainDrop = new RainDrop(dropPosition, new Vector2(5, 10), removeGameObject);
            rainDrop.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
            addGameObject.accept(rainDrop);
        }
    }
}