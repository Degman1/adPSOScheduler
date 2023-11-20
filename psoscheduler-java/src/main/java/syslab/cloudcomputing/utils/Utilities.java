package syslab.cloudcomputing.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

public class Utilities {
	public static Random rand = new Random();

	public static void setSeed(long seed) {
		rand.setSeed(seed);
	}

  /*
	 * Generate a random Integer in range [min, max)
	 * @param The minimum value to take inclusively
	 * @param The maximum value to take exclusively
	 */
	public static Integer getRandomInteger(int min, int max) {
		if (min >= max) {
			return min;
		}
		return rand.nextInt(max - min) + min;
	}
	
	/*
	 * Generate a random Double in range [min, max)
	 * @param The minimum value to take inclusively
	 * @param The maximum value to take exclusively
	 */
	public static Double getRandomDouble(double min, double max) {
		if (min >= max) {
			return min;
		}
		return rand.nextDouble() * (max - min) + min;
	}

	public static void writeObjectiveHistoryToCSV(ArrayList<ArrayList<Double>> objectiveHistories, String path) {
		String output = "";

		for (ArrayList<Double> history : objectiveHistories) {
			for (int i = 0; i < history.size(); i++) {
				output += history.get(i);
				if (i < history.size() - 1) {
					output += '\t';
				}
			}

			output += "\n";
		}

		Path _path = Paths.get(path);

		try {
			Files.write(_path, output.getBytes());
		} catch (IOException ex) {
			ex.printStackTrace(System.err);
		}
	}
}
