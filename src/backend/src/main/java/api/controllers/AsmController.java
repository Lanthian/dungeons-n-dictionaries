// java/api/controllers/AsmController.java
package api.controllers;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import api.json.JsonUtils;
import domain.modifiers.AbilityScoreModifier;
import services.AsmService;

/**
 * A controller for {@link AbilityScoreModifier}, interfaces frontend call of
 * {@link FrontController} to domain and database.
 */
public class AsmController extends Controller {

    @Override
    protected void handleGet(String[] parts, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        try {
            // Convert retrieved data into a Json text
            String json;

            // Identify endpoint
            if (parts.length > 0) {
                // Get a specific AbilityScoreModifier
                long id = Long.parseLong(parts[0]);
                AbilityScoreModifier asm = AsmService.getById(id);
                json = JsonUtils.toJson(asm);
            } else {
                // Return all AbilityScoreModifiers
                List<AbilityScoreModifier> asms = AsmService.getAll();
                json = JsonUtils.toJson(asms);
            }

            resp.getWriter().write(json);
        } catch (Exception e) {
            // Catch potential IllegalState or NumberFormat exceptions from id
            throw new ServletException("Unexpected error", e);
        }
    }

    @Override
    protected void handlePost(String[] parts, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Read Json input into DTO (domain object in this case)
            AbilityScoreModifier asm = JsonUtils.fromJson(req.getReader(), AbilityScoreModifier.class);

            // Do operation
            AsmService.OperationResult result;
            result = AsmService.createAsm(asm);

            // Interpret service method result
            String msg = "Unknown operation result";
            switch (result) {
                case CREATED:
                    msg = "Inserted new ASM";
                    break;
                case ILLEGAL_ENTITY:
                    msg = "Cannot insert new ASM - ID should not be attached";
                    break;
                case DB_FAILURE:
                    msg = "Failed to insert new ASM";
                default: break;
            }
            writeStatus(resp, result.getStatus(), msg);

        } catch (Exception e) {
            // Catch potential input IllegalState or NumberFormat exceptions
            throw new ServletException("Unexpected error", e);
        }
    }

    @Override
    protected void handlePut(String[] parts, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Read Json input into DTO (domain object in this case)
            AbilityScoreModifier asm = JsonUtils.fromJson(req.getReader(), AbilityScoreModifier.class);

            // Do operation
            AsmService.OperationResult result;
            result = AsmService.updateAsm(asm);

            // Interpret service method result
            String msg = "Unknown operation result";
            switch (result) {
                case SUCCESS:
                    msg = "Updated ASM";
                    break;
                case ILLEGAL_ENTITY:
                    msg = "Cannot update ASM - ID should be attached";
                    break;
                case DB_FAILURE:
                    msg = "Failed to update ASM";
                default: break;
            }
            writeStatus(resp, result.getStatus(), msg);

        } catch (Exception e) {
            // Catch potential input IllegalState or NumberFormat exceptions
            throw new ServletException("Unexpected error", e);
        }
    }

    @Override
    protected void handleDelete(String[] parts, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Read Json input into DTO (domain object in this case)
            AbilityScoreModifier asm = JsonUtils.fromJson(req.getReader(), AbilityScoreModifier.class);

            // Do operation
            AsmService.OperationResult result;
            result = AsmService.deleteAsm(asm);

            // Interpret service method result
            String msg = "Unknown operation result";
            switch (result) {
                case SUCCESS:
                    msg = "Deleted ASM";
                    break;
                case ILLEGAL_ENTITY:
                    msg = "Cannot delete ASM - ID should be attached";
                    break;
                case DB_FAILURE:
                    msg = "Failed to delete ASM";
                default: break;
            }
            writeStatus(resp, result.getStatus(), msg);

        } catch (Exception e) {
            // Catch potential IllegalState or NumberFormat exceptions from id
            throw new ServletException("Unexpected error", e);
        }
    }
}
