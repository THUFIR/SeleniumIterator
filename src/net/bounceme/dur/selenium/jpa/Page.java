/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bounceme.dur.selenium.jpa;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author thufir
 */
@Entity
@Table(name = "pages", catalog = "rome_aggregator", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Page.findAll", query = "SELECT p FROM Page p"),
    @NamedQuery(name = "Page.findById", query = "SELECT p FROM Page p WHERE p.id = :id"),
    @NamedQuery(name = "Page.findByCreated", query = "SELECT p FROM Page p WHERE p.created = :created"),
    @NamedQuery(name = "Page.findByLinkId", query = "SELECT p FROM Page p WHERE p.linkId = :linkId"),
    @NamedQuery(name = "Page.findByStatus", query = "SELECT p FROM Page p WHERE p.status = :status")})
public class Page implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Basic(optional = false)
    @Lob
    @Column(nullable = false, length = 65535)
    private String page;
    @Basic(optional = false)
    @Column(name = "link_id", nullable = false)
    private int linkId;
    @Basic(optional = false)
    @Column(nullable = false)
    private int status;

    public Page() {
    }

    public Page(Integer id) {
        this.id = id;
    }

    public Page(Integer id, Date created, String page, int linkId, int status) {
        this.id = id;
        this.created = created;
        this.page = page;
        this.linkId = linkId;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public int getLinkId() {
        return linkId;
    }

    public void setLinkId(int linkId) {
        this.linkId = linkId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Page)) {
            return false;
        }
        Page other = (Page) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.bounceme.dur.selenium.jpa.Page[ id=" + id + " ]";
    }
    
}
