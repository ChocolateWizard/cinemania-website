import React, { useContext, useEffect, useState } from "react";
import { GlobalContext } from "../../context/GlobalState";
import {
  deleteMediaFromWatchlist,
  postMediaIntoWatchlist,
} from "../../utils/Api";
import { toast } from "react-toastify";

export default function WatchlistButton({
  isVisible,
  shouldAdd,
  mediaType,
  data,
}) {

  const {
    addMovieToWatchlist,
    removeMovieFromWatchlist,
    addShowToWatchlist,
    removeShowFromWatchlist,
    sessionData,
  } = useContext(GlobalContext);

  const [loading, setLoading] = useState(false);
  const [isAdd, setIsAdd] = useState(shouldAdd);

  

  // useEffect(() => {}, [loading]);

  function callToAddMovieToWatchlist(movie) {
    setLoading(true);
    postMediaIntoWatchlist(movie.id)
      .then((response) => {
        if (response.status == 200) {
          addMovieToWatchlist(movie);
          setIsAdd(false);
          toast.success("Movie added to watchlist!");
        } else {
          console.error(response.data);
          toast.error("Unable to add movie to watchlist!");
        }
      })
      .catch((err) => {
        console.error(err);
        toast.error("Unable to add movie to watchlist!");
      })
      .finally(() => {
        setLoading(false);
      });
  }
  function callToRemoveMovieFromWatchlist(id) {
    setLoading(true);
    deleteMediaFromWatchlist(sessionData.jwt, id)
      .then((response) => {
        if (response.status == 200) {
          removeMovieFromWatchlist(id);
        } else {
          //TODO: error message when it didnt remove
          console.error(
            "Unable to remove movie from watchlist!" + response.data
          );
        }
      })
      .catch((err) => {
        //TODO: error message when it didnt add
        console.error("Unable to remove movie from watchlist!" + err);
      })
      .finally(() => {
        setLoading(false);
      });
  }

  function callToAddShowToWatchlist(show) {
    setLoading(true);
    postMediaIntoWatchlist(sessionData.jwt, show.id)
      .then((response) => {
        if (response.status == 200) {
          addShowToWatchlist(show);
        } else {
          //TODO: error message when it didnt add
          console.error("Unable to add tv show to watchlist!" + response.data);
        }
      })
      .catch((err) => {
        //TODO: error message when it didnt add
        console.error("Unable to add tv show to watchlist!" + err);
      })
      .finally(() => {
        setLoading(false);
      });
  }
  function callToRemoveShowFromWatchlist(id) {
    setLoading(true);
    deleteMediaFromWatchlist(sessionData.jwt, id)
      .then((response) => {
        if (response.status == 200) {
          removeShowFromWatchlist(id);
        } else {
          //TODO: error message when it didnt remove
          console.error(
            "Unable to remove tv show from watchlist!" + response.data
          );
        }
      })
      .catch((err) => {
        //TODO: error message when it didnt add
        console.error("Unable to remove tv show from watchlist!" + err);
      })
      .finally(() => {
        setLoading(false);
      });
  }

  function buttonName(loading, shouldAdd) {
    if (loading) {
      return "Loading...";
    }
    return shouldAdd ? "Add to watchlist" : "Remove from watchlist";
  }

  function buttonStyle(loading, shouldAdd) {
    if (loading) {
      return "bg-onyx-tint text-onyx-primary-10 hover:bg-onyx-primary-30";
    }
    return shouldAdd
      ? "bg-mellon-primary text-onyx-tint hover:bg-mellon-shade"
      : "bg-onyx-tint text-onyx-primary-10 hover:bg-onyx-primary-30";
  }
  function buttonAction(shouldAdd, mediaType, data) {
    switch (mediaType) {
      case "movie":
        shouldAdd
          ? callToAddMovieToWatchlist(data)
          : callToRemoveMovieFromWatchlist(data.id);
        break;
      case "tv_show":
        shouldAdd
          ? callToAddShowToWatchlist(data)
          : callToRemoveShowFromWatchlist(data.id);
        break;
      default:
      //Do nothing
    }
  }

  //======================================================================================================================
  if (isVisible === false) {
    return null;
  }
  return (
    <button
      disabled={loading}
      onClick={buttonAction(isAdd, mediaType, data)}
      className={
        "flex items-center  rounded font-semibold px-5 py-4  transition ease-in-out duration-150 " +
        buttonStyle(loading, shouldAdd)
      }
    >
      <svg
        className="bi bi-play-circle-fill fill-onyx-shade"
        xmlns="http://www.w3.org/2000/svg"
        width="20"
        height="20"
        viewBox="0 0 16 16"
      >
        <path d="M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0zM6.79 5.093A.5.5 0 0 0 6 5.5v5a.5.5 0 0 0 .79.407l3.5-2.5a.5.5 0 0 0 0-.814l-3.5-2.5z" />
      </svg>
      <span className="ml-2">{buttonName(loading, shouldAdd)}</span>
    </button>
  );
}
