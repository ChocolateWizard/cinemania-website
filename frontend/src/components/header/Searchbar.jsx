import React, { useEffect, useState } from "react";
import { isEmptyOrSpaces } from "../../utils/Util";
import { fetchMediaForSearchbar } from "../../utils/Api";
import { useNavigate } from "react-router-dom";

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
      fetchMediaForSearchbar(query, setMedia);
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
        <svg
          className="fill-current w-4 text-gray-500 mt-2 ml-2 bi bi-search"
          xmlns="http://www.w3.org/2000/svg"
          fill="currentColor"
          viewBox="0 0 16 16"
        >
          <path d="M11.742 10.344a6.5 6.5 0 1 0-1.397 1.398h-.001c.03.04.062.078.098.115l3.85 3.85a1 1 0 0 0 1.415-1.414l-3.85-3.85a1.007 1.007 0 0 0-.115-.1zM12 6.5a5.5 5.5 0 1 1-11 0 5.5 5.5 0 0 1 11 0z" />
        </svg>
      </div>
    </div>
  );
}
