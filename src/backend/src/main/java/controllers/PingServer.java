// java/controllers/PingServer.java
package controllers;

import java.io.IOException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Simple WebServlet to test API connection.
 */
@WebServlet(name = "PingServer", value = "/api/test")
public class PingServer extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.getWriter().println("API is online");
    }
}
