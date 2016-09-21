package br.edu.ifce.id3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.edu.ifce.model.Data;
import br.edu.ifce.model.Nodo;
import br.edu.ifce.service.Calcule;

public class ID3 {
	
	private List<String> classes;
	private Map<String,Integer> mapAttributesPosition;
	private Map<String,List<String>> mapAttributeAndValues;
	private Data data;
	
	public ID3(Data data){
		this.data = data;
		this.classes = data.getClasses();
		this.mapAttributesPosition = data.getMapAttributePosition();
		this.mapAttributeAndValues = data.getMap();
	}
	
	public Nodo id3 (String[][] exemplos, Map<String,Integer> mapAttributesPosition){
		Nodo nodo = new Nodo();
		
		if(isAllClasse(exemplos,classes.get(0))){
			nodo.setRotulo(classes.get(0));
			return nodo;
		}else if(isAllClasse(exemplos,classes.get(1))){
			nodo.setRotulo(classes.get(1));
			return nodo;
		}
		
		if(mapAttributesPosition.isEmpty()){
			nodo.setRotulo(valorDaMaioria(exemplos, classes));
			return nodo;
		}else{
			String attr = getBestAttribute(exemplos,mapAttributesPosition);
			nodo.setRotulo(attr);
			List<String> vi = mapAttributeAndValues.get(attr);
			
			for (int i = 0; i < vi.size(); i++) {
				String [][] exemplosVi = exemplosByAttritbute(exemplos,vi.get(i),attr);
				if(exemplosVi==null){
					nodo.setRotulo(valorDaMaioria(exemplos, classes));
				}else{
					Map<String,Integer> newMapAttributesPosition = newAttributes(mapAttributesPosition, attr);
					
					Nodo subNo = new Nodo();
					Nodo n = id3(exemplosVi, newMapAttributesPosition);//RECURSAO

					subNo.addFilho(n);
					subNo.setRotulo(vi.get(i));
					
					nodo.addFilho(subNo);
				}
			}
		}
		
		return nodo;
	}

	private String getBestAttribute(String[][] matriz,Map<String,Integer> mapAttributesPosition) {
		String best = Calcule.getBestAttribute(matriz, classes, mapAttributesPosition, mapAttributeAndValues);
		return best;
	}
	
	private String [][] exemplosByAttritbute(String [][] examples, String value ,String attr){
		int qtdCol = examples[0].length;
		int positionCol = mapAttributesPosition.get(attr);
		int lines = 0;
		int lm = 0;
		String [][] matriz=null;
		
		for (int i = 0; i < examples.length; i++) {
			if(examples[i][positionCol].equals(value)){
				lines++;
			}
		}
		
		if(lines>0){
			
			matriz = new String [lines][qtdCol];
			
			for (int i = 0; i < examples.length; i++) {
				if(examples[i][positionCol].equals(value)){
					for (int j = 0; j < qtdCol; j++) {
						matriz[lm][j] = examples[i][j]; 
					}
					lm++;
				}
			}
		}
		
		return matriz;	
	}
	
	private boolean isAllClasse(String[][] exemplos, String classe){
		int countClass = 0;
		int lines = exemplos.length;
		int colClass = exemplos[0].length - 1;
		
		for (int i = 0; i < exemplos.length; i++) {
			if(exemplos[i][colClass].equals(classe)){
				countClass++;
			}
		}
		
		if(lines != countClass){
			return false;
		}
		
		return true;
	}
	
	private String valorDaMaioria(String [][] exemplos, List<String> classes){
		int countClass1 = 0;
		int countClass2 = 0;
		int colClass = exemplos[0].length - 1;
		
		for (int i = 0; i < exemplos.length; i++) {
			if(exemplos[i][colClass].equals(classes.get(0))){
				countClass1++;
			}else{
				countClass2++;
			}
		}
		
		if(countClass1 > countClass2){
			return classes.get(0);
		}else{
			return classes.get(1);
		}
	}
	
	private Map<String,Integer> newAttributes(Map<String,Integer> mapAttributesPosition, String atributoAserRemovido){
		
		Map<String,Integer> newMap = new HashMap<>();
		
		for (Map.Entry<String,Integer> entry : mapAttributesPosition.entrySet()) {
			String key = entry.getKey();
			int position = entry.getValue();

			if(!key.equals(atributoAserRemovido)){
				newMap.put(key, position);
			}
		}
		return newMap;
	}

}