package br.com.jkavdev.fullcycle.catalogo.infrastructure.video.persistence;

import br.com.jkavdev.fullcycle.catalogo.domain.video.Video;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.util.Set;

@Document(indexName = "videos")
public class VideoDocument {

    @Id
    private String id;

    @MultiField(
            mainField = @Field(type = FieldType.Text, name = "title"),
            otherFields = @InnerField(suffix = "keyword", type = FieldType.Keyword)
    )
    private String title;

    @Field(type = FieldType.Text, name = "description")
    private String description;

    @Field(type = FieldType.Integer, name = "launched_at")
    private Integer launchedAt;

    @Field(type = FieldType.Double, name = "duration")
    private Double duration;

    @Field(type = FieldType.Keyword, name = "rating")
    private String rating;

    @Field(type = FieldType.Boolean, name = "opened")
    private boolean opened;

    @Field(type = FieldType.Boolean, name = "published")
    private boolean published;

    @Field(type = FieldType.Date, name = "created_at")
    private String createdAt;

    @Field(type = FieldType.Date, name = "updated_at")
    private String updatedAt;

    @Field(type = FieldType.Keyword, name = "video")
    private String video;

    @Field(type = FieldType.Keyword, name = "trailer")
    private String trailer;

    @Field(type = FieldType.Keyword, name = "banner")
    private String banner;

    @Field(type = FieldType.Keyword, name = "thumbnail")
    private String thumbnail;

    @Field(type = FieldType.Keyword, name = "thumbnail_half")
    private String thumbnailHalf;

    @Field(type = FieldType.Keyword, name = "cast_members")
    private Set<String> castMembers;

    @Field(type = FieldType.Keyword, name = "categories")
    private Set<String> categories;

    @Field(type = FieldType.Keyword, name = "genres")
    private Set<String> genres;

    private VideoDocument(
            final String id,
            final String title,
            final String description,
            final Integer launchedAt,
            final Double duration,
            final String rating,
            final boolean opened,
            final boolean published,
            final String createdAt,
            final String updatedAt,
            final String video,
            final String trailer,
            final String thumbnail,
            final String thumbnailHalf,
            final String banner,
            final Set<String> castMembers,
            final Set<String> categories,
            final Set<String> genres
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.launchedAt = launchedAt;
        this.duration = duration;
        this.rating = rating;
        this.opened = opened;
        this.published = published;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.video = video;
        this.trailer = trailer;
        this.banner = banner;
        this.thumbnail = thumbnail;
        this.thumbnailHalf = thumbnailHalf;
        this.castMembers = castMembers;
        this.categories = categories;
        this.genres = genres;
    }

    public static VideoDocument from(final Video video) {
        return new VideoDocument(
                video.id(),
                video.title(),
                video.description(),
                video.launchedAt().getValue(),
                video.duration(),
                video.rating().getName(),
                video.opened(),
                video.published(),
                video.createdAt().toString(),
                video.updatedAt().toString(),
                video.video(),
                video.trailer(),
                video.thumbnail(),
                video.thumbnailHalf(),
                video.banner(),
                video.castMembers(),
                video.categories(),
                video.genres()
        );
    }

    public Video toVideo() {
        return Video.with(
                id(),
                title(),
                description(),
                launchedAt(),
                duration(),
                rating(),
                opened(),
                published(),
                createdAt(),
                updatedAt(),
                banner(),
                thumbnail(),
                thumbnailHalf(),
                trailer(),
                video(),
                categories(),
                castMembers(),
                genres()
        );
    }

    public String id() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String title() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String description() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer launchedAt() {
        return launchedAt;
    }

    public void setLaunchedAt(Integer launchedAt) {
        this.launchedAt = launchedAt;
    }

    public Double duration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public String rating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public boolean opened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public boolean published() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public String createdAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String updatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String video() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String trailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public String banner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String thumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String thumbnailHalf() {
        return thumbnailHalf;
    }

    public void setThumbnailHalf(String thumbnailHalf) {
        this.thumbnailHalf = thumbnailHalf;
    }

    public Set<String> castMembers() {
        return castMembers;
    }

    public void setCastMembers(Set<String> castMembers) {
        this.castMembers = castMembers;
    }

    public Set<String> categories() {
        return categories;
    }

    public void setCategories(Set<String> categories) {
        this.categories = categories;
    }

    public Set<String> genres() {
        return genres;
    }

    public void setGenres(Set<String> genres) {
        this.genres = genres;
    }
}
