import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

package src;

public class GeneBankCreateBTree throws FileNotFoundException{
  //TODO Driver class to create a BTree file from a given gbk file	
	//args format: <0/1(no/with Cache)> <degree> <gbk file> <sequence length> [<cache size>] [<debug level>]
	public static void main(String[] args) {
		int cacheFlag, degree, seqLength, cacheSize, debugLevel;
		String gbkFileName;
		File gbk;
		
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
				
				//Parse File
				
				
			}
		}
		
	}
	
	public static void printUsage() {
		System.out.println("Expected Format for arguments: <0/1(no/with Cache)> <degree> <gbk file> <sequence length> [<cache size>] [<debug level>]");
	}
}