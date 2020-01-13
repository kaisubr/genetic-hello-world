import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Stat {
	
	public static double newRandomMultiplier() {
		return (Math.random() < 0.5)? (Math.random() + 1) : (Math.random() - 1);
	}
	
	public static int posOrNeg() {
		return (Math.random() < 0.5)? (+1) : (-1);
	}
	
	/**
	 * i2 must be greater than i.
	 * @param i lower
	 * @param i2 upper
	 * @return random int between i and i2
	 */
	public static int randomBetween(int i, int i2) {
		if (i2 < i) throw new IndexOutOfBoundsException("the second parameter, " + i2 + ", must be greather than the first, " + i + ".");
		if (i2 == i) return i;
		
		return (int) (Math.random() * (i2 - i)) + i;
	}
	
	public static String getShorter(String p, String p2) {
		return (p.length() > p2.length())? (p2) : (p);
	}

	public static String getLonger(String p, String p2) {
		// TODO Auto-generated method stub
		return (p.length() > p2.length())? p : p2;
	}

	/**
	 * Based on a Roulette Wheel algorithm, this method chooses two parents for which 
	 * individuals with larger fitness ratings occupy a larger portion of the probability
	 * in the selection phase.
	 * 
	 * @param topTier the top 10% of the organisms from the latest generation
	 * @return two parents to be used in chromosomal crossover
	 */
	public static String[] rouletteWheel(HashMap<Integer, Double> topTier) {
		// TODO Auto-generated method stub
		String[] res = new String[2];
		
		int sum = 0;
		double leastFitness = Collections.min(topTier.values());
		
		for (Map.Entry<Integer, Double> i : topTier.entrySet()) {
			//System.out.println("old value " + i.getValue() + ", least fitness " + leastFitness);
			i.setValue(i.getValue() - leastFitness); //force least fitness to be 0 instead of a negative number
			//System.out.println("new value " + i.getValue());
			sum += i.getValue();
		}
		
		System.out.println("sum " + sum);
		
		for (int i = 0; i < 2; i++) {
			int randomLimit = Stat.randomBetween(0, sum);
			System.out.println("limit " + randomLimit);
			
			int partialSum = 0, organismID = -1;
			
			for (Map.Entry<Integer, Double> v : topTier.entrySet()) {
				partialSum += v.getValue();
				if (partialSum >= randomLimit) {
					organismID = v.getKey();
					System.out.println("partial sum of " + partialSum + " exceeds it; orgid " + organismID + " wbod " + Organism.findByID(organismID).getBody());
					break;
				}
			}
			
			res[i] = Organism.findByID(organismID).getBody();
		}
		
		
		return res;
	}
}
