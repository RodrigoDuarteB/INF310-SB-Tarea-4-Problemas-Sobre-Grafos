package bo.edu.uagrm.ficct.ed2sb.grafosPesados;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import bo.edu.uagrm.ficct.ed2sb.excepciones.ExcepcionAristaNoExiste;
import bo.edu.uagrm.ficct.ed2sb.excepciones.ExcepcionAristaYaExiste;
import bo.edu.uagrm.ficct.ed2sb.excepciones.ExcepcionVerticeNoExiste;


public class DiGrafoPesado<T extends Comparable<T>> extends GrafoPesado<T> { //dirigido (unidireccional)
	
	public static double INFINITO = Double.MAX_VALUE;
	
	public DiGrafoPesado() {
		super();
	}
	
	@Override
	public void insertarArista(T verticeOrigen, T verticeDestino, double costo)
			throws ExcepcionAristaYaExiste, ExcepcionVerticeNoExiste {
		if(!this.existeVertice(verticeOrigen)) {
			throw new ExcepcionVerticeNoExiste("Vertice "+String.valueOf(verticeOrigen)+" no existe");
		}
		if(!this.existeVertice(verticeDestino)) {
			throw new ExcepcionVerticeNoExiste("Vertice "+String.valueOf(verticeDestino)+" no existe");
		}
		if(this.existeAdyacencia(verticeOrigen, verticeDestino)) {
			throw new ExcepcionAristaYaExiste("La arista ya existe entre "+String.valueOf(verticeOrigen)+" y "+String.valueOf(verticeDestino));
		}
		int posicionDeVerticeOrigen = this.posicionDeVertice(verticeOrigen);
		int posicionDeVerticeDestino = this.posicionDeVertice(verticeDestino);
		List<AdyacenteConPeso> adyacentesDelOrigen = this.listaDeAdyacencias.get(posicionDeVerticeOrigen);
		AdyacenteConPeso adyacente = new AdyacenteConPeso(posicionDeVerticeDestino, costo);
		adyacentesDelOrigen.add(adyacente);
		Collections.sort(adyacentesDelOrigen);
	}

	@Override
	public int cantidadDeAristas() {
		int aristas = 0;
		for(List<AdyacenteConPeso> adyacenciasDeVertice : this.listaDeAdyacencias) {
			aristas+=adyacenciasDeVertice.size();
		}
		return aristas;
	}

	@Override
	public void eliminarArista(T verticeOrigen, T verticeDestino)
			throws ExcepcionAristaNoExiste, ExcepcionVerticeNoExiste {
		if(!this.existeVertice(verticeOrigen)) {
			throw new ExcepcionVerticeNoExiste("Vertice "+String.valueOf(verticeOrigen)+" no existe");
		}
		if(!this.existeVertice(verticeDestino)) {
			throw new ExcepcionVerticeNoExiste("Vertice "+String.valueOf(verticeDestino)+" no existe");
		}
		if(!this.existeAdyacencia(verticeOrigen, verticeDestino)) {
			throw new ExcepcionAristaNoExiste("No existe arista entre "+String.valueOf(verticeOrigen)+" y "+String.valueOf(verticeDestino));
		}
		int posicionVerticeOrigen = this.posicionDeVertice(verticeOrigen);   
		List<AdyacenteConPeso> adyacenciasDelOrigen = this.listaDeAdyacencias.get(posicionVerticeOrigen);
		AdyacenteConPeso adyacente = new AdyacenteConPeso(posicionVerticeOrigen);
		adyacenciasDelOrigen.remove(adyacente);
	}

	@Override
	public int gradoDe(T vertice) throws ExcepcionVerticeNoExiste {
		throw new UnsupportedOperationException("No soportado en grafos dirigidos");
	}
	
	public int gradoDeEntrada(T vertice) throws ExcepcionVerticeNoExiste {
		return 0;
	}
	
	public int gradoDeSalida(T vertice) throws ExcepcionVerticeNoExiste {
		return super.gradoDe(vertice);
	}
	
	@Override
	public boolean esConexo() {
		throw new UnsupportedOperationException("No soportado en grafos dirigidos");
	}
	
	public boolean esFuertementeConexo() {
		for (T vertice : this.listaDeVertices) {
			if(this.dfs(vertice).size() != this.cantidadDeVertices()) {
				return false;
			}
		}
		return true;
	}
	
	public boolean esDebilmenteConexo() { 
		List<T> recorrido = new ArrayList<>();
		List<Boolean> marcados = this.inicializarMarcados();
		T verticeInicial = this.listaDeVertices.get(0);
		int posicionVerticeInicial = this.posicionDeVertice(verticeInicial);
		this.dfs(recorrido, marcados, posicionVerticeInicial);
		if(estanTodosMarcados(marcados)) {
			return true;
		}else {
			verticeInicial = this.noMarcadoConAdyacenteMarcado(marcados); 
			while(verticeInicial != null && !estanTodosMarcados(marcados)) { 
				posicionVerticeInicial = this.posicionDeVertice(verticeInicial); 
				this.dfs(recorrido, marcados, posicionVerticeInicial);  
				verticeInicial = this.noMarcadoConAdyacenteMarcado(marcados); 
			}
			if(!estanTodosMarcados(marcados)) {
				return false;
			}
		}
		return true;
	}
	
	private T noMarcadoConAdyacenteMarcado(List<Boolean> marcados) {
		for(int i=0; i<marcados.size(); i++) {
			if(marcados.get(i) == Boolean.FALSE) {
				List<AdyacenteConPeso> adyacentes = this.listaDeAdyacencias.get(i);
				for(AdyacenteConPeso adyacente : adyacentes) {
					if(marcados.get(adyacente.getIndiceVertice()) == Boolean.TRUE) {
						return this.listaDeVertices.get(i);
					}
				}
			}
		}
		return null;
	}
	
	
	@Override
	public int cantidadDeIslas() {
		try {
			return this.convertirANoDirigido().cantidadDeIslas();
		} catch (Exception e) {
			return 0;
		}	
	}

	public GrafoPesado<T> convertirANoDirigido() {
		GrafoPesado<T> noDirigido = new GrafoPesado<>();
		for(int i=0; i<this.cantidadDeVertices(); i++) {
			try {
				noDirigido.insertarVertice(this.listaDeVertices.get(i));
			} catch (Exception e) {
				//
			}
		}
		for(int i=0; i<this.cantidadDeVertices(); i++) {
			List<AdyacenteConPeso> adyacencias = this.listaDeAdyacencias.get(i);
			T verticeActual = noDirigido.listaDeVertices.get(i);
			for(int j=0; j<adyacencias.size(); j++) {
				T verticeDestino = this.listaDeVertices.get(adyacencias.get(j).getIndiceVertice());
				double costo = adyacencias.get(j).getCosto();
				try {
					noDirigido.insertarArista(verticeActual, verticeDestino, costo);
				} catch (Exception e) {
					//
				}
			}
		}
		return noDirigido;
	}
	
	@Override
	public boolean hayCiclos() {
		if(!this.esGrafoVacio()) {
			List<Boolean> marcados = this.inicializarMarcados();
			T verticeInicial = this.listaDeVertices.get(0);
			int posicionVerticeInicial = this.posicionDeVertice(verticeInicial);
			boolean hayCiclo = this.dfs(marcados, posicionVerticeInicial);
			while(!hayCiclo && !estanTodosMarcados(marcados)) {
				verticeInicial = primerNoMarcado(marcados);
				posicionVerticeInicial = this.posicionDeVertice(verticeInicial);
				hayCiclo = this.dfs(marcados, posicionVerticeInicial);
			}
			return hayCiclo;
		}
		return false;
	}

	private boolean dfs(List<Boolean> marcados, int posicionEnTurno) {
		boolean hayCiclos=false;
		this.marcarVertice(marcados, posicionEnTurno);
		List<AdyacenteConPeso> adyacentes = this.listaDeAdyacencias.get(posicionEnTurno);
		for(AdyacenteConPeso posicionDeAdyacente : adyacentes) {
			if(!verticeEstaMarcado(marcados, posicionDeAdyacente.getIndiceVertice())) {
				hayCiclos=this.dfs(marcados, posicionDeAdyacente.getIndiceVertice());				
			}else {
				return true;
			}
		}
		return hayCiclos;
	}
	
	//7. En un grafo dirigido usando la implementación del algoritmo de Floyd-Warsall encontrar los caminos de costo mínimo entre un vértice a 
	//	y el resto. Mostrar los costos y cuáles son los caminos
	public String caminoYCostoMinimoDesdeVerticeFloydWarshall(T verticeInicial) {
		String s="";
		double[][] matrizCostos = matrizCostosFloydWarshall();
		int[][] matrizPredecesores = matrizPredecesoresFloydWarshall();
		int posicionVerticeInicial=posicionDeVertice(verticeInicial);
		for(int i=0; i<cantidadDeVertices(); i++) {
			if(i != posicionVerticeInicial) {
				T verticeDestino = listaDeVertices.get(i);
				if(encontrarCosto(verticeInicial, verticeDestino, matrizCostos) != INFINITO) {
					s+="Camino entre "+verticeInicial+" y "+verticeDestino+": "+encontrarCamino(verticeInicial, verticeDestino, matrizPredecesores, matrizCostos)
					+" Costo: "+encontrarCosto(verticeInicial, verticeDestino, matrizCostos)+"\n";
				}else {
					s+="Camino entre "+verticeInicial+" y "+verticeDestino+": "+encontrarCamino(verticeInicial, verticeDestino, matrizPredecesores, matrizCostos)
					+" Costo: --\n";
				}				
			}
		}
		return s;
	}
	
	public double encontrarCosto(T verticeInicial, T verticeDestino, double[][] matrizCaminos) {
		int posicionVerticeInicial=posicionDeVertice(verticeInicial);
		int posicionVerticeDestino=posicionDeVertice(verticeDestino);
		double costo=matrizCaminos[posicionVerticeInicial][posicionVerticeDestino];
		if(costo != INFINITO) {
			return costo;
		}
		return INFINITO;
	}
	
	public String encontrarCamino(T verticeInicial, T verticeDestino, int[][] matrizPredecesores, double[][] matrizCaminos) {
		int posicionVerticeInicial=posicionDeVertice(verticeInicial);
		int posicionVerticeDestino=posicionDeVertice(verticeDestino);
		if(matrizCaminos[posicionVerticeInicial][posicionVerticeDestino] != INFINITO) {
			return verticeInicial+encontrarCaminoIntermedio(verticeInicial, verticeDestino, matrizPredecesores)+"->"+verticeDestino;
		}
		return "No hay camino entre los vertices";
	}
	
	private String encontrarCaminoIntermedio(T verticeInicial, T verticeDestino, int[][] matrizPredecesores) {
		String c="";
		int posicionVerticeInicial=posicionDeVertice(verticeInicial);
		int posicionVerticeDestino=posicionDeVertice(verticeDestino);
		int intermedio=matrizPredecesores[posicionVerticeInicial][posicionVerticeDestino];
		if(intermedio != -1) {
			c+=encontrarCaminoIntermedio(verticeInicial, listaDeVertices.get(intermedio), matrizPredecesores);
			c+="->"+listaDeVertices.get(intermedio);
			c+=encontrarCaminoIntermedio(listaDeVertices.get(intermedio), verticeDestino, matrizPredecesores);
			return c;
		}
		return "";
	}

	private double[][] matrizCostosFloydWarshall() {
		int cv=cantidadDeVertices();
		double[][] P = matrizAdyacenciasConPesos();
		for(int k=0; k<cv; k++) {
			for(int i=0; i<cv; i++) {
				for(int j=0; j<cv; j++) {
					if(P[i][j] > (P[i][k] + P[k][j])) {
						P[i][j]=P[i][k]+P[k][j];
					}
				}
			}
		}
		return P;
	}
	
	private int[][] matrizPredecesoresFloydWarshall() {
		int cv=cantidadDeVertices();
		double[][] P = matrizAdyacenciasConPesos();
		int[][] pred = inicializarMatrizPredecesores();
		for(int k=0; k<cv; k++) {
			for(int i=0; i<cv; i++) {
				for(int j=0; j<cv; j++) {
					if(P[i][j] > (P[i][k] + P[k][j])) {
						P[i][j]=P[i][k]+P[k][j];
						pred[i][j]=k;
					}
				}
			}
		}
		return pred;
	}
	
	private double[][] matrizAdyacenciasConPesos(){
		int cv=cantidadDeVertices();
		double[][] m = new double[cv][cv];
		for(int i=0; i<cv; i++) {
			T verticeOrigen=listaDeVertices.get(i);
			for(int j=0; j<cv; j++) {
				T verticeDestino=listaDeVertices.get(j);
				if(i==j) {
					m[i][j]=0;
				}else if(existeAdyacencia(verticeOrigen, verticeDestino)) {
					m[i][j]=peso(verticeOrigen, verticeDestino);
				}else {
					m[i][j]=INFINITO;
				}
			}
		}
		return m;
	}
	
	private int[][] inicializarMatrizPredecesores() {
		int cv=cantidadDeVertices();
		int[][] mp = new int[cv][cv];
		for(int i=0; i<cv; i++) {
			for(int j=0; j<cv; j++) {
				mp[i][j]=-1;
			}
		}
		return mp;
	}
	
	public double peso(T verticeInicial, T verticeDestino) {
		if(existeAdyacencia(verticeInicial, verticeDestino)) {
			int posicionVerticeOrigen=posicionDeVertice(verticeInicial);
			int posicionDeVerticeDestino=posicionDeVertice(verticeDestino);
			List<AdyacenteConPeso> adyacentesOrigen = listaDeAdyacencias.get(posicionVerticeOrigen);
			AdyacenteConPeso adyacenteDestino = new AdyacenteConPeso(posicionDeVerticeDestino);
			return adyacentesOrigen.get(adyacentesOrigen.indexOf(adyacenteDestino)).getCosto();
		}
		return INFINITO;
	}
	
	//8. En un grafo dirigido pesado implementar el algoritmo de Dijsktra que muestre cual es el camino de costo mínimo entre un vértice a y b 
	//	y cual el costo
	public String caminoYCostoMinimoEntreVerticesDijsktra(T verticeInicial, T verticeDestino) {
		String s="";
		List<Double> costos=inicializarCostos(verticeInicial);
		List<Boolean> marcados=inicializarMarcados();
		List<Integer> pred=inicializarPredecesores();
		int noMarcadoDeMenorCosto=posicionDeVertice(verticeInicial);
		while(!verticeEstaMarcado(marcados, posicionDeVertice(verticeDestino)) && costos.get(noMarcadoDeMenorCosto) != INFINITO) {
			marcarVertice(marcados, noMarcadoDeMenorCosto);
			List<AdyacenteConPeso> adyacentes = listaDeAdyacencias.get(noMarcadoDeMenorCosto);
			T verticeActual=listaDeVertices.get(noMarcadoDeMenorCosto);
			for(int i=0; i<adyacentes.size(); i++) {
				int posicionAdyacente=adyacentes.get(i).getIndiceVertice();
				if(!verticeEstaMarcado(marcados, posicionAdyacente)) {
					T verticeAdyacente=listaDeVertices.get(posicionAdyacente);
					double costoAdyacente=costos.get(posicionAdyacente);
					double costoActual=costos.get(noMarcadoDeMenorCosto);
					if(costoAdyacente>(costoActual+peso(verticeActual, verticeAdyacente))) {
						costos.set(posicionAdyacente, costoActual+peso(verticeActual, verticeAdyacente));
						pred.set(posicionAdyacente, noMarcadoDeMenorCosto);
					}
				}			
			}
			noMarcadoDeMenorCosto=posicionMenorCostoNoMarcado(costos, marcados);
		}
		if(costos.get(posicionDeVertice(verticeDestino)) != INFINITO){
			s+="Camino entre "+verticeInicial+" y "+verticeDestino+": "+encontrarCaminoDijsktra(pred, posicionDeVertice(verticeDestino))
			+" Costo: "+costos.get(posicionDeVertice(verticeDestino));
		}else {
			s+="Camino entre "+verticeInicial+" y "+verticeDestino+": "+"No hay camino entre los vertices Costo: --";
		}
		return s;
	}
	
	private String encontrarCaminoDijsktra(List<Integer> pred, int posicionDestino) {
		List<Integer> camino = new ArrayList<>();
		camino.add(posicionDestino); 
		int elemento=pred.get(posicionDestino); 
		int pos=posicionDestino; 
		while(elemento != -1 && camino.size()<=cantidadDeVertices()) {
			camino.add(elemento);
			pos=elemento;
			elemento=pred.get(pos);
		}
		String s=""+camino.get(camino.size()-1);
		for(int i=camino.size()-2; i>=0; i--) {
			s+="->"+camino.get(i);
		}
		return s;
	}

	private int posicionMenorCostoNoMarcado(List<Double> costos, List<Boolean> marcados) {
		int men=0;
		double comp=INFINITO;
		for(int i=0; i<costos.size(); i++) {
			if(!verticeEstaMarcado(marcados, i)) {
				if(costos.get(i)<=comp) {
					comp=costos.get(i);
					men=i;
				}
			}		
		}
		return men;
	}

	private List<Double> inicializarCostos(T verticeInicial){
		List<Double> costos = new ArrayList<>();
		int posicionVertice = posicionDeVertice(verticeInicial);
		for(int i=0; i<cantidadDeVertices(); i++) {
			if(i != posicionVertice) {
				costos.add(INFINITO);
			}else {
				costos.add(0.0);
			}			
		}
		return costos;
	}
	
	private List<Integer> inicializarPredecesores(){
		List<Integer> pred = new ArrayList<>();
		for(int i=0; i<cantidadDeVertices(); i++) {
			pred.add(-1);		
		}
		return pred;
	}
	
	//9. En un grafo dirigido pesado implementar el algoritmo de Dijsktra que muestre con que vértices hay caminos de costo mínimo partiendo 
	//	desde un vértice a y con qué costo.
	public String caminoYCostoMinimoDesdeVerticeDijsktra(T verticeInicial) {
		String s="";
		int posicionVertice=posicionDeVertice(verticeInicial);
		for(int i=0; i<cantidadDeVertices(); i++) {
			if(i != posicionVertice) {
				T verticeDestino = listaDeVertices.get(i);
				s+=caminoYCostoMinimoEntreVerticesDijsktra(verticeInicial, verticeDestino)+"\n";
			}
		}
		return s;
	}
	
}
