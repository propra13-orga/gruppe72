package fart.dungeoncrawler.mapeditor;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

/**
 * The imagemanager is responsible for loading and handling all textures needed by the mapeditor.
 * @author Timo
 *
 */
public class ImageManager
{
	private ArrayList<String> objIDs;
	private HashMap<String, BufferedImage> images;
	
	private ArrayList<ArrayList<String>> objCat;
	private final int categoryCount = 4;

	/**
	 * Creates and initializes a new instance.
	 */
	public ImageManager()
	{
		objIDs = new ArrayList<String>();
		images = new HashMap<String, BufferedImage>();
		objCat = new ArrayList<ArrayList<String>>(categoryCount);
		
		for(int i=0; i<categoryCount; i++)
			objCat.add(new ArrayList<String>());
	}
	
	/**
	 * Loads a texture from disc and stores it in correct list.
	 * @param id 
	 * @param oc category of the object
	 * @param imagePath path to the texture
	 */
	public void add(String id, ObjectCategory oc, String imagePath)
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
		
		if(oc == ObjectCategory.misc)
			objCat.get(0).add(id);
		else if(oc == ObjectCategory.enemies)
			objCat.get(1).add(id);
		else if(oc == ObjectCategory.npcs)
			objCat.get(2).add(id);
		else if(oc == ObjectCategory.items)
			objCat.get(3).add(id);
	}
	
	/**
	 * Removes a texture. 
	 * @param id
	 */
	public void remove(String id)
	{
		for(int i=0; i<objIDs.size(); i++)
		{
			if(objIDs.get(i).equals(id))
			{
				objIDs.remove(i);
				images.remove(id);
				
				for(int j=0; j<objCat.size(); j++)
				{
					if(objCat.get(j).indexOf(id) != -1)
					{
						objCat.get(j).remove(id);
						j = objCat.size();
					}
				}
				
				i = objIDs.size();
			}
		}
	}
	
	/**
	 * Returns the ID of a texture.
	 * @param index
	 * @return
	 */
	public String getID(int index)
	{
		return objIDs.get(index);
	}
	
	/**
	 * Returns the object category to a specific ID.
	 * @param id
	 * @return
	 */
	public int getCategory(String id)
	{
		for(int i=0; i<objCat.size(); i++)
			if(objCat.get(i).indexOf(id) != -1)
				return i;
		
		return -1;
	}
	
	/**
	 * Returns the texture to a specific ID.
	 * @param id
	 * @return
	 */
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
