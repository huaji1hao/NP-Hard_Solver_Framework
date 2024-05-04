package com.aim.project.uzf.heuristics;

import java.util.Random;

import com.aim.project.uzf.interfaces.HeuristicInterface;
import com.aim.project.uzf.interfaces.UAVSolutionInterface;

/**
 * @author Warren G Jackson
 * @since 1.0.0 (22/03/2024)
 */
public class AdjacentSwap extends HeuristicOperators implements HeuristicInterface {

	public AdjacentSwap(Random random) {

		super(random);
	}

	@Override
	public int apply(UAVSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {
		/*
		 * Swaps two adjacent locations in the solution representation.
		 * The number of swaps is determined by the intensity of mutation.
		 */

		int swapTimes;
		if(intensityOfMutation < 0.2) {
			swapTimes = 1;
		} else if(intensityOfMutation < 0.4) {
			swapTimes = 2;
		} else if(intensityOfMutation < 0.6) {
			swapTimes = 4;
		} else if(intensityOfMutation < 0.8) {
			swapTimes = 8;
		} else if (intensityOfMutation < 1.0){
			swapTimes = 16;
		} else {
			swapTimes = 32;
		}

		// swap two adjacent locations and if the index is the last one, swap with the first one
		for(int i = 0; i < swapTimes; i++) {
			int[] representation = solution.getSolutionRepresentation().getSolutionRepresentation();
			int index = random.nextInt(representation.length);
			int nextIndex = (index + 1) % representation.length;

			swapIndex(representation, index, nextIndex);
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
