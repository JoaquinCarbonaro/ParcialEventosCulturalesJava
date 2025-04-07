//Carbonaro Joaquin
package com.cultura.vista;

import com.cultura.eventos.*;
import com.cultura.excepciones.CapacidadExcedidaException;
import com.cultura.excepciones.EventoNoEncontradoException;
import com.cultura.gestores.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Clase principal de la aplicación de gestión de eventos, que extiende {@link Application}.
 * Esta clase gestiona la interfaz gráfica de usuario (GUI) utilizando JavaFX y se encarga de la inicialización 
 * y la carga de eventos en la vista, así como la interacción con el gestor de eventos.
 */
public class AplicacionGestionEventos extends Application {
    
    /** Gestor de eventos (controlador) que maneja las operaciones sobre los eventos. */
    private GestorEventos<Evento> gestor = new GestorEventos<>();

    /** Vista de lista de eventos, utilizada para mostrar los eventos cargados. */
    private ListView<Evento> vistaListaEventos = new ListView<>();

    /**
     * Método de inicio de la aplicación. Crea la interfaz de usuario, carga los eventos existentes desde almacenamiento 
     * y establece el contenedor principal de la vista.
     *
     * @param escenarioPrincipal El escenario principal donde se mostrará la interfaz gráfica.
     * @throws IOException Si ocurre un error al cargar los datos desde el archivo.
     * @throws ClassNotFoundException Si no se encuentra la clase necesaria durante la carga.
     */
    @Override
    public void start(Stage escenarioPrincipal) throws IOException, ClassNotFoundException {

        // Cargar datos existentes
        this.gestor.cargarDesdeBinario();

        // Contenedor principal
        VBox layoutPrincipal = new VBox(10);
        layoutPrincipal.setPadding(new Insets(10));

        // Campos de entrada
        TextField campoCodigo = new TextField();
        campoCodigo.setPromptText("Código del Evento");

        TextField campoTitulo = new TextField();
        campoTitulo.setPromptText("Título del Evento");

        TextField campoFecha = new TextField();
        campoFecha.setPromptText("Fecha del Evento (formato yyyy-MM-dd)");

        TextField campoOrganizador = new TextField();
        campoOrganizador.setPromptText("Organizador");

        TextField campoCapacidadMaxima = new TextField();
        campoCapacidadMaxima.setPromptText("Capacidad Máxima");
        
        TextField campoUbicacion = new TextField();
        campoUbicacion.setPromptText("Ubicación");
        
        TextField campoPrecio = new TextField();
        campoPrecio.setPromptText("Precio");

        TextField campoAsistentes = new TextField();
        campoAsistentes.setPromptText("Cantidad de asistentes");

        // ComboBox para seleccionar el tipo de evento
        ComboBox<String> comboTipoEvento = new ComboBox<>();
        comboTipoEvento.getItems().addAll("Concierto", "Conferencia");
        comboTipoEvento.setPromptText("Tipo de Evento");

        // Campos adicionales según el tipo de evento seleccionado
        TextField campoArtistaPrincipal = new TextField();
        campoArtistaPrincipal.setPromptText("Artista Principal");
        campoArtistaPrincipal.setDisable(true); // Inicialmente deshabilitado

        TextField campoGeneroMusical = new TextField();
        campoGeneroMusical.setPromptText("Género Musical");
        campoGeneroMusical.setDisable(true); // Inicialmente deshabilitado

        TextField campoTema = new TextField();
        campoTema.setPromptText("Tema de la Conferencia");
        campoTema.setDisable(true); // Inicialmente deshabilitado

        TextField campoPanelistas = new TextField();
        campoPanelistas.setPromptText("Panelistas (separados por coma)");
        campoPanelistas.setDisable(true); // Inicialmente deshabilitado

        // Evento para habilitar o deshabilitar campos según el tipo de evento seleccionado
        comboTipoEvento.setOnAction(e -> {
            if (comboTipoEvento.getValue().equals("Concierto")) {
                campoArtistaPrincipal.setDisable(false);
                campoGeneroMusical.setDisable(false);
                campoTema.setDisable(true);
                campoPanelistas.setDisable(true);
            } else if (comboTipoEvento.getValue().equals("Conferencia")) {
                campoTema.setDisable(false);
                campoPanelistas.setDisable(false);
                campoArtistaPrincipal.setDisable(true);
                campoGeneroMusical.setDisable(true);
            }
        });

        // Botón Agregar Evento
        Button botonAgregar = new Button("Agregar Evento");
        botonAgregar.setOnAction(e -> {
            try {
                String codigo = campoCodigo.getText();
                String titulo = campoTitulo.getText();
                
                // Convertir la fecha de String a LocalDate
                LocalDate fecha = convertirAFecha(campoFecha.getText());
                String organizador = campoOrganizador.getText();
                int capacidadMaxima = Integer.parseInt(campoCapacidadMaxima.getText());
                String ubicacion = campoUbicacion.getText();

                // Convertir el precio a double
                double precio = 0.0;
                try {
                    precio = Double.parseDouble(campoPrecio.getText());
                } catch (NumberFormatException ex) {
                    mostrarAlerta("Entrada Inválida", "Por favor, ingrese un precio válido.");
                    return;
                }

                int asistentes = Integer.parseInt(campoAsistentes.getText());
                String tipoEvento = comboTipoEvento.getValue();

                // Crear lista de panelistas desde el campo de texto
                List<String> listaPanelistas = new ArrayList<>();
                if (tipoEvento.equals("Conferencia")) {
                    String panelistasInput = campoPanelistas.getText();
                    if (!panelistasInput.isEmpty()) {
                        // Separar la cadena de panelistas por coma y eliminar espacios innecesarios
                        listaPanelistas = Arrays.stream(panelistasInput.split(","))
                                                .map(String::trim) // Elimina espacios antes y después de cada nombre
                                                .collect(Collectors.toList());
                    }
                }

                Evento nuevoEvento;
                if (tipoEvento.equals("Concierto")) {
                    String artistaPrincipal = campoArtistaPrincipal.getText();
                    String generoMusical = campoGeneroMusical.getText();
                    nuevoEvento = new Concierto(codigo, titulo, fecha, organizador, capacidadMaxima, ubicacion, precio, asistentes, artistaPrincipal, generoMusical);
                } else {
                    String tema = campoTema.getText();
                    try {
                        // Aquí se lanza la excepción CapacidadExcedidaException si los asistentes exceden la capacidad
                        nuevoEvento = new Conferencia(codigo, titulo, fecha, organizador, capacidadMaxima, ubicacion, precio, asistentes, tema, listaPanelistas);
                    } catch (CapacidadExcedidaException ex) {
                        mostrarAlerta("Error de Capacidad", "La capacidad máxima del evento ha sido excedida.");
                        return;  // Salir del método si la excepción se lanza
                    }
                }

                try {
                    this.gestor.add(nuevoEvento);
                } catch (CapacidadExcedidaException ex) {
                    mostrarAlerta("Error de Capacidad", "La capacidad del evento ha sido excedida.");
                }
                // Guardar la lista de eventos en el archivo binario después de agregar el nuevo evento
                this.gestor.guardarEnBinario();
        
                actualizarListaEventos();
                limpiarCampos(campoCodigo, campoTitulo, campoFecha, campoCapacidadMaxima, campoOrganizador, campoUbicacion, campoAsistentes, campoPrecio, campoArtistaPrincipal, campoGeneroMusical, campoTema, campoPanelistas);
            
            } catch (NumberFormatException ex) {
                mostrarAlerta("Entrada Inválida", "Por favor, ingrese datos válidos.");
            }
        });

        // Botón Actualizar Evento
        Button botonActualizar = new Button("Actualizar Evento");
        botonActualizar.setOnAction(e -> {
            Evento eventoSeleccionado = vistaListaEventos.getSelectionModel().getSelectedItem();
            if (eventoSeleccionado != null) {
                try {
                    // Obtener los valores de los campos de texto y convertirlos adecuadamente
                    String codigo = campoCodigo.getText();
                    String titulo = campoTitulo.getText();
                    LocalDate fecha = convertirAFecha(campoFecha.getText());
                    if (fecha == null) {  // Si la fecha es inválida, no continuar
                        return;
                    }

                    String organizador = campoOrganizador.getText();
                    int capacidadMaxima = Integer.parseInt(campoCapacidadMaxima.getText());
                    String ubicacion = campoUbicacion.getText();

                    // Convertir el precio a double y manejar posibles excepciones
                    double precio = 0.0;
                    try {
                        precio = Double.parseDouble(campoPrecio.getText());
                    } catch (NumberFormatException ex) {
                        mostrarAlerta("Entrada Inválida", "Por favor, ingrese un precio válido.");
                        return;
                    }

                    int asistentes = Integer.parseInt(campoAsistentes.getText());
                    String tipoEvento = comboTipoEvento.getValue();

                    // Crear lista de panelistas desde el campo de texto
                    List<String> listaPanelistas = new ArrayList<>();
                    if (tipoEvento.equals("Conferencia")) {
                        String panelistasInput = campoPanelistas.getText();
                        if (!panelistasInput.isEmpty()) {
                            // Separar la cadena de panelistas por coma y eliminar espacios innecesarios
                            listaPanelistas = Arrays.stream(panelistasInput.split(","))
                                                    .map(String::trim) // Elimina espacios antes y después de cada nombre
                                                    .collect(Collectors.toList());
                        }
                    }

                    // Actualizar el evento seleccionado según el tipo de evento
                    if (tipoEvento.equals("Concierto")) {
                        // Si es un concierto, actualizamos los atributos correspondientes
                        String artistaPrincipal = campoArtistaPrincipal.getText();
                        String generoMusical = campoGeneroMusical.getText();
                        Concierto conciertoActualizado = (Concierto) eventoSeleccionado;
                        conciertoActualizado.setArtistaPrincipal(artistaPrincipal);
                        conciertoActualizado.setGeneroMusical(generoMusical);
                    } else if (tipoEvento.equals("Conferencia")) {
                        // Si es una conferencia, actualizamos los atributos correspondientes
                        String tema = campoTema.getText();
                        Conferencia conferenciaActualizada = (Conferencia) eventoSeleccionado;
                        conferenciaActualizada.setTema(tema);
                        conferenciaActualizada.setPanelistas(listaPanelistas);
                    }

                    // Actualizar los atributos comunes del evento
                    eventoSeleccionado.setCodigo(codigo);
                    eventoSeleccionado.setTitulo(titulo);
                    eventoSeleccionado.setFecha(fecha);
                    eventoSeleccionado.setCapacidadMaxima(capacidadMaxima);
                    eventoSeleccionado.setOrganizador(organizador);
                    eventoSeleccionado.setUbicacion(ubicacion);
                    eventoSeleccionado.setPrecio(precio);

                    // Manejar la excepción CapacidadExcedidaException al actualizar los asistentes
                    try {
                        eventoSeleccionado.setAsistentes(asistentes);
                    } catch (CapacidadExcedidaException ex) {
                        mostrarAlerta("Error de Capacidad", "La capacidad del evento ha sido excedida.");
                        return;  // Salir del método si la excepción se lanza
                    }

                    // Llamar al método para actualizar el evento en el controlador
                    try {
                        this.gestor.actualizar(eventoSeleccionado);
                        mostrarAlerta("Éxito", "El evento se actualizó correctamente.");
                    } catch (Exception ex) {
                        mostrarAlerta("Error", "No se pudo actualizar el evento: " + ex.getMessage());
                    }

                    // Actualizar la lista de eventos en la interfaz y limpiar los campos
                    actualizarListaEventos();
                    limpiarCampos(campoCodigo, campoTitulo, campoFecha, campoCapacidadMaxima, campoOrganizador, campoUbicacion, campoAsistentes, campoPrecio, campoArtistaPrincipal, campoGeneroMusical, campoTema, campoPanelistas);

                } catch (NumberFormatException ex) {
                    mostrarAlerta("Entrada Inválida", "Por favor, ingrese datos válidos.");
                }
            }
        });

        // Botón Eliminar Evento
        Button botonEliminar = new Button("Eliminar Evento");
            botonEliminar.setOnAction(e -> {
                Evento eventoSeleccionado = vistaListaEventos.getSelectionModel().getSelectedItem();
                if (eventoSeleccionado != null) {
                    try {
                        // Eliminar el evento a través del controlador
                        this.gestor.remove(eventoSeleccionado);

                        // Guardar cambios en el archivo binario (persistente)
                        this.gestor.guardarEnBinario();

                        mostrarAlerta("Éxito", "El evento ha sido eliminado correctamente.");
                        actualizarListaEventos();
                        limpiarCampos(campoCodigo, campoTitulo, campoFecha, campoCapacidadMaxima, campoOrganizador, campoUbicacion, campoAsistentes, campoPrecio, campoArtistaPrincipal, campoGeneroMusical, campoTema, campoPanelistas);
                    } catch (EventoNoEncontradoException ex) {
                        mostrarAlerta("Error", "No se pudo eliminar el evento: " + ex.getMessage());
                    }
                } else {
                    mostrarAlerta("Advertencia", "Por favor, seleccione un evento para eliminar.");
                }
            });
        
        // Botón Estadística de Asistencia
        Button botonEstadistica = new Button("Generar estadística de asistencia");
        botonEstadistica.setOnAction(e -> {
        try {
            // Intentar generar las estadísticas
            this.gestor.generarEstadisticasAsistencia();

            // Mostrar alerta de éxito
            mostrarAlerta("Éxito", "La estadística de asistencia se ha generado correctamente.");
        } catch (Exception ex) {
            // Manejo genérico de cualquier otro tipo de error
            mostrarAlerta("Error inesperado", "Ocurrió un error al generar las estadísticas: " + ex.getMessage());
        }
    });
        
        // Botón Limpiar Campos
        Button botonLimpiarCampos = new Button("Limpiar Campos");
        botonLimpiarCampos.setOnAction(e -> {
            // Limpiar todos los campos de texto
            limpiarCampos(campoCodigo, campoTitulo, campoFecha, campoCapacidadMaxima, campoOrganizador, campoUbicacion, campoAsistentes, campoPrecio, campoArtistaPrincipal, campoGeneroMusical, campoTema, campoPanelistas);
            
        });
        
        // Botón Mostrar Estadísticas
        Button botonMostrarEstadisticas = new Button("Mostrar Estadísticas de Asistencia");
        botonMostrarEstadisticas.setOnAction(e -> {
            // Ruta del archivo JSON
            String rutaArchivoEstadisticas = "reporte_estadisticas.json";

            // Obtener las estadísticas
            List<Map<String, Object>> estadisticas = this.gestor.obtenerEstadisticas(rutaArchivoEstadisticas);

            // Mostrar la ventana con las estadísticas
            mostrarVentanaEstadisticas(estadisticas);
        });

        // Configuración de la vista de lista de eventos
        vistaListaEventos.setCellFactory(param -> new ListCell<>() {
            /**
             * Este método actualiza el contenido de una celda de la lista de eventos.
             * Se utiliza para mostrar la información detallada de cada evento en la vista de lista.
             * Si el evento es nulo o la celda está vacía, la celda se limpia. 
             * Si el evento está presente, se muestra la información detallada.
             *
             * @param evento El evento cuyo contenido se va a mostrar en la celda.
             * @param vacio Indica si la celda está vacía (true) o tiene contenido (false).
             */
            @Override
            protected void updateItem(Evento evento, boolean vacio) {
                super.updateItem(evento, vacio); // Llamada al método de la clase base para actualizar el item

                // Si la celda está vacía o el evento es nulo, limpiar el texto
                if (vacio || evento == null) {
                    setText(""); // Limpiar la celda si está vacía o el evento es nulo
                } else {
                    // Usar StringBuilder para construir el detalle del evento
                    StringBuilder detalles = new StringBuilder();
                    detalles.append("Código: ").append(evento.getCodigo()).append("\n")
                            .append("Título: ").append(evento.getTitulo()).append("\n")
                            .append("Fecha: ").append(evento.getFecha()).append("\n")
                            .append("Organizador: ").append(evento.getOrganizador()).append("\n")
                            .append("Capacidad Máxima: ").append(evento.getCapacidadMaxima()).append("\n")
                            .append("Ubicación: ").append(evento.getUbicacion()).append("\n")
                            .append("Precio: ").append(evento.getPrecio()).append("\n")
                            .append("Asistentes: ").append(evento.getAsistentes()).append("\n");

                    // Verificar el tipo de evento (Concierto o Conferencia) y agregar detalles adicionales
                    if (evento instanceof Concierto) {
                        Concierto concierto = (Concierto) evento;
                        detalles.append("Artista Principal: ").append(concierto.getArtistaPrincipal()).append("\n")
                                .append("Género Musical: ").append(concierto.getGeneroMusical()).append("\n");
                    } else if (evento instanceof Conferencia) {
                        Conferencia conferencia = (Conferencia) evento;
                        detalles.append("Tema: ").append(conferencia.getTema()).append("\n")
                                .append("Panelistas: ").append(String.join(", ", conferencia.getPanelistas())).append("\n");
                    }

                    // Establecer el texto de la celda con los detalles del evento
                    setText(detalles.toString());
                }
            }
        });


        // Listener para selección de evento
        vistaListaEventos.getSelectionModel().selectedItemProperty().addListener((obs, seleccionAnterior, nuevaSeleccion) -> {
            if (nuevaSeleccion != null) {
                // Limpiar los campos
                limpiarCampos(campoCodigo, campoTitulo, campoFecha, campoCapacidadMaxima, campoOrganizador, campoUbicacion, campoAsistentes, campoPrecio, campoArtistaPrincipal, campoGeneroMusical, campoTema, campoPanelistas);

                // Llenar los campos con los valores del evento seleccionado
                campoCodigo.setText(nuevaSeleccion.getCodigo());
                campoTitulo.setText(nuevaSeleccion.getTitulo());
                campoFecha.setText(nuevaSeleccion.getFecha().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                campoCapacidadMaxima.setText(String.valueOf(nuevaSeleccion.getCapacidadMaxima()));
                campoOrganizador.setText(nuevaSeleccion.getOrganizador());
                campoUbicacion.setText(nuevaSeleccion.getUbicacion());
                campoPrecio.setText(String.valueOf(nuevaSeleccion.getPrecio()));
                campoAsistentes.setText(String.valueOf(nuevaSeleccion.getAsistentes()));

                // Establecer el tipo de evento en el ComboBox
                if (nuevaSeleccion instanceof Concierto) {
                    comboTipoEvento.setValue("Concierto");
                    Concierto concierto = (Concierto) nuevaSeleccion;
                    campoArtistaPrincipal.setText(concierto.getArtistaPrincipal());
                    campoGeneroMusical.setText(concierto.getGeneroMusical());
                } else if (nuevaSeleccion instanceof Conferencia) {
                    comboTipoEvento.setValue("Conferencia");
                    Conferencia conferencia = (Conferencia) nuevaSeleccion;
                    campoTema.setText(conferencia.getTema());
                    campoPanelistas.setText(String.join(", ", conferencia.getPanelistas()));
                }
            }
        });

        // Actualizar lista inicial
        actualizarListaEventos();

        // Diseño del formulario
        GridPane gridEntrada = new GridPane();
        gridEntrada.setHgap(10);
        gridEntrada.setVgap(10);
        
        // Configurar ancho preferido para los campos
        campoCodigo.setPrefWidth(300);
        campoTitulo.setPrefWidth(300);
        campoFecha.setPrefWidth(300);
        campoCapacidadMaxima.setPrefWidth(300);
        campoOrganizador.setPrefWidth(300);
        campoUbicacion.setPrefWidth(300);
        campoPrecio.setPrefWidth(300);
        campoAsistentes.setPrefWidth(300);
        campoArtistaPrincipal.setPrefWidth(300);
        campoGeneroMusical.setPrefWidth(300);
        campoTema.setPrefWidth(300);
        campoPanelistas.setPrefWidth(300);
        comboTipoEvento.setPrefWidth(300);

        gridEntrada.addRow(0, new Label("Código:"), campoCodigo);
        gridEntrada.addRow(1, new Label("Título:"), campoTitulo);
        gridEntrada.addRow(2, new Label("Fecha:"), campoFecha);
        gridEntrada.addRow(3, new Label("Capacidad Máxima:"), campoCapacidadMaxima);
        gridEntrada.addRow(4, new Label("Organizador:"), campoOrganizador);
        gridEntrada.addRow(5, new Label("Ubicación:"), campoUbicacion);
        gridEntrada.addRow(6, new Label("Precio:"), campoPrecio); // Ahora se encuentra debajo de "Ubicación"
        gridEntrada.addRow(7, new Label("Asistentes:"), campoAsistentes);
        gridEntrada.addRow(1, new Label("Tipo de Evento:"), comboTipoEvento);
        gridEntrada.addRow(2, new Label("Artista Principal:"), campoArtistaPrincipal);
        gridEntrada.addRow(3, new Label("Género Musical:"), campoGeneroMusical);
        gridEntrada.addRow(4, new Label("Tema:"), campoTema);
        gridEntrada.addRow(5, new Label("Panelistas:"), campoPanelistas);

        // Contenedor de botones
        HBox contenedorBotones = new HBox(10, botonAgregar, botonActualizar, botonEliminar, botonEstadistica, botonMostrarEstadisticas, botonLimpiarCampos);

        // Agregar componentes al layout principal
        layoutPrincipal.getChildren().addAll(
            gridEntrada, 
            contenedorBotones, 
            new Label("Lista de Eventos:"), 
            vistaListaEventos
        );

        // Configurar escena
        Scene escena = new Scene(layoutPrincipal, 1000, 800);
        escenarioPrincipal.setTitle("Sistema de Gestión de Eventos");
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.show();
    }
    
    
    /**
    * Convierte una cadena de texto en un objeto LocalDate.
    * El formato de la fecha debe ser "yyyy-MM-dd". Si la cadena no cumple con el formato esperado,
    * se muestra una alerta de error y se retorna null.
    *
    * @param fechaStr La fecha en formato String a convertir.
    * @return La fecha convertida como LocalDate si la conversión es exitosa, o null si la fecha es inválida.
    */
   public LocalDate convertirAFecha(String fechaStr) {
       try {
           DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
           return LocalDate.parse(fechaStr, formatter);
       } catch (DateTimeParseException e) {
           mostrarAlerta("Fecha inválida", "Por favor ingrese una fecha válida en formato: yyyy-MM-dd.");
           return null; // Retorna null si la fecha no es válida
       }
   }

   /**
    * Actualiza la lista de eventos en la vista.
    * Este método obtiene todos los eventos a través del gestor de eventos y actualiza la vista
    * para reflejar los cambios.
    */
   private void actualizarListaEventos() {
       vistaListaEventos.getItems().setAll(this.gestor.getAll()); 
   }

   /**
    * Limpia los campos de texto proporcionados.
    * Recibe una cantidad variable de campos de texto y limpia su contenido.
    *
    * @param campos Los campos de texto que se desean limpiar.
    */
   private void limpiarCampos(TextField... campos) {
       for (TextField campo : campos) {
           campo.clear(); // Limpiar cada campo de texto
       }
   }
   
   /**
    * Muestra una alerta con el título y contenido proporcionados.
    * Este método se utiliza para mostrar mensajes de error o información al usuario.
    *
    * @param titulo El título de la alerta.
    * @param contenido El mensaje de contenido de la alerta.
    */
   private void mostrarAlerta(String titulo, String contenido) {
       Alert alerta = new Alert(Alert.AlertType.INFORMATION);
       alerta.setTitle(titulo);
       alerta.setHeaderText(null); // No hay cabecera en la alerta
       alerta.setContentText(contenido); // Establecer el contenido del mensaje
       alerta.showAndWait(); // Mostrar la alerta y esperar que el usuario la cierre
   }

   /**
    * Muestra una ventana con estadísticas de asistencia en una tabla.
    * La ventana incluye información sobre el código, título, cantidad de asistentes,
    * capacidad máxima y porcentaje de ocupación de los eventos.
    * 
    * @param estadisticas Lista de mapas que contienen las estadísticas de los eventos.
    *                     Cada mapa debe contener las claves: "codigo", "titulo", "asistentes", 
    *                     "capacidadMaxima" y "porcentajeOcupacion".
    */
   @SuppressWarnings("unchecked")
   private void mostrarVentanaEstadisticas(List<Map<String, Object>> estadisticas) {
       Stage ventanaEstadisticas = new Stage();
       ventanaEstadisticas.setTitle("Estadísticas de Asistencia");

       // Crear una tabla para mostrar las estadísticas
       TableView<Map<String, Object>> tablaEstadisticas = new TableView<>();

       // Crear columnas
       TableColumn<Map<String, Object>, String> columnaCodigo = new TableColumn<>("Código");
       columnaCodigo.setCellValueFactory(data -> 
           new SimpleStringProperty(String.valueOf(data.getValue().getOrDefault("codigo", "N/A")))
       );

       TableColumn<Map<String, Object>, String> columnaTitulo = new TableColumn<>("Título");
       columnaTitulo.setCellValueFactory(data -> 
           new SimpleStringProperty(String.valueOf(data.getValue().getOrDefault("titulo", "N/A")))
       );

       TableColumn<Map<String, Object>, Integer> columnaAsistentes = new TableColumn<>("Asistentes");
       columnaAsistentes.setCellValueFactory(data -> {
           Object valor = data.getValue().get("asistentes");
           return new SimpleObjectProperty<>(
               valor instanceof Number ? ((Number) valor).intValue() : 0
           );
       });

       TableColumn<Map<String, Object>, Integer> columnaCapacidad = new TableColumn<>("Capacidad Máxima");
       columnaCapacidad.setCellValueFactory(data -> {
           Object valor = data.getValue().get("capacidadMaxima");
           return new SimpleObjectProperty<>(
               valor instanceof Number ? ((Number) valor).intValue() : 0
           );
       });

       TableColumn<Map<String, Object>, Double> columnaPorcentaje = new TableColumn<>("Porcentaje de Ocupación");
       columnaPorcentaje.setCellValueFactory(data -> {
           Object valor = data.getValue().get("porcentajeOcupacion");
           return new SimpleObjectProperty<>(
               valor instanceof Number ? ((Number) valor).doubleValue() : 0.0
           );
       });

       // Agregar columnas a la tabla
       tablaEstadisticas.getColumns().addAll(columnaCodigo, columnaTitulo, columnaAsistentes, columnaCapacidad, columnaPorcentaje);

       // Añadir los datos a la tabla
       tablaEstadisticas.getItems().addAll(estadisticas);

       // Configurar la escena y mostrar la ventana
       VBox layout = new VBox(10, tablaEstadisticas);
       layout.setPadding(new Insets(10));
       Scene escena = new Scene(layout, 600, 400);
       ventanaEstadisticas.setScene(escena);
       ventanaEstadisticas.show();
   }


    /**
    * Método principal para iniciar la aplicación.
    * Llama al método `launch()` de la clase `Application` para iniciar la interfaz gráfica.
    *
    * @param args Argumentos de línea de comandos proporcionados al ejecutar la aplicación.
    */
    public static void main(String[] args) {
        launch(args); // Llamada a la clase base Application para iniciar la interfaz gráfica
    }
}