import React from 'react'

export default function HeaderTest() {

    
    return (
        <header className="border-b border-onyx-tint bg-onyx-tint">
          <nav className="container flex flex-col md:flex-row items-center justify-between py-2">
            <ul className="flex flex-col md:flex-row items-center">
              <li className="md:pl-10">
                <a href="/" className="flex flex-row">
                  <SiteLogo />
                  <h1 className="font-bold text-3xl text-mellon-primary-default">
                    Cinemania
                  </h1>
                </a>
              </li>
              <li className="mt-3 md:ml-16 md:mt-0">
                <Link to={"/movies"} className="hover:text-mellon-primary-default">
                  Movies
                </Link>
              </li>
              <li className="mt-3 md:ml-6 md:mt-0">
                <Link to={"/shows"} className="hover:text-mellon-primary-default">
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
