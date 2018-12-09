package com.core.jdbc;

import com.core.model.Movie;

import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public class MovieTools {
    public void add() throws Exception {
        String file = "E:\\code\\ecom-review-core\\src\\main\\java\\com\\core\\jdbc\\film.txt";
        Charset charset = Charset.forName("GBK");
        BuildContent content = new BuildContent();
        List<Movie> movieList = content.read(file,charset);
        Connection conn = JDBCTools.getConnection("root","123456","jdbc:mysql://192.168.99.151:3306/solr?useUnicode=true&amp;characterEncoding=utf-8","com.mysql.jdbc.Driver");
        PreparedStatement ps = conn.prepareStatement("insert into movie value (?,?,?,?,?,?,?,?,?,?)");
        for (Movie movie : movieList) {
            ps.setString(1,movie.getId());
            ps.setString(2,movie.getFilm());
            ps.setString(3,movie.getDirector());
            ps.setString(4,movie.getCountry());
            ps.setString(5,movie.getReleaseDate());
            ps.setInt(6,movie.getRunTime());
            ps.setFloat(7,movie.getScore());
            ps.setInt(8,movie.getReview());
            ps.setString(9,movie.getType());
            ps.setString(10,movie.getArea());
            ps.executeUpdate();
        }
    }

    public static void main(String[] args) {
        try {
            new MovieTools().add();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
