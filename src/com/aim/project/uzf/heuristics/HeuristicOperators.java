package com.aim.project.uzf.heuristics;

import java.util.Random;

import com.aim.project.uzf.interfaces.ObjectiveFunctionInterface;
import com.aim.project.uzf.solution.SolutionRepresentation;

/**
 * @author Warren G Jackson
 * @since 1.0.0 (22/03/2024)
 * <br>
 * This class is included (and all non-crossover heuristics subclass this class) to simplify your implementation and it
 * is intended that you include any common operations in this class to simplify your implementation of the other heuristics.
 * Furthermore, if you implement and test common functionality here, it is less likely that you introduce a bug elsewhere!
 * <br>
 * For example, think about common neighbourhood operators and any other incremental changes that you might perform
 * while applying low-level heuristics.
 */
public class HeuristicOperators {

	protected ObjectiveFunctionInterface f;

	protected final Random random;

	public HeuristicOperators(Random random) {

		this.random = random;
	}

	public void setObjectiveFunction(ObjectiveFunctionInterface f) {

		this.f = f;
	}

	public int getOperationTimes(double intensity) {
		if(intensity < 0.2) {
			return 1;
		} else if(intensity < 0.4) {
			return 2;
		} else if(intensity < 0.6) {
			return 3;
		} else if(intensity < 0.8) {
			return 4;
		} else{
			return 5;
		}
	}

	public void swapIndex(int[] solutionRepresentation, int index1, int index2) {
		int temp = solutionRepresentation[index1];
		solutionRepresentation[index1] = solutionRepresentation[index2];
		solutionRepresentation[index2] = temp;
	}

	// If representation1 is better than representation2, return true; otherwise, return false
	public boolean compareRepresentation(int[] representation1, int[] representation2, boolean includeEquality) {
		if(includeEquality){
			return f.getObjectiveFunctionValue(new SolutionRepresentation(representation1)) <=
					f.getObjectiveFunctionValue(new SolutionRepresentation(representation2));
		}else{
			return f.getObjectiveFunctionValue(new SolutionRepresentation(representation1)) <
					f.getObjectiveFunctionValue(new SolutionRepresentation(representation2));
		}
	}

	public int[] shufflePermutation(int[] array, Random rng) {
		int k;
		int temp;
		for(int n = array.length; n > 1; array[k] = temp) {
			k = rng.nextInt(n);
			--n;
			temp = array[n];
			array[n] = array[k];
		}
		return array;
	}

	protected int calculateSwapDelta(int[] representation, int i, int j) {
		int delta = 0;

		// Boundary checks for food preparation area or end of list
		int prevI = (i == 0) ? -1 : representation[i - 1];
		int nextI = (i == representation.length - 1) ? -1 : representation[i + 1];
		int prevJ = (j == 0) ? -1 : representation[j - 1];
		int nextJ = (j == representation.length - 1) ? -1 : representation[j + 1];

		int locI = representation[i];
		int locJ = representation[j];

		// Swap logic for adding costs
		if (i + 1 == j) { // If i and j are adjacent
			delta -= f.getCost(prevI, locI);
			delta -= f.getCost(locJ, nextJ);
			delta += f.getCost(prevI, locJ);
			delta += f.getCost(nextJ, locI);
		} else if(j + 1 == i) { // If j and i are adjacent
			delta -= f.getCost(prevJ, locJ);
			delta -= f.getCost(locI, nextI);
			delta += f.getCost(prevJ, locI);
			delta += f.getCost(nextI, locJ);

		} // If i and j are not adjacent, then swap the connections around i and j
		else {
			// Non-adjacent general case
			delta -= f.getCost(prevI, locI);
			delta -= f.getCost(locI, nextI);
			delta -= f.getCost(prevJ, locJ);
			delta -= f.getCost(locJ, nextJ);

			delta += f.getCost(prevI, locJ);
			delta += f.getCost(locJ, nextI);
			delta += f.getCost(prevJ, locI);
			delta += f.getCost(locI, nextJ);
		}

		return delta;
	}

}
