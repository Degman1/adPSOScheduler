package syslab.cloudcomputing;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

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
	public static Double getRandomDouble(int min, int max) {
		return Math.random() * (max - min) + min;
	}

  /**
	 * Transforms a set to an ArrayList
	 * @param set The input set
	 * @return The ArrayList object containing all items from the input set
	 */
	public static <T> ArrayList<T> setToList(Set<T> set) {
		ArrayList<T> list = new ArrayList<>();
		list.addAll(set);
		return list;
	}

	public static ArrayList<Double> addLists(ArrayList<Double> l1, ArrayList<Double> l2) {
		ArrayList<Double> res = new ArrayList<Double>();
		for (int i = 0; i < min(l1.size(), l2.size()); i++) {
			res.add(l1.get(i) + l2.get(i));
		}
	}

	public static ArrayList<Double> subtractLists(ArrayList<Double> l1, ArrayList<Double> l2) {
		ArrayList<Double> res = new ArrayList<Double>();
		for (int i = 0; i < min(l1.size(), l2.size()); i++) {
			res.add(l1.get(i) - l2.get(i));
		}
	}

	public static ArrayList<Double> multiplyLists(ArrayList<Double> l1, ArrayList<Double> l2) {
		ArrayList<Double> res = new ArrayList<Double>();
		for (int i = 0; i < min(l1.size(), l2.size()); i++) {
			res.add(l1.get(i) * l2.get(i));
		}
	}

	public static ArrayList<Double> divideLists(ArrayList<Double> l1, ArrayList<Double> l2) {
		ArrayList<Double> res = new ArrayList<Double>();
		for (int i = 0; i < min(l1.size(), l2.size()); i++) {
			res.add(l1.get(i) / l2.get(i));
		}
	}
}
