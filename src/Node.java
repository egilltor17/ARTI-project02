
public class Node{
		public Node parent;
		public Node[] children;
		public State state;
		public Node(State state)
		{
			this.state = state;
			this.parent = null;
		}
		public Node(State state, Node parent)
		{
			this.parent = parent;
			this.state = state;
		}
	}