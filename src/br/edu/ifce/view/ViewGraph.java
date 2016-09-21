package br.edu.ifce.view;

import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;

import br.edu.ifce.id3.ID3;
import br.edu.ifce.model.Data;
import br.edu.ifce.model.Nodo;

public class ViewGraph extends JFrame{

	private static final long serialVersionUID = -2707712944901661771L;
	private static List<String> listPath = new ArrayList<>();
	private static boolean isRaiz  = true;
	private static String raiz = "RAIZ:";
	private static String str;

	public ViewGraph() {
		super("Tree Decision!!!");

		mxGraph graph = new mxGraph();
		Object parent = graph.getDefaultParent();
		
		Map<String, Object> style = new HashMap<>();
		style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
		style.put(mxConstants.STYLE_FONTCOLOR,"#808080");//#3399FF
		style.put(mxConstants.STYLE_FONTSTYLE,Font.BOLD);
		style.put(mxConstants.STYLE_FILLCOLOR,"white");

		graph.getStylesheet().putCellStyle("boxstyle", style);

		graph.getModel().beginUpdate();
		try {

			Map<String,List<String>> m = executeID3(parent,graph);
			imprimir(parent, graph, m);
		}
		finally {
			graph.getModel().endUpdate();
		}

		mxGraphComponent graphComponent = new mxGraphComponent(graph);
		graphComponent.setEnabled(false);
		getContentPane().add(graphComponent);
	}

	public static void main(String[] args) {
		ViewGraph frame = new ViewGraph();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 400);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	private Map<String,List<String>> executeID3(Object parent,mxGraph graph){
	    Data data = new Data();
		ID3 id3 = new ID3(data);
		Nodo no =  id3.id3(data.getMatriz(), data.getMapAttributePosition());
		
		List<String> lm = listToPrint(no);
		String rz = getRaiz(listPath);
		Map<String,List<String>> m = mapToGraph(listPath,rz);
		
		for (Map.Entry<String, List<String>> it : m.entrySet()) {
			System.out.println(it.getKey()+" " + it.getValue());
		}
		return m;
	}

	private void imprimir(Object parent,mxGraph graph, Map<String,List<String>> m){
		String myRaiz = "";
		Map<String,Object> mObject = new HashMap<>();
		List<String> attr = new ArrayList<>();
		for (Map.Entry<String, List<String>> it : m.entrySet()) {
			String key = it.getKey();
			if(key.startsWith("RAIZ")){
				myRaiz = key;
			}
		}
		
		int x = 320;
		int y = 20;
		
		while(!m.isEmpty()){
			//[ARCO:Ensolarado, ARCO:Nublado, ARCO:Chuva]
			//[ARCO:Não, ARCO:Sim]
			List<String> lm = m.get(myRaiz);
			
			Object v1 = null;
			
			if(mObject.size()==0){
				v1 = graph.insertVertex(parent, null, myRaiz.split(":")[1],x, y, 80, 30,"boxstyle");
			}else if(mObject.size()>0){
				v1 = mObject.get(myRaiz);
				x = (int) graph.getCellGeometry(v1).getX();
				y = (int) graph.getCellGeometry(v1).getY();
				mObject.remove(myRaiz);
			}
			
			for (int i = 0; i < lm.size(); i++) {
				//[Umidade]
				// [P] [N]
				List<String> li = m.get(lm.get(i));
				
				for(int j = 0; j < li.size(); j++){
					Object v2 = graph.insertVertex(parent, null, li.get(j),x-100, y+100, 80, 30,"boxstyle");
					graph.insertEdge(parent, null,lm.get(i), v1, v2);
					if(m.get(li.get(j)) != null){attr.add(li.get(j)); mObject.put(li.get(j), v2);}
					if(li.size()>1)x = x - 100 + 50; 
				}
				x = x + 150;
				m.remove(lm.get(i));
			}
			m.remove(myRaiz);
			//System.out.println(graph.getCellGeometry(v1).getY());
			//System.out.println((String)m.keySet().toArray()[0]);
			if(!attr.isEmpty()) {myRaiz = attr.get(0); attr.remove(0);}
		}
	}
	
	private static List<String> listToPrint(Nodo no){
		if(!isRaiz){raiz = "";}
		List<Nodo> nodo = no.getFilhos();
		for (Nodo n : nodo) {
			str = raiz+no.getRotulo() + ";"+ n.getRotulo();
			listPath.add(str);
			isRaiz = false;
			listToPrint(n);
		}
		return null;
	}
	
	private static String getRaiz(List<String> list){
		String rz = null;
		for (int i = 0; i < list.size(); i++) {
			String r =  list.get(i).split(";")[0];
			
			if(r.contains("RAIZ:")){
				rz = r.split(":")[1];
			}
		}
		return rz;
	}
	
	private static Map<String,List<String>> mapToGraph(List<String> list, String rz){
		Map<String,List<String>> map = new HashMap<>();

		for (int i = 0; i < list.size(); i++) {
			String s = list.get(i);
			String [] line = s.split(";");
			
			String key = line[0];
			String value = line[1];
			
			if(map.containsKey(key)){
				List<String> li = new ArrayList<>();
				li = map.get(key);
				li.add(value);
				map.put(key, li);
			}else if(key.equals(rz)){
				List<String> li = new ArrayList<>();
				li = map.get("RAIZ:"+rz);
				li.add(value);
				map.put("RAIZ:"+rz, li);
			}else{
				List<String> lt = new ArrayList<>();
				lt.add(value);
				
				map.put(key, lt);
			}
		}
		return map;
	}
}
