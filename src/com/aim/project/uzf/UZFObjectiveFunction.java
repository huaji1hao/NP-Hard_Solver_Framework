package com.aim.project.uzf;

import com.aim.project.uzf.instance.Location;
import com.aim.project.uzf.interfaces.ObjectiveFunctionInterface;
import com.aim.project.uzf.interfaces.UZFInstanceInterface;
import com.aim.project.uzf.interfaces.SolutionRepresentationInterface;


/**
 * @author Warren G Jackson
 * @since 1.0.0 (22/03/2024)
 */
public class UZFObjectiveFunction implements ObjectiveFunctionInterface {
	private final UZFInstanceInterface oInstance;
	public UZFObjectiveFunction(UZFInstanceInterface oInstance) {
		this.oInstance = oInstance;
	}

	@Override
	public int getObjectiveFunctionValue(SolutionRepresentationInterface oSolution) {
		int[] locations = oSolution.getSolutionRepresentation();
		int totalDistance = 0;

		// Calculate the total distance by iterating through each pair of consecutive locations
		for (int i = 0; i < locations.length - 1; i++) {
			Location start = oInstance.getLocationForEnclosure(locations[i]);
			Location end = oInstance.getLocationForEnclosure(locations[i + 1]);
			totalDistance += getCost(start, end);
		}
		// Add the distance from the food preparation location to the first location
		totalDistance += getCostBetweenFoodPreparationAreaAnd(locations[0]);
		// Add the distance from the last location to the food preparation location
		totalDistance += getCostBetweenFoodPreparationAreaAnd(locations[locations.length - 1]);

		// Return the ceiling of the total distance as the objective function value
		return totalDistance;
	}
	
	public int getCost(Location oLocationA, Location oLocationB) {
		return (int) Math.ceil(Math.sqrt(
				Math.pow(oLocationA.x() - oLocationB.x(), 2) +
				Math.pow(oLocationA.y() - oLocationB.y(), 2)));
	}

	@Override
	public int getCost(int iLocationA, int iLocationB) {
		if(iLocationA == iLocationB) return 0;
		if(iLocationA == -1) return getCostBetweenFoodPreparationAreaAnd(iLocationB);
		if(iLocationB == -1) return getCostBetweenFoodPreparationAreaAnd(iLocationA);
		return getCost(oInstance.getLocationForEnclosure(iLocationA), oInstance.getLocationForEnclosure(iLocationB));
	}

	@Override
	public int getCostBetweenFoodPreparationAreaAnd(int iLocation) {
		return (int) Math.ceil(Math.sqrt(
				Math.pow(oInstance.getLocationOfFoodPreparationArea().x() - oInstance.getLocationForEnclosure(iLocation).x(), 2) +
				Math.pow(oInstance.getLocationOfFoodPreparationArea().y() - oInstance.getLocationForEnclosure(iLocation).y(), 2)));
	}

}
