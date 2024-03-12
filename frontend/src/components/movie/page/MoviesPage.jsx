import React, { useEffect, useState } from "react";

import { fetchCurrentMovies, fetchPopularMovies } from "../../../utils/Api";

import CardLoader from "../../helpers/loaders/cardLoader/CardLoader";
import CardCarousel from "../../cards/carousel/CardCarousel";
import MediaCard from "../../cards/MediaCard";

export default function MoviesPage() {
  const [currentMovies, setCurrentMovies] = useState([]);
  const [popularMovies, setPopularMovies] = useState([]);
  const [loadingCurrentMovies, setLoadingCurrentMovies] = useState(true);
  const [loadingPopularMovies, setLoadingPopularMovies] = useState(true);
  const [errorCurrentMovies, setErrorCurrentMovies] = useState(null);
  const [errorPopularMovies, setErrorPopularMovies] = useState(null);

  useEffect(() => {
    setLoadingCurrentMovies(true);
    setLoadingPopularMovies(true);

    fetchCurrentMovies(1, 10)
      .then((response) => {
        if (response.status == 200) {
          response.data.map((movie) => {
            movie.media_type = "movie";
          });        
          setCurrentMovies(response.data);
        } else {
          console.error(response.data);
          setErrorCurrentMovies(response.data);
        }
      })
      .catch((err) => {
        console.error(err);
        setErrorCurrentMovies(err);
      })
      .finally(() => {
        setLoadingCurrentMovies(false);
      });
    fetchPopularMovies(1, 10)
      .then((response) => {
        if (response.status == 200) {
          response.data.map((movie) => {
            movie.media_type = "movie";
          });
          setPopularMovies(response.data);
        } else {
          console.error(response.data);
          setErrorPopularMovies(response.data);
        }
      })
      .catch((err) => {
        console.error(err);
        setErrorPopularMovies(err);
      })
      .finally(() => {
        setLoadingPopularMovies(false);
      });
  }, []);

  const ShowCardsList = ({
    dataArray,
    errorMessage,
    loading,
    CardComponent,
  }) => {
    if (loading == true) {
      return <CardLoader />;
    }
    if (dataArray.length === 0) {
      return (
        <h2 className="mt-5 uppercase tracking-wider text-onyx-primary-30 text-lg font-bold">
          {errorMessage}
        </h2>
      );
    }
    return <CardCarousel dataArray={dataArray} CardComponent={CardComponent} />;
  };

  return (
    <div className="container mx-auto px-4 pt-16">
      <div>
        <h2 className="uppercase tracking-wider text-mellon-primary text-lg font-semibold">
          Current
        </h2>
        <ShowCardsList
          dataArray={currentMovies}
          errorMessage={"Could not load current movies"}
          loading={loadingCurrentMovies}
          CardComponent={MediaCard}
        />
      </div>
      <div className="py-24">
        <h2 className="uppercase tracking-wider text-mellon-primary text-lg font-semibold">
          Popular
        </h2>
        <ShowCardsList
          dataArray={popularMovies}
          errorMessage={"Could not load popular movies"}
          loading={loadingPopularMovies}
          CardComponent={MediaCard}
        />
      </div>
    </div>
  );
}
