//Carbonaro Joaquin
package com.cultura.gestores;

import com.cultura.comparators.*;
import com.cultura.enums.TipoEvento;
import com.cultura.eventos.*;
import com.cultura.excepciones.*;
import com.cultura.interfaces.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.*;

/**
 * Clase que proporciona métodos para gestionar eventos almacenados en un archivo binario.
 * 
 * @param <T> el tipo de los elementos gestionados (debe ser una subclase de {@code Evento}).
 */
public class GestorEventos <T extends Evento> implements Iterable<T>, Repository<T>, Serializer<List<Evento>> {

    //atributo
    private List<T> listaEventos;
    // constante para guardar/cargar datos desde binario
    private static final String ARCHIVO_BINARIO = "eventos.dat";

    /**
     * Constructor de la clase GestorEventos.
     * Inicializa la lista de eventos como una lista vacía.
     */
    public GestorEventos() {
        this.listaEventos = new ArrayList<>();
    }
    
    ////////////////////////////////////////////////////////////////////
    /**
     * Carga los eventos desde un archivo binario.
     * Si el archivo no existe, inicializa una lista vacía.
     * Si ocurre un error durante la carga, lo gestiona e inicializa una lista vacía en su lugar.
     * 
     * @throws ClassNotFoundException si ocurre un error al deserializar los objetos del archivo binario.
     */
    public void cargarDesdeBinario() throws ClassNotFoundException {
        File archivo = new File(GestorEventos.ARCHIVO_BINARIO);

        // Verificar si el archivo binario existe
        if (!archivo.exists()) {
            System.out.println("El archivo binario no existe.");
            this.listaEventos = new ArrayList<>(); // Inicializa una lista vacía
            return;
        }

        try {
            // Leer el contenido del archivo binario
            this.listaEventos = (List<T>) readBinary(GestorEventos.ARCHIVO_BINARIO);

            // Si el archivo está vacío o no contiene datos válidos, inicializa una lista vacía
            if (this.listaEventos == null) {
                this.listaEventos = new ArrayList<>();
            }
        } catch (IOException e) {
            System.out.println("Error al cargar el archivo binario: " + e.getMessage());
            this.listaEventos = new ArrayList<>(); // Inicializa una lista vacía en caso de error
        } catch (ClassNotFoundException e) {
            System.out.println("Error al deserializar el archivo binario: " + e.getMessage());
            throw e; // Relanza la excepción para que sea manejada por el llamador
        }
    }
    
    /**
     * Actualiza un evento en la lista con los datos del evento proporcionado.
     *
     * @param eventoActualizado el evento con los datos actualizados.
     * @throws Exception si no se encuentra el evento en la lista.
     */
    public void actualizar(Evento eventoActualizado) throws Exception {
        // Verificar que el evento esté en la lista antes de actualizar
        boolean eventoEncontrado = false;

        // Buscar el evento por código y actualizarlo
        for (int i = 0; i < this.listaEventos.size(); i++) {
            if (this.listaEventos.get(i).getCodigo().equals(eventoActualizado.getCodigo())) {
                this.listaEventos.set(i, (T) eventoActualizado); // Actualizar el evento
                eventoEncontrado = true;
                break;
            }
        }

        if (!eventoEncontrado) {
            throw new Exception("El evento con código " + eventoActualizado.getCodigo() + " no se encuentra en la lista.");
        }

        // Guardar los cambios en el archivo binario
        this.guardarEnBinario();
    }

    /**
     * Guarda la lista de eventos en un archivo binario.
     * Si ocurre un error durante la escritura, se notifica al usuario.
     */
    public void guardarEnBinario() {
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(ARCHIVO_BINARIO))) {
            output.writeObject(this.listaEventos);
            System.out.println("Los eventos se han guardado correctamente.");
        } catch (IOException e) {
            System.out.println("Error al guardar los eventos en archivo binario: " + e.getMessage());
        }
    }
    
    /**
     * Obtiene estadísticas desde un archivo JSON.
     *
     * @param fileName el nombre del archivo JSON que contiene las estadísticas.
     * @return una lista de mapas que representan las estadísticas.
     */
    public List<Map<String, Object>> obtenerEstadisticas(String fileName) {
        try {
            // Leer las estadísticas del archivo JSON
            List<Map<String, Object>> estadisticas = readJSON(fileName, new TypeToken<List<Map<String, Object>>>() {});

            if (estadisticas == null || estadisticas.isEmpty()) {
                System.out.println("No se encontraron estadísticas en el archivo.");
                return Collections.emptyList();
            }

            return estadisticas;
        } catch (IOException e) {
            System.out.println("Error al leer el archivo de estadísticas: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    ////////////////////////////////////////////////////////////////////
    /**
     * Agrega un evento a la lista, verificando que no exceda su capacidad.
     *
     * @param evento el evento a agregar.
     * @return {@code true} si el evento fue agregado exitosamente, {@code false} en caso contrario.
     * @throws CapacidadExcedidaException si el evento ya está lleno.
     */
    @Override
    public boolean add(Evento evento) throws CapacidadExcedidaException {
        if (evento.estaLleno()) {
            throw new CapacidadExcedidaException("Capacidad excedida para el evento: " + evento.getTitulo());
        }
        return this.listaEventos.add((T) evento); 
    }

    /**
     * Elimina un evento de la lista.
     *
     * @param evento el evento a eliminar.
     * @return {@code true} si el evento fue eliminado exitosamente, {@code false} en caso contrario.
     * @throws EventoNoEncontradoException si el evento no se encuentra en la lista.
     */
    @Override
    public boolean remove(Evento evento) throws EventoNoEncontradoException {
        boolean removed = this.listaEventos.remove((T) evento); 
        if (!removed) {
            throw new EventoNoEncontradoException("Evento no encontrado para ser eliminado.");
        }
        return removed;
    }

    /**
     * Obtiene todos los eventos registrados.
     *
     * @return una lista de eventos.
     */
    @Override
    public List<T> getAll() {
        return this.listaEventos; 
    }
    
    /**
     * Reemplaza la lista actual de eventos por una nueva lista.
     *
     * @param items la nueva lista de eventos.
     */
    @Override
    public void setAll(List<T> items) {
        this.listaEventos = items;
    }

    ////////////////////////////////////////////////////////////////////
    /**
     * Ordena los eventos de la lista por su fecha en orden natural, definido por la
     * implementación de {@code Comparable(compareTo)} en la clase {@code Evento}.
     */
    public void ordenarEventosPorFecha() {
        if (this.listaEventos.isEmpty()) {
            System.out.println("No hay eventos registrados.");
        } else {
            Collections.sort(this.listaEventos); // Usa Comparable para ordenar por fecha
            System.out.println("Eventos ordenados por fecha:");
            this.listaEventos.forEach(Evento::mostrarDetalles); // Imprime los detalles de cada evento
        }
    }
    
    ////////////////////////////////////////////////////////////////////
    /**
     * Ordena los eventos por ubicación en orden natural, utilizando un comparador personalizado.
     * Si varias ubicaciones son iguales, se desempata por el título del evento.
     * (Uso comparator: comparing, thenComparing, compare)
     */
    public void ordenarPorUbicacion() {
        this.listaEventos.sort(new UbicacionComparator()); // Usa el comparador personalizado
        System.out.println("Eventos ordenados por ubicación (con desempate por título):");
        this.listaEventos.forEach(Evento::mostrarDetalles); // Muestra los detalles de los eventos
    }

    /**
     * Ordena los eventos por precio en orden ascendente o descendente, según el valor proporcionado.
     * En caso de empate en el precio, se desempata por el organizador.
     *
     * @param ascendente {@code true} para ordenar en orden ascendente, {@code false} para descendente.
     */
    public void ordenarPorPrecioAscendente(boolean ascendente) {
        this.listaEventos.sort(new PrecioComparator(ascendente)); // Usa el comparador personalizado
        System.out.println("Eventos ordenados de forma ascendente por precio (con desempate por organizador):");
        this.listaEventos.forEach(Evento::mostrarDetalles);
    }
    
    /**
     * Ordena los eventos por precio en orden descendente o ascendente, según el valor proporcionado.
     * En caso de empate en el precio, se desempata por el organizador.
     *
     * @param descendente {@code true} para ordenar en orden descendente, {@code false} para ascendente.
     */
    public void ordenarPorPrecioDescendente(boolean descendente) {
        this.listaEventos.sort(new PrecioComparator(descendente)); // Usa el comparador personalizado
        System.out.println("Eventos ordenados de forma descendente por precio (con desempate por organizador):");
        this.listaEventos.forEach(Evento::mostrarDetalles);
    }
    
    ////////////////////////////////////////////////////////////////////
    /**
     * Proporciona un iterador para recorrer los eventos almacenados en la lista.
     * (uso: Iterable (iterator))
     * @return un iterador de tipo {@code Iterator<T>}.
     */
    @Override
    public Iterator<T> iterator() {
        return this.listaEventos.iterator(); // Devuelve el iterador de la lista
    }
    
    ////////////////////////////////////////////////////////////////////
    /**
     * Filtra los eventos según un criterio definido por la interfaz funcional {@code FiltroEvento}.
     *
     * @param filtro el filtro que define el criterio de selección.
     * @return una lista de eventos que cumplen con el criterio del filtro.
     */
    public List<T> filtrarEventos(FiltroEvento filtro) {
        return this.listaEventos.stream()
                .filter(filtro) // Aplica el filtro definido
                .collect(Collectors.toList()); // Devuelve los eventos que cumplen con el filtro
    }
    
    ////////////////////////////////////////////////////////////////////
     /**
     * Serializa una lista de eventos a un archivo binario.
     *
     * @param fileName Nombre del archivo de destino.
     * @param object   Lista de objetos {@link Evento} a serializar.
     * @return {@code true} si la operación fue exitosa, {@code false} en caso contrario.
     */
    @Override
    public boolean writeBinary(String fileName, List<Evento> object) {
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(fileName))) {
            output.writeObject(object);
            return true;
        } catch (IOException e) {
            System.out.println("Error al serializar a binario: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deserializa una lista de eventos desde un archivo binario.
     *
     * @param fileName Nombre del archivo de origen.
     * @return Lista deserializada de objetos {@link Evento}.
     * @throws IOException            Si ocurre un error al leer el archivo.
     * @throws ClassNotFoundException Si no se encuentra la clase durante la deserialización.
     */
    @Override
    public List<Evento> readBinary(String fileName) throws IOException, ClassNotFoundException {
        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(fileName))) {
            return (List<Evento>) input.readObject();
        }
    }
    
    ////////////////////////////////////////////////////////////////////
    /**
     * Serializa una lista de objetos a un archivo en formato JSON.
     *
     * @param fileName Nombre del archivo de destino.
     * @param object   Lista de objetos a serializar.
     * @return {@code true} si la operación fue exitosa, {@code false} en caso contrario.
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean writeJSON(String fileName, List<?> object) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, typeOfSrc, context) ->
                        new JsonPrimitive(src.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .create();

        try (Writer writer = new FileWriter(fileName)) {
            gson.toJson(object, writer);
            return true;
        } catch (IOException e) {
            System.out.println("Error al escribir el archivo JSON: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Deserializa un archivo JSON a un objeto de tipo genérico.
     *
     * @param <R>       Tipo del objeto a deserializar.
     * @param fileName  Nombre del archivo de origen.
     * @param typeToken {@link TypeToken} que especifica el tipo genérico.
     * @return Objeto deserializado del archivo JSON.
     * @throws IOException Si ocurre un error al leer el archivo.
     */
    @SuppressWarnings("unchecked")
    @Override
    public <R> R readJSON(String fileName, TypeToken<R> typeToken) throws IOException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Evento.class, new JsonDeserializer<Evento>() {
                    @Override
                    public Evento deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        JsonObject jsonObject = json.getAsJsonObject();

                        // Determinar el tipo de evento a partir del discriminador
                        TipoEvento tipoEvento = TipoEvento.valueOf(jsonObject.get("tipoEvento").getAsString());

                        // Crear el objeto adecuado según el tipo
                        switch (tipoEvento) {
                            case CONCIERTO:
                                return context.deserialize(json, Concierto.class);
                            case CONFERENCIA:
                                return context.deserialize(json, Conferencia.class);
                            default:
                                throw new JsonParseException("Tipo de evento no reconocido");
                        }
                    }
                })
                .registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
                    @Override
                    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        return LocalDate.parse(json.getAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    }
                })
                .create();

        try (Reader reader = new FileReader(fileName)) {
            return gson.fromJson(reader, typeToken.getType());
        } catch (IOException e) {
            System.out.println("Error al leer el archivo JSON: " + e.getMessage());
            throw e;
        }
    }
    
    ////////////////////////////////////////////////////////////////////
    /**
     * Genera un reporte en formato JSON con las estadísticas de asistencia de todos los eventos registrados.
     * Incluye el cálculo del porcentaje de ocupación.
     *
     * @return {@code true} si el archivo JSON se generó correctamente, {@code false} en caso contrario.
     */
    public boolean generarEstadisticasAsistencia() {
        // Verificar si la lista de eventos está vacía
        if (this.listaEventos.isEmpty()) {
            System.out.println("No hay eventos disponibles para generar estadísticas.");
            return false;
        }

        // Crear la lista de estadísticas
        List<Map<String, Object>> estadisticas = this.listaEventos.stream()
            .map(evento -> {
                // Se crea un mapa que contendrá los datos clave del evento
                Map<String, Object> datosEvento = new HashMap<>();
                datosEvento.put("codigo", evento.getCodigo());
                datosEvento.put("titulo", evento.getTitulo());
                datosEvento.put("asistentes", evento.getAsistentes());
                datosEvento.put("capacidadMaxima", evento.getCapacidadMaxima());

                // Calcular el porcentaje de ocupación
                try {
                    //Si la capacidad máxima es 0, no se puede calcular el porcentaje de ocupación (evitando división por cero).
                    double porcentajeOcupacion = (evento.getCapacidadMaxima() == 0) 
                            ? 0.0
                            : (double) evento.getAsistentes() / evento.getCapacidadMaxima() * 100;
                    datosEvento.put("porcentajeOcupacion", porcentajeOcupacion);
                } catch (ArithmeticException e) {
                    System.out.println("Error en el cálculo del porcentaje de ocupación para el evento: " + evento.getTitulo());
                    datosEvento.put("porcentajeOcupacion", "Error en el cálculo");
                }

                return datosEvento;
            })
            .collect(Collectors.toList());

        // Serializar las estadísticas a JSON
        return this.writeJSON("reporte_estadisticas.json", estadisticas);
    }

    /**
    * Muestra las estadísticas de asistencia leyendo el archivo JSON generado previamente.
    *
    * @param fileName Nombre del archivo JSON que contiene las estadísticas.
    */
   public void mostrarEstadisticas(String fileName) {
       try {
           // Leer el archivo JSON con las estadísticas
           List<Map<String, Object>> estadisticas = this.readJSON(fileName, new TypeToken<List<Map<String, Object>>>() {});

           // Verificar si las estadísticas están vacías
           if (estadisticas == null || estadisticas.isEmpty()) {
               System.out.println("No se encontraron estadísticas en el archivo.");
               return;
           }

           // Usar StringBuilder para construir la salida
           StringBuilder sb = new StringBuilder();
           sb.append("Estadísticas de Asistencia:\n");

           // Iterar sobre las estadísticas y agregar la información al StringBuilder
           for (Map<String, Object> evento : estadisticas) {
               sb.append("-----------------------------\n");
               sb.append("Código: ").append(evento.get("codigo")).append("\n");
               sb.append("Título: ").append(evento.get("titulo")).append("\n");
               sb.append("Asistentes: ").append(evento.get("asistentes")).append("\n");
               sb.append("Capacidad Máxima: ").append(evento.get("capacidadMaxima")).append("\n");
               sb.append("Porcentaje de Ocupación: ").append(evento.get("porcentajeOcupacion")).append("%\n");
           }

           // Mostrar la información construida con StringBuilder
           System.out.print(sb.toString());

       } catch (IOException e) {
           // Manejo de excepciones en caso de errores al leer el archivo
           System.out.println("Error al leer el archivo de estadísticas: " + e.getMessage());
       }
   }

    
    ////////////////////////////////////////////////////////////////////
    /**
     * Exporta los eventos registrados a un archivo en formato CSV.
     *
     * @param nombreDirectorio Directorio donde se guardará el archivo CSV.
     * @param nombreArchivo    Nombre del archivo CSV.
     * @return {@code true} si el archivo CSV se generó correctamente, {@code false} en caso contrario.
     */
    public boolean exportarEventosCSV(String nombreDirectorio, String nombreArchivo) {
        File directorio = new File(nombreDirectorio);

        // Verificar si el directorio existe o crearlo
        if (!directorio.exists() && !directorio.mkdir()) {
            System.out.println("Error: No se pudo crear el directorio.");
            return false;
        }

        File archivoCSV = new File(directorio, nombreArchivo);

        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(archivoCSV))) {
            escritor.write("Código,Título,Asistentes,Capacidad Máxima");
            escritor.newLine();

            for (Evento evento : this.listaEventos) {
                escritor.write(evento.getCodigo() + "," +
                        evento.getTitulo() + "," +
                        evento.getAsistentes() + "," +
                        evento.getCapacidadMaxima());
                escritor.newLine();
            }

            System.out.println("Archivo CSV generado correctamente: " + archivoCSV.getAbsolutePath());
            return true;

        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo CSV: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Muestra el contenido de un archivo CSV en la consola.
     *
     * @param nombreDirectorio   Directorio donde se encuentra el archivo CSV.
     * @param nombreArchivoCSV   Nombre del archivo CSV a leer.
     * @return {@code true} si el archivo se leyó correctamente, {@code false} en caso contrario.
     */
    public boolean mostrarContenidoCSV(String nombreDirectorio, String nombreArchivoCSV) {
        File archivoCSV = new File(nombreDirectorio, nombreArchivoCSV);

        if (!archivoCSV.exists()) {
            System.out.println("Error: El archivo CSV no existe.");
            return false;
        }

        try (BufferedReader lector = new BufferedReader(new FileReader(archivoCSV))) {
            String linea;
            System.out.println("Contenido del archivo CSV:");

            while ((linea = lector.readLine()) != null) {
                System.out.println(linea);
            }

            return true;

        } catch (IOException e) {
            System.out.println("Error al leer el archivo CSV: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Busca eventos en base a un criterio y un valor proporcionado.
     * Los criterios incluyen búsqueda por código, capacidad restante, fecha y tipo de evento.
     * 
     * @param criterio El criterio de búsqueda (1: Código, 2: Capacidad restante, 3: Fecha, 4: Tipo).
     * @param valor El valor asociado al criterio de búsqueda. El tipo de valor depende del criterio.
     * @return Una lista de eventos que cumplen con el criterio de búsqueda especificado.
     */
    public List<Evento> buscarEventos(int criterio, Object valor) {
        // Lista para almacenar los eventos encontrados
        List<Evento> eventosEncontrados = new ArrayList<>();

        // Se maneja el criterio de búsqueda mediante un switch
        switch (criterio) {
            case 1: // Buscar por código
                if (valor instanceof String codigo) {
                    // Usamos lambda directamente para filtrar por código
                    FiltroEvento filtroPorCodigo = evento -> evento.getCodigo().equals(codigo);
                    eventosEncontrados = (List<Evento>) this.filtrarEventos(filtroPorCodigo);
                }
                break;

            case 2: // Buscar por capacidad restante
                if (valor instanceof Integer capacidadRestante) {
                    // Usamos lambda para filtrar por capacidad restante
                    FiltroEvento filtroPorCapacidadRestante = evento -> 
                        (evento.getCapacidadMaxima() - evento.getAsistentes()) >= capacidadRestante;
                    eventosEncontrados = (List<Evento>) this.filtrarEventos(filtroPorCapacidadRestante);
                }
                break;

            case 3: // Buscar por fecha
                if (valor instanceof LocalDate fecha) {
                    // Usamos lambda para filtrar por fecha
                    FiltroEvento filtroPorFecha = evento -> evento.getFecha().equals(fecha);
                    eventosEncontrados = (List<Evento>) this.filtrarEventos(filtroPorFecha);
                }
                break;

            case 4: // Buscar por tipo de evento
                if (valor instanceof TipoEvento tipoEvento) {
                    // Usamos lambda para filtrar por tipo de evento
                    FiltroEvento filtroPorTipo = evento -> evento.getTipoEvento().equals(tipoEvento);
                    eventosEncontrados = (List<Evento>) this.filtrarEventos(filtroPorTipo);
                }
                break;

            default:
                // Si el criterio no es válido, se muestra un mensaje de error
                System.out.println("Criterio de búsqueda no válido.");
                break;
        }

        // Se devuelve la lista de eventos encontrados
        return eventosEncontrados;
    }
    
    /**
     * Muestra todos los eventos registrados en el sistema.
     * Si no hay eventos registrados, se muestra un mensaje indicando que la lista está vacía.
     */
    public void mostrarTodosLosEventos() {
        // Verificamos si la lista de eventos está vacía
        if (this.getAll().isEmpty()) {
            System.out.println("No hay eventos registrados.");
        } else {
            // Si hay eventos, iteramos sobre ellos y mostramos sus detalles
            Iterator<Evento> iterator = (Iterator<Evento>) this.iterator();
            while (iterator.hasNext()) {
                Evento evento = iterator.next();
                evento.mostrarDetalles();
            }
        }
    }
    
    /**
     * Modifica un evento existente basado en el código del evento.
     * Permite modificar varias propiedades del evento según la opción seleccionada.
     * 
     * @param codigo El código del evento a modificar.
     * @param opcionModificar La opción que indica qué propiedad del evento modificar.
     * @param nuevoValor El nuevo valor que se asignará a la propiedad.
     * @return true si la modificación fue exitosa, false si el evento no fue encontrado o la opción no es válida.
     */
    public boolean modificarEvento(String codigo, int opcionModificar, String nuevoValor) {
        // Buscar el evento por código en la lista de eventos
        Evento eventoModificar = this.getAll().stream()
            .filter(evento -> evento.getCodigo().equals(codigo))
            .findFirst()
            .orElse(null);  // Si no se encuentra el evento, retorna null

        if (eventoModificar == null) {
            // Si no se encuentra el evento, se retorna false
            return false;
        }

        // Realizar la modificación según la opción elegida
        switch (opcionModificar) {
            case 1:
                eventoModificar.setCodigo(nuevoValor);
                break;
            case 2:
                eventoModificar.setTitulo(nuevoValor);
                break;
            case 3:
                // Convertir el string a LocalDate
                LocalDate nuevaFecha = LocalDate.parse(nuevoValor);
                eventoModificar.setFecha(nuevaFecha);
                break;
            case 4:
                // Convertir el string a entero para la capacidad máxima
                int nuevaCapacidadMaxima = Integer.parseInt(nuevoValor);
                eventoModificar.setCapacidadMaxima(nuevaCapacidadMaxima);
                break;
            case 5:
                eventoModificar.setOrganizador(nuevoValor);
                break;
            case 6:
                eventoModificar.setUbicacion(nuevoValor);
                break;
            case 7:
                try {
                    int nuevosAsistentes = Integer.parseInt(nuevoValor);
                    eventoModificar.setAsistentes(nuevosAsistentes);
                } catch (CapacidadExcedidaException e) {
                    // Si hay un error de capacidad excedida, se captura la excepción
                    System.out.println(e.getMessage());
                    return false;
                }
                break;
            default:
                // Si la opción no es válida, se imprime un mensaje de error
                System.out.println("Opción no válida.");
                return false;
        }
        // Retorna true si la modificación fue exitosa
        return true;
    }

    /**
     * Registra un nuevo evento en el sistema.
     * Según el tipo de evento, se crea un evento de tipo Conferencia o Concierto.
     * 
     * @param codigo El código del evento a registrar.
     * @param titulo El título del evento.
     * @param fecha La fecha del evento.
     * @param capacidadMaxima La capacidad máxima de asistentes para el evento.
     * @param organizador El organizador del evento.
     * @param ubicacion La ubicación del evento.
     * @param precio El precio de las entradas al evento.
     * @param asistentes La cantidad de asistentes registrados para el evento.
     * @param tipoEvento El tipo de evento (1: Conferencia, 2: Concierto).
     * @param tema El tema de la conferencia, si es una conferencia.
     * @param panelistas Los panelistas que participarán en la conferencia.
     * @param artistaPrincipal El artista principal, si es un concierto.
     * @param generoMusical El género musical del concierto.
     * @return true si el evento fue registrado con éxito, false si hubo algún error.
     * @throws CapacidadExcedidaException Si se intenta registrar un evento con capacidad excedida.
     */
    public boolean registrarEvento(String codigo, String titulo, LocalDate fecha, int capacidadMaxima, String organizador, String ubicacion, int precio, int asistentes, int tipoEvento, String tema, List<String> panelistas, String artistaPrincipal, String generoMusical) throws CapacidadExcedidaException {
        Evento evento = null;
        
        // Dependiendo del tipo de evento, se crea una instancia de Conferencia o Concierto
        switch (tipoEvento) {
            case 1:
                // Si el tipo es Conferencia, se crea un evento de tipo Conferencia
                evento = new Conferencia(codigo, titulo, fecha, organizador, capacidadMaxima, ubicacion, precio, asistentes, tema, panelistas);
                break;

            case 2:
                // Si el tipo es Concierto, se crea un evento de tipo Concierto
                evento = new Concierto(codigo, titulo, fecha, organizador, capacidadMaxima, ubicacion, precio, asistentes, artistaPrincipal, generoMusical);
                break;

            default:
                // Si el tipo de evento no es válido, se muestra un mensaje de error
                System.out.println("Tipo de evento inválido.");
                return false;
        }

        try {
            // Agregar el evento a la lista de eventos
            this.add(evento);
            System.out.println("Evento registrado con éxito.");
            return true;
        } catch (CapacidadExcedidaException e) {
            // Si la capacidad es excedida, se captura la excepción y muestra el mensaje de error
            System.out.println("Error al registrar el evento: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Elimina un evento del sistema basado en el código del evento.
     * Si el evento no se encuentra, se lanza una excepción.
     * 
     * @param codigo El código del evento a eliminar.
     * @throws EventoNoEncontradoException Si no se encuentra un evento con el código proporcionado.
     */
    public void eliminarEvento(String codigo) throws EventoNoEncontradoException {
        // Buscar el evento a eliminar por código
        Evento eventoAEliminar = listaEventos.stream()
            .filter(evento -> evento.getCodigo().equals(codigo))
            .findFirst()
            .orElse(null);  // Si no se encuentra el evento, retorna null

        if (eventoAEliminar == null) {
            // Si no se encuentra el evento, se lanza una excepción
            throw new EventoNoEncontradoException("No se encontró un evento con el código: " + codigo);
        }

        // Si el evento se encuentra, se elimina de la lista
        listaEventos.remove(eventoAEliminar);
    }

    
}