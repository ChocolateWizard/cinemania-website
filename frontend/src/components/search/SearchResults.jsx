import React, { useContext, useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import CardLoader from "../helpers/loaders/cardLoader/CardLoader";
import { fetchMediaForSearchResults } from "../../utils/Api";
import MediaCard from "../cards/MediaCard";
import CardGrid from "../cards/grid/CardGrid";

export default function SearchResults() {
  const [loading, setLoading] = useState(true);
  const { title } = useParams();
  const [media, setMedia] = useState([]);
  

  useEffect(() => {
    fetchMediaForSearchResults(title, setMedia, setLoading);
  }, []);

  const RenderCardsList = ({ dataArray, errorMessage, CardComponent }) => {
    if (dataArray.length === 0) {
      return (
        <h2 className="mt-5 uppercase tracking-wider text-onyx-primary-30 text-lg font-bold">
          {errorMessage}
        </h2>
      );
    }
    return <CardGrid dataArray={dataArray} CardComponent={CardComponent} />;
  };
  //=======================================================================================================
  if (loading == true) {
    return <CardLoader />;
  }

  return (
    <div className="container mx-auto px-4 pt-16">
      <div>
        <h2 className="uppercase tracking-wider text-mellon-primary text-lg font-semibold">
          Results for: {title}
        </h2>
        <RenderCardsList
          dataArray={media}
          errorMessage={"No media found"}
          CardComponent={MediaCard}
        />
      </div>
    </div>
  );
}
