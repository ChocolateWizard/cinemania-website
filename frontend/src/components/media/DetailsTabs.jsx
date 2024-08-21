import React, { useState } from "react";
import ActorsSection from "./actors/ActorsSection";
import CritiquesSection from "./critiques/CritiquesSection";

export default function DetailsTabs({
  mediaId,
  actors,
  critiques,
  addCritiqueCallback,
  removeCritiqueCallback,
  addCommentCallback,
  removeCommentCallback,
  addCritiqueLikeDislikeCallback,
  removeCritiqueLikeDislikeCallback,
  addCommentLikeDislikeCallback,
  removeCommentLikeDislikeCallback,
}) {
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
      {tabsShown.cast && <ActorsSection actors={actors} />}
      {tabsShown.critiques && (
        <CritiquesSection
          mediaId={mediaId}
          critiques={critiques}
          addCritiqueCallback={addCritiqueCallback}
          removeCritiqueCallback={removeCritiqueCallback}
          addCommentCallback={addCommentCallback}
          removeCommentCallback={removeCommentCallback}
          addCritiqueLikeDislikeCallback={addCritiqueLikeDislikeCallback}
          removeCritiqueLikeDislikeCallback={removeCritiqueLikeDislikeCallback}
          addCommentLikeDislikeCallback={addCommentLikeDislikeCallback}
          removeCommentLikeDislikeCallback={removeCommentLikeDislikeCallback}
        />
      )}
    </div>
  );
}
