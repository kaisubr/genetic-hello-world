import java.util.Arrays;
import java.util.HashMap;

public class Chromosome {
	public static final String OPTIMAL_STRING = "Hello, World!";
	public static final int OPTIMAL_LENGTH = OPTIMAL_STRING.length();
	public static final String[] keys = new String[] {" ", "q","w","e","r","t","y","u","i","o","p","[","]","\"","1","2","3","4","5","6","7","8","9","0","-","=","`","a","s","d","f","g","h","j","k","l",";","'","/",".",",","m","n","b","v","c","x","z","Q","W","E","R","T","Y","U","I","O","P","A","S","D","F","G","H","J","K","L","Z","X","C","V","B","N","M","~","!","@","#","$","%","^","&","*","(",")","_","+","{","}","|",":","\\","\"","<",">","?"};
	//public static final String[] keys = new String[] {"A", "G", "T", "C"};
	/**
	 * recentGen may only be modified by Master.
	 */
	private static String[] recentGen;
	private static HashMap<Integer, Double> top10Data;
	
	public static String globalCrossover(int prevGen) {
		if (prevGen == 0) {
			//first generation is completely random.
			return randomString();
		} else {
			//return randomWithBase( recentGen[(int) Math.round(Math.random() * (recentGen.length - 1))], recentGen[(int) Math.round(Math.random() * (recentGen.length - 1))] );
			int randomIndex = (int) Math.round(Math.random() * (recentGen.length - 1));
			String[] parents = Stat.rouletteWheel(top10Data);
			//String randomParent = recentGen[randomIndex];
			//String bestCorrParent = (randomIndex == (recentGen.length - 1))? (recentGen[randomIndex - 1]) : (recentGen[randomIndex + 1]);
			//return geneticCrossover(randomParent, bestCorrParent);
			return geneticCrossover(parents[0], parents[1]);
		}
	}

	public static String quickMutate(String res) {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder(res);
		
		int mutations = 0, maxMutate = Stat.randomBetween(1, 7), spikeMutate = 0, maxSpikeMutate = 0;
		
		for (int i = 0; i < res.length(); i++) {
			//mutations are limited to a ~8% probability
			if (Stat.randomBetween(0, 12) == 1) {//if ((i % Stat.randomBetween(1, 12) == 0)) {
				if (mutations > maxMutate) break;

				sb.setCharAt(i, (char) (res.charAt(i) + (Stat.randomBetween(1, 5) * Stat.posOrNeg()))); //random char above / below
				System.out.println("changing " +res.charAt(i) + " to " + sb.charAt(i) + " [mutation #" + mutations + "]");
				mutations++;
			}
			
			//random mutations are limited to a 2% probability.
			if (Stat.randomBetween(1, 50) == 1) {//(i % randomBetween(1, 24) == 0)) {
				if (spikeMutate > maxSpikeMutate) break;
				
				sb.setCharAt(i, keys[(int) Math.round(Math.random() * (keys.length - 1))].charAt(0)); //random char
				System.out.println("spike! changing " +res.charAt(i) + " to " + sb.charAt(i));
				mutations++;
				spikeMutate++;
			}
		}
		
		return sb.toString();
//		if (getFitness(sb.toString()) < getFitness(res.toString()))
//			return sb.toString();
//		else
//			return quickMutate(res);
	}
	
	@Deprecated
	private static String randomWithBase(String parent, String parent2) {
		// TODO Auto-generated method stub
		System.out.println(parent + " and " + parent2 + " combine!");
		StringBuilder sb = new StringBuilder();
		//int max = (int)Math.round((1.5 - Math.random()) * ((parent.length() + parent2.length())/2));
		//System.out.println("max length " + max);
		for (int i = 0; i < (1 - Math.random()) * (11); i++) {
			if (i % ((int)Math.random()*3 + 1) == 0) {
				try {
					sb.append(parent.charAt(i));
				} catch (Exception e) {
					//e.printStackTrace();
					try {
						sb.append(parent2.charAt(i));
					} catch (Exception e2) {
						//e2.printStackTrace();
						sb.append(keys[(int) Math.round(Math.random() * (keys.length - 1))]);
					}
				}
			} else {
				try {
					sb.append(parent2.charAt(i));
				} catch (Exception e) {
					//e.printStackTrace();
					try {
						sb.append(parent.charAt(i));
					} catch (Exception e2) {
						//e2.printStackTrace();
						sb.append(keys[(int) Math.round(Math.random() * (keys.length - 1))]);
					}
				}
			}
		}
		return sb.toString();
	}
	
	public static String geneticCrossover(String p, String p2) {
		System.out.println("\n\nCOMBINING ** " + p + " ** and ** " + p2 + " ** ; smaller lng = " + Stat.getShorter(p, p2));
		
		String shorter = Stat.getShorter(p, p2);
		String longer = Stat.getLonger(p, p2);
		
		int prevPoint = 0, newPoint = 0;
		boolean choosingP = true; //the smaller one
		StringBuilder child = new StringBuilder();
		
		for (int i = 0; i < shorter.length(); i++) {
			//crossovers are limited to a 25% probability.
			if (Stat.randomBetween(0, 4) == 1) {//if ((i % Stat.randomBetween(1, Stat.getShorter(shorter, "ABCDEF").length())) == 0) { //unfixed probability.
				//new crossover point.
				System.out.println("\ncrossover point " + i);
				
				newPoint = i;
				
				for (int j = prevPoint; j <= newPoint; j++) {
					if (choosingP) {
						child.append(p.charAt(j));
						System.out.print("append " + p.charAt(j) + " [s]; ");
					} else {
						child.append(p2.charAt(j));
						System.out.print("append " + p2.charAt(j) + " [l]; ");
					}
				}

				choosingP = !choosingP;
				prevPoint = newPoint;
			}
		}
		
		if (!choosingP) { //choosing the larger one now, but a crossover was not justified
			if (child.toString().length() < longer.length()) {
				child.append(longer.substring(child.toString().length()));
			}
		} else {
			if (child.toString().length() < shorter.length()) {
				child.append(shorter.substring(child.toString().length()));
			}
		}
		
		
		System.out.println("resultant: " + child.toString());
		
		return child.toString();
		
//		if (getFitness(child.toString()) < getFitness(p.toString()))
//			return quickMutate(p.toString());
//		else if (getFitness(child.toString()) < getFitness(p2.toString()))
//			return quickMutate(p2.toString());
//		else
//			return child.toString();
	
	}

	public static String randomString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < Math.round(Math.random() * 100); i++) {
			sb.append(keys[(int) Math.round(Math.random() * (keys.length - 1))]);
		}
		return sb.toString();
	}

	/**
	 * 
	 * @param str
	 * @return percent correct (fitness)
	 * @deprecated see the new updated getFitness();
	 */
	public static double getPercentRight(String str) {
		double matches = 0;
		for (int i = 0; i < OPTIMAL_LENGTH; i++) {
			if (i < str.length() && str.split("")[i].toUpperCase().equals(OPTIMAL_STRING.split("")[i].toUpperCase())) {
				matches++;
			}
			if (i < str.length() && OPTIMAL_STRING.contains(str.split("")[i])) {
				matches += 0.75;
			}
		}
		
		double curScore = (0.5 * (matches)/(OPTIMAL_LENGTH)) + (0.5 * (str.length())/(OPTIMAL_LENGTH));
		
		System.out.println(curScore);
		
		for (int i = OPTIMAL_LENGTH; i < str.length(); i++) {
			//extra characters are counted against you.
			curScore -= 0.1;
		}
		
		System.out.println("new cur score adjusted to " + curScore);
		
		return curScore;
	}
	
	public static double getFitness(String str) {
		double charScore = 0;
		
		for (int i = 0; i < OPTIMAL_STRING.length(); i++) {
			if (i < str.length()) {
				int in = str.charAt(i);
				int req = OPTIMAL_STRING.charAt(i);
				double diff = Math.pow(in - req,2);

				//System.out.println(str.charAt(i) + " = " + in + " and " + OPTIMAL_STRING.charAt(i) + " = " + req);
				
				charScore -= diff;
			}
		}
		
		
		for (int j = OPTIMAL_LENGTH; j < str.length(); j++) {
			charScore -= Math.pow(str.charAt(j) - 0, 2);
			
			//System.out.println(str.charAt(j) + " = " + ((int)str.charAt(j)) + " is an extra ");
		}
		
		for (int k = str.length(); k < OPTIMAL_LENGTH; k++) {
			charScore -= Math.pow(OPTIMAL_STRING.charAt(k), 2);
			
			//System.out.println(OPTIMAL_STRING.charAt(k) + " = " + ((int)OPTIMAL_STRING.charAt(k)) + " is lacking ");
		}
		
		
		//double lenScore = -1 * Math.abs(5000 - (5000 * Math.abs(OPTIMAL_LENGTH - str.length())/OPTIMAL_LENGTH));
		
		//System.out.println("charScore = " + charScore + "; lenScoe = " + lenScore);
		
		//max score is 0.
		return charScore;// + lenScore;
	}
	
	public static void updateRecentGen(String[] newRecent) {
		recentGen = newRecent;
	}

	public static void updateTop10Data(HashMap<Integer, Double> newTop10Data) {
		// TODO Auto-generated method stub
		top10Data = newTop10Data;
	}
}
