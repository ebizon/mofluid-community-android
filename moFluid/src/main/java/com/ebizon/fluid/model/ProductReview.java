package com.ebizon.fluid.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by piyush-ios on 19/5/16.
 */
public class ProductReview {

    String create_date;
    String detail;
    String title;
    String nickname;



    public String getCreate_date() {
        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try
        {
            Date d = format.parse(create_date);
            SimpleDateFormat outputFormatter = new SimpleDateFormat("yyyy-MM-dd");
            String output = outputFormatter.format(d); // Output : 01/20/2012
            return output;
        }
        catch (Exception e)
        {
            e.printStackTrace();
return create_date;
        }

    }


    public String getDetail() {
        return detail;
    }

    public String getTitle() {
        return title;
    }

    public String getNickname() {
        return nickname;
    }

    public ArrayList<ReviewVote> getVotes() {
        return votes;
    }

    public ProductReview(String create_date,String detail, String title, String nickname, ArrayList<ReviewVote> votes) {

        this.create_date = create_date;
        this.detail = detail;
        this.title = title;
        this.nickname = nickname;
        this.votes = votes;
    }

    ArrayList<ReviewVote> votes;

    public ProductReview() {
        this.votes = new ArrayList<>();
    }

    public float getVoteAverage()
    {
        int total = 0;
        for (int i = 0; i <this.votes.size() ; i++) {
            total += this.votes.get(i).getValue();
        }
        return (float)(total/this.votes.size());
    }




}
