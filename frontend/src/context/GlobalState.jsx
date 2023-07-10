import React, { createContext, useReducer, useEffect } from "react";
import AppReducer from "./AppReducer";

const initialState = {
  movieWatchlist: localStorage.getItem("movieWatchlist") ? JSON.parse(localStorage.getItem("movieWatchlist")): [],
  showWatchlist: localStorage.getItem("showWatchlist") ? JSON.parse(localStorage.getItem("showWatchlist")): [],
};

//create context
export const GlobalContext = createContext(initialState);

//provider component
export const GlobalProvider = (props) => {
  const [state, dispach] = useReducer(AppReducer, initialState);

  useEffect(() => {
    localStorage.setItem(
      "movieWatchlist",
      JSON.stringify(state.movieWatchlist)
    );
    localStorage.setItem("showWatchlist", JSON.stringify(state.showWatchlist));
  }, [state]);

  //actions
  const addMovieToWatchlist = (movie) => {
    dispach({ type: "ADD_MOVIE_TO_WATCHLIST", payload: movie });
  };

  const removeMovieFromWatchlist = (id) => {
    dispach({ type: "REMOVE_MOVIE_FROM_WATCHLIST", payload: id });
  };

  const addShowToWatchlist = (show) => {
    dispach({ type: "ADD_SHOW_TO_WATCHLIST", payload: show });
  };

  const removeShowFromWatchlist = (id) => {
    dispach({ type: "REMOVE_SHOW_FROM_WATCHLIST", payload: id });
  };

  //This is so we can use these actions in our components
  return (
    <GlobalContext.Provider
      value={{
        movieWatchlist: state.movieWatchlist,
        showWatchlist: state.showWatchlist,
        addMovieToWatchlist,
        removeMovieFromWatchlist,
        addShowToWatchlist,
        removeShowFromWatchlist,
      }}
    >
      {props.children}
    </GlobalContext.Provider>
  );
};
