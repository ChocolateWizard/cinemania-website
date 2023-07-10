import React, { useState } from "react";

import ActorCard from "../card/ActorCard";

export default function CastCollection({ actors }) {
  const initNumberOfActors = 5;
  const [dataState, setDataState] = useState({
    numberOfActorsShown: initNumberOfActors,
    showOption: actors != null && actors.length > 5 ? true : false,
    hideOption: false,
  });

  const showMoreItems = () => {
    setDataState({
      numberOfActorsShown: actors.length,
      showOption: false,
      hideOption: true,
    });
  };

  const hideMoreItems = () => {
    setDataState({
      numberOfActorsShown: initNumberOfActors,
      showOption: true,
      hideOption: false,
    });
  };

  const ShowMoreOption = () => {
    if (dataState.showOption) {
      return (
        <div className="flex justify-center pt-4">
          <button
            onClick={() => showMoreItems()}
            className=" text-mellon-primary hover:underline"
          >
            Show all
          </button>
        </div>
      );
    }
    if (dataState.hideOption) {
      return (
        <div className="flex justify-center pt-4">
          <button
            onClick={() => hideMoreItems()}
            className=" text-mellon-primary hover:underline"
          >
            Hide
          </button>
        </div>
      );
    }
    return null;
  };

  //====================================================================================
  if (actors == null || actors.length === 0) {
    return null;
  }
  return (
    <div className="container mx-auto px-4 py-16">
      <h2 className="text-4xl font-semibold">Cast</h2>
      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-5 gap-8">
        {actors.slice(0, dataState.numberOfActorsShown).map((actor) => (
          <ActorCard actor={actor} key={actor.id} />
        ))}
      </div>
      <ShowMoreOption />
    </div>
  );
}
