package com.aim.project.uzf.heuristics;

import java.util.Random;

import com.aim.project.uzf.interfaces.HeuristicInterface;
import com.aim.project.uzf.interfaces.UAVSolutionInterface;


/**
 * @author Warren G Jackson
 * @since 1.0.0 (22/03/2024)
 */
public class DavissHillClimbing extends HeuristicOperators implements HeuristicInterface {

	public DavissHillClimbing(Random random) {
	
		super(random);
	}

	@Override
	public int apply(UAVSolutionInterface solution, double dos, double iom) {
		/*
		 * The depth of search parameter is used to determine the number of iterations
		 *
		 * This algorithm first generates a random permutation of the solution representation
		 * Then, for each element in the permutation, it swaps the element with the next element
		 * If the new representation is better, it is accepted else the swap is undone
		 */
		int iterations = getOperationTimes(dos);

		for(int i = 0 ; i < iterations; i++) {
			int[] representation = solution.getSolutionRepresentation().getSolutionRepresentation();
			int length = representation.length;
			// Random permutation for representation
			int[] perm = shufflePermutation(representation.clone(), random);

			for(int j = 0; j < length; j++) {
				representation = solution.getSolutionRepresentation().getSolutionRepresentation().clone();
				int delta = calculateSwapDelta(representation, perm[j], perm[(j + 1) % length]);

				if(delta <= 0) {
					// Swap the elements if the new representation is better
					swapIndex(representation, perm[j], perm[(j + 1) % length]);
					solution.getSolutionRepresentation().setSolutionRepresentation(representation);
				}

//				swapIndex(representation, perm[j], perm[(j + 1) % length]);
//
//				if(compareRepresentation(representation, solution.getSolutionRepresentation().getSolutionRepresentation(), true)) {
//					solution.getSolutionRepresentation().setSolutionRepresentation(representation);
//				} else {
//					swapIndex(representation, perm[j], perm[(j + 1) % length]);
//				}

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
