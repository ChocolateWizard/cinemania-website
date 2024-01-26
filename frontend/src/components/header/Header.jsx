import React, { useContext, useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import SiteLogo from "../../assets/SiteLogo";
import Searchbar from "./Searchbar";
import { GlobalContext } from "../../context/GlobalState";
import { logout } from "../../utils/Api";
import { toast } from "react-toastify";

export default function Header() {
  const navigate = useNavigate();
  const { sessionData, removeSessionData } = useContext(GlobalContext);

  function handleLogout() {
    logout()
      .then((response) => {
        if (response.status == 200) {
          removeSessionData();
          navigate(`/`);
          toast.success("You have been logged out!");
        } else {
          console.error(response.data);
          toast.error("Unable to logout!");
        }
      })
      .catch((err) => {
        console.error(err);
        toast.error("Unable to logout!");
      });
  }

  const WatchlistOption = ({ isLoggedIn }) => {
    return isLoggedIn ? (
      <li className="mt-3 md:ml-6 md:mt-0 md:mr-3">
        <Link to={"/watchlist"} className="hover:text-mellon-primary">
          Watchlist
        </Link>
      </li>
    ) : null;
  };

  const LoginRegisterOptions = ({ sessionData }) => {
    return sessionData.isLoggedIn ? (
      <ul className="flex flex-col md:flex-row items-center ml-auto">
        <li>
          <button onClick={handleLogout} className="hover:text-mellon-primary">
            Logout
          </button>
        </li>
        <li className="flex flex-row items-center ml-5">
          <div>{sessionData.user.profileName}</div>
          <img
            src={sessionData.user.profileImageUrl}
            alt="avatar"
            className="rounded-full w-8 h-8 ml-2"
          />
        </li>
      </ul>
    ) : (
      <ul className="flex flex-col md:flex-row items-center ml-auto">
        <li>
          <Link to={"/login"} className="hover:text-mellon-primary">
            Login
          </Link>
        </li>
        <li className="ml-5">
          <Link to={"/register"} className="hover:text-mellon-primary">
            Register
          </Link>
        </li>
      </ul>
    );
  };

  return (
    <header className="border-b border-onyx-tint bg-onyx-tint">
      <nav className="container flex flex-col md:flex-row items-center justify-between py-2">
        <ul className="flex flex-col md:flex-row items-center">
          <li className="md:pl-10">
            <a href="/" className="flex flex-row">
              <SiteLogo />
              <h1 className="font-bold text-3xl text-mellon-primary">
                Kinomania
              </h1>
            </a>
          </li>
          <li className="mt-3 md:ml-16 md:mt-0">
            <Link to={"/movies"} className="hover:text-mellon-primary">
              Movies
            </Link>
          </li>
          <li className="mt-3 md:ml-6 md:mt-0">
            <Link to={"/shows"} className="hover:text-mellon-primary">
              TV Shows
            </Link>
          </li>
          <WatchlistOption isLoggedIn={sessionData.isLoggedIn} />
        </ul>
        <div className="flex flex-col md:flex-row items-center">
          <Searchbar />
          <div className="mt-3 md:ml-4 md:mt-0 flex flex-col md:flex-row">
            <LoginRegisterOptions sessionData={sessionData} />
          </div>
        </div>
      </nav>
    </header>
  );
}
