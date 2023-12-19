/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.kinweb.backend.domain.dto.tv;

import com.borak.kinweb.backend.domain.dto.DTO;
import com.borak.kinweb.backend.domain.enums.Gender;
import com.borak.kinweb.backend.logic.transformers.serializers.views.JsonVisibilityViews;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mr. Poyo
 */
@JsonPropertyOrder({
    "id",
    "title",
    "releaseDate",
    "coverImageUrl",
    "description",
    "audienceRating",
    "criticsRating",
    "numberOfSeasons",
    "genres", "directors", "writers", "actors", "critiques"})
public class TVShowResponseDTO implements DTO {

    @JsonView(JsonVisibilityViews.Lite.class)
    private Long id;

    @JsonView(JsonVisibilityViews.Lite.class)
    private String title;

    @JsonView(JsonVisibilityViews.Lite.class)
    @JsonProperty(value = "cover_image_url")
    private String coverImageUrl;

    @JsonView(JsonVisibilityViews.Medium.class)
    private String description;

    @JsonView(JsonVisibilityViews.Lite.class)
    @JsonProperty(value = "release_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "d/MM/yyyy")
    private LocalDate releaseDate;

    @JsonView(JsonVisibilityViews.Medium.class)
    @JsonProperty(value = "number_of_seasons")
    private Integer numberOfSeasons;

    @JsonView(JsonVisibilityViews.Lite.class)
    @JsonProperty(value = "audience_rating")
    private Integer audienceRating;

    @JsonView(JsonVisibilityViews.Medium.class)
    @JsonProperty(value = "critics_rating")
    private Integer criticsRating;

    @JsonView(JsonVisibilityViews.Lite.class)
    private List<Genre> genres = new ArrayList<>();

    @JsonView(JsonVisibilityViews.Heavy.class)
    private List<Critique> critiques = new ArrayList<>();

    @JsonView(JsonVisibilityViews.Heavy.class)
    private List<Director> directors = new ArrayList<>();

    @JsonView(JsonVisibilityViews.Heavy.class)
    private List<Writer> writers = new ArrayList<>();

    @JsonView(JsonVisibilityViews.Heavy.class)
    private List<Actor> actors = new ArrayList<>();

    public TVShowResponseDTO() {
    }

    public TVShowResponseDTO(Long id, String title, String coverImageUrl, String description, LocalDate releaseDate, Integer numberOfSeasons, Integer audienceRating, Integer criticsRating) {
        this.id = id;
        this.title = title;
        this.coverImageUrl = coverImageUrl;
        this.description = description;
        this.releaseDate = releaseDate;
        this.numberOfSeasons = numberOfSeasons;
        this.audienceRating = audienceRating;
        this.criticsRating = criticsRating;
    }

    @JsonView(JsonVisibilityViews.Lite.class)
    @JsonPropertyOrder({"id", "name"})
    public static class Genre {

        private Long id;
        private String name;

        public Genre() {
        }

        public Genre(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    @JsonView(JsonVisibilityViews.Heavy.class)
    @JsonPropertyOrder({"critic", "rating", "description"})
    public static class Critique {

        private Critic critic;
        private Integer rating;
        private String description;

        public Critique() {
        }

        public Critique(Critic critic, Integer rating, String description) {
            this.critic = critic;
            this.rating = rating;
            this.description = description;
        }

        @JsonView(JsonVisibilityViews.Heavy.class)
        @JsonPropertyOrder({"username", "profileImageUrl"})
        public static class Critic {

            private String username;

            @JsonProperty(value = "profile_image_url")
            private String profileImageUrl;

            public Critic() {
            }

            public Critic(String username, String profileImageUrl) {
                this.username = username;
                this.profileImageUrl = profileImageUrl;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getProfileImageUrl() {
                return profileImageUrl;
            }

            public void setProfileImageUrl(String profileImageUrl) {
                this.profileImageUrl = profileImageUrl;
            }

        }

        public Critic getCritic() {
            return critic;
        }

        public void setCritic(Critic critic) {
            this.critic = critic;
        }

        public Integer getRating() {
            return rating;
        }

        public void setRating(Integer rating) {
            this.rating = rating;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

    }

    @JsonView(JsonVisibilityViews.Heavy.class)
    @JsonPropertyOrder({"id", "firstName", "lastName", "profilePhotoUrl", "gender"})
    public static class Director {

        private Long id;

        @JsonProperty(value = "first_name")
        private String firstName;

        @JsonProperty(value = "last_name")
        private String lastName;

        @JsonProperty(value = "profile_photo_url")
        private String profilePhotoUrl;

        private Gender gender;

        public Director() {
        }

        public Director(Long id, String firstName, String lastName, String profilePhotoUrl, Gender gender) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.profilePhotoUrl = profilePhotoUrl;
            this.gender = gender;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getProfilePhotoUrl() {
            return profilePhotoUrl;
        }

        public void setProfilePhotoUrl(String profilePhotoUrl) {
            this.profilePhotoUrl = profilePhotoUrl;
        }

        public Gender getGender() {
            return gender;
        }

        public void setGender(Gender gender) {
            this.gender = gender;
        }

    }

    @JsonView(JsonVisibilityViews.Heavy.class)
    @JsonPropertyOrder({"id", "firstName", "lastName", "profilePhotoUrl", "gender"})
    public static class Writer {

        private Long id;

        @JsonProperty(value = "first_name")
        private String firstName;

        @JsonProperty(value = "last_name")
        private String lastName;

        @JsonProperty(value = "profile_photo_url")
        private String profilePhotoUrl;

        private Gender gender;

        public Writer() {
        }

        public Writer(Long id, String firstName, String lastName, String profilePhotoUrl, Gender gender) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.profilePhotoUrl = profilePhotoUrl;
            this.gender = gender;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getProfilePhotoUrl() {
            return profilePhotoUrl;
        }

        public void setProfilePhotoUrl(String profilePhotoUrl) {
            this.profilePhotoUrl = profilePhotoUrl;
        }

        public Gender getGender() {
            return gender;
        }

        public void setGender(Gender gender) {
            this.gender = gender;
        }

    }

    @JsonView(JsonVisibilityViews.Heavy.class)
    @JsonPropertyOrder({"id", "firstName", "lastName", "profilePhotoUrl", "gender", "star", "starring", "roles"})
    public static class Actor {

        private Long id;

        @JsonProperty(value = "first_name")
        private String firstName;

        @JsonProperty(value = "last_name")
        private String lastName;

        @JsonProperty(value = "profile_photo_url")
        private String profilePhotoUrl;

        private Gender gender;

        @JsonProperty(value = "is_star")
        private Boolean star;

        @JsonProperty(value = "is_starring")
        private Boolean starring;

        private List<Role> roles = new ArrayList<>();

        public Actor() {
        }

        public Actor(Long id, String firstName, String lastName, String profilePhotoUrl, Gender gender, Boolean star, Boolean starring) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.profilePhotoUrl = profilePhotoUrl;
            this.gender = gender;
            this.star = star;
            this.starring = starring;
        }

        @JsonView(JsonVisibilityViews.Heavy.class)
        @JsonPropertyOrder({"id", "name"})
        public static class Role {

            private Long id;

            private String name;

            public Role() {
            }

            public Role(Long id, String name) {
                this.id = id;
                this.name = name;
            }

            public Long getId() {
                return id;
            }

            public void setId(Long id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getProfilePhotoUrl() {
            return profilePhotoUrl;
        }

        public void setProfilePhotoUrl(String profilePhotoUrl) {
            this.profilePhotoUrl = profilePhotoUrl;
        }

        public Gender getGender() {
            return gender;
        }

        public void setGender(Gender gender) {
            this.gender = gender;
        }

        public Boolean getStar() {
            return star;
        }

        public void setStar(Boolean star) {
            this.star = star;
        }

        public Boolean getStarring() {
            return starring;
        }

        public void setStarring(Boolean starring) {
            this.starring = starring;
        }

        public List<Role> getRoles() {
            return roles;
        }

        public void setRoles(List<Role> roles) {
            if (roles == null) {
                this.roles = new ArrayList<>();
            } else {
                this.roles = roles;
            }
        }

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Integer getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public void setNumberOfSeasons(Integer numberOfSeasons) {
        this.numberOfSeasons = numberOfSeasons;
    }

    public Integer getAudienceRating() {
        return audienceRating;
    }

    public void setAudienceRating(Integer audienceRating) {
        this.audienceRating = audienceRating;
    }

    public Integer getCriticsRating() {
        return criticsRating;
    }

    public void setCriticsRating(Integer criticsRating) {
        this.criticsRating = criticsRating;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        if (genres == null) {
            this.genres = new ArrayList<>();
        } else {
            this.genres = genres;
        }
    }

    public List<Critique> getCritiques() {
        return critiques;
    }

    public void setCritiques(List<Critique> critiques) {
        if (critiques == null) {
            this.critiques = new ArrayList<>();
        } else {
            this.critiques = critiques;
        }
    }

    public List<Director> getDirectors() {
        return directors;
    }

    public void setDirectors(List<Director> directors) {
        if (directors == null) {
            this.directors = new ArrayList<>();
        } else {
            this.directors = directors;
        }
    }

    public List<Writer> getWriters() {
        return writers;
    }

    public void setWriters(List<Writer> writers) {
        if (writers == null) {
            this.writers = new ArrayList<>();
        } else {
            this.writers = writers;
        }
    }

    public List<Actor> getActors() {
        return actors;
    }

    public void setActors(List<Actor> actors) {
        if (actors == null) {
            this.actors = new ArrayList<>();
        } else {
            this.actors = actors;
        }
    }

}
