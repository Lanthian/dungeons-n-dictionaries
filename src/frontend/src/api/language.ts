// api/language.ts
import type { Language } from '../features/language/types/language';
import axiosInstance from './axiosInstance';
import { handleAPI } from './utils';

const BASE_URL = 'language';

/**
 * API class to make backend calls regarding Languages.
 */
class LanguageAPI {

  async fetchLanguages(): Promise<Language[]> {
    return handleAPI({
      request: () => axiosInstance.get(`${BASE_URL}`),
      errorMessage: "Get languages failed"
    });
  }
}

export default new LanguageAPI();
