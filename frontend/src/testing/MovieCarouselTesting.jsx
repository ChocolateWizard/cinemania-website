import React from "react";
import MovieCardTest2 from "./MovieCardTest2";

import Carousel from "react-multi-carousel";
import "react-multi-carousel/lib/styles.css";
import "./testing.css";

export default function MovieCarouselTesting({ movies }) {
  const responsive = {
    superLargeDesktop: {
      // the naming can be any, depends on you.
      breakpoint: { max: 4000, min: 3000 },
      items: 5,
    },
    desktop: {
      breakpoint: { max: 3000, min: 1024 },
      items: 5,
    },
    tablet: {
      breakpoint: { max: 1024, min: 464 },
      items: 3,
    },
    mobile: {
      breakpoint: { max: 464, min: 0 },
      items: 1,
    },
  };

  const CustomLeftArrow = ({ onClick, ...rest }) => {
    const {
      onMove,
      carouselState: { currentSlide, deviceType },
    } = rest;
    // onMove means if dragging or swiping in progress.
    return (
      <button
        onClick={() => onClick()}
        className=" w-[60px] h-[60px] rounded-full bg-onyx-contrast opacity-20 border-2 absolute top-1/2 transform -translate-y-1/2  -left-[30px] text-onyx-primary-0"
      >
        prev
      </button>
    );
  };

  const CustomRightArrow = ({ onClick, ...rest }) => {
    const {
      onMove,
      carouselState: { currentSlide, deviceType },
    } = rest;
    // onMove means if dragging or swiping in progress.
    return (
      <button
        onClick={() => onClick()}
        className=" w-[60px] h-[60px] rounded-full border-mellon-primary border-2 absolute top-1/2 transform -translate-y-1/2  -right-[10px]"
      >
        next
      </button>
    );
  };

  //   const ButtonGroup = ({ next, previous, goToSlide, ...rest }) => {
  //     const {
  //       carouselState: { currentSlide },
  //     } = rest;
  //     if(currentSlide === 0){
  //         return (
  //             <div className="carousel-button-group">
  //               <button className="invisible w-[60px] h-[60px] rounded-full bg-onyx-contrast opacity-20 border-2 absolute top-1/2 transform -translate-y-1/2  -left-[0px] text-onyx-primary-0" onClick={() => previous()}>Prev</button>
  //               <button className="w-[60px] h-[60px] rounded-full border-mellon-primary border-2 absolute top-1/2 transform -translate-y-1/2  -right-[0px]" onClick={() => next()}>Next</button>
  //             </div>
  //           );
  //     }
  //     return (
  //       <div className="carousel-button-group">
  //         <button className="w-[60px] h-[60px] rounded-full bg-onyx-contrast opacity-20 border-2 absolute top-1/2 transform -translate-y-1/2  -left-[0px] text-onyx-primary-0" onClick={() => previous()}>Prev</button>
  //         <button className="w-[60px] h-[60px] rounded-full border-mellon-primary border-2 absolute top-1/2 transform -translate-y-1/2  -right-[0px]" onClick={() => next()}>Next</button>
  //       </div>
  //     );
  //   };

  const ButtonGroup = ({ next, previous, goToSlide, ...rest }) => {
    const {
      carouselState: { currentSlide },
    } = rest;
    if (currentSlide === 0) {
      return (
        <div className="carousel-button-group">
          <button
            className="invisible w-[60px] h-[60px] rounded-full bg-onyx-contrast opacity-20 border-2 absolute top-1/2 transform -translate-y-1/2  -left-[0px] text-onyx-primary-0"
            onClick={() => previous()}
          >
            Prev
          </button>
          <button
            className="w-[60px] h-[60px] rounded-full border-mellon-primary border-2 absolute top-1/2 transform -translate-y-1/2  -right-[0px]"
            onClick={() => next()}
          >
            Next
          </button>
        </div>
      );
    }
    return (
      <div className="carousel-button-group">
        <button
          className="w-[60px] h-[60px] rounded-full bg-onyx-contrast opacity-20 border-2 absolute top-1/2 transform -translate-y-1/2  -left-[0px] text-onyx-primary-0"
          onClick={() => previous()}
        >
          Prev
        </button>
        <button
          className="w-[60px] h-[60px] rounded-full border-mellon-primary border-2 absolute top-1/2 transform -translate-y-1/2  -right-[0px]"
          onClick={() => next()}
        >
          Next
        </button>
      </div>
    );
  };

  return (
    <div className="mt-8 ">
      <Carousel
        responsive={responsive}
        renderDotsOutside={false}
        partialVisible={false}
      >
        {movies.map((movie) => (
          <MovieCardTest2 movie={movie} />
        ))}
      </Carousel>
    </div>
  );
}
