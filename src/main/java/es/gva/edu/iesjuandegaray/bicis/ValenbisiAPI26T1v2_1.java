package es.gva.edu.iesjuandegaray.bicis;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class ValenbisiAPI26T1v2_1 {

	
	// https://geoportal.valencia.es/server/rest/services/OPENDATA/Trafico/MapServer/228/query?where=1=1&outFields=*&f=json
    private static final String API_URL =
            "https://geoportal.valencia.es/server/rest/services/OPENDATA/Trafico/MapServer/228/query"
            + "?where=1%3D1"
            + "&outFields=*"
            + "&returnGeometry=true"
            + "&f=json";

    public static void main(String[] args) {

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            HttpGet request = new HttpGet(API_URL);
            HttpResponse response = httpClient.execute(request);

            HttpEntity entity = response.getEntity();

            if (entity != null) {

                String result = EntityUtils.toString(entity);

                // Convertimos a JSON
                JSONObject jsonObject = new JSONObject(result);

                // Obtenemos el array "features"
                JSONArray features = jsonObject.getJSONArray("features");

                System.out.println("Número de estaciones: " + features.length());
                System.out.println();

                // BUCLE SENCILLO
                for (int i = 0; i < features.length(); i++) {

                    JSONObject feature = features.getJSONObject(i);

                    System.out.println("----- ESTACIÓN -----");

                    // ATRIBUTOS
                    if (feature.has("attributes")) {

                        JSONObject attr = feature.getJSONObject("attributes");

                        String nombre = attr.getString("address");
                        int bicis = attr.getInt("available");
                        int anclajes = attr.getInt("free");

                        System.out.println("Nombre: " + nombre);
                        System.out.println("Bicis disponibles: " + bicis);
                        System.out.println("Anclajes libres: " + anclajes);

                    } else {
                        System.out.println("Sin atributos");
                    }

                    // GEOMETRÍA
                    if (feature.has("geometry")) {

                        JSONObject geo = feature.getJSONObject("geometry");

                        double x = geo.optDouble("x", 0);
                        double y = geo.optDouble("y", 0);

                        System.out.println("X: " + x);
                        System.out.println("Y: " + y);

                    } else {
                        System.out.println("Sin geometría");
                    }

                    System.out.println("--------------------------");
                }
            }

        } catch (IOException e) {
            System.out.println("Error en la petición HTTP:");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Error procesando JSON:");
            e.printStackTrace();
        }
    }
}


