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
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import pepse.world.*;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Tree;

import java.awt.*;
import java.util.List;
import java.util.function.Supplier;

public class PepseGameManager extends GameManager {
//    private WorldManager worldManager;
    private Vector2 windowDimensions;

    private static final int SEED = 12345;
    private static final float NIGHT_CYCLE_LENGTH = 30;
    private static final float SUN_CYCLE_LENGTH = 30;
    private static final Color SUN_HALO_COLOR = new Color(255, 255, 0, 20);
    private static final float AVATAR_HEIGHT_OFFSET = 50;
    private static final float ENERGY_DISPLAY_WIDTH = 100;
    private static final float ENERGY_DISPLAY_HEIGHT = 30;
    private static final Vector2 ENERGY_DISPLAY_POSITION = new Vector2(10, 10);

    private Avatar avatar;
    private Terrain terrain;
    private Tree tree;
    //    private Vector2 windowDimensions;
    private int minXCoord;
    private int maxXCoord;

    public static void main(String[] args) {
        new PepseGameManager().run();

    }

    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader, UserInputListener inputListener,
                               WindowController windowController) {

        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        windowDimensions = windowController.getWindowDimensions();
        minXCoord = (int) -windowDimensions.x();
        maxXCoord = (int) (2 * windowDimensions.x());


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
        // initialize the worldManager
//        worldManager = new WorldManager(gameObjects(), windowController, inputListener, windowController.getWindowDimensions(), 12345);

    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        updateWorld();
//        worldManager.update();
        updateAvatarLocation();

    }

    private void ctreateSky() {
        GameObject sky = Sky.create(windowDimensions);
        gameObjects().addGameObject(sky, Layer.BACKGROUND);
    }

    private void createNight() {
        GameObject night = Night.create(windowDimensions, NIGHT_CYCLE_LENGTH);
        gameObjects().addGameObject(night, Layer.FOREGROUND);
//        GameObject night = Night.create(windowController.getWindowDimensions(), 30);
//        gameObjects().addGameObject(night, Layer.FOREGROUND);

    }

    private void createSunAndSunHalo() {
        GameObject sun = Sun.create(windowDimensions, SUN_CYCLE_LENGTH);
        gameObjects().addGameObject(sun, Layer.BACKGROUND + 1);
        GameObject sunHalo = SunHalo.create(sun);
        gameObjects().addGameObject(sunHalo, Layer.BACKGROUND + 2);

//        GameObject sun = Sun.create(windowController.getWindowDimensions(), 30);
//        gameObjects().addGameObject(sun, Layer.BACKGROUND + 1);
//
//        GameObject sunHalo = SunHalo.create(sun);
//        gameObjects().addGameObject(sunHalo, Layer.BACKGROUND + 2);

    }

    private void createTerrain() {
//        terrain = new Terrain(windowDimensions, SEED);
//        terrain.createInRange(minXCoord, maxXCoord);

        terrain = new Terrain(windowDimensions, 12345);
//        int windowWidth = (int) windowDimensions.x();
//        List<Block> blocks = terrain.createInRange(0, windowWidth);
        List<Block> blocks = terrain.createInRange(minXCoord, maxXCoord);
        for (Block block : blocks) {
            gameObjects().addGameObject(block, Layer.STATIC_OBJECTS);
        }
    }

    private void createTrees() {
        tree = new Tree(gameObjects(), terrain, SEED);
        tree.createInRange(minXCoord, maxXCoord);

//
//        Tree tree = new Tree(gameObjects(), terrain, 12345);
//        tree.createInRange(0, windowWidth);
//
        gameObjects().layers().shouldLayersCollide(Layer.DEFAULT, Layer.FOREGROUND - 1, true);
    }


    private void createAvatar(UserInputListener inputListener, ImageReader imageReader) {
        float avatarXPosition = windowDimensions.x() / 2;
        float avatarYPosition = terrain.getGroundHeightAt(avatarXPosition) - AVATAR_HEIGHT_OFFSET;
        Vector2 avatarPosition = new Vector2(avatarXPosition, avatarYPosition);
        avatar = new Avatar(avatarPosition, inputListener, imageReader);
        gameObjects().addGameObject(avatar, Layer.DEFAULT);
        gameObjects().layers().shouldLayersCollide(Layer.DEFAULT, Layer.FOREGROUND, true);

//        // Calculate the height of the blocks at the desired x position
//        float avatarXPosition = windowController.getWindowDimensions().x() / 2;
//        float avatarYPosition = terrain.getGroundHeightAt(avatarXPosition) - 50; // Adjust the height to
//        // place the avatar on top of the blocks
//
//        // Add the avatar to the game
//        Vector2 avatarPosition = new Vector2(avatarXPosition, avatarYPosition);
//        Avatar avatar = new Avatar(avatarPosition, inputListener, imageReader);
//        gameObjects().addGameObject(avatar, Layer.DEFAULT);
//        gameObjects().layers().shouldLayersCollide(Layer.DEFAULT, Layer.FOREGROUND, true);

    }

    private void createEnergyDisplay() {
        GameObject energyDisplay = new EnergyDisplay(ENERGY_DISPLAY_POSITION, new Vector2(ENERGY_DISPLAY_WIDTH, ENERGY_DISPLAY_HEIGHT), avatar);
        energyDisplay.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(energyDisplay, Layer.UI);

//        // Add energy display
//        GameObject energyDisplay = new EnergyDisplay(new Vector2(10, 10),
//                new Vector2(100, 30), avatar);
//        gameObjects().addGameObject(energyDisplay, Layer.UI);

    }

    private void createClouds() {
        Vector2 cloudPosition = new Vector2(-200, 100);
        List<Block> cloudBlocks = Cloud.create(cloudPosition, windowDimensions, 10, gameObjects());
        for (Block cloudBlock : cloudBlocks) {
            cloudBlock.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES); //todo - validate
            gameObjects().addGameObject(cloudBlock, Layer.BACKGROUND + 3);
        }
        avatar.addJumpListener(new RainJumpListener(cloudBlocks.get(0), gameObjects()));

//        // Add cloud
//        Vector2 cloudPosition = new Vector2(-200, 100);
//        List<Block> cloudBlocks = Cloud.create(cloudPosition, windowController.getWindowDimensions(), 10, gameObjects());
//        for (Block cloudBlock : cloudBlocks) {
//            gameObjects().addGameObject(cloudBlock, Layer.BACKGROUND + 3);
//        }
//
//        // Add rain
//        avatar.addJumpListener(new RainJumpListener(cloudBlocks.get(0), gameObjects()));
    }

    private void updateWorld() {
        int windowWidth = (int) windowDimensions.x();
        float centerMinCoordDelta = avatar.getCenter().x() - minXCoord;
        float centerMaxCoordDelta = maxXCoord - avatar.getCenter().x();
        if (centerMinCoordDelta < windowWidth) {
            expandGround(minXCoord - windowWidth, minXCoord);
            minXCoord -= windowWidth;
//            updateWorldBoundaries(-windowWidth);
        }
        if (centerMaxCoordDelta < windowWidth) {
            expandGround(maxXCoord, maxXCoord + windowWidth);
            maxXCoord += windowWidth;
//            updateWorldBoundaries(windowWidth);
        }
        removeObjectsOutOfBounds();
    }

    private void expandGround(int minX, int maxX) {
        List<Block> blocks = terrain.createInRange(minX, maxX);
        for (Block block : blocks) {
            gameObjects().addGameObject(block, Layer.STATIC_OBJECTS);
        }
        tree.createInRange(minX, maxX);
//        terrain.createInRange(minX, maxX);
//        tree.createInRange(minX, maxX);
    }

    private void updateWorldBoundaries(int windowWidth) {
        minXCoord += windowWidth;
        maxXCoord += windowWidth;
    }

    private void removeObjectsOutOfBounds() {
        for (GameObject gameObject : gameObjects()) {
            float xCoordLeftGameObject = gameObject.getTopLeftCorner().x();
            if (xCoordLeftGameObject < minXCoord || xCoordLeftGameObject > maxXCoord) {
                String gameObjectTag = gameObject.getTag();
                switch (gameObjectTag) {
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

    private void updateAvatarLocation() {
        float xCoordAvatarTopLeftCorner = avatar.getTopLeftCorner().x();
        if (avatar.getTopLeftCorner().y() > terrain.getGroundHeightAt(xCoordAvatarTopLeftCorner) + 0.5f * Block.SIZE) {
            avatar.setTopLeftCorner(new Vector2(xCoordAvatarTopLeftCorner, windowDimensions.y() - terrain.getGroundHeightAt(xCoordAvatarTopLeftCorner - avatar.getDimensions().y())));
        }
    }
}
