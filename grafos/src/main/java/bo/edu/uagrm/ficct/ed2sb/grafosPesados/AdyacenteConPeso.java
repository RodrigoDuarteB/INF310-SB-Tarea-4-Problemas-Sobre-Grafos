package bo.edu.uagrm.ficct.ed2sb.grafosPesados;

public class AdyacenteConPeso implements Comparable<AdyacenteConPeso> {

	private int indiceVertice;
	private double costo;
	
	public AdyacenteConPeso(int vertice) {
		this.indiceVertice = vertice;
	}
	
	public AdyacenteConPeso(int vertice, double costo) {
		this.indiceVertice = vertice;
		this.costo = costo;
	}

	public int getIndiceVertice() {
		return indiceVertice;
	}

	public void setIndiceVertice(int indiceVertice) {
		this.indiceVertice = indiceVertice;
	}

	public double getCosto() {
		return costo;
	}

	public void setCosto(double costo) {
		this.costo = costo;
	}

	@Override
	public int compareTo(AdyacenteConPeso otroVertice) {
		Integer este = this.indiceVertice;
		Integer otro = otroVertice.getIndiceVertice();
		return este.compareTo(otro);
	}
	
	@Override
	public boolean equals(Object otro) {
		if(otro != null && this.getClass() == otro.getClass()) {
			return this.indiceVertice == ((AdyacenteConPeso)otro).getIndiceVertice();
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "("+indiceVertice+", "+costo+")";
	}

}
