import React from "react";
import { Input } from "@material-tailwind/react";

export default function CommentAddForm({ critiqueId, onAddUsersComment }) {
  function handleAddComment(e) {
    if (e.key === "Enter") {
      onAddUsersComment(critiqueId, e.target.value);
    }
  }
  return (
    <Input
      onKeyDown={handleAddComment}
      variant="standard"
      label="Add comment"
      color="amber"
    />
  );
}
