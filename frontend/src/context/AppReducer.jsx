export default (state, action) => {
  switch (action.type) {
    case "SET_SESSION_DATA":
      return {
        ...state,
        sessionData: {
          isLoggedIn: true,
          user: {
            firstName: action.payload.first_name,
            lastName: action.payload.last_name,
            gender: action.payload.gender,
            profileName: action.payload.profile_name,
            profileImageUrl: action.payload.profile_image_url,
            role: action.payload.role,
            country: {
              id: action.payload.country.id,
              name: action.payload.country.name,
              officialStateName: action.payload.country.official_state_name,
              code: action.payload.country.code,
            },
          },
        },
        //array of movie objects
        movieWatchlist: action.payload.library.movies,
        //array of tv show objects
        showWatchlist: action.payload.library.tv_shows,
        //array of critiqueIds (long values)
        mediaCritiques: action.payload.critiques,
        //array of commentIds (long values)
        critiquesComments: action.payload.comments,
        //array of objects, example:[{{"critique_id":1,"like":true},{"critique_id":2,"like":false}}]
        critiquesLikeDislikes: action.payload.critique_like_dislikes,
        //array of objects, example:[{{"comment_id":1,"like":true},{"comment_id":2,"like":false}}]
        commentsLikeDislikes: action.payload.comment_like_dislikes,
      };
    case "REMOVE_SESSION_DATA":
      return {
        ...state,
        sessionData: { isLoggedIn: false, user: null },
        movieWatchlist: [],
        showWatchlist: [],
        mediaCritiques: [],
        critiquesComments: [],
        critiquesLikeDislikes: [],
        commentsLikeDislikes: [],
      };
    case "ADD_MOVIE_TO_WATCHLIST":
      return {
        ...state,
        movieWatchlist: [action.payload, ...state.movieWatchlist],
      };
    case "REMOVE_MOVIE_FROM_WATCHLIST":
      return {
        ...state,
        movieWatchlist: state.movieWatchlist.filter(
          (movie) => movie.id !== action.payload
        ),
      };
    case "ADD_SHOW_TO_WATCHLIST":
      return {
        ...state,
        showWatchlist: [action.payload, ...state.showWatchlist],
      };
    case "REMOVE_SHOW_FROM_WATCHLIST":
      return {
        ...state,
        showWatchlist: state.showWatchlist.filter(
          (movie) => movie.id !== action.payload
        ),
      };
    case "ADD_CRITIQUE":
      return {
        ...state,
        mediaCritiques: [action.payload, ...state.mediaCritiques],
      };
    case "REMOVE_CRITIQUE":
      return {
        ...state,
        mediaCritiques: state.mediaCritiques.filter(
          (critiqueId) => critiqueId !== action.payload.critique_id
        ),
        critiquesLikeDislikes: state.critiquesLikeDislikes.filter(
          (likeDislike) =>
            likeDislike.critique_id !== action.payload.critique_id
        ),
        critiquesComments: action.payload.comment_id
          ? state.critiquesComments.filter(
              (commentId) => commentId !== action.payload.comment_id
            )
          : state.critiquesComments,
        commentsLikeDislikes: action.payload.comment_id
          ? state.commentsLikeDislikes.filter(
              (likeDislike) =>
                likeDislike.comment_id !== action.payload.comment_id
            )
          : state.commentsLikeDislikes,
      };
    case "ADD_CRITIQUE_LIKEDISLIKE":
      return {
        ...state,
        critiquesLikeDislikes: [action.payload, ...state.critiquesLikeDislikes],
      };
    case "UPDATE_CRITIQUE_LIKEDISLIKE":
      return {
        ...state,
        critiquesLikeDislikes: [
          action.payload,
          ...state.critiquesLikeDislikes.filter(
            (likeDislike) =>
              likeDislike.critique_id !== action.payload.critique_id
          ),
        ],
      };
    case "REMOVE_CRITIQUE_LIKEDISLIKE":
      return {
        ...state,
        critiquesLikeDislikes: [
          ...state.critiquesLikeDislikes.filter(
            (likeDislike) => likeDislike.critique_id !== action.payload
          ),
        ],
      };
    case "ADD_COMMENT":
      return {
        ...state,
        critiquesComments: [action.payload, ...state.critiquesComments],
      };
    case "REMOVE_COMMENT":
      return {
        ...state,
        critiquesComments: state.critiquesComments.filter(
          (commentId) => commentId !== action.payload
        ),
        commentsLikeDislikes: state.commentsLikeDislikes.filter(
          (likeDislike) => likeDislike.comment_id !== action.payload
        ),
      };
    case "ADD_COMMENT_LIKEDISLIKE":
      return {
        ...state,
        commentsLikeDislikes: [action.payload, ...state.commentsLikeDislikes],
      };
    case "UPDATE_COMMENT_LIKEDISLIKE":
      return {
        ...state,
        commentsLikeDislikes: [
          action.payload,
          ...state.commentsLikeDislikes.filter(
            (likeDislike) =>
              likeDislike.comment_id !== action.payload.comment_id
          ),
        ],
      };
    case "REMOVE_COMMENT_LIKEDISLIKE":
      return {
        ...state,
        commentsLikeDislikes: [
          ...state.commentsLikeDislikes.filter(
            (likeDislike) => likeDislike.comment_id !== action.payload
          ),
        ],
      };
    default:
      return state;
  }
};
