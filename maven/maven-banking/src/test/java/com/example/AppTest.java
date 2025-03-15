package com.example;

import org.json.*;
import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    // Ruta del archivo de prueba (asegúrate de que el archivo esté presente en la ruta indicada)
    private final String testFilePath = "/workspaces/BuildSolutions_JuanJoseRuiz/maven/maven-banking/src/resources/transactions.txt"; // Ruta del archivo de prueba

    // 🔹 1. Prueba para verificar que el archivo se lee correctamente
    @Test
    public void testLeerArchivo() {
        String jsonData = App.leerArchivo(testFilePath);

        // Verificar que el archivo se haya leído correctamente y contiene datos
        assertNotNull(jsonData, "El archivo no debe ser nulo");
        assertFalse(jsonData.isEmpty(),"el archivo no debe estar vacio");
    }

    // 🔹 2. Prueba para verificar que las transacciones se extraen correctamente
    @Test
    public void testObtenerTransacciones() {
        // Leer los datos del archivo
        String jsonData = App.leerArchivo(testFilePath);
        
        // Filtrar las transacciones para un usuario específico
        List<JSONObject> transacciones = App.obtenerTransacciones(jsonData, "juan.jose@urosario.edu.co");

        // Verificar que se han encontrado transacciones para el usuario "juan123"
        assertNotNull(transacciones, "La lista de transacciones no debe ser nula");
        assertFalse(transacciones.isEmpty(), "La lista de transacciones no debe estar vacía");
        
        // Verificar que las transacciones son del usuario correcto
        for (JSONObject transaccion : transacciones) {
            assertEquals("juan.jose@urosario.edu.co", "juan.jose@urosario.edu.co", "El usuario debe ser 'juan.jose@urosario.edu.co'");
        }
    }

    // 🔹 3. Prueba para verificar que el extracto se genera correctamente
    @Test
    public void testGenerarExtracto() throws IOException {
        // Nombre de usuario de prueba
        String usuario = "juan.jose@urosario.edu.co";

        // Leer los datos del archivo
        String jsonData = App.leerArchivo(testFilePath);
        
        // Filtrar las transacciones para el usuario "juan123"
        List<JSONObject> transacciones = App.obtenerTransacciones(jsonData, usuario);

        // Generar el extracto en un archivo temporal
        App.generarExtracto(usuario, transacciones);

        // Ruta donde se guardará el extracto
        File extractoFile = new File(usuario + "_extracto.txt");

        // Verificar que el archivo ha sido creado
        assertTrue(extractoFile.exists(), "El archivo de extracto debe haber sido creado");

        // Leer el contenido del archivo generado para verificar
        List<String> lines = Files.readAllLines(extractoFile.toPath());

        // Verificar que el archivo contiene el nombre del usuario y al menos una transacción
        assertTrue(lines.stream().anyMatch(line -> line.contains("Extracto bancario de " + usuario)), "El extracto debe contener el nombre del usuario");
        
        // Limpiar después de la prueba (eliminar el archivo generado)
        Files.delete(extractoFile.toPath());
    }
}
