package com.core.jdbc;

import com.core.model.Movie;
import com.core.model.Science;

import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public class ScienceTools {
    public void add() throws Exception {
        String file = "E:\\code\\ecom-review-core\\src\\main\\java\\com\\core\\jdbc\\科学.txt";
        Charset charset = Charset.forName("UTF-8");
        BuildContent content = new BuildContent();
        List<Science> scienceList = content.read2(file,charset);
        Connection conn = JDBCTools.getConnection("root","123456","jdbc:mysql://localhost:3306/solr?useUnicode=true&amp;characterEncoding=utf-8","com.mysql.jdbc.Driver");
        PreparedStatement ps = conn.prepareStatement("insert into science value (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
        for (Science science : scienceList) {
            ps.setInt(1,science.getId());
            ps.setString(2,science.getAgency());
            ps.setString(3,science.getCenter());
            ps.setString(4,science.getCenterStatus());
            ps.setString(5,science.getFacility());
            ps.setInt(6,science.getOccupied());
            ps.setString(7,science.getStatus());
            ps.setString(8,science.getLink());
            ps.setObject(9,science.getRecordDate());
            ps.setObject(10,science.getLastUpdate());
            ps.setString(11,science.getAddress());
            ps.setString(12,science.getCity());
            ps.setString(13,science.getState());
            ps.setString(14,science.getZip());
            ps.setString(15,science.getCountry());
            ps.setString(16,science.getContact());
            ps.setString(17,science.getMailStop());
            ps.setString(18,science.getPhone());
            ps.executeUpdate();
        }
    }

    public static void main(String[] args) {
        try {
            new ScienceTools().add();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
