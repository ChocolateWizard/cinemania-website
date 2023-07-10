import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import SiteLogo from "../../assets/SiteLogo";
import Searchbar from "./Searchbar";

export default function Header() {
 

  return (
    <header className="border-b border-onyx-tint bg-onyx-tint">
      <nav className="container flex flex-col md:flex-row items-center justify-between py-2">
        <ul className="flex flex-col md:flex-row items-center">
          <li className="md:pl-10">
            <a href="/" className="flex flex-row">
              <SiteLogo />
              <h1 className="font-bold text-3xl text-mellon-primary">
                Cinemania
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
          <li className="mt-3 md:ml-6 md:mt-0 md:mr-3">
            <Link to={"/watchlist"} className="hover:text-mellon-primary">
              Watchlist
            </Link>
          </li>
        </ul>
        <div className="flex flex-col md:flex-row items-center">
          <Searchbar />
          <div className="mt-3 md:ml-4 md:mt-0 flex flex-col md:flex-row">
            <ul className="flex flex-col md:flex-row items-center ml-auto">
              <li>
                <a href="#" className="hover:text-mellon-primary">
                  <i className="fa-solid fa-arrow-right-to-bracket"></i>
                  Login
                </a>
              </li>
              <li className="ml-5">
                <a href="#" className="hover:text-mellon-primary">
                  Register
                </a>
              </li>
            </ul>
          </div>
        </div>
      </nav>
    </header>
  );
}
