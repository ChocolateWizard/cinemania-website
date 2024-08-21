import React from "react";
import Person2SVG from "../../../helpers/svg/Person2SVG";
import LikeDislikeBtnGroup from "./LikeDislikeBtnGroup";

export default function CritiqueCard({
  critique,
  isUsersCritique,
  onRemoveUsersCritique,
  addCritiqueLikeDislikeCallback,
  removeCritiqueLikeDislikeCallback,
}) {
  function handleClickDelete() {
    onRemoveUsersCritique(critique);
  }

  //==========================================================================================================
  return (
    <div
      className={`flex flex-col p-4 my-3 border-2 bg-onyx-primary-20 ${
        isUsersCritique
          ? "border-mellon-primary-default"
          : "border-onyx-primary-20"
      }`}
    >
      <div className="flex flex-row justify-between items-center">
        <div className="flex flex-row items-center">
          <UserProfilePicture url={critique.critic.profile_image_url} />
          <div className="flex flex-row ml-4">
            <p className="font-bold">{critique.critic.profile_name}</p>
            {isUsersCritique && (
              <button
                onClick={handleClickDelete}
                className="ml-4 px-2 py-1 bg-red-900 text-white rounded"
              >
                Remove
              </button>
            )}
          </div>
        </div>
        <div className="text-2xl font-bold text-blue-400">
          {critique.rating}%
        </div>
      </div>
      <p className="mt-4 text-wrap break-all">{critique.description}</p>
      <div className="flex flex-row justify-between mt-4">
        <LikeDislikeBtnGroup
          critiqueId={critique.id}
          critiqueLikesNumber={critique.number_of_likes}
          critiqueDislikesNumber={critique.number_of_dislikes}
          addCritiqueLikeDislikeCallback={addCritiqueLikeDislikeCallback}
          removeCritiqueLikeDislikeCallback={removeCritiqueLikeDislikeCallback}
        />
        <div className="text-blue-800">{critique.created_at}</div>
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
