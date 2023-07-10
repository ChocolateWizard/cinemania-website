import React from "react";
import SiteLogo from "./siteLogo";

const Navbar = () => {
  return (
    <nav className="border-b border-onyx-tint">
      <div className="container mx-auto flex flex-col md:flex-row items-center justify-between px-4 py-6 ">
        <ul className="flex flex-col md:flex-row items-center">
          <li>
            <a href="#">
              <SiteLogo />
            </a>
          </li>
          <li className="md:ml-16 mt-3 md:mt-0">
            <a href="#" className="hover:text-mellon-primary">
              Movies
            </a>
          </li>
          <li className="md:ml-6 mt-3 md:mt-0">
            <a href="#" className="hover:text-mellon-primary">
              TV Shows
            </a>
          </li>
          <li className="md:ml-6 mt-3 md:mt-0">
            <a href="#" className="hover:text-mellon-primary">
              Actors
            </a>
          </li>
        </ul>
        <div className="flex flex-col md:flex-row items-center">
          <div className="relative mt-3 md:mt-0">
            <input
              type="text"
              className="bg-onyx-contrast text-onyx-tint rounded-full w-64 px-4 pl-8 py-1"
              placeholder="Search"
            />
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
          <div className="md:ml-4 mt-3 md:mt-0 flex flex-col md:flex-row">
            <ul className="flex flex-col md:flex-row items-center">
              <li>
                <a href="#" className="hover:text-laravel">
                  <i className="fa-solid fa-arrow-right-to-bracket"></i>
                  Login
                </a>
              </li>
              <li className="ml-5">
                <a href="#" className="hover:text-laravel">
                  Register
                </a>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
