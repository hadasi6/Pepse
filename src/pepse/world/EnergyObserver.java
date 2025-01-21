package pepse.world;

/**
 * Interface for objects that want to observe energy.
 */
public interface EnergyObserver {
    /**
     * Updates the energy.
     *
     * @param energy the new energy value
     */
    void updateEnergy(float energy);
}
