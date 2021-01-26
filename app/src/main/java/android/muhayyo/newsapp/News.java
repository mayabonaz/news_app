package android.muhayyo.newsapp;

/**
 * @link News A class representing News object. Contains information relating to a news article
 */

public class News {

    private String sectionName;
    private String webTitle;
    private String authorName;
    private String webPublicationDate;
    private String webUrl;

    /**
     * Public constructor that creates News objects
     *
     * @param sectionName        News section
     * @param webTitle           Article title
     * @param authorName         Author name
     * @param webPublicationDate Date of publication
     * @param webUrl             URL address of the news article
     */
    public News(String sectionName, String webTitle, String authorName, String webPublicationDate, String webUrl) {
        this.sectionName = sectionName;
        this.webTitle = webTitle;
        this.authorName = authorName;
        this.webPublicationDate = webPublicationDate;
        this.webUrl = webUrl;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getArticleTitle() {
        return webTitle;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getPublishedDate() {
        return webPublicationDate;
    }

    public String getWebUrl() {
        return webUrl;
    }
}
