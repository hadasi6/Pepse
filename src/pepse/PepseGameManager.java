package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import pepse.world.Avatar;
import pepse.world.Block;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;

import java.awt.*;
import java.util.List;

public class PepseGameManager extends GameManager {
    public static void main(String[] args) {
        new PepseGameManager().run();

    }

    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader, UserInputListener inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        GameObject sky = Sky.create(windowController.getWindowDimensions());
        gameObjects().addGameObject(sky, Layer.BACKGROUND);

        Terrain terrain = new Terrain(windowController.getWindowDimensions(), 12345);
        int windowWidth = (int) windowController.getWindowDimensions().x();
        List<Block> blocks = terrain.createInRange(0, windowWidth);
        for (Block block : blocks) {
            gameObjects().addGameObject(block, Layer.FOREGROUND);
        }

        GameObject night = Night.create(windowController.getWindowDimensions(), 30);
        gameObjects().addGameObject(night, Layer.BACKGROUND);

        GameObject sun = Sun.create(windowController.getWindowDimensions(), 30);
        gameObjects().addGameObject(sun, Layer.BACKGROUND+1);

        GameObject sunHalo = SunHalo.create(sun);
        gameObjects().addGameObject(sunHalo, Layer.BACKGROUND);


        // Calculate the height of the blocks at the desired x position
        float avatarXPosition = windowController.getWindowDimensions().x() / 2;
        float avatarYPosition = terrain.getGroundHeightAt(avatarXPosition) - 50; // Adjust the height to
        // place the avatar on top of the blocks

        // Add the avatar to the game
        Vector2 avatarPosition = new Vector2(avatarXPosition, avatarYPosition);
        Avatar avatar = new Avatar(avatarPosition, inputListener, imageReader);
        gameObjects().addGameObject(avatar, Layer.DEFAULT);
        gameObjects().layers().shouldLayersCollide(Layer.DEFAULT, Layer.FOREGROUND, true);

        // Add energy display
        GameObject energyDisplay = new GameObject(Vector2.ZERO, new Vector2(100, 30),
                new TextRenderable(()->"Energy: " + avatar.getEnergy(), new Font("Arial", Font.PLAIN, 20),
                        Color.WHITE));
        gameObjects().addGameObject(energyDisplay, Layer.UI);
    }


}