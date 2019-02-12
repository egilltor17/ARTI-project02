import java.util.Stack;

public class State 
{
	public Point[] white;
	public Point[] black;
	public int score;
	public Stack<Integer> actions;
	public int lastMove;
	public State(){}
	public State(int width, int height)
	{
		white = new Point[width * 2];
		black = new Point[width * 2];
		for(int i = 0; i < width; i++)
		{
			white[i] = new Point(i, 0);
			white[i + width] = new Point(i, 1);
			black[i] = new Point(i, height - 1);
			black[i + width] = new Point(i, height - 2);
		}
		score = 0;
		lastMove = 0;
		//Since there are only 10 tiles max on width and height we shift the numbers so we can store them in a int
		actions = new Stack<Integer>();
	}
	public void listOfActions()
	{
		for(Point w:white)
		{
			if(w == null)
			{
				continue;
			}
			actions.push(w.x * 1000 + w.y * 100  + w.x * 10 + w.y + 1);
			for(Point b:black)
			{
				if(b == null)
				{
					continue;
				}
				if(b.x == w.x && b.y -1 == w.y)
				{
					actions.remove(w.x * 1000 + w.y * 100  + w.x * 10 + w.y + 1);
				}
				else if(b.y - 1 == w.y && (b.x - 1 == w.x || b.x + 1 == w.x))
				{
					actions.push(w.x * 1000 + w.y * 100  + b.x * 10 + w.y + 1);
				}
			}
		}
	}
	public State act(int m, int height)
	{
		score = 0;
		lastMove = m;
		int topWPawn = 0, topBPawn = 0;
		int y2 = m % 10, x2 = (m /= 10) % 10, y1 = (m /= 10) % 10, x1 = m / 10;
		for(int i = 0; i < black.length; i++)
		{
			System.out.println("b" + black + " " + i);

			//black checks
			if(black[i] == null)
			{
				score++;
				continue;
			}
			if(black[i].x == x2 && black[i].y == y2)
			{
				black[i] = null;
				score++;
			}
			int b = (height - black[i].y - 1);
			if(b > topBPawn)
			{
				topBPawn = b;
			}
			System.out.println("w" + white + " " + i);
			//white checks
			Point w = white[i];
			if(w == null)
			{
				score--;
				continue;
			}
			if(w.x == x1 && w.y == y1)
			{
				white[i].x = x2;
				white[i].y = y2;
			}
			if(y2 > topWPawn)
			{
				topWPawn = y2;
			}
		}
		score -= topBPawn * 2;
		score += topWPawn * 2;
		return this;
	}
}