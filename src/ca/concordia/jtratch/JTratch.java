/**
 * 
 */
package ca.concordia.jtratch;

//Import log4j classes.
import org.apache.logging.log4j.Logger;

import ca.concordia.jtratch.utility.Config;
import ca.concordia.jtratch.utility.IOFile;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;


/**
 * @author gbp
 * JTratch is a translation of NTratch. (an ExceptionAnalysis tool for C#, based on LogAdvisor)
 * Credits: LogAdvisor - https://github.com/cuhk-cse/LogAdvisor
 * 			Jieming Zhu, Pinjia He, Qiang Fu, Hongyu Zhang, Michael R. Lyu, and Dongmei Zhang, "Learning to Log: Helping Developers Make Informed Logging Decisions," in Proc. of ACM/IEEE ICSE, 2015.
 */
public class JTratch {

	private static final Logger logger = LogManager.getLogger(JTratch.class);
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final String inputMode = args[0];
		final String filePath = args[1];
		
		IOFile.FolderPath = filePath;
		
		//Load Config file
		Config.Load("Config.txt");
				
		logger.trace("Entering application at" + System.getProperty("user.dir"));
        
		CodeWalker walker = new CodeWalker();
        try {
			walker.LoadByInputMode(inputMode, filePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        
		logger.trace("Exiting application.");

	}

}
