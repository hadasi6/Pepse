package pepse.world;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents a block in the game world.
 * author: @Hadas
 */
public class Block extends GameObject {

    /**
     * The size of the block.
     */
    public static final int SIZE = 30; // The size of the block

    /**
     * Creates a block in the game world.
     *
     * @param topLeftCorner the top-left corner of the block
     * @param renderable    the renderable of the block
     */
    public Block(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);

        this.setTag("Block");
    }
}
