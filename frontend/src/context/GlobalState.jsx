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
  critiquesComments: localStorage.getItem("critiquesComments")
    ? JSON.parse(localStorage.getItem("critiquesComments"))
    : [],
  critiquesLikeDislikes: localStorage.getItem("critiquesLikeDislikes")
    ? JSON.parse(localStorage.getItem("critiquesLikeDislikes"))
    : [],
  commentsLikeDislikes: localStorage.getItem("commentsLikeDislikes")
    ? JSON.parse(localStorage.getItem("commentsLikeDislikes"))
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
    localStorage.setItem(
      "critiquesComments",
      JSON.stringify(state.critiquesComments)
    );
    localStorage.setItem(
      "critiquesLikeDislikes",
      JSON.stringify(state.critiquesLikeDislikes)
    );
    localStorage.setItem(
      "commentsLikeDislikes",
      JSON.stringify(state.commentsLikeDislikes)
    );
  }, [state]);

  //actions
  const setSessionData = (user) => {
    dispach({ type: "SET_SESSION_DATA", payload: user });
  };
  const removeSessionData = () => {
    dispach({ type: "REMOVE_SESSION_DATA", payload: null });
  };

  const addMovieToWatchlist = (movie) => {
    dispach({ type: "ADD_MOVIE_TO_WATCHLIST", payload: movie });
  };
  const removeMovieFromWatchlist = (movieId) => {
    dispach({ type: "REMOVE_MOVIE_FROM_WATCHLIST", payload: movieId });
  };

  const addShowToWatchlist = (show) => {
    dispach({ type: "ADD_SHOW_TO_WATCHLIST", payload: show });
  };
  const removeShowFromWatchlist = (showId) => {
    dispach({ type: "REMOVE_SHOW_FROM_WATCHLIST", payload: showId });
  };

  //CRITIQUE
  const addCritique = (critiqueId) => {
    dispach({ type: "ADD_CRITIQUE", payload: critiqueId });
  };
  //critique.id of critique to be deleted
  //if user has a comment under deleted critique, provide its comment.id
  const removeCritique = (critiqueId, commentId) => {
    dispach({
      type: "REMOVE_CRITIQUE",
      payload: { critique_id: critiqueId, comment_id: commentId },
    });
  };
  //CRITIQUE LIKE/DISLIKE
  const addCritiqueLikeDislike = (likeDislike) => {
    dispach({ type: "ADD_CRITIQUE_LIKEDISLIKE", payload: likeDislike });
  };
  const updateCritiqueLikeDislike = (likeDislike) => {
    dispach({ type: "UPDATE_CRITIQUE_LIKEDISLIKE", payload: likeDislike });
  };
  const removeCritiqueLikeDislike = (critiqueId) => {
    dispach({ type: "REMOVE_CRITIQUE_LIKEDISLIKE", payload: critiqueId });
  };

  //COMMENTS
  const addComment = (commentId) => {
    dispach({ type: "ADD_COMMENT", payload: commentId });
  };
  const removeComment = (commentId) => {
    dispach({ type: "REMOVE_COMMENT", payload: commentId });
  };

  //COMMENTS LIKE/DISLIKE
  const addCommentLikeDislike = (likeDislike) => {
    dispach({ type: "ADD_COMMENT_LIKEDISLIKE", payload: likeDislike });
  };
  const updateCommentLikeDislike = (likeDislike) => {
    dispach({ type: "UPDATE_COMMENT_LIKEDISLIKE", payload: likeDislike });
  };
  const removeCommentLikeDislike = (commentId) => {
    dispach({ type: "REMOVE_COMMENT_LIKEDISLIKE", payload: commentId });
  };

  //This is so we can use these actions in our components
  return (
    <GlobalContext.Provider
      value={{
        sessionData: state.sessionData,
        movieWatchlist: state.movieWatchlist,
        showWatchlist: state.showWatchlist,
        mediaCritiques: state.mediaCritiques,
        critiquesComments: state.critiquesComments,
        critiquesLikeDislikes: state.critiquesLikeDislikes,
        commentsLikeDislikes: state.commentsLikeDislikes,
        setSessionData,
        removeSessionData,
        addMovieToWatchlist,
        removeMovieFromWatchlist,
        addShowToWatchlist,
        removeShowFromWatchlist,
        addCritique,
        removeCritique,
        addCritiqueLikeDislike,
        updateCritiqueLikeDislike,
        removeCritiqueLikeDislike,
        addComment,
        removeComment,
        addCommentLikeDislike,
        updateCommentLikeDislike,
        removeCommentLikeDislike,
      }}
    >
      {props.children}
    </GlobalContext.Provider>
  );
};
