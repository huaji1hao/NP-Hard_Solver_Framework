package com.aim.project.uzf.heuristics;

import java.util.Random;
import com.aim.project.uzf.interfaces.HeuristicInterface;
import com.aim.project.uzf.interfaces.UAVSolutionInterface;

/**
 * Performs an adjacent pair swap heuristic.
 * Selects two pairs of consecutive elements, disconnects their current links, 
 * and reconnects them in a swapped order to potentially find a better solution.
 */
public class EdgeRecombination extends HeuristicOperators implements HeuristicInterface {

    public EdgeRecombination(Random random) {
        super(random);
    }

    @Override
    public int apply(UAVSolutionInterface solution, double dos, double iom) {
        int depth = getOperationTimes(dos);
        for(int i = 0; i < depth; i++) {
            int[] representation = solution.getSolutionRepresentation().getSolutionRepresentation().clone();
            int length = representation.length;

            if (length < 4) {
                // Need at least 4 elements to perform a meaningful adjacent pair swap
                return solution.getObjectiveFunctionValue();
            }

            // Get two distinct indices to start the pairs
            int firstStart = random.nextInt(length - 1);  // Ensure there's room for at least one element after
            int secondStart = random.nextInt(length - 1); // Same as above

            // Ensure the indices do not overlap and there is at least one element separating them
            while (Math.abs(firstStart - secondStart) < 2) {
                secondStart = random.nextInt(length - 1);
            }

            // Ensure secondStart is always after firstStart
            if (firstStart > secondStart) {
                int temp = firstStart;
                firstStart = secondStart;
                secondStart = temp;
            }

            // Perform swaps
            // Swap end of first pair with start of second pair
            swapIndex(representation, firstStart + 1, secondStart);

            // Check if the new solution is better
            if (compareRepresentation(representation, solution.getSolutionRepresentation().getSolutionRepresentation(), true)) {
                solution.getSolutionRepresentation().setSolutionRepresentation(representation);
            }
        }

        return f.getObjectiveFunctionValue(solution.getSolutionRepresentation());
    }

    @Override
    public boolean isCrossover() {
        return false;
    }

    @Override
    public boolean usesIntensityOfMutation() {
        return false;
    }

    @Override
    public boolean usesDepthOfSearch() {
        return true;
    }

}
