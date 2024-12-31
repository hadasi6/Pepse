package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.world.Terrain;

//import danogl.gui.rendering.OvalRenderable;

import java.awt.*;
import java.util.Random;

public class Tree {

    private static final Color TRUNK_COLOR = new Color(100, 50, 20);
    private static final Color LEAF_COLOR = new Color(50, 200, 30);
    private static final Color FRUIT_COLOR = new Color(255, 0, 0);
    private static final int TRUNK_WIDTH = 20;
    private static final int LEAF_SIZE = 20;
    private static final int FRUIT_SIZE = 15;
    private static final float TREE_PROBABILITY = 0.1f;
    private static final float LEAF_PROBABILITY = 0.6f;
    private static final float FRUIT_PROBABILITY = 0.2f;



    private GameObjectCollection gameObjects;
    private Terrain terrain;
    private Random random;

    public Tree(GameObjectCollection gameObjects, Terrain terrain, int seed) {
        this.terrain = terrain;
        this.random = new Random(seed);
        this.gameObjects = gameObjects;
    }

    public void createInRange(int minX, int maxX) {
        for (int x = minX; x < maxX; x += TRUNK_WIDTH) {
            if (random.nextFloat() < TREE_PROBABILITY) {
                createTree(x);
            }
        }
    }

    private void createTree(int x) {
        float groundHeight = terrain.getGroundHeightAt(x);
        int trunkHeight = random.nextInt(100) + 50;
        createTrunk(x, groundHeight, trunkHeight);
        createLeaves(x, groundHeight - trunkHeight);
        createFruits(x, groundHeight - trunkHeight);
        createFruits(x, groundHeight - trunkHeight);
    }

    private void createTrunk(int x, float groundHeight, int trunkHeight) {
        for (int y=0; y<trunkHeight; y += TRUNK_WIDTH) {
            GameObject trunk = new GameObject(new Vector2(x, groundHeight-y-TRUNK_WIDTH),
                    new Vector2(TRUNK_WIDTH, TRUNK_WIDTH), new RectangleRenderable(TRUNK_COLOR));
//            trunk.setTags("trunk");
            trunk.physics().preventIntersectionsFromDirection(Vector2.ZERO);
            trunk.physics().setMass(Float.MAX_VALUE);
            gameObjects.addGameObject(trunk, Layer.STATIC_OBJECTS);
            gameObjects.layers().shouldLayersCollide(Layer.STATIC_OBJECTS, Layer.DEFAULT, true);
        }
    }

    private void createLeaves(int x, float topOfTrunk) {
        for (int dx = -LEAF_SIZE; dx <= LEAF_SIZE; dx+=LEAF_SIZE) {
            for (int dy = -LEAF_SIZE; dy <= LEAF_SIZE; dy += LEAF_SIZE) {
                if (random.nextFloat() < LEAF_PROBABILITY) {
                    GameObject leaf =
                            new GameObject(new Vector2(x+dx, topOfTrunk +dy), new Vector2(LEAF_SIZE,
                                    LEAF_SIZE), new RectangleRenderable(LEAF_COLOR));
                    leaf.setTag("leaf");
                    gameObjects.addGameObject(leaf, Layer.FOREGROUND-1);

                    // Add transitions for leaf oscillation
                    addLeafOscillation(leaf, dx, dy);
                }
            }
        }
    }

    private void addLeafOscillation(GameObject leaf, int dx, int dy) {
        // Transition for leaf angle
        new ScheduledTask(leaf, random.nextFloat(), false, () -> {new Transition<>(leaf,
                leaf.renderer()::setRenderableAngle, -10f, 10f, Transition.LINEAR_INTERPOLATOR_FLOAT, 2,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);});

        // Transition for leaf width
        new ScheduledTask(leaf, random.nextFloat(), false, () -> { new Transition<>(leaf,
                angle -> leaf.setDimensions(new Vector2(LEAF_SIZE + angle, LEAF_SIZE)), -5f, 5f,
                        Transition.LINEAR_INTERPOLATOR_FLOAT, 2,
                        Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);});
    }

    private void createFruits(int x, float topOfTrunk) {
        for (int dx = -LEAF_SIZE; dx <= LEAF_SIZE; dx += LEAF_SIZE) {
            for (int dy = -LEAF_SIZE; dy <= LEAF_SIZE; dy += LEAF_SIZE) {
                if (random.nextFloat() < FRUIT_PROBABILITY) {
                    GameObject fruit = new GameObject(new Vector2(x + dx, topOfTrunk + dy),
                            new Vector2(FRUIT_SIZE, FRUIT_SIZE), new OvalRenderable(FRUIT_COLOR)) {
                        @Override
                        public void onCollisionEnter(GameObject other, danogl.collisions.Collision collision) {
                            if (other.getTag().equals("avatar")) {
                                setDimensions(Vector2.ZERO); // Hide the fruit
                                new ScheduledTask(this, 30, false, () -> setDimensions(new Vector2(FRUIT_SIZE, FRUIT_SIZE))); // Respawn after 30 seconds
                            }
                        }
                    };
                    fruit.setTag("fruit");
                    gameObjects.addGameObject(fruit, Layer.FOREGROUND);
                }
            }
        }
    }
}
