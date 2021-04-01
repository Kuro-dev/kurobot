package org.kurodev.discord.command.guild.standard.nasa.json;

import java.util.List;

public class RoverImages {
    private final List<RoverImage> photos;

    public RoverImages(List<RoverImage> photos) {
        this.photos = photos;
    }

    public List<RoverImage> getPhotos() {
        return photos;
    }
}
