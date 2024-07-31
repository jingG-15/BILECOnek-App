package com.holanda.bilecoagmaonlineregistration2021.Adapters;

import java.io.Serializable;


public class sign_chooser_var_link implements Serializable {

    private String image_name;
    private String image_path;

    public String getImage_name() {
        return image_name;
    }

    public String getImage_path() {
        return image_path;
    }

    public sign_chooser_var_link(String image_name, String image_path) {
        this.image_name = image_name;
        this.image_path = image_path;
    }
}
