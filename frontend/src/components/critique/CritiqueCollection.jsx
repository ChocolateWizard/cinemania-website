import React, { useContext, useEffect, useState } from "react";
import { GlobalContext } from "../../context/GlobalState";
import CritiqueCard from "./CritiqueCard";
import { postCritique } from "../../utils/Api";
import { toast } from "react-toastify";
import Person2SVG from "../helpers/svg/Person2SVG";

//current media id and its critiques
export default function CritiqueCollection({ id, critiques }) {
  const { addCritique, sessionData } = useContext(GlobalContext);
  //it can only add if its critique is not present in all critiques for given movie
  const [canAdd, setCanAdd] = useState(
    sessionData.isLoggedIn &&
      critiques.find((c) => {
        return c.critic.profile_name === sessionData.user.profileName;
      })
      ? false
      : true
  );

  const [isShowSelected, setIsShowSelected] = useState(true);
  const [isAddSelected, setIsAddSelected] = useState(false);

  function clickShowCritiques() {
    setIsShowSelected(true);
    setIsAddSelected(false);
  }
  function clickAddCritique() {
    setIsShowSelected(false);
    setIsAddSelected(true);
  }

  //==================================================================================

  const ShowTab = () => {
    const [rating, setRating] = useState("");
    const [description, setDescription] = useState("");

    function handleAddCritique(e) {
      e.preventDefault();
      postCritique(
        id,
        JSON.stringify({ rating: rating, description: description })
      )
        .then((response) => {
          switch (response.status) {
            case 205:
              addCritique({
                media: { id: id },
                rating: rating,
                description: description,
              });
              window.location.reload(false);
              break;
            default:
              toast.success("Successfully added critique!");
          }
        })
        .catch((err) => {
          switch (err.response.status) {
            case 400:
              console.error(err);
              toast.error(
                <div className="flex flex-col">
                  {err.response.data.details.map((d) => {
                    return <div>{d}</div>;
                  })}
                </div>
              );
              break;
            default:
              console.error(err);
              toast.error("Unable to add critique!");
          }
        });
    }

    const onRatingChange = (e) => {
      // e.preventDefault();
      setRating(e.target.value);
    };
    const onDescriptionChange = (e) => {
      // e.preventDefault();
      setDescription(e.target.value);
    };

    if (isShowSelected) {
      if (critiques.length === 0) {
        //no critiques for given movie
        return (
          <div className="flex flex-col justify-center items-center mb-32 mt-20">
            <h1 className="font-bold uppercase text-4xl text-onyx-tint">
              No critiques found
            </h1>
          </div>
        );
      } else {
        const usersCritique = critiques.find(
          (critique) =>
            critique.critic.profile_name === sessionData.user.profileName
        );
        if (!usersCritique) {
          return (
            <div className="flex flex-col mx-10 my-16">
              {critiques.map((critique, index) => {
                return (
                  <CritiqueCard
                    key={index}
                    id={id}
                    critique={critique}
                    isUsersCritique={false}
                  />
                );
              })}
            </div>
          );
        }
        return (
          <div className="flex flex-col mx-10 my-16">
            <CritiqueCard
              id={id}
              critique={usersCritique}
              isUsersCritique={true}
            />
            {critiques
              .filter((critique) => {
                return (
                  critique.critic.profile_name !== sessionData.user.profileName
                );
              })
              .map((critique, index) => {
                return (
                  <CritiqueCard
                    id={id}
                    key={index}
                    critique={critique}
                    isUsersCritique={false}
                  />
                );
              })}
          </div>
        );
      }
    }
    if (isAddSelected) {
      return (
        <div className="flex flex-col border-2 bg-onyx-primary-20 border-mellon-primary-default p-4 my-3">
          <div className="flex flex-row justify-between items-center">
            <div className="flex flex-row items-center">
              <UserProfilePicture url={sessionData.user.profileImageUrl} />
              <div className="flex flex-row ml-4">
                <p className="font-bold">{sessionData.user.profileName}</p>
              </div>
            </div>
            <div className="text-2xl font-bold text-blue-400">
              <input
                className="text-2xl rounded w-12 text-right font-bold text-blue-400 bg-onyx-primary-15"
                type="text"
                value={rating}
                onChange={onRatingChange}
              />
              %
            </div>
          </div>
          <input
            className="mt-4 rounded text-wrap break-all bg-onyx-primary-15"
            type="text"
            value={description}
            onChange={onDescriptionChange}
          />

          <div className="mt-5 items-end text-right">
            <button
              onClick={handleAddCritique}
              className="px-2 py-1 items-end text-white text-xl rounded bg-mellon-primary-default "
            >
              Add critique
            </button>
          </div>
        </div>
      );
    }
  };

  const ListAllCritiques = ({ critiques }) => {
    if (critiques.length === 0) {
      return (
        <div className="flex flex-col justify-center items-center mb-32 mt-20">
          <h1 className="font-bold uppercase text-4xl text-onyx-tint">
            No critiques found
          </h1>
        </div>
      );
    }
    //list all critiques for given media
    return (
      <div className="flex flex-col mx-10 my-16">
        {critiques.map((critique, index) => {
          return (
            <CritiqueCard
              key={index}
              id={id}
              critique={critique}
              isUsersCritique={false}
            />
          );
        })}
      </div>
    );
  };

  //======================================================================================

  //if user isn't logged in or he's REGULAR
  if (
    !(
      sessionData.isLoggedIn &&
      (sessionData.user.role === "CRITIC" ||
        sessionData.user.role === "ADMINISTRATOR")
    )
  ) {
    return <ListAllCritiques critiques={critiques} />;
  }
  //user is logged in and is CRITIC || ADMINISTRATOR
  return (
    <div className="flex flex-col">
      <div className="flex flex-row">
        <button onClick={clickShowCritiques}>
          <h2
            className={
              "text-2xl ml-4 font-semibold hover:text-mellon-primary-default " +
              (isShowSelected ? "text-mellon-primary-default" : "")
            }
          >
            Show
          </h2>
        </button>
        <button disabled={!canAdd} onClick={clickAddCritique}>
          <h2
            className={
              canAdd
                ? isAddSelected
                  ? "text-2xl ml-4 font-semibold hover:text-mellon-primary-default text-mellon-primary-default"
                  : "text-2xl ml-4 font-semibold hover:text-mellon-primary-default"
                : "text-2xl ml-4 font-semibold text-onyx-primary-20"
            }
          >
            Add
          </h2>
        </button>
      </div>
      <ShowTab />
    </div>
  );
}

function UserProfilePicture({ url }) {
  if (url) {
    return <img className="w-12 h-12 rounded-full" src={url} />;
  }
  return (
    <div className="flex items-center justify-center rounded-full w-12 h-12 bg-onyx-primary-35">
      <Person2SVG className="fill-onyx-primary-50 p-2.5" />
    </div>
  );
}
