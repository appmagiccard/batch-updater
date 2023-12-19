package com.magicauction.batchupdater.entity;

public class BulkDataObject {

    private String object;
    private String id;
    private String type;
    private String updated_at;
    private String uri;
    private String name;
    private String description;
    private String size;
    private String download_uri;
    private String content_type;
    private String content_encoding;

    @Override
    public String toString() {
        return "BulkDataObject{" +
                "object='" + object + '\'' +
                ", id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", uri='" + uri + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", size='" + size + '\'' +
                ", download_uri='" + download_uri + '\'' +
                ", content_type='" + content_type + '\'' +
                ", content_encoding='" + content_encoding + '\'' +
                '}';
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDownload_uri() {
        return download_uri;
    }

    public void setDownload_uri(String download_uri) {
        this.download_uri = download_uri;
    }

    public String getContent_type() {
        return content_type;
    }

    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }

    public String getContent_encoding() {
        return content_encoding;
    }

    public void setContent_encoding(String content_encoding) {
        this.content_encoding = content_encoding;
    }

    public BulkDataObject(String object, String id, String type, String updated_at, String uri, String name, String description, String size, String download_uri, String content_type, String content_encoding) {
        this.object = object;
        this.id = id;
        this.type = type;
        this.updated_at = updated_at;
        this.uri = uri;
        this.name = name;
        this.description = description;
        this.size = size;
        this.download_uri = download_uri;
        this.content_type = content_type;
        this.content_encoding = content_encoding;
    }

    public BulkDataObject() {
    }
}
