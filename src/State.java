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
	public State(State state)
	{
		this.agent = state.agent.clone();
		this.enemy = state.enemy.clone();
		this.score = state.score;
		this.actions = null;
		this.lastMove = state.lastMove;
		this.terminal = state.terminal;
		//Since there are only 10 tiles max on width and height we shift the numbers so we can store them in a int
	}
	public List<int[]> listOfActions(int move)
	{
		actions = new ArrayList<int[]>();
		for(Point w:agent)
		{
			if(w == null)
			{
				continue;
			}
			int[] forward = new int[]{w.x, w.y, w.x, (w.y + move)};
			actions.add(forward);
			for(int i = 0; i < enemy.length; i++)
			{
				if(enemy[i] != null && (enemy[i].x == w.x && enemy[i].y - move == w.y))
				{
					actions.remove(forward);
				}
				else if(agent[i] != null && (agent[i].x == w.x && agent[i].y - move == w.y))
				{
					actions.remove(forward);
				}
				else if(enemy[i] != null && enemy[i].y - move == w.y && (enemy[i].x - 1 == w.x || enemy[i].x + 1 == w.x))
				{
					actions.add(new int[] {w.x, w.y, enemy[i].x, (w.y + move)});
				}
			}
		}
		return actions;
	}
	public void act(int[] m, boolean myTurn)
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
				}
				if(enemy[i] != null && enemy[i].x == x2 && enemy[i].y == y2)
				{
					enemy[i] = null;
				}
			}
		}
		else
		{
			for(int i = 0; i < enemy.length; i++)
			{
				if(enemy[i] != null && enemy[i].x == x1 && enemy[i].y == y1)
				{
					enemy[i].x = x2;
					enemy[i].y = y2;
				}
				if(agent[i] != null && agent[i].x == x2 && agent[i].y == y2)
				{
					agent[i] = null;
				}
				
			}
		}
	}
	public void unact(int[] m, boolean myTurn)
	{
		int x1 = m[0], y1 = m[1], x2 = m[2], y2 = m[3];
		if(myTurn)
		{
			for(int i = 0; i < agent.length; i++)
			{
				if(agent[i] != null && agent[i].x == x2 && agent[i].y == y2)
				{
					agent[i].x = x1;
					agent[i].y = y1;
				}
				if(x1 != x2)
				{
					for(int j = 0; j < enemy.length; j++)
					{
						if(enemy[j] == null)
						{
							
							enemy[j] = new Point(x2, y2);
							break;
						}
					}
				}
			}
		}
		else
		{
			for(int i = 0; i < enemy.length; i++)
			{
				if(enemy[i] != null && enemy[i].x == x2 && enemy[i].y == y2)
				{
					enemy[i].x = x1;
					enemy[i].y = y1;
				}
				if(x1 != x2)
				{
					for(int j = 0; j < agent.length; j++)
					{
						if(agent[i] == null)
						{
							
							agent[i] = new Point(x2, y2);
							break;
						}
					}
				}
			}
		}
	}
	public int evaluateState()
	{
		score = 0;
		int topWPawn = 0;
		int topBPawn = 0;
		for(int i = 0; i < enemy.length; i++)
		{
			Point b = enemy[i];
			//enemy checks
			if(b == null)
			{
				score++;
				continue;
			}
			
			int bestB = ((enemy.length / 2) - b.y - 1);
			if(bestB > topBPawn)
			{
				topBPawn = bestB;
			}
			//agent checks
			Point w = agent[i];
			if(w == null)
			{
				score--;
				continue;
			}
			if(w.y > topWPawn)
			{
				topWPawn = w.y;
			}
		}
		score -= topBPawn * 2;
		score += topWPawn * 2;
		return score;
	}
	public void switchSides()
	{
		Point[] temp = agent;
		agent = enemy;
		enemy = temp;
	}
}