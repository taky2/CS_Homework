/*
 * This class contains all the keyword objects neccessary to store 
 * information from the URLs.
 */
public class A5_Keyword implements Comparable<Keyword> {
    private String key;
    private URLs urls;

    /**
     * Constructor
     * @param key String value for keyword
     */
    public Keyword(String key) {
        this.key = key;
        urls = new URLs();
    }

    /**
     * Accessor to return key
     * @return Returns string
     */
    public String getKey() {
        return key;
    }

    /**
     * Accessor for returning URL
     * @return Returns list of URLs
     */
    public URLs getUrls() {
        return urls;
    }

    /**
     * Accessor for returning all URLs
     * @return Return array of URLs
     */
    public String[] getArrayUrls() {
        return urls.getAll();
    }

    /**
     * Prints urls to console.
     */
    public void printUrls() {
        String[] toPrint = urls.getAll();
        for (int i = 0; i < toPrint.length; i++) {
            System.out.println(toPrint[i]);
        }
    }

    /**
     * Prints out keyword and urls to console.
     */
    public void print() {
        System.out.println("**********************************");
        System.out.println();
        System.out.printf("Keyword: %s\n", key);
        System.out.printf("URLs where %s appears:\n", key);
        System.out.println();
        String[] urprint = urls.getAll();
        for (int i = 0; i < urls.size(); i++) {
            System.out.println(urprint[i]);
        }
        System.out.println("----------------------------------\n");
    }

    /**
     * Method to add a url to the url list.
     * @param url
     */
    public void addUrl(String url) {
        urls.add(url);
    }

    /**
     * Compares two keywords
     * @param other The keyword to compare to.
     * @return Returns -10 if less than, 10 if more than, and 0 if equal to.
     */
    public int compareTo(Keyword other) {
        return key.compareToIgnoreCase(other.getKey());
    }
}
