package fart.dungeoncrawler.network;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

import Utils.Vector2;

import fart.dungeoncrawler.IDrawable;

public class DeathMatchStatistics implements IDrawable {
	private static DeathMatchStatistics instance;
	
	private static Color bgColor = new Color(0.4f, 0.4f, 0.8f, 0.75f);
	private static Font font = new Font("Arial", 0x1, 16);
	private static Color fontColor = new Color(0.1f, 0.1f, 0.35f);
	private static Vector2 startPos = new Vector2(20 * 32, 2 * 32);
	
	private ClientInfo[] infos;
	private Integer[] kills;
	private Integer[] deaths;
	
	private DeathMatchStatistics(ArrayList<ClientInfo> infos) {
		this.infos = new ClientInfo[infos.size()];
		infos.toArray(this.infos);
		kills = new Integer[infos.size()];
		deaths = new Integer[infos.size()];
		
		for(int i = 0; i < kills.length; i++) {
			kills[i] = 0;
			deaths[i] = 0;
		}
	}
	
	public static void createInstance(ArrayList<ClientInfo> infos) {
		instance = new DeathMatchStatistics(infos);
	}
	
	public static DeathMatchStatistics getInstance() {
		return instance;
	}
	
	public void killed(int killerID, int deadID) {
		kills[killerID] += 1;
		deaths[deadID] += 1;
	}

	@Override
	public void draw(Graphics2D graphics) {
		graphics.setColor(bgColor);
		graphics.fillRect((int)startPos.x, (int)startPos.y, 10 * 32, 6 * 32);
		
		graphics.setColor(fontColor);
		graphics.setFont(font);
		
		graphics.drawString("Kills", startPos.x + 16 + 5 * 32, startPos.y + 32);
		graphics.drawString("Deaths", startPos.x + 16 + 7 * 32, startPos.y + 32);
		
		for(int i = 0; i < infos.length; i++) {
			graphics.drawString(infos[i].name, startPos.x + 16, startPos.y + 64 + i * 32);
			graphics.drawString(kills[i].toString(), startPos.x + 16 + 5 * 32, startPos.y + 64 + i * 32);
			graphics.drawString(deaths[i].toString(), startPos.x + 16 + 7 * 32, startPos.y + 64 + i * 32);
		}
	}

}
