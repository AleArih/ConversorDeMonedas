import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.Gson;
import java.util.Scanner;

public class Main {

    // Clase para mapear la respuesta de la API
    class ExchangeRateResponse {
        String result;
        double conversion_rate;
    }

    public static void main(String[] args) {
        Scanner lectura = new Scanner(System.in);
        String direccion;

        double cantidad;

        while (true) {
            System.out.println("****************");
            System.out.println("Sean bienvenidos al conversor de monedas");
            System.out.println("""
                    1) DÓLAR A PESO ARGENTINO
                    2) PESO ARGENTINO A DOLAR
                    3) DÓLAR A REAL BRASILEÑO
                    4) REAL BRASILEÑO A DOLAR
                    5) DOLAR A PESO COLOMBIANO
                    6) PESO COLOMBIANO A DOLAR
                    7) SALIR
                    
                    ELIJA UNA OPCIÓN VÁLIDA: """);

            int numero = lectura.nextInt();

            if (numero == 7) {
                System.out.println("Saliendo...");
                break;
            }

            System.out.print("Ingrese la cantidad a convertir: ");
            cantidad = lectura.nextDouble();

            switch (numero) {
                case 1 -> direccion = "https://v6.exchangerate-api.com/v6/c6f5f7657519157996a6b666/pair/USD/ARS";
                case 2 -> direccion = "https://v6.exchangerate-api.com/v6/c6f5f7657519157996a6b666/pair/ARS/USD";
                case 3 -> direccion = "https://v6.exchangerate-api.com/v6/c6f5f7657519157996a6b666/pair/USD/BRL";
                case 4 -> direccion = "https://v6.exchangerate-api.com/v6/c6f5f7657519157996a6b666/pair/BRL/USD";
                case 5 -> direccion = "https://v6.exchangerate-api.com/v6/c6f5f7657519157996a6b666/pair/USD/COP";
                case 6 -> direccion = "https://v6.exchangerate-api.com/v6/c6f5f7657519157996a6b666/pair/COP/USD";
                default -> {
                    System.out.println("Opción inválida");
                    continue;
                }
            }

            try {
                // Crear cliente HTTP
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(direccion))
                        .build();

                // Obtener respuesta de la API
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                // Parsear respuesta JSON usando Gson
                Gson gson = new Gson();
                ExchangeRateResponse jsonResponse = gson.fromJson(response.body(), ExchangeRateResponse.class);

                // Verificar si la respuesta es válida
                if (jsonResponse.result.equals("success")) {
                    // Calcular el resultado
                    double tasa = jsonResponse.conversion_rate;
                    double resultado = cantidad * tasa;
                    System.out.println("El valor de " + cantidad + " es " + resultado + " en la moneda destino.");
                } else {
                    System.out.println("Error en la respuesta de la API");
                }

            } catch (Exception e) {
                System.out.println("Error al realizar la conversión: " + e.getMessage());
            }
        }
    }
}
