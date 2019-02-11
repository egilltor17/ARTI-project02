import java.util.Stack;

public class State 
{
	public Point[] white;
	public Point[] black;
	public int score;
	public Stack<String> actions;
	public int[] lastMove;
	public State(){}
	public State(int width, int height)
	{
		lastMove = null;
		Point[] white = new Point[width * 2];
		Point[] black = new Point[width * 2];
		for(int i = 0; i < width; i++)
		{
			white[i] = new Point(i, 0);
			white[i] = new Point(i, 1);
			black[i] = new Point(i, height - 1);
			black[i] = new Point(i, height - 2);

		}
		actions = new Stack<String>();
		
	}
	public void listOfActions()
	{
		for(Point w:white)
		{
			actions.push(w.x + "" + w.y + "" + w.x + "" + w.y + 1);
			actions.push(w.x + "" + w.y + "" + (w.x - 1) + "" + w.y + 1);
			actions.push(w.x + "" + w.y + "" + (w.x + 1) + "" + w.y + 1);
			for(Point b:black)
			{
				if(b.x == w.x && b.y -1 == w.y)
				{
					actions.remove(w.x + "" + w.y + "" + w.x + "" + w.y + 1);

				}
				else if(b.y - 1 == w.y && (b.x - 1 == w.x || b.x + 1 == w.x))
				{
					actions.remove(w.x + "" + w.y + "" + b.x + "" + w.y + 1);

				}
			}
		}
	}
}