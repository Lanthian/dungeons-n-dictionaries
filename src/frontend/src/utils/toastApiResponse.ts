// utils/toastApiResponse.ts
import { toast } from 'react-hot-toast';
import { getMessage, isSuccess, type ApiResponse } from '../api/apiResponse';

/**
 * Interprets an ApiResponse `res` and utilises `toast` to display the relevant
 * success/error message. Uses `defaultSuccess` and `defaultError` when no
 * message accompanies the API response.
 *
 * @returns true if response status === "success", false otherwise
 */
export function toastApiResponse<T>(
  res: ApiResponse<T>,
  defaultSuccess = "Request succeeded.",
  defaultError = "Request failed."
) {
  if (isSuccess(res)) {
    toast.success(getMessage(res) || defaultSuccess);
    return true;
  } else {
    toast.error(getMessage(res) || defaultError);
    return false;
  }
}
