package fart.dungeoncrawler;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Animation implements IUpdateable {
	private BufferedImage[] textures;
	private float curDuration;
	private float frameDuration;
	private int numFrames;
	private int curFrame;
	
	public Animation(BufferedImage[] textures, float frameDuration) {
		this.textures = textures;
		this.frameDuration = frameDuration;
		
		curDuration = 0;
		curFrame = 0;
		numFrames = textures.length;
	}
	
	public static Animation createWalkingAnimation(BufferedImage texture, float frameDuration){
		BufferedImage[] walkTextures = new BufferedImage[4];
		int size = Tilemap.TILE_SIZE;
		
		for(int i = 0; i < 4; i++) {
			walkTextures[i] = new BufferedImage(Tilemap.TILE_SIZE, Tilemap.TILE_SIZE, texture.getType());
			Graphics2D graphics = walkTextures[i].createGraphics();
			
			graphics.drawImage(texture, 0, 0, size, size, size * i, 0, size * i + size, size, null);
		}
		
		walkTextures[3] = walkTextures[1];
		
		Animation result = new Animation(walkTextures, frameDuration);
		
		return result;
	}

	@Override
	public void update(float elapsed) {
		curDuration += elapsed;
		
		if(curDuration >= frameDuration) {
			curDuration %= frameDuration;
			
			curFrame += 1;
			//System.out.println("CurFrame = " + curFrame);
			if(curFrame >= numFrames) {
				curFrame = 0;
				//System.out.println("CurFrame = " + curFrame);
			}
		}
	}
	
	public BufferedImage getTextureByFrame(int frame) {
		return textures[frame];
	}
	
	public void reset() {
		System.out.println("reset");
		curDuration = 0;
		curFrame = 0;
	}
	
	public BufferedImage getTexture() {
		return textures[curFrame];
	}
}
