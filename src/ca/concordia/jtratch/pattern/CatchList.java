package ca.concordia.jtratch.pattern;

import java.util.ArrayList;

public class CatchList extends ArrayList<CatchBlock> {
	public int NumLogged = 0;
    public int NumThrown = 0;
    public int NumLoggedAndThrown = 0;
    public int NumLoggedNotThrown = 0;
}
