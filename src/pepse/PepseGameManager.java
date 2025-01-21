package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;
import pepse.world.*;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Flora;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

/**
 * The main class for the Pepse game
 * author: @Hadas
 */
public class PepseGameManager extends GameManager {

    // field - window dimensions
    private Vector2 windowDimensions;

    // Constants
    private static final float NIGHT_CYCLE_LENGTH = 30;
    private static final float SUN_CYCLE_LENGTH = 30;

    private static final float AVATAR_HEIGHT_OFFSET = 50;
    private static final float ENERGY_DISPLAY_WIDTH = 100;
    private static final float ENERGY_DISPLAY_HEIGHT = 30;
    private static final Vector2 ENERGY_DISPLAY_POSITION = new Vector2(10, 10);

    // Fields
    private static int seed;
    private Avatar avatar;
    private Terrain terrain;
    private Flora flora;
    private int minXCoord;
    private int maxXCoord;

    /**
     * The main method
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        new PepseGameManager().run();

    }

    /**
     * Initializes the game
     *
     * @param imageReader the image reader
     * @param soundReader the sound reader
     * @param inputListener the input listener
     * @param windowController the window controller
     */
    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader, UserInputListener inputListener,
                               WindowController windowController) {

        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        windowDimensions = windowController.getWindowDimensions();
        minXCoord = (int) -windowDimensions.x();
        maxXCoord = (int) (2 * windowDimensions.x());
        Random random = new Random();
        seed = random.nextInt();

        ctreateSky();
        createNight();
        createSunAndSunHalo();
        createTerrain();
        createTrees();
        createAvatar(inputListener, imageReader);
        createEnergyDisplay();
        createClouds();

        //set the camera to follow the avatar
        setCamera(new Camera(
                avatar,
                windowController.getWindowDimensions().mult(0.5f).subtract(avatar.getCenter()),
                windowController.getWindowDimensions(),
                windowController.getWindowDimensions()
        ));
    }

    /**
     * Updates the game
     *
     * @param deltaTime the time since the last update
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        updateWorld();
        updateAvatarLocation();
    }

    /**
     * Creates the sky
     */
    private void ctreateSky() {
        GameObject sky = Sky.create(windowDimensions);
        gameObjects().addGameObject(sky, Layer.BACKGROUND);
    }

    /**
     * Creates the night
     */
    private void createNight() {
        GameObject night = Night.create(windowDimensions, NIGHT_CYCLE_LENGTH);
        gameObjects().addGameObject(night, Layer.FOREGROUND);
    }

    /**
     * Creates the sun and sun halo
     */
    private void createSunAndSunHalo() {
        GameObject sun = Sun.create(windowDimensions, SUN_CYCLE_LENGTH);
        gameObjects().addGameObject(sun, Layer.BACKGROUND + 1);
        GameObject sunHalo = SunHalo.create(sun.getTopLeftCorner(), sun.getDimensions().mult(1.5f));

        sunHalo.addComponent((float deltaTime) -> sunHalo.setCenter(sun.getCenter()));
        gameObjects().addGameObject(sunHalo, Layer.BACKGROUND + 2);
    }

    /**
     * Creates the terrain
     */
    private void createTerrain() {
        terrain = new Terrain(windowDimensions, seed);
        List<Block> blocks = terrain.createInRange(minXCoord, maxXCoord);
        for (Block block : blocks) {
            gameObjects().addGameObject(block, Layer.STATIC_OBJECTS);
        }
    }

    /**
     * Creates the trees
     */
    private void createTrees() {
        flora = new Flora((gameObject)-> gameObjects().addGameObject(gameObject, Layer.FOREGROUND - 1),
                (gameObject)-> gameObjects().addGameObject(gameObject, Layer.STATIC_OBJECTS),
                terrain,
                seed);
        gameObjects().layers().shouldLayersCollide(Layer.STATIC_OBJECTS, Layer.DEFAULT, true);
        flora.createInRange(minXCoord, maxXCoord);

        gameObjects().layers().shouldLayersCollide(
                Layer.DEFAULT, Layer.FOREGROUND - 1, true);
    }


    /**
     * Creates the avatar
     *
     * @param inputListener the input listener
     * @param imageReader the image reader
     */
    private void createAvatar(UserInputListener inputListener, ImageReader imageReader) {
        float avatarXPosition = windowDimensions.x() / 2;
        float avatarYPosition = terrain.getGroundHeightAt(avatarXPosition) - AVATAR_HEIGHT_OFFSET;
        Vector2 avatarPosition = new Vector2(avatarXPosition, avatarYPosition);
        avatar = new Avatar(avatarPosition, inputListener, imageReader);
        gameObjects().addGameObject(avatar, Layer.DEFAULT);
        gameObjects().layers().shouldLayersCollide(Layer.DEFAULT, Layer.FOREGROUND, true);
    }

    /**
     * Creates the energy display
     */
    private void createEnergyDisplay() {
        GameObject energyDisplay = new EnergyDisplay(ENERGY_DISPLAY_POSITION,
                new Vector2(ENERGY_DISPLAY_WIDTH, ENERGY_DISPLAY_HEIGHT), avatar);
        energyDisplay.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(energyDisplay, Layer.UI);
    }

    /**
     * Creates the clouds
     */
    private void createClouds() {
        Vector2 cloudPosition = new Vector2(-200, 100);


        List<Block> cloudBlocks = Cloud.create(cloudPosition, windowDimensions,
                10, seed);
        for (Block cloudBlock : cloudBlocks) {
            cloudBlock.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES); //todo - validate
            gameObjects().addGameObject(cloudBlock, Layer.BACKGROUND + 3);
        }
        Supplier<Vector2> cloudPositionSupplier = () -> cloudBlocks.get(0).getCenter();
        avatar.addJumpListener(new RainJumpListener(cloudPositionSupplier,
                (gameObject) -> gameObjects().addGameObject(gameObject, Layer.BACKGROUND + 3),
                (gameObject) -> gameObjects().removeGameObject(gameObject, Layer.BACKGROUND + 3)));
    }

    /**
     * Updates the world
     */
    private void updateWorld() {
        int windowWidth = (int) windowDimensions.x();
        float centerMinCoordDelta = avatar.getCenter().x() - minXCoord;
        float centerMaxCoordDelta = maxXCoord - avatar.getCenter().x();
        if (centerMinCoordDelta < windowWidth) {
            expandGround(minXCoord - windowWidth, minXCoord);
            minXCoord -= windowWidth;
        }
        if (centerMaxCoordDelta < windowWidth) {
            expandGround(maxXCoord, maxXCoord + windowWidth);
            maxXCoord += windowWidth;
        }
        removeObjectsOutOfBounds();
    }

    /**
     * Expands the ground
     *
     * @param minX the minimum x coordinate
     * @param maxX the maximum x coordinate
     */
    private void expandGround(int minX, int maxX) {
        List<Block> blocks = terrain.createInRange(minX, maxX);
        for (Block block : blocks) {
            gameObjects().addGameObject(block, Layer.STATIC_OBJECTS);
        }
        flora.createInRange(minX, maxX);
    }


    /**
     * Removes objects that are out of bounds
     */
    private void removeObjectsOutOfBounds() {
        for (GameObject gameObject : gameObjects()) {
            float objXCoord = gameObject.getTopLeftCorner().x();
            if (objXCoord < minXCoord || objXCoord > maxXCoord) {
                String tag = gameObject.getTag();
                switch (tag) {
                    case "ground":
                        gameObjects().removeGameObject(gameObject, Layer.STATIC_OBJECTS);
                        break;
                    case "trunk":
                        gameObjects().removeGameObject(gameObject, Layer.STATIC_OBJECTS + 10);
                        break;
                    case "leaf":
                        gameObjects().removeGameObject(gameObject, Layer.FOREGROUND - 1);
                        break;
                }
            }
        }
    }

    /**
     * Updates the avatar location
     */
    private void updateAvatarLocation() {
        float xCoordAvatarTopLeftCorner = avatar.getTopLeftCorner().x();
        if (avatar.getTopLeftCorner().y() > terrain.getGroundHeightAt(
                xCoordAvatarTopLeftCorner) + 0.5f * Block.SIZE) {
            avatar.setTopLeftCorner(new Vector2(xCoordAvatarTopLeftCorner,
                    windowDimensions.y() - terrain.getGroundHeightAt(
                            xCoordAvatarTopLeftCorner - avatar.getDimensions().y())));
        }
    }

}
