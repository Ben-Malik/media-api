package com.my.media.model;

import java.util.List;

import com.my.media.enums.MediaType;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * An encapsulation of the media or reponse of the requests. 
 * <p> A {@linkplain Media} could be either of type Book or Album.</p>
 * 
 * @author ben-maliktchamalam
 */
@AllArgsConstructor
@NoArgsConstructor
public class Media {
    
    private String title;

    private List<String> authors;

    private MediaType type;

    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public MediaType getType() {
        return type;
    }

    public void setType(MediaType type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((authors == null) ? 0 : authors.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Media other = (Media) obj;
        if (authors == null) {
            if (other.authors != null)
                return false;
        } else if (!authors.equals(other.authors))
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        if (type != other.type)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Media [authors=" + authors + ", title=" + title + ", type=" + type + "]";
    }
    
}
