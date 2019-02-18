import java.util.ArrayList;
import java.util.List;

public class State 
{
	public int height;
	public Point[] agent;
	public Point[] enemy;
	public int score;
	public List<int[]> actions;
	public boolean terminal;
	public boolean reachedDepth;
	public int side;
	
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
		this.terminal = false;
		this.reachedDepth = false;
		this.score = 0;
	}
	
	public State(State state)
	{
		this.height = state.height;
		//need to make new arrays and points, so the new state doesn't share them with the other state
		this.agent = new Point[state.agent.length];
		this.enemy = new Point[state.enemy.length];
		for(int i = 0; i < this.agent.length; i++)
		{
			if(state.agent[i] != null)
			{
				this.agent[i] = new Point(state.agent[i].x, state.agent[i].y);
			}
			if(state.enemy[i] != null)
			{
				this.enemy[i] = new Point(state.enemy[i].x, state.enemy[i].y);
			}
		}
		this.score = 0;
		this.actions = null;
		this.terminal = false;
		this.reachedDepth = false;
		this.side = state.side;
	}
	//find all legal actions in the current state, if no action is found the state is a draw
	public List<int[]> listOfActions()
	{
		actions = new ArrayList<int[]>();
		for(Point w:agent) {
			if(w == null) {
				continue;
			}
			//add forward so we can potentially remove it id there is a pawn in front of the pawn
			int[] forward = new int[]{w.x, w.y, w.x, (w.y + side)};
			actions.add(forward);
			for(int i = 0; i < enemy.length; i++) {
				//this can only happen once so no chance that removing will throw an exception
				if(enemy[i] != null && (enemy[i].x == w.x && enemy[i].y - side == w.y)) {
					actions.remove(forward);
				}
				else if(agent[i] != null && (agent[i].x == w.x && agent[i].y - side == w.y)) {
					actions.remove(forward);
				}
				//check for diagonal enemy
				if(enemy[i] != null && enemy[i].y - side == w.y && (enemy[i].x - 1 == w.x || enemy[i].x + 1 == w.x)) {
					actions.add(new int[] {w.x, w.y, enemy[i].x, (w.y + side)});
				}
			}
		}
		//no moves.... draw
		if(actions.isEmpty()) {
			terminal = true;
		}
		return actions;
	}
	//Execute the move m
	public void act(int[] m)
	{
		
		int x1 = m[0], y1 = m[1], x2 = m[2], y2 = m[3];
		boolean move = true;
		for(int i = 0; i < agent.length; i++) {
			//find a pawn in the starting destination
			if(agent[i] != null && agent[i].x == x1 && agent[i].y == y1) {
				//error handling
				if(!move) {
					agent[i] = null;
					System.out.println("cant move same guy twice");
				}
				else {
					agent[i].x = x2;
					agent[i].y = y2;
				}
				move = false;
			}
			//if there's an enemy in the new square we must kill it
			if(enemy[i] != null && enemy[i].x == x2 && enemy[i].y == y2) {
				enemy[i] = null;
			}
		}
		if(move) {
			System.out.println("no one moved");
		}
		//check if the move ends the game
		if(side == 1) {
			if(y2 == height) {
				terminal = true;
			}
		}
		else {
			if (y2 == 1) {
				terminal = true;
			}
		}
		//switch sides so the other player can move next
		side = -side;
		switchSides();
	}
	//undo a move and restore pawns
	public void unact(int[] m)
	{
		terminal = false;
		//switch sides before doing anything so we don't restore for the wrong side
		side = -side;
		switchSides();
		int x1 = m[0], y1 = m[1], x2 = m[2], y2 = m[3];
		for(int i = 0; i < agent.length; i++) {
			if(agent[i] != null && agent[i].x == x2 && agent[i].y == y2) {
				//undo move
				agent[i].x = x1;
				agent[i].y = y1;
				//restore enemy if moved diagonally
				if(x1 != x2) {
					for(int j = 0; j < enemy.length; j++) {
						if(enemy[j] == null) {
							
							enemy[j] = new Point(x2, y2);
							break;
						}
					}
				}
			}
		}
	}
	//main evaluation function
	public int evaluateState()
	{
		score = 0;
		int topAPawn = 0;
		int topEPawn = 0;
		for(int i = 0; i < enemy.length; i++){
			Point b = enemy[i];
			Point w = agent[i];

			//enemy checks
			if(b == null) {
				//if an enemy is dead increase the score
				score++;
			}
			else {
				if(side == 1) {

					int bestE = (height - b.y - 1);
					if(bestE > topEPawn) {
						topEPawn = bestE;
					}
					if(terminal && b.y == 1) {
						score = -100;
						return score;
					}
				}
				else {

					if(b.y > topEPawn) {
						topEPawn = b.y;
					}
					
					if(terminal && b.y == height) {
						score = -100;
						return score;
					}
				}
			}

			//agent checks
			if(w == null) {
				//each of our dead pawns decrease score
				score--;
			}
			else {

				if(side == 1) {
					if(w.y > topAPawn) {
						topAPawn = w.y;
					}
					if(terminal && w.y == height) {
						score = 100;
						return score;
					}
				}
				else {
					int bestA = (height - w.y - 1);
					if(bestA > topAPawn) {
						topAPawn = bestA;
					}
					if(terminal && w.y == 1) {
						score = 100;
						return score;
					}
				}
			}	
		}
		if(terminal) {
			score = 0;
			return score;
		}
		//multiply by two because progress is more important than killing
		score -= topEPawn * 2;
		score += topAPawn * 2;
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
	public void print()
	{
    	char[][] field = new char[height + 1][agent.length / 2 + 1];
		for(int i = 0; i < agent.length; i++)
    	{
    		System.out.println();
    		if(agent[i] != null)
    		{
        		if(side == 1)
        		{
        			System.out.print("W" + agent[i].x + agent[i].y + "      ");
        			field[agent[i].y][agent[i].x] = 'W';
        		}
        		else
        		{
        			System.out.print("B" + agent[i].x + agent[i].y + "      ");
        			field[agent[i].y][agent[i].x] = 'B';

        		}
    		}
    		else
    		{
    			if(side == 1)
        		{
        			System.out.print("W        ");
        		}
        		else
        		{
        			System.out.print("B        ");
        		}
    		}
    		if(enemy[i] != null)
    		{
    			if(side == -1)
        		{
        			System.out.print("W" + enemy[i].x + enemy[i].y + "      ");
            		field[enemy[i].y][enemy[i].x] = 'W';
        		}
        		else
        		{
        			System.out.print("B" + enemy[i].x + enemy[i].y + "      ");
            		field[enemy[i].y][enemy[i].x] = 'B';
        		}
    		}
    		else
    		{
    			if(side == -1)
        		{
        			System.out.print("W        ");
        		}
        		else
        		{
        			System.out.print("B        ");
        		}
    		}
    	}
    	System.out.println();
		System.out.println("------------------------------------");
    	for(int i = field.length - 1; i >= 0; i--)
    	{
    		for(int j = 0; j < field[i].length; j++)
    		{
    			System.out.print(field[i][j] + " ");
    		}
    		System.out.println();
    	}
		System.out.println("------------------------------------");
	}
}