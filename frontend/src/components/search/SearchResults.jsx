import React, { useEffect, useState } from "react";
import { useSearchParams } from "react-router-dom";
import CardLoader from "../helpers/loaders/cardLoader/CardLoader";
import { fetchMediaForSearchResults } from "../../utils/Api";
import MediaCard from "../cards/MediaCard";
import CardGrid from "../cards/grid/CardGrid";

export default function SearchResults() {
  const [loading, setLoading] = useState(true);
  const [media, setMedia] = useState([]);
  const title = useSearchParams()[0].get("title");

  useEffect(() => {
    setLoading(true);
    if (title) {
      fetchMediaForSearchResults(1, 30, title)
        .then((res) => {
          setMedia(res.data);
        })
        .catch((err) => {
          console.error(err);
        })
        .finally(() => {
          setLoading(false);
        });
    } else {
      setMedia([]);
      setLoading(false);
    }
  }, [title]);

  if (loading) {
    return <CardLoader />;
  }
  return (
    <section className="container mx-auto px-4 pt-16 pb-6">
      <p className="uppercase tracking-wider text-mellon-primary-default text-lg font-semibold">
        Results for: "
        <span className="normal-case text-onyx-contrast">{title}</span>"
      </p>
      <MediaCardsList
        media={media}
        errorMessage={"No media found"}
        CardComponent={MediaCard}
      />
    </section>
  );
}

function MediaCardsList({ media, errorMessage, CardComponent }) {
  if (media.length === 0) {
    return (
      <p className="mt-5 uppercase tracking-wider text-onyx-primary-30 text-lg font-bold">
        {errorMessage}
      </p>
    );
  }
  return <CardGrid dataArray={media} CardComponent={CardComponent} />;
}
