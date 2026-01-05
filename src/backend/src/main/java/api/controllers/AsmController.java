// java/api/controllers/AsmController.java
package api.controllers;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import api.json.JsonUtils;
import api.utils.NumberUtils;
import domain.modifiers.AbilityScoreModifier;
import services.AsmService;

/**
 * A controller for {@link AbilityScoreModifier}, interfaces frontend call of
 * {@link FrontController} to domain and database.
 */
public class AsmController extends Controller {

    @Override
    protected void handleGet(String[] parts, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        // Convert retrieved data into a Json text
        String json;

        // Identify endpoint
        if (parts.length > 0 && NumberUtils.isLong(parts[0])) {
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
    }

    @Override
    protected void handlePost(String[] parts, HttpServletRequest req, HttpServletResponse resp) throws IOException {
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
    }

    @Override
    protected void handlePut(String[] parts, HttpServletRequest req, HttpServletResponse resp) throws IOException {            // Read Json input into DTO (domain object in this case)
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
    }

    @Override
    protected void handleDelete(String[] parts, HttpServletRequest req, HttpServletResponse resp) throws IOException {
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
    }
}
