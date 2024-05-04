package com.aim.project.uzf.interfaces;

/**
 * @author Warren G Jackson
 * @since 1.0.0 (22/03/2024)
 */
public interface XOHeuristicInterface extends HeuristicInterface {

	/**
	 * 
	 * @param oParent1            Parent solution 1
	 * @param oParent2            Parent solution 2
	 * @param dDepthOfSearch       current DOS setting
	 * @param dIntensityOfMutation currentIOM setting
	 */
	public double apply(UAVSolutionInterface oParent1, UAVSolutionInterface oParent2, UAVSolutionInterface oChild,
						double dDepthOfSearch, double dIntensityOfMutation);

}
