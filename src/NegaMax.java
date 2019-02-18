public class NegaMax
{
	
	private static final int WIN = 100;
	private static final int LOSS = -100;

	private long timeLimit;
	
	public NegaMax(int playClock)
	{
		// We underestimate the deadline to give us time to return a move
		this.timeLimit = System.currentTimeMillis() + (1000 * playClock - 50);
	}
	
	// Depth Search template, return null when no moves are available
	int[] iterativeDepthSearch(State state, int maxDepth, State backupState) {
		int[] bestMove = null;		
		try {
			int depth = 2;
			while(true) {
				//bestMove = MiniMaxDepthLimitedRoot(state, depth);
				bestMove = AlphaBetaRoot(state, depth, LOSS, WIN);
				System.out.println("Depth: " + depth + " bestMove: " + bestMove[0] + " " + bestMove[1] + " " + bestMove[2] + " " + bestMove[3] + " score " + state.score + " side " + state.side);
				if(state.score == -100 * state.side || !state.reachedDepth) {
					return bestMove;
				}
				depth += 2;
			}
		} catch(Exception e) {
			System.out.println("\n\n\n" + e);

			state.agent = new Point[backupState.agent.length];
			state.enemy = new Point[backupState.enemy.length];
			for(int i = 0; i < state.agent.length; i++)
			{
				if(backupState.agent[i] != null) {
					state.agent[i] = new Point(backupState.agent[i].x, backupState.agent[i].y);
				}
				if(backupState.enemy[i] != null) {
					state.enemy[i] = new Point(backupState.enemy[i].x, backupState.enemy[i].y);
				}
			}
			state.side = backupState.side;
		}
		return bestMove;
	}
	
	
	//===============================================================================================================
	// MiniMax ------------------------------------------------------------------------------------------------------
	//===============================================================================================================
	
	// Pseudo code taken from slides	
	
	public int[] MiniMaxDepthLimitedRoot(State state, int depth) throws Exception {

		int bestValue = LOSS;
		int[] bestMove = null; 
		for(int[] action:state.listOfActions()) {
			state.act(action);	// do move
			int value = -MiniMaxDepthLimited(state, depth -1);
			state.unact(action);		// undo move
			if(bestValue < value) {
				bestValue = value;
				bestMove = action;
			}
		}
		System.out.println("best value " + bestValue + " score " + state.score);
		return bestMove;
	}
	
	private int MiniMaxDepthLimited(State state, int depth) throws Exception {
		if(state.terminal || depth <= 0) { 
			state.reachedDepth = (depth == 0);
			return state.evaluateState();
		}
		if(System.currentTimeMillis() >= timeLimit) {
			throw new Exception(); // We are out of time
		} 
		int bestValue = LOSS;
		
		for(int[] action:state.listOfActions()) {
			state.act(action);	// do move
			int value = -MiniMaxDepthLimited(state, depth -1);
			state.unact(action);		// undo move
			if(bestValue < value) { 
				bestValue = value;
			}
		}
		if(state.terminal) {
			state.reachedDepth = (depth == 0);
			return state.evaluateState();
		}
		//System.out.println();
		return bestValue;
	}

	//===============================================================================================================
	// AlphBeta -----------------------------------------------------------------------------------------------------
	//===============================================================================================================
	
	// Pseudo code taken from slides
	
	public int[] AlphaBetaRoot(State state, int depth, int alpha, int beta) throws Exception {
		int bestValue = LOSS;
		int[] bestMove = null; 
		
		for(int[] action:state.listOfActions()) {
			state.act(action);	// do move
			int value = -AlphaBeta(state, depth-1, -beta, -alpha);
			state.unact(action);		// undo move

			if(bestValue < value || bestMove == null) {
				bestValue = value;
				bestMove = action;
			}
			if(bestValue > alpha) {
 				alpha = bestValue; 			//(adjust the lower bound)
 				if (alpha >= beta) {
					break; 	//(beta cutoff)
				}
 			}
		}
		System.out.println("best value " + bestValue);
		return bestMove;
	}
 		
	private int AlphaBeta(State state, int depth, int alpha, int beta) throws Exception {
		if (state.terminal || depth <= 0) {
			state.reachedDepth = (depth == 0);
			return state.evaluateState();
		}
		if(System.currentTimeMillis() >= timeLimit) {
			throw new Exception(); // We are out of time
		} 
 		
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
				if (alpha >= beta) {
					break; 	//(beta cutoff)
				}
 			}
		}
 		if(state.terminal) {
			state.reachedDepth = (depth == 0);
			return state.evaluateState();
		}
 		return bestValue;
	}	
}
