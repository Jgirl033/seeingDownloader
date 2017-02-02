/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package team.beatles.mtime.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "mtime_collect")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MtimeCollect.findAll", query = "SELECT m FROM MtimeCollect m"),
    @NamedQuery(name = "MtimeCollect.findByUid", query = "SELECT m FROM MtimeCollect m WHERE m.uid = :uid"),
    @NamedQuery(name = "MtimeCollect.findByOne", query = "SELECT m FROM MtimeCollect m WHERE m.one = :one"),
    @NamedQuery(name = "MtimeCollect.findByOneUrl", query = "SELECT m FROM MtimeCollect m WHERE m.oneUrl = :oneUrl"),
    @NamedQuery(name = "MtimeCollect.findByTwo", query = "SELECT m FROM MtimeCollect m WHERE m.two = :two"),
    @NamedQuery(name = "MtimeCollect.findByTwoUrl", query = "SELECT m FROM MtimeCollect m WHERE m.twoUrl = :twoUrl"),
    @NamedQuery(name = "MtimeCollect.findByThree", query = "SELECT m FROM MtimeCollect m WHERE m.three = :three"),
    @NamedQuery(name = "MtimeCollect.findByThreeUrl", query = "SELECT m FROM MtimeCollect m WHERE m.threeUrl = :threeUrl"),
    @NamedQuery(name = "MtimeCollect.findByFour", query = "SELECT m FROM MtimeCollect m WHERE m.four = :four"),
    @NamedQuery(name = "MtimeCollect.findByFourUrl", query = "SELECT m FROM MtimeCollect m WHERE m.fourUrl = :fourUrl"),
    @NamedQuery(name = "MtimeCollect.findByFive", query = "SELECT m FROM MtimeCollect m WHERE m.five = :five"),
    @NamedQuery(name = "MtimeCollect.findByFiveUrl", query = "SELECT m FROM MtimeCollect m WHERE m.fiveUrl = :fiveUrl")})
public class MtimeCollect implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "uid")
    private String uid;
    @Column(name = "one")
    private String one;
    @Column(name = "one_url")
    private String oneUrl;
    @Column(name = "two")
    private String two;
    @Column(name = "two_url")
    private String twoUrl;
    @Column(name = "three")
    private String three;
    @Column(name = "three_url")
    private String threeUrl;
    @Column(name = "four")
    private String four;
    @Column(name = "four_url")
    private String fourUrl;
    @Column(name = "five")
    private String five;
    @Column(name = "five_url")
    private String fiveUrl;
    @JoinColumn(name = "uid", referencedColumnName = "uid", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private MtimeUser mtimeUser;

    public MtimeCollect() {
    }

    public MtimeCollect(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getOne() {
        return one;
    }

    public void setOne(String one) {
        this.one = one;
    }

    public String getOneUrl() {
        return oneUrl;
    }

    public void setOneUrl(String oneUrl) {
        this.oneUrl = oneUrl;
    }

    public String getTwo() {
        return two;
    }

    public void setTwo(String two) {
        this.two = two;
    }

    public String getTwoUrl() {
        return twoUrl;
    }

    public void setTwoUrl(String twoUrl) {
        this.twoUrl = twoUrl;
    }

    public String getThree() {
        return three;
    }

    public void setThree(String three) {
        this.three = three;
    }

    public String getThreeUrl() {
        return threeUrl;
    }

    public void setThreeUrl(String threeUrl) {
        this.threeUrl = threeUrl;
    }

    public String getFour() {
        return four;
    }

    public void setFour(String four) {
        this.four = four;
    }

    public String getFourUrl() {
        return fourUrl;
    }

    public void setFourUrl(String fourUrl) {
        this.fourUrl = fourUrl;
    }

    public String getFive() {
        return five;
    }

    public void setFive(String five) {
        this.five = five;
    }

    public String getFiveUrl() {
        return fiveUrl;
    }

    public void setFiveUrl(String fiveUrl) {
        this.fiveUrl = fiveUrl;
    }

    public MtimeUser getMtimeUser() {
        return mtimeUser;
    }

    public void setMtimeUser(MtimeUser mtimeUser) {
        this.mtimeUser = mtimeUser;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (uid != null ? uid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MtimeCollect)) {
            return false;
        }
        MtimeCollect other = (MtimeCollect) object;
        if ((this.uid == null && other.uid != null) || (this.uid != null && !this.uid.equals(other.uid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "team.beatles.mtime.entity.MtimeCollect[ uid=" + uid + " ]";
    }
    
}
