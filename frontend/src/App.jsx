import React, { useState } from "react";

import { BrowserRouter as Router, Routes, Route } from "react-router-dom";

import Header from "./components/header/Header";
import Home from "./components/home/Home";
import PageNotFound from "./components/pageNotFound/PageNotFound";
import Footer from "./components/footer/Footer";
import MoviesPage from "./components/movie/page/MoviesPage";
import ShowsPage from "./components/show/page/ShowsPage";
import Watchlist from "./components/watchlist/Watchlist";

import { GlobalProvider } from "./context/GlobalState";
import SearchResults from "./components/search/SearchResults";
import AuthRouteGuard from "./context/AuthRouteGuard";
import LoginPage from "./components/login/LoginPage";
import RegisterPage from "./components/register/RegisterPage";
import { Slide, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import SearchOptions from "./components/search/SearchOptions";
import Searchbar from "./components/header/Searchbar";
import MediaDetails from "./components/media/MediaDetails";

//different options for comboboxes in search filter options to customize searching of media
const searchOptions = {
  mediaTypes: [
    { value: "all", label: "All" },
    { value: "movie", label: "Movie" },
    { value: "tv_show", label: "TV Show" },
  ],
  genres: [
    {
      value: "all",
      label: "All",
    },
    {
      value: "1",
      label: "Action",
    },
    {
      value: "3",
      label: "Adventure",
    },
    {
      value: "4",
      label: "Animation",
    },
    {
      value: "5",
      label: "Comedy",
    },
    {
      value: "8",
      label: "Drama",
    },
    {
      value: "12",
      label: "Horror",
    },
    {
      value: "15",
      label: "Mystery",
    },
    {
      value: "24",
      label: "Thriller",
    },
  ],
};

function App() {
  //sets if search filter options panel is open or closed
  const [searchOptionsVisible, setSearchOptionsVisible] = useState(false);
  const [searchOption, setSearchOption] = useState({
    mediaType: "all",
    genre: "all",
  });

  return (
    <GlobalProvider>
      <div className="min-h-screen">
        <ToastContainer
          position="top-center"
          autoClose={5000}
          hideProgressBar={false}
          newestOnTop={false}
          closeOnClick
          rtl={false}
          pauseOnFocusLoss
          draggable
          pauseOnHover
          theme="colored"
          transition={Slide}
        />
        <Router>
          <Header>
            <Searchbar
              searchOption={searchOption}
              searchOptionsVisible={searchOptionsVisible}
              changeSearchOptionsVisibility={() => {
                setSearchOptionsVisible(!searchOptionsVisible);
              }}
            />
          </Header>
          <main>
            <SearchOptions
              visible={searchOptionsVisible}
              setMediaType={(value) => {
                setSearchOption({ ...searchOption, mediaType: value });
              }}
              setGenre={(value) => {
                setSearchOption({ ...searchOption, genre: value });
              }}
              options={searchOptions}
              defaultOption={searchOption}
            />
            <Routes>
              <Route path="/" exact element={<Home />} />
              <Route path="/movies" exact element={<MoviesPage />} />
              <Route path="/shows" exact element={<ShowsPage />} />
              <Route
                path="/watchlist"
                exact
                element={
                  <AuthRouteGuard mustBeLoggedIn={true}>
                    <Watchlist />
                  </AuthRouteGuard>
                }
              />
              <Route
                path="/login"
                exact
                element={
                  <AuthRouteGuard mustBeLoggedIn={false}>
                    <LoginPage />
                  </AuthRouteGuard>
                }
              />
              <Route
                path="/register"
                exact
                element={
                  <AuthRouteGuard mustBeLoggedIn={false}>
                    <RegisterPage />
                  </AuthRouteGuard>
                }
              />
              <Route path="/movie/:id" element={<MediaDetails mediaType={"movie"} />} />
              <Route path="/show/:id" element={<MediaDetails mediaType={"tv_show"} />} />
              <Route path="/search" element={<SearchResults />} />
              <Route path="/page-not-found" element={<PageNotFound />} />
              <Route path="/*" element={<PageNotFound />} />
            </Routes>
          </main>
          <Footer />
        </Router>
      </div>
    </GlobalProvider>
  );
}

export default App;
