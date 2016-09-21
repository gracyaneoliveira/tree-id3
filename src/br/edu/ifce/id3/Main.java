package br.edu.ifce.id3;

import java.util.List;

import br.edu.ifce.model.Data;
import br.edu.ifce.model.Nodo;

public class Main {
	
	public static void main(String[] args) {

		Data data = new Data();
		ID3 id3 = new ID3(data);
		Nodo no =  id3.id3(data.getMatriz(), data.getMapAttributePosition());
		int count = 1;
		imprimir(no,"|-- ",count);
	}
	
	private static void imprimir(Nodo no, String s,int count){
		System.out.println(s+no.getRotulo());
		s = "|" + s;
		List<Nodo> nodo = no.getFilhos();
		for (Nodo n : nodo) {
			imprimir(n,s,count);
		}
	}
}
