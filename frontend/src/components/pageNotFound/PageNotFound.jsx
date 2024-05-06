import React from "react";
import PageNotFoundSVG from "../helpers/svg/PageNotFoundSVG";

export default function PageNotFound() {
  return (
    <div className="flex flex-col justify-center items-center md:mt-20">
      <PageNotFoundSVG />
      <h1 className="font-bold text-4xl text-onyx-tint">PAGE NOT FOUND</h1>
    </div>
  );
}
