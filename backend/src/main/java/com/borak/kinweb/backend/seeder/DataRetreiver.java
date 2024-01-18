/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.seeder;

import com.borak.kinweb.backend.domain.classes.MyImage;
import com.borak.kinweb.backend.domain.enums.Gender;
import com.borak.kinweb.backend.seeder.domain.api.ApiGenre;
import com.borak.kinweb.backend.seeder.domain.api.ApiDiscoverMovie;
import com.borak.kinweb.backend.seeder.domain.api.ApiDetailsMovie;
import com.borak.kinweb.backend.seeder.domain.api.ApiDiscoverTV;
import com.borak.kinweb.backend.seeder.domain.api.ApiCreditsCast;
import com.borak.kinweb.backend.seeder.domain.api.ApiCreditsCrew;
import com.borak.kinweb.backend.seeder.domain.api.ApiDetailsTV;
import com.borak.kinweb.backend.seeder.domain.api.ApiTVCreator;
import com.borak.kinweb.backend.seeder.domain.db.ActingDB;
import com.borak.kinweb.backend.seeder.domain.db.ActingRoleDB;
import com.borak.kinweb.backend.seeder.domain.db.ActorDB;
import com.borak.kinweb.backend.seeder.domain.db.DirectorDB;
import com.borak.kinweb.backend.seeder.domain.db.GenreDB;
import com.borak.kinweb.backend.seeder.domain.db.MovieDB;
import com.borak.kinweb.backend.seeder.domain.db.TVShowDB;
import com.borak.kinweb.backend.seeder.domain.db.WriterDB;
import java.time.Year;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 * @author Mr. Poyo
 */
@Component
public class DataRetreiver {

    private static final String API_KEY_NAME = "api_key";
    private static final String API_KEY_VALUE = "61b1b3cee17aea2187f4384ff926a3da";
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String BASE_IMAGES_URL = "https://image.tmdb.org/t/p/";

    private static final Logger log = LoggerFactory.getLogger(DatabaseSeeder.class);
//===============================================================================================================
//===========================PUBLIC METGODS======================================================================
//===============================================================================================================   

    //returns all movie and tv genres grouped in one list without duplicates by its id
    List<GenreDB> getGenres() throws Exception {
        log.info("----->Retreiving genres...");
        String movieGenresUrl = BASE_URL + "genre/movie/list?" + API_KEY_NAME + "=" + API_KEY_VALUE;
        String tvGenresUrl = BASE_URL + "genre/tv/list?" + API_KEY_NAME + "=" + API_KEY_VALUE;
        List<GenreDB> movieGenres = ApiCaller.getInstance().getGenres(movieGenresUrl);
        List<GenreDB> tvGenres = ApiCaller.getInstance().getGenres(tvGenresUrl);
        return Stream.concat(movieGenres.stream(), tvGenres.stream()).distinct().collect(Collectors.toList());
    }

    //fetch movies
    List<MovieDB> getMovies() throws Exception {
        log.info("----->Retreiving movies...");
        int minPage = 1;
        final int maxPage = 3;
        int minYear = 1965;
        final int maxYear = Year.now().getValue() - 1;
        Map<String, String> discoverMoviesURLParams = new LinkedHashMap<>() {
            {
                put(API_KEY_NAME, API_KEY_VALUE);
                put("include_adult", "false");
                put("include_video", "false");
                put("language", "en-US");
                put("sort_by", "vote_count.desc");
                put("primary_release_year", "%d");
                put("page", "%d");
            }
        };

        String discoverURL = BASE_URL + "discover/movie?" + buildURLParams(discoverMoviesURLParams);
        List<Integer> allMoviesIDs = getMovieIdsByCriteria(discoverURL, minYear, maxYear, minPage, maxPage);
        String detailsURL = BASE_URL + "movie/%d?" + API_KEY_NAME + "=" + API_KEY_VALUE + "&append_to_response=credits";
        List<MovieDB> movies = new ArrayList<>();
        for (Integer id : allMoviesIDs) {
            String movieURL = String.format(detailsURL, id);
            ApiDetailsMovie details = ApiCaller.getInstance().getMovieDetails(movieURL);
            MovieDB movie = convertToMovieDB(details);
            movies.add(movie);
        }
        return movies;
    }

    //fetch shows
    List<TVShowDB> getTVShows() throws Exception {
        log.info("----->Retreiving tv shows...");
        int minPage = 1;
        final int maxPage = 3;
        int minYear = 2000;
        final int maxYear = Year.now().getValue() - 1;
        Map<String, String> discoverTVURLParams = new LinkedHashMap<>() {
            {
                put(API_KEY_NAME, API_KEY_VALUE);
                put("include_adult", "false");
                put("include_null_first_air_dates", "false");
                put("language", "en-US");
                put("sort_by", "vote_count.desc");
                put("first_air_date_year", "%d");
                put("page", "%d");
            }
        };
        String discoverURL = BASE_URL + "discover/tv?" + buildURLParams(discoverTVURLParams);
        List<Integer> allTVIds = getTVIdsByCriteria(discoverURL, minYear, maxYear, minPage, maxPage);
        String detailsURL = BASE_URL + "tv/%d?" + API_KEY_NAME + "=" + API_KEY_VALUE + "&append_to_response=credits";
        List<TVShowDB> shows = new ArrayList<>();
        for (Integer id : allTVIds) {
            String showURL = String.format(detailsURL, id);
            ApiDetailsTV details = ApiCaller.getInstance().getTVDetails(showURL);
            TVShowDB show = convertToTVShowDB(details);
            shows.add(show);
        }
        return shows;
    }

    MyImage getPosterImageAsMyImage(String posterImagePath) throws Exception {
        String url = BASE_IMAGES_URL + "original" + posterImagePath;
        String posterImageName = posterImagePath.substring(1);
        return ApiCaller.getInstance().getPosterImageAsMyImage(url, posterImageName);
    }

//===============================================================================================================
//===========================PRIVATE METGODS=====================================================================
//===============================================================================================================
    private String buildURLParams(Map<String, String> map) {
        StringBuilder urlParams = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (urlParams.length() != 0) {
                urlParams.append('&');
            }
            urlParams.append(entry.getKey());
            urlParams.append('=');
            urlParams.append(entry.getValue());
        }
        return urlParams.toString();
    }

    private List<Integer> getMovieIdsByCriteria(String urlPattern, int minYear, int maxYear, int minPage, int maxPage) throws Exception {
        List<ApiDiscoverMovie> allDiscoveredMovies = new LinkedList<>();
        for (int i = minYear; i <= maxYear; i++) {
            for (int j = minPage; j <= maxPage; j++) {
                String url = String.format(urlPattern, i, j);
                List<ApiDiscoverMovie> movies = ApiCaller.getInstance().getDiscoveredMovies(url);
                allDiscoveredMovies.addAll(movies);
            }
        }
        return allDiscoveredMovies.stream().map((entity) -> entity.getId()).distinct().collect(Collectors.toList());
    }

    private MovieDB convertToMovieDB(ApiDetailsMovie details) {
        MovieDB movie = new MovieDB();
        movie.setId((long) details.getId());
        movie.setTitle(details.getTitle());
        movie.setDescription(details.getOverview());
        movie.setReleaseDate(details.getReleaseDate());
        movie.setLength(details.getRuntime());
        movie.setAudienceRating((int) (Math.round(details.getVoteAverage() * 10)));
        movie.setCoverImagePath(details.getPosterPath());
        for (ApiGenre genre : details.getGenres()) {
            movie.getGenres().add(new GenreDB(genre.getId(), genre.getName()));
        }
        for (ApiCreditsCrew crew : details.getCredits().getCrew()) {
            DirectorDB director;
            WriterDB writer;
            String[] names;
            switch (crew.getDepartment()) {
                case "Directing":
                    director = new DirectorDB();
                    director.setId((long) crew.getId());
                    names = getFirstLastName(crew.getName());
                    director.setFirstName(names[0]);
                    director.setLastName(names[1]);
                    director.setGender(parseGender(crew.getGender()));
                    director.setProfilePhotoPath(crew.getProfilePath());
                    movie.getDirectors().add(director);
                    break;
                case "Writing":
                    writer = new WriterDB();
                    writer.setId((long) crew.getId());
                    names = getFirstLastName(crew.getName());
                    writer.setFirstName(names[0]);
                    writer.setLastName(names[1]);
                    writer.setGender(parseGender(crew.getGender()));
                    writer.setProfilePhotoPath(crew.getProfilePath());
                    movie.getWriters().add(writer);
                    break;
                default:
            }
        }
        int numberOfStars = 3;
        for (ApiCreditsCast cast : details.getCredits().getCast()) {
            ActorDB actor = new ActorDB();
            actor.setId((long) cast.getId());
            String[] names = getFirstLastName(cast.getName());
            actor.setFirstName(names[0]);
            actor.setLastName(names[1]);
            actor.setGender(parseGender(cast.getGender()));
            actor.setProfilePhotoPath(cast.getProfilePath());
            boolean isStarAndStarring = (numberOfStars-- > 0);
            actor.setStar(isStarAndStarring);

            ActingDB acting = new ActingDB();
            acting.setMedia(movie);
            acting.setActor(actor);
            acting.setStarring(isStarAndStarring);
            String[] roles = getRoles(cast.getCharacter());
            long roleId = 1;
            for (String role : roles) {
                acting.getRoles().add(new ActingRoleDB(acting, roleId++, role));
            }
            movie.getActings().add(acting);
        }
        return movie;
    }

    private TVShowDB convertToTVShowDB(ApiDetailsTV details) {
        TVShowDB show = new TVShowDB();
        show.setId((long) details.getId());
        show.setTitle(details.getName());
        show.setReleaseDate(details.getFirstAirDate());
        show.setDescription(details.getOverview());
        show.setNumberOfSeasons(details.getNumberOfSeasons());
        show.setAudienceRating((int) (Math.round(details.getVoteAverage() * 10)));
        show.setCoverImagePath(details.getPosterPath());
        for (ApiGenre genre : details.getGenres()) {
            show.getGenres().add(new GenreDB(genre.getId(), genre.getName()));
        }
        for (ApiTVCreator creator : details.getCreatedBy()) {
            DirectorDB director = new DirectorDB();
            director.setId((long) creator.getId());
            String[] names = getFirstLastName(creator.getName());
            director.setFirstName(names[0]);
            director.setLastName(names[1]);
            director.setGender(parseGender(creator.getGender()));
            director.setProfilePhotoPath(creator.getProfilePath());
            show.getDirectors().add(director);
        }
        for (ApiCreditsCrew crew : details.getCredits().getCrew()) {
            WriterDB writer;
            String[] names;
            switch (crew.getDepartment()) {
                case "Directing":
                    //do nothing
                    break;
                case "Writing":
                    writer = new WriterDB();
                    writer.setId((long) crew.getId());
                    names = getFirstLastName(crew.getName());
                    writer.setFirstName(names[0]);
                    writer.setLastName(names[1]);
                    writer.setGender(parseGender(crew.getGender()));
                    writer.setProfilePhotoPath(crew.getProfilePath());
                    show.getWriters().add(writer);
                    break;
                default:
            }
        }
        int numberOfStars = 3;
        for (ApiCreditsCast cast : details.getCredits().getCast()) {
            ActorDB actor = new ActorDB();
            actor.setId((long) cast.getId());
            String[] names = getFirstLastName(cast.getName());
            actor.setFirstName(names[0]);
            actor.setLastName(names[1]);
            actor.setGender(parseGender(cast.getGender()));
            actor.setProfilePhotoPath(cast.getProfilePath());
            boolean isStarAndStarring = (numberOfStars-- > 0);
            actor.setStar(isStarAndStarring);

            ActingDB acting = new ActingDB();
            acting.setMedia(show);
            acting.setActor(actor);
            acting.setStarring(isStarAndStarring);
            String[] roles = getRoles(cast.getCharacter());
            long roleId = 1;
            for (String role : roles) {
                acting.getRoles().add(new ActingRoleDB(acting, roleId++, role));
            }
            show.getActings().add(acting);
        }
        return show;
    }

    private String[] getFirstLastName(String name) {
        String[] names = name.split(" ", 2);
        String firstName = names[0];
        String lastName = names.length > 1 ? names[1] : "";
        return new String[]{firstName.trim(), lastName.trim()};
    }

    private Gender parseGender(int gender) {
        return switch (gender) {
            case 1 ->
                Gender.FEMALE;
            case 2 ->
                Gender.MALE;
            default ->
                Gender.OTHER;
        };
    }

    private String[] getRoles(String character) {
        return character.split(" / ");
    }

    private List<Integer> getTVIdsByCriteria(String urlPattern, int minYear, int maxYear, int minPage, int maxPage) throws Exception {
        List<ApiDiscoverTV> allDiscoveredTV = new LinkedList<>();
        for (int i = minYear; i <= maxYear; i++) {
            for (int j = minPage; j <= maxPage; j++) {
                String url = String.format(urlPattern, i, j);
                List<ApiDiscoverTV> tv = ApiCaller.getInstance().getDiscoveredTV(url);
                allDiscoveredTV.addAll(tv);
            }
        }
        return allDiscoveredTV.stream().map((entity) -> entity.getId()).distinct().collect(Collectors.toList());
    }

}
