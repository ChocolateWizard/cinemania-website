import axios from "axios";

const BASE_URL = "" + import.meta.env.VITE_API_URL;

export function fetchPopularMovies(page, size) {
  return axios.get(BASE_URL + `/api/movies/popular?page=${page}&size=${size}`);
}
export function fetchPopularShows(page, size) {
  return axios.get(BASE_URL + `/api/tv/popular?page=${page}&size=${size}`);
}
export function fetchMediaDetails(mediaId, mediaType) {
  switch (mediaType) {
    case "movie":
      return axios.get(BASE_URL + `/api/movies/${mediaId}/details`);
    case "tv_show":
      return axios.get(BASE_URL + `/api/tv/${mediaId}/details`);
    default:
      console.error("Unknown media type");
      return null;
  }
}

export function fetchCountries() {
  return axios.get(BASE_URL + `/api/countries`);
}
export function fetchMediaForSearchbar(page, size, title) {
  return axios.get(
    BASE_URL + `/api/medias/search?page=${page}&size=${size}&title=${title}`
  );
}
export function fetchMediaForSearchResults(url) {
  return axios.get(BASE_URL + url);
}
export function fetchMoviesForMoviesPage(url) {
  return axios.get(BASE_URL + url);
}
export function fetchShowsForShowsPage(url) {
  return axios.get(BASE_URL + url);
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
export function postMediaIntoWatchlist(mediaId) {
  return axios.post(BASE_URL + `/api/users/library/${mediaId}`, null, {
    withCredentials: true,
  });
}
export function deleteMediaFromWatchlist(mediaId) {
  return axios.delete(BASE_URL + `/api/users/library/${mediaId}`, {
    withCredentials: true,
  });
}

//CRITIQUES
export function postCritique(critique) {
  return axios.post(BASE_URL + `/api/critiques`, critique, {
    withCredentials: true,
    headers: { "Content-Type": "application/json" },
  });
}
export function deleteCritique(critiqueId) {
  return axios.delete(BASE_URL + `/api/critiques/${critiqueId}`, {
    withCredentials: true,
  });
}

//COMMENTS
export function postComment(critiqueId, comment) {
  return axios.post(
    BASE_URL + `/api/critiques/${critiqueId}/comments`,
    comment,
    {
      withCredentials: true,
      headers: { "Content-Type": "application/json" },
    }
  );
}
export function deleteComment(critiqueId, commentId) {
  return axios.delete(
    BASE_URL + `/api/critiques/${critiqueId}/comments/${commentId}`,
    {
      withCredentials: true,
    }
  );
}

//CRITIQUE LIKES/DISLIKES
export function postCritiqueLike(critiqueId) {
  return axios.post(BASE_URL + `/api/critiques/${critiqueId}/likes`, null, {
    withCredentials: true,
  });
}
export function postCritiqueDislike(critiqueId) {
  return axios.post(BASE_URL + `/api/critiques/${critiqueId}/dislikes`, null, {
    withCredentials: true,
  });
}
export function putCritiqueLike(critiqueId) {
  return axios.put(BASE_URL + `/api/critiques/${critiqueId}/likes`, null, {
    withCredentials: true,
  });
}
export function putCritiqueDislike(critiqueId) {
  return axios.put(BASE_URL + `/api/critiques/${critiqueId}/dislikes`, null, {
    withCredentials: true,
  });
}
export function deleteCritiqueLike(critiqueId) {
  return axios.delete(BASE_URL + `/api/critiques/${critiqueId}/likes`, {
    withCredentials: true,
  });
}
export function deleteCritiqueDislike(critiqueId) {
  return axios.delete(BASE_URL + `/api/critiques/${critiqueId}/dislikes`, {
    withCredentials: true,
  });
}
//COMMENT LIKES/DISLIKES
export function postCommentLike(critiqueId, commentId) {
  return axios.post(
    BASE_URL + `/api/critiques/${critiqueId}/comments/${commentId}/likes`,
    null,
    {
      withCredentials: true,
    }
  );
}
export function postCommentDislike(critiqueId, commentId) {
  return axios.post(
    BASE_URL + `/api/critiques/${critiqueId}/comments/${commentId}/dislikes`,
    null,
    {
      withCredentials: true,
    }
  );
}
export function putCommentLike(critiqueId, commentId) {
  return axios.put(
    BASE_URL + `/api/critiques/${critiqueId}/comments/${commentId}/likes`,
    null,
    {
      withCredentials: true,
    }
  );
}
export function putCommentDislike(critiqueId, commentId) {
  return axios.put(
    BASE_URL + `/api/critiques/${critiqueId}/comments/${commentId}/dislikes`,
    null,
    {
      withCredentials: true,
    }
  );
}
export function deleteCommentLike(critiqueId, commentId) {
  return axios.delete(
    BASE_URL + `/api/critiques/${critiqueId}/comments/${commentId}/likes`,
    {
      withCredentials: true,
    }
  );
}
export function deleteCommentDislike(critiqueId, commentId) {
  return axios.delete(
    BASE_URL + `/api/critiques/${critiqueId}/comments/${commentId}/dislikes`,
    {
      withCredentials: true,
    }
  );
}
