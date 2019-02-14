
public class NegaMax {
	
	private static final int WIN = 100;
	private static final int LOSS = -100;

	private int player;
	private int height;
	private long timeLimit;
	
	public NegaMax(int height, int playClock, int player)
	{
		this.height = height;
		// We underestimate the deadline to give us time to return a move
		this.timeLimit = System.currentTimeMillis() + (950 * playClock);
		this.player = player;		
	}
	
	// Depth Search template, return null when no moves are available
	int[] depthSearch(State s, int depth) {
		int[] bestMove = null;
		try {
			for(int i = 1; i <= depth; i++) {
				bestMove = MiniMaxDepthLimitedRoot(s, i, player);
				//bestMove = AlphaBetaRoot(s, i, LOSS, WIN);
				//bestMove = IterativeDepeningRoot(s, i);
			}
		} catch(Exception e) {}
		return bestMove;
	}
	
	
	//===============================================================================================================
	// MiniMax ------------------------------------------------------------------------------------------------------
	//===============================================================================================================
	
	// Pseudo code taken from slides
	
	// make the root state return the move type!
	
	
	// Call: minmaxValue = MiniMaxDepthLimitedRoot( initialState, depth )	
	public int[] MiniMaxDepthLimitedRoot(State state, int depth, int side) throws Exception {

		int bestValue = LOSS;
		int[] bestMove = null; 
	
		for(int[] action:state.listOfActions(side)) {
			state.act(action, true, side);		// do move
			state.switchSides();
			int value = -MiniMaxDepthLimited(state, depth -1, -side);
			state.switchSides();
			state.unact(action, true);	// undo move

			if(bestValue < value) {
				bestValue = value;
				bestMove = action;
			}
		}
		return bestMove;
	}
	
	private int MiniMaxDepthLimited(State state, int depth, int side) throws Exception {
		if(System.currentTimeMillis() >= timeLimit) { throw new Exception(); } // We are out of time
		
		if(state.terminal || depth <= 0) { 
			return state.evaluateState(side);
		}
		int bestValue = LOSS;
		
		for(int[] action:state.listOfActions(side)) {
			// do move
			state.act(action, true, side);
			state.switchSides(); 
			int value = -MiniMaxDepthLimited(state, depth -1, -side);
			state.terminal = false;
			state.switchSides();
			state.unact(action, true);
			// undo move
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
 		int bestValue = LOSS;
 		State successor = null;
 		
 		for(int[] action:state.listOfActions(player)) {	
 			// do move
 			//state.act(action, true);
 			int value = -AlphaBeta(state, depth - 1, -beta, -alpha); //(Note: switch and negate bounds)
			// undo move
 			if(bestValue < value) {
				bestValue = value;
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
			return state.evaluateState(player);
		}
		if(System.currentTimeMillis() >= timeLimit) { throw new Exception(); } // We are out of time
 		
		int bestValue = LOSS;
 		
 		for(int[] action:state.listOfActions(player)) {
 			if(System.currentTimeMillis() >= timeLimit) {throw new Exception();} 	// We are out of time
 			
 			//state.act(action, true, side);
 			int value = -AlphaBeta(state, depth - 1, -beta, -alpha); //(Note: switch and negate bounds)
			
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
	
	// iterate depth by 2!
	
	/* Pseudo code fom slides
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
	
	/* Pseudo code from wikipedia
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
