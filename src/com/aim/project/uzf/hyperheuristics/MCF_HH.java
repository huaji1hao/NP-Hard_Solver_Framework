package com.aim.project.uzf.hyperheuristics;

import com.aim.project.uzf.UZFDomain;
import com.aim.project.uzf.SolutionPrinter;
import com.aim.project.uzf.interfaces.UAVSolutionInterface;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;

import java.util.Arrays;

/**
 * Implements a Modified Choice Function (MCF) Hyper-Heuristic which uses a hybrid approach
 * of different heuristic types to solve optimization problems across multiple domains.
 * The hyper-heuristic selects heuristics based on a dynamic scoring mechanism, incorporating
 * crossover heuristics periodically and managing a pool of elite solutions.
 *
 * <p> The MCF hyper-heuristic operates by dynamically adjusting the scores of heuristics based on their
 * performance, frequency of use, and synergistic effects when used in sequence. The goal is to balance
 * exploration and exploitation using an adaptive memory mechanism and crossover operations
 * to generate robust solutions across various problem landscapes.
 *
 * <p> This class maintains a memory of elite solutions and manages crossover operations at a high level,
 * providing a framework for experimenting with crossover in combination with other heuristic types.
 *
 * @author John H. Drake, Ender Ozcan, Edmund K. Burke
 * @programmer Junfeng ZHU
 */
public class MCF_HH extends HyperHeuristic {
    private static final int SECOND_PARENT_INDEX = 2;
    private static final int BEST_ACCEPTED_INDEX = 3; // start index of elite solutions
    private final int bestSolutionNum = 5;  // number of elite solutions to maintain

    private int[] f1;  // heuristic success frequency
    private int[][] f2; // pairwise heuristic success matrix
    private int[] f3;   // time since last use of heuristic
    private int prevHeuristic = 0; // index of the last heuristic used

    /**
     * Constructs a new MCF hyper-heuristic using the provided seed for randomization.
     * This constructor initializes the hyper-heuristic with a specified random seed
     * to ensure reproducible results.
     *
     * @param lSeed The seed for randomization.
     */
    public MCF_HH(long lSeed) {
        super(lSeed);
    }

    /**
     * Solves the optimization problem using the Modified Choice Function Hyper-Heuristic.
     * This method orchestrates the selection and application of heuristics over the course of the search,
     * managing crossover and maintaining a pool of elite solutions to enhance the search process.
     *
     * @param oProblem The problem domain to be optimized, encapsulated in a {@link ProblemDomain} object.
     */
    @Override
    protected void solve(ProblemDomain oProblem) {
        // set memory size to store elite solutions
        oProblem.setMemorySize(4 + bestSolutionNum);

        int currentIndex = 0;
        int candidateIndex = 1;
        oProblem.initialiseSolution(currentIndex);
        for(int i = 0; i < bestSolutionNum; i++) {
            oProblem.copySolution(currentIndex, BEST_ACCEPTED_INDEX + i);
        }

        double currentCost = oProblem.getFunctionValue(currentIndex);
        int numberOfHeuristics = oProblem.getNumberOfHeuristics();

        // initialise MCF function arrays
        f1 = new int[numberOfHeuristics];
        f2 = new int[numberOfHeuristics][numberOfHeuristics];
        f3 = new int[numberOfHeuristics];

        // cache indices of crossover heuristics
        boolean[] isCrossover = new boolean[numberOfHeuristics];
        Arrays.fill(isCrossover, false);

        for(int i : oProblem.getHeuristicsOfType(ProblemDomain.HeuristicType.CROSSOVER)) {
            isCrossover[i] = true;
        }

        double candidateCost;
        int xoCounter = 0;
        // main search loop
        while(!hasTimeExpired()) {
            int h = getChoiceHeuristic();
            // binary heuristic
            if(isCrossover[h]) {
                xoCounter++;
                // apply reinitialisation with a probability of 1/bestSolutionNum
                if(xoCounter % bestSolutionNum == 0) {
                    oProblem.initialiseSolution(SECOND_PARENT_INDEX);
                    candidateCost = oProblem.applyHeuristic(h, currentIndex, SECOND_PARENT_INDEX, candidateIndex);
                } else {
                    // or apply crossover with a random elite solution
                    candidateCost = oProblem.applyHeuristic(h, currentIndex, BEST_ACCEPTED_INDEX + rng.nextInt(bestSolutionNum), candidateIndex);
                }
            } else {
                // unary heuristic
                candidateCost = oProblem.applyHeuristic(h, currentIndex, candidateIndex);
            }

            // update best
            if(candidateCost < currentCost) {
                int worstIndex = getWorstBestSolutionIndex(oProblem);
                oProblem.copySolution(candidateIndex, worstIndex);
                // update the score of the heuristic
                updateHeuristic(h, true);
            } else {
                updateHeuristic(h, false);
            }

            // accept improving or equal moves
            if(candidateCost <= currentCost) {
                currentCost = candidateCost;
                currentIndex = 1 - currentIndex;
                candidateIndex = 1 - candidateIndex;
            }
            // update the last heuristic used
            prevHeuristic = h;
        }

        UAVSolutionInterface oSolution = ((UZFDomain) oProblem).getBestSolution();
        SolutionPrinter oSolutionPrinter = new SolutionPrinter("out.csv");
        oSolutionPrinter.printSolution(((UZFDomain) oProblem).getLoadedInstance().getSolutionAsListOfLocations(oSolution));
    }

    /**
     * Returns the index of the worst elite solution in the pool of best solutions.
     * This method calculates the index of the worst elite solution in the pool of best solutions
     * based on the objective function value of each solution.
     *
     * @param oProblem The problem domain to be optimized, encapsulated in a {@link ProblemDomain} object.
     * @return The index of the worst elite solution in the pool of best solutions.
     */
    private int getWorstBestSolutionIndex(ProblemDomain oProblem) {
        int worstIndex = BEST_ACCEPTED_INDEX;
        double worstCost = oProblem.getFunctionValue(worstIndex);

        for (int i = 1; i < bestSolutionNum; i++) {
            int currentIndex = BEST_ACCEPTED_INDEX + i;
            double currentCost = oProblem.getFunctionValue(currentIndex);

            if (currentCost > worstCost) {
                worstCost = currentCost;
                worstIndex = currentIndex;
            }
        }

        return worstIndex;
    }

    /**
     * Updates the Modified Choice Function arrays based on the success of a heuristic.
     * This method updates the Modified Choice Function arrays
     * based on the success of a heuristic,
     * adjusting the frequency of heuristic success, pairwise heuristic success matrix,
     * and time since last use of heuristic.
     *
     * @param index The index of the heuristic to update.
     * @param isImproving A boolean value indicating whether the heuristic is improving the solution.
     */
    private void updateHeuristic(int index, boolean isImproving){
        if(isImproving) {
            f1[index]++;
            f3[index] = 0;
            f2[index][prevHeuristic]++;
        } else {
            f1[index]--;
            f3[index] = 0;
            f2[index][prevHeuristic]--;
        }

        for(int i = 0; i < f1.length; i++) {
            if(i != index) {
                f3[i]++;
            }
        }

    }

    /**
     * Returns the index of the heuristic to apply based on the Modified Choice Function.
     * This method calculates the index of the heuristic to apply based on the Modified Choice Function,
     * using a dynamic scoring mechanism to select the most promising heuristic.
     *
     * @return The index of the heuristic to apply.
     */
    private int getChoiceHeuristic() {
        int bestIndex = 0;
        double bestScore = getHeuristic(0);

        for (int i = 1; i < f1.length; i++) {
            double currentScore = getHeuristic(i);
            if (currentScore > bestScore) {
                bestScore = currentScore;
                bestIndex = i;
            }
        }

        return bestIndex;
    }

    /**
     * Returns the heuristic score based on the Modified Choice Function.
     * This method calculates the heuristic score based on the Modified Choice Function,
     * using a dynamic scoring mechanism to evaluate the performance of a heuristic.
     *
     * @param index The index of the heuristic to evaluate.
     * @return The heuristic score based on the Modified Choice Function.
     */
    private double getHeuristic(int index){
        // weight for f1 in heuristic score calculation
        double alpha = 0.8;
        // weight for f2
        double beta = 15;
        // weight for f3
        double gamma = 2000;
        return alpha * f1[index] + beta * f2[index][prevHeuristic] + gamma * f3[index];
    }

    @Override
    public String toString() {
        return "MCF_HH";
    }
}
