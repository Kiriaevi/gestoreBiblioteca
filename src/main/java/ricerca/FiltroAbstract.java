package ricerca;

public abstract class FiltroAbstract implements Filtro{
    protected Filtro filtro;
    public FiltroAbstract(Filtro filtro) {
        this.filtro = filtro;
    }
}
