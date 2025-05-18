package comparatori;

import entities.Libro;

import java.util.Comparator;

public interface Ordinamento {
    Comparator<Libro> ottieniComparatore();
}
