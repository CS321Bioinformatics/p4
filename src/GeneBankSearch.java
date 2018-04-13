import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

package src;

public class GeneBankSearch{
  //TODO driver class to perform search on a user provided BTree file
	//args format:  <0/1(no/with Cache)> <btree file> <query file> [<cache size>] [<debug level>]
	public static void main(String[] args) throws FileNotFoundException{
		int cacheFlag, cacheSize, debugLevel;
		String bTFileName, queryFileName;
		File bTree, query
		
		
		if ((args.length == 0) || (args.length < 3 || args.length > 5)) { //verify correct amount of args
			printUsage();
			System.exit(1);
		}else {
			try {
				if (args.length == 5) {
					cacheSize = Integer.parseInt(args[3]);
					debugLevel = Integer.parseInt(args[4]);
				}
				
				cacheFlag = Integer.parseInt(args[0]);
				bTree = new File(args[1]);
				query = new File(args[2]);
				
				
				
			}
		}
		
	}
	
	public static void printUsage() {
		System.out.println("Expected Format for arguments: <0/1(no/with Cache)> <btree file> <query file> [<cache size>] [<debug level>]");
	}
}