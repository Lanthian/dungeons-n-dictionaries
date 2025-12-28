// java/api/controllers/ProficiencyController.java
package api.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import api.json.JsonUtils;
import api.utils.NumberUtils;
import domain.modifiers.proficiency.Proficiency;
import domain.types.ProficiencyType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.ProficiencyService;

/**
 * A controller for {@link Proficiency} and all subclasses, interfaces frontend
 * call of {@link FrontController} to domain and database.
 */
public class ProficiencyController extends Controller {

    @Override
    protected void handleGet(String[] parts, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        try {
            // Convert retrieved data into a Json text
            String json;

            // Identify endpoint
            if (parts.length == 1 && NumberUtils.isLong(parts[0])) {
                // /{id} - Get a specific Proficiency
                long id = Long.parseLong(parts[0]);
                Proficiency proficiency = ProficiencyService.getById(id);
                json = JsonUtils.toJson(proficiency);

            } else if (parts.length > 0) {
                // /{type} - Return all proficiencies based on type
                ProficiencyType type = ProficiencyType.fromString(parts[0]);
                List<? extends Proficiency> proficiencies = ProficiencyService.getAllByType(type);
                json = JsonUtils.toJson(proficiencies);

            } else {
                throw new IllegalStateException("Proficiency type or ID required");
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
            Proficiency proficiency = JsonUtils.fromJson(req.getReader(), Proficiency.class);

            // Do operation
            ProficiencyService.OperationResult result;
            result = ProficiencyService.createProficiency(proficiency);

            // Interpret service method result
            String msg = "Unknown operation result";
            switch (result) {
                case SUCCESS:
                    msg = "Inserted new proficiency";
                    break;
                case ILLEGAL_ENTITY:
                    msg = "Cannot insert new proficiency - ID should not be attached";
                    break;
                case DB_FAILURE:
                    msg = "Failed to insert new proficiency";
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
            Proficiency proficiency = JsonUtils.fromJson(req.getReader(), Proficiency.class);

            // Do operation
            ProficiencyService.OperationResult result;
            result = ProficiencyService.updateProficiency(proficiency);

            // Interpret service method result
            String msg = "Unknown operation result";
            switch (result) {
                case SUCCESS:
                    msg = "Updated proficiency";
                    break;
                case ILLEGAL_ENTITY:
                    msg = "Cannot update proficiency - ID should be attached";
                    break;
                case DB_FAILURE:
                    msg = "Failed to update proficiency";
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
            Proficiency proficiency = JsonUtils.fromJson(req.getReader(), Proficiency.class);

            // Do operation
            ProficiencyService.OperationResult result;
            result = ProficiencyService.deleteProficiency(proficiency);

            // Interpret service method result
            String msg = "Unknown operation result";
            switch (result) {
                case SUCCESS:
                    msg = "Deleted proficiency";
                    break;
                case ILLEGAL_ENTITY:
                    msg = "Cannot delete proficiency - ID should be attached";
                    break;
                case DB_FAILURE:
                    msg = "Failed to delete proficiency";
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
