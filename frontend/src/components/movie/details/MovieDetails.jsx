import React, { useContext } from "react";
import { GlobalContext } from "../../../context/GlobalState";

import { useParams } from "react-router-dom";
import { fetchMovieDetailsData } from "../../../utils/Api";
import CardLoader from "../../helpers/loaders/cardLoader/CardLoader";
import CastCollection from "../../person/actor/collection/CastCollection";

export default function MovieDetails() {
  const { id } = useParams();
  const { addMovieToWatchlist, removeMovieFromWatchlist, movieWatchlist } =
    useContext(GlobalContext);
  const { data, loading, error } = fetchMovieDetailsData(id);

  let storedMovie = movieWatchlist.find(
    (o) => data.movieData != null && o.id === data.movieData.id
  );

  const watchlistDisabled = storedMovie ? true : false;

  const DirectorsTab = ({ directorsAsText }) => {
    if (directorsAsText == null || directorsAsText === "") {
      return null;
    }
    return (
      <div className="border-t-2 border-onyx-tint">
        <div className="mt-2 mb-2 text-lg">
          <div className="">Directors: {directorsAsText}</div>
        </div>
      </div>
    );
  };
  const WritersTab = ({ writersAsText }) => {
    if (writersAsText == null || writersAsText === "") {
      return null;
    }
    return (
      <div className="border-y-2 border-onyx-tint">
        <div className="mt-2 mb-2 text-lg">
          <div className="">Writers: {writersAsText}</div>
        </div>
      </div>
    );
  };

  const MovieTabs = ({ directorsAsText, writersAsText }) => {
    if (
      (directorsAsText == null || directorsAsText === "") &&
      (writersAsText == null || writersAsText === "")
    ) {
      return null;
    }
    return (
      <div className=" mt-12">
        <DirectorsTab directorsAsText={directorsAsText} />
        <WritersTab writersAsText={writersAsText} />
      </div>
    );
  };

  const ShowButton = ({ disabled }) => {
    return (
      <button
        onClick={
          disabled
            ? () => removeMovieFromWatchlist(data.movieData.id)
            : () => addMovieToWatchlist(data.movieData)
        }
        className={
          "flex items-center  rounded font-semibold px-5 py-4  transition ease-in-out duration-150 " +
          (disabled
            ? "bg-onyx-tint text-onyx-primary-10 hover:bg-onyx-primary-30"
            : "bg-mellon-primary text-onyx-tint hover:bg-mellon-shade")
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
        <span className="ml-2">
          {disabled ? "Remove from watchlist" : "Add to watchlist"}
        </span>
      </button>
    );
  };

  //=======================================================================================
  if (loading.loadingMovieData == true) {
    return <CardLoader />;
  }
  if (data.movieData === null) {
    return (
      <h2 className="px-4 mt-5 uppercase tracking-wider text-onyx-primary-30 text-lg font-bold">
        Unable to load movie details
      </h2>
    );
  }

  return (
    <>
      <div className="border-b border-onyx-tint">
        <div className="container mx-auto px-4 py-16 flex flex-col md:flex-row">
          <img className="w-64 md:w-96" src={data.movieData.coverPath} alt="" />
          <div className="md:ml-24">
            <h2 className="text-4xl font-semibold">{data.movieData.title}</h2>
            <div className="flex flex-wrap items-center text-gray-400 text-sm mt-2">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="16"
                height="16"
                className="bi bi-star-fill fill-mellon-primary"
                viewBox="0 0 16 16"
              >
                <path d="M3.612 15.443c-.386.198-.824-.149-.746-.592l.83-4.73L.173 6.765c-.329-.314-.158-.888.283-.95l4.898-.696L7.538.792c.197-.39.73-.39.927 0l2.184 4.327 4.898.696c.441.062.612.636.282.95l-3.522 3.356.83 4.73c.078.443-.36.79-.746.592L8 13.187l-4.389 2.256z" />
              </svg>
              <span className="ml-1">{data.movieData.rating}%</span>
              <span className="mx-2">|</span>
              <span>{data.movieData.releaseDate}</span>
              <span className="mx-2">|</span>
              <span>{data.movieData.genresAsText}</span>
            </div>
            <p className="text-onyx-contrast mt-8">
              {data.movieData.description}
            </p>
            <MovieTabs
              directorsAsText={data.movieData.directorsAsText}
              writersAsText={data.movieData.writersAsText}
            />

            <div className="mt-12">
              <ShowButton disabled={watchlistDisabled} />
            </div>
          </div>
        </div>
      </div>

      <div className="border-b border-onyx-tint">
        <CastCollection actors={data.movieData.cast} />
      </div>
    </>
  );
}
