package com.aim.project.uzf.interfaces;

/**
 * @author Warren G Jackson
 * @since 1.0.0 (22/03/2024)
 *
 * Interface for an objective function for UZF.
 */
public interface ObjectiveFunctionInterface {

	/**
	 * 
	 * @param solutionRepresentation The representation of the current solution
	 * @return The objective function of the current solution represented by <code>solutionRepresentation</code>
	 */
	public int getObjectiveFunctionValue(SolutionRepresentationInterface solutionRepresentation);
	
	/**
	 * 
	 * @param iLocationA ID of the enclosure travelling from.
	 * @param iLocationB ID of the enclosure travelling to.
	 * @return The distance between enclosures <code>iLocationA</code> and <code>iLocationB</code>.
	 */
	public int getCost(int iLocationA, int iLocationB);
	
	/**
	 * 
	 * @param iLocation ID of the enclosure.
	 * @return The cost of going from the food preparation area to the enclosure location with ID iLocation.
	 */
	public int getCostBetweenFoodPreparationAreaAnd(int iLocation);

}
