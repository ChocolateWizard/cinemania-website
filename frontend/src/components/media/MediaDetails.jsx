import React, { useContext, useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { GlobalContext } from "../../context/GlobalState";
import { fetchMediaDetails } from "../../utils/Api";
import {
  concatDirectorNames,
  concatGenreNames,
  concatWriterNames,
} from "../../utils/Util";
import CardLoader from "../helpers/loaders/cardLoader/CardLoader";
import DetailsTabs from "./DetailsTabs";
import NoImageSVG from "../helpers/svg/NoImageSVG";
import StarSVG from "../helpers/svg/StarSVG";
import WatchlistButton from "../watchlist/WatchlistButton";

export default function MediaDetails({ mediaType }) {
  const { id } = useParams();
  const [media, setMedia] = useState(null);
  const [critiques, setCritiques] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const { sessionData } = useContext(GlobalContext);

  useEffect(() => {
    fetchMediaDetails(id, mediaType)
      .then((res) => {
        res.data.media_type = mediaType;
        setMedia(res.data);
        setCritiques(res.data.critiques);
      })
      .catch((err) => {
        console.error(err);
        setError("Unable to load media details");
      })
      .finally(() => {
        setLoading(false);
      });
  }, []);

  function addCritique(newCritique) {
    setCritiques([newCritique, ...critiques]);
  }
  function removeCritique(critiqueId) {
    setCritiques(critiques.filter((c) => c.id !== critiqueId));
  }
  function addComment(critiqueId, newComment) {
    setCritiques((prevCritiques) =>
      prevCritiques.map((critique) =>
        critique.id === critiqueId
          ? {
              ...critique,
              comments: [newComment, ...critique.comments],
            }
          : critique
      )
    );
  }

  function removeComment(critiqueId, commentId) {
    setCritiques((prevCritiques) =>
      prevCritiques.map((critique) =>
        critique.id === critiqueId
          ? {
              ...critique,
              comments: critique.comments.filter(
                (comment) => comment.id !== commentId
              ),
            }
          : critique
      )
    );
  }
  function addCritiqueLikeDislike(critiqueId, isLike) {
    setCritiques((prevCritiques) => {
      return prevCritiques.map((critique) => {
        if (critique.id === critiqueId) {
          // Create a copy of the critique with updated likes/dislikes
          return {
            ...critique,
            number_of_likes: isLike
              ? critique.number_of_likes + 1
              : critique.number_of_likes,
            number_of_dislikes: !isLike
              ? critique.number_of_dislikes + 1
              : critique.number_of_dislikes,
          };
        }
        return critique; // Return unchanged critique if the id doesn't match
      });
    });
  }
  function removeCritiqueLikeDislike(critiqueId, isLike) {
    setCritiques((prevCritiques) => {
      return prevCritiques.map((critique) => {
        if (critique.id === critiqueId) {
          // Create a copy of the critique with updated likes/dislikes
          return {
            ...critique,
            number_of_likes: isLike
              ? critique.number_of_likes - 1
              : critique.number_of_likes,
            number_of_dislikes: !isLike
              ? critique.number_of_dislikes - 1
              : critique.number_of_dislikes,
          };
        }
        return critique; // Return unchanged critique if the id doesn't match
      });
    });
  }
  function addCommentLikeDislike(critiqueId, commentId, isLike) {
    setCritiques((prevCritiques) => {
      return prevCritiques.map((critique) => {
        if (critique.id === critiqueId) {
          // Find and update the specific comment within the critique
          const updatedComments = critique.comments.map((comment) => {
            if (comment.id === commentId) {
              return {
                ...comment,
                number_of_likes: isLike
                  ? comment.number_of_likes + 1
                  : comment.number_of_likes,
                number_of_dislikes: !isLike
                  ? comment.number_of_dislikes + 1
                  : comment.number_of_dislikes,
              };
            }
            return comment; // Return unchanged comment if the id doesn't match
          });

          // Return the updated critique with the updated comments
          return {
            ...critique,
            comments: updatedComments,
          };
        }
        return critique; // Return unchanged critique if the id doesn't match
      });
    });
  }
  function removeCommentLikeDislike(critiqueId, commentId, isLike) {
    setCritiques((prevCritiques) => {
      return prevCritiques.map((critique) => {
        if (critique.id === critiqueId) {
          // Find and update the specific comment within the critique
          const updatedComments = critique.comments.map((comment) => {
            if (comment.id === commentId) {
              return {
                ...comment,
                number_of_likes: isLike
                  ? comment.number_of_likes - 1
                  : comment.number_of_likes,
                number_of_dislikes: !isLike
                  ? comment.number_of_dislikes - 1
                  : comment.number_of_dislikes,
              };
            }
            return comment; // Return unchanged comment if the id doesn't match
          });

          // Return the updated critique with the updated comments
          return {
            ...critique,
            comments: updatedComments,
          };
        }
        return critique; // Return unchanged critique if the id doesn't match
      });
    });
  }

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
                {critiques.length !== 0
                  ? `${
                      critiques.reduce(
                        (sum, critique) => sum + critique.rating,
                        0
                      ) / critiques.length
                    }%`
                  : "N/A"}
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
          mediaId={media.id}
          actors={media.actors}
          critiques={critiques}
          addCritiqueCallback={addCritique}
          removeCritiqueCallback={removeCritique}
          addCommentCallback={addComment}
          removeCommentCallback={removeComment}
          addCritiqueLikeDislikeCallback={addCritiqueLikeDislike}
          removeCritiqueLikeDislikeCallback={removeCritiqueLikeDislike}
          addCommentLikeDislikeCallback={addCommentLikeDislike}
          removeCommentLikeDislikeCallback={removeCommentLikeDislike}
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

function MovieLength({ media, mediaType }) {
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
