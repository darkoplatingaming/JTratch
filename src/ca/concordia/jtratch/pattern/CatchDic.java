package ca.concordia.jtratch.pattern;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ca.concordia.jtratch.utility.IOFile;

public class CatchDic extends HashMap<String, CatchList> {
	public int NumCatch = 0;
    public int NumLogged = 0;
    public int NumThrown = 0;
    public int NumLoggedAndThrown = 0;
    public int NumLoggedNotThrown = 0;
    private static final Logger logger = LogManager.getLogger(CatchDic.class.getName());
	
    public void Add(List<CatchBlock> catchList)
    {
        for (CatchBlock catchBlock : catchList)
        {
            if (catchBlock == null) continue;
            NumCatch++;
            String exception = catchBlock.ExceptionType;
            if (this.containsKey(exception))
            {
                this.get(exception).add(catchBlock);
            }
            else
            {
                //Create a new list for this type.
                this.put(exception, new CatchList());
                this.get(exception).add(catchBlock);
            }

            //Update Statistics
            if (catchBlock.OperationFeatures.get("Logged") == 1)
            {
                this.get(exception).NumLogged++;
                NumLogged++;
                if (catchBlock.OperationFeatures.get("Thrown") == 1)
                {
                    this.get(exception).NumLoggedAndThrown++;
                    NumLoggedAndThrown++;
                }
                else
                {
                    this.get(exception).NumLoggedNotThrown++;
                    NumLoggedNotThrown++;
                }
            }
            if (catchBlock.OperationFeatures.get("Thrown") == 1)
            {
                this.get(exception).NumThrown++;
                NumThrown++;
            }
        }
    }

    public void PrintToFile()
    {
    	logger.trace("Writing CatchBlock features into file...");
    	Charset charset = Charset.forName("UTF-8");
    	Path file = Paths.get(IOFile.CompleteFileName("CatchBlock.txt"));
    	Path fileMeta = Paths.get(IOFile.CompleteFileName("CatchBlock_Meta.txt"));
    	Path fileCSV = Paths.get(IOFile.CompleteFileName("CatchBlock.csv"));
    	
    	Integer catchId = 0;
        String metaKey = CatchBlock.Splitter;
        
        for ( String meta : CatchBlock.MetaKeys)
        {
            metaKey += (meta + CatchBlock.Splitter);
        }
        
    	try 
    	(
			BufferedWriter bw = Files.newBufferedWriter(file, charset);
			BufferedWriter metaBW = Files.newBufferedWriter(fileMeta, charset);
			BufferedWriter csvBW = Files.newBufferedWriter(fileCSV, charset);
		)
    	{
    		metaBW.write(metaKey);
    		metaBW.newLine();
    		metaBW.write("--------------------------------------------------------");
    		metaBW.newLine();
    		metaBW.write( "NumExceptionType: "+ this.keySet().size() +", NumCatchBlock: "+ NumCatch +", NumLogged: "+ NumLogged +", "
                + "NumThrown: "+ NumThrown +", NumLoggedAndThrown: "+ NumLoggedAndThrown +", NumLoggedNotThrown: "+ NumLoggedNotThrown +"." );
    		metaBW.newLine();
    	
    		for (Map.Entry<String,CatchList> entry : this.entrySet())
    		{
    			metaBW.write("--------------------------------------------------------");
        		metaBW.newLine();
        		CatchList catchList = entry.getValue();
        		metaBW.write( "Exception Type: "+ entry.getKey() +", NumCatchBlock: "+ catchList.size() +", NumLogged: "+ catchList.NumLogged +", "
                        + "NumThrown: "+ catchList.NumThrown +", NumLoggedAndThrown: "+ catchList.NumLoggedAndThrown +", NumLoggedNotThrown: "+ catchList.NumLoggedNotThrown +"." );
            		metaBW.newLine();
            		
            	for (CatchBlock catchblock : catchList)
            	{
            		catchId++;
            		bw.write("ID:" + catchId + CatchBlock.Splitter + catchblock.PrintFeatures());
            		bw.newLine();
            		metaBW.write("ID:" + catchId + CatchBlock.Splitter + catchblock.PrintMetaInfo());
            		metaBW.newLine();
            		csvBW.write(catchId + "," + catchblock.PrintCSV());
            		csvBW.newLine();
            		
            	}
            	metaBW.newLine();
            	metaBW.newLine();
            	bw.flush();
            	metaBW.flush();
            	csvBW.flush();            	
    		}
    		
    		//Print summary
            metaBW.write("------------------------ Summary -------------------------");
    		metaBW.newLine();
    		metaBW.write("Exception Type" + "\t" +
                    "NumCatch" + "\t" +
                    "NumLogged" + "\t" +
                    "NumThrown" + "\t" +
                    "NumLoggedAndThrown" + "\t" +
                    "NumLoggedNotThrown");
    		metaBW.newLine();
    		
    		for (Map.Entry<String,CatchList> entry : this.entrySet())
    		{
        		CatchList catchList = entry.getValue();
        		metaBW.write(entry.getKey() + "\t" +
                        catchList.size() + "\t" +
                        catchList.NumLogged + "\t" +
                        catchList.NumThrown + "\t" +
                        catchList.NumLoggedAndThrown + "\t" +
                        catchList.NumLoggedNotThrown);
        		metaBW.newLine();
    		}
    		bw.close();
    		metaBW.close();
    		csvBW.close();
    		logger.trace("Writing done.");
        	
    	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
