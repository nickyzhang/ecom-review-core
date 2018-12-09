package com.core.search.lucene.tools;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class BuildDocTools {
    public static List<Document> buildDocs(File srcFile, String[] fields, int[] stored,
                                           String regex, Charset charset){
        List<Document> docList = new ArrayList<Document>();
        try {
            List<String> lines = FileUtils.readLines(srcFile,charset);
            if (CollectionUtils.isEmpty(lines)) return null;
            Document document = null;
            String[] words = null;
            for (String line : lines) {
                document = new Document();
                words = line.split(regex);
                for (int i = 0; i < words.length; i++) {
                    Field field = null;
                    if (stored[i] == 1)
                        field = new StringField(fields[i],words[i],Field.Store.YES);
                    else
                        field = new TextField(fields[i],words[i],Field.Store.YES);
                    document.add(field);
                }
                docList.add(document);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return docList;
    }

    public static List<Film> buildFilmList(File srcFile, String[] fields, int[] stored,
                                           String regex, Charset charset){
        List<Film> filmList = new ArrayList<Film>();
        try {
            List<String> lines = FileUtils.readLines(srcFile,charset);
            if (CollectionUtils.isEmpty(lines)) return null;
            Film film = null;
            String[] words = null;
            for (String line : lines) {
                film = new Film();
                words = line.split(regex);
                for (int i = 0; i < words.length; i++) {
                    film.setId(words[0]);
                    film.setFilm(words[1]);
                    film.setDirector(words[2]);
                    film.setCountry(words[3]);
                    film.setReleaseDate(words[4]);
                    film.setRunTime(words[5]);
                    film.setScore(words[6]);
                    film.setReview(words[7]);
                    film.setType(words[8]);
                    film.setArea(words[9]);
                }
                filmList.add(film);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filmList;
    }

    public static class Film{
        String id;
        String film;
        String director;
        String country;
        String releaseDate;
        String runTime;
        String score;
        String review;
        String type;
        String area;

        public Film() {
        }

        public Film(String id, String film, String director, String country, String releaseDate, String runTime, String score, String review, String type, String area) {
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

        public String getRunTime() {
            return runTime;
        }

        public void setRunTime(String runTime) {
            this.runTime = runTime;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getReview() {
            return review;
        }

        public void setReview(String review) {
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
}
