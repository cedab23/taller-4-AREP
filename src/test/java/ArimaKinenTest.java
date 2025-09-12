import org.junit.jupiter.api.*;

import arep1.taller1.ArimaKinen;

import java.net.*;
import java.net.http.*;

class ArimaKinenTest {

    // Thread to run the server
    private static Thread serverThread;

    // Client HTTP to simulate requests
    private HttpClient client = HttpClient.newHttpClient();

    @BeforeAll
    static void startServer() throws InterruptedException {
        serverThread = new Thread(() -> {
            try {
                ArimaKinen.main(new String[] {});
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        serverThread.setDaemon(true);
        serverThread.start();
        Thread.sleep(4000);
    }

    @Test
    void testRegisterHorse() throws Exception {
        String json = "{\"horse\":\"Maruzensky\",\"jockey\":\"Cedab23\",\"strategy\":\"Delantero\"}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:5000/api/register"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertTrue(response.body().contains("Maruzensky"));
    }

    @Test
    void testGetTable() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:5000/api/table"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertTrue(response.body().startsWith("["));
    }

    @Test
    void testFileNotFound() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:5000/kikuka-sho.html"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(404, response.statusCode());
        Assertions.assertTrue(response.body().contains("404"));
    }

}