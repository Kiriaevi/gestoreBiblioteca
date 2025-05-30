package ordering;

import entities.Libro;

import java.util.Comparator;

public class OrdinamentoAutore extends OrdinamentoAbstract{

    public OrdinamentoAutore(boolean discendente) {
        super(discendente);
    }

    @Override
    public Comparator<Libro> ottieniComparatore() {
        return super.discendente ? Comparator.comparing(Libro::autore).reversed() :
                Comparator.comparing(Libro::autore);
    }
}
