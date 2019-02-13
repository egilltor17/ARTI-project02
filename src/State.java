import java.util.ArrayList;
import java.util.List;

public class State 
{
	public Point[] agent;
	public Point[] enemy;
	public int score;
	public List<int[]> actions;
	public int[] lastMove;
	public boolean terminal;
	public State(){}
	public State(int width, int height)
	{
		agent = new Point[width * 2];
		enemy = new Point[width * 2];
		for(int i = 0; i < width; i++)
		{
			agent[i] = new Point(i + 1, 1);
			agent[i + width] = new Point(i + 1, 2);
			enemy[i] = new Point(i + 1, height);
			enemy[i + width] = new Point(i + 1, height - 1);
		}
		score = 0;
		lastMove = null;
		//Since there are only 10 tiles max on width and height we shift the numbers so we can store them in a int
	}
	public List<int[]> listOfActions()
	{
		actions = new ArrayList<int[]>();
		for(Point w:agent)
		{
			if(w == null)
			{
				continue;
			}
			int[] forward = new int[]{w.x, w.y, w.x, (w.y + 1)};
			actions.add(forward);
			for(int i = 0; i < enemy.length; i++)
			{
				if(enemy[i] == null )
				{
					continue;
				}
				if((enemy[i].x == w.x && enemy[i].y -1 == w.y) || (agent[i] != null  && agent[i].x == w.x && agent[i].y -1 == w.y))
				{
					actions.remove(forward);
				}
				else if(enemy[i].y - 1 == w.y && (enemy[i].x - 1 == w.x || enemy[i].x + 1 == w.x))
				{
					actions.add(new int[] {w.x, w.y, enemy[i].x, (w.y + 1)});
				}
			}
			System.out.println(actions);
		}
		return actions;
	}
	public State act(int[] m, int height, boolean myTurn)
	{
		lastMove = m;
		int x1 = m[0], y1 = m[1], x2 = m[2], y2 = m[3];
		if(myTurn)
		{
			for(int i = 0; i < agent.length; i++)
			{
				if(agent[i] != null && agent[i].x == x1 && agent[i].y == y1)
				{
					agent[i].x = x2;
					agent[i].y = y2;
					break;
				}
			}
		}
		for(int i = 0; i < enemy.length; i++)
		{
			if(enemy[i] != null && enemy[i].x == x1 && enemy[i].y == y1)
			{
				enemy[i].x = x2;
				enemy[i].y = y2;
				break;			}
		}
		return this;
		
		/*for(int i = 0; i < enemy.length; i++)
		{
			//enemy checks
			if(enemy[i] == null)
			{
				score++;
				continue;
			}
			if(enemy[i].x == x2 && enemy[i].y == y2)
			{
				enemy[i] = null;
				score++;
			}
			int b = (height - enemy[i].y - 1);
			if(b > topBPawn)
			{
				topBPawn = b;
			}
			//agent checks
			Point w = agent[i];
			if(w == null)
			{
				score--;
				continue;
			}
			if(w.x == x1 && w.y == y1)
			{
				agent[i].x = x2;
				agent[i].y = y2;
			}
			if(y2 > topWPawn)
			{
				topWPawn = y2;
			}
		}
		score -= topBPawn * 2;
		score += topWPawn * 2;
		return this;*/
	}
}