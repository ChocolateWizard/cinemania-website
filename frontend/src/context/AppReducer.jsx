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
    default:
      return state;
  }
};
