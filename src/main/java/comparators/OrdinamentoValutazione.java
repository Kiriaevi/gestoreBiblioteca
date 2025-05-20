package comparators;

import entities.Libro;

import java.util.Comparator;

public class OrdinamentoValutazione extends OrdinamentoAbstract{

    public OrdinamentoValutazione(boolean discendente) {
        super(discendente);
    }

    @Override
    public Comparator<Libro> ottieniComparatore() {
        return super.discendente ? Comparator.comparing(Libro::valutazione).reversed() :
                Comparator.comparing(Libro::valutazione);
    }
}
