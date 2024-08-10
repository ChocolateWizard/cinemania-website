import React, { useContext, useEffect, useState } from "react";
import { fetchMediaDetails } from "../../utils/Api";
import {
  concatDirectorNames,
  concatGenreNames,
  concatWriterNames,
} from "../../utils/Util";
import CardLoader from "../../components/helpers/loaders/cardLoader/CardLoader";
import DetailsTabs from "../../components/tabs/DetailsTabs";
import NoImageSVG from "../../components/helpers/svg/NoImageSVG";
import { useParams } from "react-router-dom";
import { GlobalContext } from "../../context/GlobalState";
import StarSVG from "../../components/helpers/svg/StarSVG";
import WatchlistButton from "../../components/watchlist/WatchlistButton";

export default function MediaDetails({ mediaType }) {
  const { id } = useParams();
  const [media, setMedia] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const { sessionData } = useContext(GlobalContext);

  useEffect(() => {
    fetchMediaDetails(id, mediaType)
      .then((res) => {
        setMedia(res.data);
      })
      .catch((err) => {
        console.error(err);
        setError("Unable to load media details");
      })
      .finally(() => {
        setLoading(false);
      });
  }, []);

  //=======================================================================================
  if (loading) {
    return <CardLoader />;
  }
  if (error) {
    return (
      <h2 className="px-4 mt-5 uppercase tracking-wider text-onyx-primary-30 text-lg font-bold">
        {error}
      </h2>
    );
  }

  return (
    <section>
      <div className="border-b border-onyx-tint">
        <div className="container mx-auto px-4 py-16 flex flex-col md:flex-row">
          <CoverImage url={media.cover_image_url} />
          <div className="md:ml-24">
            <h2 className="text-4xl font-semibold">{media.title}</h2>
            <div className="flex flex-wrap items-center text-gray-400 text-sm mt-2">
              <StarSVG
                width="16"
                height="16"
                className="fill-mellon-primary-default"
              />
              <span className="ml-1">{media.audience_rating}%</span>
              <span className="mx-2">|</span>
              <StarSVG width="16" height="16" className="fill-blue-400" />
              <span className="ml-1">
                {media.critics_rating ? media.critics_rating : "N/A "}%
              </span>
              <span className="mx-2">|</span>
              <span>{media.release_date}</span>
              <span className="mx-2">|</span>
              <MovieLength mediaType={mediaType} media={media} />
              <span>{concatGenreNames(media.genres, ", ")}</span>
            </div>
            <p className="text-onyx-contrast mt-8">{media.description}</p>
            <MediaTabs media={media} mediaType={mediaType} />

            <div className="mt-12">
              <WatchlistButton
                visible={sessionData.isLoggedIn}
                media={media}
                mediaType={mediaType}
              />
            </div>
          </div>
        </div>
      </div>

      <div className="border-b border-onyx-tint">
        <DetailsTabs
          id={media.id}
          actors={media.actors}
          critiques={media.critiques}
        />
      </div>
    </section>
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

function MovieLength({media, mediaType}) {
  return mediaType === "tv_show" ? null : (
    <>
      <span>
        {Math.floor(media.length / 60)}h {media.length % 60}m
      </span>
      <span className="mx-2">|</span>
    </>
  );
}

function MediaTabs({ media, mediaType }) {
  switch (mediaType) {
    case "movie":
      return (
        <MovieTabs
          directors={concatDirectorNames(media.directors, ", ", 3)}
          writers={concatWriterNames(media.writers, ", ", 3)}
        />
      );
    case "tv_show":
      return (
        <TVShowTabs
          creators={concatDirectorNames(media.directors, ", ", 3)}
          numberOfSeasons={media.number_of_seasons}
        />
      );
    default:
      console.err("Unknown media type");
      return null;
  }
}
function MovieTabs({ directors, writers }) {
  if (!directors && !writers) {
    return null;
  }
  return (
    <div className=" mt-12">
      {!directors ? null : (
        <div className="border-t-2 border-onyx-tint">
          <div className="mt-2 mb-2 text-lg">
            <div className="">Directors: {directors}</div>
          </div>
        </div>
      )}
      {!writers ? null : (
        <div className="border-y-2 border-onyx-tint">
          <div className="mt-2 mb-2 text-lg">
            <div className="">Writers: {writers}</div>
          </div>
        </div>
      )}
    </div>
  );
}

function TVShowTabs({ creators, numberOfSeasons }) {
  if (!creators && !numberOfSeasons) {
    return null;
  }
  return (
    <div className=" mt-12">
      {!creators ? null : (
        <div className="border-t-2 border-onyx-tint">
          <div className="mt-2 mb-2 text-lg">
            <div className="">Creators: {creators}</div>
          </div>
        </div>
      )}
      {!numberOfSeasons ? null : (
        <div className="border-y-2 border-onyx-tint">
          <div className="mt-2 mb-2 text-lg">
            <div className="">Seasons: {numberOfSeasons}</div>
          </div>
        </div>
      )}
    </div>
  );
}
