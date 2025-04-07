//Carbonaro Joaquin
package com.cultura.interfaces;

import com.cultura.eventos.*;
import java.util.function.Predicate;

/**
 * Interfaz funcional que representa un filtro para los eventos.
 * Esta interfaz extiende `Predicate<Evento>`, permitiendo aplicar un filtro específico a un evento.
 * 
 * @functionalInterface
 * La interfaz define el método `test`, que evalúa si un evento cumple con el criterio de filtrado.
 */
@FunctionalInterface
public interface FiltroEvento extends Predicate<Evento> {

    /**
     * Este método permite aplicar un filtro sobre los eventos.
     * Al heredar de Predicate<Evento>, la interfaz ya proporciona una implementación del método
     * `test()`, que evalúa si un evento cumple con el criterio del filtro.
     * 
     * @param evento El evento a evaluar.
     * @return true si el evento cumple con el criterio de filtrado, false en caso contrario.
     */
    @Override
    boolean test(Evento evento);
}