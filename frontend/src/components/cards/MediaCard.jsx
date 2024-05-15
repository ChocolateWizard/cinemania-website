import { Link } from "react-router-dom";
import { concatGenreNames } from "../../utils/Util";
import StarSVG from "../helpers/svg/StarSVG";
import NoImageSVG from "../helpers/svg/NoImageSVG";

export default function MediaCard({ data }) {
  const detailsPageURL = getPageURL(data);

  return (
    <div className="mt-8 px-2">
      <Link to={detailsPageURL}>
        <CoverImage url={data.cover_image_url} />
      </Link>
      <div className="mt-2">
        <Link to={detailsPageURL} className="text-lg mt-2 hover:text-gray-300">
          {data.title}
        </Link>
        <div className="flex items-center text-gray-400 text-sm mt-1">
          <StarSVG
            width="16"
            height="16"
            className="fill-mellon-primary-default"
          />
          <span className="ml-1">{data.audience_rating}%</span>
          <span className="mx-2">|</span>
          <span>{data.release_date}</span>
        </div>
        <div className="text-gray-400 text-sm">
          {concatGenreNames(data.genres, ", ")}
        </div>
      </div>
    </div>
  );
}

function CoverImage({ url }) {
  if (url) {
    return (
      <img className="hover:opacity-75 transition" height="350" src={url} />
    );
  }
  return (
    <div className="bg-onyx-tint h-[361px] flex items-center justify-center hover:opacity-75 transition">
      <NoImageSVG className="fill-onyx-primary-default p-10" />
    </div>
  );
}

function getPageURL(data) {
  switch (data.media_type) {
    case "movie":
      return `/movie/${data.id}`;
    case "tv_show":
      return `/show/${data.id}`;
    default:
      return "/page-not-found";
  }
}
