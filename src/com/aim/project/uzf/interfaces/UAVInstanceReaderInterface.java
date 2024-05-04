package com.aim.project.uzf.interfaces;

import java.nio.file.Path;
import java.util.Random;

/**
 * @author Warren G Jackson
 * @since 1.0.0 (22/03/2024)
 */
public interface UAVInstanceReaderInterface {

	/**
	 * 
	 * @param path The path to the instance file.
	 * @param random The random number generator to use.
	 * @return A new instance of the UZF problem as specified by the instance file.
	 */
	public UZFInstanceInterface readUZFInstance(Path path, Random random);
}
