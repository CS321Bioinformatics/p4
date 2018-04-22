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
		Scanner lineScan, charScan;
		String line, DNA;
		StringBuilder totalDNA = new StringBuilder();
		char token;
		boolean readLine = false ;
		
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
				lineScan = new Scanner(gbk);
				while(lineScan.hasNextLine()) {
					line = lineScan.nextLine();
					
					if(line.substring(0,5).equals("ORIGIN")) {
						readLine = true;
					}else if(line.substring(0,5).equals("//")) {
						readLine = false;
					}
					if(readline) {
						charScan = new Scanner(line);
						while (charScan.hasNext()) {
							token = charScan.Next();
							if (token != '\n' || 1token.isDigit() || token != ' ') {
								totalDNA.append(token);
							}
						}
						charScan.close();
					}
					
				}
				lineScan.close(); 
				
			} catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
            	DNA = totalDNA.toString();
            	int index = 0;
				while((index + seqLength) < DNA.length()) {
					String currString = DNA.substring(index,seqLength);
					boolean isValid = true;
					for(int i = 0; i < currString.length(); i++) {
						if(currString.charAt(i) == 'n') {
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
        }
		
	}
	
	public static void printUsage() {
		System.out.println("Expected Format for arguments: <0/1(no/with Cache)> <degree> <gbk file> <sequence length> [<cache size>] [<debug level>]");
	}
}