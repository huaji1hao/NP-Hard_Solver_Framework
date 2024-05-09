package com.aim.project.uzf.heuristics;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.aim.project.uzf.interfaces.ObjectiveFunctionInterface;
import com.aim.project.uzf.interfaces.UAVSolutionInterface;
import com.aim.project.uzf.interfaces.XOHeuristicInterface;

public class OrderCrossover implements XOHeuristicInterface {

    private final Random random;
    protected ObjectiveFunctionInterface f;

    public OrderCrossover(Random random) {
        this.random = random;
    }

    @Override
    public int apply(UAVSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {
        System.out.println("Wrong OrderCrossover apply method called.");
        return -1;
    }

    @Override
    public double apply(UAVSolutionInterface p1, UAVSolutionInterface p2, UAVSolutionInterface c, double depthOfSearch, double intensityOfMutation) {
        int[] parent1 = p1.getSolutionRepresentation().getSolutionRepresentation();
        int[] parent2 = p2.getSolutionRepresentation().getSolutionRepresentation();
        int[] offspring1 = new int[parent1.length];
        int[] offspring2 = new int[parent2.length];

        int start = random.nextInt(parent1.length - 2) + 1;
        int end = random.nextInt(parent1.length - start - 1) + start + 1;

        Arrays.fill(offspring1, -1); // Initialize with -1 to denote empty slots
        Arrays.fill(offspring2, -1);

        // Copy segments
        System.arraycopy(parent2, start, offspring1, start, end - start + 1);
        System.arraycopy(parent1, start, offspring2, start, end - start + 1);

        // Fill remaining slots
        fillRemaining(offspring1, parent1, end);
        fillRemaining(offspring2, parent2, end);

        // randomly choose one of the offspring
        if(random.nextBoolean()) {
            c.getSolutionRepresentation().setSolutionRepresentation(offspring1);
        } else {
            c.getSolutionRepresentation().setSolutionRepresentation(offspring2);
        }

        return f.getObjectiveFunctionValue(c.getSolutionRepresentation());
    }

    /**
     * Fill the remaining slots of the offspring with the values from the parent
     * that are not in the segment.
     *
     * @param offspring The offspring to fill
     * @param parent The parent to get the values from
     * @param end The end index of the segment
     */
    private void fillRemaining(int[] offspring, int[] parent, int end) {
        // Create a set of the values in the segment
        Set<Integer> included = new HashSet<>();
        for (int value : offspring) {
            if (value != -1) {
                included.add(value);
            }
        }
        // Fill the remaining slots with the values from the parent
        // that are not in the segment
        int fillIndex = (end + 1) % parent.length;
        for (int i = 0; i < parent.length; i++) {
            int candidateIndex = (end + 1 + i) % parent.length;
            // If the value is not in the segment, add it to the offspring
            if (!included.contains(parent[candidateIndex])) {
                offspring[fillIndex] = parent[candidateIndex];
                fillIndex = (fillIndex + 1) % parent.length;
            }
        }
    }

    @Override
    public void setObjectiveFunction(ObjectiveFunctionInterface f) {
        this.f = f;
    }

    @Override
    public boolean isCrossover() {
        return true;
    }

    @Override
    public boolean usesIntensityOfMutation() {
        return false;
    }

    @Override
    public boolean usesDepthOfSearch() {
        return false;
    }
}
