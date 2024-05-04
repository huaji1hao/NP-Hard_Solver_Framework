package com.aim.project.uzf.interfaces;

/**
 * @author Warren G Jackson
 * @since 1.0.0 (22/03/2024)
 */
public interface SolutionRepresentationInterface extends Cloneable {
	
	/**
	 * 
	 * @return The current solution representation
	 */
	public int[] getSolutionRepresentation();
	
	/**
	 * Sets the representation of the solution to the new representation.
	 * @param aiRepresentation The new representation
	 */
	public void setSolutionRepresentation(int[] aiRepresentation);
	
	/**
	 * 
	 * @return The total number of locations in this instance (includes the food preparation area).
	 */
	public int getNumberOfLocations();

	/**
	 * 
	 * @return A deep clone of the solution representation.
	 */
	public SolutionRepresentationInterface clone();
}
