//Carbonaro Joaquin
package com.cultura.comparators;

import com.cultura.eventos.*;
import java.util.Comparator;


/**
 * Comparator para ordenar los eventos por su precio.
 * La comparación puede ser ascendente o descendente según el valor de la propiedad `ascendente`.
 */
public class PrecioComparator implements Comparator<Evento> {
    
    /** 
     * Indica si la comparación debe ser ascendente o descendente.
     */
    private boolean ascendente;

    /**
     * Constructor para crear un comparador de precios.
     * 
     * @param ascendente true si la comparación debe ser ascendente, false si es descendente.
     */
    public PrecioComparator(boolean ascendente) {
        this.ascendente = ascendente;
    }

    /**
     * Compara dos eventos en base a su precio y, en caso de empate, por su organizador.
     * 
     * @param e1 El primer evento a comparar.
     * @param e2 El segundo evento a comparar.
     * @return un valor negativo, cero o positivo si el primer evento es menor, igual o mayor que el segundo.
     */
    @Override
    public int compare(Evento e1, Evento e2) {
        // Compara por precio (ascendente/descendente) y luego por organizador en caso de empate
        return Comparator.comparing((Evento e) -> e.getPrecio(), 
                ascendente ? Comparator.naturalOrder() : Comparator.reverseOrder()) // Comparación por precio
                .thenComparing((Evento e) -> e.getOrganizador())  // Comparación por organizador en caso de empate
                .compare(e1, e2);
    }
}
