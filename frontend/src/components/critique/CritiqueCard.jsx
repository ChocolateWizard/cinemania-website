import React, { useContext, useState } from "react";
import { GlobalContext } from "../../context/GlobalState";
import { deleteCritique, putCritique } from "../../utils/Api";
import { toast } from "react-toastify";

export default function CritiqueCard({ id, critique, isUsersCritique }) {
  const { updateCritique, removeCritique } = useContext(GlobalContext);

  const [updateToggled, setUpdateToggled] = useState(false);
  const [rating, setRating] = useState(critique.rating);
  const [description, setDescription] = useState(critique.description);

  function handleClickDelete() {
    deleteCritique(id)
      .then((response) => {
        if (response.status === 205) {
          removeCritique(id);
          window.location.reload(false);
        } else {
          console.error(response.data);
          toast.error("Unable to delete critique!");
        }
      })
      .catch((err) => {
        console.error(err);
        toast.error("Unable to delete critique!");
      });
  }
  function handleClickUpdate() {
    setUpdateToggled(!updateToggled);
    setRating(critique.rating);
    setDescription(critique.description);
  }
  function handleClickSave() {
    putCritique(id, { rating: rating, description: description })
      .then((response) => {
        switch (response.status) {
          case 205:
            updateCritique({
              media: { id: id },
              rating: rating,
              description: description,
            });
            window.location.reload(false);
            break;
          default:
            toast.success("Successfully updated critique!");
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
            toast.error("Unable to update critique!");
        }
      });
  }

  //==========================================================================================================
  if (isUsersCritique) {
    return (
      <div className="flex flex-col border-2 bg-onyx-primary-20 border-mellon-primary-default p-4 my-3">
        <div className="flex flex-row justify-between items-center">
          <div className="flex flex-row items-center">
            <img
              className="w-12 h-12 rounded-full"
              src={critique.critic.profile_image_url}
              alt="User Image"
            />
            <div className="flex flex-row ml-4">
              <p className="font-bold">{critique.critic.profile_name}</p>
              <div className="flex flex-row space-x-2 ml-4">
                <button
                  onClick={handleClickUpdate}
                  className={
                    updateToggled
                      ? "px-2 py-1 text-white rounded bg-mellon-shade"
                      : "px-2 py-1 text-white rounded bg-mellon-primary-default"
                  }
                >
                  Update
                </button>
                {updateToggled ? null : (
                  <button
                    onClick={handleClickDelete}
                    className="px-2 py-1 bg-red-900 text-white rounded"
                  >
                    Remove
                  </button>
                )}
              </div>
            </div>
          </div>
          <div className="text-2xl font-bold text-blue-400">
            <input
              disabled={!updateToggled}
              className={
                updateToggled
                  ? "text-2xl rounded w-12 text-right font-bold text-blue-400 bg-onyx-primary-15"
                  : "text-2xl rounded w-12 text-right font-bold text-blue-400 bg-inherit"
              }
              type="text"
              value={rating}
              onChange={(e) => setRating(e.target.value)}
            />
            %
          </div>
        </div>
        <input
          disabled={!updateToggled}
          className={
            updateToggled
              ? "mt-4 rounded text-wrap break-all bg-onyx-primary-15"
              : "mt-4 rounded text-wrap break-all bg-inherit"
          }
          value={description}
          onChange={(e) => setDescription(e.target.value)}
        />
        {updateToggled ? (
          <div className="mt-5 items-end text-right">
            <button
              onClick={handleClickSave}
              className="px-2 py-1 items-end text-white text-xl rounded bg-mellon-primary-default "
            >
              Save
            </button>
          </div>
        ) : null}
      </div>
    );
  }

  return (
    <div className="flex flex-col border-2 bg-onyx-primary-20 border-onyx-primary-20 p-4 my-3">
      <div className="flex flex-row justify-between items-center">
        <div className="flex flex-row items-center">
          <img
            className="w-12 h-12 rounded-full"
            src={critique.critic.profile_image_url}
            alt="User Image"
          />
          <div className="ml-4">
            <p className="font-bold">{critique.critic.profile_name}</p>
          </div>
        </div>
        <div className="text-2xl font-bold text-blue-400">
          {critique.rating}%
        </div>
      </div>
      <p className="mt-4 text-wrap break-all">{critique.description}</p>
    </div>
  );
}
