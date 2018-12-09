package com.core.jdbc;

import com.core.model.Movie;
import com.core.model.Science;
import com.core.search.lucene.tools.BuildDocTools;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BuildContent {
    public List<Movie> read(String file, Charset charset) {
        List<Movie> movieList = new ArrayList<Movie>();
        File srcFile = FileUtils.getFile(file);
        try {
            List<String> lines = FileUtils.readLines(srcFile,charset);
            if (CollectionUtils.isEmpty(lines)) return null;
            Movie movie = null;
            String[] words = null;
            for (String line : lines) {
                movie = new Movie();
                words = line.split("\t");
                for (int i = 0; i < words.length; i++) {
                    movie.setId(words[0]);
                    movie.setFilm(words[1]);
                    movie.setDirector(words[2]);
                    movie.setCountry(words[3]);
                    movie.setReleaseDate(words[4]);
                    movie.setRunTime(Integer.parseInt(words[5]));
                    movie.setScore(Float.parseFloat(words[6]));
                    movie.setReview(Integer.parseInt(words[7]));
                    movie.setType(words[8]);
                    movie.setArea(words[9]);
                }
                movieList.add(movie);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return movieList;
    }


    public List<Science> read2(String file, Charset charset) {
        List<Science> movieList = new ArrayList<Science>();
        File srcFile = FileUtils.getFile(file);
        Random r = new Random();

        try {
            List<String> lines = FileUtils.readLines(srcFile,charset);
            if (CollectionUtils.isEmpty(lines)) return null;
            Science science = null;
            String[] words = null;
            for (int i = 0; i < lines.size(); i++) {
                Integer occupied  = 0;
                LocalDate recordDate = null;
                LocalDate lastUpdate = null;
                words = lines.get(i).split(",");
                if (words[4] != null && !words[4].equals("")) {
                    occupied = Integer.parseInt(words[4]);
                }

                if (words[7] != null && !words[7].equals("")) {
                    if (words[7].equals("300 E St SW"))
                        continue;
                    DateTimeFormatter format = DateTimeFormatter.ofPattern("M/d/yyyy");
                    recordDate = LocalDate.parse(words[7],format);
                }
                if (words[8] != null && !words[8].equals("")) {
                    if (words[8].equals("300 E St SW"))
                        continue;
                    DateTimeFormatter format = DateTimeFormatter.ofPattern("M/d/yyyy");
                    lastUpdate = LocalDate.parse(words[8],format);
                }
                science = new Science(i,words[0],words[1],words[2],words[3],occupied,words[5],words[6],
                        recordDate,lastUpdate,words[9],words[10],words[11],words[12],words[13],words[14],
                        words[15],words[16]);
                movieList.add(science);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return movieList;
    }
}
