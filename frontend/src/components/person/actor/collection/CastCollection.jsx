import React, { useState } from "react";

import ActorCard from "../card/ActorCard";

const defaultMaxNumActorsShown = 5;

export default function CastCollection({ actors }) {
  const [dataState, setDataState] = useState({
    maxNumActorsShown: defaultMaxNumActorsShown,
    showOptionVisible: actors?.length > defaultMaxNumActorsShown ? true : false,
    hideOptionVisible: false,
  });

  function showMoreItems() {
    setDataState({
      maxNumActorsShown: actors.length,
      showOptionVisible: false,
      hideOptionVisible: true,
    });
  }

  function hideMoreItems() {
    setDataState({
      maxNumActorsShown: defaultMaxNumActorsShown,
      showOptionVisible: true,
      hideOptionVisible: false,
    });
  }

  if (actors?.length) {
    return (
      <div className="container mx-auto px-4 py-16">
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-5 gap-8">
          {actors.slice(0, dataState.maxNumActorsShown).map((actor) => (
            <ActorCard key={actor.id} actor={actor} />
          ))}
        </div>
        <ShowMoreOption
          showOptionVisible={dataState.showOptionVisible}
          hideOptionVisible={dataState.hideOptionVisible}
          showMoreItems={showMoreItems}
          hideMoreItems={hideMoreItems}
        />
      </div>
    );
  }
}

function ShowMoreOption({
  showOptionVisible,
  hideOptionVisible,
  showMoreItems,
  hideMoreItems,
}) {
  if (showOptionVisible) {
    return (
      <div className="flex justify-center pt-4">
        <button
          onClick={showMoreItems}
          className=" text-mellon-primary-default hover:underline"
        >
          Show all
        </button>
      </div>
    );
  }
  if (hideOptionVisible) {
    return (
      <div className="flex justify-center pt-4">
        <button
          onClick={hideMoreItems}
          className=" text-mellon-primary-default hover:underline"
        >
          Hide
        </button>
      </div>
    );
  }
}
