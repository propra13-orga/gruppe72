package fart.dungeoncrawler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import nu.xom.*;

public class MapLoader
{
	private int width, height;
	private int output[][];
	private ArrayList<Element> descriptions;
	private ArrayList<Element> gameobjects;
	
	public MapLoader()
	{
		descriptions = new ArrayList<Element>();
		gameobjects = new ArrayList<Element>();
	}
	
	public void loadMap(String fileName)
	{
		File source = new File(fileName);
		
		// Loads the XML File into "map"
		Document map = null;
		try
		{
			Builder builder = new Builder(true);
			map = builder.build(source);
		}
		catch (ValidityException ex)
		{
			map = ex.getDocument();
		}
		catch (ParsingException ex)
		{
			System.err.println("This file is not well-formed.");
			System.exit(1);
		}
		catch (IOException ex)
		{
			System.err.println("Could not read file " + fileName);
			System.exit(1);
		}
		
		Element current;
		
		// Read width and height of map and create an array
		current = map.getRootElement().getChildElements("width").get(0);
		width = Integer.parseInt(current.getChild(0).getValue());
		current = map.getRootElement().getChildElements("height").get(0);
		height = Integer.parseInt(current.getChild(0).getValue());
		output = new int[width][height];
		//System.out.println("width: " + width + ", height: " + height);
		
		String tiles = map.getRootElement().getChildElements("tiles").get(0).getValue();
		int i=0, j=0;
		char curChar;
	
		// go through tiles string
		for(int k=0; k<tiles.length(); k++)
		{
			curChar = tiles.charAt(k);
			
			// If read character is a new line and we filled a line,
			// go into next line
			if(curChar == '\n' && i==width)
			{
				j++;
				i=0;
			}
			// If read character is neither a new line nor a tab,
			// fill character into array
			else if(curChar == ' ')
			{
				output[i][j] |= 1;
				i++;
			}
			else if(curChar == '#')
			{
				output[i][j] |= 2;
				i++;
			}
		}
		
		current = map.getRootElement().getChildElements("descriptions").get(0);
		for(i=0; i<current.getChildElements().size(); i++)
			descriptions.add(current.getChildElements().get(i));

		current = map.getRootElement().getChildElements("gameobjects").get(0);
		for(i=0; i<current.getChildElements().size(); i++)
		{
			Element tmp = current.getChildElements().get(i);
			for(j=0; j<tmp.getChildElements().size(); j++)
				gameobjects.add(tmp.getChildElements().get(j));
		}
	}
	
	public int[][] getMap()
	{
		return output;
	}
	
	public ArrayList<Element> getDescriptions()
	{
		return descriptions;
	}
	
	public ArrayList<Element> getGameObjects()
	{
		return gameobjects;
	}
	
	public void outputAll()
	{
		for(int j=0; j<height; j++)
		{
			for(int i=0; i<width; i++)
			{
				System.out.print(output[i][j]);
			}
			System.out.println();
		}
		
		for(int i=0; i<descriptions.size(); i++)
		{
			String data = "";
			Element current = descriptions.get(i);
			
			data += current.getQualifiedName();
			data += " name: " + current.getAttribute(0).getValue();
			data += " - id: " + current.getAttribute(1).getValue() + "\n";
			for(int j=0; j<current.getChildElements().size(); j++)
			{
				data += current.getChildElements().get(j).getQualifiedName();
				data += ": " + current.getChildElements().get(j).getValue() + "\n";
			}
			
			System.out.println(data);
		}
		
		for(int i=0; i<gameobjects.size(); i++)
		{
			String data = "";
			Element current = gameobjects.get(i);
			
			data += current.getQualifiedName();
			data += " id: " + current.getAttribute(0).getValue() + "\n";
			for(int j=0; j<current.getChildElements().size(); j++)
			{
				data += current.getChildElements().get(j).getQualifiedName();
				data += ": " + current.getChildElements().get(j).getValue() + "\n";
			}
			
			System.out.println(data);
		}
	}
}
