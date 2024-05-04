package com.aim.project.uzf.interfaces;

/**
 * @author Warren G Jackson
 * @since 1.0.0 (22/03/2024)
 */
public interface HeuristicInterface {

	/**
	 * Applies this heuristic to the solution <code>oSolution</code>
	 * and updates the objective value of the solution.
	 * @param oSolution The solution to apply the heuristic to.
	 * @param dDepthOfSearch The current depth of search setting.
	 * @param dIntensityOfMutation The current intensity of mutation setting.
	 */
	public int apply(UAVSolutionInterface oSolution, double dDepthOfSearch, double dIntensityOfMutation);
	
	public boolean isCrossover();
	
	public boolean usesIntensityOfMutation();
	
	public boolean usesDepthOfSearch();
	
	public void setObjectiveFunction(ObjectiveFunctionInterface oObjectiveFunction);
}
