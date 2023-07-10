import React, { useContext } from "react";
import { GlobalContext } from "../../context/GlobalState";
import CardGrid from "../cards/grid/CardGrid";
import MediaCard from "../cards/MediaCard";

export default function Watchlist() {
  const { movieWatchlist, showWatchlist } = useContext(GlobalContext);

  const RenderCardsList = ({ dataArray, errorMessage, CardComponent }) => {
    if (dataArray.length === 0) {
      return (
        <h2 className="mt-5 uppercase tracking-wider text-onyx-primary-30 text-lg font-bold">
          {errorMessage}
        </h2>
      );
    }
    return <CardGrid dataArray={dataArray} CardComponent={CardComponent} />;
  };

  return (
    <div className="container mx-auto px-4 pt-16">
      <div>
        <h2 className="uppercase tracking-wider text-mellon-primary text-lg font-semibold">
          Movies to watch
        </h2>
        <RenderCardsList
          dataArray={movieWatchlist}
          errorMessage={"No movies in watchlist"}
          CardComponent={MediaCard}
        />
      </div>
      <div className="py-24">
        <h2 className="uppercase tracking-wider text-mellon-primary text-lg font-semibold">
          Shows to watch
        </h2>
        <RenderCardsList
          dataArray={showWatchlist}
          errorMessage={"No shows in watchlist"}
          CardComponent={MediaCard}
        />
      </div>
    </div>
  );
}
