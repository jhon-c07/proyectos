import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Arrays;

public class jhon {
    // Método de ordenamiento Insertion Sort
    public static void insertionSort(char[] arr) {
        for (int i = 1; i < arr.length; i++) {
            char key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j = j - 1;
            }
            arr[j + 1] = key;
        }
        System.out.println("Letras intentadas ordenadas: " + Arrays.toString(arr));
    }

    // Comprobar si el jugador ha ganado
    public static boolean haGanado(char[] palabraSecreta, char[] adivinadas) {
        return Arrays.equals(palabraSecreta, adivinadas);
    }

    // Mostrar el progreso del jugador
    public static void mostrarProgreso(char[] adivinadas) {
        System.out.println("Progreso actual: " + Arrays.toString(adivinadas));
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Leer palabras desde el archivo JSON
        List<String> palabras = GestorArchivoJSON.leerPalabras();
        if (palabras.isEmpty()) {
            System.out.println("La lista de palabras está vacía. Verifica el archivo palabras.json.");
            return;
        }

        int opcion = 0;
        while (opcion != 5) {
            System.out.println("\nOpciones disponibles:");
            System.out.println("1. Jugar Ahorcado");
            System.out.println("2. Añadir una palabra");
            System.out.println("3. Eliminar una palabra");
            System.out.println("4. Ver lista de palabras");
            System.out.println("5. Salir");
            System.out.print("Selecciona una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            switch (opcion) {
                case 1:
                    jugarAhorcado(palabras, scanner);
                    break;
                case 2:
                    System.out.print("Introduce la nueva palabra: ");
                    String nuevaPalabra = scanner.nextLine().toLowerCase();
                    if (!palabras.contains(nuevaPalabra)) {
                        palabras.add(nuevaPalabra);
                        GestorArchivoJSON.guardarPalabras(palabras);
                        System.out.println("Palabra añadida correctamente.");
                    } else {
                        System.out.println("La palabra ya existe.");
                    }
                    break;
                case 3:
                    System.out.println("Lista de palabras actuales: " + palabras);
                    System.out.print("Introduce la palabra a eliminar: ");
                    String palabraAEliminar = scanner.nextLine().toLowerCase();
                    if (palabras.remove(palabraAEliminar)) {
                        GestorArchivoJSON.guardarPalabras(palabras);
                        System.out.println("Palabra eliminada correctamente.");
                    } else {
                        System.out.println("La palabra no existe.");
                    }
                    break;
                case 4:
                    System.out.println("Palabras disponibles: " + palabras);
                    break;
                case 5:
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción inválida. Intenta de nuevo.");
                    break;
            }
        }
        scanner.close();
    }

    public static void jugarAhorcado(List<String> palabras, Scanner scanner) {
        Random random = new Random();

        String palabra = palabras.get(random.nextInt(palabras.size()));
        char[] palabraSecreta = palabra.toCharArray();
        char[] adivinadas = new char[palabra.length()];
        Arrays.fill(adivinadas, '_'); // Inicializamos con guiones bajos

        int intentosMaximos = 6;
        int errores = 0;
        char[] letrasIntentadas = new char[26]; // Para almacenar las letras ya intentadas
        int numIntentos = 0; // Contador de intentos

        System.out.println("\n¡Bienvenido al juego del ahorcado!");

        while (errores < intentosMaximos && !haGanado(palabraSecreta, adivinadas)) {
            System.out.println("\nTienes " + (intentosMaximos - errores) + " intentos restantes.");
            mostrarProgreso(adivinadas);

            System.out.print("Introduce una letra: ");
            char letra = scanner.next().toLowerCase().charAt(0);

            if (new String(letrasIntentadas).contains(String.valueOf(letra))) {
                System.out.println("Ya has intentado esa letra.");
                continue;
            }

            letrasIntentadas[numIntentos++] = letra;
            insertionSort(letrasIntentadas);

            boolean acierto = false;
            for (int i = 0; i < palabraSecreta.length; i++) {
                if (palabraSecreta[i] == letra) {
                    adivinadas[i] = letra;
                    acierto = true;
                }
            }

            if (acierto) {
                System.out.println("¡Correcto!");
            } else {
                System.out.println("Letra incorrecta.");
                errores++;
            }
        }

        if (haGanado(palabraSecreta, adivinadas)) {
            System.out.println("¡Felicidades, has ganado! La palabra era: " + palabra);
        } else {
            System.out.println("Has perdido. La palabra era: " + palabra);
        }
    }
}

class GestorArchivoJSON {
    private static final String ARCHIVO_JSON = "palabras.json";

    // Método para leer palabras desde un archivo JSON
    public static List<String> leerPalabras() {
        List<String> palabras = new ArrayList<>();
        try {
            String contenido = new String(Files.readAllBytes(Paths.get(ARCHIVO_JSON)));
            contenido = contenido.replace("[", "").replace("]", "").replace("\"", ""); // Eliminar formato JSON
            for (String palabra : contenido.split(",")) {
                palabras.add(palabra.trim());
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }
        return palabras;
    }

    // Método para guardar palabras en el archivo JSON
    public static void guardarPalabras(List<String> palabras) {
        try {
            String contenido = palabras.toString().replace(" ", "");
            Files.write(Paths.get(ARCHIVO_JSON), contenido.getBytes());
        } catch (IOException e) {
            System.out.println("Error al guardar el archivo: " + e.getMessage());
        }
    }
}
