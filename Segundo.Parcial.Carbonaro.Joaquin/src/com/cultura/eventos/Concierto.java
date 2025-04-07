//Carbonaro Joaquin
package com.cultura.eventos;

import com.cultura.enums.TipoEvento;
import java.time.LocalDate;

/**
 * Clase que representa un concierto, extendiendo la clase {@link Evento}.
 * Esta clase agrega información específica sobre conciertos como el artista principal y el género musical.
 */
public class Concierto extends Evento {
    
    // Atributos específicos de la clase Concierto
    private String artistaPrincipal;
    private String generoMusical;

    /**
     * Constructor para crear un objeto Concierto.
     *
     * @param codigo Código único del evento.
     * @param titulo Título del evento.
     * @param fecha Fecha del evento.
     * @param organizador Organizador del evento.
     * @param capacidadMaxima Capacidad máxima de asistentes al evento.
     * @param ubicacion Ubicación donde se realizará el evento.
     * @param precio Precio de las entradas al evento.
     * @param asistentes Número de asistentes actuales al evento.
     * @param artistaPrincipal Artista principal que se presentará en el concierto.
     * @param generoMusical Género musical del concierto (ejemplo: Rock, Clásica).
     */
    public Concierto(String codigo, String titulo, LocalDate fecha, String organizador, int capacidadMaxima, String ubicacion, double precio, int asistentes, String artistaPrincipal, String generoMusical) {
        super(codigo, titulo, fecha, organizador, capacidadMaxima, TipoEvento.CONCIERTO, ubicacion, precio, asistentes);
        this.artistaPrincipal = artistaPrincipal;
        this.generoMusical = generoMusical;
    }

    // Getters y Setters

    /**
     * Obtiene el nombre del artista principal del concierto.
     *
     * @return El nombre del artista principal.
     */
    public String getArtistaPrincipal() {
        return artistaPrincipal;
    }

    /**
     * Obtiene el género musical del concierto.
     *
     * @return El género musical del concierto.
     */
    public String getGeneroMusical() {
        return generoMusical;
    }

    /**
     * Establece el nombre del artista principal del concierto.
     *
     * @param artistaPrincipal El nombre del artista principal.
     */
    public void setArtistaPrincipal(String artistaPrincipal) {
        this.artistaPrincipal = artistaPrincipal;
    }

    /**
     * Establece el género musical del concierto.
     *
     * @param generoMusical El género musical del concierto.
     */
    public void setGeneroMusical(String generoMusical) {
        this.generoMusical = generoMusical;
    }

    /**
     * Método que verifica si el concierto es de música clásica.
     *
     * @return {@code true} si el género musical es "Clásica", {@code false} en caso contrario.
     */
    public boolean esMusicaClasica() {
        return "Clásica".equalsIgnoreCase(generoMusical);
    }

    /**
     * Método que verifica si el concierto está lleno, comparando el número de asistentes con la capacidad máxima.
     *
     * @return {@code true} si la cantidad de asistentes es mayor o igual a la capacidad máxima, {@code false} en caso contrario.
     */
    @Override
    public boolean estaLleno() {
        return getAsistentes() >= getCapacidadMaxima();
    }

    /**
     * Método que genera una representación en cadena con los detalles específicos del concierto.
     * 
     * @return Una cadena con los detalles del concierto, incluyendo los detalles de la clase padre.
     */
    @Override
    public String toString() {
        String detalles = super.toString();
        detalles += "Artista principal: " + artistaPrincipal + "\n";
        detalles += "Género musical: " + generoMusical + "\n";
        detalles += "¿Es música clásica?: " + (esMusicaClasica() ? "Sí" : "No") + "\n";
        return detalles;
    }

    /**
     * Método que muestra todos los detalles del concierto, utilizando la representación en cadena generada por toString().
     */
    @Override
    public void mostrarDetalles() {
        // Llamar al toString() para mostrar todos los detalles
        System.out.println(toString());
    }

    
}
