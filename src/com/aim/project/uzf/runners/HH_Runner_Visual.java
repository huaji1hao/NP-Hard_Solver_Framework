package com.aim.project.uzf.runners;


import java.awt.Color;

import com.aim.project.uzf.UZFDomain;
import com.aim.project.uzf.instance.Location;
import com.aim.project.uzf.solution.UZFSolution;
import com.aim.project.uzf.visualiser.UAVView;

import AbstractClasses.HyperHeuristic;

/**
 * @author Warren G Jackson
 * @since 1.0.0 (22/03/2024)
 *
 * Runs a hyper-heuristic using a default configuration then displays the best solution found.
 */
public abstract class HH_Runner_Visual {

	private final int instanceId;

	public HH_Runner_Visual(int instanceId) {

		this.instanceId = instanceId;
	}
	
	public void run() {
		long seed = 18032024;
		long timeLimit = 42 * 1000;
		UZFDomain problem = new UZFDomain(seed);
		problem.loadInstance(instanceId);
		HyperHeuristic hh = getHyperHeuristic(seed);
		hh.setTimeLimit(timeLimit);
		hh.loadProblemDomain(problem);
		hh.run();
		
		System.out.println("f(s_best) = " + hh.getBestSolutionValue());
		new UAVView(problem.getLoadedInstance(), problem, Color.RED, Color.GREEN);
		
	}
	
	/** 
	 * Transforms the best solution found, represented as a TSPSolution, into an ordering of Location's
	 * which the visualiser tool uses to draw the tour.
	 */
	protected Location[] transformSolution(UZFSolution solution, UZFDomain problem) {
		
		return problem.getRouteOrderedByLocations();
	}
	
	/**
	 * Allows a general visualiser runner by making the HyperHeuristic abstract.
	 * You can sub-class this class to run any hyper-heuristic that you want.
	 */
	protected abstract HyperHeuristic getHyperHeuristic(long seed);
}
