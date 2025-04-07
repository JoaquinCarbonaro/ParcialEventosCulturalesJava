//Carbonaro Joaquin
package com.cultura.eventos;

import com.cultura.enums.TipoEvento;
import com.cultura.excepciones.CapacidadExcedidaException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa una conferencia, que extiende la clase {@link Evento}.
 * Esta clase contiene información específica sobre el tema y los panelistas de una conferencia.
 */
public class Conferencia extends Evento {

    // Atributos
    private String tema;
    private List<String> panelistas;

    /**
     * Constructor de la clase {@code Conferencia}.
     * Inicializa los atributos específicos de la conferencia y verifica si la capacidad máxima se ha excedido.
     *
     * @param codigo        Código único del evento.
     * @param titulo        Título del evento.
     * @param fecha         Fecha en la que se llevará a cabo el evento.
     * @param organizador   Nombre del organizador del evento.
     * @param capacidadMaxima Capacidad máxima del evento.
     * @param ubicacion     Ubicación donde se llevará a cabo el evento.
     * @param precio        Precio de entrada al evento.
     * @param asistentes    Número de asistentes al evento.
     * @param tema          Tema principal de la conferencia.
     * @param panelistas    Lista de panelistas que participarán en la conferencia.
     * @throws CapacidadExcedidaException Si el número de asistentes excede la capacidad máxima.
     */
    public Conferencia(String codigo, String titulo, LocalDate fecha, String organizador, int capacidadMaxima, String ubicacion, double precio, int asistentes, String tema, List<String> panelistas) throws CapacidadExcedidaException {
        super(codigo, titulo, fecha, organizador, capacidadMaxima, TipoEvento.CONFERENCIA, ubicacion, precio, asistentes);
        this.tema = tema;
        this.panelistas = new ArrayList<>(panelistas);

        // Verificar si la capacidad máxima se ha excedido
        if (asistentes > capacidadMaxima) {
            throw new CapacidadExcedidaException("La capacidad máxima ha sido excedida.");
        }
    }

    // Getters y Setters

    /**
     * Obtiene el tema de la conferencia.
     * 
     * @return El tema de la conferencia.
     */
    public String getTema() {
        return tema;
    }

    /**
     * Obtiene la lista de panelistas de la conferencia.
     * 
     * @return Lista de panelistas.
     */
    public List<String> getPanelistas() {
        return panelistas;
    }

    /**
     * Establece el tema de la conferencia.
     * 
     * @param tema El nuevo tema de la conferencia.
     */
    public void setTema(String tema) {
        this.tema = tema;
    }

    /**
     * Establece la lista de panelistas de la conferencia.
     * 
     * @param panelistas Lista de panelistas de la conferencia.
     */
    public void setPanelistas(List<String> panelistas) {
        this.panelistas = panelistas;
    }

    /**
     * Obtiene la cantidad de panelistas que participan en la conferencia.
     * 
     * @return La cantidad de panelistas.
     */
    public int cantidadPanelistas() {
        return panelistas.size();
    }

    /**
     * Verifica si la conferencia ha alcanzado su capacidad máxima de asistentes.
     * 
     * @return {@code true} si la conferencia está llena, {@code false} en caso contrario.
     */
    @Override
    public boolean estaLleno() {
        // Si la cantidad de asistentes es mayor o igual a la capacidad máxima, el evento está lleno
        return getAsistentes() >= getCapacidadMaxima();
    }

    /**
     * Método para generar una representación en cadena con los detalles comunes y específicos de la conferencia.
     * 
     * @return Una cadena con los detalles completos de la conferencia.
     */
    @Override
    public String toString() {
        String detalles = super.toString();
        detalles += "Tema: " + getTema() + "\n";
        detalles += "Panelistas: " + String.join(", ", getPanelistas()) + "\n";
        detalles += "Cantidad de panelistas: " + cantidadPanelistas() + "\n";
        return detalles;
    }

    /**
     * Método para mostrar los detalles de la conferencia en consola.
     * Muestra todos los detalles, incluidos los específicos de la conferencia.
     */
    @Override
    public void mostrarDetalles() {
        System.out.println(toString());
    }
}
