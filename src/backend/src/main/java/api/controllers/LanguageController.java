// java/api/controllers/LanguageController.java
package api.controllers;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import api.json.JsonUtils;
import api.utils.NumberUtils;
import domain.modifiers.Language;
import services.LanguageService;

/**
 * A controller for {@link Language}, interfaces frontend call of
 * {@link FrontController} to domain and database.
 */
public class LanguageController extends Controller {

    @Override
    protected void handleGet(String[] parts, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        // Convert retrieved data into a Json text
        String json;

        // Identify endpoint
        if (parts.length > 0 && NumberUtils.isLong(parts[0])) {
            // Get a specific Language
            long id = Long.parseLong(parts[0]);
            Language language = LanguageService.getById(id);
            json = JsonUtils.toJson(language);
        } else {
            // Return all Languages
            List<Language> languages = LanguageService.getAll();
            json = JsonUtils.toJson(languages);
        }

        resp.getWriter().write(json);
    }

    @Override
    protected void handlePost(String[] parts, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Read Json input into DTO (domain object in this case)
        Language language = JsonUtils.fromJson(req.getReader(), Language.class);

        // Do operation
        LanguageService.OperationResult result;
        result = LanguageService.createLanguage(language);

        // Interpret service method result
        String msg = "Unknown operation result";
        switch (result) {
            case CREATED:
                msg = "Inserted new language";
                break;
            case ILLEGAL_ENTITY:
                msg = "Cannot insert new language - ID should not be attached";
                break;
            case DB_FAILURE:
                msg = "Failed to insert new language";
            default: break;
        }
        writeStatus(resp, result.getStatus(), msg);
    }

    @Override
    protected void handlePut(String[] parts, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Read Json input into DTO (domain object in this case)
        Language language = JsonUtils.fromJson(req.getReader(), Language.class);

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
            default: break;
        }
        writeStatus(resp, result.getStatus(), msg);
    }

    @Override
    protected void handleDelete(String[] parts, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Read Json input into DTO (domain object in this case)
        Language language = JsonUtils.fromJson(req.getReader(), Language.class);

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
            default: break;
        }
        writeStatus(resp, result.getStatus(), msg);
    }
}
