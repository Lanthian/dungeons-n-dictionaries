// java/controllers/LanguageController.java
package controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.google.gson.GsonBuilder;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import domain.modifiers.Language;
import services.LanguageService;

/**
 * A controller for {@link Language}, interfaces frontend call of
 * {@link FrontController} to domain and database.
 */
public class LanguageController extends Controller {

    @Override
    protected void handleGet(String[] parts, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        try {
            List<Language> languages = LanguageService.getAll();

            // Convert retrieved data into a Json object array
            String json = new GsonBuilder().create().toJson(languages);
            resp.getWriter().write(json);
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        } catch (Exception e) {
            // Catch potential IllegalState or NumberFormat exceptions from id
            throw new ServletException("Unexpected error", e);
        }
    }
}
