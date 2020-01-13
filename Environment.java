
public class Environment {
	public Organism o;
	public int gen;
	public int orgID;
	public Environment(int gen, int orgID) {
		//an environment is a world.
		
		this.gen = gen;
		this.orgID = orgID;
		o = new Organism(gen, orgID);
	}
	
	public Organism getOrganism() {
		return o;
	}
}
