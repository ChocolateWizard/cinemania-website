import React from "react";
import MovieCardTest from "./movieCardTest";




const showMovieCardsList = (movies, errorMessage) => {
  if (movies.length === 0) {
    return (
      <h2 className="mt-5 uppercase tracking-wider text-onyx-primary-30 text-lg font-bold">
        {errorMessage}
      </h2>
    );
  }
  return (
    //carousel-container
    <div className="mt-8 relative">
      {/* track */}
      <div className="flex overflow-hidden gap-4">
        {movies.map((movie) => (
          <MovieCardTest movie={movie} />
        ))}
      </div>
      <div className="nav">
          <button className=" w-[60px] h-[60px] rounded-full bg-onyx-contrast opacity-20 border-2 absolute top-1/2 transform -translate-y-1/2  -left-[30px] text-onyx-primary-0">prev</button>
          <button className=" w-[60px] h-[60px] rounded-full border-mellon-primary border-2 absolute top-1/2 transform -translate-y-1/2  -right-[30px]">next</button>
      </div>
    </div>
  );
};

const MoviesTest = ({ movies }) => {


  return (
    <div className="container mx-auto px-4 pt-16">
      <div className="popular-movies">
        <h2 className="uppercase tracking-wider text-mellon-primary text-lg font-semibold">
          Popular movies
        </h2>
        {showMovieCardsList(movies, "Could not load popular movies")}
      </div>     
    </div>
  );
};

export default MoviesTest;
