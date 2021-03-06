package edu.kit.aquaplanning.sat;

import java.util.Arrays;

import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;

public class SatSolver {
	
	protected ISolver solver;
	protected int[] model;
	
	public SatSolver() {
		solver = SolverFactory.newDefault();
		model = null;
	}

	/**
	 * Add a clause which is an array of integers, a positive (negative) integer 
	 * represents a positive (negative) literal. Variables are numbered starting
	 * with 1 and NOT ZERO!
	 * @param clause
	 * @return
	 */
	public boolean addClause(int[] clause) {
		try {
			solver.addClause(new VecInt(clause));
		} catch (ContradictionException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * Set the time limit for each individual solve call
	 * @param seconds
	 */
	public void setSolverTimeLimit(int seconds) {
		solver.setTimeout(seconds);
	}
	
	/**
	 * Return true if the formula specified by the addClause calls is satisfiable under
	 * the given assumptions and false it is unsatisfiable. Return null in case of the
	 * time limit is reached.
	 * @param assumptions list of temporary unit clauses that hold only for this call
	 * @return
	 */
	public Boolean isSatisfiable(int[] assumptions) {
		try {
			int[] s4jModel = solver.findModel(new VecInt(assumptions));
			if (s4jModel == null) {
				model = null;
				return false;
			} else {
				// transform the model into a more convenient format
				model = new int[solver.nVars()+1];
				Arrays.fill(model, 0);
				for (int lit : s4jModel) {
					model[Math.abs(lit)] = lit;
				}
				return true;
			}
		} catch (TimeoutException e) {
			return null;
		}
	}
	
	/**
	 * Return true if the formula specified by the addClause calls is satisfiable
	 * false it is unsatisfiable. Return null in case of the time limit is reached.
	 * @return
	 */
	public Boolean isSatisfiable() {
		return isSatisfiable(new int[] {});
	}
	
	/**
	 * Get the truth values of the satisfying assignment if it exists.
	 * getModel()[var] is +var if true and -var if false
	 * @return
	 */
	public int[] getModel() {
		return model;
	}
	

}
