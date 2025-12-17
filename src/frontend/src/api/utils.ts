// api/utils.ts
import { isAxiosError, type AxiosResponse } from "axios";
import type { ApiResponse } from "./apiResponse";

/* GPT generated section: Pattern matching overloads for type inference...... */
// Overload #1 - with handler
export async function handleAPI<T, R>(opts: {
  request: () => Promise<AxiosResponse<T>>;
  handler: (res: AxiosResponse<T>) => R;
  successMessage?: string,
  errorMessage: string;
}): Promise<ApiResponse<R>>;

// Overload #2 - without handler
export async function handleAPI<T>(opts: {
  request: () => Promise<AxiosResponse<T>>;
  successMessage?: string,
  errorMessage: string;
}): Promise<ApiResponse<T>>;
/* ....................................................... End GPT disclaimer */

/**
 * Helper method intended to make an asynchronous axios request, then wrap
 * the output in a handler of some kind. Returns an ApiResponse with
 * .data by default if no handler provided.
 * Displays a custom error message on response failure.
 *
 * @param request asynchronous axios call returning Promise<{data: T, status:
 *        number}> on success
 * @param handler optional handler to validate/transform axios data response
 * @param successMessage optional message included if no message with success
 * @param errorMessage custom message to include if no error message returned
 * @returns ApiResponse with .data from axios request by default, handled .data
 *          if handler provided
 */
export async function handleAPI<T, R = T>({
  request,
  handler = undefined,
  successMessage = "Action succeeded",
  errorMessage = "An error occurred",
}: {
  request: () => Promise<AxiosResponse<T>>,
  handler?: (res: AxiosResponse<T>) => R,
  successMessage?: string,
  errorMessage: string
}): Promise<ApiResponse<R>> {
  try {
    const res = await request();
    // Use custom handler to validate/transform response data if provided
    // Otherwise, just return the data
    return {
      status: "success",
      message: successMessage,
      data: handler ? handler(res) : (res.data as unknown as R)
    }
  } catch (err: unknown) {
    // Catch non-Axios / programming errors (potentially from handler used)
    if (!isAxiosError(err)) {
      return {
        status: "error",
        message: "Unexpected error occured"
      }
    }

    if (err.response) {
      // Determine most specific error message applicable
      return {
        status: "error",
        message: processAPIError({
          status: err.response.status,
          dataMessage: err.response.data?.message,
          defaultMessage: errorMessage,
        })
      }
    } else {
      return {
        status: "error",
        message: "No response from server"
      }
    }
  }
}

/**
 * Private helper to convert HTTP status codes into meaningful error messages.
 * Returns `defaultMessage` instead of detecting error code 500.
 */
function processAPIError({
  status,
  dataMessage,
  defaultMessage,
}: {
  status: number;
  dataMessage?: string;
  defaultMessage: string;
}): string {
  // Base map for utilised status codes
  const statusMap: Record<number, string> = {
    400: "Invalid request data",
    401: "You are not authenticated. Please log in again.",
    403: "You do not have permission to perform this action.",
    404: "Resource(s) not found",
  };

  // Return message in priority order
  return dataMessage || statusMap[status] || defaultMessage;
}
