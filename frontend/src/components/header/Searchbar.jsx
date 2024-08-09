import React, { useEffect, useState } from "react";
import { fetchMediaForSearchbar } from "../../utils/Api";
import { useNavigate } from "react-router-dom";
import SearchSVG from "../helpers/svg/SearchSVG";
import ArrowRightSVG from "../helpers/svg/ArrowRightSVG";

export default function Searchbar({
  searchOption,
  searchOptionsVisible,
  changeSearchOptionsVisibility,
}) {
  const navigate = useNavigate();
  const [query, setQuery] = useState("");
  const [media, setMedia] = useState([]);

  //query value changed
  useEffect(() => {
    const title = query.trim();
    if (title) {
      fetchMediaForSearchbar(1, 10, title)
        .then((res) => {
          setMedia(res.data);
        })
        .catch((err) => {
          console.error(err);
        });
    }
  }, [query]);

  //media array changed
  useEffect(() => {
    //TODO: implement searchbar dropdown menu
  }, [media]);

  //value entered in searchbar
  function onChange(e) {
    setQuery(e.target.value);
  }
  //enter pressed in searchbar
  function onSubmit(e) {
    e.preventDefault();
    var url = `/search?title=${query.trim()}`;
    if (searchOption.genre !== "all") {
      url = url + `&genreId=${searchOption.genre}`;
    }
    if (searchOption.mediaType !== "all") {
      url = url + `&mediaType=${searchOption.mediaType}`;
    }
    navigate(url);
  }

  return (
    <div className="relative w-full m-3 md:m-0">
      <form onSubmit={onSubmit}>
        <input
          type="text"
          className="bg-onyx-contrast text-onyx-tint rounded-full w-full md:w-64 pr-8 pl-8 py-1 focus:outline-none focus:ring-[3px] focus:ring-mellon-primary-default"
          placeholder="Search"
          value={query}
          onChange={onChange}
        />
      </form>
      <SearchSVG
        width="16"
        height="16"
        className="absolute top-2 left-2 fill-onyx-primary-40"
      />
      {/* Search options button */}
      <button
        onClick={changeSearchOptionsVisibility}
        className="absolute top-1.5 right-2.5"
      >
        <ArrowRightSVG
          width="20"
          height="20"
          className={`fill-onyx-primary-40 transform duration-500 ${
            searchOptionsVisible ? "-rotate-[270deg]" : "-rotate-90"
          }`}
        />
      </button>
    </div>
  );
}
