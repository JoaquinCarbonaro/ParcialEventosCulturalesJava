//Carbonaro Joaquin
package com.cultura.excepciones;

/**
 * Excepción personalizada para manejar casos donde un evento no se encuentra.
 * Esta excepción se lanza cuando se intenta acceder o realizar operaciones sobre un evento que no existe.
 */
public class EventoNoEncontradoException extends Exception {

    /**
     * Constructor que recibe el mensaje de la excepción.
     * 
     * @param mensaje El mensaje que describe el error o la causa de la excepción.
     */
    public EventoNoEncontradoException(String mensaje) {
        super(mensaje); // Llama al constructor de la clase base (Exception)
    }
}