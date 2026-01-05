// java/api/controllers/FeatController.java
package api.controllers;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import api.json.JsonUtils;
import domain.modifiers.Feat;
import services.FeatService;

/**
 * A controller for {@link Feat}, interfaces frontend call of
 * {@link FrontController} to domain and database.
 */
public class FeatController extends Controller {

    @Override
    protected void handleGet(String[] parts, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        try {
            // Convert retrieved data into a Json text
            String json;

            // Identify endpoint
            if (parts.length > 0) {
                // Get a specific Feat
                long id = Long.parseLong(parts[0]);
                Feat feat = FeatService.getById(id);
                json = JsonUtils.toJson(feat);
            } else {
                // Return all Feats
                List<Feat> feats = FeatService.getAll();
                json = JsonUtils.toJson(feats);
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
            Feat feat = JsonUtils.fromJson(req.getReader(), Feat.class);

            // Do operation
            FeatService.OperationResult result;
            result = FeatService.createFeat(feat);

            // Interpret service method result
            String msg = "Unknown operation result";
            switch (result) {
                case CREATED:
                    msg = "Inserted new feat";
                    break;
                case ILLEGAL_ENTITY:
                    msg = "Cannot insert new feat - ID should not be attached";
                    break;
                case DB_FAILURE:
                    msg = "Failed to insert new feat";
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
            Feat feat = JsonUtils.fromJson(req.getReader(), Feat.class);

            // Do operation
            FeatService.OperationResult result;
            result = FeatService.updateFeat(feat);

            // Interpret service method result
            String msg = "Unknown operation result";
            switch (result) {
                case SUCCESS:
                    msg = "Updated feat";
                    break;
                case ILLEGAL_ENTITY:
                    msg = "Cannot update feat - ID should be attached";
                    break;
                case DB_FAILURE:
                    msg = "Failed to update feat";
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
            Feat feat = JsonUtils.fromJson(req.getReader(), Feat.class);

            // Do operation
            FeatService.OperationResult result;
            result = FeatService.deleteFeat(feat);

            // Interpret service method result
            String msg = "Unknown operation result";
            switch (result) {
                case SUCCESS:
                    msg = "Deleted feat";
                    break;
                case ILLEGAL_ENTITY:
                    msg = "Cannot delete feat - ID should be attached";
                    break;
                case DB_FAILURE:
                    msg = "Failed to delete feat";
                default: break;
            }
            writeStatus(resp, result.getStatus(), msg);

        } catch (Exception e) {
            // Catch potential IllegalState or NumberFormat exceptions from id
            throw new ServletException("Unexpected error", e);
        }
    }
}
