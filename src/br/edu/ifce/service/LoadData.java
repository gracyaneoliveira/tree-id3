package br.edu.ifce.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LoadData {
	
	private static final String FILE = "data/data.txt";

	public static List<String> readData() {
		List<String> dataList = new ArrayList<String>();
		
		File file = new File(FILE);

		Scanner sc = null;
		try {
			sc = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			dataList.add(line);
		}
		sc.close();
		
		return dataList;
	}
}
