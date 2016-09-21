package br.edu.ifce.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import br.edu.ifce.service.LoadData;

public class Data {
	
	private List<String> dataList = new ArrayList<>();
	private Map<String,Integer> mapAttributePosition = new HashMap<>();
	private Map<String,List<String>> map = new LinkedHashMap<String,List<String>>();
	private String[][] matriz;
	private int lin;
	private int col;
	private List<String> atributos = new ArrayList<>();
	private List<String> classes = new ArrayList<>(); 
	
	public Data(){
		dataList = LoadData.readData();
		String [] attr = dataList.remove(0).split(";"); //a primeira linha é o nome de cada coluna
		atributos = Arrays.asList(attr); //O último elemento não é atributo
		col = attr.length;
		lin = dataList.size();
		fillMatriz();
		fillMapWithAttributesEValores();
	}
	
	private String[][] fillMatriz(){
		int j = 0;
		String [] value;
		matriz = new String[lin][col];
		
		for (int i = 0; i < lin; i++) {
			
			value = dataList.get(i).split(";");
			
			while(j < col){
				matriz[i][j] = value[j];
				j++;
			}
			
			j = 0;
		}
		
		return matriz;
	}
	
	private void fillMapWithAttributesEValores(){
		int j = 0;
		List<String> values;

		while (j < getCol()-1){
			values = new ArrayList<>();
			for (int i = 0; i < getLin(); i++) {
				String val = matriz[i][j];
				
				if(!values.contains(val)){
					values.add(val);
				}
				
				if(!this.classes.contains(matriz[i][col-1])){
					this.classes.add(matriz[i][col-1]);
				}
			}
			
			map.put(atributos.get(j), values);
			mapAttributePosition.put(atributos.get(j), j);
			j++;
		}
	}
	
	public Map<String, Integer> getMapAttributePosition() {
		return mapAttributePosition;
	}

	public List<String> getClasses() {
		return classes;
	}

	public Map<String,List<String>> getMap(){
		return map;
	}
	
	public List<String> getAtributos() {
		return atributos;
	}

	public String[][] getMatriz() {
		return matriz;
	}

	public int getLin() {
		return lin;
	}

	public int getCol() {
		return col;
	}
}
