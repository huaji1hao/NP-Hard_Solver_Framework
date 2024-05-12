package com.aim.project.uzf.runners;


import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;
import com.aim.project.uzf.UZFDomain;
import com.aim.project.uzf.hyperheuristics.MCF_HH;
import com.aim.project.uzf.visualiser.UAVView;

import java.awt.*;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author Warren G. Jackson
 *
 * Creates an initial solution and finishes.
 * Can be used for testing your initialisation method.
 */
public class InitialisationTest_HH extends HyperHeuristic {

	public InitialisationTest_HH(long seed) {
		
		super(seed);
	}

	@Override
	protected void solve(ProblemDomain problem) {

		problem.initialiseSolution(0);
		hasTimeExpired();
	}

	@Override
	public String toString() {

		return "InitialisationTest_HH";
	}

	public static void main(String[] args) {

		// TODO update to point to the correct problem instance
		int INSTANCE_ID = 8;

		long seed = 10_05_2024;

		// this should make no difference in the test
		long timeLimit = 60_000 * 5;

		UZFDomain problem = new UZFDomain(seed);
		problem.loadInstance(INSTANCE_ID);
//		HyperHeuristic hh = new InitialisationTest_HH(seed);
		HyperHeuristic hh = new MCF_HH(seed);
		hh.setTimeLimit(timeLimit);
		hh.loadProblemDomain(problem);
		hh.run();

		// solution should be initialised in index 0
		System.out.println("f(s_init) = " + problem.getFunctionValue(0));
		System.out.println("Initial solution = " + Arrays.toString(problem.getSolution(0)));
		System.out.println("Co-ordinates = " + Arrays.stream(problem.getSolution(0)).boxed()
				.map(i -> problem.getLoadedInstance().getLocationForEnclosure(i))
				.map(l -> "(" + l.x() + ", " + l.y() + ")")
				.collect(Collectors.joining(" - ")));
	}
}
