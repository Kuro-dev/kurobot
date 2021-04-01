package org.kurodev.discord.command.guild.standard.nasa.json;

public class RoverImage {
    private final Camera camera;
    private final String img_src;
    private final String earth_date;
    private final RoverData rover;

    public RoverImage(Camera camera, String img_src, String earth_date, RoverData rover) {
        this.camera = camera;
        this.img_src = img_src;
        this.earth_date = earth_date;
        this.rover = rover;
    }

    @Override
    public String toString() {
        return "RoverImage{" +
                "camera=" + camera +
                ", img_src='" + img_src + '\'' +
                ", earth_date='" + earth_date + '\'' +
                ", rover=" + rover +
                '}';
    }

    public Camera getCamera() {
        return camera;
    }

    public String getImgSrc() {
        return img_src;
    }

    public String getEarthDate() {
        return earth_date;
    }

    public RoverData getRover() {
        return rover;
    }
}
