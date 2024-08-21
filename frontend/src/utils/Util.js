
export function concatGenreNames(genres, delimiter) {
  let gs = "";
  for (let i = 0; i < genres.length; i++) {
    if (gs.length === 0) {
      gs = genres[i].name;
      continue;
    }
    gs += delimiter + genres[i].name;
  }
  return gs;
}

export function concatDirectorNames(directors, delimiter, number) {
  let ds = "";
  for (let i = 0; i < directors.length && i < number; i++) {
    if (ds.length === 0) {
      ds = directors[i].first_name + " " + directors[i].last_name;
      continue;
    }
    ds += delimiter + directors[i].first_name + " " + directors[i].last_name;
  }
  return ds;
}

export function concatWriterNames(writers, delimiter, number) {
  let ws = "";
  for (let i = 0; i < writers.length && i < number; i++) {
    if (ws.length === 0) {
      ws = writers[i].first_name + " " + writers[i].last_name;
      continue;
    }
    ws += delimiter + writers[i].first_name + " " + writers[i].last_name;
  }
  return ws;
}

export function concatActorRoleNames(roles, delimiter, number) {
  let rs = "";
  for (let i = 0; i < roles.length && i < number; i++) {
    if (rs.length === 0) {
      rs = roles[i].name;
      continue;
    }
    rs += delimiter + roles[i].name;
  }
  return rs;
}

//checks if any commentId from commentIds array is present as an id in comments array
//if true returns that id
//returns null if none found
export function findMatchingId(commentIds, comments) {
  for (let commentId of commentIds) {
    if (comments.some(comment => comment.id === commentId)) {
      return commentId;
    }
  }
  return null;
}


