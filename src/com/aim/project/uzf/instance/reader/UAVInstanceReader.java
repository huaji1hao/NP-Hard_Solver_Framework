package com.aim.project.uzf.instance.reader;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.aim.project.uzf.instance.Location;
import com.aim.project.uzf.instance.UZFInstance;
import com.aim.project.uzf.interfaces.UZFInstanceInterface;
import com.aim.project.uzf.interfaces.UAVInstanceReaderInterface;

/**
 * @author Warren G Jackson
 * @since 1.0.0 (22/03/2024)
 */
public class UAVInstanceReader implements UAVInstanceReaderInterface {
	private static final Logger LOGGER = Logger.getLogger(UAVInstanceReader.class.getName());
	@Override
	public UZFInstanceInterface readUZFInstance(Path path, Random random) {
		try {
			List<String> lines = Files.readAllLines(path);
			boolean isDataSection = false;
			boolean isPreparationArea = false;
			Location foodPreparationLocation = null;
			ArrayList<Location> locations = new ArrayList<>();

			for (String line : lines) {
				if (line.startsWith("PREPARATION_AREA")) {
					isDataSection = true; // begin to read data
					isPreparationArea = true;
					continue;
				} else if (line.startsWith("ENCLOSURE_LOCATIONS")) {
					isPreparationArea = false;
					continue;
				} else if(line.startsWith("EOF")){
					if (foodPreparationLocation == null)
						throw new IllegalArgumentException("Food preparation location data is missing.");

					return new UZFInstance(locations.size(), locations.toArray(new Location[0]), foodPreparationLocation, random);
				}

				// If we are in the data section, read the locations
				if (isDataSection && !line.isEmpty()) {
					String[] parts = line.split("\\s+");
					int x = Integer.parseInt(parts[0]);
					int y = Integer.parseInt(parts[1]);

					if (isPreparationArea) {
						foodPreparationLocation = new Location(-1, x, y);
					} else {
						locations.add(new Location(x, y));
					}
				}
			}

		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Failed to read the UAV instance from the path: " + path, e);
		}

		return null;
	}
}
