package com.aim.project.uzf.interfaces;

import com.aim.project.uzf.instance.Location;

/**
 * @author Warren G Jackson
 * @since 1.0.0 (22/03/2024)
 */
public interface Visualisable {

	/**
	 * 
	 * @return The UZF route in visited location order.
	 */
	public Location[] getRouteOrderedByLocations();
	
	/**
	 * 
	 * @return The problem instance that is currently loaded.
	 */
	public UZFInstanceInterface getLoadedInstance();

	/**
	 *
	 * @return The integer array representing the ordering of the best solution.
	 */
	public int[] getBestSolutionRepresentation();
}
