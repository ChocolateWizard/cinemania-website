import React from "react";

import MissingProfileImageLogo from "../../../../assets/MissingProfileImageLogo";
import { concatActorRoleNames } from "../../../../utils/Util";

export default function ActorCard({ actor }) {
  const showProfilePhoto = () => {
    if (actor.profile_photo_url === null) {
      return <MissingProfileImageLogo />;
    }
    return (
      <img
        className="h-movie-cast-profile hover:opacity-75 transition ease-in-out duration-150"
        src={actor.profile_photo_url}
        alt="profile_photo"
      />
    );
  };

  return (
    <div className="mt-8">
      <a href="">{showProfilePhoto()}</a>
      <div className="mt-2">
        <a href="" className="text-lg mt-2 hover:text-gray:300">
          {actor.first_name+" "+actor.last_name}
        </a>
        <div className="flex items-center text-gray-400 text-sm mt-1">
          {concatActorRoleNames(actor.roles,", ",5)}
        </div>
      </div>
    </div>
  );
}
