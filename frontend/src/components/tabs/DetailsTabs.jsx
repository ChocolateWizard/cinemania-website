import React, { useState } from "react";
import CastCollection from "../person/actor/collection/CastCollection";
import CritiqueCollection from "../critique/CritiqueCollection";

export default function DetailsTabs({ id, actors, critiques }) {
  const [castShown, setCastShown] = useState(true);
  const [critiquesShown, setCritiquesShown] = useState(false);

  function showCastTab() {
    setCritiquesShown(false);
    setCastShown(true);
  }
  function showCritiquesTab() {
    setCastShown(false);
    setCritiquesShown(true);
  }

  //===========================================================================================================================
  const TabContent = () => {
    if (castShown) {
      return <CastCollection actors={actors} />;
    }
    if (critiquesShown) {
      return <CritiqueCollection id={id} critiques={critiques} />;
    }
  };

  return (
    <div className="flex flex-col">
      <div className="flex flex-row">
        <button onClick={showCastTab}>
          <h2
            className={
              "text-4xl ml-4 font-semibold hover:text-mellon-primary-default " +
              (castShown ? "text-mellon-primary-default" : "")
            }
          >
            Cast
          </h2>
        </button>
        <button onClick={showCritiquesTab}>
          <h2
            className={
              "text-4xl ml-4 font-semibold hover:text-mellon-primary-default " +
              (critiquesShown ? "text-mellon-primary-default" : "")
            }
          >
            Critiques
          </h2>
        </button>
      </div>
      <TabContent />
    </div>
  );
}
