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
            // Convert retrieved data into a Json text
            String json;

            // Identify endpoint
            if (parts.length > 0) {
                // Get a specific Language
                long id = Long.parseLong(parts[0]);
                Language language = LanguageService.getById(id);
                json = new GsonBuilder().create().toJson(language);
            } else {
                // Return all Languages
                List<Language> languages = LanguageService.getAll();
                json = new GsonBuilder().create().toJson(languages);
            }

            resp.getWriter().write(json);
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        } catch (Exception e) {
            // Catch potential IllegalState or NumberFormat exceptions from id
            throw new ServletException("Unexpected error", e);
        }
    }

    @Override
    protected void handlePost(String[] parts, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Read Json input into DTO (domain object in this case)
            Language language = new GsonBuilder().create().fromJson(req.getReader(), Language.class);

            // Do operation
            LanguageService.OperationResult result;
            result = LanguageService.createLanguage(language);

            // Interpret service method result
            String msg = "Unknown operation result";
            switch (result) {
                case SUCCESS:
                    msg = "Inserted new language";
                    break;
                case ILLEGAL_ENTITY:
                    msg = "Cannot insert new language - ID should not be attached";
                    break;
                case DB_FAILURE:
                    msg = "Failed to insert new language";
            }
            writeResponse(resp, result.isSuccess(), msg);

        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        } catch (Exception e) {
            // Catch potential input IllegalState or NumberFormat exceptions
            throw new ServletException("Unexpected error", e);
        }
    }

    @Override
    protected void handlePut(String[] parts, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Read Json input into DTO (domain object in this case)
            Language language = new GsonBuilder().create().fromJson(req.getReader(), Language.class);

            // Do operation
            LanguageService.OperationResult result;
            result = LanguageService.updateLanguage(language);

            // Interpret service method result
            String msg = "Unknown operation result";
            switch (result) {
                case SUCCESS:
                    msg = "Updated language";
                    break;
                case ILLEGAL_ENTITY:
                    msg = "Cannot update language - ID should be attached";
                    break;
                case DB_FAILURE:
                    msg = "Failed to update language";
            }
            writeResponse(resp, result.isSuccess(), msg);

        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        } catch (Exception e) {
            // Catch potential input IllegalState or NumberFormat exceptions
            throw new ServletException("Unexpected error", e);
        }
    }

    @Override
    protected void handleDelete(String[] parts, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Read Json input into DTO (domain object in this case)
            Language language = new GsonBuilder().create().fromJson(req.getReader(), Language.class);

            // Do operation
            LanguageService.OperationResult result;
            result = LanguageService.deleteLanguage(language);

            // Interpret service method result
            String msg = "Unknown operation result";
            switch (result) {
                case SUCCESS:
                    msg = "Deleted language";
                    break;
                case ILLEGAL_ENTITY:
                    msg = "Cannot delete language - ID should be attached";
                    break;
                case DB_FAILURE:
                    msg = "Failed to delete language";
            }
            writeResponse(resp, result.isSuccess(), msg);

        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        } catch (Exception e) {
            // Catch potential IllegalState or NumberFormat exceptions from id
            throw new ServletException("Unexpected error", e);
        }
    }
}
