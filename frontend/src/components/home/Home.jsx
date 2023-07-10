import React from "react";

import { fetchHomepageData } from "../../utils/Api";

import CardLoader from "../helpers/loaders/cardLoader/CardLoader";
import CardCarousel from "../cards/carousel/CardCarousel";
import MediaCard from "../cards/MediaCard";

export default function Home() {
  const response = fetchHomepageData();

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
        <h2 className="uppercase tracking-wider text-mellon-primary text-lg font-semibold">
          Popular movies
        </h2>
        <RenderCardsList
          dataArray={response.data.movies}
          errorMessage={"Could not load popular movies"}
          loading={response.loading.loadingMovies}
          CardComponent={MediaCard}
        />
      </div>
      <div className="py-24">
        <h2 className="uppercase tracking-wider text-mellon-primary text-lg font-semibold">
          Popular shows
        </h2>
        <RenderCardsList
          dataArray={response.data.shows}
          errorMessage={"Could not load popular shows"}
          loading={response.loading.loadingShows}
          CardComponent={MediaCard}
        />
      </div>
    </div>
  );
}
