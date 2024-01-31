import axios from "axios";

const BASE_URL = "" + import.meta.env.VITE_API_URL;

export function fetchPopularMovies(page, size) {
  return axios.get(BASE_URL + `/api/movies/popular?page=${page}&size=${size}`);
}
export function fetchPopularShows(page, size) {
  return axios.get(BASE_URL + `/api/tv/popular?page=${page}&size=${size}`);
}
export function fetchCurrentMovies(page, size) {
  return axios.get(BASE_URL + `/api/movies/current?page=${page}&size=${size}`);
}
export function fetchCurrentShows(page, size) {
  return axios.get(BASE_URL + `/api/tv/current?page=${page}&size=${size}`);
}
export function fetchMovieDetails(id) {
  return axios.get(BASE_URL + `/api/movies/${id}/details`);
}
export function fetchShowDetails(id) {
  return axios.get(BASE_URL + `/api/tv/${id}/details`);
}
export function fetchCountries() {
  return axios.get(BASE_URL + `/api/countries`);
}
export function fetchMediaForSearchbar(page, size, title) {
  return axios.get(
    BASE_URL + `/api/medias/search?page=${page}&size=${size}&title=${title}`
  );
}
export function fetchMediaForSearchResults(page, size, title) {
  return axios.get(
    BASE_URL + `/api/medias/search?page=${page}&size=${size}&title=${title}`
  );
}

export function register(data) {
  return axios.post(BASE_URL + `/api/auth/register`, data, {
    withCredentials: true,
    headers: { "Content-Type": "multipart/form-data" },
  });
}
export function login(data) {
  return axios.post(BASE_URL + `/api/auth/login`, data, {
    withCredentials: true,
    headers: { "Content-Type": "application/json" },
  });
}
export function logout() {
  return axios.post(BASE_URL + `/api/auth/logout`, null, {
    withCredentials: true,
  });
}
export function postMediaIntoWatchlist(id) {
  return axios.post(BASE_URL + `/api/users/library/${id}`, null, {
    withCredentials: true,
  });
}
export function deleteMediaFromWatchlist(id) {
  return axios.delete(BASE_URL + `/api/users/library/${id}`, {
    withCredentials: true,
  });
}
export function postCritique(id, critique) {
  return axios.post(BASE_URL + `/api/critiques/${id}`, critique, {
    withCredentials: true,
    headers: { "Content-Type": "application/json" },
  });
}
export function putCritique(id, critique) {
  return axios.put(BASE_URL + `/api/critiques/${id}`, critique, {
    withCredentials: true,
  });
}
export function deleteCritique(id) {
  return axios.delete(BASE_URL + `/api/critiques/${id}`, {
    withCredentials: true,
  });
}

