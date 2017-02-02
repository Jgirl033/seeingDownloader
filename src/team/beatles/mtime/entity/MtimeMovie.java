/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package team.beatles.mtime.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "mtime_movie")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MtimeMovie.findAll", query = "SELECT m FROM MtimeMovie m"),
    @NamedQuery(name = "MtimeMovie.findByMid", query = "SELECT m FROM MtimeMovie m WHERE m.mid = :mid"),
    @NamedQuery(name = "MtimeMovie.findByName", query = "SELECT m FROM MtimeMovie m WHERE m.name = :name"),
    @NamedQuery(name = "MtimeMovie.findByDirector", query = "SELECT m FROM MtimeMovie m WHERE m.director = :director"),
    @NamedQuery(name = "MtimeMovie.findByScreenwriter", query = "SELECT m FROM MtimeMovie m WHERE m.screenwriter = :screenwriter"),
    @NamedQuery(name = "MtimeMovie.findByPerformer", query = "SELECT m FROM MtimeMovie m WHERE m.performer = :performer"),
    @NamedQuery(name = "MtimeMovie.findByCompany", query = "SELECT m FROM MtimeMovie m WHERE m.company = :company"),
    @NamedQuery(name = "MtimeMovie.findByArea", query = "SELECT m FROM MtimeMovie m WHERE m.area = :area"),
    @NamedQuery(name = "MtimeMovie.findByJsonSrc", query = "SELECT m FROM MtimeMovie m WHERE m.jsonSrc = :jsonSrc"),
    @NamedQuery(name = "MtimeMovie.findByBookOffice", query = "SELECT m FROM MtimeMovie m WHERE m.bookOffice = :bookOffice"),
    @NamedQuery(name = "MtimeMovie.findByRating", query = "SELECT m FROM MtimeMovie m WHERE m.rating = :rating"),
    @NamedQuery(name = "MtimeMovie.findByEvaluationNumber", query = "SELECT m FROM MtimeMovie m WHERE m.evaluationNumber = :evaluationNumber")})
public class MtimeMovie implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "mid")
    private Integer mid;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Column(name = "director")
    private String director;
    @Basic(optional = false)
    @Column(name = "screenwriter")
    private String screenwriter;
    @Basic(optional = false)
    @Column(name = "performer")
    private String performer;
    @Basic(optional = false)
    @Column(name = "company")
    private String company;
    @Basic(optional = false)
    @Column(name = "area")
    private String area;
    @Basic(optional = false)
    @Lob
    @Column(name = "synopsis")
    private String synopsis;
    @Basic(optional = false)
    @Column(name = "json_src")
    private String jsonSrc;
    @Basic(optional = false)
    @Column(name = "book_office")
    private double bookOffice;
    @Basic(optional = false)
    @Column(name = "rating")
    private double rating;
    @Basic(optional = false)
    @Column(name = "evaluation_number")
    private int evaluationNumber;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mtimeMovie")
    private Collection<MtimeComment> mtimeCommentCollection;

    public MtimeMovie() {
    }

    public MtimeMovie(Integer mid) {
        this.mid = mid;
    }

    public MtimeMovie(Integer mid, String name, String director, String screenwriter, String performer, String company, String area, String synopsis, String jsonSrc, double bookOffice, double rating, int evaluationNumber) {
        this.mid = mid;
        this.name = name;
        this.director = director;
        this.screenwriter = screenwriter;
        this.performer = performer;
        this.company = company;
        this.area = area;
        this.synopsis = synopsis;
        this.jsonSrc = jsonSrc;
        this.bookOffice = bookOffice;
        this.rating = rating;
        this.evaluationNumber = evaluationNumber;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getScreenwriter() {
        return screenwriter;
    }

    public void setScreenwriter(String screenwriter) {
        this.screenwriter = screenwriter;
    }

    public String getPerformer() {
        return performer;
    }

    public void setPerformer(String performer) {
        this.performer = performer;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getJsonSrc() {
        return jsonSrc;
    }

    public void setJsonSrc(String jsonSrc) {
        this.jsonSrc = jsonSrc;
    }

    public double getBookOffice() {
        return bookOffice;
    }

    public void setBookOffice(double bookOffice) {
        this.bookOffice = bookOffice;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getEvaluationNumber() {
        return evaluationNumber;
    }

    public void setEvaluationNumber(int evaluationNumber) {
        this.evaluationNumber = evaluationNumber;
    }

    @XmlTransient
    public Collection<MtimeComment> getMtimeCommentCollection() {
        return mtimeCommentCollection;
    }

    public void setMtimeCommentCollection(Collection<MtimeComment> mtimeCommentCollection) {
        this.mtimeCommentCollection = mtimeCommentCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mid != null ? mid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MtimeMovie)) {
            return false;
        }
        MtimeMovie other = (MtimeMovie) object;
        if ((this.mid == null && other.mid != null) || (this.mid != null && !this.mid.equals(other.mid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "team.beatles.mtime.entity.MtimeMovie[ mid=" + mid + " ]";
    }
    
}
