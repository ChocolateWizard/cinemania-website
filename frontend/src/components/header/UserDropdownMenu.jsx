import React, { useContext, useEffect, useRef, useState } from "react";

import SignOutSVG from "../helpers/svg/SignOutSVG";
import SettingsSVG from "../helpers/svg/SettingsSVG";
import DatabaseSVG from "../helpers/svg/DatabaseSVG";
import PapersSVG from "../helpers/svg/PapersSVG";
import LibrarySVG from "../helpers/svg/LibrarySVG";
import ArrowRightSVG from "../helpers/svg/ArrowRightSVG";
import { CSSTransition } from "react-transition-group";
import ArrowLeftSVG from "../helpers/svg/ArrowLeftSVG";
import "./UserDropdownMenu.css";
import { Link, useNavigate } from "react-router-dom";
import { GlobalContext } from "../../context/GlobalState";
import { logout } from "../../utils/Api";
import { toast } from "react-toastify";
import MovieTapeSVG from "../helpers/svg/MovieTapeSVG";
import TelevisionSVG from "../helpers/svg/TelevisionSVG";
import PersonsSVG from "../helpers/svg/PersonsSVG";

export default function UserDropdownMenu({ exitMenu }) {
  const navigate = useNavigate();
  const { sessionData, removeSessionData } = useContext(GlobalContext);
  const [activeMenu, setActiveMenu] = useState("main");
  const [menuHeight, setMenuHeight] = useState(null);
  const dropdownRef = useRef();

  function handleSignOut() {
    logout()
      .then((res) => {
        removeSessionData();
        exitMenu();
        navigate(`/`);
        toast.success("You have been logged out!");
      })
      .catch((err) => {
        console.error(err);
        toast.error("Unable to logout!");
      });
  }

  useEffect(() => {
    setMenuHeight(dropdownRef.current?.firstChild.offsetHeight + 32);
  }, []);

  function calcHeight(el) {
    const height = el.offsetHeight;
    setMenuHeight(height + 32);
  }

  function DropdownLinkItem({ to, leftIcon, rightIcon, children }) {
    return (
      <Link
        to={to}
        className="h-[50px] w-full flex items-center rounded-lg transition-all duration-200 p-2 hover:bg-onyx-primary-30"
        onClick={() => exitMenu()}
      >
        <span>{leftIcon}</span>
        {children}
        <span className="ml-auto">{rightIcon}</span>
      </Link>
    );
  }

  function DropdownButtonItem({
    goToMenu,
    onClick,
    leftIcon,
    rightIcon,
    children,
  }) {
    return (
      <button
        type="button"
        className="h-[50px] w-full flex items-center rounded-lg transition-all duration-200 p-2 hover:bg-onyx-primary-30"
        onClick={() =>
          onClick ? onClick() : goToMenu && setActiveMenu(goToMenu)
        }
      >
        <span>{leftIcon}</span>
        {children}
        <span className="ml-auto">{rightIcon}</span>
      </button>
    );
  }

  //====================================================================================================
  return (
    <div
      className={`absolute z-10 flex flex-col top-[58px] w-[300px] transform translate-y-[50%] md:translate-y-[0%] md:-translate-x-[30%] bg-onyx-primary-25 
      border rounded-lg p-4 overflow-hidden user-dropdown-menu`}
      style={{ height: menuHeight }}
      ref={dropdownRef}
    >
      {/* ---------------MAIN MENU--------------------------------------- */}
      <CSSTransition
        in={activeMenu === "main"}
        unmountOnExit
        timeout={500}
        classNames={"header-menu-primary"}
        onEnter={calcHeight}
      >
        <div className="header-menu w-[268px]">
          <DropdownLinkItem
            to={"/watchlist"}
            leftIcon={
              <LibrarySVG
                height="25px"
                width="25px"
                className="fill-onyx-primary-50 mr-1"
              />
            }
          >
            My Watchlist
          </DropdownLinkItem>
          {sessionData.user.role !== "REGULAR" && (
            <DropdownLinkItem
              to={"/page-not-found"}
              leftIcon={
                <PapersSVG
                  height="25px"
                  width="25px"
                  className="fill-onyx-primary-50 mr-1"
                />
              }
            >
              My Critiques
            </DropdownLinkItem>
          )}

          {sessionData.user.role === "ADMINISTRATOR" && (
            <DropdownButtonItem
              leftIcon={
                <DatabaseSVG height="25px" width="25px" className="mr-1" />
              }
              rightIcon={
                <ArrowRightSVG
                  height="25px"
                  width="25px"
                  className="fill-onyx-primary-50"
                />
              }
              goToMenu={"manageData"}
            >
              Manage data
            </DropdownButtonItem>
          )}

          <DropdownButtonItem
            leftIcon={
              <SettingsSVG
                height="25px"
                width="25px"
                className="fill-onyx-primary-50 mr-1"
              />
            }
            rightIcon={
              <ArrowRightSVG
                height="25px"
                width="25px"
                className="fill-onyx-primary-50"
              />
            }
            goToMenu="settings"
          >
            Settings
          </DropdownButtonItem>
          <DropdownButtonItem
            leftIcon={
              <SignOutSVG
                height="25px"
                width="25px"
                className="fill-onyx-primary-50 mr-1"
              />
            }
            onClick={handleSignOut}
          >
            Sign out
          </DropdownButtonItem>
        </div>
      </CSSTransition>

      {/* --------------SETTINGS MENU---------------------------------------- */}
      <CSSTransition
        in={activeMenu === "settings"}
        unmountOnExit
        timeout={500}
        classNames={"header-menu-secondary"}
        onEnter={calcHeight}
      >
        <div className="header-menu">
          <DropdownButtonItem
            leftIcon={
              <ArrowLeftSVG
                height="25px"
                width="25px"
                className="fill-onyx-primary-50"
              />
            }
            goToMenu="main"
          ></DropdownButtonItem>
        </div>
      </CSSTransition>
      {/* --------------MANAGE DATA MENU---------------------------------------- */}
      <CSSTransition
        in={activeMenu === "manageData"}
        unmountOnExit
        timeout={500}
        classNames={"header-menu-secondary"}
        onEnter={calcHeight}
      >
        <div className="header-menu">
          <DropdownButtonItem
            leftIcon={
              <ArrowLeftSVG
                height="25px"
                width="25px"
                className="fill-onyx-primary-50"
              />
            }
            goToMenu="main"
          ></DropdownButtonItem>
          <DropdownLinkItem
            leftIcon={
              <MovieTapeSVG
                height="25px"
                width="25px"
                className="fill-onyx-primary-50 mr-2"
              />
            }
            to={"/page-not-found"}
          >
            Manage movies
          </DropdownLinkItem>
          <DropdownLinkItem
            leftIcon={
              <TelevisionSVG
                height="25px"
                width="25px"
                className="fill-onyx-primary-50 mr-2 mb-1"
              />
            }
            to={"/page-not-found"}
          >
            Manage tv shows
          </DropdownLinkItem>
          <DropdownLinkItem
            leftIcon={
              <PersonsSVG
                height="25px"
                width="25px"
                className="fill-onyx-primary-50 mr-2"
              />
            }
            to={"/page-not-found"}
          >
            Manage persons
          </DropdownLinkItem>
        </div>
      </CSSTransition>
    </div>
  );
}
