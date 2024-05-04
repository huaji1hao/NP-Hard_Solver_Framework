package com.aim.project.uzf.hyperheuristics;


import com.aim.project.uzf.UZFDomain;
import com.aim.project.uzf.SolutionPrinter;
import com.aim.project.uzf.interfaces.UAVSolutionInterface;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;

import java.util.Arrays;

public class SR_IE_HH extends HyperHeuristic {

	private static final int SECOND_PARENT_INDEX = 2;

	private static final int BEST_ACCEPTED_INDEX = 3;

	public SR_IE_HH(long lSeed) {
		
		super(lSeed);
	}

	@Override
	protected void solve(ProblemDomain oProblem) {

		oProblem.setMemorySize(4);

		int currentIndex = 0;
		int candidateIndex = 1;
		oProblem.initialiseSolution(currentIndex);
		oProblem.copySolution(currentIndex, BEST_ACCEPTED_INDEX);

		double currentCost = oProblem.getFunctionValue(currentIndex);
		int numberOfHeuristics = oProblem.getNumberOfHeuristics();

		// cache indices of crossover heuristics
		boolean[] isCrossover = new boolean[numberOfHeuristics];
		Arrays.fill(isCrossover, false);

		for(int i : oProblem.getHeuristicsOfType(ProblemDomain.HeuristicType.CROSSOVER)) {

			isCrossover[i] = true;
		}

		// main search loop
		double candidateCost;
		while(!hasTimeExpired()) {

			int h = rng.nextInt(numberOfHeuristics);
			if(isCrossover[h]) {

				if(rng.nextBoolean()) {
					// randomly choose between crossover with newly initialised solution
					oProblem.initialiseSolution(SECOND_PARENT_INDEX);
					candidateCost = oProblem.applyHeuristic(h, currentIndex, SECOND_PARENT_INDEX, candidateIndex);
				} else {
					// or with best solution accepted so far
					candidateCost = oProblem.applyHeuristic(h, currentIndex, BEST_ACCEPTED_INDEX, candidateIndex);
				}
			} else {
				candidateCost = oProblem.applyHeuristic(h, currentIndex, candidateIndex);
			}

			// update best
			if(candidateCost < currentCost) {

				oProblem.copySolution(candidateIndex, BEST_ACCEPTED_INDEX);
			}

			// accept improving or equal moves
			if(candidateCost <= currentCost) {

				currentCost = candidateCost;
				currentIndex = 1 - currentIndex;
				candidateIndex = 1 - candidateIndex;
			}
		}

		
		UAVSolutionInterface oSolution = ((UZFDomain) oProblem).getBestSolution();
		SolutionPrinter oSolutionPrinter = new SolutionPrinter("out.csv");
		oSolutionPrinter.printSolution( ((UZFDomain) oProblem).getLoadedInstance().getSolutionAsListOfLocations(oSolution));
	}

	@Override
	public String toString() {

		return "SR_IE_HH";
	}
}
