import { Navigate, useParams } from "react-router-dom";
import { useContext } from "react";
import { GlobalContext } from "./GlobalState";

export default function AuthRouteGuard({ children,mustBeLoggedIn }){
  const { sessionData } = useContext(GlobalContext);

  if (sessionData.isLoggedIn === mustBeLoggedIn) {
    return children;
  } else {
    return <Navigate to="/" replace />;
  }
};
