import React, { useContext, useState } from "react";

import { toast } from "react-toastify";
import { GlobalContext } from "../../context/GlobalState";
import {
  postMediaIntoWatchlist,
  deleteMediaFromWatchlist,
} from "../../utils/Api";

export default function WatchlistButton({ visible, media, mediaType }) {
  const {
    movieWatchlist,
    showWatchlist,
    addMovieToWatchlist,
    removeMovieFromWatchlist,
    addShowToWatchlist,
    removeShowFromWatchlist,
  } = useContext(GlobalContext);

  function getIsAddInitialState() {
    if (media) {
      switch (mediaType) {
        case "movie":
          return movieWatchlist.find((wm) => wm.id === media.id) ? false : true;
        case "tv_show":
          return showWatchlist.find((ws) => ws.id === media.id) ? false : true;
        default:
          return false;
      }
    }
    return false;
  }

  const [loading, setLoading] = useState(false);
  const [isAdd, setIsAdd] = useState(() => getIsAddInitialState());

  function callToAddToWatchlist() {
    setLoading(true);
    postMediaIntoWatchlist(media.id)
      .then((response) => {
        switch (mediaType) {
          case "movie":
            addMovieToWatchlist(media);
            setIsAdd(false);
            break;
          case "tv_show":
            addShowToWatchlist(media);
            setIsAdd(false);
            break;
          default:
            console.error("Unknown media type!");
        }
        toast.success("Added to watchlist!");
      })
      .catch((err) => {
        console.error(err);
        toast.error("Unable to add to watchlist!");
      })
      .finally(() => {
        setLoading(false);
      });
  }
  function callToRemoveFromWatchlist() {
    setLoading(true);
    deleteMediaFromWatchlist(media.id)
      .then((response) => {
        switch (mediaType) {
          case "movie":
            removeMovieFromWatchlist(media.id);
            setIsAdd(true);
            break;
          case "tv_show":
            removeShowFromWatchlist(media.id);
            setIsAdd(true);
            break;
          default:
            console.error("Unknown media type!");
        }
        toast.success("Removed from watchlist!");
      })
      .catch((err) => {
        console.error(err);
        toast.error("Unable to remove from watchlist!");
      })
      .finally(() => {
        setLoading(false);
      });
  }

  function buttonName() {
    if (loading) {
      return "Loading...";
    }
    return isAdd ? "Add to watchlist" : "Remove from watchlist";
  }

  function buttonStyle() {
    if (loading) {
      return "bg-onyx-tint text-onyx-primary-10 hover:bg-onyx-primary-30";
    }
    return isAdd
      ? "bg-mellon-primary text-onyx-tint hover:bg-mellon-shade"
      : "bg-onyx-tint text-onyx-primary-10 hover:bg-onyx-primary-30";
  }
  function buttonAction() {
    if (isAdd) {
      callToAddToWatchlist();
    } else {
      callToRemoveFromWatchlist();
    }
  }

  //======================================================================================================================
  if (!visible) {
    return null;
  }
  return (
    <button
      disabled={loading}
      onClick={buttonAction}
      className={
        "flex items-center  rounded font-semibold px-5 py-4  transition ease-in-out duration-150 " +
        buttonStyle()
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
      <span className="ml-2">{buttonName()}</span>
    </button>
  );
}
