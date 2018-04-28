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

				//handling degree and sequence length ("xyz.gbk.btree.data.k.t") t is degree and k is seq length
				degreeString += bTFileName.charAt(bTFileName.length()-1);
				degree = Integer.parseInt(degreeString);
				sequenceString += bTFileName.charAt(bTFileName.length()-3);
				sequence = Integer.parseInt(sequenceString);


//				//TODO testing RAM class 4/17/18
//				String tempForPrint = "";
//				String tempDataString = "G"; //0x00101101
				RAM ram = new RAM();

				BTree tree = new BTree(sequence, degree, bTFileName, cacheFlag, cacheSize);
				//TODO Trying to work on the conversions, keep getting ("a" * how much the inputLength is)
				//long data = ram.convertGBKtoSubseq(tempDataString);
				//tempForPrint = ram.convertLongtoString(data,1);

				//System.out.println(tempForPrint);
				try {
					Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("dump"), "utf-8"));



				Scanner sc = new Scanner(query);


				while(sc.hasNext())
				{
					String line = sc.nextLine();
					long data = ram.convertGBKtoSubseq(line);
					//tempForPrint = ram.convertLongtoString(data,4);
					TreeObject x = tree.BTreeSearch(tree.root, data);
					if (x != null){
						writer.write(line + ":" + '\t' + x.frequency);
					}
				}

//				if(debugLevel == 0){
//					tree.inOrderWriteFile(writer, );
//				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

				
			}
			catch (Exception err){
				System.err.println("Something went wrong in GeneBankSearch");
			}

		}

	}
	
	public static void printUsage() {
		System.out.println("Expected Format for arguments: <0/1(no/with Cache)> <btree file> <query file> [<cache size>] [<debug level>]");
		System.exit(1);
	}
}