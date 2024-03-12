import React, { useEffect,useState } from "react";

import { fetchCurrentShows, fetchPopularShows } from "../../../utils/Api";

import CardLoader from "../../helpers/loaders/cardLoader/CardLoader";
import CardCarousel from "../../cards/carousel/CardCarousel";
import MediaCard from "../../cards/MediaCard";

export default function ShowsPage() {
  const [currentShows, setCurrentShows] = useState([]);
  const [popularShows, setPopularShows] = useState([]);
  const [loadingCurrentShows, setLoadingCurrentShows] = useState(true);
  const [loadingPopularShows, setLoadingPopularShows] = useState(true);
  const [errorCurrentShows, setErrorCurrentShows] = useState(null);
  const [errorPopularShows, setErrorPopularShows] = useState(null);

  useEffect(() => {
    setLoadingCurrentShows(true);
    setLoadingPopularShows(true);

    fetchCurrentShows(1, 10)
      .then((response) => {
        if (response.status == 200) {
          response.data.map((show) => {
            show.media_type = "tv_show";
          });
          setCurrentShows(response.data);
        } else {
          console.log(response.data);
          setErrorCurrentShows(response.data);
        }
      })
      .catch((err) => {
        console.error(err);
        setErrorCurrentShows(err);
      })
      .finally(() => {
        setLoadingCurrentShows(false);
      });

    fetchPopularShows(1, 10)
      .then((response) => {
        if (response.status == 200) {
          response.data.map((show) => {
            show.media_type = "tv_show";
          });
          setPopularShows(response.data);
        } else {
          console.error(response.data);
          setErrorPopularShows(response.data);
        }
      })
      .catch((err) => {
        console.error(err);
        setErrorPopularShows(err);
      })
      .finally(() => {
        setLoadingPopularShows(false);
      });
  }, []);

  const ShowCardsList = ({
    dataArray,
    errorMessage,
    loading,
    CardComponent,
  }) => {
    if (loading == true) {
      return <CardLoader />;
    }
    if (dataArray.length === 0) {
      return (
        <h2 className="mt-5 uppercase tracking-wider text-onyx-primary-30 text-lg font-bold">
          {errorMessage}
        </h2>
      );
    }
    return <CardCarousel dataArray={dataArray} CardComponent={CardComponent} />;
  };

  return (
    <div className="container mx-auto px-4 pt-16">
      <div>
        <h2 className="uppercase tracking-wider text-mellon-primary text-lg font-semibold">
          Current
        </h2>
        <ShowCardsList
          dataArray={currentShows}
          errorMessage={"Could not load current shows"}
          loading={loadingCurrentShows}
          CardComponent={MediaCard}
        />
      </div>
      <div className="py-24">
        <h2 className="uppercase tracking-wider text-mellon-primary text-lg font-semibold">
          Popular
        </h2>
        <ShowCardsList
          dataArray={popularShows}
          errorMessage={"Could not load popular shows"}
          loading={loadingPopularShows}
          CardComponent={MediaCard}
        />
      </div>
    </div>
  );
}
