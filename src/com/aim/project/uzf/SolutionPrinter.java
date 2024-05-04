package com.aim.project.uzf;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;

import com.aim.project.uzf.instance.Location;

public class SolutionPrinter {

	private final String strOutputFilePath;
	
	public SolutionPrinter(String strOutputFilePath) {
		
		this.strOutputFilePath = strOutputFilePath;
	}
	
	/**
	 * 
	 * @param loRouteLocations The array of Locations ordered in route order.
	 */
	public void printSolution(List<Location> loRouteLocations) {

		OutputStream os;
		try {
			os = new FileOutputStream(strOutputFilePath);
			PrintStream printStream = new PrintStream(os);

			Iterator<Location> it = loRouteLocations.iterator();
			Location loc;

			if(it.hasNext()) {

				loc = it.next();
				printStream.print(loc.iLocationId());
			}

			while(it.hasNext()) {

				loc = it.next();
				printStream.print("-" + loc.iLocationId());
			}
			printStream.close();
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
	}
}
