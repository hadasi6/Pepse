package pepse.world;

import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class that generates the terrain in the game world.
 */
public class Terrain {

    // Constants
    private final float groundHeightAtX0;
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private static final int TERRAIN_DEPTH = 20;
    // Fields
    private Random random;
    private final NoiseGenerator noiseGenerator;

    /**
     * Constructor for the Terrain class.
     *
     * @param windowDimensions the dimensions of the window
     * @param seed             the seed for the random number generator
     */
    public Terrain(Vector2 windowDimensions, int seed) {
        this.groundHeightAtX0 = ((float) 2 / 3) * windowDimensions.y();
        this.random = new Random(seed);
        this.noiseGenerator = new NoiseGenerator(seed, (int) groundHeightAtX0);
    }

    /**
     * Gets the ground height at a given x-coordinate.
     *
     * @param x the x-coordinate
     * @return the ground height
     */
    public float getGroundHeightAt(float x) {
        float noise = (float) noiseGenerator.noise(x, Block.SIZE * 7);
        return groundHeightAtX0 + noise;
//        return groundHeightAtX0;
    }

    /**
     * Creates the terrain in a given range.
     *
     * @param minX the minimum x-coordinate
     * @param maxX the maximum x-coordinate
     * @return the list of blocks
     */
    public List<Block> createInRange(int minX, int maxX) {
        List<Block> blocks = new ArrayList<>();
        int roundedMinX = minX - minX % Block.SIZE;
        int roundedMaxX = maxX - maxX % Block.SIZE;
        for (int x = roundedMinX; x <= roundedMaxX; x += Block.SIZE) {
            float topBlockY = (float) Math.floor(getGroundHeightAt(x) / Block.SIZE) * Block.SIZE;
            for (int i = 0; i < TERRAIN_DEPTH; i++) {
                float y = topBlockY + i * Block.SIZE;
                Block block = createBlock(i, x, y);
                blocks.add(block);
            }
        }
        return blocks;
    }

    /**
     * Creates a block at a given position.
     *
     * @param depth the depth of the block
     * @param x     the x-coordinate
     * @param y     the y-coordinate
     * @return the block
     */
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
