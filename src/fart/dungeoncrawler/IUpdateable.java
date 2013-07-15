package fart.dungeoncrawler;

/**
 * Everything that has to be updated in the game implements this interface.
 * @author Felix
 *
 */
public interface IUpdateable {
	/**
	 * Updates the objectstate. 
	 * @param elapsed the time elapsed since the last frame. 
	 */
	void update(float elapsed);
}
