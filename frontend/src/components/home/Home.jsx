import React, { useEffect, useState } from "react";

import { fetchPopularMovies, fetchPopularShows } from "../../utils/Api";

import CardLoader from "../helpers/loaders/cardLoader/CardLoader";
import CardCarousel from "../cards/carousel/CardCarousel";
import MediaCard from "../cards/MediaCard";

export default function Home() {
  const [movies, setMovies] = useState([]);
  const [shows, setShows] = useState([]);
  const [loadingMovies, setLoadingMovies] = useState(true);
  const [loadingShows, setLoadingShows] = useState(true);
  const [errorMovies, setErrorMovies] = useState(null);
  const [errorShows, setErrorShows] = useState(null);

  useEffect(() => {
    fetchPopularMovies(1, 10)
      .then((response) => {
        if (response.data.length == 0) {
          setErrorMovies("No movies found");
        } else {
          response.data.map((movie) => {
            movie.media_type = "movie";
          });
          setMovies(response.data);
        }
      })
      .catch((err) => {
        console.error(err);
        setErrorMovies("Could not load popular movies");
      })
      .finally(() => {
        setLoadingMovies(false);
      });

    fetchPopularShows(1, 10)
      .then((response) => {
        if (response.data.length == 0) {
          setErrorShows("No tv shows found");
        } else {
          response.data.map((show) => {
            show.media_type = "tv_show";
          });
          setShows(response.data);
        }
      })
      .catch((err) => {
        console.error(err);
        setErrorShows("Could not load popular shows");
      })
      .finally(() => {
        setLoadingShows(false);
      });
  }, []);

  return (
    <div className="container mx-auto px-4 pt-16 pb-24 space-y-24">
      <div>
        <h2 className="uppercase tracking-wider text-mellon-primary-default text-lg font-semibold">
          Popular movies
        </h2>
        <RenderCardsList
          dataArray={movies}
          errorMessage={errorMovies}
          loading={loadingMovies}
        />
      </div>
      <div>
        <h2 className="uppercase tracking-wider text-mellon-primary-default text-lg font-semibold">
          Popular shows
        </h2>
        <RenderCardsList
          dataArray={shows}
          errorMessage={errorShows}
          loading={loadingShows}
        />
      </div>
    </div>
  );
}

function RenderCardsList({ dataArray, errorMessage, loading }) {
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
