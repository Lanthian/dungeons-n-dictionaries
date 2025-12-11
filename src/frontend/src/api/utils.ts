// api/utils.ts
import type { AxiosResponse } from "axios";

/* GPT generated section: Pattern matching overloads for type inference...... */
// Overload #1 - with handler
export async function handleAPI<T, R>(opts: {
  request: () => Promise<AxiosResponse<T>>;
  handler: (res: AxiosResponse<T>) => R;
  errorMessage: string;
}): Promise<R>;

// Overload #2 - without handler
export async function handleAPI<T>(opts: {
  request: () => Promise<AxiosResponse<T>>;
  errorMessage: string;
}): Promise<T>;
/* ....................................................... End GPT disclaimer */

/**
 * Helper method intended to make an asynchronous axios request, then wrap
 * the output in a handler of some kind. Returns the response .data by default
 * if no handler provided. Displays a custom error message on response failure.
 *
 * @param request asynchronous axios call returning Promise<{data: T, status:
 *        number}> on success
 * @param handler optional handler to validate/transform axios response
 * @param errorMessage custom message to throw if no error message returned
 * @returns res.data from axios request by default, handled if handler provided
 */
export async function handleAPI<T, R = T>({
  request,
  handler = undefined,
  errorMessage = "An error occurred",
}: {
  request: () => Promise<AxiosResponse<T>>,
  handler?: (res: AxiosResponse<T>) => R,
  errorMessage: string
}): Promise<R> {
  try {
    const res = await request();
    // Use custom handler to validate/transform response if provided
    // Otherwise, just return the data
    return handler ? handler(res) : (res.data as unknown as R);

  } catch (err: any) {
    if (err.response) {
      // Determine most specific error message applicable
      const message = processAPIError({
        status: err.response.status,
        dataMessage: err.response.data?.message,
        defaultMessage: errorMessage,
      });
      throw new Error(message);
    } else if (err.request) {
      throw new Error("No response from server");
    } else {
      throw new Error(err.message);
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
