//import java.util.List;
//import java.util.Random;

public class BreakthroughAgent implements Agent
{
	private int playClock; // this is how much time (in seconds) we have before nextAction needs to return a move
	private boolean myTurn; // whether it is this agent's turn or not
	private State state;
	private State backupState;
	
	/*
		init(String role, int playclock) is called once before you have to select the first action. Use it to initialize the agent. role is either "white" or "black" and playclock is the number of seconds after which nextAction must return.
	*/
    public void init(String role, int width, int height, int playClock) {
		this.playClock = playClock;
		myTurn = !role.equals("white");
		this.state = new State(width, height);
		this.backupState = new State(width, height);
		this.state.side = 1;
		this.backupState.side = 1;
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
            	backupState.act(lastMove);
            	state.print();

    		}
    		NegaMax nega = new NegaMax(playClock);
    		int[] move = nega.iterativeDepthSearch(state, backupState);
    		if (move == null)
    		{
    			return "NOOP";
    		}
        	state.act(move);
        	backupState.act(move);
    		state.print();
        	return "(move " + move[0] + " " + move[1] + " " + move[2] + " " + move[3] + ")";
    	}
    	return "NOOP";
    	
	}

	// is called when the game is over or the match is aborted
	@Override
	public void cleanup() {

	}

}
