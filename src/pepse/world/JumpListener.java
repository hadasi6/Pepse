package pepse.world;

/**
 * Interface for objects that want to listen to jump events.
 */
public interface JumpListener {
    /**
     * Called when a jump event occurs.
     */
    void onJump();
}
