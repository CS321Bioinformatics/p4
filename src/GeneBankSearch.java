package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class GeneBankSearch{
  //TODO driver class to perform search on a user provided BTree file
	//args format:  <0/1(no/with Cache)> <btree file> <query file> [<cache size>] [<debug level>]
	public static void main(String[] args) throws FileNotFoundException{
		int cacheFlag, cacheSize, debugLevel;
		String bTFileName, queryFileName;
		File bTree, query;
		
		
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

				//TODO testing RAM class 4/17/18
				String tempForPrint = "";
				String tempDataString = "G"; //0x00101101
				RAM ram = new RAM();
				//TODO Trying to work on the conversions, keep getting ("a" * how much the inputLength is)
				long data = ram.convertGBKtoSubseq(tempDataString);
				tempForPrint = ram.convertLongtoString(data,1);

				System.out.println(tempForPrint);

//				Scanner sc = new Scanner(query);
//				while(sc.hasNext())
//				{,
//
//					String line = sc.nextLine();
//					long data = ram.convertGBKtoSubseq(line);
//					tempForPrint = ram.convertLongtoString(data,4);
//					System.out.println(data);
//				}


				
			}
			catch (Exception err){
				System.err.println("Something went wrong in GeneBankSearch");
			}

		}

	}
	
	public static void printUsage() {
		System.out.println("Expected Format for arguments: <0/1(no/with Cache)> <btree file> <query file> [<cache size>] [<debug level>]");
	}
}