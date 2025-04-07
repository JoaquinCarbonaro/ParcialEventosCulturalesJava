//Carbonaro Joaquin
package com.cultura.eventos;

import com.cultura.enums.*;
import com.cultura.excepciones.CapacidadExcedidaException;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Clase abstracta que representa un evento, implementando {@link Serializable} y {@link Comparable<Evento>}.
 * Esta clase proporciona la estructura base para los diferentes tipos de eventos, como conciertos y conferencias.
 * Los eventos incluyen detalles como el código, título, fecha, organizador, capacidad, y más.
 * Además, permite comparar eventos por su fecha y manejar la capacidad de asistentes.
 */
public abstract class Evento implements Serializable, Comparable<Evento> {

    // serialVersionUID para asegurar compatibilidad entre versiones
    private static final long serialVersionUID = 1L;

    // Atributos
    private String codigo;
    private String titulo;
    private LocalDate fecha;
    private String organizador;
    private int capacidadMaxima;
    private TipoEvento tipoEvento;  // ATRIBUTO para enum
    private String ubicacion; // ATRIBUTO para comparator
    private double precio;    // ATRIBUTO para comparator
    private int asistentes; // Atributo para manejo de capacidad

    /**
     * Constructor de la clase {@code Evento}.
     * Inicializa los atributos de un evento con los valores proporcionados.
     *
     * @param codigo Código único del evento.
     * @param titulo Título del evento.
     * @param fecha Fecha en que se llevará a cabo el evento.
     * @param organizador Organizador responsable del evento.
     * @param capacidadMaxima Capacidad máxima de asistentes al evento.
     * @param tipoEvento Tipo de evento (por ejemplo, concierto, conferencia).
     * @param ubicacion Ubicación donde se realizará el evento.
     * @param precio Precio de entrada al evento.
     * @param asistentes Número actual de asistentes al evento.
     */
    public Evento(String codigo, String titulo, LocalDate fecha, String organizador, int capacidadMaxima, TipoEvento tipoEvento, String ubicacion, double precio, int asistentes) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.fecha = fecha;
        this.organizador = organizador;
        this.capacidadMaxima = capacidadMaxima;
        this.tipoEvento = tipoEvento;
        this.ubicacion = ubicacion;
        this.precio = precio;
        this.asistentes = asistentes;
    }

    // Getters y Setters

    /**
     * Obtiene el código del evento.
     * 
     * @return El código del evento.
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Establece el código del evento.
     * 
     * @param codigo El nuevo código del evento.
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * Obtiene el título del evento.
     * 
     * @return El título del evento.
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Establece el título del evento.
     * 
     * @param titulo El nuevo título del evento.
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Obtiene la fecha del evento.
     * 
     * @return La fecha del evento.
     */
    public LocalDate getFecha() {
        return fecha;
    }

    /**
     * Establece la fecha del evento.
     * 
     * @param fecha La nueva fecha del evento.
     */
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    /**
     * Obtiene el organizador del evento.
     * 
     * @return El organizador del evento.
     */
    public String getOrganizador() {
        return organizador;
    }

    /**
     * Establece el organizador del evento.
     * 
     * @param organizador El nuevo organizador del evento.
     */
    public void setOrganizador(String organizador) {
        this.organizador = organizador;
    }

    /**
     * Obtiene la capacidad máxima del evento.
     * 
     * @return La capacidad máxima del evento.
     */
    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    /**
     * Establece la capacidad máxima del evento.
     * 
     * @param capacidadMaxima La nueva capacidad máxima del evento.
     */
    public void setCapacidadMaxima(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }

    /**
     * Obtiene el tipo de evento (por ejemplo, concierto o conferencia).
     * 
     * @return El tipo de evento.
     */
    public TipoEvento getTipoEvento() {
        return tipoEvento;
    }

    /**
     * Establece el tipo de evento.
     * 
     * @param tipoEvento El nuevo tipo de evento.
     */
    public void setTipoEvento(TipoEvento tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    /**
     * Obtiene la ubicación del evento.
     * 
     * @return La ubicación del evento.
     */
    public String getUbicacion() {
        return ubicacion;
    }

    /**
     * Establece la ubicación del evento.
     * 
     * @param ubicacion La nueva ubicación del evento.
     * @throws IllegalArgumentException Si la ubicación está vacía o es nula.
     */
    public void setUbicacion(String ubicacion) {
        if (ubicacion == null || ubicacion.isEmpty()) {
            throw new IllegalArgumentException("La ubicación no puede estar vacía.");
        }
        this.ubicacion = ubicacion;
    }

    /**
     * Obtiene el precio del evento.
     * 
     * @return El precio del evento.
     */
    public double getPrecio() {
        return precio;
    }

    /**
     * Establece el precio del evento.
     * 
     * @param precio El nuevo precio del evento.
     * @throws IllegalArgumentException Si el precio es negativo.
     */
    public void setPrecio(double precio) {
        if (precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo.");
        }
        this.precio = precio;
    }

    /**
     * Obtiene el número de asistentes actuales al evento.
     * 
     * @return El número de asistentes actuales al evento.
     */
    public int getAsistentes() {
        return asistentes;
    }

    /**
     * Establece el número de asistentes al evento.
     * 
     * @param asistentes El nuevo número de asistentes.
     * @throws CapacidadExcedidaException Si el número de asistentes excede la capacidad máxima del evento.
     */
    public void setAsistentes(int asistentes) throws CapacidadExcedidaException {
        if (asistentes > capacidadMaxima) {
            throw new CapacidadExcedidaException("Capacidad excedida para el evento: " + this.getTitulo());
        }
        this.asistentes = asistentes;
    }
    
    /**
     * Método para obtener la capacidad restante del evento.
     * Este método no está implementado en esta clase base.
     * 
     * @return La capacidad restante del evento.
     * @throws UnsupportedOperationException Si se intenta llamar a este método sin implementación.
     */
    public int getCapacidadRestante() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Método para obtener el tipo de evento.
     * Este método no está implementado en esta clase base.
     * 
     * @return El tipo de evento.
     * @throws UnsupportedOperationException Si se intenta llamar a este método sin implementación.
     */
    public TipoEvento getTipo() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    // Métodos abstractos

    /**
     * Método abstracto que determina si el evento está lleno, basado en la capacidad y el número de asistentes.
     * 
     * @return {@code true} si el evento está lleno, {@code false} en caso contrario.
     */
    public abstract boolean estaLleno();

    /**
     * Método abstracto para mostrar los detalles del evento.
     * Este método debe ser implementado en las clases que extienden {@code Evento}.
     */
    public abstract void mostrarDetalles();

    // Método compareTo para comparar eventos por fecha

    /**
     * Compara dos eventos basados en su fecha.
     * 
     * @param otroEvento El evento con el cual comparar.
     * @return Un valor negativo si este evento es anterior al otro, 
     *         cero si son iguales, o un valor positivo si este evento es posterior al otro.
     */
    @Override
    public int compareTo(Evento otroEvento) {
        return this.fecha.compareTo(otroEvento.fecha); // Compara las fechas de los eventos
    }
    
    /**
     * Método que genera una representación en cadena con los detalles comunes de todos los eventos.
     * 
     * @return Una cadena con los detalles del evento.
     */
    @Override
    public String toString() {
        StringBuilder detalles = new StringBuilder();
        detalles.append("Código: ").append(codigo).append("\n");
        detalles.append("Título: ").append(titulo).append("\n");
        detalles.append("Fecha: ").append(fecha).append("\n");
        detalles.append("Organizador: ").append(organizador).append("\n");
        detalles.append("Capacidad máxima: ").append(capacidadMaxima).append("\n");
        detalles.append("Ubicación: ").append(ubicacion).append("\n");
        detalles.append("Precio: ").append(precio).append("\n");
        detalles.append("Asistentes: ").append(asistentes).append("/").append(capacidadMaxima).append("\n");

        detalles.append("¿Está lleno?: ").append(estaLleno() ? "Sí" : "No").append("\n");

        return detalles.toString();
    }
    
}