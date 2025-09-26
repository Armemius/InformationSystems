import type User from '$lib/types/user';
import { writable } from 'svelte/store';

export const authStore = writable<User | undefined | null>();
const USER_DATA_KEY = "user"

authStore.subscribe(user => {
  if (user) {
    persistUser(user);
  }
});


export const loadUser = () => {
  try {
    const storedToken = localStorage.getItem(USER_DATA_KEY);
    if (storedToken) {
      authStore.set(JSON.parse(atob(storedToken)) as User);
    } else {
      authStore.set(null);
    }
  } catch {
    console.error("Invalid user data provided, failed to parse");
    clearUser();
  }
}

export const persistUser = (user: User) => {
  localStorage.setItem(USER_DATA_KEY, btoa(JSON.stringify(user)))
}

export const clearUser = () => {
  authStore.set(null);
  localStorage.removeItem(USER_DATA_KEY);
}

if (typeof window !== 'undefined') {
  loadUser();
}
