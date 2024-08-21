import React from "react";
import Person2SVG from "../../../helpers/svg/Person2SVG";
import LikeDislikeBtnGroup from "./LikeDislikeBtnGroup";

export default function CommentCard({
  critiqueId,
  comment,
  isUsersComment,
  onRemoveUsersComment,
  addCommentLikeDislikeCallback,
  removeCommentLikeDislikeCallback,
}) {
  function handleClickDelete() {
    onRemoveUsersComment(critiqueId, comment.id);
  }

  //==========================================================================================================
  return (
    <div
      className={`flex flex-col p-4 my-3 border-2 bg-onyx-primary-20 ${
        isUsersComment
          ? "border-mellon-primary-default"
          : "border-onyx-primary-20"
      }`}
    >
      <div className="flex flex-row justify-between items-center">
        <div className="flex flex-row items-center">
          <UserProfilePicture url={comment.user.profile_image_url} />
          <div className="flex flex-row ml-4">
            <p className="font-bold">{comment.user.profile_name}</p>
            {isUsersComment && (
              <button
                onClick={handleClickDelete}
                className="ml-4 px-2 py-1 bg-red-900 text-white rounded"
              >
                Remove
              </button>
            )}
          </div>
        </div>
      </div>
      <p className="mt-4 text-wrap break-all">{comment.content}</p>
      <div className="flex flex-row justify-between mt-4">
        <LikeDislikeBtnGroup
          critiqueId={critiqueId}
          commentId={comment.id}
          commentLikesNumber={comment.number_of_likes}
          commentDislikesNumber={comment.number_of_dislikes}
          addCommentLikeDislikeCallback={addCommentLikeDislikeCallback}
          removeCommentLikeDislikeCallback={removeCommentLikeDislikeCallback}
        />
        <div className="text-blue-700">{comment.created_at}</div>
      </div>
    </div>
  );
}

function UserProfilePicture({ url }) {
  if (url) {
    return <img className="w-12 h-12 rounded-full" src={url} />;
  }
  return (
    <div className="flex items-center justify-center rounded-full w-12 h-12 bg-onyx-primary-35">
      <Person2SVG className="fill-onyx-primary-50 p-2.5" />
    </div>
  );
}
