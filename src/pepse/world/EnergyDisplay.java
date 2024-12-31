package pepse.world;

import danogl.GameObject;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class EnergyDisplay extends GameObject implements EnergyObserver {
    private final TextRenderable textRenderable;

    public EnergyDisplay(Vector2 topLeftCorner, Vector2 dimensions, Avatar avatar) {
        super(topLeftCorner, dimensions, null);
        this.textRenderable = new TextRenderable("Energy: " + (int) avatar.getEnergy(),"Arial");
        this.textRenderable.setColor(Color.WHITE);
        this.renderer().setRenderable(textRenderable);
        avatar.addObserver(this);
    }

    @Override
    public void updateEnergy(float energy) {
        textRenderable.setString("Energy: " + (int) energy);
    }
}