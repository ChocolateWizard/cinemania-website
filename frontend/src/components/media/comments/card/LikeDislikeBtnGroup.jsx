import React, { useContext, useState } from "react";
import LikeSVG from "../../../helpers/svg/LikeSVG";
import DislikeSVG from "../../../helpers/svg/DislikeSVG";
import { GlobalContext } from "../../../../context/GlobalState";
import {
  deleteCommentDislike,
  deleteCommentLike,
  postCommentDislike,
  postCommentLike,
  putCommentDislike,
  putCommentLike,
} from "../../../../utils/Api";
import { toast } from "react-toastify";

export default function LikeDislikeBtnGroup({
  critiqueId,
  commentId,
  commentLikesNumber,
  commentDislikesNumber,
  addCommentLikeDislikeCallback,
  removeCommentLikeDislikeCallback,
}) {
  const {
    sessionData,
    addCommentLikeDislike,
    updateCommentLikeDislike,
    removeCommentLikeDislike,
    commentsLikeDislikes,
  } = useContext(GlobalContext);

  const [disabled, setDisabled] = useState(() => {
    //if not logged in or is CRITIC, then disable buttons
    return !sessionData.isLoggedIn || sessionData.user?.role === "CRITIC";
  });
  const [likeDislikeState, setLikeDislikeState] = useState(() => {
    const comment = commentsLikeDislikes.find(
      (pom) => pom.comment_id === commentId
    );
    if (comment) {
      return comment.like
        ? {
            likesNum: commentLikesNumber,
            dislikesNum: commentDislikesNumber,
            hasLiked: true,
            hasDisliked: false,
          }
        : {
            likesNum: commentLikesNumber,
            dislikesNum: commentDislikesNumber,
            hasLiked: false,
            hasDisliked: true,
          };
    }
    return {
      likesNum: commentLikesNumber,
      dislikesNum: commentDislikesNumber,
      hasLiked: false,
      hasDisliked: false,
    }; // Default state if no critique is found
  });

  function onClickLike() {
    setDisabled(true);
    if (!likeDislikeState.hasLiked && !likeDislikeState.hasDisliked) {
      //if neither had liked nor disliked
      postCommentLike(critiqueId, commentId)
        .then((res) => {
          addCommentLikeDislike({
            comment_id: commentId,
            like: true,
          });
          setLikeDislikeState({
            ...likeDislikeState,
            likesNum: likeDislikeState.likesNum + 1,
            hasLiked: true,
            hasDisliked: false,
          });
          addCommentLikeDislikeCallback(critiqueId, commentId, true);
        })
        .catch((err) => {
          console.error(err);
          toast.error("Could not like comment");
        })
        .finally(() => {
          setDisabled(false);
        });
    } else if (likeDislikeState.hasLiked) {
      //if liked but hasn't disliked
      deleteCommentLike(critiqueId, commentId)
        .then((res) => {
          removeCommentLikeDislike(commentId);
          setLikeDislikeState({
            ...likeDislikeState,
            likesNum: likeDislikeState.likesNum - 1,
            hasLiked: false,
            hasDisliked: false,
          });
          removeCommentLikeDislikeCallback(critiqueId, commentId, true);
        })
        .catch((err) => {
          console.error(err);
          toast.error("Could not unlike comment");
        })
        .finally(() => {
          setDisabled(false);
        });
    } else {
      //if disliked, but not liked
      putCommentLike(critiqueId, commentId)
        .then((res) => {
          updateCommentLikeDislike({
            comment_id: commentId,
            like: true,
          });
          setLikeDislikeState({
            likesNum: likeDislikeState.likesNum + 1,
            dislikesNum: likeDislikeState.dislikesNum - 1,
            hasLiked: true,
            hasDisliked: false,
          });
          removeCommentLikeDislikeCallback(critiqueId, commentId, false);
          addCommentLikeDislikeCallback(critiqueId, commentId, true);
        })
        .catch((err) => {
          console.error(err);
          toast.error("Could not like comment");
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
      postCommentDislike(critiqueId, commentId)
        .then((res) => {
          addCommentLikeDislike({
            comment_id: commentId,
            like: false,
          });
          setLikeDislikeState({
            ...likeDislikeState,
            dislikesNum: likeDislikeState.dislikesNum + 1,
            hasLiked: false,
            hasDisliked: true,
          });
          addCommentLikeDislikeCallback(critiqueId, commentId, false);
        })
        .catch((err) => {
          console.error(err);
          toast.error("Could not dislike comment");
        })
        .finally(() => {
          setDisabled(false);
        });
    } else if (likeDislikeState.hasDisliked) {
      //if disliked but hasn't liked
      deleteCommentDislike(critiqueId, commentId)
        .then((res) => {
          removeCommentLikeDislike(commentId);
          setLikeDislikeState({
            ...likeDislikeState,
            dislikesNum: likeDislikeState.dislikesNum - 1,
            hasLiked: false,
            hasDisliked: false,
          });
          removeCommentLikeDislikeCallback(critiqueId, commentId, false);
        })
        .catch((err) => {
          console.error(err);
          toast.error("Could not undislike comment");
        })
        .finally(() => {
          setDisabled(false);
        });
    } else {
      //if liked, but not disliked
      putCommentDislike(critiqueId, commentId)
        .then((res) => {
          updateCommentLikeDislike({
            comment_id: commentId,
            like: false,
          });
          setLikeDislikeState({
            likesNum: likeDislikeState.likesNum - 1,
            dislikesNum: likeDislikeState.dislikesNum + 1,
            hasLiked: false,
            hasDisliked: true,
          });
          removeCommentLikeDislikeCallback(critiqueId, commentId, true);
          addCommentLikeDislikeCallback(critiqueId, commentId, false);
        })
        .catch((err) => {
          console.error(err);
          toast.error("Could not dislike comment");
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
