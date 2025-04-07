//Carbonaro Joaquin
package com.cultura.comparators;

import com.cultura.eventos.*;
import java.util.Comparator;

/**
 * Comparator para ordenar los eventos por su ubicación.
 * La comparación se realiza primero por ubicación y, en caso de empate, por título.
 */
public class UbicacionComparator implements Comparator<Evento> {

    /**
     * Compara dos eventos en base a su ubicación y, en caso de empate, por su título.
     * 
     * @param e1 El primer evento a comparar.
     * @param e2 El segundo evento a comparar.
     * @return un valor negativo, cero o positivo si el primer evento es menor, igual o mayor que el segundo.
     */
    @Override
    public int compare(Evento e1, Evento e2) {
        // Compara primero por ubicación y luego por título en caso de empate
        return Comparator.comparing((Evento e) -> e.getUbicacion()) // Comparación por ubicación
                .thenComparing((Evento e) -> e.getTitulo())  // Comparación por título en caso de empate
                .compare(e1, e2);
    }
}