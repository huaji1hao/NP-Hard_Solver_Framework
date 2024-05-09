package com.aim.project.uzf;

import com.aim.project.uzf.heuristics.*;
import com.aim.project.uzf.instance.InitialisationMode;
import com.aim.project.uzf.instance.Location;
import com.aim.project.uzf.instance.reader.UAVInstanceReader;
import com.aim.project.uzf.interfaces.*;

import AbstractClasses.ProblemDomain;
import com.aim.project.uzf.solution.UZFSolution;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Warren G Jackson
 * @since 1.0.0 (22/03/2024)
 */
public class UZFDomain extends ProblemDomain implements Visualisable {
	private final int[] mutations;
	private final int[] localSearches;
	private final int[] crossovers;

	private UAVSolutionInterface[] solutionMemory;
	private UAVSolutionInterface bestEverSolution;
	UZFInstanceInterface instance;

	public UZFDomain(long seed) {
		// set default memory size and create the array of low-level heuristics
		super(seed);
		mutations = new int[]{3, 4, 7};
		localSearches = new int[]{0, 1, 2, 6};
		crossovers = new int[]{5, 8};

		this.setDepthOfSearch(0.6);
		this.setIntensityOfMutation(0.3);
	}

	@Override
	public int getNumberOfHeuristics() {
		// has to be hard-coded due to the design of the HyFlex framework
		return 10;
	}

	// Unary heuristic
	@Override
	public double applyHeuristic(int hIndex, int currentIndex, int candidateIndex) {
		// apply heuristic and return the objective value of the candidate solution
		HeuristicInterface heuristic;
        switch (hIndex) {
            case 0 -> heuristic = new KDavissHillClimbing(rng);
            case 1 -> heuristic = new NextDescent(rng);
            case 2 -> heuristic = new DavissHillClimbing(rng);
			case 3 -> heuristic = new Reinsertion(rng);
			case 4 -> heuristic = new AdjacentSwap(rng);
			case 6 -> heuristic = new EdgeRecombination(rng);
			case 7 -> heuristic = new RandomSwap(rng);
            default -> heuristic = new SegmentInversion(rng);
        }

		heuristic.setObjectiveFunction(instance.getUZFObjectiveFunction());
		solutionMemory[candidateIndex] = solutionMemory[currentIndex].clone();

		int value = heuristic.apply(solutionMemory[candidateIndex], depthOfSearch, intensityOfMutation);
		solutionMemory[candidateIndex].setObjectiveFunctionValue(value);

		updateBestSolution(candidateIndex);
		return value;
	}

	// Binary heuristic
	@Override
	public double applyHeuristic(int hIndex, int parent1Index, int parent2Index, int candidateIndex) {
		XOHeuristicInterface heuristic;
		if(hIndex == 5) {
			heuristic = new PMX(rng);
		} else if(hIndex == 8) {
			heuristic = new OrderCrossover(rng);
		} else {
			return -1;
		}

		heuristic.setObjectiveFunction(instance.getUZFObjectiveFunction());
		solutionMemory[candidateIndex] = solutionMemory[parent1Index].clone();
		double value = heuristic.apply(solutionMemory[parent1Index], solutionMemory[parent2Index], solutionMemory[candidateIndex], depthOfSearch, intensityOfMutation);
		solutionMemory[candidateIndex].setObjectiveFunctionValue((int)value);

		updateBestSolution(candidateIndex);
		return value;
	}

	@Override
	public String bestSolutionToString() {
		StringBuilder s = new StringBuilder();
		s.append("Best Solution Found:\n");
		for(int i = 0; i < this.bestEverSolution.getNumberOfLocations() - 1; ++i) {
			s.append(this.bestEverSolution.getSolutionRepresentation().getSolutionRepresentation()[i]).append(" ");
		}
		s.append("\nObjective Function Value: ").append(this.bestEverSolution.getObjectiveFunctionValue()).append("\n");

		return s.toString();
	}

	@Override
	public boolean compareSolutions(int a, int b) {
		UZFSolution solutionA = (UZFSolution) solutionMemory[a];
		UZFSolution solutionB = (UZFSolution) solutionMemory[b];
		for(int i = 0; i < solutionA.getNumberOfLocations() - 1; ++i) {
			if (solutionA.getSolutionRepresentation().getSolutionRepresentation()[i] != solutionB.getSolutionRepresentation().getSolutionRepresentation()[i]) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void copySolution(int a, int b) {
		// BEWARE this should copy the solution, not the reference to it!
		// That is, that if we apply a heuristic to the solution in index 'b',
		// then it does not modify the solution in index 'a' or vice-versa.
		UAVSolutionInterface solution = solutionMemory[a].clone();
		solutionMemory[b] = solution;
	}

	@Override
	public double getBestSolutionValue() {
		return bestEverSolution.getObjectiveFunctionValue();
	}
	
	@Override
	public double getFunctionValue(int index) {
		return instance.getUZFObjectiveFunction().getObjectiveFunctionValue(solutionMemory[index].getSolutionRepresentation());
	}

	@Override
	public int[] getHeuristicsOfType(HeuristicType type) {
        return switch (type) {
            case MUTATION -> this.mutations;
            case CROSSOVER -> this.crossovers;
            case LOCAL_SEARCH -> this.localSearches;
            default -> null;
        };
	}


	@Override
	public int[] getHeuristicsThatUseDepthOfSearch() {
		return localSearches;
	}

	@Override
	public int[] getHeuristicsThatUseIntensityOfMutation() {
		return mutations;
	}

	@Override
	public int getNumberOfInstances() {
		return 7;
	}

	@Override
	public void initialiseSolution(int index) {
		// make sure that you also update the best solution!
		solutionMemory[index] = instance.createSolution(InitialisationMode.CONSTRUCTIVE);
//		solutionMemory[index] = instance.createSolution(InitialisationMode.RANDOM);
		updateBestSolution(index);
	}

	@Override
	public void loadInstance(int instanceId) {
		// load the instance (referenced by ID) from file
		UAVInstanceReaderInterface reader = new UAVInstanceReader();
		Path path = switch (instanceId) {
			case 0 -> Paths.get("instances/uzf/square.uzf");
			case 1 -> Paths.get("instances/uzf/libraries-15.uzf");
            case 2 -> Paths.get("instances/uzf/carparks-40.uzf");
			case 3 -> Paths.get("instances/uzf/tramstops-85.uzf");
			case 4 -> Paths.get("instances/uzf/grid.uzf");
			case 5 -> Paths.get("instances/uzf/clustered-enclosures.uzf");
            case 6 -> Paths.get("instances/uzf/chatgpt-instance-100-enclosures.uzf");
            default -> throw new IllegalArgumentException("Invalid instance ID: " + instanceId);
        };
        instance = reader.readUZFInstance(path, rng);

		//  here might be a good place to set the objective function within each low-level heuristic

	}

	@Override
	public void setMemorySize(int size) {
		UAVSolutionInterface[] newSolutionMemory = new UZFSolution[size];
		if (this.solutionMemory != null) {
			for(int x = 0; x < this.solutionMemory.length; ++x) {
				if (x < size) {
					newSolutionMemory[x] = this.solutionMemory[x];
				}
			}
		}

		this.solutionMemory = newSolutionMemory;
	}

	@Override
	public String solutionToString(int index) {
		StringBuilder s = new StringBuilder();
		int [] solution = this.solutionMemory[index].getSolutionRepresentation().getSolutionRepresentation();
		s.append("Solution ").append(index).append(":\n");
		for(int i = 0; i < this.solutionMemory[index].getNumberOfLocations() - 1; ++i) {
			s.append(solution[i]).append(" ");
		}
		s.append("Objective Function Value: ").append(this.solutionMemory[index].getObjectiveFunctionValue()).append("\n");

		return s.toString();
	}

	@Override
	public String toString() {
		Location foodPreparationLocation = instance.getLocationOfFoodPreparationArea();
		StringBuilder s = new StringBuilder();
		s.append("UZF Instance:\n");
		s.append("Number of Locations: ").append(instance.getNumberOfLocations()).append("\n");
		s.append("Food Preparation Location: ").append(foodPreparationLocation.x()).append(", ").append(foodPreparationLocation.y()).append("\n");
		s.append("Locations:\n");

		for(int i = 0; i < instance.getNumberOfLocations(); ++i) {
			Location location = instance.getLocationForEnclosure(i);
			s.append(i).append(": ").append(location.x()).append(", ").append(location.y()).append("\n");
		}

		return s.toString();
	}
	
	private void updateBestSolution(int index) {
		// make sure we cannot modify the best solution accidentally after storing it!
		if (bestEverSolution == null || solutionMemory[index].getObjectiveFunctionValue() < getBestSolutionValue()) {
			bestEverSolution = solutionMemory[index].clone();
			System.out.println("Better solution found: " + bestEverSolution.getObjectiveFunctionValue());
		}
	}
	
	@Override
	public UZFInstanceInterface getLoadedInstance() {
		return instance;
	}

	/**
	 * @return The integer array representing the ordering of the best solution.
	 */
	@Override
	public int[] getBestSolutionRepresentation() {
		return bestEverSolution.getSolutionRepresentation().getSolutionRepresentation();
	}

	/**
	 * @return The ordering of the best solution as an array of Location objects.
	 */
	@Override
	public Location[] getRouteOrderedByLocations() {
		Location[] route = new Location[bestEverSolution.getNumberOfLocations() + 1];
		route[0] = instance.getLocationOfFoodPreparationArea();

		int[] solution = bestEverSolution.getSolutionRepresentation().getSolutionRepresentation();
		for(int i = 0; i < solution.length; ++i) {
			route[i + 1] = instance.getLocationForEnclosure(solution[i]);
		}

		route[route.length - 1] = instance.getLocationOfFoodPreparationArea();
		return route;
	}

	public UAVSolutionInterface getBestSolution() {
		return bestEverSolution;
	}
}
