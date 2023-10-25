package syslab.cloudcomputing.utils;

import java.util.Random;

public class Utilities {
  /*
	 * Generate a random Integer in range [min, max)
	 * @param The minimum value to take inclusively
	 * @param The maximum value to take exclusively
	 */
	public static Integer getRandomInteger(int min, int max) {
		Random ran = new Random();
		return ran.nextInt(max - min) + min;
	}
	
	/*
	 * Generate a random Double in range [min, max)
	 * @param The minimum value to take inclusively
	 * @param The maximum value to take exclusively
	 */
	public static Double getRandomDouble(double min, double max) {
		return Math.random() * (max - min) + min;
	}
}
