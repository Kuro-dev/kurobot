package org.kurodev.discord.command.guild.standard.nasa.json;

public class RoverData {
    private final String name, landing_date, launch_date, status;

    public RoverData(String name, String landing_date, String launch_date, String status) {
        this.name = name;
        this.landing_date = landing_date;
        this.launch_date = launch_date;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getLandingDate() {
        return landing_date;
    }

    public String getLaunchDate() {
        return launch_date;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "RoverData{" +
                "name='" + name + '\'' +
                ", landing_date='" + landing_date + '\'' +
                ", launch_date='" + launch_date + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
