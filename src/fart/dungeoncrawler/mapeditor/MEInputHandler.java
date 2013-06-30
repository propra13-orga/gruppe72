package fart.dungeoncrawler.mapeditor;

import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputListener;

public class MEInputHandler implements MouseInputListener
{

	@Override
	public void mouseClicked(MouseEvent e)
	{
		useMouseLocation(e, true);
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		useMouseLocation(e, false);
	}

	public void useMouseLocation(MouseEvent e, boolean clicked)
	{
		Object obj = e.getSource();
		
		if(obj instanceof MEPanel)
			((MEPanel)obj).useMouseLocation(e.getPoint(), clicked);
	}
}
