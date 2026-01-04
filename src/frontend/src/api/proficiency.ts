// api/proficiency.ts
import type {
  ArmourProficiency,
  Proficiency,
  SkillProficiency,
  ToolProficiency
} from "../features/proficiency/types/proficiency";
import type { ApiResponse } from "./apiResponse";
import axiosInstance from "./axiosInstance";
import { handleAPI } from "./utils";

const BASE_URL = "proficiency"

/**
 * API class to make backend calls regarding Proficiency-s.
 */
class ProficiencyAPI {

  /* --------------------------- Fetch  Endpoints --------------------------- */

  /** GET /proficiency/{id} */
  async fetchProficiency(id: number): Promise<ApiResponse<Proficiency>> {
    return handleAPI({
      request: () => axiosInstance.get(`${BASE_URL}/${id}`),
      successMessage: "Proficiency retrieved",
      errorMessage: "Get proficiency failed"
    });
  }

  /** GET /proficiency/armour */
  async fetchArmourProficiencies(): Promise<ApiResponse<ArmourProficiency[]>> {
    return handleAPI({
      request: () => axiosInstance.get(`${BASE_URL}/armour`),
      successMessage: "Armour proficiencies retrieved",
      errorMessage: "Get armour proficiencies failed"
    });
  }

  /** GET /proficiency/skill */
  async fetchSkillProficiencies(): Promise<ApiResponse<SkillProficiency[]>> {
    return handleAPI({
      request: () => axiosInstance.get(`${BASE_URL}/skill`),
      successMessage: "Skill proficiencies retrieved",
      errorMessage: "Get skill proficiencies failed"
    });
  }

  /** GET /proficiency/tool */
  async fetchToolProficiencies(): Promise<ApiResponse<ToolProficiency[]>> {
    return handleAPI({
      request: () => axiosInstance.get(`${BASE_URL}/tool`),
      successMessage: "Tool proficiencies retrieved",
      errorMessage: "Get tool proficiencies failed"
    });
  }

  /* ------------------- Insert, Update, Delete Endpoints ------------------- */

  /** POST /proficiency */
  async createProficiency(proficiency: Proficiency): Promise<ApiResponse> {
    return handleAPI({
      request: () => axiosInstance.post(BASE_URL, proficiency),
      successMessage: "Proficiency created",
      errorMessage: "Create proficiency failed",
    });
  }

  /** PUT /proficiency */
  async updateProficiency(proficiency: Proficiency): Promise<ApiResponse> {
    return handleAPI({
      request: () => axiosInstance.put(BASE_URL, proficiency),
      successMessage: "Proficiency updated",
      errorMessage: "Update proficiency failed",
    });
  }

  /** DELETE /proficiency */
  async deleteProficiency(proficiency: Proficiency): Promise<ApiResponse> {
    return handleAPI({
      request: () => axiosInstance.delete(BASE_URL, { data: proficiency }),
      successMessage: "Proficiency deleted",
      errorMessage: "Delete proficiency failed",
    });
  }
}

export default new ProficiencyAPI();