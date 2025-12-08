// java/controllers/FrontController.java
package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;

@WebServlet(name = "FrontController", value = "/api/*")
public class FrontController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        /* Authentication & Authorisation intercepts HTTPrequest before
            Frontcontroller delegation. Checks authentication token if
            necessary and validates endpoint is allowed. */

        String path = req.getPathInfo();
        if (path == null || path.equals("/")) {
            Controller.writeResponse(resp, false, "Please provide a valid path");
            return;
        }

        // Retrieve relevant controller and pass request and user details to it
        String[] parts = path.substring(1).split("/");
        String resource = parts[0];
        parts = Arrays.copyOfRange(parts, 1, parts.length);

        Controller controller = ControllerRegistry.getController(resource);
        if (controller == null) {
            // Unrecognised resource request
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            Controller.writeResponse(resp, false, "Unrecognised resource");
            return;
        }

        controller.handle(parts, req, resp);
    }
}