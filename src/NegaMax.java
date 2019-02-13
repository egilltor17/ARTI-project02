
public class NegaMax {
	
	private static final int WIN = 100;
	private static final int LOSS = -100;
	private int height;
	private long timeLimit;
	
	public NegaMax(int height, int playClock)
	{
		this.height = height;
		// We underestimate the deadline to give us time to return a move
		this.timeLimit = System.currentTimeMillis() + (950 * playClock); 
	}
	
	// Depth Search template, return null when no moves are available
	int[] depthSearch(State s, int depth) {
		State bestState = null;
		try {
			for(int i = 1; i <= depth; i++) {
				bestState = MiniMaxDepthLimitedRoot(s, i);
				//bestState = AlphaBetaDepthLimitedRoot(s, i);
				//bestState = IterativeDepeningRoot(s, i);
			}
		} catch(Exception e) {}
		if(bestState == null) {
			return null;
		} else {
			return new int[] {bestState.lastMove};
		}
	}
	
	
	//===============================================================================================================
	// MiniMax ------------------------------------------------------------------------------------------------------
	//===============================================================================================================
	
	// Pseudo code taken from slides
	
	// make the root state return the move type!
	
	
	// Call: minmaxValue = MiniMaxDepthLimitedRoot( initialState, depth )	
	public State MiniMaxDepthLimitedRoot(State state, int depth) throws Exception {
		if(state.terminal || depth <= 0) { 
			return state;
		}
			int bestValue = LOSS;
			State successor = null;
			for(int i:state.listOfActions()) {
				if(System.currentTimeMillis() >= timeLimit) { throw new Exception(); } // We are out of time
				State suc = state.act(i, height);
				int value = -MiniMaxDepthLimited(state.act(i, height), depth -1);
				if(bestValue < value) {
					bestValue = value;
					successor = suc;				
				}
			}
	 	return successor;
	}
	
	private int MiniMaxDepthLimited(State state, int depth) throws Exception {
		if(state.terminal || depth <= 0) { 
			return evaluate(state);
		}
		int bestValue = LOSS;
	
		for(int i:state.listOfActions()) {
			if(System.currentTimeMillis() >= timeLimit) { throw new Exception(); } // We are out of time
			
			int value = -MiniMaxDepthLimited(state.act(i, height), depth -1);
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
	
	public State AlphaBetaRoot (State state, int depth, int alpha, int beta) throws Exception {
		if (state.terminal || depth <= 0) { 
			return state; //evaluate(state);
		}
 		int bestValue = LOSS;
 		State successor = null;
 		for(int i:state.listOfActions()) {
 			if(System.currentTimeMillis() >= timeLimit) { throw new Exception(); } // We are out of time
			
 			State suc = state.act(i, height);
 			int value = -AlphaBeta(suc, depth - 1, -beta, -alpha); //(Note: switch and negate bounds)
			
 			if(bestValue < value) {
				bestValue = value;
				successor = suc;
			}
 			
 			if(bestValue > alpha) {
 				alpha = bestValue; 			//(adjust the lower bound)
 				if (alpha >= beta) break; 	//(beta cutoff)
 			}
		}
 		return successor;
	}
 		
	private int AlphaBeta (State state, int depth, int alpha, int beta) throws Exception {
		if (state.terminal || depth <= 0) {
			return evaluate(state);
		}
 		int bestValue = LOSS;
 		for(int i:state.listOfActions()) {
 			if(System.currentTimeMillis() >= timeLimit) {throw new Exception();} 	// We are out of time
 			
 			int value = -AlphaBeta(state.act(i, height), depth - 1, -beta, -alpha); //(Note: switch and negate bounds)
			
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
	
	//===============================================================================================================
	// IterativeDepening --------------------------------------------------------------------------------------------
	//===============================================================================================================
	
	
	// Call: goalNode = DFS( initialNode )
	State DFS(State s, int depth) {
 		if(s.score == WIN || s.score == LOSS || depth <= 0) return s;
 		State goalState = null;
 		for(int i:s.listOfActions()) {
			goalState = DFS(s.act(i, height), depth - 1);
			if (goalState != null) return goalState;
		}
 		return null;
	}
	
	
	/* Pseudo code
	// Call: goalNode = DFS( initialNode )
	Node DFS ( Node n ) {
 		if ( n.state is goal state ) return n;
 		for ( for all successors sâ€™ of n.state ) {
			goalnode = DFS( new Node(n, s') );
			if (goalnode != null) return goalnode;
		}
 		return null;
	}
	*/
	
	/* Pseudo code 
	function IDDFS(root)
   		for depth from 0 to âˆž
       		found, remaining â†� DLS(root, depth)
       		if found â‰  null
           		return found
       		else if not remaining
           		return null

	function DLS(node, depth)
   		if depth = 0
       		if node is a goal
           		return (node, true)
       		else
           		return (null, true)    (Not found, but may have children)

   		else if depth > 0
       		any_remaining â†� false
       		foreach child of node
           		found, remaining â†� DLS(child, depthâˆ’1)
           		if found â‰  null
               		return (found, true)   
           		if remaining
               		any_remaining â†� true    (At least one node found at depth, let IDDFS deepen)
       		return (null, any_remaining)
	*/
	
	
	// TODO: EVALUATE 
	int evaluate(State s) {
		return 0;
	}
	
}
