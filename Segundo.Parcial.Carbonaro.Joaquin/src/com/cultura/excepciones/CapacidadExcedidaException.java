//Carbonaro Joaquin
package com.cultura.excepciones;

/**
 * Excepción personalizada para manejar casos donde se excede la capacidad máxima de un evento.
 * Esta excepción se lanza cuando se intenta agregar más asistentes a un evento de los que permite su capacidad máxima.
 */
public class CapacidadExcedidaException extends Exception {

    /**
     * Constructor que recibe el mensaje de la excepción.
     * 
     * @param mensaje El mensaje que describe el error o la causa de la excepción.
     */
    public CapacidadExcedidaException(String mensaje) {
        super(mensaje); // Llama al constructor de la clase base (Exception)
    }
}