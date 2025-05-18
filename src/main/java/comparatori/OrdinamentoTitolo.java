package comparatori;

import entities.Libro;

import java.util.Comparator;

public class OrdinamentoTitolo extends OrdinamentoAbstract{

    public OrdinamentoTitolo(boolean discendente) {
        super(discendente);
    }

    @Override
    public Comparator<Libro> ottieniComparatore() {
        return super.discendente ? Comparator.comparing(Libro::getTitolo).reversed() :
                Comparator.comparing(Libro::getTitolo);
    }
}
