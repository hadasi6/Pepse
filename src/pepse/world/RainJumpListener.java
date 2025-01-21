package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Vector2;

import java.util.function.Consumer;

/**
 * Listener for jump events that creates rain.
 */
public class RainJumpListener implements JumpListener {
    // The cloud to create rain from
    private final GameObject cloud;
    // The game objects to add the rain to
    private final Consumer<GameObject> addRainDrop;
    private final Consumer<GameObject> removeRainDrop;

    /**
     * Creates a new rain jump listener.
     *
     * @param cloud       the cloud to create rain from
     * @param gameObjects the game objects to add the rain to
     */
    public RainJumpListener(GameObject cloud, Consumer<GameObject> addRainDrop, Consumer<GameObject> removeRainDrop) {
        this.cloud = cloud;
        this.addRainDrop = addRainDrop;
        this.removeRainDrop = removeRainDrop;
    }

    /**
     * Creates rain when the avatar jumps.
     */
    @Override
    public void onJump() {
        System.out.println("Avatar jumped, creating rain...");
        Vector2 currentCloudPosition = cloud.getCenter(); //todo -validate
        Cloud.createRain(currentCloudPosition, 10, addRainDrop, removeRainDrop);
    }
}