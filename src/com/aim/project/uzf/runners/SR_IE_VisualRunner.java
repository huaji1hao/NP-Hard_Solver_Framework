package com.aim.project.uzf.runners;


import com.aim.project.uzf.hyperheuristics.MCF_HH;
import com.aim.project.uzf.hyperheuristics.SR_IE_HH;

import AbstractClasses.HyperHeuristic;
import com.aim.project.uzf.hyperheuristics.TB_IE_HH;

/**
 * @author Warren G Jackson
 * @since 1.0.0 (22/03/2024)
 *
 * Runs a simple random IE hyper-heuristic then displays the best solution found
 */
public class SR_IE_VisualRunner extends HH_Runner_Visual {

	public SR_IE_VisualRunner(int instanceId) {
		super(instanceId);
	}
	@Override
	protected HyperHeuristic getHyperHeuristic(long seed) {

//		return new TB_IE_HH(seed);
//		return new SR_IE_HH(seed);
		return new MCF_HH(seed);
	}
	
	public static void main(String [] args) {
		HH_Runner_Visual runner = new SR_IE_VisualRunner(6);
		runner.run();
	}

}
