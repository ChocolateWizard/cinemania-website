import React, { useEffect, useState } from "react";

import { fetchMoviesForMoviesPage } from "../../../utils/Api";

import CardLoader from "../../helpers/loaders/cardLoader/CardLoader";
import CardCarousel from "../../cards/carousel/CardCarousel";
import MediaCard from "../../cards/MediaCard";

const carouselData = [
  {
    id: 1,
    label: "Action",
    fetchURL:
      "/api/medias/search?page=1&size=10&genreIds=1,8" +
      "&sortByReleaseDate=desc&mediaType=movie",
  },
  {
    id: 2,
    label: "Animation",
    fetchURL:
      "/api/medias/search?page=1&size=10&genreIds=4" +
      "&sortByReleaseDate=desc&mediaType=movie",
  },
  {
    id: 3,
    label: "Comedy",
    fetchURL:
      "/api/medias/search?page=1&size=10&genreIds=1,3,5" +
      "&sortByReleaseDate=desc&mediaType=movie",
  },
  {
    id: 4,
    label: "Horror",
    fetchURL:
      "/api/medias/search?page=1&size=10&genreIds=12" +
      "&sortByReleaseDate=desc&mediaType=movie",
  },
];

export default function MoviesPage() {
  return (
    <div className="container mx-auto px-4 pt-16 pb-24 space-y-24">
      {carouselData.map((data) => {
        return (
          <MoviesOfGenre key={data.id} label={data.label} url={data.fetchURL} />
        );
      })}
    </div>
  );
}

function MoviesOfGenre({ label, url }) {
  const [movies, setMovies] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchMoviesForMoviesPage(url)
      .then((response) => {
        if (response.data.length == 0) {
          setError("No movies found");
        } else {
          setMovies(response.data);
        }
      })
      .catch((err) => {
        console.error(err);
        setError("Could not load movies");
      })
      .finally(() => {
        setLoading(false);
      });
  }, []);

  return (
    <div>
      <h2 className="uppercase tracking-wider text-mellon-primary-default text-lg font-semibold">
        {label}
      </h2>
      <MoviesCardsCarousel
        dataArray={movies}
        errorMessage={error}
        loading={loading}
      />
    </div>
  );
}

function MoviesCardsCarousel({ dataArray, errorMessage, loading }) {
  if (loading == true) {
    return <CardLoader />;
  }
  if (errorMessage != null) {
    return (
      <h2 className="mt-5 uppercase tracking-wider text-onyx-primary-30 text-lg font-bold">
        {errorMessage}
      </h2>
    );
  }
  return <CardCarousel dataArray={dataArray} CardComponent={MediaCard} />;
}
