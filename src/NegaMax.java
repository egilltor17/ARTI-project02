import java.util.List;

public class NegaMax {
	
	private static final int WIN = 100;
	private static final int LOSS = -100;
	private int maxDepth;
	private long timeLimit;
	
	public NegaMax(int playClock)
	{
		// We underestimate the deadline to give us time to return a move
		this.timeLimit = System.currentTimeMillis() + (1000 * playClock - 70);
	}
	
	// Depth Search template, return null when no moves are available
	int[] iterativeDepthSearch(State state, State backupState)
	{
		int[] bestMove = null;		
		try {
			int depth = 2;
			while(true) {
				maxDepth = depth;
				
				//UNCOMMENT DO TEST WITHOUT ALPHA BETA PRUNING
				//bestMove = MiniMaxDepthLimitedRoot(state, depth);
				
				bestMove = AlphaBetaRoot(state, depth, LOSS, WIN);
				System.out.println("Depth: " + depth + " bestMove: " + bestMove[0] + " " + bestMove[1] + " " + bestMove[2] + " " + bestMove[3] + " side " + state.side);
				if(!state.reachedDepth) {
					return bestMove;
				}
				state.reachedDepth = false;
				depth += 2;
			}
		} catch(Exception e) {
			System.out.println("\n\n\n" + e);
			//Make new dynamic variables so the two states don't share them
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
	
	public int[] MiniMaxDepthLimitedRoot(State state, int depth) throws Exception
	{

		int bestValue = LOSS;
		int[] bestMove = null; 
		for(int[] action:state.listOfActions()) {
			state.act(action);	// do move
			int value = -MiniMaxDepthLimited(state, depth -1);
			state.unact(action);		// undo move
			//if best move is null a move hasn't been selected so must still select a move
			if(bestValue < value || bestMove == null) {
				bestValue = value;
				bestMove = action;
			}
		}
		System.out.println("best value " + bestValue);
		return bestMove;
	}
	
	private int MiniMaxDepthLimited(State state, int depth) throws Exception
	{
		if(state.terminal || depth <= 0) { 
			if(depth == 0) {
 				state.reachedDepth = true;
 			}
			return state.evaluateState(maxDepth - depth);
			//return state.evaluateStateForBig(maxDepth - depth);
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
			if(depth == 0) {
 				state.reachedDepth = true;
 			}
			return state.evaluateState(maxDepth - depth);
			//return state.evaluateStateForBig(maxDepth - depth);
		}
		return bestValue;
	}

	//===============================================================================================================
	// AlphBeta -----------------------------------------------------------------------------------------------------
	//===============================================================================================================
	
	// Pseudo code taken from slides
	
	public int[] AlphaBetaRoot(State state, int depth, int alpha, int beta) throws Exception
	{
		int bestValue = LOSS;
		int[] bestMove = null; 
		
		List<int[]> actions = state.listOfActions();
		actions.sort((a, b) -> (((b[3] - a[3]) * state.side) - Math.abs(a[0] - a[2])));
		for(int[] action:actions) {
			state.act(action);			// do move
			int value = -AlphaBeta(state, depth-1, -beta, -alpha);
			state.unact(action);		// undo move
			//if bestMove is null a move hasn't been selected so must still select a move
			if(bestValue < value || bestMove == null) {
				bestValue = value;
				bestMove = action;
			}
			if(bestValue > alpha) {
 				alpha = bestValue; 			//adjust the lower bound
 				if (alpha >= beta) {
					break; 					//beta cutoff
				}
 			}
		}
		System.out.println("best value " + bestValue);
		return bestMove;
	}
 		
	private int AlphaBeta(State state, int depth, int alpha, int beta) throws Exception
	{
		// the state has a game over or the depth has been reached
		if (state.terminal || depth <= 0) {
			if(depth == 0) {
 				state.reachedDepth = true;
 			}			
			return state.evaluateState(maxDepth - depth);
			//return state.evaluateStateForBig(maxDepth - depth);
		}
		if(System.currentTimeMillis() >= timeLimit) {
			throw new Exception(); // We are out of time
		} 
 		
		int bestValue = LOSS;
		List<int[]> actions = state.listOfActions();
		// Lambda function ranks moves based on the farthest pawn with a bonus for a kill
		actions.sort((a, b) -> (((b[3] - a[3]) * state.side) - Math.abs(a[0] - a[2])));	
 		for(int[] action:actions) {
 			state.act(action);			// do move
 			int value = -AlphaBeta(state, depth-1, -beta, -alpha); //Note: switch and negate bounds
 			state.unact(action);		// undo move
			
 			if(bestValue < value) {
				bestValue = value;
			}
			
			if(bestValue > alpha) {
				alpha = bestValue; 			//adjust the lower bound
				if (alpha >= beta) {
					break; 					//beta cutoff
				}
 			}
		}
 		// if the list is empty we have a draw, evaluate
 		if(state.terminal) {
 			if(depth == 0) {
 				state.reachedDepth = true;
 			}
			return state.evaluateState(maxDepth - depth);
			//return state.evaluateStateForBig(maxDepth - depth);
		}
 		return bestValue;
	}
}
