export interface ToggleApi {
  toggle?: () => void
}

export type SelectApi = ToggleApi;
export type CreateApi = ToggleApi;
export type AbsorbApi = ToggleApi;
export type MergeApi = ToggleApi;

export interface EditApi<T> {
  toggle?: (arg0: T) => void;
}

export interface ShowApi<T> {
  toggle?: (arg0: T) => void;
  toggleById?: (arg0: number) => void;
}
