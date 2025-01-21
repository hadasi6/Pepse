package pepse.world;

import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Terrain {

    private final float groundHeightAtX0;
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private static final int TERRAIN_DEPTH = 20;
    private Random random;

    public Terrain(Vector2 windowDimensions, int seed) {
        this.groundHeightAtX0 = ((float) 2 / 3) * windowDimensions.y();
        this.random = new Random(seed);
    }

    public float getGroundHeightAt(float x) {
        return groundHeightAtX0;
    }

    public List<Block> createInRange(int minX, int maxX) {
        List<Block> blocks = new ArrayList<>();
        int roundedMinX = minX - minX % Block.SIZE;
        int roundedMaxX = maxX - maxX % Block.SIZE;
        for (int x = roundedMinX; x <= roundedMaxX; x += Block.SIZE) {
            float topBlockY = (float) Math.floor(getGroundHeightAt(x) / Block.SIZE) * Block.SIZE;
            for (int i=0; i< TERRAIN_DEPTH; i++) {
                float y = topBlockY + i * Block.SIZE;
                Block block = createBlock(i ,x, y);
                blocks.add(block);
            }
        }
        return blocks;
    }

    private Block createBlock(int depth, float x, float y) {
        // Use the hash of the x position and the seed to get a random color
//        Random random = new Random(gameObjects.hash(x, seed)); //todo - infinite world
        Renderable rendererBlock = new RectangleRenderable(ColorSupplier.approximateColor(
                BASE_GROUND_COLOR));

        Block block = new Block(new Vector2(x, y), rendererBlock);
        block.setTag("ground");
        return block;
    }
}
