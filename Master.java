import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Master {
	
	private static final String SPLITTER = "\t|\t";

	Environment[] e;
	
	int top10Perc;
	int gen;
	
	//	      ID	 Fitness
	HashMap<Integer, Double> database;
	HashMap<Integer, Double> top10Data;
	
	
	public Master(Environment[] e, int gen) throws IOException {
		this.e = e;
		this.gen = gen;
		//top 10%
		top10Perc = (int)(0.5 * (e.length));
		
		database = new LinkedHashMap<>();
		for (int i = 0; i < e.length; i++) {
			database.put(e[i].getOrganism().getID(), e[i].getOrganism().getFitness());
			//also put in the previous gen's organisms from data.out
			readInPrevGen();
		}
		
		Stream<Map.Entry<Integer, Double>> sorted =
			    database.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()));
	
		top10Data = sorted.limit(top10Perc).collect(Collectors.toMap(
		          Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
	}


	private void readInPrevGen() throws IOException {
		// TODO Auto-generated method stub
		FileReader fr = new FileReader("data.out");
		Scanner scn = new Scanner(fr);
		
		if (gen == 0) {
			PrintWriter writer = new PrintWriter("data.out");
			writer.print("");
			writer.close();
			return;
		} else {
			scn.nextLine(); // first line is generation.
		}
		
		while (scn.hasNext()) {
			String ln = scn.nextLine();
			String[] sp = ln.split(SPLITTER);
			int id = Integer.valueOf(sp[0]);
			database.put(id, Organism.findByID(id).getFitness());
		}
	}


	/**
	 * Pushes the contents of top10Data into a text document so that Chromosome can make any necessary mutations appropriately
	 * @throws IOException 
	 */
	public void pushSurvivorsData() throws IOException {
		// TODO Auto-generated method stub
		String[] arr = new String[top10Data.size()];
		
		FileWriter fw = new FileWriter("data.out", false); //overwrite existing data
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw);
		
		out.println("g" + gen  + "; id " + SPLITTER + " body " + SPLITTER + " fitness ");
		int i = 0;
		boolean exit = false;
		for (Map.Entry<Integer, Double> item : top10Data.entrySet()) {
			int id = item.getKey();
			double fitness = item.getValue();
			Organism o = Organism.findByID(id);
			out.println(o.getID() + SPLITTER + o.getBody() + SPLITTER + o.getFitness());
			arr[i] = o.getBody();
			if (fitness == 0.0) exit = true;
			i++;
		}
		out.close();
		
		System.out.println(Arrays.toString(arr));
		
		Chromosome.updateRecentGen(arr);
		Chromosome.updateTop10Data(top10Data);
		
		System.out.println("done");
		if (exit) 
			System.exit(0);
	}
}
