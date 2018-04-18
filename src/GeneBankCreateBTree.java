package src;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;



public class GeneBankCreateBTree {
  //TODO Driver class to create a BTree file from a given gbk file	
	//args format: <0/1(no/with Cache)> <degree> <gbk file> <sequence length> [<cache size>] [<debug level>]
	public static void main(String[] args) {
		int cacheFlag, degree, seqLength, cacheSize, debugLevel;
		String gbkFileName;
		File gbk;
		Scanner scan;
		String line;
		
		if ((args.length == 0) || (args.length < 4 || args.length > 6)) { //verify correct amount of args
			printUsage();
			System.exit(1);
		}else {
			try {
				if (args.length == 6) {
					cacheSize = Integer.parseInt(args[4]);
					debugLevel = Integer.parseInt(args[5]);
				}
				
				cacheFlag = Integer.parseInt(args[0]);
				degree = Integer.parseInt(args[1]);
				seqLength = Integer.parseInt(args[3]);
				gbk = new File(args[2]);
				
//				BTreeNode tree = new BTreeNode();
				
				//Parse File
				scan = new Scanner(gbk);
				while(scan.hasNextLine()) {
					line = scan.nextLine();
					if(line.substring(0,5).equals("ORIGIN")) {
						int index = 0;
						while((index + seqLength) < line.length()) {
							String currString = line.substring(index,seqLength);
							boolean isValid = true;
							for(int i = 0; i < currString.length(); i++) {
								if(currString.charAt(i) == ' ' || currString.charAt(i) == 'n' || currString.charAt(i) == '\n') {
									isValid = false;
								}
							}
							if (isValid) {
								//convert string to long
//								long sequence = convertGBKtoSubseq(currString);
								//make tree object
//								TreeObject newObj = new TreeObject(sequence);
								//is this going to be added to a nod here or inside of BTree class?
								//add object to current node or new node
								//add node to tree if necessary
								
							}
						}
					}
						
				}
				
			} catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
		
	}
	
	public static void printUsage() {
		System.out.println("Expected Format for arguments: <0/1(no/with Cache)> <degree> <gbk file> <sequence length> [<cache size>] [<debug level>]");
	}
}