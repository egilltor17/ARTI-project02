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
    	state.print();
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
            	state.print();

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
    		if (move == null)
    		{
    			return "NOOP";
    		}
        	state.act(move);
    		state.print();
        	return "(move " + move[0] + " " + move[1] + " " + move[2] + " " + move[3] + ")";
    	}
    	return "NOOP";
    	
	}

	// is called when the game is over or the match is aborted
	@Override
	public void cleanup() {
		// TODO: cleanup so that the agent is ready for the next match
	}

}
