import java.util.EmptyStackException;

public class NegaMax {
	
	private static final int WIN = 100;
	private static final int LOSS = -100;

	private long timeLimit;
	
	public NegaMax(int playClock)
	{
		// We underestimate the deadline to give us time to return a move
		this.timeLimit = System.currentTimeMillis() + (900 * playClock);
	}
	
	// Depth Search template, return null when no moves are available
	int[] iterativeDepthSearch(State state, int maxDepth, State backupState) {
		int[] bestMove = null;		
		try {
			int depth = 2;
			while(true) {
				//bestMove = MiniMaxDepthLimitedRoot(state, depth);
				/*for(int i = 0; i < state.agent.length; i++)
				{
					System.out.print( "A" + state.agent[i].x + state.agent[i].y + " E" + state.enemy[i].x + state.enemy[i].y + "  ");
				}
				System.out.println();*/
				bestMove = AlphaBetaRoot(state, depth, LOSS, WIN);
				System.out.println("Depth: " + depth + " bestMove: " + bestMove[0] + " " + bestMove[1] + " " + bestMove[2] + " " + bestMove[3] + " state " + state.score);
				if(state.score == -100)
				{
					return bestMove;
				}
				depth += 2;
			}
		} catch(EmptyStackException e) {
			System.out.println("\n\n\n" + e);

			state.agent = new Point[backupState.agent.length];
			state.enemy = new Point[backupState.enemy.length];
			for(int i = 0; i < state.agent.length; i++)
			{
				if(backupState.agent[i] != null)
				{
					state.agent[i] = new Point(backupState.agent[i].x, backupState.agent[i].y);
				}
				if(backupState.enemy[i] != null)
				{
					state.enemy[i] = new Point(backupState.enemy[i].x, backupState.enemy[i].y);
				}
			}
			state.side = backupState.side;
			System.out.println("\n state  side: " + state.side + " terminal " + state.terminal + " agent: " + state.agent + state.enemy);
			state.print();	
			System.out.println("\n OLDstate  side: " + backupState.side + " terminal " + backupState.terminal + " agent: " + backupState.agent + backupState.enemy);
			backupState.print();
			System.out.println("oooooooooooooooooooooooooooooooldstate" + state);
			System.out.println("\n\n\nstate restored ");
		} catch(Exception e) {
			System.out.println("\n\n\n" + e);
			System.out.println("Some other exception");
		}
		return bestMove;
	}
	
	
	//===============================================================================================================
	// MiniMax ------------------------------------------------------------------------------------------------------
	//===============================================================================================================
	
	// Pseudo code taken from slides	
	
	public int[] MiniMaxDepthLimitedRoot(State state, int depth) throws EmptyStackException {

		int bestValue = LOSS;
		int[] bestMove = null; 
		for(int[] action:state.listOfActions()) {
			//State oldState = new State(state);
			state.act(action);	// do move
			int value = -MiniMaxDepthLimited(state, depth -1);
			//System.out.println("isWhite " + (state.side == -1) + " terminal " + state.terminal + " action "  + action[0] + " " + action[1] + " " + action[2] + " " + action[3]);
			state.unact(action);		// undo move
			//state = oldState;
			if(bestValue < value) {
				bestValue = value;
				bestMove = action;
			}
		}
		System.out.println("best value " + bestValue);
		return bestMove;
	}
	
	private int MiniMaxDepthLimited(State state, int depth) throws EmptyStackException {
		if(state.terminal || depth <= 0) { 
			//System.out.println("depth" + depth + " terminal " + state.terminal);
			return state.evaluateState();
		}
		if(System.currentTimeMillis() >= timeLimit) {
			System.out.println("\n\n\n out of time " + (System.currentTimeMillis() - timeLimit));

			throw new EmptyStackException();
		} // We are out of time
		
		int bestValue = LOSS;
		
		for(int[] action:state.listOfActions()) {
			//State oldState = new State(state);
			state.act(action);	// do move
			int value = -MiniMaxDepthLimited(state, depth -1);
			/*for(int i = 0; i < depth; i++)
			{
				System.out.print("| ");
			}
			*///System.out.println("isWhite " + (state.side == -1) + " value: " + value + " terminal " + state.terminal + " action "  + action[0] + " " + action[1] + " " + action[2] + " " + action[3]);
			state.unact(action);		// undo move
			//state = oldState;
			if(bestValue < value) { 
				bestValue = value;
			}
		}
		if(state.terminal)
		{
			return state.evaluateState();
		}
		//System.out.println();
		return bestValue;
	}

	//===============================================================================================================
	// AlphBeta -----------------------------------------------------------------------------------------------------
	//===============================================================================================================
	
	// Pseudo code taken from slides
	
	public int[] AlphaBetaRoot(State state, int depth, int alpha, int beta) throws EmptyStackException {
		int bestValue = LOSS;
		int[] bestMove = null; 
		
		for(int[] action:state.listOfActions()) {
			state.act(action);	// do move
			int value = -AlphaBeta(state, depth-1, -beta, -alpha);
			state.unact(action);		// undo move

			if(bestValue < value) {
				bestValue = value;
				bestMove = action;
			}
			
			if(bestValue > alpha) {
 				alpha = bestValue; 			//(adjust the lower bound)
 				if (alpha >= beta) break; 	//(beta cutoff)
 			}
		}
		System.out.println("best value " + bestValue);
		return bestMove;
	}
 		
	private int AlphaBeta(State state, int depth, int alpha, int beta) throws EmptyStackException {
		if (state.terminal || depth <= 0) {
			return state.evaluateState();
		}
		if(System.currentTimeMillis() >= timeLimit) { throw new EmptyStackException(); } // We are out of time
 		
		int bestValue = LOSS;
 		
 		for(int[] action:state.listOfActions()) {
 			state.act(action);	// do move
 			int value = -AlphaBeta(state, depth-1, -beta, -alpha); //(Note: switch and negate bounds)
 			state.unact(action);		// undo move
			
 			if(bestValue < value) {
				bestValue = value;
			}
			
			if(bestValue > alpha) {
				alpha = bestValue; 			//(adjust the lower bound)
				if (alpha >= beta) break; 	//(beta cutoff)
 			}
		}
 		if(state.terminal)
		{
			return state.evaluateState();
		}
 		return bestValue;
	}	
}
