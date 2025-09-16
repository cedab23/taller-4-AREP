package arep1.taller1;

import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.*;

public class ArimaKinen {

    private static final Map<String, String> MIME_TYPES = new HashMap<>();
    static {
        MIME_TYPES.put("html", "text/html");
        MIME_TYPES.put("css", "text/css");
        MIME_TYPES.put("js", "application/javascript");
        MIME_TYPES.put("pdf", "application/pdf");
        MIME_TYPES.put("txt", "text/plain");
        MIME_TYPES.put("json", "application/json");
        MIME_TYPES.put("xml", "application/xml");
        MIME_TYPES.put("png", "image/png");
        MIME_TYPES.put("jpg", "image/jpeg");
        MIME_TYPES.put("jpeg", "image/jpeg");
        MIME_TYPES.put("gif", "image/gif");
        MIME_TYPES.put("mp4", "video/mp4");
    }

    @FunctionalInterface
    interface RouteHandler {
        void handle(Request req, Response res) throws IOException;
    }

    static class Request {
        public final String path;
        public final String method;
        public final Map<String, String> params;

        public Request(String path, String method, Map<String, String> params) {
            this.path = path;
            this.method = method;
            this.params = params;
        }
    }

    static class Response {
        private final OutputStream out;

        public Response(OutputStream out) {
            this.out = out;
        }

        public void send(int status, String message, String contentType, byte[] content) throws IOException {
            sendResponse(out, status, message, contentType, content);
        }
    }

    private static final Map<String, RouteHandler> getRoutes = new HashMap<>();

    public static void get(String path, RouteHandler handler) {
        getRoutes.put(path, handler);
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        boolean running = true;
        ArrayList<Horse> racers = new ArrayList<>();

        try {
            serverSocket = new ServerSocket(5000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 5000");
            System.exit(1);
        }

        // Registro del GreetingController
        Class<?> controllerClass = GreetingController.class;
        if (controllerClass.isAnnotationPresent(RestController.class)) {
            final Object controllerInstance;
            try {
                controllerInstance = controllerClass.getDeclaredConstructor().newInstance();
                System.out.println("GreetingController registrado exitosamente");
            } catch (Exception e) {
                System.err.println("Error instanciando GreetingController: " + e.getMessage());
                throw new RuntimeException(e);
            }
            for (Method method : controllerClass.getDeclaredMethods()) {
                GetMapping getMappingAnnotation = method.getAnnotation(GetMapping.class);
                if (getMappingAnnotation != null) {
                    String path = getMappingAnnotation.value(); // "/greeting" endpoint
                    System.out.println("Router registry: " + path + " -> " + method.getName());

                    get(path, (req, res) -> {
                        try {
                            Parameter[] parameters = method.getParameters();
                            Object[] methodArgs = new Object[parameters.length];

                            for (int i = 0; i < parameters.length; i++) {
                                RequestParam requestParam = parameters[i].getAnnotation(RequestParam.class);
                                if (requestParam != null) {
                                    String paramValue = req.params.getOrDefault(requestParam.value(), requestParam.defaultValue());
                                    methodArgs[i] = paramValue;
                                    System.out.println("Param: " + requestParam.value() + " = " + paramValue);
                                } else {
                                    methodArgs[i] = null;
                                }
                            }
                            Object result = method.invoke(controllerInstance, methodArgs);
                            res.send(200, "OK", "text/plain", result.toString().getBytes());
                        } catch (Exception e) {
                            System.err.println("Error invocando " + method.getName() + ": " + e.getMessage());
                            res.send(500, "Internal Server Error", "text/plain",
                                    ("Error: " + e.getMessage()).getBytes());
                        }
                    });
                }
            }
        }

        // Lambda endpoint for Arima Kinen
        get("/api/arimakinen2", (req, res) -> {
            String msg = "{\"result\": \"Arima Kinen Custom Endpoint\"}";
            res.send(200, "OK", "application/json", msg.getBytes());
        });

        while (running) {
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
                String inputLine;
                String method = "GET";
                URI requestUri = null;
                String path = "/";
                boolean firstLine = true;
                int contentLength = 0;

                OutputStream out = clientSocket.getOutputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                while ((inputLine = in.readLine()) != null) {
                    if (firstLine) {
                        String[] requestParts = inputLine.split(" ");
                        method = requestParts[0];
                        requestUri = new URI(requestParts[1]);
                        path = requestUri.getPath();
                        System.out.println("Method: " + method + " | Path: " + path);
                        firstLine = false;
                    }
                    if (inputLine.toLowerCase().startsWith("content-length:")) {
                        contentLength = Integer.parseInt(inputLine.split(":")[1].trim());
                        System.out.println("recibido: " + contentLength);
                    }
                    System.out.println("Received: " + inputLine);
                    if (inputLine.trim().isEmpty()) {
                        break;
                    }
                }

                RouteHandler handler = null;
                if (method.equals("GET")) {
                    handler = getRoutes.get(path);
                }

                if (handler != null) {
                    handler.handle(new Request(path, method, getParameters(requestUri)), new Response(out));
                } else if (path.equals("/api/register")) {
                    char[] body = new char[contentLength];
                    in.read(body, 0, contentLength);
                    String json = new String(body);
                    Horse horse = mapHorse(json);
                    racers.add(horse);
                    String registrationMessage = "{\"result\": \"" + horse.getHorse()
                            + " ha sido registrado exitosamente\"}";
                    sendResponse(out, 200, "OK", "application/json", registrationMessage.getBytes());

                } else if (path.equals("/api/table")) {
                    String json = horsesToJson(racers);
                    sendResponse(out, 200, "OK", "application/json", json.getBytes());
                } else {

                    Map<String, String> parameters = getParameters(requestUri);
                    String filePath = "";
                    String mimeType = null;
                    if (parameters.isEmpty()) {
                        String[] pathParts = requestUri.getPath().split("/");
                        if (pathParts.length > 1 && !pathParts[1].isEmpty()) {
                            filePath = pathParts[1];
                            String[] parts = filePath.split("\\.");
                            if (parts.length > 1) {
                                mimeType = MIME_TYPES.get(parts[parts.length - 1]);
                            }
                        }
                        System.out.println("file request: " + filePath + " | " + mimeType);
                    } else {
                        filePath = getFileName(parameters);
                        mimeType = MIME_TYPES.get(parameters.get("type"));
                        System.out.println("path request: " + filePath + " | " + mimeType);
                    }
                    sendFile(out, filePath, mimeType);
                }
                out.close();
                in.close();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            } catch (Exception e) {
                System.err.println("ERROR: " + e.getMessage());
                System.exit(1);
            } finally {
                clientSocket.close();
            }

        }
        serverSocket.close();
    }

    private static void sendResponse(OutputStream out, int status, String message, String contentType, byte[] content)
            throws IOException {

        String header = "HTTP/1.1 " + status + " " + message + "\r\n" +
                "Content-Type: " + contentType + "; charset=UTF-8\r\n" +
                "\r\n";
        out.write(header.getBytes());
        out.write(content);
        out.flush();
    }

    private static void sendFile(OutputStream out, String path, String contentType) throws IOException {
        try {
            InputStream inputStream = ArimaKinen.class.getClassLoader().getResourceAsStream(path);
            if (inputStream == null) {
                send404(out);
                return;
            }
            byte[] content = inputStream.readAllBytes();
            sendResponse(out, 200, "OK", contentType, content);
        } catch (Exception e) {
            send404(out);
        }
    }

    private static void send404(OutputStream out) throws IOException {
        String htmlContent = "<html><body><h1>404 - NOT FOUND</h1></body></html>";
        byte[] content = htmlContent.getBytes();
        sendResponse(out, 404, "Not Found", "text/html", content);
    }

    private static Map<String, String> getParameters(URI uri) {
        Map<String, String> parameters = new HashMap<>();
        String query = uri.getQuery();
        if (query != null && !query.isEmpty()) {

            String[] parts = query.split("&");
            for (String part : parts) {
                String[] value = part.split("=");
                if (value.length == 2) {
                    parameters.put(value[0], value[1]);
                }

            }

        }

        return parameters;
    }

    private static String getFileName(Map<String, String> parameters) {

        String fileName = parameters.get("name") + "." + parameters.get("type");
        return fileName;
    }

    private static Horse mapHorse(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, Horse.class);
        } catch (Exception e) {
            System.err.println("Error parseando JSON: " + e.getMessage());
            return null;
        }
    }

    private static String horsesToJson(List<Horse> horses) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(horses);
        } catch (Exception e) {
            System.err.println("Error al convertir los datos: " + e.getMessage());
            return "[]";
        }
    }

}