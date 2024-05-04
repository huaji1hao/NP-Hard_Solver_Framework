package com.aim.project.uzf.heuristics;

import com.aim.project.uzf.interfaces.HeuristicInterface;
import com.aim.project.uzf.interfaces.UAVSolutionInterface;

import java.util.Random;

public class SegmentInversion extends HeuristicOperators implements HeuristicInterface {

    public SegmentInversion(Random random) {
        super(random);
    }

    @Override
    public int apply(UAVSolutionInterface solution, double dos, double iom) {
        int depth = getOperationTimes(dos);
        for(int i = 0; i < depth; ++i) {
            int[] representation = solution.getSolutionRepresentation().getSolutionRepresentation().clone();
            int length = representation.length;

            // Randomly select two indices to define the inversion segment
            int start = random.nextInt(length);
            int end = random.nextInt(length);

            // Ensure start is less than end
            if (start > end) {
                int temp = start;
                start = end;
                end = temp;
            }

            // Perform the segment inversion
            reverseSegment(representation, start, end);

            // Check if the new solution is better
            if (compareRepresentation(representation, solution.getSolutionRepresentation().getSolutionRepresentation(), true)) {
                solution.getSolutionRepresentation().setSolutionRepresentation(representation);
            }
        }

        // Return the current objective value if not improved
        return f.getObjectiveFunctionValue(solution.getSolutionRepresentation());
    }

    private void reverseSegment(int[] array, int start, int end) {
        while (start < end) {
            int temp = array[start];
            array[start] = array[end];
            array[end] = temp;
            start++;
            end--;
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
