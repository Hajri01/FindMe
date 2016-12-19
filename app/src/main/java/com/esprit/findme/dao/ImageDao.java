package com.esprit.findme.dao;

import com.esprit.findme.models.Image;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Med-Amine on 29/11/2016.
 */

public class ImageDao {

    public void getImages(List<Image> images, String jsonResponse) {
        try {
            JSONObject jo = new JSONObject(jsonResponse);
            JSONArray array = jo.getJSONArray("images");
            for (int i = 0; i < array.length(); i++) {
                JSONObject j = array.getJSONObject(i);
                Image image = new Image(j);
                images.add(image);
/*                ObjectMapper mapper = new ObjectMapper();
                try {
                    Card card = mapper.readValue(j.toString(),Card.class);
                    cards.add(card);

                    String jsonCard = mapper.writeValueAsString(card);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
