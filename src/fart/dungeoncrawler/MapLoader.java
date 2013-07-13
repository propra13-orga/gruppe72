package fart.dungeoncrawler;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import fart.dungeoncrawler.actor.*;
import fart.dungeoncrawler.enums.Heading;
import fart.dungeoncrawler.enums.NPCType;
import fart.dungeoncrawler.items.ItemCollection;
import fart.dungeoncrawler.npc.states.EnemyStateMachine;

import Utils.Vector2;

import nu.xom.*;

/*
 * 		 ID	OBJECT
 * 		------------------
 * 		  0	PORTAL
 * 		  1	GOAL
 * 		  2	FIRE (TRAP)
 * 		  3	MELEEENEMY
 * 		  4	BOSSENEMY
 * 		  5	SHOP NPC
 * 		  6 TALKING NPC
 * 		  7 CHECKPOINT
 * 
 * 		 ID	ITEM
 * 		------------------
 * 		100 
 * 		101
 * 		 .
 * 		 .
 * 		 .
 */

public class MapLoader
{
	private int width, height;
	private int output[][];
	private ArrayList<ActorDescription> aDescriptions;
	private ArrayList<BaseDescription> bDescriptions;
	private String descLoc[][];
	private Game game;
	private StaticObjectManager sManager;
	private DynamicObjectManager dManager;
	private Collision collision;
	private String mapName;
	
	public MapLoader(Game game,
					StaticObjectManager sManager,
					DynamicObjectManager dManager,
					Collision collision)
	{
		this.game = game;
		this.sManager = sManager;
		this.dManager = dManager;
		this.collision = collision;
	}
	
	public void loadMap(Tilemap tilemap, String fileName)
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
			ex.printStackTrace();
			System.exit(1);
		}
		catch (IOException ex)
		{
			System.err.println("Could not read file " + fileName);
			System.exit(1);
		}
		
		Element current;
		
		current = map.getRootElement().getChildElements("name").get(0);
		mapName = current.getChild(0).getValue();
		// Read width and height of map and create an array
		current = map.getRootElement().getChildElements("width").get(0);
		width = Integer.parseInt(current.getChild(0).getValue());
		Tilemap.setWidth(width);
		current = map.getRootElement().getChildElements("height").get(0);
		height = Integer.parseInt(current.getChild(0).getValue());
		Tilemap.setHeight(height);
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
		aDescriptions = new ArrayList<ActorDescription>();
		bDescriptions = new ArrayList<BaseDescription>();
		descLoc = new String[current.getChildElements().size()][2];
		for(i=0; i<current.getChildElements().size(); i++)
		{
			Element tmp = current.getChildElements().get(i);
			boolean saveToA = false;
			
			// Add PortalDescription
			if(tmp.getAttribute(1).getValue().equals("0"))
			{
				bDescriptions.add(new PortalDescription(tmp.getChildElements().get(0).getValue()));
			}
			// Add GoalDescription (BaseDescription)
			else if(tmp.getAttribute(1).getValue().equals("1"))
			{
				bDescriptions.add(new BaseDescription(tmp.getChildElements().get(0).getValue()));
			}
			
			// Add fire TrapDescription
			else if(tmp.getAttribute(1).getValue().equals("2"))
			{
				bDescriptions.add(new TrapDescription(tmp.getChildElements().get(0).getValue(),
						Integer.parseInt(tmp.getChildElements().get(3).getValue())));
			}
			
			// Add MeleeEnemy EnemyDescription
			else if(tmp.getAttribute(1).getValue().equals("3"))
			{
				boolean isRanged = tmp.getChildElements().get(1).getValue().equals("0")?false:true;
				aDescriptions.add(new EnemyDescription(isRanged,
						tmp.getChildElements().get(0).getValue(),
						Integer.parseInt(tmp.getChildElements().get(2).getValue()),
						Integer.parseInt(tmp.getChildElements().get(3).getValue()),
						Integer.parseInt(tmp.getChildElements().get(4).getValue()),
						Integer.parseInt(tmp.getChildElements().get(5).getValue()),
						Integer.parseInt(tmp.getChildElements().get(6).getValue()),
						Stats.getMeleeStats(Integer.parseInt(tmp.getChildElements().get(5).getValue())), Heading.Down));
				saveToA = true;
			}
			// Add BossEnemy EnemyDescription
			else if(tmp.getAttribute(1).getValue().equals("4"))
			{
				boolean isRanged = tmp.getChildElements().get(1).getValue().equals("0")?false:true;
				aDescriptions.add(new EnemyDescription(isRanged,
						tmp.getChildElements().get(0).getValue(),
						Integer.parseInt(tmp.getChildElements().get(2).getValue()),
						Integer.parseInt(tmp.getChildElements().get(3).getValue()),
						Integer.parseInt(tmp.getChildElements().get(4).getValue()),
						Integer.parseInt(tmp.getChildElements().get(5).getValue()),
						Integer.parseInt(tmp.getChildElements().get(6).getValue()),
						Stats.getMeleeStats(Integer.parseInt(tmp.getChildElements().get(5).getValue())), Heading.Down));
				saveToA = true;
			}
			else if(tmp.getAttribute(1).getValue().equals("5"))
			{
				aDescriptions.add(new NPCDescription(tmp.getChildElements().get(0).getValue(),
								NPCType.Shop.ordinal(),
								Integer.parseInt(tmp.getChildElements().get(1).getValue()),
								Integer.parseInt(tmp.getChildElements().get(2).getValue()),
								new Stats(), Heading.Down));
				saveToA = true;
			}
			else if(tmp.getAttribute(1).getValue().equals("6"))
			{
				aDescriptions.add(new NPCDescription(tmp.getChildElements().get(0).getValue(),
								NPCType.Talking.ordinal(),
								Integer.parseInt(tmp.getChildElements().get(1).getValue()),
								Integer.parseInt(tmp.getChildElements().get(2).getValue()),
								new Stats(), Heading.Down));
				saveToA = true;
			}
			else if(tmp.getAttribute(1).getValue().equals("8"))
			{
				aDescriptions.add(new NPCDescription(tmp.getChildElements().get(0).getValue(),
								NPCType.Talking.ordinal(),
								Integer.parseInt(tmp.getChildElements().get(1).getValue()),
								Integer.parseInt(tmp.getChildElements().get(2).getValue()),
								new Stats(), Heading.Down));
				saveToA = true;
			}
			else
				bDescriptions.add(null);
			
			descLoc[i][0] = tmp.getAttribute(1).getValue();
			
			if(saveToA)
				descLoc[i][1] = String.valueOf(aDescriptions.size()-1);
			else
				descLoc[i][1] = String.valueOf(bDescriptions.size()-1);
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
					for(int k=0; k<descLoc.length && pd == null; k++)
						if(descLoc[k][0].equals("0"))
							pd = (PortalDescription) bDescriptions.get(Integer.parseInt(descLoc[k][1]));
					
					if(pd!=null)
					{
						int posX = Integer.parseInt(tmp2.getChildElements().get(0).getValue());
						int posY = Integer.parseInt(tmp2.getChildElements().get(1).getValue());
						String mapToName = tmp2.getChildElements().get(2).getValue();
						int mapToX = Integer.parseInt(tmp2.getChildElements().get(3).getValue());
						int mapToY = Integer.parseInt(tmp2.getChildElements().get(4).getValue());
						
						Portal portal = new Portal(game, pd.getSpritePath(), mapToName, new Vector2(posX,posY), new Vector2(mapToX,mapToY));
						sManager.addObject(portal);
						collision.addTrigger(portal);
					}
					else
					{
						System.err.println("No PortalDescription in XML file, but Portal Objects");
					}
				}
				// Load ALL the goals
				else if(tmp2.getAttribute(0).getValue().equals("1"))
				{
					int posX = Integer.parseInt(tmp2.getChildElements().get(0).getValue());
					int posY = Integer.parseInt(tmp2.getChildElements().get(1).getValue());
					
					Goal goal = new Goal(new Vector2(posX,posY), game);
					sManager.addObject(goal);
					collision.addTriggerOnKey(goal);
				}
				// Load ALL the fire traps
				else if(tmp2.getAttribute(0).getValue().equals("2"))
				{
					TrapDescription td = null;
					for(int k=0; k<descLoc.length && td == null; k++)
						if(descLoc[k][0].equals("2"))
							td = (TrapDescription) bDescriptions.get(Integer.parseInt(descLoc[k][1]));
					
					if(td!=null)
					{
						int posX = Integer.parseInt(tmp2.getChildElements().get(0).getValue());
						int posY = Integer.parseInt(tmp2.getChildElements().get(1).getValue());
						
						Trap trap = new Trap(td, new Vector2(posX,posY));
						sManager.addObject(trap);
						collision.addTrigger(trap);
					}
					else
					{
						System.err.println("No TrapDescription in XML file, but Firetrap Objects");
					}
				}
				// Load ALL the Melee Enemies
				else if(tmp2.getAttribute(0).getValue().equals("3"))
				{
					EnemyDescription ed = null;
					for(int k=0; k<descLoc.length && ed == null; k++)
						if(descLoc[k][0].equals("3"))
							ed = (EnemyDescription) aDescriptions.get(Integer.parseInt(descLoc[k][1]));
					
					if(ed!=null)
					{
						int posX = Integer.parseInt(tmp2.getChildElements().get(0).getValue());
						int posY = Integer.parseInt(tmp2.getChildElements().get(1).getValue());
						
						MeleeEnemy me = new MeleeEnemy(game, new Vector2(posX*Tilemap.TILE_SIZE,posY*Tilemap.TILE_SIZE), ed);
						EnemyStateMachine machine = new EnemyStateMachine(me, game.getAllPlayers());
						me.setMachine(machine);
					}
					else
					{
						System.err.println("No EnemyDescription in XML file, but MeleeEnemy Objects");
					}
				}
				// Load ALL the Boss Enemies
				else if(tmp2.getAttribute(0).getValue().equals("4"))
				{
					EnemyDescription ed = null;
					for(int k=0; k<descLoc.length && ed == null; k++)
						if(descLoc[k][0].equals("4"))
							ed = (EnemyDescription) aDescriptions.get(Integer.parseInt(descLoc[k][1]));
					
					if(ed!=null)
					{
						int posX = Integer.parseInt(tmp2.getChildElements().get(0).getValue());
						int posY = Integer.parseInt(tmp2.getChildElements().get(1).getValue());
						
						BossEnemy be = new BossEnemy(game, new Vector2(posX*Tilemap.TILE_SIZE,posY*Tilemap.TILE_SIZE), ed);
						EnemyStateMachine machine = new EnemyStateMachine(be, game.getAllPlayers());
						be.setMachine(machine);
					}
					else
					{
						System.err.println("No EnemyDescription in XML file, but BossEnemy Objects");
					}
				}
				else if(tmp2.getAttribute(0).getValue().equals("5"))
				{
					NPCDescription npcd = null;
					for(int k=0; k<descLoc.length && npcd == null; k++)
						if(descLoc[k][0].equals("5"))
							npcd = (NPCDescription) aDescriptions.get(Integer.parseInt(descLoc[k][1]));
					
					if(npcd!=null)
					{
						int posX = Integer.parseInt(tmp2.getChildElements().get(0).getValue());
						int posY = Integer.parseInt(tmp2.getChildElements().get(1).getValue());
						
						NPCShop npcshop = new NPCShop(game, new Vector2(posX*Tilemap.TILE_SIZE,posY*Tilemap.TILE_SIZE),
										npcd, new Rectangle(posX*Tilemap.TILE_SIZE-16,posY*Tilemap.TILE_SIZE-16, Tilemap.TILE_SIZE+32, Tilemap.TILE_SIZE+32));
						dManager.addObject(npcshop);
						collision.addTriggerOnKey(npcshop);
						collision.addStaticObject(npcshop.getCollisionRect());
					}
					else
					{
						System.err.println("No NPCDescription in XML file, but NPCShop Objects");
					}
				}
				else if(tmp2.getAttribute(0).getValue().equals("6"))
				{
					NPCDescription npcd = null;
					for(int k=0; k<descLoc.length && npcd == null; k++)
						if(descLoc[k][0].equals("6"))
							npcd = (NPCDescription) aDescriptions.get(Integer.parseInt(descLoc[k][1]));
					
					if(npcd!=null)
					{
						int posX = Integer.parseInt(tmp2.getChildElements().get(0).getValue());
						int posY = Integer.parseInt(tmp2.getChildElements().get(1).getValue());
						
						NPCTalking npctalk = new NPCTalking(game, new Vector2(posX*Tilemap.TILE_SIZE,posY*Tilemap.TILE_SIZE),
										npcd, new Rectangle(posX*Tilemap.TILE_SIZE-16,posY*Tilemap.TILE_SIZE-16, Tilemap.TILE_SIZE+32, Tilemap.TILE_SIZE+32));
						sManager.addObject(npctalk);
						collision.addTriggerOnKey(npctalk);
					}
					else
					{
						System.err.println("No NPCDescription in XML file, but NPCTalking Objects");
					}
				}
				// Load ALL the checkpoints
				else if(tmp2.getAttribute(0).getValue().equals("7"))
				{
					int posX = Integer.parseInt(tmp2.getChildElements().get(0).getValue());
					int posY = Integer.parseInt(tmp2.getChildElements().get(1).getValue());
					
					posX *= Tilemap.TILE_SIZE;
					posY *= Tilemap.TILE_SIZE;
					
					CheckPoint cp = new CheckPoint(game, dManager, collision, tilemap, new Rectangle(posX, posY, 32, 32));
					sManager.addObject(cp);
					collision.addTriggerOnKey(cp);
				}
				else if(tmp2.getAttribute(0).getValue().equals("8"))
				{
					NPCDescription npcd = null;
					for(int k=0; k<descLoc.length && npcd == null; k++)
						if(descLoc[k][0].equals("8"))
							npcd = (NPCDescription) aDescriptions.get(Integer.parseInt(descLoc[k][1]));
					
					if(npcd!=null)
					{
						int posX = Integer.parseInt(tmp2.getChildElements().get(0).getValue());
						int posY = Integer.parseInt(tmp2.getChildElements().get(1).getValue());
						
						ArrayList<Integer> questlist = new ArrayList<Integer>();
						for(int k=2; k<tmp2.getChildElements().size(); k++)
							questlist.add(Integer.valueOf(tmp2.getChildElements().get(k).getValue()));
						
						NPCQuest npcquest = new NPCQuest(game, new Vector2(posX*Tilemap.TILE_SIZE,posY*Tilemap.TILE_SIZE),
										npcd, new Rectangle(posX*Tilemap.TILE_SIZE-16,posY*Tilemap.TILE_SIZE-16, Tilemap.TILE_SIZE+32, Tilemap.TILE_SIZE+32),
										questlist);
						sManager.addObject(npcquest);
						collision.addTriggerOnKey(npcquest);
					}
					else
					{
						System.err.println("No NPCDescription in XML file, but NPCQuest Objects");
					}
				}
				else if(Integer.parseInt(tmp2.getAttribute(0).getValue()) >= 100)
				{
					int itemID = Integer.parseInt(tmp2.getAttribute(0).getValue())-100;
					
					if(itemID < ItemCollection.getInstance().getItemCount())
					{
						int posX = Integer.parseInt(tmp2.getChildElements().get(0).getValue());
						int posY = Integer.parseInt(tmp2.getChildElements().get(1).getValue());
						new MapItem(game, itemID, new Vector2(posX*Tilemap.TILE_SIZE,posY*Tilemap.TILE_SIZE));
					}
					else
					{
						System.err.println("Item with ID "+itemID+" does not exist.");
					}
				}
			}
		}
	}
	
	public int[][] getMap()
	{
		return output;
	}
	
	public String getMapName() {
		return mapName;
	}
}
