package com.core.model;

public class Movie {
    String id;
    String film;
    String director;
    String country;
    String releaseDate;
    Integer runTime;
    Float score;
    Integer review;
    String type;
    String area;

    public Movie() {
    }

    public Movie(String id, String film, String director, String country, String releaseDate, Integer runTime, Float score, Integer review, String type, String area) {
        this.id = id;
        this.film = film;
        this.director = director;
        this.country = country;
        this.releaseDate = releaseDate;
        this.runTime = runTime;
        this.score = score;
        this.review = review;
        this.type = type;
        this.area = area;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFilm() {
        return film;
    }

    public void setFilm(String film) {
        this.film = film;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Integer getRunTime() {
        return runTime;
    }

    public void setRunTime(Integer runTime) {
        this.runTime = runTime;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public Integer getReview() {
        return review;
    }

    public void setReview(Integer review) {
        this.review = review;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
