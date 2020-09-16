package server;

import java.io.OutputStream;

public class HelloServlet implements Servlet {
    @Override
    public void init() throws Exception {
        System.out.println("HelloServlet is initiated");
    }

    @Override
    public void service(byte[] requestBuffer, OutputStream out) throws Exception {
        String request = new String(requestBuffer);
        String firstLineOfRequest = request.substring(0, request.indexOf("\r\n"));
        String[] parts = firstLineOfRequest.split(" ");
        String method = parts[0];
        String uri = parts[1];

        String username = null;

        if (method.equalsIgnoreCase("get") && uri.indexOf("username") != -1) {
            String parameters = uri.substring(uri.indexOf("?"), uri.length());

            parts = parameters.split("&");
            parts = parts[0].split("=");
            username = parts[1];
        }

        if (method.equalsIgnoreCase("post")) {
            int locate = request.indexOf("\r\n\r\n");
            String content = request.substring(locate + 4, request.length());
            if (content.indexOf("username") != -1) {
                parts = content.split("&");
                parts = parts[0].split("=");
                username = parts[1];
            }
        }

        out.write("HTTP/1.1 200 OK\r\n".getBytes());
        out.write("Content-Type:text/html\r\n\r\n".getBytes());
        String content = "<html><head><title>HelloWorld</title></head><body>";
        content += "<h1>Hello:" + username + "</h1></body><head>";
        out.write(content.getBytes());
    }
}
