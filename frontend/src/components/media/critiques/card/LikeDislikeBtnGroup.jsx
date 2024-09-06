import React, { useContext, useState } from "react";
import { GlobalContext } from "../../../../context/GlobalState";
import {
  postCritiqueLike,
  postCritiqueDislike,
  putCritiqueLike,
  putCritiqueDislike,
  deleteCritiqueLike,
  deleteCritiqueDislike,
} from "../../../../utils/Api";
import LikeSVG from "../../../helpers/svg/LikeSVG";
import DislikeSVG from "../../../helpers/svg/DislikeSVG";
import { toast } from "react-toastify";

export default function LikeDislikeBtnGroup({
  critiqueId,
  critiqueLikesNumber,
  critiqueDislikesNumber,
  addCritiqueLikeDislikeCallback,
  removeCritiqueLikeDislikeCallback,
}) {
  const {
    sessionData,
    addCritiqueLikeDislike,
    updateCritiqueLikeDislike,
    removeCritiqueLikeDislike,
    critiquesLikeDislikes,
  } = useContext(GlobalContext);

  const [disabled, setDisabled] = useState(() => {
    //if not logged in or is CRITIC, then disable buttons
    return !sessionData.isLoggedIn || sessionData.user?.role === "CRITIC";
  });
  const [likeDislikeState, setLikeDislikeState] = useState(() => {
    const critique = critiquesLikeDislikes.find(
      (pom) => pom.critique_id === critiqueId
    );
    if (critique) {
      return critique.like
        ? {
            likesNum: critiqueLikesNumber,
            dislikesNum: critiqueDislikesNumber,
            hasLiked: true,
            hasDisliked: false,
          }
        : {
            likesNum: critiqueLikesNumber,
            dislikesNum: critiqueDislikesNumber,
            hasLiked: false,
            hasDisliked: true,
          };
    }
    return {
      likesNum: critiqueLikesNumber,
      dislikesNum: critiqueDislikesNumber,
      hasLiked: false,
      hasDisliked: false,
    }; // Default state if no critique is found
  });

  function onClickLike() {
    setDisabled(true);
    if (!likeDislikeState.hasLiked && !likeDislikeState.hasDisliked) {
      //if neither had liked nor disliked
      postCritiqueLike(critiqueId)
        .then((res) => {
          addCritiqueLikeDislike({
            critique_id: critiqueId,
            like: true,
          });
          setLikeDislikeState({
            ...likeDislikeState,
            likesNum: likeDislikeState.likesNum + 1,
            hasLiked: true,
            hasDisliked: false,
          });
          addCritiqueLikeDislikeCallback(critiqueId, true);
        })
        .catch((err) => {
          console.error(err);
          toast.error("Could not like critique");
        })
        .finally(() => {
          setDisabled(false);
        });
    } else if (likeDislikeState.hasLiked) {
      //if liked but hasn't disliked
      deleteCritiqueLike(critiqueId)
        .then((res) => {
          removeCritiqueLikeDislike(critiqueId);
          setLikeDislikeState({
            ...likeDislikeState,
            likesNum: likeDislikeState.likesNum - 1,
            hasLiked: false,
            hasDisliked: false,
          });
          removeCritiqueLikeDislikeCallback(critiqueId, true);
        })
        .catch((err) => {
          console.error(err);
          toast.error("Could not unlike critique");
        })
        .finally(() => {
          setDisabled(false);
        });
    } else {
      //if disliked, but not liked
      putCritiqueLike(critiqueId)
        .then((res) => {
          updateCritiqueLikeDislike({
            critique_id: critiqueId,
            like: true,
          });
          setLikeDislikeState({
            likesNum: likeDislikeState.likesNum + 1,
            dislikesNum: likeDislikeState.dislikesNum - 1,
            hasLiked: true,
            hasDisliked: false,
          });
          removeCritiqueLikeDislikeCallback(critiqueId, false);
          addCritiqueLikeDislikeCallback(critiqueId, true);
        })
        .catch((err) => {
          console.error(err);
          toast.error("Could not like critique");
        })
        .finally(() => {
          setDisabled(false);
        });
    }
  }

  function onClickDislike() {
    setDisabled(true);
    if (!likeDislikeState.hasLiked && !likeDislikeState.hasDisliked) {
      //if neither had disliked nor liked
      postCritiqueDislike(critiqueId)
        .then((res) => {
          addCritiqueLikeDislike({
            critique_id: critiqueId,
            like: false,
          });
          setLikeDislikeState({
            ...likeDislikeState,
            dislikesNum: likeDislikeState.dislikesNum + 1,
            hasLiked: false,
            hasDisliked: true,
          });
          addCritiqueLikeDislikeCallback(critiqueId, false);
        })
        .catch((err) => {
          console.error(err);
          toast.error("Could not dislike critique");
        })
        .finally(() => {
          setDisabled(false);
        });
    } else if (likeDislikeState.hasDisliked) {
      //if disliked but hasn't liked
      deleteCritiqueDislike(critiqueId)
        .then((res) => {
          removeCritiqueLikeDislike(critiqueId);
          setLikeDislikeState({
            ...likeDislikeState,
            dislikesNum: likeDislikeState.dislikesNum - 1,
            hasLiked: false,
            hasDisliked: false,
          });
          removeCritiqueLikeDislikeCallback(critiqueId, false);
        })
        .catch((err) => {
          console.error(err);
          toast.error("Could not undislike critique");
        })
        .finally(() => {
          setDisabled(false);
        });
    } else {
      //if liked, but not disliked
      putCritiqueDislike(critiqueId)
        .then((res) => {
          updateCritiqueLikeDislike({
            critique_id: critiqueId,
            like: false,
          });
          setLikeDislikeState({
            likesNum: likeDislikeState.likesNum - 1,
            dislikesNum: likeDislikeState.dislikesNum + 1,
            hasLiked: false,
            hasDisliked: true,
          });
          removeCritiqueLikeDislikeCallback(critiqueId, true);
          addCritiqueLikeDislikeCallback(critiqueId, false);
        })
        .catch((err) => {
          console.error(err);
          toast.error("Could not dislike critique");
        })
        .finally(() => {
          setDisabled(false);
        });
    }
  }

  return (
    <div
      className="flex flex-row border-2 rounded-2xl divide-x-2 
      divide-onyx-primary-50 border-onyx-primary-50 bg-onyx-primary-35 w-40"
    >
      <button
        disabled={disabled}
        onClick={onClickLike}
        className={`flex flex-row w-full justify-end ${
          !disabled && "hover:bg-onyx-primary-40"
        } rounded-l-2xl`}
      >
        <span className="text-onyx-contrast text-lg pr-1 pt-[2px]">
          {likeDislikeState.likesNum}
        </span>
        <LikeSVG
          width={30}
          height={30}
          className={`mr-1 ${
            likeDislikeState.hasLiked
              ? "fill-mellon-primary-default"
              : "fill-onyx-primary-50"
          }`}
        />
      </button>
      <button
        onClick={onClickDislike}
        disabled={disabled}
        className={`flex flex-row w-full justify-end ${
          !disabled && "hover:bg-onyx-primary-40"
        } rounded-r-2xl`}
      >
        <span className="text-onyx-contrast  text-lg pr-1 pt-[2px]">
          {likeDislikeState.dislikesNum}
        </span>
        <DislikeSVG
          width={30}
          height={30}
          className={`mr-1 ${
            likeDislikeState.hasDisliked
              ? "fill-mellon-primary-default"
              : "fill-onyx-primary-50"
          }`}
        />
      </button>
    </div>
  );
}
