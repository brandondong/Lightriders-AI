package lightriders.ai;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

class TreeHeightSearch<T> {

	private final int depth;

	private final Function<T, List<T>> childNodes;

	private final Function<T, Integer> evaluation;

	/**
	 * @param depth
	 *            The depth to search to or 0 for no limit
	 * @param childNodes
	 *            Generates an array of possible child nodes for a parent node
	 * @param evaluation
	 *            Estimates the height of a given node
	 */
	public TreeHeightSearch(int depth, Function<T, List<T>> childNodes, Function<T, Integer> evaluation) {
		this.depth = depth;
		this.childNodes = childNodes;
		this.evaluation = evaluation;
	}

	/**
	 * Finds the highest subtree index for the given node.
	 * 
	 * @param node
	 *            The current node which is guaranteed to have children
	 * @return The index of the highest subtree
	 */
	public int highestSubtree(T node) {
		List<T> children = childNodes.apply(node);
		int maxIndex = -1;
		int maxHeight = -1;
		for (int i = 0; i < children.size(); i++) {
			int subtreeHeight = height(depth - 1, children.get(i));
			if (subtreeHeight > maxHeight) {
				maxHeight = subtreeHeight;
				maxIndex = i;
			}
		}
		return maxIndex;
	}

	private int height(int depth, T node) {
		if (depth == 0) {
			return evaluation.apply(node);
		}
		List<T> children = childNodes.apply(node);
		return children.stream().map(childNode -> 1 + height(depth - 1, childNode)).max(Comparator.naturalOrder())
				.orElse(0);
	}

}
