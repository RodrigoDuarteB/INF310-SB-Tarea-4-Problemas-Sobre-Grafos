package bo.edu.uagrm.ficct.ed2sb.ui;

import bo.edu.uagrm.ficct.ed2sb.grafos.DiGrafo;
import bo.edu.uagrm.ficct.ed2sb.grafos.Grafo;
import bo.edu.uagrm.ficct.ed2sb.grafosPesados.DiGrafoPesado;

public class TestGrafos {
	
	public static void mostrarMatriz(boolean[][] m, int n) {
		String s;
		for(int i=0; i<n; i++) {
			s="v"+i+"->";
			for(int j=0; j<n; j++) {
				if(j+1==n) {
					s+=m[i][j];
				}else {
					s+=m[i][j]+", ";
				}
			}
			System.out.println(s);
		}
	}
	
	public static void main(String[] args) {
		Grafo<Integer> noDirigido = new Grafo<>();
		//insertar vertices
		try {
			noDirigido.insertarVertice(0);
			noDirigido.insertarVertice(1);
			noDirigido.insertarVertice(2);
			noDirigido.insertarVertice(3);
			noDirigido.insertarVertice(4);
			noDirigido.insertarVertice(5);
			noDirigido.insertarVertice(6);
//			g.insertarVertice(7);
//			g.insertarVertice(8);
		} catch (Exception e) {
			System.out.println(e);
		}
		
		//insertar aristas
		try {
			noDirigido.insertarArista(0, 1);
//			g.insertarArista(0, 2);
//			g.insertarArista(1, 2);
			noDirigido.insertarArista(1, 3);
			noDirigido.insertarArista(2, 4);
			noDirigido.insertarArista(3, 4);
			noDirigido.insertarArista(4, 5);
			noDirigido.insertarArista(5, 6);
//			g.insertarArista(7, 8);
		} catch (Exception e) {
			System.out.println(e);
		}
		System.out.println("Grafo (no dirigido)");
		System.out.println("Lista de Vertices: "+noDirigido.getListaDeVertices());
		System.out.println("Lista de Adyacencias: "+noDirigido.getListaDeAdyacencias());
		System.out.println("Cantidad de Aristas: "+noDirigido.cantidadDeAristas());
		
		System.out.println("dfs: "+noDirigido.dfs(noDirigido.getListaDeVertices().get(0)));
		
		System.out.println();
		
//		//eliminar aristas
//		try {
//			g.eliminarArista(2, 7);
//		} catch (Exception e) {
//			System.out.println(e);
//		}
//		System.out.println("Lista de Vertices: "+g.getListaDeVertices());
//		System.out.println("Lista de Adyacencias: "+g.getListaDeAdyacencias());
//		System.out.println("Cantidad de Aristas: "+g.cantidadDeAristas());
//		System.out.println("Recorrido BFS: "+g.bfs(g.getListaDeVertices().get(0)));
//		System.out.println();
		
		
		
		DiGrafo<Integer> dirigido = new DiGrafo<>();
		//insertar vertices
		try {
			dirigido.insertarVertice(0);
			dirigido.insertarVertice(1);
			dirigido.insertarVertice(2);
			dirigido.insertarVertice(3);
			dirigido.insertarVertice(4);
//			dg.insertarVertice(5);
//			dg.insertarVertice(6);
//			dg.insertarVertice(7);
//			dg.insertarVertice(8);
		} catch (Exception e) {
			System.out.println(e);
		}
		
		//insertar aristas
		try {
			dirigido.insertarArista(0, 1);
			dirigido.insertarArista(1, 3);
			dirigido.insertarArista(1, 4);
			dirigido.insertarArista(2, 2);
			dirigido.insertarArista(2, 4);
			dirigido.insertarArista(3, 1);
			dirigido.insertarArista(4, 2);
//			dg.insertarArista(5, 6);
//			dg.insertarArista(6, 7);
//			dg.insertarArista(7, 8);
		} catch (Exception e) {
			System.out.println(e);
		}
		System.out.println("DiGrafo (dirigido)");
		System.out.println("Lista de Vertices: "+dirigido.getListaDeVertices());
		System.out.println("Lista de Adyacencias: "+dirigido.getListaDeAdyacencias());
		System.out.println("Cantidad de Aristas: "+dirigido.cantidadDeAristas());
		
		
		
		//mostrarMatriz(dg.matrizCaminosWarshall(), dg.cantidadDeVertices());
		System.out.println();
		
		
		DiGrafoPesado<Integer> dirigidoPesado = new DiGrafoPesado<>();
		//insertar vertices
		try {
			dirigidoPesado.insertarVertice(0);
			dirigidoPesado.insertarVertice(1);
			dirigidoPesado.insertarVertice(2);
			dirigidoPesado.insertarVertice(3);
			dirigidoPesado.insertarVertice(4);
//			dgp.insertarVertice(5);
//			dgp.insertarVertice(6);
//			dgp.insertarVertice(7);
//			dgp.insertarVertice(8);
		} catch (Exception e) {
			System.out.println(e);
		}
		
		//insertar aristas
		try {
			dirigidoPesado.insertarArista(0, 1, 1);
			dirigidoPesado.insertarArista(1, 3, 4);
			dirigidoPesado.insertarArista(1, 4, 7);
			dirigidoPesado.insertarArista(2, 0, 3);
			dirigidoPesado.insertarArista(2, 1, 2);
			dirigidoPesado.insertarArista(2, 4, 4);
			dirigidoPesado.insertarArista(3, 0, 6);
			dirigidoPesado.insertarArista(3, 4, 2);
			dirigidoPesado.insertarArista(4, 3, 3);
//			dgp.insertarArista(7, 8, 0);
		} catch (Exception e) {
			System.out.println(e);
		}
		System.out.println("DiGrafo Pesado (dirigido)");
		System.out.println("Lista de Vertices: "+dirigidoPesado.getListaDeVertices());
		System.out.println("Lista de Adyacencias: "+dirigidoPesado.getListaDeAdyacencias());
		System.out.println("Cantidad de Aristas: "+dirigidoPesado.cantidadDeAristas());
		//7
		System.out.println(dirigidoPesado.caminoYCostoMinimoDesdeVerticeFloydWarshall(4));
		//8
		System.out.println(dirigidoPesado.caminoYCostoMinimoEntreVerticesDijsktra(4, 2));
		//9
		System.out.println(dirigidoPesado.caminoYCostoMinimoDesdeVerticeDijsktra(4));
		System.out.println();
		
		//Practico
		System.out.println("Práctico");
//		1. En un grafo dirigido implementar un algoritmo para encontrar si el grafo tiene ciclos
		System.out.println("1. Dirigido ¿Hay Ciclos?: "+dirigido.hayCiclos());
//		2. En un grafo dirigido implementar un algoritmo para encontrar si es débilmente conexo
		System.out.println("2. No dirigido ¿Es Débilmente Conexo?: "+dirigido.esDebilmenteConexo());
//		3. En un grafo no dirigido implementar un algoritmo para encontrar si el grafo tiene ciclo
		System.out.println("3. No Dirigido ¿Hay Ciclos?: "+noDirigido.hayCiclos());
//		4. En un grafo no dirigido implementar un algoritmo para encontrar el número de islas que hay en el grafo
		System.out.println("4. No Dirigido Cantidad de Islas: "+noDirigido.cantidadDeIslas());
//		5. En un grafo dirigido implementar un algoritmo para encontrar el número de islas que hay en el grafo
		System.out.println("5. Dirigido Cantidad de Islas: "+dirigido.cantidadDeIslas());
//		6. En un grafo dirigido implementar el algoritmo de Warshal, que luego muestre entre que vértices hay camino
		System.out.println("6. Algoritmo de Wharsall Caminos: ");
		mostrarMatriz(dirigido.matrizCaminosWarshall(), dirigido.cantidadDeVertices());
//		7. En un grafo dirigido usando la implementación del algoritmo de Floyd-Warsall encontrar los caminos de costo mínimo entre un 
		// vértice a y el resto. Mostrar los costos y cuáles son los caminos
		System.out.println("7. Floyd-Warsall: ");
		System.out.println(dirigidoPesado.caminoYCostoMinimoDesdeVerticeFloydWarshall(0));
//		8. En un grafo dirigido pesado implementar el algoritmo de Dijsktra que muestre cual es el camino de costo mínimo entre un vértice a y b 
		// y cual el costo
		System.out.println("8. Dijsktra Entre vertices: ");
		System.out.println(dirigidoPesado.caminoYCostoMinimoEntreVerticesDijsktra(4, 2));
//		9. En un grafo dirigido pesado implementar el algoritmo de Dijsktra que muestre con que vértices hay caminos de costo mínimo partiendo 
		// desde un vértice a y con qué costo.
		System.out.println("8. Dijsktra desde vertice: ");
		System.out.println(dirigidoPesado.caminoYCostoMinimoDesdeVerticeDijsktra(4));
		//14. En un grafo dirigido solo usando como base la lógica de un recorrido (dfs o bfs) encuentre desde que vértices se puede llegar a un 
		// vértice a.
		System.out.println(dirigido.verticesQuePuedenLlegarA(5));
		
	}
	
}
