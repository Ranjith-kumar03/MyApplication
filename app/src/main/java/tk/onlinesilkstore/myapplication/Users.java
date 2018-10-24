package tk.onlinesilkstore.myapplication;

import android.widget.Toast;

public class Users {

    public String name;
    public String status;
    public String thump_image;


    public Users()
{

}

    public Users(String name, String status, String image, String thumb_image) {
        this.name = name;
        this.status = status;
        this.thump_image= thumb_image;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getThump_image() {
        return thump_image;
    }

    public void setThump_image(String thump_image) {
        this.thump_image = thump_image;
    }




}
