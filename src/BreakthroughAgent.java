import java.util.List;
import java.util.Random;
import java.util.Stack;

public class BreakthroughAgent implements Agent
{
	private String role; // the name of this agent's role (white or black)
	private int playclock; // this is how much time (in seconds) we have before nextAction needs to return a move
	private boolean myTurn; // whether it is this agent's turn or not
	private int width, height; // dimensions of the board
	private State state;
	
	/*
		init(String role, int playclock) is called once before you have to select the first action. Use it to initialize the agent. role is either "white" or "black" and playclock is the number of seconds after which nextAction must return.
	*/
    public void init(String role, int width, int height, int playclock) {
		this.role = role;
		this.playclock = playclock;
		myTurn = !role.equals("white");
		this.width = width;
		this.height = height;
		this.state = new State(width, height);

    }

	// lastMove is null the first time nextAction gets called (in the initial state)
    // otherwise it contains the coordinates x1,y1,x2,y2 of the move that the last player did
    public String nextAction(int[] lastMove) {
    	myTurn = !myTurn;
    	if(myTurn)
    	{
    		if(lastMove != null)
    		{
            	System.out.println("enemy " + lastMove[0] + " " + lastMove[1] + " " + lastMove[2] + " " + lastMove[3]);
            	state.act(lastMove, height, !myTurn);
    		}
    		List<int[]> actions = state.listOfActions();
    		System.out.println("stack size " + actions.size());
        	Random random = new Random();
        	int[] move = actions.get(random.nextInt(actions.size() - 1));
        	move = state.act(move, height, myTurn).lastMove;
        	System.out.println("agent " + move);
        	return "(move " + move[0] + " " + move[1] + " " + move[2] + " " + move[3] + ")";
    	}
    	return "NOOP";
    	
	}
    public void print()
    {
    	
    }

	// is called when the game is over or the match is aborted
	@Override
	public void cleanup() {
		// TODO: cleanup so that the agent is ready for the next match
	}

}
