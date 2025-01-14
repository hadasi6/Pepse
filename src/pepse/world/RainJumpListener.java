package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Vector2;

public class RainJumpListener implements JumpListener {
//    private final Vector2 cloudPosition;
    private final GameObject cloud;
    private final GameObjectCollection gameObjects;

    public RainJumpListener(GameObject cloud, GameObjectCollection gameObjects) {
        this.cloud = cloud;
        this.gameObjects = gameObjects;
    }

    @Override
    public void onJump() {
        System.out.println("Avatar jumped, creating rain...");
        Vector2 currentCloudPosition = cloud.getTopLeftCorner();
        Cloud.createRain(currentCloudPosition, 10, gameObjects);
    }
}