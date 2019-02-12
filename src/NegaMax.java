
public class NegaMax {
	
	private static final int WIN = 100;
	private static final int LOSS = -100;
	
	// Call: minmaxValue = MiniMax( initialState )
	int MiniMax(State state) {
		if(state.score == WIN || state.score == LOSS) {
			return state.score;
		}
		
		int bestValue = LOSS;
	
		for(int i:state.listOfActions()) {
			int value = -MiniMax(state.act(i));
			if(bestValue < value) {
				bestValue = value;
			}
		}
	 	return bestValue;
	}
	
	
	int MiniMaxDepthLimited(State state, int depth) {
		if(state.score == WIN || state.score == LOSS || depth <= 0) {
			return evaluate(state);
		}
		
		int bestValue = LOSS;
	
		for(int i:state.listOfActions()) {
			int value = -MiniMaxDepthLimited(state.act(i), --depth);
			if(bestValue < value) {
				bestValue = value;
			}
		}
	 	return bestValue;
	}
	
	/* Pseudo code:
	// Call: minmaxValue = MiniMax( initialState )
	Value MiniMax ( State s ) {
	 	if ( s is terminal )
	 		return value of s from the perspective of the side to move
	 	bestValue = LOSS;
	 	for ( for all successors s’ of s ) {
	 		value = - MiniMax( s’ ); (note: negate value)
	 		bestValue = max( value, bestValue );
	 	}
	 	return bestValue;
	}
	// Value always relative to the side to move!
	*/
	
	
	int AlphaBeta (State state, int depth, int alpha, int beta) {
		if (state.score == WIN || state.score == LOSS || depth <= 0) {
			return evaluate(state);
		}
 		int bestValue = LOSS;
 		for(int i:state.listOfActions()) {
 			int value = -AlphaBeta(state.act(i), --depth, -beta, -alpha); //(Note: switch and negate bounds)
			if(bestValue < value) {
				bestValue = value;
			}
 		if(bestValue > alpha) {
 			alpha = bestValue; //(adjust the lower bound)
 			if (alpha >= beta) break; //(beta cutoff)
 			}
		}
 		return bestValue;
	}
	
	/* Pseudo code
	// Call: value = AlphaBeta( MaxDepth, s, -INF, INF )
	Value AlphaBeta (int depth, State s, Value alpha,Value beta) {
		if ( s is terminal or depth <= 0 )
 			return evaluate( s );
 		bestValue = -INF;
 		for ( for all successors s’ of s ) {
 			value = - AlphaBeta( depth–1, s’, - beta, - alpha ); (Note: switch and negate bounds)
 		bestValue = max( value, bestValue);
 		if ( bestValue > alpha ) {
 			alpha = bestValue; (adjust the lower bound)
 			if ( alpha >= beta ) break; (beta cutoff)
 			}
 			}
 		return bestValue;
	} 
	*/
	
	// Call: goalNode = DFS( initialNode )
	State DFS(State s, int depth) {
 		if(s.score == WIN || s.score == LOSS || depth <= 0) return n;
 		State goalState = null;
 		for(int i:s.listOfActions()) {
			goalState = DFS(s.act(i), --depth);
			if (goalState != null) return goalState;
		}
 		return null;
	}
	
	
	/* Pseudo code
	// Call: goalNode = DFS( initialNode )
	Node DFS ( Node n ) {
 		if ( n.state is goal state ) return n;
 		for ( for all successors s’ of n.state ) {
			goalnode = DFS( new Node(n, s') );
			if (goalnode != null) return goalnode;
		}
 		return null;
	}
	*/
	
	/* Pseudo code 
	function IDDFS(root)
   		for depth from 0 to ∞
       		found, remaining ← DLS(root, depth)
       		if found ≠ null
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
       		any_remaining ← false
       		foreach child of node
           		found, remaining ← DLS(child, depth−1)
           		if found ≠ null
               		return (found, true)   
           		if remaining
               		any_remaining ← true    (At least one node found at depth, let IDDFS deepen)
       		return (null, any_remaining)
	*/
	
	
	// TODO: EVALUATE 
	int evaluate(State s) {
		return 0;
	}
	
}