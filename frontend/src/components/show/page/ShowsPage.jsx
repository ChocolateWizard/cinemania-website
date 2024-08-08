import React, { useEffect, useState } from "react";

import { fetchShowsForShowsPage } from "../../../utils/Api";

import CardLoader from "../../helpers/loaders/cardLoader/CardLoader";
import CardCarousel from "../../cards/carousel/CardCarousel";
import MediaCard from "../../cards/MediaCard";

const carouselData = [
  {
    id: 1,
    label: "Action & Adventure",
    fetchURL:
      "/api/medias/search?page=1&size=10&genreIds=2" +
      "&sortByReleaseDate=desc&mediaType=tv_show",
  },
  {
    id: 2,
    label: "Animation",
    fetchURL:
      "/api/medias/search?page=1&size=10&genreIds=4" +
      "&sortByReleaseDate=desc&mediaType=tv_show",
  },
  {
    id: 3,
    label: "Comedy",
    fetchURL:
      "/api/medias/search?page=1&size=10&genreIds=5,8" +
      "&sortByReleaseDate=desc&mediaType=tv_show",
  },
  {
    id: 4,
    label: "Kids",
    fetchURL:
      "/api/medias/search?page=1&size=10&genreIds=13" +
      "&sortByReleaseDate=desc&mediaType=tv_show",
  },
];

export default function ShowsPage() {
  return (
    <div className="container mx-auto px-4 pt-16 pb-24 space-y-24">
      {carouselData.map((data) => {
        return (
          <ShowsOfGenre key={data.id} label={data.label} url={data.fetchURL} />
        );
      })}
    </div>
  );
}

function ShowsOfGenre({ label, url }) {
  const [shows, setShows] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchShowsForShowsPage(url)
      .then((response) => {
        if (response.data.length == 0) {
          setError("No shows found");
        } else {
          setShows(response.data);
        }
      })
      .catch((err) => {
        console.error(err);
        setError("Could not load shows");
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
      <ShowsCardsCarousel
        dataArray={shows}
        errorMessage={error}
        loading={loading}
      />
    </div>
  );
}

function ShowsCardsCarousel({ dataArray, errorMessage, loading }) {
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
