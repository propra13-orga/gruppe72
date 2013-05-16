package fart.dungeoncrawler;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Controller implements KeyListener
{
	private boolean	leftPressed   = false,
						upPressed     = false,
						rightPressed  = false,
						downPressed   = false,
						returnPressed = false;
	
	public boolean isLeftPressed() { return leftPressed; }
	public void setLeftPressed(boolean b) { leftPressed = b; }
	
	public boolean isUpPressed() { return upPressed; }
	public void setUpPressed(boolean b) { upPressed = b; }
	
	public boolean isRightPressed() { return rightPressed; }
	public void setRightPressed(boolean b) { rightPressed = b; }
	
	public boolean isDownPressed() { return downPressed; }
	public void setDownPressed(boolean b) { downPressed = b; }
	
	public boolean isReturnPressed() { return returnPressed; }
	public void setReturnPressed(boolean b) { returnPressed = b; }
	
	@Override
	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyCode() == KeyEvent.VK_LEFT)
			setLeftPressed(true);
		else if(e.getKeyCode() == KeyEvent.VK_UP)
			setUpPressed(true);
		else if(e.getKeyCode() == KeyEvent.VK_RIGHT)
			setRightPressed(true);
		else if(e.getKeyCode() == KeyEvent.VK_DOWN)
			setDownPressed(true);
		else if(e.getKeyCode() == KeyEvent.VK_ENTER);
			setReturnPressed(true);
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		if(e.getKeyCode() == KeyEvent.VK_LEFT)
			setLeftPressed(false);
		else if(e.getKeyCode() == KeyEvent.VK_UP)
			setUpPressed(false);
		else if(e.getKeyCode() == KeyEvent.VK_RIGHT)
			setRightPressed(false);
		else if(e.getKeyCode() == KeyEvent.VK_DOWN)
			setDownPressed(false);
		else if(e.getKeyCode() == KeyEvent.VK_ENTER);
			setReturnPressed(false);
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		
	}
	
}
