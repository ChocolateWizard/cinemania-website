import React, { useContext, useState } from "react";
import { GlobalContext } from "../../../context/GlobalState";
import CritiqueCard from "./card/CritiqueCard";
import CritiqueAddForm from "./CritiqueAddForm";
import CardLoader from "../../helpers/loaders/cardLoader/CardLoader";
import CommentsSection from "../comments/CommentsSection";
import { deleteCritique, postCritique } from "../../../utils/Api";
import { findMatchingId } from "../../../utils/Util";
import { toast } from "react-toastify";

export default function CritiquesSection({
  mediaId,
  critiques,
  addCritiqueCallback,
  removeCritiqueCallback,
  addCommentCallback,
  removeCommentCallback,
  addCritiqueLikeDislikeCallback,
  removeCritiqueLikeDislikeCallback,
  addCommentLikeDislikeCallback,
  removeCommentLikeDislikeCallback,
}) {
  const { sessionData, critiquesComments, addCritique, removeCritique } =
    useContext(GlobalContext);
  const [loading, setLoading] = useState(false);
  const userAuthData = {
    isLoggedIn: sessionData.isLoggedIn,
    role: sessionData.user?.role,
    profileName: sessionData.user?.profileName,
  };

  //its null|undefined if it doesnt exist
  const [usersCritique, setUsersCritique] = useState(
    critiques.find((critique) => {
      return critique.critic.profile_name === userAuthData.profileName;
    })
  );
  //same array as critiques if none found by filter
  const restOfCritiques = critiques.filter(
    (critique) => critique.critic.profile_name !== userAuthData.profileName
  );

  const [btnDataState, setBtnDataState] = useState({
    btnShowCritiquesSelected: true,
    btnAddCritiqueSelected: false,
    btnAddCritiqueDisabled: userAuthData.isLoggedIn && usersCritique,
  });

  function onAddUsersCritique(mediaId, rating, description) {
    setLoading(true);
    postCritique(
      JSON.stringify({
        media_id: mediaId,
        rating: rating,
        description: description,
      })
    )
      .then((res) => {
        addCritique(res.data.id);
        res.data.number_of_likes = 0;
        res.data.number_of_dislikes = 0;
        res.data.comments = [];
        setUsersCritique(res.data);
        setBtnDataState({
          btnShowCritiquesSelected: true,
          btnAddCritiqueSelected: false,
          btnAddCritiqueDisabled: true,
        });
        addCritiqueCallback(res.data);
        toast.success("Successfully added critique");
      })
      .catch((err) => {
        console.error(err);
        toast.error(
          <div className="flex flex-col">
            {err.response.data.details.map((d) => {
              return <div>{d}</div>;
            })}
          </div>
        );
      })
      .finally(() => setLoading(false));
  }

  function onRemoveUsersCritique(critique) {
    setLoading(true);
    deleteCritique(critique.id)
      .then((res) => {
        const commentId = findMatchingId(critiquesComments, critique.comments);
        removeCritique(critique.id, commentId);
        setUsersCritique(null);
        setBtnDataState({
          btnShowCritiquesSelected: true,
          btnAddCritiqueSelected: false,
          btnAddCritiqueDisabled: false,
        });
        removeCritiqueCallback(critique.id);
        toast.success("Successfully deleted critique");
      })
      .catch((err) => {
        console.error(err);
        toast.error("Unable to delete critique");
      })
      .finally(() => setLoading(false));
  }

  if (loading) {
    return <CardLoader />;
  }
  return (
    <div className={`flex flex-col`}>
      <CritiquesBtnGroup
        visible={userAuthData.isLoggedIn && userAuthData.role !== "REGULAR"}
      >
        <BtnShowCritiques
          selected={btnDataState.btnShowCritiquesSelected}
          onClick={() =>
            setBtnDataState({
              ...btnDataState,
              btnShowCritiquesSelected: true,
              btnAddCritiqueSelected: false,
            })
          }
        />
        <BtnAddCritique
          disabled={btnDataState.btnAddCritiqueDisabled}
          selected={btnDataState.btnAddCritiqueSelected}
          onClick={() =>
            setBtnDataState({
              ...btnDataState,
              btnShowCritiquesSelected: false,
              btnAddCritiqueSelected: true,
            })
          }
        />
      </CritiquesBtnGroup>
      {btnDataState.btnAddCritiqueSelected ? (
        <CritiqueAddForm
          mediaId={mediaId}
          addUsersCritiqueCallback={onAddUsersCritique}
        />
      ) : (
        <CritiquesCollection
          usersCritique={usersCritique}
          restOfCritiques={restOfCritiques}
          onRemoveUsersCritique={onRemoveUsersCritique}
          addCommentCallback={addCommentCallback}
          removeCommentCallback={removeCommentCallback}
          addCritiqueLikeDislikeCallback={addCritiqueLikeDislikeCallback}
          removeCritiqueLikeDislikeCallback={removeCritiqueLikeDislikeCallback}
          addCommentLikeDislikeCallback={addCommentLikeDislikeCallback}
          removeCommentLikeDislikeCallback={removeCommentLikeDislikeCallback}
        />
      )}
    </div>
  );
}

//header buttons
function CritiquesBtnGroup({ visible, children }) {
  if (visible) {
    return <div className="flex flex-row">{children}</div>;
  }
}

function BtnShowCritiques({ onClick, selected }) {
  return (
    <button onClick={onClick}>
      <h2
        className={`text-2xl ml-4 font-semibold ${
          selected
            ? "text-mellon-primary-default"
            : "hover:text-mellon-primary-default"
        }`}
      >
        Show
      </h2>
    </button>
  );
}
function BtnAddCritique({ onClick, selected, disabled }) {
  return (
    <button disabled={disabled} onClick={onClick}>
      <h2
        className={`text-2xl ml-4 font-semibold ${
          !disabled
            ? selected
              ? " text-mellon-primary-default"
              : " hover:text-mellon-primary-default"
            : " text-onyx-primary-20"
        }`}
      >
        Add
      </h2>
    </button>
  );
}

function CritiquesCollection({
  usersCritique,
  restOfCritiques,
  onRemoveUsersCritique,
  addCommentCallback,
  removeCommentCallback,
  addCritiqueLikeDislikeCallback,
  removeCritiqueLikeDislikeCallback,
  addCommentLikeDislikeCallback,
  removeCommentLikeDislikeCallback,
}) {
  //if users critique = null | undefined
  if (!usersCritique && restOfCritiques.length === 0) {
    return (
      <div className="flex flex-col justify-center items-center mb-32 mt-20">
        <h1 className="font-bold uppercase text-4xl text-onyx-tint">
          No critiques found
        </h1>
      </div>
    );
  }
  return (
    <div className="flex flex-col mx-10 my-16">
      {usersCritique && (
        <div>
          <CritiqueCard
            critique={usersCritique}
            isUsersCritique={true}
            onRemoveUsersCritique={onRemoveUsersCritique}
            addCritiqueLikeDislikeCallback={addCritiqueLikeDislikeCallback}
            removeCritiqueLikeDislikeCallback={
              removeCritiqueLikeDislikeCallback
            }
          />
          <CommentsSection
            critiqueId={usersCritique.id}
            comments={usersCritique.comments}
            addCommentCallback={addCommentCallback}
            removeCommentCallback={removeCommentCallback}
            addCommentLikeDislikeCallback={addCommentLikeDislikeCallback}
            removeCommentLikeDislikeCallback={removeCommentLikeDislikeCallback}
          />
        </div>
      )}
      {restOfCritiques.map((critique) => {
        return (
          <div key={critique.id}>
            <CritiqueCard
              critique={critique}
              isUsersCritique={false}
              addCritiqueLikeDislikeCallback={addCritiqueLikeDislikeCallback}
              removeCritiqueLikeDislikeCallback={
                removeCritiqueLikeDislikeCallback
              }
            />
            <CommentsSection
              critiqueId={critique.id}
              comments={critique.comments}
              addCommentCallback={addCommentCallback}
              removeCommentCallback={removeCommentCallback}
              addCommentLikeDislikeCallback={addCommentLikeDislikeCallback}
              removeCommentLikeDislikeCallback={
                removeCommentLikeDislikeCallback
              }
            />
          </div>
        );
      })}
    </div>
  );
}
