/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package team.beatles.mtime.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "mtime_comment")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MtimeComment.findAll", query = "SELECT m FROM MtimeComment m"),
    @NamedQuery(name = "MtimeComment.findByUid", query = "SELECT m FROM MtimeComment m WHERE m.mtimeCommentPK.uid = :uid"),
    @NamedQuery(name = "MtimeComment.findByMid", query = "SELECT m FROM MtimeComment m WHERE m.mtimeCommentPK.mid = :mid"),
    @NamedQuery(name = "MtimeComment.findBySource", query = "SELECT m FROM MtimeComment m WHERE m.source = :source"),
    @NamedQuery(name = "MtimeComment.findByStatus", query = "SELECT m FROM MtimeComment m WHERE m.status = :status"),
    @NamedQuery(name = "MtimeComment.findByRating", query = "SELECT m FROM MtimeComment m WHERE m.rating = :rating"),
    @NamedQuery(name = "MtimeComment.findByTime", query = "SELECT m FROM MtimeComment m WHERE m.time = :time")})
public class MtimeComment implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MtimeCommentPK mtimeCommentPK;
    @Basic(optional = false)
    @Column(name = "source")
    private int source;
    @Column(name = "status")
    private String status;
    @Basic(optional = false)
    @Column(name = "rating")
    private double rating;
    @Basic(optional = false)
    @Column(name = "time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;
    @Basic(optional = false)
    @Lob
    @Column(name = "comment")
    private String comment;
    @JoinColumn(name = "uid", referencedColumnName = "uid", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private MtimeUser mtimeUser;
    @JoinColumn(name = "mid", referencedColumnName = "mid", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private MtimeMovie mtimeMovie;

    public MtimeComment() {
    }

    public MtimeComment(MtimeCommentPK mtimeCommentPK) {
        this.mtimeCommentPK = mtimeCommentPK;
    }

    public MtimeComment(MtimeCommentPK mtimeCommentPK, int source, double rating, Date time, String comment) {
        this.mtimeCommentPK = mtimeCommentPK;
        this.source = source;
        this.rating = rating;
        this.time = time;
        this.comment = comment;
    }

    public MtimeComment(String uid, int mid) {
        this.mtimeCommentPK = new MtimeCommentPK(uid, mid);
    }

    public MtimeCommentPK getMtimeCommentPK() {
        return mtimeCommentPK;
    }

    public void setMtimeCommentPK(MtimeCommentPK mtimeCommentPK) {
        this.mtimeCommentPK = mtimeCommentPK;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public MtimeUser getMtimeUser() {
        return mtimeUser;
    }

    public void setMtimeUser(MtimeUser mtimeUser) {
        this.mtimeUser = mtimeUser;
    }

    public MtimeMovie getMtimeMovie() {
        return mtimeMovie;
    }

    public void setMtimeMovie(MtimeMovie mtimeMovie) {
        this.mtimeMovie = mtimeMovie;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mtimeCommentPK != null ? mtimeCommentPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MtimeComment)) {
            return false;
        }
        MtimeComment other = (MtimeComment) object;
        if ((this.mtimeCommentPK == null && other.mtimeCommentPK != null) || (this.mtimeCommentPK != null && !this.mtimeCommentPK.equals(other.mtimeCommentPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "team.beatles.mtime.entity.MtimeComment[ mtimeCommentPK=" + mtimeCommentPK + " ]";
    }
    
}
