import React, { useContext, useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { GlobalContext } from "../../../context/GlobalState";

import { fetchMovieDetails } from "../../../utils/Api";
import CardLoader from "../../helpers/loaders/cardLoader/CardLoader";
import {
  concatDirectorNames,
  concatGenreNames,
  concatWriterNames,
} from "../../../utils/Util";
import DetailsTabs from "../../tabs/DetailsTabs";
import WatchlistButton from "../../watchlist/WatchlistButton";
import StarSVG from "../../helpers/svg/StarSVG";
import NoImageSVG from "../../helpers/svg/NoImageSVG";

export default function MovieDetails() {
  const { id } = useParams();
  const [movie, setMovie] = useState(null);
  const [loadingMovie, setLoadingMovie] = useState(true);
  const [errorMovie, setErrorMovie] = useState(null);

  const { sessionData } = useContext(GlobalContext);

  useEffect(() => {
    setLoadingMovie(true);
    fetchMovieDetails(id)
      .then((response) => {
        response.data.media_type = "movie";
        setMovie(response.data);
      })
      .catch((err) => {
        console.error(err);
        setErrorMovie(err);
      })
      .finally(() => {
        setLoadingMovie(false);
      });
  }, []);

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

  //=======================================================================================
  if (loadingMovie) {
    return <CardLoader />;
  }
  if (movie === null || errorMovie != null) {
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
          <CoverImage url={movie.cover_image_url} />
          <div className="md:ml-24">
            <h2 className="text-4xl font-semibold">{movie.title}</h2>
            <div className="flex flex-wrap items-center text-gray-400 text-sm mt-2">
              <StarSVG
                width="16"
                height="16"
                className="fill-mellon-primary-default"
              />
              <span className="ml-1">{movie.audience_rating}%</span>
              <span className="mx-2">|</span>
              <StarSVG width="16" height="16" className="fill-blue-400" />
              <span className="ml-1">
                {movie.critics_rating !== null ? movie.critics_rating : "N/A "}%
              </span>
              <span className="mx-2">|</span>
              <span>{movie.release_date}</span>
              <span className="mx-2">|</span>
              <span>
                {Math.floor(movie.length / 60)}h {movie.length % 60}m
              </span>
              <span className="mx-2">|</span>
              <span>{concatGenreNames(movie.genres, ", ")}</span>
            </div>
            <p className="text-onyx-contrast mt-8">{movie.description}</p>
            <MovieTabs
              directorsAsText={concatDirectorNames(movie.directors, ", ", 3)}
              writersAsText={concatWriterNames(movie.writers, ", ", 3)}
            />

            <div className="mt-12">
              <WatchlistButton
                visible={sessionData.isLoggedIn}
                media={movie}
                mediaType={"movie"}
              />
            </div>
          </div>
        </div>
      </div>

      <div className="border-b border-onyx-tint">
        <DetailsTabs
          id={movie.id}
          actors={movie.actors}
          critiques={movie.critiques}
        />
      </div>
    </>
  );
}

function CoverImage({ url }) {
  if (url) {
    return <img className="w-64 md:w-96" src={url} />;
  }
  return (
    <div className="bg-onyx-tint w-64 md:w-[450px] md:h-[560px] flex items-center justify-center hover:opacity-75 transition">
      <NoImageSVG className="fill-onyx-primary-default p-10" />
    </div>
  );
}
