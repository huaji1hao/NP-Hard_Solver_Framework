package com.aim.project.uzf.heuristics;


import java.util.Random;

import com.aim.project.uzf.interfaces.HeuristicInterface;
import com.aim.project.uzf.interfaces.UAVSolutionInterface;
import com.aim.project.uzf.solution.SolutionRepresentation;


/**
 * @author Warren G Jackson
 * @since 1.0.0 (22/03/2024)
 */
public class NextDescent extends HeuristicOperators implements HeuristicInterface {

	public NextDescent(Random random) {
	
		super(random);
	}

	@Override
	public int apply(UAVSolutionInterface solution, double dos, double iom) {
		/*
		 * The iterTimes is determined by the intensity of search
		 * During each iteration, the heuristic will randomly select a starting index
		 * and perform swaps on the representation until it finds an improved solution
		 */
        int iterTimes = getOperationTimes(dos);

		for (int i = 0; i < iterTimes; i++) {
			boolean improved = false;
			int[] representation = solution.getSolutionRepresentation().getSolutionRepresentation().clone();
			int startIndex = random.nextInt(representation.length);
			int length = representation.length;

			// Perform swaps starting from random index and wrapping around
			for (int j = 0; j < length - 1; j++) {
				int currentIndex = (startIndex + j) % length;
				int nextIndex = (currentIndex + 1) % length;

				// Calculate delta cost of the swap
				int delta = calculateSwapDelta(representation, currentIndex, nextIndex);

				// If delta cost shows improvement, update the total cost and break
				if (delta <= 0) {
					swapIndex(representation, currentIndex, nextIndex);
					improved = true;
					break; // Exit the inner loop if an improvement is found
				}

//				// Perform a swap
//				swapIndex(representation, currentIndex, nextIndex);
//
//				// Check if the new solution is better
//				if (compareRepresentation(representation, solution.getSolutionRepresentation().getSolutionRepresentation(), true)) {
//					improved = true;
//					break; // Exit the inner loop if an improvement is found
//				} else {
//					// Swap back if not improved
//					swapIndex(representation, currentIndex, nextIndex);
//				}
			}

			if(improved){
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
