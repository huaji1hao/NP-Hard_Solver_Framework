package com.aim.project.uzf.hyperheuristics;

import com.aim.project.uzf.UZFDomain;
import com.aim.project.uzf.SolutionPrinter;
import com.aim.project.uzf.interfaces.UAVSolutionInterface;
import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;
import java.util.*;

/**
 * The tabu-based hyper-heuristic uses a tabu list to store recently applied heuristics.
 * Use simulated annealing to accept worse solutions with a probability based on the temperature.
 */
public class TB_IE_HH extends HyperHeuristic {

    private final int tabuTenure;
    private final Map<Integer, Integer> tabuList = new HashMap<>();
    private double temperature = 10000;
    private static final int SECOND_PARENT_INDEX = 2;
    private static final int BEST_ACCEPTED_INDEX = 3;

    public TB_IE_HH(long seed) {
        super(seed);
        this.tabuTenure = 6; // Sets the duration that moves are kept in the tabu list.
    }

    @Override
    protected void solve(ProblemDomain oProblem) {
        oProblem.setMemorySize(4); // Sets memory size for problem domain to store solutions.

        int currentIndex = 0;
        int candidateIndex = 1;
        oProblem.initialiseSolution(currentIndex);
        oProblem.copySolution(currentIndex, BEST_ACCEPTED_INDEX);

        double candidateCost;
        double currentCost = oProblem.getFunctionValue(currentIndex);

        int numberOfHeuristics = oProblem.getNumberOfHeuristics();

        // Initialize boolean array for crossover heuristic identification.
        boolean[] isCrossover = new boolean[numberOfHeuristics];

        // Mark indices of crossover heuristics as true.
        for (int i : oProblem.getHeuristicsOfType(ProblemDomain.HeuristicType.CROSSOVER)) {
            isCrossover[i] = true;
        }

        while (!hasTimeExpired()) {
            int h = rng.nextInt(numberOfHeuristics); // Randomly select a heuristic.

            // Check if the heuristic is currently tabu; if so, skip this iteration.
            if (tabuList.containsKey(h)) {
                continue;
            }

//            updateTemperature(); // Update temperature for the cooling schedule.

            // Apply heuristic based on its type.
            if(isCrossover[h]) {
                if(rng.nextBoolean()) {
                    // Randomly decide to crossover with a newly initialized solution or the best solution so far.
                    oProblem.initialiseSolution(SECOND_PARENT_INDEX);
                    candidateCost = oProblem.applyHeuristic(h, currentIndex, SECOND_PARENT_INDEX, candidateIndex);
                } else {
                    candidateCost = oProblem.applyHeuristic(h, currentIndex, BEST_ACCEPTED_INDEX, candidateIndex);
                }
            } else {
                candidateCost = oProblem.applyHeuristic(h, currentIndex, candidateIndex);
            }

            // Check if the candidate solution is better or acceptable based on simulated annealing.
//            if(candidateCost < currentCost || rng.nextDouble() < calculateAcceptanceProbability(candidateCost, currentCost)) {
            if(candidateCost < currentCost) {
                currentCost = candidateCost;
                oProblem.copySolution(candidateIndex, BEST_ACCEPTED_INDEX); // Update best known solution.
                currentIndex = 1 - currentIndex; // Swap indices for current and candidate solutions.
                candidateIndex = 1 - candidateIndex;
                updateTabuList(h, tabuTenure); // Update tabu list with the heuristic.
            }

            // Clear the tabu list if necessary.
            if (shouldClearTabuList()) {
                tabuList.clear();
            }
        }

        // After the search, retrieve and print the best solution found.
        UAVSolutionInterface bestSolution = ((UZFDomain) oProblem).getBestSolution();
        SolutionPrinter solutionPrinter = new SolutionPrinter("out.csv");
        solutionPrinter.printSolution(((UZFDomain) oProblem).getLoadedInstance().getSolutionAsListOfLocations(bestSolution));
    }

    // Updates the tabu list by reducing tenure and removing expired entries.
    private void updateTabuList(int heuristic, int tenure) {
        tabuList.put(heuristic, tenure);
        tabuList.forEach((key, value) -> tabuList.put(key, value - 1));
        tabuList.values().removeIf(lifespan -> lifespan <= 0);
    }

    // Determines if the tabu list should be cleared based on size or random chance.
    private boolean shouldClearTabuList() {
        return tabuList.size() > 9 || rng.nextDouble() < 0.16;
    }

    // Calculates acceptance probability for simulated annealing.
    private double calculateAcceptanceProbability(double candidateCost, double currentCost) {
        // Simulated annealing acceptance probability function.
        // f(x) = e^((f(x) - f(x')) / T)
        return Math.exp((currentCost - candidateCost) / temperature);
    }

    // Implements a cooling schedule to gradually decrease the temperature.
    private void updateTemperature() {
//        double beta = 0.005;
//        temperature = temperature / (1 + beta * temperature);
        double alpha = 0.98;
        temperature *= alpha;
    }

    @Override
    public String toString() {
        return "Tabu Search HyperHeuristic";
    }
}
