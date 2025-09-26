import { API_URL } from "$lib/config";
import { authStore } from "$lib/stores/user";
import axios from "axios";

const api = axios.create({
  baseURL: API_URL
})

authStore.subscribe(user => {
  if (user) {
    api.defaults.headers["Authorization"] = user.token;
  } else {
    delete api.defaults.headers["Authorization"];
  }
})

export default api;
