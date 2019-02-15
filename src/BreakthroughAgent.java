//import java.util.List;
//import java.util.Random;

public class BreakthroughAgent implements Agent
{
	private String role; // the name of this agent's role (white or black)
	private int playClock; // this is how much time (in seconds) we have before nextAction needs to return a move
	private boolean myTurn; // whether it is this agent's turn or not
	private int width, height; // dimensions of the board
	private State state;
	
	/*
		init(String role, int playclock) is called once before you have to select the first action. Use it to initialize the agent. role is either "white" or "black" and playclock is the number of seconds after which nextAction must return.
	*/
    public void init(String role, int width, int height, int playClock) {
		this.role = role;
		this.playClock = playClock;
		myTurn = !role.equals("white");
		this.width = width;
		this.height = height;
		this.state = new State(width, height);
    	if(!myTurn)
    	{
    		state.side = 1;
    	}
    	else
    	{
    		state.switchSides();
    		state.side = -1;
    	}
    	print();
    }

	// lastMove is null the first time nextAction gets called (in the initial state)
    // otherwise it contains the coordinates x1,y1,x2,y2 of the move that the last player did
    public String nextAction(int[] lastMove) {
    	myTurn = !myTurn;
    	if(myTurn)
    	{
    		if(lastMove != null)
    		{
            	state.act(lastMove);
            	print();
    		}
    		/*List<int[]> actions = state.listOfActions(side);
        	Random random = new Random();
        	int[] move = actions.get(random.nextInt(actions.size() - 1));*/
    		NegaMax nega = new NegaMax(playClock);
    		/*int[] move = null;
    		try {
    			move = nega.MiniMaxDepthLimitedRoot(state, 6, side);
    		}catch(Exception e) {System.out.println("something happened");};*/
    		int[]move = nega.iterativeDepthSearch(state, 10);
        	state.act(move);
        	System.out.println(state.lastMove[0] + " " + state.lastMove[1] + " " + state.lastMove[2] + " " + state.lastMove[3]);
        	print();
        	return "(move " + move[0] + " " + move[1] + " " + move[2] + " " + move[3] + ")";
    	}
    	return "NOOP";
    	
	}
    
    public void print()
    {
    	char[][] field = new char[height + 1][width + 1];
    	for(int i = 0; i < state.agent.length; i++)
    	{
    		System.out.println();
    		if(state.agent[i] != null)
    		{
        		if(state.side == 1)
        		{
        			System.out.print("W" + state.agent[i].x + state.agent[i].y + "      ");
        			field[state.agent[i].y][state.agent[i].x] = 'W';
        		}
        		else
        		{
        			System.out.print("W" + state.agent[i].x + state.agent[i].y + "      ");
        			field[state.agent[i].y][state.agent[i].x] = 'B';

        		}
    		}
    		else
    		{
    			if(state.side == 1)
        		{
        			System.out.print("W        ");
        		}
        		else
        		{
        			System.out.print("B        ");
        		}
    		}
    		if(state.enemy[i] != null)
    		{
    			if(state.side == -1)
        		{
        			System.out.print("W" + state.enemy[i].x + state.enemy[i].y + "      ");
            		field[state.enemy[i].y][state.enemy[i].x] = 'W';
        		}
        		else
        		{
        			System.out.print("B" + state.enemy[i].x + state.enemy[i].y + "      ");
            		field[state.enemy[i].y][state.enemy[i].x] = 'B';
        		}
    		}
    		else
    		{
    			if(state.side == -1)
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

	// is called when the game is over or the match is aborted
	@Override
	public void cleanup() {
		// TODO: cleanup so that the agent is ready for the next match
	}

}
