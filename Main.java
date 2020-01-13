import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;

public class Main {

	static Environment[] e;
	public static final int ENVIRONMENT_SIZE = 100; //every world must have only 1 organism.
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//qwertyuiop[]\1234567890-=`asdfghjkl;'/.,mnbvcxzQWERTYUIOPASDFGHJKLZXCVBNM~!@#$%^&*()_+{}|:"<>?
		
		revokeAllSysOutRightAccess();
		
		String str = "qwertyuiop[]\\1234567890-=`asdfghjkl;'/.,mnbvcxzQWERTYUIOPASDFGHJKLZXCVBNM~!@#$%^&*()_+{}|:\\\"<>?";
//		for (int i = 0; i < str.length(); i++) {
//			System.out.print("\"" + str.charAt(i) + "\",");
//		}
		
		int id = 0;
		for (int gen = 0; gen < 7000; gen++) { //in reality generations are infinite.
			e = new Environment[ENVIRONMENT_SIZE];
			for (int org = 0; org < ENVIRONMENT_SIZE; org++) {
				e[org] = new Environment(gen, id++); //ID's are unique for every organism ever created
			}
			
			Master m = new Master(e, gen);
			m.pushSurvivorsData();
		}
		
		//System.out.println(Chromosome.getFitness(Chromosome.geneticCrossover(Chromosome.quickMutate("Gfmnp Vpsme"), Chromosome.quickMutate("Gfmnp Vpsme"))));
	}

	private static void revokeAllSysOutRightAccess() {
		// TODO Auto-generated method stub
		System.out.println("[!] Methods from the output stream are blocked, see Main.revokeAllSysOutRightAccess."); 
		
		System.setOut(new PrintStream(new OutputStream() {
		    public void write(int b) { /*overriden*/ }
		    public void close() {}
		    public void flush() {}
		    public void write(byte[] b) {}
		    public void write(byte[] b, int off, int len) {}
		}));

	}

}
