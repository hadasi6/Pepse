package pepse.world;

import danogl.GameObject;
import danogl.util.Vector2;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Listener for jump events that creates rain.
 * author: @Hadas
 */
public class RainJumpListener implements JumpListener {
    private final Supplier<Vector2> cloudPositionSupplier; // Supplier for the cloud's position
    private final Consumer<GameObject> addRainDrop; // The game objects to add the rain to
    private final Consumer<GameObject> removeRainDrop; // The game objects to remove the rain from

    private static final int NUM_RAIN_DROP = 10;

    /**
     * Creates a new RainJumpListener.
     *
     * @param cloudPositionSupplier the supplier for the cloud's position
     * @param addRainDrop the game objects to add the rain to
     * @param removeRainDrop the game objects to remove the rain from
     */
    public RainJumpListener(Supplier<Vector2> cloudPositionSupplier, Consumer<GameObject> addRainDrop,
                            Consumer<GameObject> removeRainDrop) {
        this.cloudPositionSupplier = cloudPositionSupplier;
        this.addRainDrop = addRainDrop;
        this.removeRainDrop = removeRainDrop;
    }

    /**
     * Creates rain when the avatar jumps.
     */
    @Override
    public void onJump() {
        Vector2 currentCloudPosition = cloudPositionSupplier.get();
        Cloud.createRain(currentCloudPosition, NUM_RAIN_DROP, addRainDrop, removeRainDrop);
    }
}