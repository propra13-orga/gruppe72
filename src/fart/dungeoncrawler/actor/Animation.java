package fart.dungeoncrawler.actor;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import fart.dungeoncrawler.IUpdateable;
import fart.dungeoncrawler.Tilemap;

public class Animation implements IUpdateable {
	private BufferedImage[] textures;
	private float curDuration;
	private float frameDuration;
	private int numFrames;
	private int curFrame;
	
	/**
	 * Represents an animation. 
	 * 
	 * @param textures Frames for the animation. 
	 * @param frameDuration Duration per frame in ms.
	 */
	public Animation(BufferedImage[] textures, float frameDuration) {
		this.textures = textures;
		this.frameDuration = frameDuration;
		
		curDuration = 0;
		curFrame = 0;
		numFrames = textures.length;
	}
	
	/**
	 * Creates a WalkingAnimation from a BufferedImage. This image must contain 3 frames, each frame should be 
	 * TILE_SIZE * TILE_SIZE, the width of the BufferdImage should be TILE_SIZE * 3. Four frames are generated:
	 * Frame 1: Left frame in source
	 * Frame 2: Mid frame in source
	 * Frame 3: Right frame in source
	 * Frame 4: Mid frame in source
	 * 
	 * @param texture Spritesheet for this animation.
	 * @param frameDuration	Duration per frame in ms.
	 * @return
	 */
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
	
	/**
	 * Returns the texture for the given frameindex.
	 * @param frame frameIndex
	 * @return Desired texture.
	 */
	public BufferedImage getTextureByFrame(int frame) {
		return textures[frame];
	}
	
	/**
	 * Resets the state of this animation.
	 */
	public void reset() {
		System.out.println("reset");
		curDuration = 0;
		curFrame = 0;
	}
	
	/**
	 * Returns the texture for the current state of the animation
	 * @return Current texture.
	 */
	public BufferedImage getTexture() {
		return textures[curFrame];
	}
	
	/**
	 * Returns the current frameIndex
	 * @return frameIndex.
	 */
	public int getCurrentFrame() {
		return curFrame;
	}
}
