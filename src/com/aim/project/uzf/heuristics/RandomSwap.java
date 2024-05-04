package com.aim.project.uzf.heuristics;

import java.util.Random;
import com.aim.project.uzf.interfaces.HeuristicInterface;
import com.aim.project.uzf.interfaces.UAVSolutionInterface;

/**
 * Performs a random swap heuristic.
 * Randomly selects two indices and swaps their values in the solution's representation.
 * This heuristic can introduce significant changes to the solution structure with a simple operation.
 */
public class RandomSwap extends HeuristicOperators implements HeuristicInterface {

    public RandomSwap(Random random) {
        super(random);
    }

    @Override
    public int apply(UAVSolutionInterface solution, double dos, double iom) {
        int iterTimes = getOperationTimes(iom);
        for (int i = 0; i < iterTimes; i++) {
            int[] representation = solution.getSolutionRepresentation().getSolutionRepresentation().clone();
            int length = representation.length;

            if (length < 2) {
                // Need at least 2 elements to perform a random swap
                return solution.getObjectiveFunctionValue();
            }

            // Randomly select two distinct indices
            int firstIndex = random.nextInt(length);
            int secondIndex = random.nextInt(length);
            while (firstIndex == secondIndex) {
                secondIndex = random.nextInt(length);
            }

            // Perform the swap
            swapIndex(representation, firstIndex, secondIndex);

            // Check if the new solution is better
//            if (compareRepresentation(representation, solution.getSolutionRepresentation().getSolutionRepresentation(), true)) {
//                solution.getSolutionRepresentation().setSolutionRepresentation(representation);
//            }
            solution.getSolutionRepresentation().setSolutionRepresentation(representation);
        }

        return f.getObjectiveFunctionValue(solution.getSolutionRepresentation());
    }

    @Override
    public boolean isCrossover() {
        return false;
    }

    @Override
    public boolean usesIntensityOfMutation() {
        return true;
    }

    @Override
    public boolean usesDepthOfSearch() {
        return false;
    }
}
