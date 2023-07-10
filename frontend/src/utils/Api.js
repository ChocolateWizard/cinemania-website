import axios from "axios";
import { useEffect, useState } from "react";

const BASE_URL = "" + import.meta.env.VITE_API_URL;
const TOKEN = "" + import.meta.env.VITE_API_TOKEN;

const headers = {
  Authorization: "bearer " + TOKEN,
};

function formatForeignAPI(data, intendedComponent) {
  switch (data.mediaType) {
    case "movie":
      data.coverPath =
        data.poster_path == null
          ? "https://picsum.photos/id/237/200/300"
          : "https://image.tmdb.org/t/p/original" + data.poster_path;
      data.rating = data.vote_average;
      data.releaseDate = data.release_date;
      data.description = data.overview;
      break;
    case "show":
      data.coverPath =
        data.poster_path === null
          ? "https://picsum.photos/id/237/200/300"
          : "https://image.tmdb.org/t/p/original" + data.poster_path;
      data.title = data.name;
      data.rating = data.vote_average;
      data.releaseDate = data.first_air_date;
      data.description = data.overview;
      if (intendedComponent == "ShowDetails") {
        data.numberOfSeasons = data.number_of_seasons;
      }
      break;
    default:
  }
}

export function fetchHomepageData() {
  const [movies, setMovies] = useState([]);
  const [shows, setShows] = useState([]);
  const [loadingMovies, setLoadingMovies] = useState(true);
  const [loadingShows, setLoadingShows] = useState(true);
  const [errorMovies, setErrorMovies] = useState(null);
  const [errorShows, setErrorShows] = useState(null);

  useEffect(() => {
    setLoadingMovies(true);
    setLoadingShows(true);
    axios
      .get(BASE_URL + "/movie/popular", {
        headers,
      })
      .then((response1) => {
        //Movies returned successfully
        return axios
          .get(BASE_URL + "/genre/movie/list", {
            headers,
          })
          .then((response2) => {
            //Movie genres returned successfully
            response1.data.results.map((movie) => {
              movie.genres = [];
              movie.genresAsText = "";
              movie.mediaType = "movie";
              for (let i = 0; i < movie.genre_ids.length; i++) {
                for (let j = 0; j < response2.data.genres.length; j++) {
                  if (movie.genre_ids[i] === response2.data.genres[j].id) {
                    movie.genres.push(response2.data.genres[j]);
                    movie.genresAsText =
                      movie.genresAsText + ", " + response2.data.genres[j].name;
                    break;
                  }
                }
              }
              movie.genresAsText = movie.genresAsText.slice(2);
              formatForeignAPI(movie, "Home");
            });
            setMovies(response1.data.results);
          });
      })
      .catch((err) => {
        setErrorMovies(err);
      })
      .finally(() => {
        setLoadingMovies(false);
      });
    //======================================
    axios
      .get(BASE_URL + "/tv/top_rated", {
        headers,
      })
      .then((response1) => {
        return axios
          .get(BASE_URL + "/genre/tv/list", {
            headers,
          })
          .then((response2) => {
            response1.data.results.map((show) => {
              show.genres = [];
              show.genresAsText = "";
              show.mediaType = "show";
              for (let i = 0; i < show.genre_ids.length; i++) {
                for (let j = 0; j < response2.data.genres.length; j++) {
                  if (show.genre_ids[i] === response2.data.genres[j].id) {
                    show.genres.push(response2.data.genres[j]);
                    show.genresAsText =
                      show.genresAsText + ", " + response2.data.genres[j].name;
                    break;
                  }
                }
              }
              show.genresAsText = show.genresAsText.slice(2);
              formatForeignAPI(show, "Home");
            });
            setShows(response1.data.results);
          });
      })
      .catch((err) => {
        setErrorShows(err);
      })
      .finally(() => {
        setLoadingShows(false);
      });
  }, []);

  return {
    data: { movies: movies, shows: shows },
    loading: { loadingMovies: loadingMovies, loadingShows: loadingShows },
    error: { errorMovies: errorMovies, errorShows: errorShows },
  };
}

export function fetchMovieDetailsData(id) {
  //Need: Movie details, directors,actors and stars and their roles
  const [movieData, setMovieData] = useState(null);
  const [loadingMovieData, setLoadingMovieData] = useState(true);
  const [errorMovieData, setErrorMovieData] = useState(null);

  useEffect(() => {
    setLoadingMovieData(true);
    axios
      .get(BASE_URL + `/movie/${id}`, {
        headers,
      })
      .then((details) => {
        //Movie details returned
        return axios
          .get(BASE_URL + `/movie/${id}/credits`, {
            headers,
          })
          .then((credits) => {
            //Movie credits returned
            details.data.mediaType = "movie";
            details.data.genresAsText = "";
            for (let i = 0; i < details.data.genres.length; i++) {
              details.data.genresAsText =
                details.data.genresAsText + ", " + details.data.genres[i].name;
            }
            details.data.genresAsText = details.data.genresAsText.slice(2);
            //Array of genres converted to string of names
            details.data.directorsAsText = "";
            details.data.writersAsText = "";
            for (let i = 0; i < credits.data.crew.length; i++) {
              if (
                credits.data.crew[i].department === "Directing" &&
                credits.data.crew[i].job.includes("Director")
              ) {
                details.data.directorsAsText =
                  details.data.directorsAsText +
                  ", " +
                  credits.data.crew[i].name;
              }
              if (credits.data.crew[i].department === "Writing") {
                details.data.writersAsText =
                  details.data.writersAsText + ", " + credits.data.crew[i].name;
              }
            }
            details.data.directorsAsText =
              details.data.directorsAsText.slice(2);
            details.data.writersAsText = details.data.writersAsText.slice(2);
            details.data.cast = credits.data.cast;
            formatForeignAPI(details.data, "MovieDetails");

            //Array of directors and writers converted to string of names
            setMovieData(details.data);
          });
      })
      .catch((err) => {
        setErrorMovieData(err);
      })
      .finally(() => {
        setLoadingMovieData(false);
      });
  }, []);

  return {
    data: { movieData: movieData },
    loading: { loadingMovieData: loadingMovieData },
    error: { errorMovieData: errorMovieData },
  };
}

export function fetchShowDetailsData(id) {
  const [showData, setShowData] = useState(null);
  const [loadingShowData, setLoadingShowData] = useState(true);
  const [errorShowData, setErrorShowData] = useState(null);

  useEffect(() => {
    setLoadingShowData(true);
    axios
      .get(BASE_URL + `/tv/${id}`, {
        headers,
      })
      .then((details) => {
        //Show details returned
        return axios
          .get(BASE_URL + `/tv/${id}/credits`, {
            headers,
          })
          .then((credits) => {
            //Show credits returned
            details.data.mediaType = "show";
            details.data.genresAsText = "";
            for (let i = 0; i < details.data.genres.length; i++) {
              details.data.genresAsText =
                details.data.genresAsText + ", " + details.data.genres[i].name;
            }
            details.data.genresAsText = details.data.genresAsText.slice(2);
            //Array of genres converted to string of names
            details.data.creatorsAsText = "";
            for (let i = 0; i < details.data.created_by.length; i++) {
              details.data.creatorsAsText =
                details.data.creatorsAsText +
                ", " +
                details.data.created_by[i].name;
            }
            details.data.creatorsAsText = details.data.creatorsAsText.slice(2);
            //Array of creators converted to string of names
            details.data.cast = credits.data.cast;
            formatForeignAPI(details.data, "ShowDetails");
            setShowData(details.data);
          });
      })
      .catch((err) => {
        setErrorShowData(err);
      })
      .finally(() => {
        setLoadingShowData(false);
      });
  }, []);

  return {
    data: { showData: showData },
    loading: { loadingShowData: loadingShowData },
    error: { errorShowData: errorShowData },
  };
}

export function fetchMoviesPageData() {
  const [movies, setMovies] = useState([]);
  const [loadingMovies, setLoadingMovies] = useState(true);
  const [errorMovies, setErrorMovies] = useState(null);

  useEffect(() => {
    setLoadingMovies(true);
    axios
      .get(BASE_URL + "/movie/popular", {
        headers,
      })
      .then((response1) => {
        //Movies returned successfully
        return axios
          .get(BASE_URL + "/genre/movie/list", {
            headers,
          })
          .then((response2) => {
            //Movie genres returned successfully
            response1.data.results.map((movie) => {
              movie.genres = [];
              movie.genresAsText = "";
              movie.mediaType = "movie";
              for (let i = 0; i < movie.genre_ids.length; i++) {
                for (let j = 0; j < response2.data.genres.length; j++) {
                  if (movie.genre_ids[i] === response2.data.genres[j].id) {
                    movie.genres.push(response2.data.genres[j]);
                    movie.genresAsText =
                      movie.genresAsText + ", " + response2.data.genres[j].name;
                    break;
                  }
                }
              }
              movie.genresAsText = movie.genresAsText.slice(2);
              formatForeignAPI(movie, "MoviesPage");
            });
            setMovies(response1.data.results);
          });
      })
      .catch((err) => {
        setErrorMovies(err);
      })
      .finally(() => {
        setLoadingMovies(false);
      });
  }, []);

  return {
    data: { movies: movies },
    loading: { loadingMovies: loadingMovies },
    error: { errorMovies: errorMovies },
  };
}

export function fetchShowsPageData() {
  const [shows, setShows] = useState([]);
  const [loadingShows, setLoadingShows] = useState(true);
  const [errorShows, setErrorShows] = useState(null);

  useEffect(() => {
    setLoadingShows(true);
    axios
      .get(BASE_URL + "/tv/top_rated", {
        headers,
      })
      .then((response1) => {
        return axios
          .get(BASE_URL + "/genre/tv/list", {
            headers,
          })
          .then((response2) => {
            response1.data.results.map((show) => {
              show.genres = [];
              show.genresAsText = "";
              show.mediaType = "show";
              for (let i = 0; i < show.genre_ids.length; i++) {
                for (let j = 0; j < response2.data.genres.length; j++) {
                  if (show.genre_ids[i] === response2.data.genres[j].id) {
                    show.genres.push(response2.data.genres[j]);
                    show.genresAsText =
                      show.genresAsText + ", " + response2.data.genres[j].name;
                    break;
                  }
                }
              }
              show.genresAsText = show.genresAsText.slice(2);
              formatForeignAPI(show, "ShowsPage");
            });
            setShows(response1.data.results);
          });
      })
      .catch((err) => {
        setErrorShows(err);
      })
      .finally(() => {
        setLoadingShows(false);
      });
  }, []);

  return {
    data: { shows: shows },
    loading: { loadingShows: loadingShows },
    error: { errorShows: errorShows },
  };
}

export function fetchMediaForSearchbar(title, setMedia) {
  axios
    .get(BASE_URL + "/search/movie", {
      headers,
      params: {
        query: title,
      },
    })
    .then((movieData) => {
      //Movies returned successfully
      return axios
        .get(BASE_URL + "/search/tv", {
          headers,
          params: {
            query: title,
          },
        })
        .then((showsData) => {
          setMedia(movieData.data.results.concat(showsData.data.results));
        });
    })
    .catch((err) => {
      console.log(err);
    });
  //======================================
}

export function fetchMediaForSearchResults(title, setMedia, setLoading) {
  setLoading(true);
  axios
    .get(BASE_URL + "/search/movie", {
      headers,
      params: {
        query: title,
      },
    })
    .then((movieData) => {
      //Movies returned successfully
      return axios
        .get(BASE_URL + "/genre/movie/list", {
          headers,
        })
        .then((movieGenresData) => {
          //Movie genres returned successfully
          movieData.data.results.map((movie) => {
            movie.genres = [];
            movie.genresAsText = "";
            movie.mediaType = "movie";
            for (let i = 0; i < movie.genre_ids.length; i++) {
              for (let j = 0; j < movieGenresData.data.genres.length; j++) {
                if (movie.genre_ids[i] === movieGenresData.data.genres[j].id) {
                  movie.genres.push(movieGenresData.data.genres[j]);
                  movie.genresAsText =
                    movie.genresAsText +
                    ", " +
                    movieGenresData.data.genres[j].name;
                  break;
                }
              }
            }
            movie.genresAsText = movie.genresAsText.slice(2);
            formatForeignAPI(movie, "SearchResults");
          });
          //-----------------------------------------------------
          return axios
            .get(BASE_URL + "/search/tv", {
              headers,
              params: {
                query: title,
              },
            })
            .then((showsData) => {
              //TV shows returned successfully
              return axios
                .get(BASE_URL + "/genre/tv/list", {
                  headers,
                })
                .then((showGenresData) => {
                  //TV genres returned successfully
                  showsData.data.results.map((show) => {
                    show.genres = [];
                    show.genresAsText = "";
                    show.mediaType = "show";
                    for (let i = 0; i < show.genre_ids.length; i++) {
                      for (
                        let j = 0;
                        j < showGenresData.data.genres.length;
                        j++
                      ) {
                        if (
                          show.genre_ids[i] === showGenresData.data.genres[j].id
                        ) {
                          show.genres.push(showGenresData.data.genres[j]);
                          show.genresAsText =
                            show.genresAsText +
                            ", " +
                            showGenresData.data.genres[j].name;
                          break;
                        }
                      }
                    }
                    show.genresAsText = show.genresAsText.slice(2);
                    formatForeignAPI(show, "SearchResults");
                  });
                  setMedia(
                    movieData.data.results.concat(showsData.data.results)
                  );
                });
            });
        });
    })
    .catch((err) => {
      console.log(err);
    })
    .finally(() => {
      setLoading(false);
    });
}
