import React, { useContext, useEffect, useRef, useState } from "react";
import { Link } from "react-router-dom";
import { GlobalContext } from "../../context/GlobalState";
import SiteLogoSVG from "../helpers/svg/SiteLogoSVG";
import Person2SVG from "../helpers/svg/Person2SVG";
import UserDropdownMenu from "./UserDropdownMenu";

export default function Header({ children }) {
  const { sessionData } = useContext(GlobalContext);

  return (
    <header className="bg-onyx-tint">
      <nav>
        <ul className="flex flex-col md:flex-row justify-between">
          <li>
            <ul className="flex flex-col md:flex-row">
              <li>
                <Link
                  to={"/"}
                  className="group flex flex-row justify-center items-center pr-4 pl-3 py-2 h-full"
                >
                  <SiteLogoSVG
                    height="40"
                    className="fill-mellon-primary-default group-hover:opacity-75 "
                  />
                  <span className="font-bold text-4xl text-mellon-primary-default group-hover:opacity-75">
                    Cinemania
                  </span>
                </Link>
              </li>
              <li>
                <Link
                  to={"/movies"}
                  className="text-lg flex justify-center items-center md:pt-2 px-2 h-full 
                  hover:text-mellon-primary-default  hover:bg-onyx-primary-20 
                  "
                >
                  Movies
                </Link>
              </li>
              <li>
                <Link
                  to={"/shows"}
                  className="text-lg flex justify-center items-center md:pt-2 px-2 h-full 
                  hover:text-mellon-primary-default  hover:bg-onyx-primary-20 
                  "
                >
                  TV Shows
                </Link>
              </li>
            </ul>
          </li>
          {/* Searcbar, login and register */}
          <li>
            <ul className="flex flex-col md:flex-row h-full">
              <li className="flex justify-center items-center">
                {children}
              </li>
              <UserMenu sessionData={sessionData} />
            </ul>
          </li>
        </ul>
      </nav>
    </header>
  );
}

function UserMenu({ sessionData }) {
  const [open, setOpen] = useState(false);
  const node = useRef();

  function exitMenu() {
    setOpen(false);
  }

  //This is so whenever user clicks outside of dropdown menu, it closes if its open
  useEffect(() => {
    // Function to handle click events
    const handleClick = (e) => {
      if (node.current.contains(e.target)) {
        // Inside click
        return;
      }
      // Outside click
      setOpen(false);
    };

    // Add the event listener when the component mounts
    if (open) {
      document.addEventListener("mousedown", handleClick);
    } else {
      document.removeEventListener("mousedown", handleClick);
    }

    // Cleanup the event listener when the component unmounts
    return () => {
      document.removeEventListener("mousedown", handleClick);
    };
  }, [open]);

  if (!sessionData.isLoggedIn) {
    return (
      <>
        <li>
          <Link
            to={"/login"}
            className="text-lg flex justify-center items-center md:pt-2  px-4 h-full 
      hover:text-mellon-primary-default  hover:bg-onyx-primary-20"
          >
            Login
          </Link>
        </li>
        <li>
          <Link
            to={"/register"}
            className="text-lg flex justify-center items-center md:pt-2 px-4  h-full 
      hover:text-mellon-primary-default  hover:bg-onyx-primary-20"
          >
            Register
          </Link>
        </li>
      </>
    );
  }
  return (
    <li
      className="flex justify-center items-center md:mr-5 md:ml-10"
      ref={node}
    >
      <button
        type="button"
        onClick={() => {
          setOpen(!open);
        }}
        className="group flex flex-row justify-center items-center space-x-2 h-full"
      >
        <span className="group-hover:opacity-75">
          {sessionData.user.profileName}
        </span>
        {sessionData.user.profileImageUrl ? (
          <img
            src={sessionData.user.profileImageUrl}
            className="rounded-full w-10 h-10 group-hover:opacity-75"
          />
        ) : (
          <div className="flex items-center justify-center rounded-full w-10 h-10 group-hover:opacity-75 bg-onyx-primary-35">
            <Person2SVG className="fill-onyx-primary-50 p-2" />
          </div>
        )}
      </button>
      {open && <UserDropdownMenu exitMenu={exitMenu} />}
    </li>
  );
}
