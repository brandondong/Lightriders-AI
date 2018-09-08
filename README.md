# Lightriders-AI
[Match vs rank 1](https://playground.riddles.io/competitions/light-riders/matches/6a5bcf49-6e65-4c1b-8b43-7fcb5527a9a7)

## High level overview

Similar to approaches for other turn based games like chess, the bot relies on searching the game space. However, this is not a perfect information game (player moves are made simultaneously) so a typical algorithm like alpha-beta search cannot be used to model the player decisions. Instead a given board is evaluated to a value as follows:
* If not a game ending position for either player, create a matrix of boards corresponding to the next possible game states resulting from pairs of moves chosen by the two players.
* Evaluate each cell recursively to receive a zero sum payoff matrix.
* Calculate the Nash equilibrium value of the table. Return this value for our given board.

If our given board was the root of the round, we would be instead be interested in the move probabilities to generate the equilibrium. Both the Nash equilibrium value and strategy can be calculated by reducing to a linear programming problem and solving that instance. It is fairly easy to see that using this strategy against itself results in a Nash equilibrium and therefore it is optimal for this zero sum game.

Like chess, the search space is intractable to fully traverse in any reasonable amount of time. Throwing away the guarantee of optimality, a depth cutoff is employed and the leaves are assigned values based on an evaluation function. Choosing a constant depth cutoff is a bad idea as search times can vary wildly depending on the configuration of the current board which affects branching factor and pruning. Instead, iterative deepening is employed where the search is repeated with a continually increasing depth cutoff until a time limit is reached. Although some nodes are searched multiple times, the large average branching factor means that earlier depth searches take a relatively negligable amount of time.

The evaluation function tries to optimize for the amount of area the bot can cut off the opponent from reaching. Specifically it uses the metric of the number of empty cells reachable by a shorter path from the bot than the opponent subtracted by the reverse condition. This can be efficiently computed by performing a simultaneous breadth-first search from the bot and opponent positions and counting the board cells depending on which search reached them first.

## Domain specific optimizations

Pruning techniques such as alpha-beta pruning do not apply to this new search algorithm with matrices. However, due to the nature of Nash equilibria, we may find a pure strategy equilibrium before evaluating all cells in the matrix. This occurs when a cell is the greatest value in its column and least in its row. The search does not need to be applied recursively for the remaining empty cells which will result in entire branches of the search space being pruned. In general, we should aim to evaluate cells in some smart order to maximize the chances for pure equilibira pruning. The current implementation achieves this by greedily expanding equilibrium candiates closest to confirmation. From match experience, pruning happens surprisingly often for this game.

The game can reach a state where the two players become separated and unable to affect the paths of the other. In this case, the opponent in the matrix of move possibilities can be ignored and the game reduces to finding the longest path in the self contained grid graph. Unfortunately, no polynomial time algorithm is known for this problem. We use a search algorithm again, this time finding the branch that maximizes the search tree height. Iterative deepening is once again used and an estimator function estimates the remaining path length for the nodes at the cutoff. The separated condition is not a common scenario so even though it is relatively quick to check, it is not done on every single node in our general search algorithm. Instead, it just done once at the start of each round to determine which search algorithm the best move will be computed with.

## Future improvements/considerations

* Improving the evaluation function would likely have the largest impact in bot performance. For the most part, the current function performs astonishingly well given its simplicity. The difficulty lies in identifying areas where it is weak which would greatly benefit from tweaking. For example, in the match loss vs rank 1, which move or series of moves cost it the game?

* During iterative deepening, move probabilities from previous depths are not currently leveraged in any way. In theory, these could be used to evaluate stronger moves first in hopes for earlier pruning similar to what is done with alpha-beta search and chess.

* Currently, time is allocated uniformly for all rounds and the game is assumed to last the maximum length for safety. It would be great to be able to come up with some heuristic to detect the importance of a decision and factor that into the time controls.

* The code needs further profiling for performance bottlenecks.
