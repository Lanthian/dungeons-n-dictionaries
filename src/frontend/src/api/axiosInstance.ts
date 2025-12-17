/** api/axiosInstance.ts
 *
 * Developed in a private project, in collaboration with:
 * - Yuk Hang Cheng : https://github.com/SKYYKS-9998
 */
import axios, { type AxiosInstance } from 'axios';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';

const axiosInstance: AxiosInstance = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

export default axiosInstance;
