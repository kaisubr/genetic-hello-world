import java.util.ArrayList;
import java.util.List;

public class Organism {
	private String res;
	private int id;
	private double fitness;
	private static List<Organism> history = new ArrayList<>();
	
	public Organism(int gen, int id) {
		
		res = Chromosome.globalCrossover(gen);
		res = Chromosome.quickMutate(res);
		
		//fitness = Chromosome.getPercentRight(res);
		fitness = Chromosome.getFitness(res);
		this.id = id;
		System.out.println("Gen " + gen + ", org " + id + ": " + res);
		history.add(this);
	}
	
	public int getID() {
		return id;
	}
	
	public double getFitness() {
		return fitness;
	}
	
	public static Organism findByID(int id) {
		return history.get(id);
	}

	public String getBody() {
		// TODO Auto-generated method stub
		return res;
	}
}
