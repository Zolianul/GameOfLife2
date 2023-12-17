public class Message {
    protected String content;
    protected String header;

    public Message(String content, String header) {
        this.content = content;
        this.header = header;
    }

    public Message(String content)
    {
        this.content=content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
}
