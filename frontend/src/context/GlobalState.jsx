import React, { createContext, useReducer, useEffect } from "react";
import AppReducer from "./AppReducer";

const initialState = {
  sessionData: localStorage.getItem("sessionData")
    ? JSON.parse(localStorage.getItem("sessionData"))
    : {
        isLoggedIn: false,
        user: null,
      },
  movieWatchlist: localStorage.getItem("movieWatchlist")
    ? JSON.parse(localStorage.getItem("movieWatchlist"))
    : [],
  showWatchlist: localStorage.getItem("showWatchlist")
    ? JSON.parse(localStorage.getItem("showWatchlist"))
    : [],
  mediaCritiques: localStorage.getItem("mediaCritiques")
    ? JSON.parse(localStorage.getItem("mediaCritiques"))
    : [],
};

//create context
export const GlobalContext = createContext(initialState);

//provider component
export const GlobalProvider = (props) => {
  const [state, dispach] = useReducer(AppReducer, initialState);

  useEffect(() => {
    localStorage.setItem("sessionData", JSON.stringify(state.sessionData));
    localStorage.setItem(
      "movieWatchlist",
      JSON.stringify(state.movieWatchlist)
    );
    localStorage.setItem("showWatchlist", JSON.stringify(state.showWatchlist));
    localStorage.setItem(
      "mediaCritiques",
      JSON.stringify(state.mediaCritiques)
    );
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

  const addCritique = (critique) => {
    dispach({ type: "ADD_CRITIQUE", payload: critique });
  };
  const updateCritique = (critique) => {
    dispach({ type: "UPDATE_CRITIQUE", payload: critique });
  };

  const removeCritique = (id) => {
    dispach({ type: "REMOVE_CRITIQUE", payload: id });
  };
  const setSessionData = (user) => {
    dispach({ type: "SET_SESSION_DATA", payload: user });
  };
  const removeSessionData = () => {
    dispach({ type: "REMOVE_SESSION_DATA", payload: null });
  };

  //This is so we can use these actions in our components
  return (
    <GlobalContext.Provider
      value={{
        sessionData: state.sessionData,
        movieWatchlist: state.movieWatchlist,
        showWatchlist: state.showWatchlist,
        mediaCritiques: state.mediaCritiques,
        addMovieToWatchlist,
        removeMovieFromWatchlist,
        addShowToWatchlist,
        removeShowFromWatchlist,
        addCritique,
        updateCritique,
        removeCritique,
        setSessionData,
        removeSessionData,
      }}
    >
      {props.children}
    </GlobalContext.Provider>
  );
};
