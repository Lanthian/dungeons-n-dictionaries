// java/api/controllers/Controller.java
package api.controllers;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Abstract Controller class to be extended by all relevant controllers in the
 * backend. Required {@code handleGet(), handlePost(), handlePut(),
 * handleDelete()} methods to be implemented by subclass to define controller
 * capabilities.
 */
public abstract class Controller {

    /**
     * Controller method to distinguish between different servlet backend
     * resource requests. Set up for CRUD access by default.
     *
     * @param parts string API endpoint called, split on {@code '/'}
     * @param req {@link HttpServletRequest} object received from API call
     * @param resp {@link HttpServletResponse} object configured for API report
     * @throws IOException
     */
    public void handle(String[] parts, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // The below methods are to be implemented by relevant Controller
        switch (req.getMethod()) {
            case "GET":
                handleGet(parts, req, resp);
                break;
            case "POST":
                handlePost(parts, req, resp);
                break;
            case "PUT":
                handlePut(parts, req, resp);
                break;
            case "DELETE":
                handleDelete(parts, req, resp);
                break;
            default:
                resp.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
                writeResponse(resp, false, "Unrecognised method");
        }
    }

    protected void handleGet(String[] parts, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
    }

    protected void handlePost(String[] parts, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
    }

    protected void handlePut(String[] parts, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
    }

    protected void handleDelete(String[] parts, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
    }

    /**
     * Utility method to shorthand simple servlet responses.
     *
     * @param resp {@link HttpServletResponse} object configured for API report
     * @param success boolean value to set success/failure of response status
     * @param message response message written back
     * @throws IOException
     */
    protected static void writeResponse(HttpServletResponse resp, boolean success, String message) throws IOException {
        Gson gson = new Gson();
        JsonObject json = new JsonObject();
        json.addProperty("status", success ? "success" : "failure");
        json.addProperty("message", message);

        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(json));
    }
}
