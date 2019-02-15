
public class NegaMax {
	
	private static final int WIN = 100;
	private static final int LOSS = -100;

	private int player;
	private long timeLimit;
	
	public NegaMax(int playClock, int player)
	{
		// We underestimate the deadline to give us time to return a move
		this.timeLimit = System.currentTimeMillis() + (950 * playClock);
		this.player = player;		
	}
	
	// Depth Search template, return null when no moves are available
	int[] iterativeDepthSearch(State state, int maxDepth) {
		int[] bestMove = null;
		State oldState = new State(state);
		try {
			for(int depth = 1; depth <= maxDepth; depth += 2) {
				//bestMove = MiniMaxDepthLimitedRoot(state, depth, player);
				bestMove = AlphaBetaRoot(state, depth, player, LOSS, WIN);
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
	
	public int[] MiniMaxDepthLimitedRoot(State state, int depth, int side) throws Exception {

		int bestValue = LOSS;
		int[] bestMove = null; 
	
		for(int[] action:state.listOfActions(side)) {
			state.act(action, true, side);	// do move
			state.switchSides();
			int value = -MiniMaxDepthLimited(state, depth -1, -side);
			state.switchSides();
			state.unact(action, true);		// undo move

			if(bestValue < value) {
				bestValue = value;
				bestMove = action;
			}
		}
		return bestMove;
	}
	
	private int MiniMaxDepthLimited(State state, int depth, int side) throws Exception {
		if(state.terminal || depth <= 0) { 
			return state.evaluateState(side);
		}
		
		if(System.currentTimeMillis() >= timeLimit) { throw new Exception(); } // We are out of time
		
		int bestValue = LOSS;
		
		for(int[] action:state.listOfActions(side)) {
			state.act(action, true, side);	// do move
			state.switchSides(); 
			int value = -MiniMaxDepthLimited(state, depth -1, -side);
			state.terminal = false;
			state.switchSides();
			state.unact(action, true);		// undo move
			
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
	
	public int[] AlphaBetaRoot(State state, int depth, int side, int alpha, int beta) throws Exception {

		int bestValue = LOSS;
		int[] bestMove = null; 
	
		for(int[] action:state.listOfActions(side)) {
			state.act(action, true, side);	// do move
			state.switchSides();
			int value = -AlphaBeta(state, depth-1, -side, -beta, -alpha);
			state.switchSides();
			state.unact(action, true);		// undo move

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
 		
	private int AlphaBeta(State state, int depth, int side, int alpha, int beta) throws Exception {
		if (state.terminal || depth <= 0) {
			return state.evaluateState(player);
		}
		if(System.currentTimeMillis() >= timeLimit) { throw new Exception(); } // We are out of time
 		
		int bestValue = LOSS;
 		
 		for(int[] action:state.listOfActions(side)) {
 			state.act(action, true, side);	// do move
			state.switchSides(); 
 			int value = -AlphaBeta(state, depth-1, -side, -beta, -alpha); //(Note: switch and negate bounds)
 			state.terminal = false;
			state.switchSides();
			state.unact(action, true);		// undo move
			
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
