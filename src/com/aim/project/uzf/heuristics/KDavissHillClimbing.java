package com.aim.project.uzf.heuristics;

import com.aim.project.uzf.interfaces.HeuristicInterface;
import com.aim.project.uzf.interfaces.UAVSolutionInterface;

import java.util.Random;

public class KDavissHillClimbing extends HeuristicOperators implements HeuristicInterface {

    public KDavissHillClimbing(Random random) {

        super(random);
    }

    @Override
    public int apply(UAVSolutionInterface solution, double dos, double iom) {
        /*
         * The depth of search parameter is used to determine the number of iterations
         *
         * This algorithm first generates a random permutation of the solution representation
         * Then, for each element in the permutation, it swaps the element with the next element k times
         * If the new representation is better, it is accepted else the swap is undone
         */
        int iterations = getOperationTimes(dos);

        for (int i = 0; i < iterations; i++) {
            int[] representation = solution.getSolutionRepresentation().getSolutionRepresentation().clone();
            int length = representation.length;
            // Random permutation for representation
            int[] perm = shufflePermutation(representation.clone(), random);
            int k = random.nextInt(length - 1);

            for (int j = 0; j < length; j++) {
                int[] originalRepresentation = representation.clone();
                swapKIndex(representation, perm[j], k);

                if (compareRepresentation(representation, solution.getSolutionRepresentation().getSolutionRepresentation(), true)) {
                    solution.getSolutionRepresentation().setSolutionRepresentation(representation);
                } else {
                    representation = originalRepresentation;
                }

            }
        }

        return f.getObjectiveFunctionValue(solution.getSolutionRepresentation());
    }

    private void swapKIndex(int[] solutionRepresentation, int index, int k) {
        int length = solutionRepresentation.length;
        for(int i = 0; i < k; i++) {
            int current = (index + i) % length;
            int next = (index + i + 1) % length;
            swapIndex(solutionRepresentation, current, next);
        }
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
