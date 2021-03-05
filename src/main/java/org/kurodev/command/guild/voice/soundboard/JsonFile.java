package org.kurodev.command.guild.voice.soundboard;

/**
 * @author kuro
 **/
public class JsonFile {
    private final String slug;
    private final String title;
    private final String url;
    private final String name;

    public JsonFile(String slug, String title, String url, String name) {
        this.slug = slug;
        this.title = title;
        this.url = url;
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "JsonFile{" +
                "slug='" + slug + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}

