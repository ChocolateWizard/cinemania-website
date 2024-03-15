import React, { useEffect,useState } from "react";

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
    setLoadingMovies(true);
    setLoadingShows(true);

    //fetch popular movies from api. Method returns promise
    fetchPopularMovies(1, 10)
      .then((response) => {
        if (response.status == 200) {
          response.data.map((movie) => {
            movie.media_type = "movie";
          });
          setMovies(response.data);
        } else {
          console.error(response.data);
          setErrorMovies(response.data);
        }
      })
      .catch((err) => {
        console.error(err);
        setErrorMovies(err);
      })
      .finally(() => {
        setLoadingMovies(false);
      });

    //fetch popular shows from api. Method returns promise
    fetchPopularShows(1, 10)
      .then((response) => {
        if (response.status == 200) {
          response.data.map((show) => {
            show.media_type = "tv_show";
          });
          setShows(response.data);
        } else {
          console.error(response.data);
          setErrorShows(response.data);
        }
      })
      .catch((err) => {
        console.error(err);
        setErrorShows(err);
      })
      .finally(() => {
        setLoadingShows(false);
      });
  }, []);

  const RenderCardsList = ({
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
        <h2 className="uppercase tracking-wider text-mellon-primary-default text-lg font-semibold">
          Popular movies
        </h2>
        <RenderCardsList
          dataArray={movies}
          errorMessage={"Could not load popular movies"}
          loading={loadingMovies}
          CardComponent={MediaCard}
        />
      </div>
      <div className="py-24">
        <h2 className="uppercase tracking-wider text-mellon-primary-default text-lg font-semibold">
          Popular shows
        </h2>
        <RenderCardsList
          dataArray={shows}
          errorMessage={"Could not load popular shows"}
          loading={loadingShows}
          CardComponent={MediaCard}
        />
      </div>
    </div>
  );
}
