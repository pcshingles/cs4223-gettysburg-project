/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright ©2016-2017 Gary F. Pollice
 *******************************************************************************/
package gettysburg.engine.utility.configure;

import static org.junit.Assert.*;
import java.io.*;
import org.junit.Test;
import com.google.gson.Gson;
import student.gettysburg.engine.utility.configure.LoadableSquare;

/**
 * Test cases for the Configurator.
 * 
 * @version Jun 17, 2017
 */
public class ConfiguratorTest
{
	private Gson gson = new Gson();
	
	@Test
	public void test()
	{
		LoadableSquare[] squares = null;
		try {
			Reader reader = new FileReader("configuration/board.json");
			squares = gson.fromJson(reader, LoadableSquare[].class);
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertEquals(513, squares.length);
	}

}
