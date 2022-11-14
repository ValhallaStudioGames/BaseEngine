package com.valhalla.engine.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileEditor {
	
	/**
	 * Reads in a file from a given location and returns the file split into a new piece every 'SPACE' or 'ENTER'.<br>
	 * <u>Note:</u> This function reads file as if they were plain text.
	 * @param file <b>(File)</b> the file to read from.
	 * @return (String[]) File split into an array, with new array-element being added each 'SPACE' or 'ENTER'.
	 * @throws IOException
	 * @author BauwenDR
	 */
	public static String[] fileReader(File file) throws IOException {
		StringBuilder builder = new StringBuilder();
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		String line;
		while((line = reader.readLine()) != null) {
			builder.append(line + "\n");
		}
			
		reader.close();
		String fileContent = builder.toString();
		return fileContent.split("\\s+");
	}
	
	/**
	 * Transforms the input String[column][row] into a file on a given location with every incrementation of column starting a new line and row adding a 'SPACE' between 2 elements.
	 * @param text <b>(String[][])</b> 2-dimensional array of text to put out in a file.
	 * @param path <b>(String)</b> Location of where the file will be saved.
	 * @throws IOException
	 * @author BauwenDR
	 */
	public static void writeFile(String[][] text, String path) throws IOException {
		FileWriter writer = new FileWriter(path);
			
		for(int i = 0; i < text.length; i++) {
			for(int j = 0; j < text[i].length; j++) {
				if(j != 0) 
					writer.write(" ");
				writer.write(text[i][j]);
			}
			writer.write(System.lineSeparator());
		}
		writer.close();
	}
}
