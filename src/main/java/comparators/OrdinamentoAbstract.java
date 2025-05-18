package comparators;

import entities.Libro;

import java.util.Comparator;

public abstract class OrdinamentoAbstract implements Ordinamento{
    protected boolean discendente = false;
    public OrdinamentoAbstract(boolean discendente) {
        this.discendente = discendente;
    }
    @Override
    public abstract Comparator<Libro> ottieniComparatore();
}
