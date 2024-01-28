import React, {  useContext, useState } from "react";

import { toast } from "react-toastify";
import { GlobalContext } from "../../../context/GlobalState";
import { deleteMediaFromWatchlist, postMediaIntoWatchlist } from "../../../utils/Api";

export default function MovieWatchlistButton({ visible, movie }) {
  const { movieWatchlist, addMovieToWatchlist, removeMovieFromWatchlist } = useContext(GlobalContext);

  const [loading, setLoading] = useState(false);
  const [isAdd, setIsAdd] = useState(
    movieWatchlist.find((wm) => movie != null && wm.id === movie.id)!=null
      ? false
      : true
  );

  function callToAddMovieToWatchlist() {
    setLoading(true);
    postMediaIntoWatchlist(movie.id)
      .then((response) => {
        if (response.status == 204) {
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
  function callToRemoveMovieFromWatchlist() {
    setLoading(true);
    deleteMediaFromWatchlist(movie.id)
      .then((response) => {
        if (response.status == 204) {
          removeMovieFromWatchlist(movie.id);
          setIsAdd(true);
          toast.success("Movie removed from watchlist!");
        } else {
          console.error(response.data);
          toast.error("Unable to remove movie from watchlist!");
        }
      })
      .catch((err) => {
        console.error(err);
        toast.error("Unable to remove movie from watchlist!");
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
      callToAddMovieToWatchlist();
    } else {
      callToRemoveMovieFromWatchlist();
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
