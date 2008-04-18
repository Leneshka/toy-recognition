package org.amse.shElena.toyRec.algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.amse.shElena.toyRec.sampleBase.ISampleBase;

public class OptimalAlgorithm extends Algorithm {
	private  OptimalAlgorithm() {
	}

	private static Algorithm myInstance = new  OptimalAlgorithm();

	public static Algorithm getInstance() {
		return myInstance;
	}


	public void learnBase(ISampleBase base) {
		SimpleComparisonAlgorithm.getInstance().learnBase(base);
		ClassComparisonAlgorithm.getInstance().learnBase(base);
	}

	public List<ComparisonResult> getWholeResults(boolean[] picture) {
		List<ComparisonResult> sim = SimpleComparisonAlgorithm.getInstance().getWholeResults(picture);
		List<ComparisonResult> cl = ClassComparisonAlgorithm.getInstance().getWholeResults(picture);
		
		List<ComparisonResult> res = new ArrayList<ComparisonResult>(sim.size());
		Map<Character, Integer> classDif = new HashMap<Character, Integer>();
		
		for(ComparisonResult r: cl){
			classDif.put(r.getSymbol(), r.getDifference());
		}
		
		for(ComparisonResult r: sim){
			res.add(new ComparisonResult(r.getSymbol(), (r.getDifference() + classDif.get(r.getSymbol()))/2));
		}
		
		return res;	
	}
	

	public String toString() {
		return "Optimal algorithm";
	}
}
