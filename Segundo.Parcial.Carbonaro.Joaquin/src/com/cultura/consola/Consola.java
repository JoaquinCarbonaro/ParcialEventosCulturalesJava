//Carbonaro Joaquin
package com.cultura.consola;

import com.cultura.enums.*;
import com.cultura.eventos.*;
import com.cultura.gestores.*;
import com.cultura.excepciones.*;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Clase Consola que contiene el método principal para la interacción con el usuario.
 * Se encarga de presentar un menú de opciones y realizar operaciones sobre los eventos.
 * Las operaciones incluyen registrar, modificar, eliminar, buscar, etc.
 */
public class Consola {
    /**
     * Método principal que sirve de punto de entrada para la aplicación.
     * Muestra el menú de opciones y permite al usuario interactuar con el sistema para gestionar eventos.
     * Se realizan varias operaciones basadas en la opción seleccionada por el usuario.
     * 
     * @param args Los argumentos de línea de comandos (no utilizados en este caso).
     * @throws CapacidadExcedidaException Si se intenta registrar un evento con una cantidad de asistentes superior a la capacidad máxima.
     */
    public static void main(String[] args) throws CapacidadExcedidaException {
        
        GestorEventos<Evento> gestor = new GestorEventos<>();
        Scanner scanner = new Scanner(System.in);
        int opcion;
        
        /*// Instancias para inicializar con eventos creados
        Concierto concierto1 = new Concierto("C001", "Rock en el Parque", LocalDate.of(2024, 12, 15), "Producciones Rock LTDA", 5000, "Estadio Nacional", 100.0, 3500, "Queen Revival Band", "Rock");
        Concierto concierto2 = new Concierto("C002", "Jazz Nights", LocalDate.of(2024, 11, 20), "Eventos Jazz Pro", 1000, "Sala de Conciertos", 80.0, 850, "Miles Davis Tribute", "Clásica");
        Concierto concierto3 = new Concierto("C003", "Pop Music Festival", LocalDate.of(2025, 1, 10), "PopWorld Entertainment", 3000, "Parque Central", 120.0, 2800, "Taylor Cover", "Pop");
        Concierto concierto4 = new Concierto("C004", "Electro Beats Night", LocalDate.of(2025, 2, 5), "EDM Productions", 4000, "Club Electrónico", 90.0, 3500, "DJ Electric", "Electrónica");

        Conferencia conferencia1 = new Conferencia("CF001", "Inteligencia Artificial 2024", LocalDate.of(2024, 11, 30), "Tech Future Events", 300, "Auditorio Central", 50.0, 280, "El futuro de la IA", List.of("Dr. Jane Smith", "Dr. Alan Turing", "Prof. Emily Davis"));
        Conferencia conferencia2 = new Conferencia("CF002", "Economía Verde", LocalDate.of(2024, 10, 10), "EcoGlobal Talks", 200, "Centro de Convenciones", 30.0, 190, "Sostenibilidad y Finanzas", List.of("Dr. Maria Green", "Ing. John Eco", "Prof. Laura Green"));
        Conferencia conferencia3 = new Conferencia("CF003", "Blockchain y Finanzas", LocalDate.of(2025, 3, 15), "CryptoEvents Corp", 150, "Sala Blockchain", 70.0, 140, "El impacto de Blockchain en la economía", List.of("Dr. Alex Blockchain", "Prof. Sandra Tech", "Dr. Leo FinTech"));
        Conferencia conferencia4 = new Conferencia("CF004", "Cambio Climático", LocalDate.of(2025, 4, 20), "Green Future Talks", 250, "Foro Ambiental", 40.0, 240, "Estrategias para un planeta sostenible", List.of("Dr. Carla Nature", "Ing. Marco Verde", "Prof. Eliana Climate"));

        // Agregar eventos al gestor
        try {
            gestor.add(concierto1);
            gestor.add(concierto2);
            gestor.add(concierto3);
            gestor.add(concierto4);
            gestor.add(conferencia1);
            gestor.add(conferencia2);
            gestor.add(conferencia3);
            gestor.add(conferencia4);
        } catch (CapacidadExcedidaException e) {
            System.out.println("Error al agregar eventos iniciales: " + e.getMessage());
        }*/
        
        do {
            System.out.println("\n--- Gestión de Eventos Culturales ---");
            System.out.println("1. Registrar Evento");
            System.out.println("2. Mostrar Todos los Eventos");
            System.out.println("3. Buscar Evento");
            System.out.println("4. Modificar Evento");
            System.out.println("5. Eliminar Evento");
            System.out.println("6. Guardar Eventos en Archivo");
            System.out.println("7. Cargar Eventos desde Archivo");
            System.out.println("8. Ordenar Eventos");
            System.out.println("9. Generar Estadísticas de Asistencia");
            System.out.println("10. Cargar Estadísticas de Asistencia");
            System.out.println("11. Exportar Eventos a CSV");
            System.out.println("12. Cargar Eventos a CSV");
            System.out.println("13. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine();  // Limpiar el buffer

            switch (opcion) {
                case 1:
                    // Solicitar la información del evento al usuario
                    String codigo = null, titulo = null, fechaStr = null, organizador = null, ubicacion = null;
                    int capacidadMaxima = 0, precio = 0, asistentes = 0, tipoEvento = 0;
                    LocalDate fecha = null;
                    String tema = null, artistaPrincipal = null, generoMusical = null;
                    List<String> panelistas = new ArrayList<>();

                    try {
                        System.out.print("Ingrese el código del evento: ");
                        codigo = scanner.nextLine();

                        System.out.print("Ingrese el título del evento: ");
                        titulo = scanner.nextLine();

                        System.out.print("Ingrese la fecha del evento (formato yyyy-MM-dd): ");
                        fechaStr = scanner.nextLine();
                        fecha = LocalDate.parse(fechaStr);  // Puede lanzar DateTimeParseException

                        System.out.print("Ingrese la capacidad máxima del evento: ");
                        capacidadMaxima = scanner.nextInt();
                        scanner.nextLine();  // Limpiar el buffer

                        System.out.print("Ingrese el organizador del evento: ");
                        organizador = scanner.nextLine();

                        System.out.print("Ingrese la ubicación del evento: ");
                        ubicacion = scanner.nextLine();

                        System.out.print("Ingrese el precio del evento: ");
                        precio = scanner.nextInt();

                        System.out.print("Ingrese la cantidad de asistentes del evento: ");
                        asistentes = scanner.nextInt();
                        scanner.nextLine();  // Limpiar el buffer

                        System.out.print("Ingrese el tipo de evento (1: Conferencia, 2: Concierto): ");
                        tipoEvento = scanner.nextInt();
                        scanner.nextLine();  // Limpiar el buffer

                        // Variables para detalles específicos del evento
                        switch (tipoEvento) {
                            case 1:
                                System.out.print("Ingrese el tema de la Conferencia: ");
                                tema = scanner.nextLine();

                                System.out.print("Ingrese los panelistas de la Conferencia (separados por coma): ");
                                String panelistasStr = scanner.nextLine();
                                panelistas = Arrays.asList(panelistasStr.split(","));
                                break;
                            case 2:
                                System.out.print("Ingrese el artista Principal del Concierto: ");
                                artistaPrincipal = scanner.nextLine();

                                System.out.print("Ingrese el género Musical del Concierto: ");
                                generoMusical = scanner.nextLine();
                                break;
                            default:
                                System.out.println("Tipo de evento inválido.");
                                break;
                        }

                        // Llamar al método registrarEvento del Gestor
                        boolean registrado = gestor.registrarEvento(codigo, titulo, fecha, capacidadMaxima, organizador, ubicacion, precio, asistentes, tipoEvento, tema, panelistas, artistaPrincipal, generoMusical);

                        if (!registrado) {
                            System.out.println("No se pudo registrar el evento.");
                        }

                    } catch (DateTimeParseException e) {
                        // Captura errores en la fecha
                        System.out.println("Error en el formato de fecha. Asegúrese de usar el formato yyyy-MM-dd.");
                    } catch (NumberFormatException e) {
                        // Captura errores en la conversión de enteros
                        System.out.println("Error: uno de los valores numéricos ingresados no es válido.");
                    } catch (Exception e) {
                        // Captura cualquier otra excepción no prevista
                        System.out.println("Ocurrió un error inesperado: " + e.getMessage());
                    }
                    break;
            ////////////////////////////////////////////////////////////////////////////////////////////////////////
                case 2:
                    // Mostrar Todos los Eventos
                    gestor.mostrarTodosLosEventos();
                    break;
            ////////////////////////////////////////////////////////////////////////////////////////////////////////
                case 3:
                    // Buscar Evento
                    System.out.println("Seleccione el criterio de búsqueda:");
                    System.out.println("1. Buscar Evento por Código");
                    System.out.println("2. Buscar Evento por Capacidad Restante");
                    System.out.println("3. Buscar Evento por Fecha");
                    System.out.println("4. Buscar Evento por Tipo");
                    int opcionBuscar = scanner.nextInt();
                    scanner.nextLine(); // Limpiar el buffer

                    Object valorCriterio = null;

                    switch (opcionBuscar) {
                        case 1: // Buscar por código
                            System.out.print("Ingrese el código del evento a buscar: ");
                            valorCriterio = scanner.nextLine();
                            break;
                        case 2: // Buscar por capacidad restante
                            System.out.print("Ingrese la capacidad mínima restante: ");
                            valorCriterio = scanner.nextInt();
                            scanner.nextLine(); // Limpiar el buffer
                            break;
                        case 3: // Buscar por fecha
                            System.out.print("Ingrese la fecha a buscar (formato yyyy-MM-dd): ");
                            String fechaBusquedaStr = scanner.nextLine();
                            valorCriterio = LocalDate.parse(fechaBusquedaStr);
                            break;
                        case 4: // Buscar por tipo
                            System.out.print("Ingrese el tipo de evento (1: Conferencia, 2: Concierto): ");
                            int tipoBusqueda = scanner.nextInt();
                            scanner.nextLine(); // Limpiar el buffer
                            valorCriterio = (tipoBusqueda == 1) ? TipoEvento.CONFERENCIA : TipoEvento.CONCIERTO;
                            break;
                        default:
                            System.out.println("Opción inválida.");
                            break;
                    }

                    if (valorCriterio != null) {
                        List<Evento> eventosEncontrados = gestor.buscarEventos(opcionBuscar, valorCriterio);

                        // Mostrar resultados
                        if (!eventosEncontrados.isEmpty()) {
                            System.out.println("Eventos encontrados:");
                            eventosEncontrados.forEach(Evento::mostrarDetalles);
                        } else {
                            System.out.println("No se encontraron eventos que coincidan con los criterios de búsqueda.");
                        }
                    }
                    break;
            ////////////////////////////////////////////////////////////////////////////////////////////////////////            
                case 4:
                    // Modificar Evento
                    System.out.print("Ingrese el código del evento a modificar: ");
                    String codigoModificar = scanner.nextLine();

                    // Mostrar el menú de opciones
                    System.out.println("Seleccione el atributo que desea modificar:");
                    System.out.println("1. Código");
                    System.out.println("2. Título");
                    System.out.println("3. Fecha");
                    System.out.println("4. Capacidad Máxima");
                    System.out.println("5. Organizador");
                    System.out.println("6. Ubicación");
                    System.out.println("7. Asistentes");
                    System.out.print("Opción: ");
                    int opcionModificar = scanner.nextInt();
                    scanner.nextLine();  // Limpiar el buffer

                    // Solicitar el nuevo valor
                    System.out.print("Ingrese el nuevo valor: ");
                    String nuevoValor = scanner.nextLine();

                    // Llamar al método en Gestor para realizar la modificación
                    boolean modificacionExitosa = gestor.modificarEvento(codigoModificar, opcionModificar, nuevoValor);

                    if (modificacionExitosa) {
                        System.out.println("Evento modificado con éxito.");
                    } else {
                        System.out.println("La modificación no se pudo realizar.");
                    }
                    break;
            ////////////////////////////////////////////////////////////////////////////////////////////////////////    
                case 5:
                    // Eliminar Evento
                    System.out.print("Ingrese el código del evento a eliminar: ");
                    String codigoEliminar = scanner.nextLine();

                    try {
                        // Llamamos al método del gestor para eliminar el evento
                        gestor.eliminarEvento(codigoEliminar);
                        System.out.println("Evento eliminado con éxito.");
                    } catch (EventoNoEncontradoException e) {
                        // Si no se encuentra el evento, mostramos el mensaje de error
                        System.out.println("Error al eliminar el evento: " + e.getMessage());
                    }
                    break;
            ////////////////////////////////////////////////////////////////////////////////////////////////////////         
                case 6:
                    // Guardar Eventos en Archivo
                    System.out.println("¿En qué formato desea guardar los eventos?");
                    System.out.println("1. Formato JSON");
                    System.out.println("2. Formato Binario");
                    int formatoGuardar = scanner.nextInt();
                    scanner.nextLine();  // Limpiar buffer

                    System.out.print("Ingrese el nombre del archivo para guardar los eventos (debe ser un archivo .json o .dat segun corresponda): ");
                    String nombreArchivoGuardar = scanner.nextLine();

                    boolean guardadoExitoso = false;
                    switch (formatoGuardar) {
                        case 1:
                            // Guardar en formato JSON
                            guardadoExitoso = gestor.writeJSON(nombreArchivoGuardar, gestor.getAll());
                            if (guardadoExitoso) {
                                System.out.println("Eventos guardados correctamente en formato JSON.");
                            } else {
                                System.out.println("Error al guardar los eventos en formato JSON.");
                            }
                            break;
                        case 2:
                            // Guardar en formato binario
                            guardadoExitoso = gestor.writeBinary(nombreArchivoGuardar, gestor.getAll());
                            if (guardadoExitoso) {
                                System.out.println("Eventos guardados correctamente en formato binario.");
                            } else {
                                System.out.println("Error al guardar los eventos en formato binario.");
                            }
                            break;
                        default:
                            System.out.println("Opción no válida.");
                            break;
                    }
                    break;
            ////////////////////////////////////////////////////////////////////////////////////////////////////////        
                case 7:
                    // Cargar Eventos desde Archivo
                    System.out.println("¿En qué formato desea cargar los eventos?");
                    System.out.println("1. Formato JSON");
                    System.out.println("2. Formato Binario");
                    int formatoCargar = scanner.nextInt();
                    scanner.nextLine();  // Limpiar buffer

                    System.out.print("Ingrese el nombre del archivo para cargar los eventos (debe ser un archivo .json o .dat segun corresponda): ");
                    String nombreArchivoCargar = scanner.nextLine();

                    List<Evento> eventosCargados = null;
                    switch (formatoCargar) {
                        case 1:
                            // Cargar desde formato JSON
                            try {
                                eventosCargados = gestor.readJSON(nombreArchivoCargar, new TypeToken<List<Evento>>() {});
                                if (eventosCargados == null || eventosCargados.isEmpty()) {
                                    System.out.println("No se encontraron eventos en el archivo JSON.");
                                } else {
                                    gestor.setAll(eventosCargados);  // Actualizar lista de eventos
                                    System.out.println("Eventos cargados correctamente desde el archivo JSON.");
                                }
                            } catch (IOException e) {
                                System.out.println("Error al cargar los eventos desde el archivo JSON: " + e.getMessage());
                            }
                            break;
                        case 2:
                            // Cargar desde formato binario
                            try {
                                eventosCargados = gestor.readBinary(nombreArchivoCargar);
                                gestor.setAll(eventosCargados);  // Actualizar lista de eventos
                                System.out.println("Eventos cargados correctamente desde el archivo binario.");
                            } catch (IOException | ClassNotFoundException e) {
                                System.out.println("Error al cargar los eventos desde el archivo binario: " + e.getMessage());
                            }
                            break;
                        default:
                            System.out.println("Opción no válida.");
                    }
                    break;  
            ////////////////////////////////////////////////////////////////////////////////////////////////////////
                case 8:
                    // ordenar eventos
                    System.out.println("Seleccione una opción para ordenar los eventos:");
                    System.out.println("1. Ordenar por fecha");
                    System.out.println("2. Ordenar por ubicación");
                    System.out.println("3. Ordenar por precio ascendente");
                    System.out.println("4. Ordenar por precio descendente");
                    int opcionOrdenar = scanner.nextInt();
                    scanner.nextLine();  // Limpiar el buffer

                    switch (opcionOrdenar) {
                        case 1:
                            // Ordenar por fecha
                            gestor.ordenarEventosPorFecha();
                            break;
                        case 2:
                            // Ordenar por ubicación
                            gestor.ordenarPorUbicacion();
                            break;
                        case 3:
                            // Ordenar por precio ascendente
                            gestor.ordenarPorPrecioAscendente(true);
                            break;
                        case 4:
                            // Ordenar por precio descendente
                            gestor.ordenarPorPrecioDescendente(false);
                            break;
                        default:
                            System.out.println("Opción no válida.");
                            break;
                    }
                    break;
            ////////////////////////////////////////////////////////////////////////////////////////////////////////    
                case 9:
                    // Generar Estadísticas de Asistencia (json)
                    if (gestor.generarEstadisticasAsistencia()) {
                        System.out.println("Estadísticas generadas correctamente.");
                    } else {
                        System.out.println("No se pudieron generar estadísticas.");
                    }
                    break;
            ////////////////////////////////////////////////////////////////////////////////////////////////////////
                case 10:
                    // Mostrar estadísticas de asistencia (json)
                    System.out.println("Mostrando las estadísticas de asistencia:");
                    gestor.mostrarEstadisticas("reporte_estadisticas.json");
                    break;
            ////////////////////////////////////////////////////////////////////////////////////////////////////////
                case 11:
                    // Exportar Eventos a CSV
                    System.out.print("Ingrese el nombre del directorio: ");
                    String directorioCSV = scanner.nextLine();
                    
                    System.out.print("Ingrese el nombre del archivo CSV: ");
                    String archivoCSV = scanner.nextLine();
                    
                    if (gestor.exportarEventosCSV(directorioCSV, archivoCSV)) {
                        System.out.println("Eventos exportados correctamente a CSV.");
                    } else {
                        System.out.println("Error al exportar a CSV.");
                    }
                    break;
            ////////////////////////////////////////////////////////////////////////////////////////////////////////
                case 12:
                    // Mostrar contenido del archivo CSV
                    System.out.print("Ingrese el nombre del directorio donde se encuentra el archivo CSV: ");
                    String directorioMostrarCSV = scanner.nextLine();

                    System.out.print("Ingrese el nombre del archivo CSV a mostrar (con extensión .csv): ");
                    String archivoMostrarCSV = scanner.nextLine();

                    // Llamamos al método del gestor para mostrar el contenido del archivo CSV
                    if (!gestor.mostrarContenidoCSV(directorioMostrarCSV, archivoMostrarCSV)) {
                        System.out.println("Error al mostrar el archivo CSV.");
                    }
                    break;
            ////////////////////////////////////////////////////////////////////////////////////////////////////////
                case 13:
                    // Salir
                    System.out.println("Saliendo del sistema...");
                    break;
            ////////////////////////////////////////////////////////////////////////////////////////////////////////        
                default:
                    System.out.println("Opción inválida. Intente nuevamente.");
            }

        } while (opcion != 13);
        
        scanner.close();
    }
}