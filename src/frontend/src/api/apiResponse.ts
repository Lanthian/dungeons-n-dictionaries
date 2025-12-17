// api/apiResponse.ts

/**
 * Standardised backend response format as a type.
 */
export type ApiResponse<T = void> = {
  status: "success" | "error";
  message?: string;
  data?: T;
}

/* -------------------------------- Getters  -------------------------------- */

export function isSuccess<T>(res: ApiResponse<T>): boolean {
  return res.status === "success";
}

export function getData<T>(res: ApiResponse<T>): T | undefined {
  return isSuccess(res) ? res.data : undefined;
}

export function getMessage<T>(res: ApiResponse<T>): string | undefined {
  return res.message;
}
