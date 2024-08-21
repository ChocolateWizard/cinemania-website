import React, { useContext, useState } from "react";
import { GlobalContext } from "../../../context/GlobalState";
import Person2SVG from "../../helpers/svg/Person2SVG";

export default function CritiqueAddForm({ mediaId, addUsersCritiqueCallback }) {
  const { sessionData } = useContext(GlobalContext);
  const [rating, setRating] = useState("");
  const [description, setDescription] = useState("");

  function onRatingChange(e) {
    setRating(e.target.value);
  }
  function onDescriptionChange(e) {
    setDescription(e.target.value);
  }

  function handleAddCritique() {
    addUsersCritiqueCallback(mediaId, rating, description);
  }

  return (
    <div className="flex flex-col border-2 bg-onyx-primary-20 border-mellon-primary-default p-4 my-3">
      <div className="flex flex-row justify-between items-center">
        <div className="flex flex-row items-center">
          <UserProfilePicture url={sessionData.user.profileImageUrl} />
          <div className="flex flex-row ml-4">
            <p className="font-bold">{sessionData.user.profileName}</p>
          </div>
        </div>
        <div className="text-2xl font-bold text-blue-400">
          <input
            className="text-2xl rounded w-12 text-right font-bold text-blue-400 bg-onyx-primary-15"
            type="text"
            value={rating}
            onChange={onRatingChange}
          />
          %
        </div>
      </div>
      <input
        className="mt-4 rounded text-wrap break-all bg-onyx-primary-15"
        type="text"
        value={description}
        onChange={onDescriptionChange}
      />

      <div className="mt-5 items-end text-right">
        <button
          onClick={handleAddCritique}
          className="px-2 py-1 items-end text-white text-xl rounded bg-mellon-primary-default "
        >
          Add critique
        </button>
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
