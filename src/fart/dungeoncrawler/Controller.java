package fart.dungeoncrawler;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

/**
 * The controller handles keyboard-input. 
 * @author Timo
 *
 */
public class Controller implements KeyListener
{
	private boolean lastFrame[] = new boolean[525];
	private boolean thisFrame[] = new boolean[525];
	
	private HashMap<Integer, Boolean> tmp = new HashMap<Integer, Boolean>();
	
	/**
	 * Returns if the given key is currently pressed. 
	 * @param key
	 * @return
	 */
	public boolean isPressed(int key)
	{
		if(key < thisFrame.length)
			return thisFrame[key];
		return false;
	}
	
	/**
	 * Returns if left arrow is pressed.
	 * @return
	 */
	public boolean isLeftPressed() { return isPressed(37); }
	/**
	 * Returns if up arroq is pressed. 
	 * @return
	 */
	public boolean isUpPressed() { return isPressed(38); }
	/**
	 * Returns if right arrow is pressed. 
	 * @return
	 */
	public boolean isRightPressed() { return isPressed(39); }
	/**
	 * Returns if down arrow is pressed. 
	 * @return
	 */
	public boolean isDownPressed() { return isPressed(40); }
	
	/**
	 * Returns if a key was just pressed. This only returns true at the beginning (in the first frame)
	 * of a key-press. 
	 * @param key
	 * @return
	 */
	public boolean justPressed(int key)
	{
		if(key < thisFrame.length)
		{
			if(!lastFrame[key] && thisFrame[key])
				return true;
		}
		return false;
	}
	
	/**
	 * Returns if a key was just released. This only returns true directly (in the first frame) after
	 * releasing the key. 
	 * @param key
	 * @return
	 */
	public boolean justReleased(int key)
	{
		if(key < thisFrame.length)
		{
			if(lastFrame[key] && !thisFrame[key])
				return true;
		}
		return false;
	}
	
	/**
	 * Updates the array of keys. This has to be done to be able to check if a key was just pressed/released.
	 */
	public void update()
	{
		lastFrame = thisFrame.clone();
		for(Map.Entry<Integer, Boolean> entry : tmp.entrySet())
			thisFrame[entry.getKey()] = entry.getValue();
		
		tmp.clear();
	}
	
	@Override
	public void keyPressed(KeyEvent e)
	{
		tmp.put(e.getKeyCode(), true);
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		tmp.put(e.getKeyCode(), false);
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		
	}
	
}