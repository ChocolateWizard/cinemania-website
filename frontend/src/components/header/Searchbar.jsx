import React, { useEffect, useState } from "react";
import { isEmptyOrSpaces } from "../../utils/Util";
import { fetchMediaForSearchbar } from "../../utils/Api";
import { useNavigate } from "react-router-dom";
import SearchSVG from "../helpers/svg/SearchSVG";

export default function Searchbar() {
  const navigate = useNavigate();
  const [query, setQuery] = useState("");
  const [media, setMedia] = useState([]);

  //value entered in searchbar
  const onChange = (e) => {
    e.preventDefault();
    setQuery(e.target.value);
  };
  //title value changed
  useEffect(() => {
    if (!isEmptyOrSpaces(query)) {
      fetchMediaForSearchbar(1, 10, query)
        .then((response) => {
          if (response.status == 200) {
            setMedia(response.data);
          } else {
            console.error(response.data);
          }
        })
        .catch((err) => {
          console.error(err);
        });
    }
  }, [query]);

  //TODO: implement searchbar dropdown menu
  //media array changed
  useEffect(() => {
    if (media.length !== 0) {
      console.log(media);
    } else {
      console.log("No results");
    }
  }, [media]);

  //enter pressed in searchbar
  const onSubmit = (e) => {
    if (isEmptyOrSpaces(query)) {
      e.preventDefault();
    } else {
      navigate(`/search/${query}`);
    }
  };

  return (
    <div className="relative mt-3 md:mt-0">
      <form onSubmit={onSubmit}>
        <input
          type="text"
          className="bg-onyx-contrast text-onyx-tint rounded-full w-64 px-4 pl-8 py-1"
          placeholder="Search"
          value={query}
          onChange={onChange}
        />
      </form>

      <div className="absolute top-0">
        <SearchSVG className="fill-current w-4 text-gray-500 mt-2 ml-2" />
      </div>
    </div>
  );
}
