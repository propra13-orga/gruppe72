package Utils;

public class Vector2 {
	public float x;
	public float y;
	
	public static final Vector2 Zero = new Vector2();
	
	public Vector2() {
		x = 0;
		y = 0;
	}
	
	public Vector2(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2 add(Vector2 v) {
		return new Vector2(x + v.x, y + v.y);
	}
	
	public Vector2 sub(Vector2 v) {
		return new Vector2(x - v.x, y - v.y);
	}
	
	public Vector2 mul(float s) {
		return new Vector2((x * s), (y * s));
	}
	
	public void normalize() {
		float xabs = Math.abs(x);
		float yabs = Math.abs(y);
		
		if(xabs >= yabs) {
			x = (x > 0) ? 1 : -1;
			y = 0;
		} else {
			x = 0;
			y = (y > 0) ? 1 : -1;
		}
	}
	
	public float length() {
		return (float)(Math.sqrt(x * x + y * y));
	}
	
	public float dot(Vector2 v) {
		return v.x * x + v.y * y;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o.getClass() == getClass()) {
			Vector2 v = (Vector2)o;
			return (v.x == x) && (v.y == y);
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		int result = 7;
		result = result * 17 + (int)(x * 31);
		result = result * 17 + (int)(y * 31);
		
		return result;
	}
}
