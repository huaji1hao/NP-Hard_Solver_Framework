package com.aim.project.uzf.solution;

import com.aim.project.uzf.interfaces.UAVSolutionInterface;
import com.aim.project.uzf.interfaces.SolutionRepresentationInterface;

/**
 * @author Warren G Jackson
 * @since 1.0.0 (22/03/2024)
 */
public class UZFSolution implements UAVSolutionInterface {
	private SolutionRepresentationInterface representation;
	private int objectiveFunctionValue;
	public UZFSolution(SolutionRepresentationInterface representation, int objectiveFunctionValue) {
		this.representation = representation;
		this.objectiveFunctionValue = objectiveFunctionValue;
	}

	@Override
	public int getObjectiveFunctionValue() {
		return objectiveFunctionValue;
	}

	@Override
	public void setObjectiveFunctionValue(int objectiveFunctionValue) {
		this.objectiveFunctionValue = objectiveFunctionValue;
	}

	@Override
	public SolutionRepresentationInterface getSolutionRepresentation() {
		return representation;
	}
	
	@Override
	public UAVSolutionInterface clone() {
		try {
			UZFSolution clone = (UZFSolution) super.clone();
			clone.representation = (SolutionRepresentationInterface) this.representation.clone();
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new AssertionError(); // Can never happen
		}
	}

	@Override
	public int getNumberOfLocations() {
		return representation.getNumberOfLocations();
	}
}
