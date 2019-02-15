import java.util.ArrayList;
import java.util.List;

public class State 
{
	public int height;
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
		this.height = height;
		score = 0;
		lastMove = null;
	}
	
	public State(State state)
	{
		this.agent = state.agent.clone();
		this.enemy = state.enemy.clone();
		this.score = state.score;
		this.actions = null;
		this.lastMove = state.lastMove;
		this.terminal = state.terminal;
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
		if(actions.isEmpty())
		{
			terminal = true;
		}
		return actions;
	}
	
	public void act(int[] m, boolean myTurn, int side)
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
		if(side == 1)
		{
			if(y2 == height);	
			{
				terminal = true;
			}
		}
		else
		{
			if (y2 == 1)
			{
				terminal = true;
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
					//undo move
					agent[i].x = x1;
					agent[i].y = y1;
					//restore enemy if moved diagonally
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
		}
		else
		{
			for(int i = 0; i < enemy.length; i++)
			{
				if(enemy[i] != null && enemy[i].x == x2 && enemy[i].y == y2)
				{
					enemy[i].x = x1;
					enemy[i].y = y1;
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
	}
	
	public int evaluateState(int side)
	{

		score = 0;
		int topAPawn = 0;
		int topEPawn = 0;
		for(int i = 0; i < enemy.length; i++)
		{
			Point b = enemy[i];
			Point w = agent[i];
			//enemy checks
			if(b == null)
			{
				score++;
			}
			else
			{
				if(side == 1)
				{
					int bestE = (height - b.y - 1);
					if(bestE > topEPawn)
					{
						topEPawn = bestE;
					}
					if(terminal && b.y == height)
					{
						score = -100;
						return score;
					}
				}
				else
				{
					if(b.y > topEPawn)
					{
						topEPawn = b.y;
					}
					if(terminal && b.y == 1)
					{
						score = -100;
						return score;
					}
				}
			}
			//agent checks
			if(w == null)
			{
				score--;
			}
			else
			{
				if(side == 1)
				{
					if(w.y > topAPawn)
					{
						topAPawn = w.y;
					}
					if(terminal && b.y == height)
					{
						score = 100;
						return score;					
					}
				}
				else
				{
					int bestA = (height - w.y - 1);
					if(bestA > topAPawn)
					{
						topAPawn = bestA;
					}
					if(terminal && b.y == 1)
					{
						score = 100;
						return score;
					}
				}
			}	
			if(terminal)
			{
				return 0;
			}
		}
		//if black is evaluating swap values
		System.out.println( "P " + side + " E " + topEPawn + " A " + topAPawn + " S " + score);
		score -= topEPawn * 4;
		score += topAPawn * 4;
		return score;
	}
	
	public void switchSides()
	{
		Point[] temp = agent;
		agent = enemy;
		enemy = temp;
	}
	
	/*
	 * Hash: 0000 000T AAAA AAAA  AAAA EEEE EEEE EEEE EEEE 
	 */
	public long hashState(State state)
	{
		long hash = 0;
		for(int i = 0; i < state.agent.length; i++) {
			if(state.agent[i] != null) {
				hash += (state.agent[i].x + (state.agent[i].y * state.agent.length / 2));
			}
		}
		hash = hash << 12;
		for(int i = 0; i < state.enemy.length; i++) {
			if(state.enemy[i] != null) {
				hash += (state.enemy[i].x + (state.enemy[i].y * state.enemy.length / 2));
			}
		}
		return hash;
	}
}