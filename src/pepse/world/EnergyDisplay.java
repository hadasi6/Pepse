package pepse.world;

import danogl.GameObject;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Displays the energy of the avatar.
 */
public class EnergyDisplay extends GameObject implements EnergyObserver {
    /**
     * The text renderable of the display.
     */
    private final TextRenderable textRenderable;

    /**
     * Creates a new energy display.
     *
     * @param topLeftCorner the top left corner of the display
     * @param dimensions    the dimensions of the display
     * @param avatar        the avatar to observe
     */
    public EnergyDisplay(Vector2 topLeftCorner, Vector2 dimensions, Avatar avatar) {
        super(topLeftCorner, dimensions, null);
        this.textRenderable = new TextRenderable("Energy: " + (int) avatar.getEnergy(), "Arial");
        this.textRenderable.setColor(Color.WHITE);
        this.renderer().setRenderable(textRenderable);
        avatar.addObserver(this);
    }

    /**
     * Updates the energy display.
     *
     * @param energy the new energy value
     */
    @Override
    public void updateEnergy(float energy) {
        textRenderable.setString("Energy: " + (int) energy);
    }
}