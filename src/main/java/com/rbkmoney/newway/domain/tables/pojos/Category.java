/*
 * This file is generated by jOOQ.
 */
package com.rbkmoney.newway.domain.tables.pojos;


import java.io.Serializable;
import java.time.LocalDateTime;

import javax.annotation.Generated;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Category implements Serializable {

    private static final long serialVersionUID = 258776696;

    private Long          id;
    private Long          versionId;
    private Integer       categoryId;
    private LocalDateTime wtime;
    private Boolean       current;

    public Category() {}

    public Category(Category value) {
        this.id = value.id;
        this.versionId = value.versionId;
        this.categoryId = value.categoryId;
        this.wtime = value.wtime;
        this.current = value.current;
    }

    public Category(
        Long          id,
        Long          versionId,
        Integer       categoryId,
        LocalDateTime wtime,
        Boolean       current
    ) {
        this.id = id;
        this.versionId = versionId;
        this.categoryId = categoryId;
        this.wtime = wtime;
        this.current = current;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersionId() {
        return this.versionId;
    }

    public void setVersionId(Long versionId) {
        this.versionId = versionId;
    }

    public Integer getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public LocalDateTime getWtime() {
        return this.wtime;
    }

    public void setWtime(LocalDateTime wtime) {
        this.wtime = wtime;
    }

    public Boolean getCurrent() {
        return this.current;
    }

    public void setCurrent(Boolean current) {
        this.current = current;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Category other = (Category) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        if (versionId == null) {
            if (other.versionId != null)
                return false;
        }
        else if (!versionId.equals(other.versionId))
            return false;
        if (categoryId == null) {
            if (other.categoryId != null)
                return false;
        }
        else if (!categoryId.equals(other.categoryId))
            return false;
        if (wtime == null) {
            if (other.wtime != null)
                return false;
        }
        else if (!wtime.equals(other.wtime))
            return false;
        if (current == null) {
            if (other.current != null)
                return false;
        }
        else if (!current.equals(other.current))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.versionId == null) ? 0 : this.versionId.hashCode());
        result = prime * result + ((this.categoryId == null) ? 0 : this.categoryId.hashCode());
        result = prime * result + ((this.wtime == null) ? 0 : this.wtime.hashCode());
        result = prime * result + ((this.current == null) ? 0 : this.current.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Category (");

        sb.append(id);
        sb.append(", ").append(versionId);
        sb.append(", ").append(categoryId);
        sb.append(", ").append(wtime);
        sb.append(", ").append(current);

        sb.append(")");
        return sb.toString();
    }
}
