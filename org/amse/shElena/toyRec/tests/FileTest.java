package org.amse.shElena.toyRec.tests;

import java.io.File;

import org.amse.shElena.toyRec.algorithms.SimpleComparisonAlgorithm;
import org.amse.shElena.toyRec.manager.IManager;
import org.amse.shElena.toyRec.manager.Manager;
import org.amse.shElena.toyRec.sampleBase.ISampleBase;
import org.amse.shElena.toyRec.sampleBase.SampleBase;

import junit.framework.TestCase;

/**
 * Tests all breakdown cases in working with files
 * 
 * Tested classes are SampleBase and Manager.
 * 
 */
public class FileTest extends TestCase {
	/**
	 * SampleBase - load unexisting file
	 */
	public void test0() {
		ISampleBase b = new SampleBase(12);
		boolean ex = false;
		try {
			b.loadSampleBase(new File("Vasya"));
		} catch (RuntimeException e) {
			ex = true;
		}
		assertTrue(ex);
	}

	/**
	 * SampleBase - load unsuitable file
	 */
	public void test1() {
		ISampleBase b = new SampleBase(12);
		boolean ex = false;
		try {
			b.loadSampleBase(new File(
					"org/amse/shElena/toyRec/tests/testFiles/1.txt"));
		} catch (RuntimeException e) {
			ex = true;
		}
		assertTrue(ex);
	}

	/**
	 * ISampleBase - load unsuitable file - wrong sample length
	 */
	public void test2() {
		SampleBase b = new SampleBase(12);// 35 is right length
		boolean ex = false;
		try {
			File f = new File("org/amse/shElena/toyRec/tests/testFiles/2.sb");
			b.loadSampleBase(f);
		} catch (RuntimeException e) {
			if (e.getMessage().equals(
					"Symbol base wasn`t loaded: wrong size of samples.")) {
				ex = true;
			}
		}
		assertTrue(ex);
	}

	/**
	 * SampleBase - load unsuitable file - wrong symbol in a sample
	 */
	public void test3() {
		ISampleBase b = new SampleBase(35);// 35 is right length
		boolean ex = false;
		try {
			File f = new File("org/amse/shElena/toyRec/tests/testFiles/3.sb");
			b.loadSampleBase(f);
		} catch (RuntimeException e) {
			if (e
					.getMessage()
					.equals(
							"Symbol base wasn`t loaded: file contains forbidden symbol.")) {
				ex = true;
			}
		}
		assertTrue(ex);
	}

	/*
	 * SampleBase tests are over.
	 */

	/**
	 * Manager - load unexisting file
	 */
	public void test4() {
		IManager m = new Manager(2, 2, SimpleComparisonAlgorithm.getInstance());
		boolean ex = false;
		try {
			m.createFileSymbolBase(new File("Vasya"));
		} catch (RuntimeException e) {
			ex = true;
		}
		assertTrue(ex);

		ex = true;
		try {
			m.createPictureSymbolBase(new File("Vasya"));
		} catch (RuntimeException e) {
			ex = false;
		}
		assertTrue(ex);
	}

	/**
	 * Manager - load unsuitable file
	 */
	public void test5() {
		IManager m = new Manager(2, 2, SimpleComparisonAlgorithm.getInstance());
		boolean ex = false;
		try {
			m.createFileSymbolBase(new File(
					"org/amse/shElena/toyRec/tests/testFiles/1.txt"));
		} catch (RuntimeException e) {
			ex = true;
		}
		assertTrue(ex);

		ex = true;
		try {
			m.createPictureSymbolBase(new File(
					"org/amse/shElena/toyRec/tests/testFiles/1.txt"));
		} catch (RuntimeException e) {
			ex = false;
		}
		assertTrue(ex);
	}

	/**
	 * Manager - load unsuitable file - wrong sample length
	 */
	public void test6() {
		IManager m = new Manager(2, 2, SimpleComparisonAlgorithm.getInstance());
		boolean ex = false;
		try {
			m.createFileSymbolBase(new File(
					"org/amse/shElena/toyRec/tests/testFiles/2.sb"));
		} catch (RuntimeException e) {
			if (e.getMessage().equals(
					"Symbol base wasn`t loaded: wrong size of samples.")) {
				ex = true;
			}
		}
		assertTrue(ex);
	}

	/**
	 * Manager - load unsuitable file - wrong sample length
	 */
	public void test7() {
		IManager m = new Manager(5, 7, SimpleComparisonAlgorithm.getInstance());
		boolean ex = false;
		try {
			m.createFileSymbolBase(new File(
					"org/amse/shElena/toyRec/tests/testFiles/3.sb"));
		} catch (RuntimeException e) {
			if (e
					.getMessage()
					.equals(
							"Symbol base wasn`t loaded: file contains forbidden symbol.")) {
				ex = true;
			}
		}
		assertTrue(ex);
	}
}
