package fart.dungeoncrawler.mapeditor;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class ImageManager
{
	private ArrayList<String> objIDs;
	private HashMap<String, BufferedImage> images;

	public ImageManager()
	{
		objIDs = new ArrayList<String>();
		images = new HashMap<String, BufferedImage>();
	}
	
	public void add(String id, String imagePath)
	{
		BufferedImage bi = null;
		
		try
		{
			bi = ImageIO.read(new File(imagePath));
		} catch (IOException e)
		{
			System.err.println("Could not load image "+imagePath);
			return;
		}
		
		objIDs.add(id);
		images.put(id, bi);
	}
	
	public void remove(String id)
	{
		for(int i=0; i<objIDs.size(); i++)
		{
			if(objIDs.get(i).equals(id))
			{
				objIDs.remove(i);
				images.remove(id);
				
				i = objIDs.size();
			}
		}
	}
	
	public String getID(int index)
	{
		return objIDs.get(index);
	}
	
	public BufferedImage getImage(String id)
	{
		return (BufferedImage)images.get(id);
	}
	
	public BufferedImage getImage(int idIndex)
	{
		return (BufferedImage)images.get(objIDs.get(idIndex));
	}
	
	public int getSize()
	{
		return objIDs.size();
	}
}
