package bo.edu.uagrm.ficct.ed2sb.grafos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import bo.edu.uagrm.ficct.ed2sb.excepciones.ExcepcionAristaNoExiste;
import bo.edu.uagrm.ficct.ed2sb.excepciones.ExcepcionAristaYaExiste;
import bo.edu.uagrm.ficct.ed2sb.excepciones.ExcepcionVerticeNoExiste;


public class DiGrafo<T extends Comparable<T>> extends Grafo<T> { //dirigido (unidireccional)
	
	public DiGrafo() {
		super();
	}
	
	@Override
	public void insertarArista(T verticeOrigen, T verticeDestino)
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
		List<Integer> adyacentesDelOrigen = this.listaDeAdyacencias.get(posicionDeVerticeOrigen);
		adyacentesDelOrigen.add(posicionDeVerticeDestino);
		Collections.sort(adyacentesDelOrigen);
	}

	@Override
	public int cantidadDeAristas() {
		int aristas = 0;
		for(List<Integer> adyacenciasDeVertice : this.listaDeAdyacencias) {
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
		List<Integer> adyacenciasDelOrigen = this.listaDeAdyacencias.get(posicionVerticeOrigen);
		adyacenciasDelOrigen.remove(Integer.valueOf(posicionVerticeOrigen));
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
	
	//2. En un grafo dirigido implementar un algoritmo para encontrar si es débilmente conexo
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
				List<Integer> adyacentes = this.listaDeAdyacencias.get(i);
				for(Integer adyacente : adyacentes) {
					if(marcados.get(adyacente.intValue()) == Boolean.TRUE) {
						return this.listaDeVertices.get(i);
					}
				}
			}
		}
		return null;
	}
	
	//5. En un grafo dirigido implementar un algoritmo para encontrar el número de islas que hay en el grafo
	@Override
	public int cantidadDeIslas() {
		try {
			return this.convertirANoDirigido().cantidadDeIslas();
		} catch (Exception e) {
			return 0;
		}	
	}

	public Grafo<T> convertirANoDirigido() {
		Grafo<T> noDirigido = new Grafo<>();
		for(int i=0; i<this.cantidadDeVertices(); i++) {
			try {
				noDirigido.insertarVertice(this.listaDeVertices.get(i));
			} catch (Exception e) {
				//
			}
		}
		for(int i=0; i<this.cantidadDeVertices(); i++) {
			List<Integer> adyacencias = this.listaDeAdyacencias.get(i);
			T verticeActual = noDirigido.listaDeVertices.get(i);
			for(int j=0; j<adyacencias.size(); j++) {
				T verticeDestino = this.listaDeVertices.get(adyacencias.get(j).intValue());
				try {
					noDirigido.insertarArista(verticeActual, verticeDestino);
				} catch (Exception e) {
					//
				}
			}
		}
		return noDirigido;
	}
	
	// 1. En un grafo dirigido implementar un algoritmo para encontrar si el grafo tiene ciclos
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
		List<Integer> adyacentes = this.listaDeAdyacencias.get(posicionEnTurno);
		for(Integer posicionDeAdyacente : adyacentes) {
			if(!verticeEstaMarcado(marcados, posicionDeAdyacente)) {
				hayCiclos=this.dfs(marcados, posicionDeAdyacente.intValue());				
			}else {
				return true;
			}
		}
		return hayCiclos;
	}
	
	//6. En un grafo dirigido implementar el algoritmo de Warshal, que luego muestre entre que vértices hay camino
	public boolean[][] matrizCaminosWarshall() {
		int cv=cantidadDeVertices();
		boolean[][] P = matrizDeAdyacencias();
		for(int k=0; k<cv; k++) {
			for(int i=0; i<cv; i++) {
				for(int j=0; j<cv; j++) {
					P[i][j]=P[i][j] || (P[i][k] && P[k][j]);
				}
			}
		}
		return P;
	}
	
	public boolean[][] matrizDeAdyacencias() {
		int cv=cantidadDeVertices();
		boolean[][] matriz = new boolean[cv][cv];
		for(int i=0; i<cv; i++) {
			T verticeOrigen=listaDeVertices.get(i);
			for(int j=0; j<cv; j++) {
				T verticeDestino=listaDeVertices.get(j);
				if(existeAdyacencia(verticeOrigen, verticeDestino)) {
					matriz[i][j]=true;
				}
			}
		}
		return matriz;
	}
	
	//14. En un grafo dirigido solo usando como base la lógica de un recorrido (dfs o bfs) encuentre desde que vértices se puede llegar a un vértice a.
	public List<T> verticesQuePuedenLlegarA(T verticeDestino) {
		int posicionVertice=posicionDeVertice(verticeDestino);
		List<T> vertices = new ArrayList<>();
		for(int i=0; i<cantidadDeVertices(); i++) {
			if(i != posicionVertice) {
				T verticeInicial = listaDeVertices.get(i);
				List<Boolean> marcados = this.inicializarMarcados();
				Queue<T> colaDeVertices = new LinkedList<>();
				colaDeVertices.offer(verticeInicial);
				int posicionVerticeInicial = this.posicionDeVertice(verticeInicial);
				this.marcarVertice(marcados, posicionVerticeInicial);
				do {
					T verticeEnTurno = colaDeVertices.poll();
					if(verticeEnTurno == verticeDestino) {
						if(!vertices.contains(verticeEnTurno)) {
							vertices.add(verticeEnTurno);
						}
					}
					int posicionVerticeEnTurno = this.posicionDeVertice(verticeEnTurno);
					List<Integer> adyacenciasVerticeEnTurno = this.listaDeAdyacencias.get(posicionVerticeEnTurno);
					for(Integer posicionDeAdyacente : adyacenciasVerticeEnTurno) {
						if(!verticeEstaMarcado(marcados, posicionDeAdyacente)) {
							colaDeVertices.offer(this.listaDeVertices.get(posicionDeAdyacente));
							this.marcarVertice(marcados, posicionDeAdyacente);
						}
					}
				} while (!colaDeVertices.isEmpty());
			}
		}
		return vertices;
	}
	
	
}
