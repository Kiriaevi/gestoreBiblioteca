package comparators;

import entities.Libro;

import java.util.Comparator;

public class OrdinamentoTitolo extends OrdinamentoAbstract{

    public OrdinamentoTitolo(boolean discendente) {
        super(discendente);
    }

    @Override
    public Comparator<Libro> ottieniComparatore() {
        return super.discendente ? Comparator.comparing(Libro::titolo).reversed() :
                Comparator.comparing(Libro::titolo);
    }
}
