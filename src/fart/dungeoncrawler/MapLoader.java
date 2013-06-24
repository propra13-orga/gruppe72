package fart.dungeoncrawler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import fart.dungeoncrawler.actor.BaseDescription;

import Utils.Vector2;

import nu.xom.*;

/*
 * 		ID	OBJECT
 * 		------------------
 * 		 0	PORTAL
 * 		 1	GOAL
 * 		 2	FIRE (TRAP)
 * 		 3	MELEEENEMY
 */

public class MapLoader
{
	private int width, height;
	private int output[][];
	private ArrayList<BaseDescription> descriptions;
	private String descLoc[][];
	private Game game;
	private StaticObjectManager sManager;
	private DynamicObjectManager dManager;
	private Collision collision;
	
	public MapLoader(Game game,
					/*String fileName,*/
					StaticObjectManager sManager,
					DynamicObjectManager dManager,
					Collision collision)
	{
		//loadMap(game, fileName, sManager, dManager, collision);
		this.game = game;
		this.sManager = sManager;
		this.dManager = dManager;
		this.collision = collision;
	}
	
	public void loadMap(//Game game,
					String fileName/*,
					StaticObjectManager sManager,
					DynamicObjectManager dManager,
					Collision collision*/)
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
		
		collision.changeMap(this);
		sManager.clearObjects();
		dManager.clearObjects();
		
		current = map.getRootElement().getChildElements("descriptions").get(0);
		descriptions = new ArrayList<BaseDescription>();
		descLoc = new String[current.getChildElements().size()][2];
		for(i=0; i<current.getChildElements().size(); i++)
		{
			Element tmp = current.getChildElements().get(i);
			if(tmp.getAttribute(1).getValue().equals("0"))
			{
				descriptions.add(new PortalDescription(tmp.getChildElements().get(0).getValue()));
			}
			else if(tmp.getAttribute(1).getValue().equals("1"))
			{
				descriptions.add(new BaseDescription(tmp.getChildElements().get(0).getValue()));
			}
			else if(tmp.getAttribute(1).getValue().equals("2"))
			{
				descriptions.add(new TrapDescription(tmp.getChildElements().get(0).getValue(),
						Integer.parseInt(tmp.getChildElements().get(3).getValue())));
			}
			else if(tmp.getAttribute(1).getValue().equals("3"))
			{
				boolean isRanged = tmp.getChildElements().get(0).getValue().equals("0")?false:true;
				descriptions.add(null);
			}
			else
				descriptions.add(null);
			
			descLoc[i][0] = tmp.getAttribute(1).getValue();
			descLoc[i][1] = String.valueOf(descriptions.size()-1);
		}
		
		//DEBUG
		for(i=0; i<descLoc.length; i++)
		{
			System.out.println(descLoc[i][0]+", "+descLoc[i][1]);
		}

		current = map.getRootElement().getChildElements("gameobjects").get(0);
		for(i=0; i<current.getChildElements().size(); i++)
		{
			Element tmp = current.getChildElements().get(i);
			for(j=0; j<tmp.getChildElements().size(); j++)
			{
				Element tmp2 = tmp.getChildElements().get(j);
				
				// Load ALL the portals
				if(tmp2.getAttribute(0).getValue().equals("0"))
				{
					//PortalDescription pd = (PortalDescription) descriptions[0];
					PortalDescription pd = null;
					for(int k=0; k<descriptions.size() && pd == null; k++)
						if(descLoc[k][0].equals("0"))
							pd = (PortalDescription) descriptions.get(Integer.parseInt(descLoc[k][1]));
					
					if(pd==null)
					{
						System.err.println("No PortalDescription in XML file, but Portal Objects");
						System.exit(1);
					}
					
					int posX = Integer.parseInt(tmp2.getChildElements().get(0).getValue());
					int posY = Integer.parseInt(tmp2.getChildElements().get(1).getValue());
					String mapToName = tmp2.getChildElements().get(2).getValue();
					int mapToX = Integer.parseInt(tmp2.getChildElements().get(3).getValue());
					int mapToY = Integer.parseInt(tmp2.getChildElements().get(4).getValue());
					
					Portal portal = new Portal(game, pd.getSpritePath(), mapToName, new Vector2(posX,posY), new Vector2(mapToX,mapToY));
					sManager.addObject(portal);
					collision.addTrigger(portal);
				}
				// Load ALL the goals
				else if(tmp2.getAttribute(0).getValue().equals("1"))
				{
					int posX = Integer.parseInt(tmp2.getChildElements().get(0).getValue());
					int posY = Integer.parseInt(tmp2.getChildElements().get(1).getValue());
					
					Goal goal = new Goal(new Vector2(posX,posY), game);
					sManager.addObject(goal);
					collision.addTrigger(goal);
				}
				// Load ALL the fire traps
				else if(tmp2.getAttribute(0).getValue().equals("2"))
				{
					TrapDescription td = null;
					for(int k=0; k<descriptions.size() && td == null; k++)
						if(descLoc[k][0].equals("2"))
							td = (TrapDescription) descriptions.get(Integer.parseInt(descLoc[k][1]));
					
					if(td==null)
					{
						System.err.println("No TrapDescription in XML file, but Firetrap Objects");
						System.exit(1);
					}
					
					int posX = Integer.parseInt(tmp2.getChildElements().get(0).getValue());
					int posY = Integer.parseInt(tmp2.getChildElements().get(1).getValue());
					
					Trap trap = new Trap(td, new Vector2(posX,posY));
					sManager.addObject(trap);
					collision.addTrigger(trap);
				}
			}
		}
	}
	
	public int[][] getMap()
	{
		return output;
	}
}
