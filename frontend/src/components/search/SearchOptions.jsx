import React from "react";
import { Select, Option } from "@material-tailwind/react";

export default function SearchOptions({
  visible,
  setMediaType,
  setGenre,
  options,
  defaultOption,
}) {
  return (
    <div
      className={
        visible
          ? "bg-onyx-contrast pt-4 pb-4 flex flex-row space-x-10 pl-10"
          : "hidden"
      }
    >
      <div>
        <Select
          label="Media type"
          value={defaultOption.mediaType}
          onChange={(val) => setMediaType(val)}
        >
          {options.mediaTypes.map((type, i) => {
            return (
              <Option key={i} value={type.value}>
                {type.label}
              </Option>
            );
          })}
        </Select>
      </div>
      <div>
        <Select
          label="Genre"
          value={defaultOption.genre}
          onChange={(val) => setGenre(val)}
        >
          {options.genres.map((genre, i) => {
            return (
              <Option key={i} value={genre.value}>
                {genre.label}
              </Option>
            );
          })}
        </Select>
      </div>
    </div>
  );
}
