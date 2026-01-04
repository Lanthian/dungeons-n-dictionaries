// features/feat/utils/getFeatFull.tsx
import FeatAPI from '../../../api/feat'
import { getData, isSuccess } from "../../../api/apiResponse";
import { toastApiResponse } from "../../../utils/toastApiResponse";
import type { Feat } from "../types/feat";

// Input parameters for FeatView
type Params = {
  feat: Feat;
  silentToast?: boolean;
}

/**
 * Retrieve a fully saturated Feat from a partially saturated one.
 *
 * @param feat A Feat object that has an ID set
 * @param silentToast Silences toast notifications on API actions if true
 */
export async function getFeatFull({
  feat,
  silentToast = false
}: Params) {
  const res = await FeatAPI.fetchFeat(feat.id as number);
  // Silence toast notification if needed
  if (silentToast ? isSuccess(res) : toastApiResponse(res)) {
    // User needs to check that if successful, response type is not undefined
    return getData(res);
  }
  // Return null on failure
  return null;
}
