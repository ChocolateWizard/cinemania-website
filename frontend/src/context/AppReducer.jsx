export default (state, action) => {
  switch (action.type) {
    case "ADD_MOVIE_TO_WATCHLIST":
      return {
        ...state,
        movieWatchlist: [action.payload, ...state.movieWatchlist],
      };
    case "REMOVE_MOVIE_FROM_WATCHLIST":
      return {
        ...state,
        movieWatchlist: state.movieWatchlist.filter(
          (movie) => movie.id !== action.payload
        ),
      };
    case "ADD_SHOW_TO_WATCHLIST":
      return {
        ...state,
        showWatchlist: [action.payload, ...state.showWatchlist],
      };
    case "REMOVE_SHOW_FROM_WATCHLIST":
      return {
        ...state,
        showWatchlist: state.showWatchlist.filter(
          (movie) => movie.id !== action.payload
        ),
      };
    case "ADD_CRITIQUE":
      return {
        ...state,
        mediaCritiques: [action.payload, ...state.mediaCritiques],
      };
    case "UPDATE_CRITIQUE":
      return {
        ...state,
        mediaCritiques: [
          action.payload,
          ...state.mediaCritiques.filter(
            (critique) => critique.media_id !== action.payload.media_id
          ),
        ],
      };
    case "REMOVE_CRITIQUE":
      return {
        ...state,
        mediaCritiques: state.mediaCritiques.filter(
          (critique) => critique.media_id !== action.payload
        ),
      };
    case "SET_SESSION_DATA":
      return {
        ...state,
        sessionData: {
          isLoggedIn: true,
          user: {
            firstName: action.payload.first_name,
            lastName: action.payload.last_name,
            gender: action.payload.gender,
            profileName: action.payload.profile_name,
            profileImageUrl: action.payload.profile_image_url,
            role: action.payload.role,
            country: action.payload.country,
          },
        },
        movieWatchlist: action.payload.medias.filter(
          (media) => media.media_type == "movie"
        ),
        showWatchlist: action.payload.medias.filter(
          (media) => media.media_type == "tv_show"
        ),
        mediaCritiques: action.payload.critiques,
      };
    case "REMOVE_SESSION_DATA":
      return {
        ...state,
        sessionData: { isLoggedIn: false, user: null },
        movieWatchlist: [],
        showWatchlist: [],
        mediaCritiques: [],
      };
    default:
      return state;
  }
};
