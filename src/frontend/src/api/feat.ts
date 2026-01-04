// api/feat.ts
import type { Feat } from "../features/feat/types/feat";
import type { ApiResponse } from "./apiResponse";
import axiosInstance from "./axiosInstance";
import { handleAPI } from "./utils";

const BASE_URL = "feat"

/**
 * API class to make backend calls regarding Feats.
 */
class FeatAPI {

  /** GET /feat */
  async fetchFeats(): Promise<ApiResponse<Feat[]>> {
    return handleAPI({
      request: () => axiosInstance.get(BASE_URL),
      successMessage: "Feats retrieved",
      errorMessage: "Get feats failed"
    });
  }

  /** GET /feat/{id} */
  async fetchFeat(id: number): Promise<ApiResponse<Feat>> {
    return handleAPI({
      request: () => axiosInstance.get(`${BASE_URL}/${id}`),
      successMessage: "Feat retrieved",
      errorMessage: "Get feat failed"
    });
  }

  /** POST /feat */
  async createFeat(feat: Feat): Promise<ApiResponse> {
    return handleAPI({
      request: () => axiosInstance.post(BASE_URL, feat),
      successMessage: "Feat created",
      errorMessage: "Create feat failed",
    });
  }

  /** PUT /feat */
  async updateFeat(feat: Feat): Promise<ApiResponse> {
    return handleAPI({
      request: () => axiosInstance.put(BASE_URL, feat),
      successMessage: "Feat updated",
      errorMessage: "Update feat failed",
    });
  }

  /** DELETE /feat */
  async deleteFeat(feat: Feat): Promise<ApiResponse> {
    return handleAPI({
      request: () => axiosInstance.delete(BASE_URL, { data: feat }),
      successMessage: "Feat deleted",
      errorMessage: "Delete feat failed",
    });
  }
}

export default new FeatAPI();