import React, { useContext, useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { GlobalContext } from "../../../context/GlobalState";

import { fetchShowDetails } from "../../../utils/Api";
import CardLoader from "../../helpers/loaders/cardLoader/CardLoader";
import { concatDirectorNames, concatGenreNames } from "../../../utils/Util";
import WatchlistButton from "../../watchlist/WatchlistButton";
import DetailsTabs from "../../tabs/DetailsTabs";
import StarSVG from "../../helpers/svg/StarSVG";
import NoImageSVG from "../../helpers/svg/NoImageSVG";

export default function ShowDetails() {
  const { id } = useParams();
  const [show, setShow] = useState(null);
  const [loadingShow, setLoadingShow] = useState(true);
  const [errorShow, setErrorShow] = useState(null);

  const { sessionData } = useContext(GlobalContext);

  useEffect(() => {
    setLoadingShow(true);
    fetchShowDetails(id)
      .then((response) => {
        response.data.media_type = "tv_show";
        setShow(response.data);
      })
      .catch((err) => {
        console.error(err);
        setErrorShow(err);
      })
      .finally(() => {
        setLoadingShow(false);
      });
  }, []);

  const CreatorsTab = ({ creatorsAsText }) => {
    if (creatorsAsText == null || creatorsAsText === "") {
      return null;
    }
    return (
      <div className="border-t-2 border-onyx-tint">
        <div className="mt-2 mb-2 text-lg">
          <div className="">Creators: {creatorsAsText}</div>
        </div>
      </div>
    );
  };
  const NumberOfSeasonsTab = ({ numberOfSeasons }) => {
    if (numberOfSeasons == null || numberOfSeasons === 0) {
      return null;
    }
    return (
      <div className="border-y-2 border-onyx-tint">
        <div className="mt-2 mb-2 text-lg">
          <div className="">Seasons: {numberOfSeasons}</div>
        </div>
      </div>
    );
  };

  const ShowTabs = ({ creatorsAsText, numberOfSeasons }) => {
    if (
      (creatorsAsText == null || creatorsAsText === "") &&
      (numberOfSeasons == null || numberOfSeasons === 0)
    ) {
      return null;
    }
    return (
      <div className="mt-12">
        <CreatorsTab creatorsAsText={creatorsAsText} />
        <NumberOfSeasonsTab numberOfSeasons={numberOfSeasons} />
      </div>
    );
  };

  //==========================================================================================================
  if (loadingShow) {
    return <CardLoader />;
  }
  if (show === null || errorShow != null) {
    return (
      <h2 className="px-4 mt-5 uppercase tracking-wider text-onyx-primary-30 text-lg font-bold">
        Unable to load show details
      </h2>
    );
  }

  return (
    <>
      <div className="border-b border-onyx-tint">
        <div className="container mx-auto px-4 py-16 flex flex-col md:flex-row">
          <CoverImage url={show.cover_image_url} />
          <div className="md:ml-24">
            <h2 className="text-4xl font-semibold">{show.title}</h2>
            <div className="flex flex-wrap items-center text-gray-400 text-sm mt-2">
              <StarSVG
                width="16"
                height="16"
                className="fill-mellon-primary-default"
              />
              <span className="ml-1">{show.audience_rating}%</span>
              <span className="mx-2">|</span>
              <StarSVG width="16" height="16" className="fill-blue-400" />
              <span className="ml-1">
                {show.critics_rating !== null ? show.critics_rating : "N/A "}%
              </span>
              <span className="mx-2">|</span>
              <span>{show.release_date}</span>
              <span className="mx-2">|</span>
              <span>{concatGenreNames(show.genres, ", ")}</span>
            </div>
            <p className="text-onyx-contrast mt-8">{show.description}</p>
            <ShowTabs
              creatorsAsText={concatDirectorNames(show.directors, ", ", 3)}
              numberOfSeasons={show.number_of_seasons}
            />

            <div className="mt-12">
              <WatchlistButton
                visible={sessionData.isLoggedIn}
                media={show}
                mediaType={"tv_show"}
              />
            </div>
          </div>
        </div>
      </div>

      <div className="border-b border-onyx-tint">
        <DetailsTabs
          id={show.id}
          actors={show.actors}
          critiques={show.critiques}
        />
      </div>
    </>
  );
}

function CoverImage({ url }) {
  if (url) {
    return <img className="w-64 md:w-96" src={url} />;
  }
  return (
    <div className="bg-onyx-tint w-64 md:w-[450px] md:h-[560px] flex items-center justify-center hover:opacity-75 transition">
      <NoImageSVG className="fill-onyx-primary-default p-10" />
    </div>
  );
}
