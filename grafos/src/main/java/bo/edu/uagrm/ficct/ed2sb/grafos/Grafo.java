package bo.edu.uagrm.ficct.ed2sb.grafos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import bo.edu.uagrm.ficct.ed2sb.excepciones.ExcepcionAristaNoExiste;
import bo.edu.uagrm.ficct.ed2sb.excepciones.ExcepcionAristaYaExiste;
import bo.edu.uagrm.ficct.ed2sb.excepciones.ExcepcionVerticeNoExiste;
import bo.edu.uagrm.ficct.ed2sb.excepciones.ExcepcionVerticeYaExiste;

public class Grafo<T extends Comparable<T>> { //no dirigido (bidireccional)
	protected List<T> listaDeVertices;
	protected List<List<Integer>> listaDeAdyacencias;
	protected static final int POSICION_INVALIDA = -1;
	
	public Grafo() {
		this.listaDeVertices = new ArrayList<>();
		this.listaDeAdyacencias = new ArrayList<>();
	}
	
	public List<T> getListaDeVertices(){
		return this.listaDeVertices;
	}
	
	public List<List<Integer>> getListaDeAdyacencias(){
		return this.listaDeAdyacencias;
	}
	
	public void insertarVertice(T vertice) throws ExcepcionVerticeYaExiste{
		if(this.existeVertice(vertice)) {
			throw new ExcepcionVerticeYaExiste("Vertice "+String.valueOf(vertice)+" ya existe");
		}else {
			this.listaDeVertices.add(vertice);
			List<Integer> adyacentesDelVertice = new ArrayList<>();
			this.listaDeAdyacencias.add(adyacentesDelVertice);
		}
	}

	protected boolean existeVertice(T vertice) {
		return this.posicionDeVertice(vertice) != POSICION_INVALIDA;
	}
	
	protected int posicionDeVertice(T vertice) {
		if(vertice != null) {
			for(int i=0; i<this.listaDeVertices.size(); i++) {
				if(this.listaDeVertices.get(i).compareTo(vertice) == 0) {
					return i;
				}
			}
		}
		return POSICION_INVALIDA;
	}
	
	public int cantidadDeVertices() {
		return this.listaDeVertices.size();
	}
	
	public void insertarArista(T verticeOrigen, T verticeDestino) throws ExcepcionAristaYaExiste, ExcepcionVerticeNoExiste {
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
		if(posicionDeVerticeOrigen != posicionDeVerticeDestino) {
			List<Integer> adyacentesDelDestino = this.listaDeAdyacencias.get(posicionDeVerticeDestino);
			adyacentesDelDestino.add(posicionDeVerticeOrigen);
			Collections.sort(adyacentesDelDestino);
		}
	}
	
	/**
	 * 
	 * @precondicion los vertices existen en el grafo
	 * @param verticeOrigen
	 * @param verticeDestino
	 * @return true si existe adyacencia
	 */
	public boolean existeAdyacencia(T verticeOrigen, T verticeDestino) {
		int posicionDeVerticeOrigen = this.posicionDeVertice(verticeOrigen);
		int posicionDeVerticeDestino = this.posicionDeVertice(verticeDestino);
		List<Integer> adyacentesDelOrigen = this.listaDeAdyacencias.get(posicionDeVerticeOrigen);
		return adyacentesDelOrigen.contains(posicionDeVerticeDestino);
	}
	
	public int cantidadDeAristas() {
		int aristas=0;
		List<Integer> pasados = new ArrayList<>();
		for(int i=0; i<this.listaDeAdyacencias.size(); i++) {
			List<Integer> adyacenciasDeVertice = this.listaDeAdyacencias.get(i);
			for(int j=0; j<adyacenciasDeVertice.size(); j++) {
				if(!pasados.contains(adyacenciasDeVertice.get(j))) {
					aristas++;
				}
			}
			pasados.add(Integer.valueOf(i));
		}
		return aristas;
	}
	
	public void eliminarVertice(T verticeAEliminar) throws ExcepcionVerticeNoExiste {
		if(!this.existeVertice(verticeAEliminar)) {
			throw new ExcepcionVerticeNoExiste("El Vertice "+String.valueOf(verticeAEliminar)+" no existe");
		}else {
			int posicionVertice = this.posicionDeVertice(verticeAEliminar);
			this.listaDeVertices.remove(posicionVertice);
			this.listaDeAdyacencias.remove(posicionVertice);			
			for(List<Integer> adyacentesDeVertice : this.listaDeAdyacencias) {
				if(adyacentesDeVertice.contains(posicionVertice)) {
					adyacentesDeVertice.remove(posicionVertice);
				}
				for(int i=0; i<adyacentesDeVertice.size(); i++) {
					int posicionAdyacente = adyacentesDeVertice.get(i);
					if(posicionAdyacente > posicionVertice) {
						posicionAdyacente--;
						adyacentesDeVertice.set(i, posicionAdyacente);
					}
				}
			}
		}
	}
								
	public void eliminarArista(T verticeOrigen, T verticeDestino) throws ExcepcionAristaNoExiste, ExcepcionVerticeNoExiste {
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
		int posicionVerticeDestino = this.posicionDeVertice(verticeDestino); 
		List<Integer> adyacenciasDelOrigen = this.listaDeAdyacencias.get(posicionVerticeOrigen);
		adyacenciasDelOrigen.remove(Integer.valueOf(posicionVerticeDestino));
		if(posicionVerticeOrigen != posicionVerticeDestino) {
			List<Integer> adyacenciasDelDestino = this.listaDeAdyacencias.get(posicionVerticeDestino);
			adyacenciasDelDestino.remove(Integer.valueOf(posicionVerticeOrigen));
		}
	}
	
	public int gradoDe(T vertice) throws ExcepcionVerticeNoExiste { 
		if(!this.existeVertice(vertice)) {
			throw new ExcepcionVerticeNoExiste("El Vertice "+String.valueOf(vertice)+" no existe");
		}else {
			int posicionVertice = this.posicionDeVertice(vertice);
			return this.listaDeAdyacencias.get(posicionVertice).size();
		}
	}
	
	public List<T> bfs(T verticeInicial){
		List<T> recorrido = new ArrayList<>();
		if(this.existeVertice(verticeInicial)) {
			List<Boolean> marcados = this.inicializarMarcados();
			Queue<T> colaDeVertices = new LinkedList<>();
			colaDeVertices.offer(verticeInicial);
			int posicionVerticeInicial = this.posicionDeVertice(verticeInicial);
			this.marcarVertice(marcados, posicionVerticeInicial);
			do {
				T verticeEnTurno = colaDeVertices.poll();
				recorrido.add(verticeEnTurno);
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
		return recorrido;
	}

	protected boolean verticeEstaMarcado(List<Boolean> marcados, int posicionDeAdyacente) {
		return marcados.get(posicionDeAdyacente);
	}

	protected void marcarVertice(List<Boolean> marcados, int posicionVerticeInicial) {
		marcados.set(posicionVerticeInicial, Boolean.TRUE);
	}

	protected List<Boolean> inicializarMarcados() {
		List<Boolean> marcados = new ArrayList<>();
		for(int i=0; i<this.cantidadDeVertices(); i++) {
			marcados.add(Boolean.FALSE);
		}
		return marcados;
	}
	
	public List<T> dfs(T verticeInicial){
		List<T> recorrido = new ArrayList<>();
		if(this.existeVertice(verticeInicial)) {
			List<Boolean> marcados = this.inicializarMarcados();
			int posicionVerticeInicial = this.posicionDeVertice(verticeInicial);
			this.dfs(recorrido, marcados, posicionVerticeInicial);
		}
		return recorrido;
	}

	protected void dfs(List<T> recorrido, List<Boolean> marcados, int posicionEnTurno) {
		this.marcarVertice(marcados, posicionEnTurno);
		recorrido.add(this.listaDeVertices.get(posicionEnTurno));
		List<Integer> adyacenciasVerticeEnTurno = this.listaDeAdyacencias.get(posicionEnTurno);
		for(Integer posicionDeAdyacente : adyacenciasVerticeEnTurno) {
			if(!verticeEstaMarcado(marcados, posicionDeAdyacente)) {
				this.dfs(recorrido, marcados, posicionDeAdyacente);
			}
		}
	}
	
	public boolean esConexo() {
		return this.bfs(this.listaDeVertices.get(0)).size() == this.cantidadDeVertices();
	}
	
	protected boolean estanTodosMarcados(List<Boolean> marcados) {
		for(Boolean vertice : marcados) {
			if(vertice == Boolean.FALSE) {
				return false;
			}
		}
		return true;
	}
	
	public boolean esGrafoVacio() {
		return this.listaDeVertices.size() == 0;
	}
	
	//4. En un grafo no dirigido implementar un algoritmo para encontrar el número de islas que hay en el grafo
	public int cantidadDeIslas() {
		int c=0;
		if(!this.esGrafoVacio()) {
			List<T> recorrido = new ArrayList<>();
			List<Boolean> marcados = this.inicializarMarcados();
			T verticeInicial = this.listaDeVertices.get(0);
			int posicionVerticeInicial = this.posicionDeVertice(verticeInicial); 
			while(!estanTodosMarcados(marcados)) { 
				this.dfs(recorrido, marcados, posicionVerticeInicial);
				c++;
				verticeInicial = this.primerNoMarcado(marcados);
				posicionVerticeInicial = this.posicionDeVertice(verticeInicial);
			}
		}
		return c;
	}

	protected T primerNoMarcado(List<Boolean> marcados) {
		for(int i=0; i<marcados.size(); i++) {
			if(marcados.get(i) == Boolean.FALSE) {
				return this.listaDeVertices.get(i);
			}
		}
		return null;
	}
	
	//3. En un grafo no dirigido implementar un algoritmo para encontrar si el grafo tiene ciclo
	public boolean hayCiclos() {
		if(!this.esGrafoVacio()) {
			Grafo<T> auxiliar = new Grafo<>();
			for(int i=0; i<cantidadDeVertices(); i++) {
				try {
					auxiliar.insertarVertice(this.listaDeVertices.get(i));
				} catch (Exception e) {
					//
				}
			}
			List<Boolean> marcados = this.inicializarMarcados();
			T verticeInicial = this.listaDeVertices.get(0);
			int posicionVerticeInicial = this.posicionDeVertice(verticeInicial);
			boolean hayCiclo = dfs(marcados, posicionVerticeInicial, auxiliar);
			while(!hayCiclo && !estanTodosMarcados(marcados)) {
				verticeInicial = primerNoMarcado(marcados);
				posicionVerticeInicial = this.posicionDeVertice(verticeInicial);
				hayCiclo = dfs(marcados, posicionVerticeInicial, auxiliar);
			}
			return hayCiclo;
		}	
		return false;
	}
	
	protected boolean dfs(List<Boolean> marcados, int posicionEnTurno, Grafo<T> auxiliar) {
		boolean hayCiclos=false;
		this.marcarVertice(marcados, posicionEnTurno);
		List<Integer> adyacentes = this.listaDeAdyacencias.get(posicionEnTurno);
		for(Integer posicionDeAdyacente : adyacentes) {
			if(!verticeEstaMarcado(marcados, posicionDeAdyacente)) {
				try {
					auxiliar.insertarArista(this.listaDeVertices.get(posicionEnTurno), this.listaDeVertices.get(posicionDeAdyacente.intValue()));
					hayCiclos=this.dfs(marcados, posicionDeAdyacente.intValue(), auxiliar);
				} catch (Exception e) { 
					//
				}				
			}else {
				if(!auxiliar.existeAdyacencia(this.listaDeVertices.get(posicionEnTurno), this.listaDeVertices.get(posicionDeAdyacente.intValue()))){
					return true;
				}
			}
		}
		return hayCiclos;
	}
	
}
