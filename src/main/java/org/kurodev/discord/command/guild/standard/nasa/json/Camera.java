package org.kurodev.discord.command.guild.standard.nasa.json;

public class Camera {
    private final String name, full_name;

    public Camera(String name, String full_name) {
        this.name = name;
        this.full_name = full_name;
    }

    @Override
    public String toString() {
        return "Camera{" +
                "name='" + name + '\'' +
                ", full_name='" + full_name + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public String getFull_name() {
        return full_name;
    }
}
