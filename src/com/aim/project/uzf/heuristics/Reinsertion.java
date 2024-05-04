package com.aim.project.uzf.heuristics;

import java.util.Random;

import com.aim.project.uzf.interfaces.HeuristicInterface;
import com.aim.project.uzf.interfaces.UAVSolutionInterface;
import com.aim.project.uzf.solution.SolutionRepresentation;

/**
 * @author Warren G Jackson
 * @since 1.0.0 (22/03/2024)
 */
public class Reinsertion extends HeuristicOperators implements HeuristicInterface {

//	This mutation type heuristic removes a single element and reinserts it into a different position of the solution. It should be possible to choose any element for removal, and it should be possible that it gets reinserted at any other position.
	public Reinsertion(Random random) {

		super(random);
	}

	@Override
	public int apply(UAVSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {
		/*
		 * The intensity of mutation is used to determine how many times the heuristic is applied.
		 * This algorithm removes an element from the solution and reinserts it at a different position.
		 */
		int insertTimes = getOperationTimes(intensityOfMutation);

		// remove an element and reinsert it at a different position for insertTimes times
		for(int i = 0; i < insertTimes; i++) {
			int[] representation = solution.getSolutionRepresentation().getSolutionRepresentation();
			int removeIndex = random.nextInt(representation.length);
			int insertIndex = random.nextInt(representation.length);

			// remove the element at removeIndex and insert it at insertIndex
			int removeValue = representation[removeIndex];
			if(removeIndex < insertIndex) {
				for(int j = removeIndex; j < insertIndex; j++) {
					representation[j] = representation[j + 1];
				}
			} else if(removeIndex > insertIndex){
				for(int j = removeIndex; j > insertIndex; j--) {
					representation[j] = representation[j - 1];
				}
			}
			representation[insertIndex] = removeValue;
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
