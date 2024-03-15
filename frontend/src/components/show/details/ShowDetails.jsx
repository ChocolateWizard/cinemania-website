import React, { useContext, useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { GlobalContext } from "../../../context/GlobalState";

import { fetchShowDetails } from "../../../utils/Api";
import CardLoader from "../../helpers/loaders/cardLoader/CardLoader";
import {
  concatDirectorNames,
  concatGenreNames,
  getCoverImageURL,
} from "../../../utils/Util";
import WatchlistButton from "../../watchlist/WatchlistButton";
import DetailsTabs from "../../tabs/DetailsTabs";

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
          <img
            className="w-64 md:w-96"
            src={getCoverImageURL(show.cover_image_url)}
            alt="cover image"
          />
          <div className="md:ml-24">
            <h2 className="text-4xl font-semibold">{show.title}</h2>
            <div className="flex flex-wrap items-center text-gray-400 text-sm mt-2">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="16"
                height="16"
                className="bi bi-star-fill fill-mellon-primary-default"
                viewBox="0 0 16 16"
              >
                <path d="M3.612 15.443c-.386.198-.824-.149-.746-.592l.83-4.73L.173 6.765c-.329-.314-.158-.888.283-.95l4.898-.696L7.538.792c.197-.39.73-.39.927 0l2.184 4.327 4.898.696c.441.062.612.636.282.95l-3.522 3.356.83 4.73c.078.443-.36.79-.746.592L8 13.187l-4.389 2.256z" />
              </svg>
              <span className="ml-1">{show.audience_rating}%</span>
              <span className="mx-2">|</span>
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="16"
                height="16"
                className="bi bi-star-fill fill-blue-400"
                viewBox="0 0 16 16"
              >
                <path d="M3.612 15.443c-.386.198-.824-.149-.746-.592l.83-4.73L.173 6.765c-.329-.314-.158-.888.283-.95l4.898-.696L7.538.792c.197-.39.73-.39.927 0l2.184 4.327 4.898.696c.441.062.612.636.282.95l-3.522 3.356.83 4.73c.078.443-.36.79-.746.592L8 13.187l-4.389 2.256z" />
              </svg>
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
