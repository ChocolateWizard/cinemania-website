import React, { useContext, useState } from "react";
import { GlobalContext } from "../../../context/GlobalState";
import CommentAddForm from "./CommentAddForm";
import CommentCard from "./card/CommentCard";
import CardLoader from "../../helpers/loaders/cardLoader/CardLoader";
import { postComment,deleteComment } from "../../../utils/Api";
import { toast } from "react-toastify";

export default function CommentsSection({
  critiqueId,
  comments,
  addCommentCallback,
  removeCommentCallback,
  addCommentLikeDislikeCallback,
  removeCommentLikeDislikeCallback,
}) {
  const { sessionData, addComment,removeComment } = useContext(GlobalContext);
  const [loading, setLoading] = useState(false);
  const userAuthData = {
    isLoggedIn: sessionData.isLoggedIn,
    role: sessionData.user?.role,
    profileName: sessionData.user?.profileName,
  };

  //its null|undefined if it doesnt exist
  const [usersComment, setUsersComment] = useState(
    comments.find((comment) => {
      return comment.user.profile_name === userAuthData.profileName;
    })
  );

  //same array as comments if none found by filter
  const restOfComments = comments.filter(
    (comment) => comment.user.profile_name !== userAuthData.profileName
  );

  function onAddUsersComment(critiqueId, content) {
    setLoading(true);
    postComment(
      critiqueId,
      JSON.stringify({
        content: content,
      })
    )
      .then((res) => {
        addComment(res.data.id);
        res.data.number_of_likes = 0;
        res.data.number_of_dislikes = 0;
        setUsersComment(res.data);
        addCommentCallback(critiqueId,res.data);
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
      .finally(() => {
        setLoading(false);
      });
  }
  function onRemoveUsersComment(critiqueId,commentId) {   
    setLoading(true);
    deleteComment(critiqueId, commentId)
      .then((res) => {
        removeComment(commentId);
        setUsersComment(null);
        removeCommentCallback(critiqueId, commentId);
        toast.success("Successfully deleted comment");
        
      })
      .catch((err) => {
        console.error(err);
        toast.error("Unable to delete comment");
      })
      .finally(() => setLoading(false));
  }


  if (loading) {
    return <CardLoader />;
  }
  return (
    <div className={`flex flex-row`}>
      <div className="w-[50%]"></div>
      <div className="flex flex-col w-[50%]">
        {userAuthData.isLoggedIn &&
          userAuthData.role !== "CRITIC" &&
          !usersComment && (
            <CommentAddForm
              critiqueId={critiqueId}
              onAddUsersComment={onAddUsersComment}
            />
          )}
        <CommentsCollection
          critiqueId={critiqueId}
          usersComment={usersComment}
          restOfComments={restOfComments}
          onRemoveUsersComment={onRemoveUsersComment}
          addCommentLikeDislikeCallback={addCommentLikeDislikeCallback}
          removeCommentLikeDislikeCallback={removeCommentLikeDislikeCallback}         
        />
      </div>
    </div>
  );
}

function CommentsCollection({
  critiqueId,
  usersComment,
  restOfComments,
  onRemoveUsersComment,
  addCommentLikeDislikeCallback,
  removeCommentLikeDislikeCallback
}) {
  return (
    <div className="flex flex-col">
      {usersComment && (
        <CommentCard
          critiqueId={critiqueId}
          comment={usersComment}
          isUsersComment={true}       
          onRemoveUsersComment={onRemoveUsersComment}
          addCommentLikeDislikeCallback={addCommentLikeDislikeCallback}
          removeCommentLikeDislikeCallback={removeCommentLikeDislikeCallback}
        />
      )}
      {restOfComments.map((comment) => (
        <CommentCard
          key={comment.id}
          critiqueId={critiqueId}
          comment={comment}
          isUsersComment={false}
          addCommentLikeDislikeCallback={addCommentLikeDislikeCallback}
          removeCommentLikeDislikeCallback={removeCommentLikeDislikeCallback}
        />
      ))}
    </div>
  );
}
