package eecs2030_SnakeProject;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class Point2D {

	public static final Map<String, Point2D> INSTANCES = new TreeMap<String, Point2D>();
	public static final Map<String, Point2D> POISON_INSTANCES = new TreeMap<String, Point2D>();
	private int x, y;

	private Point2D(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public static Point2D getInstance(int x, int y) {
		String key = ""+x+""+y;
		Point2D tmp = INSTANCES.get(key);
		if(tmp==null)
		{
			tmp = new Point2D(x, y);
			INSTANCES.put(key, tmp);
		}

		return tmp;

	}

	public static Point2D getPosition() {
		Random random = new Random();
		int x = (random.nextInt(33 - 2) + 2) * 25;
		int y = (random.nextInt(23 - 5) + 5) * 25;
		Point2D tmp = getInstance(x, y);
		return tmp;
	}
	
	public static void getPoisonInstance(int x, int y) {
		String key = ""+x+""+y;
		Point2D tmp = INSTANCES.get(key);
		if(tmp==null && POISON_INSTANCES.get(key)==null){
			tmp = new Point2D(x, y);
			POISON_INSTANCES.put(key, tmp);
		}
	}
	

	public int getX() {
		return x;
	}


	public int getY() {
		return y;
	}


}
