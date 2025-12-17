// api/language.ts
import type { Language } from '../features/language/types/language';
import type { ApiResponse } from './apiResponse';
import axiosInstance from './axiosInstance';
import { handleAPI } from './utils';

const BASE_URL = 'language';

/**
 * API class to make backend calls regarding Languages.
 */
class LanguageAPI {

  /** GET /lanugage */
  async fetchLanguages(): Promise<ApiResponse<Language[]>> {
    return handleAPI({
      request: () => axiosInstance.get(BASE_URL),
      errorMessage: "Get languages failed"
    });
  }

  /** POST /lanugage */
  async createLanguage(language: Language): Promise<ApiResponse> {
    return handleAPI({
      request: () => axiosInstance.post(BASE_URL, language),
      errorMessage: "Create language failed",
    });
  }

  /** PUT /lanugage */
  async updateLanguage(language: Language): Promise<ApiResponse> {
    return handleAPI({
      request: () => axiosInstance.put(BASE_URL, language),
      errorMessage: "Update language failed"
    });
  }

  /** DELETE /lanugage */
  async deleteLanguage(language: Language): Promise<ApiResponse> {
    return handleAPI({
      request: () => axiosInstance.delete(BASE_URL, { data: language }),
      errorMessage: "Delete language failed"
    });
  }
}

export default new LanguageAPI();
