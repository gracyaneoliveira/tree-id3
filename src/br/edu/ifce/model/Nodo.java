package br.edu.ifce.model;

import java.util.ArrayList;
import java.util.List;

public class Nodo {
	
	private String rotulo;
	private List<Nodo> filhos = new ArrayList<Nodo>();
	
	public Nodo(){
		
	}
	
	public Nodo(String rotulo){
		this.rotulo = rotulo;
	}
	
	public void addFilho(Nodo filho){
		filhos.add(filho);
	}

	public String getRotulo() {
		return rotulo;
	}

	public void setRotulo(String rotulo) {
		this.rotulo = rotulo;
	}

	public List<Nodo> getFilhos() {
		return filhos;
	}
}
