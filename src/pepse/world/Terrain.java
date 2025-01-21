package pepse.world;

import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that generates the terrain in the game world.
 * author: @Hadas
 */
public class Terrain {

    private final float groundHeightAtX0; // The height of the ground at x = 0
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74); // ground color
    private static final int TERRAIN_DEPTH = 20; // The depth of the terrain
    private final NoiseGenerator noiseGenerator; // The noise generator

    /**
     * Creates a new terrain.
     *
     * @param windowDimensions the dimensions of the window
     * @param seed             the seed for the noise generator
     */
    public Terrain(Vector2 windowDimensions, int seed) {
        this.groundHeightAtX0 = ((float) 2 / 3) * windowDimensions.y();
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
                Block block = createBlock(x, y);
                blocks.add(block);
            }
        }
        return blocks;
    }

    /**
     * Creates a block at a given x and y coordinate.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return the block
     */
    private Block createBlock(float x, float y) {
        Renderable rendererBlock = new RectangleRenderable(ColorSupplier.approximateColor(
                BASE_GROUND_COLOR));

        Block block = new Block(new Vector2(x, y), rendererBlock);
        block.setTag("ground");
        return block;
    }
}
