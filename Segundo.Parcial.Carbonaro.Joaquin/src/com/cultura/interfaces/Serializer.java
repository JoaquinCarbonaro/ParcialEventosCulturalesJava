//Carbonaro Joaquin
package com.cultura.interfaces;

import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.util.List;


/**
 * Interfaz genérica que define operaciones de serialización y deserialización para objetos de tipo `T`.
 * Permite la escritura y lectura de objetos en formatos binarios y JSON.
 * 
 * @param <T> El tipo de objeto que se desea serializar o deserializar.
 */
public interface Serializer<T> {

    /**
     * Serializa un objeto en formato binario y lo guarda en un archivo.
     * 
     * @param fileName El nombre del archivo donde se guardará el objeto serializado.
     * @param object El objeto que se desea serializar.
     * @return true si la serialización fue exitosa, false en caso contrario.
     * @throws IOException Si ocurre un error al escribir el archivo.
     */
    boolean writeBinary(String fileName, T object) throws IOException;

    /**
     * Deserializa un objeto desde un archivo binario.
     * 
     * @param fileName El nombre del archivo desde donde se leerá el objeto.
     * @return El objeto deserializado.
     * @throws Exception Si ocurre un error al deserializar el objeto.
     */
    T readBinary(String fileName) throws Exception;

    /**
     * Serializa una lista de objetos en formato JSON y la guarda en un archivo.
     * 
     * @param fileName El nombre del archivo donde se guardará el JSON.
     * @param object La lista de objetos que se desea serializar.
     * @return true si la serialización fue exitosa, false en caso contrario.
     * @throws IOException Si ocurre un error al escribir el archivo JSON.
     */
    boolean writeJSON(String fileName, List<?> object) throws IOException;

    /**
     * Deserializa una lista de objetos desde un archivo JSON.
     * 
     * @param fileName El nombre del archivo desde donde se leerá el JSON.
     * @param typeToken El tipo de los objetos contenidos en el JSON, utilizado para la deserialización.
     * @param <R> El tipo de los objetos deserializados.
     * @return La lista de objetos deserializados.
     * @throws IOException Si ocurre un error al leer el archivo JSON.
     */
    <R> R readJSON(String fileName, TypeToken<R> typeToken) throws IOException;
}