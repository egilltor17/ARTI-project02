
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
	int[] iterativeDepthSearch(State state, int maxDepth) {
		int[] bestMove = null;
		State oldState = new State(state);
		try {

			for(int depth = 2; depth <= maxDepth; depth += 2) {
				bestMove = MiniMaxDepthLimitedRoot(state, depth);
				//bestMove = AlphaBetaRoot(state, depth, player, LOSS, WIN);
			}
		} catch(Exception e) {
			state = oldState;
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
		return bestMove;
	}
	
	private int MiniMaxDepthLimited(State state, int depth) throws Exception {
		if(state.terminal || depth <= 0) { 
			return state.evaluateState();
		}
		
		if(System.currentTimeMillis() >= timeLimit) { throw new Exception(); } // We are out of time
		
		int bestValue = LOSS;
		
		for(int[] action:state.listOfActions()) {
			state.act(action);	// do move
			int value = -MiniMaxDepthLimited(state, depth -1);
			state.terminal = false;
			state.unact(action);		// undo move
			
			if(bestValue < value) { 
				bestValue = value;
			}
		}
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

			if(bestValue < value) {
				bestValue = value;
				bestMove = action;
			}
			
			if(bestValue > alpha) {
 				alpha = bestValue; 			//(adjust the lower bound)
 				if (alpha >= beta) break; 	//(beta cutoff)
 			}
		}
		return bestMove;
	}
 		
	private int AlphaBeta(State state, int depth, int alpha, int beta) throws Exception {
		if (state.terminal || depth <= 0) {
			return state.evaluateState();
		}
		if(System.currentTimeMillis() >= timeLimit) { throw new Exception(); } // We are out of time
 		
		int bestValue = LOSS;
 		
 		for(int[] action:state.listOfActions()) {
 			state.act(action);	// do move
 			int value = -AlphaBeta(state, depth-1, -beta, -alpha); //(Note: switch and negate bounds)
 			state.terminal = false;
			state.unact(action);		// undo move
			
 			if(bestValue < value) {
				bestValue = value;
			}
			
			if(bestValue > alpha) {
				alpha = bestValue; 			//(adjust the lower bound)
				if (alpha >= beta) break; 	//(beta cutoff)
 			}
		}
 		return bestValue;
	}	
}
