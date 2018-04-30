package src;

import java.io.*;
import java.util.Scanner;


public class GeneBankSearch{
  //TODO driver class to perform search on a user provided BTree file
	private static boolean cacheFlag = false;
	private static int cacheArg, cacheSize, debugLevel = 0;
	private static String bTFileName, queryFileName;
	//args format:  <0/1(no/with Cache)> <btree file> <query file> [<cache size>] [<debug level>]
	public static void main(String[] args) throws FileNotFoundException{
		int degree,sequence;
		String sequenceString = "", degreeString = "";
		//file names

		File bTree, query;
		
		
		if ((args.length == 0) || (args.length < 3 || args.length > 5)) { //verify correct amount of args
			printUsage();
			System.exit(1);
		}else {
			try {
				//check argsLength
				if (args.length == 5) {
					cacheSize = Integer.parseInt(args[3]);
					debugLevel = Integer.parseInt(args[4]);
				}else if (args.length != 3){
					printUsage();
				}

				//handle arg 0
				cacheArg =Integer.parseInt(args[0]);
				if(cacheArg == 1)
					cacheFlag = true;

				//handle file args
				bTFileName = args[1];
				bTree = new File(bTFileName);
				queryFileName = args[2];
				query = new File(queryFileName);



				//grab char in file name for degree and sequence
				for(int i = bTFileName.length()-1; i >=0; i--){
					if(bTFileName.charAt(i) != '.') degreeString += bTFileName.charAt(i);
					else break;
				}
				for(int i = bTFileName.length()-degreeString.length() - 2; i >=0; i--){
					if(bTFileName.charAt(i) != '.') sequenceString += bTFileName.charAt(i);
					else break;
				}
				//reverse if there were more than one digits in either degree or sequence length
				degreeString = reverse(degreeString);
				sequenceString = reverse(sequenceString);
				degree = Integer.parseInt(degreeString);
				sequence = Integer.parseInt(sequenceString);
				RAM ram = new RAM();

				BTree tree = new BTree(bTree, sequence, degree, cacheFlag, cacheSize);

				try {
                    Scanner sc = new Scanner(query);
                    while(sc.hasNext())
                    {
                        String line = sc.nextLine();
                        long data = ram.convertGBKtoSubseq(line);
                        TreeObject x = tree.BTreeSearch(tree.root, data);
                        if (x != null){
                            System.out.println(line + ": " + x.frequency);
                        }

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
				
			}
			catch (Exception err){
				System.err.println("Something went wrong in GeneBankSearch");
				err.printStackTrace();
			}

		}

	}
	
	public static void printUsage() {
		System.out.println("Expected Format for arguments: <0/1(no/with Cache)> <btree file> <query file> [<cache size>] [<debug level>]");
		System.exit(1);
	}
	public static String reverse(String x){
		if (x.length() == 1) return x;
		else return "" + x.charAt(x.length()-1) +  reverse(x.substring(0, x.length()-1));
	}
}