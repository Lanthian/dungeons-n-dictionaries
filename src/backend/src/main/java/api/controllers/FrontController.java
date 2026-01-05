// java/api/controllers/FrontController.java
package api.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;

import datasource.exceptions.IllegalPersistException;
import datasource.exceptions.PersistenceException;

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
            Controller.writeStatus(resp, HttpServletResponse.SC_BAD_REQUEST, "Please provide a valid path");
            return;
        }

        // Retrieve relevant controller and pass request and user details to it
        String[] parts = path.substring(1).split("/");
        String resource = parts[0];
        parts = Arrays.copyOfRange(parts, 1, parts.length);

        Controller controller = ControllerRegistry.getController(resource);
        if (controller == null) {
            // Unrecognised resource request
            Controller.writeStatus(resp, HttpServletResponse.SC_NOT_FOUND, "Unrecognised resource");
            return;
        }

        // Wrap the handle call to catch and manage common exceptions globally
        try {
            controller.handle(parts, req, resp);

        } catch (IllegalPersistException e) {
            // Database constraint violation
            Controller.writeStatus(resp, HttpServletResponse.SC_CONFLICT, e.getDetail());

        } catch (PersistenceException e) {
            // Other database failure
            Controller.writeStatus(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());

        } catch (Exception e) {
            // Unaccounted for exception - patch all these occurrences
            throw new ServletException("Unexpected error", e);
        }
    }
}