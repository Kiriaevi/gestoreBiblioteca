package ordering;

import entities.Libro;

import java.util.Comparator;

public abstract class OrdinamentoAbstract implements Ordinamento{
    protected boolean discendente;
    public OrdinamentoAbstract(boolean discendente) {
        this.discendente = discendente;
    }
    @Override
    public abstract Comparator<Libro> ottieniComparatore();
}
