package com.example;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import org.json.*;

public class App {

    // 🔹 1. Leer el archivo JSON desde un .txt
    public static String leerArchivo(String rutaArchivo) {
        try {
            // Leer todo el contenido del archivo en una sola cadena
            return new String(Files.readAllBytes(Paths.get(rutaArchivo)));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 🔹 2. Obtener transacciones de un usuario específico
    public static List<JSONObject> obtenerTransacciones(String jsonData, String usuario) {
        List<JSONObject> transaccionesUsuario = new ArrayList<>();
        
        try {
            // Parsear el string JSON a un objeto JSONObject
            JSONObject jsonObject = new JSONObject(jsonData);

            // Verificar si el usuario existe en el JSON
            if (jsonObject.has(usuario)) {
                // Obtener las transacciones del usuario específico
                JSONArray transacciones = jsonObject.getJSONArray(usuario);

                // Iterar sobre las transacciones
                for (int i = 0; i < transacciones.length(); i++) {
                    JSONObject transaccion = transacciones.getJSONObject(i);
                    transaccionesUsuario.add(transaccion);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return transaccionesUsuario;
    }

    // 🔹 3. Generar extracto bancario en un archivo .txt
    public static void generarExtracto(String usuario, List<JSONObject> transacciones) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(usuario + "_extracto.txt"))) {
            writer.write("Extracto bancario de " + usuario + "\n");
            writer.write("-----------------------------\n");

            // Escribir las transacciones en el archivo
            for (JSONObject transaccion : transacciones) {
                String tipo = transaccion.getString("type");
                double balance = transaccion.getDouble("balance");
                String timestamp = transaccion.getString("timestamp");

                writer.write("Tipo: " + tipo + "\n");
                writer.write("Balance: " + balance + "\n");
                writer.write("Fecha y hora: " + timestamp + "\n");
                writer.write("-----------------------------\n");
            }

            writer.write("Fin del extracto\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Solicitar usuario
        Scanner scanner = new Scanner(System.in);
        System.out.print("Introduce el nombre del usuario: ");
        String usuario = scanner.nextLine();

        // Leer archivo JSON
        String rutaArchivo = "/workspaces/BuildSolutions_JuanJoseRuiz/maven/maven-banking/src/resources/transactions.txt";  // Ruta de tu archivo .txt
        String jsonData = leerArchivo(rutaArchivo);

        if (jsonData != null) {
            // Obtener las transacciones del usuario
            List<JSONObject> transacciones = obtenerTransacciones(jsonData, usuario);

            // Generar el extracto bancario
            if (!transacciones.isEmpty()) {
                generarExtracto(usuario, transacciones);
                System.out.println("Extracto bancario generado correctamente.");
            } else {
                System.out.println("No se encontraron transacciones para el usuario " + usuario);
            }
        } else {
            System.out.println("Hubo un error al leer el archivo.");
        }

        scanner.close();
    }
}
