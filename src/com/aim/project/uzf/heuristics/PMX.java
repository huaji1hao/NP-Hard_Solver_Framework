package com.aim.project.uzf.heuristics;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;

import com.aim.project.uzf.interfaces.ObjectiveFunctionInterface;
import com.aim.project.uzf.interfaces.UAVSolutionInterface;
import com.aim.project.uzf.interfaces.XOHeuristicInterface;

/**
 * @author Warren G Jackson
 * @since 1.0.0 (22/03/2024)
 */
public class PMX implements XOHeuristicInterface {

	private final Random random;
	protected ObjectiveFunctionInterface f;

	public PMX(Random random) {
		
		this.random = random;
	}

	@Override
	public int apply(UAVSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {
		// This method should not be called
		System.out.println("Wrong PMX apply method called.");
		return -1;
	}

	@Override
	public double apply(UAVSolutionInterface p1, UAVSolutionInterface p2, UAVSolutionInterface c, double depthOfSearch, double intensityOfMutation) {
		int [] parent1 = p1.getSolutionRepresentation().getSolutionRepresentation();
		int [] parent2 = p2.getSolutionRepresentation().getSolutionRepresentation();
		int [] offspring1 = parent1.clone();
		int [] offspring2 = parent2.clone();
		int length = parent1.length;

		// Ensure crossover points do not include the first or last element and are distinct
		int startCrossoverPoint = random.nextInt(length - 2) + 1;  // Start from 1 to length-2
		int endCrossoverPoint = random.nextInt(length - startCrossoverPoint - 1) + startCrossoverPoint + 1; // Ensure end is always after start

		// Swap the elements between the crossover points
		for(int i = startCrossoverPoint; i <= endCrossoverPoint; i++) {
			offspring1[i] = parent2[i];
			offspring2[i] = parent1[i];
		}

		Map<Integer, Integer> mapping1 = new HashMap<>();
		Map<Integer, Integer> mapping2 = new HashMap<>();

		// Create mappings for the crossover points
		for(int i = startCrossoverPoint; i <= endCrossoverPoint; i++) {
			mapping1.put(parent1[i], parent2[i]);
			mapping2.put(parent2[i], parent1[i]);
		}

		// Resolve conflicts for the positions outside the crossover points
		for(int i = 0; i < length; i++) {
			if (i < startCrossoverPoint || i > endCrossoverPoint) {
				offspring1[i] = resolveConflict(parent1[i], mapping2, offspring1);
				offspring2[i] = resolveConflict(parent2[i], mapping1, offspring2);
			}
		}

		// Randomly choose one of the offspring to be the solution
		if(random.nextBoolean()){
			c.getSolutionRepresentation().setSolutionRepresentation(offspring1);
		} else {
			c.getSolutionRepresentation().setSolutionRepresentation(offspring2);
		}

		return f.getObjectiveFunctionValue(c.getSolutionRepresentation());
	}

	private int resolveConflict(int element, Map<Integer, Integer> mapping, int[] offspring) {
		HashSet<Integer> elementsInOffspring = new HashSet<>();
		for (int val : offspring) elementsInOffspring.add(val);

		// while element in mapping and offspring: element = mapping[element]
		while (mapping.containsKey(element) && elementsInOffspring.contains(element)) {
			element = mapping.get(element);
		}
		return element;
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
