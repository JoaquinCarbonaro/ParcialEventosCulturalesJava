//Carbonaro Joaquin
package com.cultura.interfaces;

import com.cultura.excepciones.*;
import java.util.List;

/**
 * Interfaz genérica que define operaciones básicas para un repositorio de objetos.
 * Permite agregar, eliminar y obtener elementos de tipo `T`.
 * 
 * @param <T> El tipo de objeto que maneja el repositorio, por ejemplo, `Evento`.
 */
public interface Repository<T> {

    /**
     * Agrega un nuevo elemento al repositorio.
     * 
     * @param item El elemento que se desea agregar al repositorio.
     * @return true si el elemento fue agregado exitosamente, false en caso contrario.
     * @throws CapacidadExcedidaException Si la capacidad máxima del repositorio es excedida.
     */
    boolean add(T item) throws CapacidadExcedidaException;

    /**
     * Elimina un elemento del repositorio.
     * 
     * @param item El elemento que se desea eliminar del repositorio.
     * @return true si el elemento fue eliminado exitosamente, false en caso contrario.
     * @throws EventoNoEncontradoException Si el evento no se encuentra en el repositorio.
     */
    boolean remove(T item) throws EventoNoEncontradoException;

    /**
     * Obtiene todos los elementos del repositorio.
     * 
     * @return Una lista con todos los elementos del repositorio.
     */
    List<T> getAll();

    /**
     * Establece una lista de elementos en el repositorio, reemplazando la lista existente.
     * 
     * @param items La lista de elementos que se establecerán en el repositorio.
     */
    void setAll(List<T> items);
}
