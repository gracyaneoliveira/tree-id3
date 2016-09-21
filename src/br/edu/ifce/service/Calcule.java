package br.edu.ifce.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Calcule {
	
	private static double calculeEntropyAllTable(String [][] matriz, List<String> classes) {
		double countClasse1 = 0.0;
		double countClasse2 = 0.0;
		
		int lin = matriz.length;
		int col = matriz[0].length - 1;
		
		for (int i = 0; i < lin; i++) {
			if(matriz[i][col].equals(classes.get(0))){
				countClasse1++;
			}else{
				countClasse2++;
			}
		}
		
		double entropy = entropie(countClasse1, countClasse2);
		
		return entropy;
	}
	
	private static double log2(double n) {
	    return (Math.log(n) / Math.log(2));
	}
	
	private static Map<String,Double> calculeGainByAttribute(String [][] matriz, List<String> classes, Map<String,Integer> mapAttributesPosition,
			Map<String,List<String>> mapAttributeAndValues) {
		
		Map<String,Double> mapAttributeEntropie = new HashMap<>();
		
		double E = calculeEntropyAllTable(matriz, classes);
		
		double gain = 0.0;
		for (Map.Entry<String,Integer> entry : mapAttributesPosition.entrySet()) { //Pega atributo por atributo
			String key = entry.getKey();
			int indexCol = entry.getValue(); //coluna do atributo a ser testado
			
			List<String> values = mapAttributeAndValues.get(key);
			double resto = calculeResto(matriz, values, indexCol, classes);
			
			gain =  E - resto;
			
			mapAttributeEntropie.put(key, gain);
		}
		
		return mapAttributeEntropie;
	}
	
	public static String getBestAttribute(String [][] matriz, List<String> classes, Map<String,Integer> mapAttributesPosition,
			Map<String,List<String>> mapAttributeAndValues){
		
		double maior = Double.NEGATIVE_INFINITY;
		String atributo="";
		
		Map<String,Double> mapAttributeEntropie = calculeGainByAttribute(matriz, classes, mapAttributesPosition, mapAttributeAndValues);
				
		for (Map.Entry<String,Double> entry : mapAttributeEntropie.entrySet()) {
			String key = entry.getKey();
			double ganho = entry.getValue();
			
			if(maior < ganho){
				maior = ganho;
				atributo = key;
			}
		}
		
		return atributo;
	}

	private static double calculeResto(String [][] matriz, List<String> values, int indexCol, List<String> classes){
		double countClasse1 = 0.0;
		double countClasse2 = 0.0;
		double resto = 0.0;
		double entropie = 0.0;
		
		int colClasse = matriz[0].length-1;
		double lines = matriz.length; //conferir depois. Deve ser o numero de linhas visitados em cada iteração para o calculo de all tables
		
		for (int i = 0; i < values.size(); i++) {  //values = entidades dos atributos
			for (int j = 0; j < matriz.length; j++) {
				if(matriz[j][indexCol].equals(values.get(i))){
					if(matriz[j][colClasse].equals(classes.get(0))){
						countClasse1++;
					}else{
						countClasse2++;
					}
				}
			}
			
			entropie = entropie(countClasse1,countClasse2);
			resto = resto + ((countClasse1+countClasse2)/lines)*entropie;
			countClasse1 = 0.0;
			countClasse2 = 0.0;
		}
		
		return resto;
	}
	
	private static double entropie(double countClasse1, double countClasse2){
		double total = countClasse1+countClasse2;
		double value1 = countClasse2 != 0 ? -(countClasse2/total)*log2(countClasse2/total) : 0;
		double value2 = countClasse1 != 0 ? -(countClasse1/total)*log2(countClasse1/total) : 0;
		
		return (value1+value2);
	}
	
	public static boolean isAllPositives(String[][] exemplos , String classe){
		int lin = exemplos.length;
		int col = exemplos[0].length - 1;
		int countClasse = 0;
		
		for (int i = 0; i < exemplos.length; i++) {
			if(exemplos[i][col].equals(classe)){
				countClasse++;
			}
		}
		
		if(lin != countClasse){
			return false;
		}
		
		return true;
	}
	
}
