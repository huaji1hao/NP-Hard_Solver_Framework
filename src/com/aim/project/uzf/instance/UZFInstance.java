package com.aim.project.uzf.instance;


import java.util.*;

import com.aim.project.uzf.UZFObjectiveFunction;
import com.aim.project.uzf.interfaces.ObjectiveFunctionInterface;
import com.aim.project.uzf.interfaces.UZFInstanceInterface;
import com.aim.project.uzf.interfaces.UAVSolutionInterface;
import com.aim.project.uzf.solution.SolutionRepresentation;
import com.aim.project.uzf.solution.UZFSolution;


/**
 * @author Warren G Jackson
 * @since 1.0.0 (22/03/2024)
 */
public class UZFInstance implements UZFInstanceInterface {
	private final int numberOfLocations;
	private final Location[] aoLocations;
	private final Location foodPreparationLocation;
	private final Random random;
	private final ObjectiveFunctionInterface objectiveFunction;
	private final Map<Integer, Location> locationMap; // Map for quick ID to Location mapping

	public UZFInstance(int numberOfLocations, Location[] aoLocations, Location foodPreparationLocation, Random random) {
		this.numberOfLocations = numberOfLocations;
		this.aoLocations = aoLocations;
		this.foodPreparationLocation = foodPreparationLocation;
		this.random = random;

		// Create the objective function object
		objectiveFunction = new UZFObjectiveFunction(this);
		// Initialize the map and populate it
		this.locationMap = new HashMap<>();
		for (Location location : aoLocations) {
			this.locationMap.put(location.iLocationId(), location);
		}
	}

	@Override
	public UZFSolution createSolution(InitialisationMode mode) {
        return switch (mode) {
            case RANDOM -> createRandomSolution();
            case CONSTRUCTIVE -> createConstructiveSolution();
        };
	}

	@Override
	public ObjectiveFunctionInterface getUZFObjectiveFunction() {
		return objectiveFunction;
	}

	@Override
	public int getNumberOfLocations() {
		return numberOfLocations;
	}

	@Override
	public Location getLocationForEnclosure(int iEnclosureId) {
		Location location = locationMap.get(iEnclosureId);
		if (location == null) {
			System.err.println("Location not found for ID: " + iEnclosureId);
		}
		return location;
	}

	@Override
	public Location getLocationOfFoodPreparationArea() {
		return foodPreparationLocation;
	}

	@Override
	public ArrayList<Location> getSolutionAsListOfLocations(UAVSolutionInterface oSolution) {
		int[] locationIds = oSolution.getSolutionRepresentation().getSolutionRepresentation();
		ArrayList<Location> locations = new ArrayList<>();
		for (int locationId : locationIds) {
			locations.add(getLocationForEnclosure(locationId));
		}
		return locations;
	}

	private UZFSolution createRandomSolution() {
		List<Location> shuffledLocations = new ArrayList<>(List.of(aoLocations));
		// Shuffle the locations to create a random solution
		Collections.shuffle(shuffledLocations, random);
		// Convert the shuffled locations to an array of location IDs
		int[] locationIds = shuffledLocations.stream().mapToInt(Location::iLocationId).toArray();
		// Create a solution representation object
		SolutionRepresentation representation = new SolutionRepresentation(locationIds);
		return new UZFSolution(representation, objectiveFunction.getObjectiveFunctionValue(representation));
	}

	private UZFSolution createConstructiveSolution() {
		List<Location> solutionLocations = new ArrayList<>();

		// List without the food preparation location.
		List<Location> remainingLocations = new ArrayList<>(List.of(aoLocations));

		// Randomly select a starting location from the available locations.
		Location currentLocation = remainingLocations.get(random.nextInt(remainingLocations.size()));
		solutionLocations.add(currentLocation);
		remainingLocations.remove(currentLocation);

		// Apply the greedy nearest neighbor algorithm to construct the rest of the solution.
		while (!remainingLocations.isEmpty()) {
			Location nextLocation = nearestLocation(currentLocation, remainingLocations);
			solutionLocations.add(nextLocation);
			remainingLocations.remove(nextLocation);
			currentLocation = nextLocation;
		}

		// Convert the solution locations to an array of location IDs.
		int[] locationIds = solutionLocations.stream().mapToInt(Location::iLocationId).toArray();
		SolutionRepresentation representation = new SolutionRepresentation(locationIds);

		// Calculate the objective function value for the solution representation.
		return new UZFSolution(representation, objectiveFunction.getObjectiveFunctionValue(representation));
	}

	private Location nearestLocation(Location currentLocation, List<Location> remainingLocations) {
		Location nearest = null;
		double minDistance = Double.MAX_VALUE;
		for (Location location : remainingLocations) {
			int distance = objectiveFunction.getCost(currentLocation.iLocationId(), location.iLocationId());
			if (distance < minDistance) {
				minDistance = distance;
				nearest = location;
			}
		}
		return nearest;
	}


}
