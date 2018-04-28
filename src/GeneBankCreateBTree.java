package src;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;



public class GeneBankCreateBTree {
	//TODO Driver class to create a BTree file from a given gbk file	
	//args format: <0/1(no/with Cache)> <degree> <gbk file> <sequence length> [<cache size>] [<debug level>]
	public static void main(String[] args) throws IOException {
		int degree, seqLength = 0, cacheSize =0, debugLevel = 0;
		boolean cacheFlag = false;
		String gbkFileName;
		File gbk = null;
		Scanner lineScan, charScan;
		String line = "", DNA = "";
		StringBuilder totalDNA = new StringBuilder();
		String token;
		boolean readLine = false ;
		RAM ram = new RAM();
		BTree btree = null;

		if ((args.length == 0) || (args.length < 4 || args.length > 6)) { //verify correct amount of args
			//printUsage();
			//System.exit(1);
		}else {
			try {
				if (args.length == 6) {
					cacheSize = Integer.parseInt(args[4]);
					debugLevel = Integer.parseInt(args[5]);
				}
				int cache = Integer.parseInt(args[0]);
				if (cache == 1)
						cacheFlag = true;
				else cacheFlag = false;
				degree = Integer.parseInt(args[1]);
				seqLength = Integer.parseInt(args[3]);
				gbk = new File(args[2]);

				//BTreeNode tree = new BTreeNode();
				btree = new BTree(seqLength, degree, gbk, cacheFlag, cacheSize);

				//Parse File
				lineScan = new Scanner(gbk);
				while(lineScan.hasNextLine()) {
					line = lineScan.nextLine();

					
					if(line.substring(0,2) == ("//")) {
						readLine = false;
					}else if(line.substring(0,6).equals("ORIGIN")) {
						readLine = true;
						line = lineScan.nextLine();
					}
					if(readLine) {
						charScan = new Scanner(line);
						while (charScan.hasNext()) {
							token = charScan.next();
							for(int x = 0; x < token.length();++x){
							  if ((token.charAt(x) != ('\n')) && (token.charAt(x) != ' ') && (token.charAt(x) < 48  ||  token.charAt(x) > 57) ) {
								  totalDNA.append(token.charAt(x));
							  }
							}
						}
						charScan.close();
					}

				}
				lineScan.close(); 

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				DNA = totalDNA.toString();
				int index = 0;
				while((index + seqLength) <= DNA.length()) {
					String currString = DNA.substring(index,index + seqLength);
					boolean isValid = true;
					for(int i = 0; i < currString.length(); i++) {
						if(currString.charAt(i) == 'n') {
							isValid = false;
						}
					}
					if (isValid) {
						//convert currString to long
						long sequence = ram.convertGBKtoSubseq(currString);
//						btree.writeToFile(Long.toString(sequence),1); //TODO node instead of subseq
						//make tree object

						try {
							btree.BTreeInsert(btree, sequence);
						} catch (IOException e) {
							e.printStackTrace();
						}


						//search for object
						//if found increment freq
						//else insert
						//either into and existing node or a new node
					//System.out.println(currString);
					}
					index++;
				}
				//output dump or poop
//				if(debugLevel>0){
//					File dump = new File("dump1");
//					dump.delete();
//					dump.createNewFile();
//					PrintWriter writer = new PrintWriter(dump);
//					btree.inOrderWriteFile(btree.root,writer, seqLength);
//					writer.close();
//				}

				if(cacheFlag){
					btree.flushCache();
				}
				//System.out.println("done");

				System.exit(0);
			}
			
		}
		
	}

}
/*
	public void printUsage() {
		System.out.println("Expected Format for arguments: <0/1(no/with Cache)> <degree> <gbk file> <sequence length> [<cache size>] [<debug level>]");

	}*/
