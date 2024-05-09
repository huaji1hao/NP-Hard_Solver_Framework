package com.aim.project.uzf.solution;

import com.aim.project.uzf.interfaces.SolutionRepresentationInterface;

/**
 * @author Warren G Jackson
 * @since 1.0.0 (22/03/2024)
 */
public class SolutionRepresentation implements SolutionRepresentationInterface {
	private int[] aiRepresentation;

	// Constructor to deep copy the given array
	public SolutionRepresentation(int[] aiRepresentation) {
		this.aiRepresentation = aiRepresentation;
	}

	@Override
	public int[] getSolutionRepresentation() {
		return aiRepresentation;
	}

	@Override
	public void setSolutionRepresentation(int[] aiSolutionRepresentation) {
		this.aiRepresentation = aiSolutionRepresentation;
	}

	@Override
	public int getNumberOfLocations() {
		// all locations and the food preparation area
		return aiRepresentation.length + 1;
	}

	@Override
	public SolutionRepresentationInterface clone() {
		try {
			SolutionRepresentation clone = (SolutionRepresentation) super.clone();  // Clone the SolutionRepresentation object
			clone.aiRepresentation = this.aiRepresentation.clone(); // Deep clone the array
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new AssertionError(); // This shouldn't happen since we are Cloneable
		}
	}

}
