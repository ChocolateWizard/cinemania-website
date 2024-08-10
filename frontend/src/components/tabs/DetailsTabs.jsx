import React, { useState } from "react";
import CastCollection from "../person/actor/collection/CastCollection";
import CritiqueCollection from "../critique/CritiqueCollection";

export default function DetailsTabs({ id, actors, critiques }) {
  const [tabsShown, setTabsShows] = useState({
    cast: true,
    critiques: false,
  });

  function showCastTab() {
    setTabsShows({
      cast: true,
      critiques: false,
    });
  }
  function showCritiquesTab() {
    setTabsShows({
      cast: false,
      critiques: true,
    });
  }

  return (
    <div className="flex flex-col">
      <div className="flex flex-row">
        <button onClick={showCastTab}>
          <h2
            className={
              "text-4xl ml-4 font-semibold hover:text-mellon-primary-default " +
              (tabsShown.cast ? "text-mellon-primary-default" : "")
            }
          >
            Cast
          </h2>
        </button>
        <button onClick={showCritiquesTab}>
          <h2
            className={
              "text-4xl ml-4 font-semibold hover:text-mellon-primary-default " +
              (tabsShown.critiques ? "text-mellon-primary-default" : "")
            }
          >
            Critiques
          </h2>
        </button>
      </div>
      <TabContent
        tabsShown={tabsShown}
        actors={actors}
        critiques={critiques}
        id={id}
      />
    </div>
  );
}

function TabContent({ tabsShown, actors, critiques, id }) {
  if (tabsShown.cast) {
    return <CastCollection actors={actors} />;
  }
  if (tabsShown.critiques) {
    return <CritiqueCollection id={id} critiques={critiques} />;
  }
}
