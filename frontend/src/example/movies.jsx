import React from "react";
import MovieCard from "./movieCard";

const showMovieCardsList = (movies, errorMessage) => {
  if (movies.length === 0) {
    return (
      <h2 className="mt-5 uppercase tracking-wider text-onyx-primary-30 text-lg font-bold">
        {errorMessage}
      </h2>
    );
  }
  return (
    <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-5 gap-8">
      {movies.map((movie) => (
        <MovieCard movie={movie} />
      ))}
    </div>
  );
};

const Movies = ({ movies }) => {
  return (
    <div className="container mx-auto px-4 pt-16">
      <div className="popular-movies">
        <h2 className="uppercase tracking-wider text-mellon-primary text-lg font-semibold">
          Popular movies
        </h2>
        {showMovieCardsList(movies, "Could not load popular movies")}
      </div>
      <div className="now-playing-movies py-24">
        <h2 className="uppercase tracking-wider text-mellon-primary text-lg font-semibold">
          Current movies
        </h2>
        {showMovieCardsList(movies, "Could not load current movies")}
      </div>
    </div>
  );
};

export default Movies;
