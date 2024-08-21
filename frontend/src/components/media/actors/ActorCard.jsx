import React from "react";

import { concatActorRoleNames } from "../../../utils/Util";
import PersonSVG from "../../helpers/svg/PersonSVG";

export default function ActorCard({ actor }) {
  return (
    <div className="mt-8">
      <a href="#">
        <ProfilePhoto actor={actor} />
      </a>
      <div className="mt-2">
        <a href="#" className="text-lg mt-2 hover:text-gray-300">
          {actor.first_name + " " + actor.last_name}
        </a>
        <div className="flex items-center text-gray-400 text-sm mt-1">
          {concatActorRoleNames(actor.roles, ", ", 5)}
        </div>
      </div>
    </div>
  );
}

function ProfilePhoto({ actor }) {
  if (!actor.profile_photo_url) {
    return (
      <div className="bg-onyx-tint h-[325px] w-[216px] flex items-center justify-center hover:opacity-75 transition">
        <PersonSVG className="fill-onyx-primary-default" />
      </div>
    );
  }
  return (
    <img
      height={325}
      width={216}
      className="h-[325px] w-[216px] hover:opacity-75 transition"
      src={actor.profile_photo_url}
      alt="Actors profile photo"
    />
  );
}
