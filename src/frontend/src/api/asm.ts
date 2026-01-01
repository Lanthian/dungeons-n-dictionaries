// api/asm.ts
import type { Asm } from '../features/asm/types/asm';
import type { ApiResponse } from './apiResponse';
import axiosInstance from './axiosInstance';
import { handleAPI } from './utils';

const BASE_URL = 'asm';

/**
 * API class to make backend calls regarding ability score modifiers (ASMs).
 */
class AsmAPI {

  /** GET /asm */
  async fetchAsms(): Promise<ApiResponse<Asm[]>> {
    return handleAPI({
      request: () => axiosInstance.get(BASE_URL),
      successMessage: "ASMs retrieved",
      errorMessage: "Get ASMs failed"
    });
  }

  /** GET /asm/{id} */
  async fetchAsm(id: number): Promise<ApiResponse<Asm>> {
    return handleAPI({
      request: () => axiosInstance.get(`${BASE_URL}/${id}`),
      successMessage: "ASM retrieved",
      errorMessage: "Get ASM failed"
    });
  }

  /** POST /asm */
  async createAsm(asm: Asm): Promise<ApiResponse> {
    return handleAPI({
      request: () => axiosInstance.post(BASE_URL, asm),
      successMessage: "ASM created",
      errorMessage: "Create ASM failed",
    });
  }

  /** PUT /asm */
  async updateAsm(asm: Asm): Promise<ApiResponse> {
    return handleAPI({
      request: () => axiosInstance.put(BASE_URL, asm),
      successMessage: "ASM updated",
      errorMessage: "Update ASM failed"
    });
  }

  /** DELETE /asm */
  async deleteAsm(asm: Asm): Promise<ApiResponse> {
    return handleAPI({
      request: () => axiosInstance.delete(BASE_URL, { data: asm }),
      successMessage: "ASM deleted",
      errorMessage: "Delete ASM failed"
    });
  }
}

export default new AsmAPI();
