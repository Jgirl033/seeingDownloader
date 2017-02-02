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
@Table(name = "mtime_wish")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MtimeWish.findAll", query = "SELECT m FROM MtimeWish m"),
    @NamedQuery(name = "MtimeWish.findByUid", query = "SELECT m FROM MtimeWish m WHERE m.uid = :uid"),
    @NamedQuery(name = "MtimeWish.findByOne", query = "SELECT m FROM MtimeWish m WHERE m.one = :one"),
    @NamedQuery(name = "MtimeWish.findByOneUrl", query = "SELECT m FROM MtimeWish m WHERE m.oneUrl = :oneUrl"),
    @NamedQuery(name = "MtimeWish.findByTwo", query = "SELECT m FROM MtimeWish m WHERE m.two = :two"),
    @NamedQuery(name = "MtimeWish.findByTwoUrl", query = "SELECT m FROM MtimeWish m WHERE m.twoUrl = :twoUrl"),
    @NamedQuery(name = "MtimeWish.findByThree", query = "SELECT m FROM MtimeWish m WHERE m.three = :three"),
    @NamedQuery(name = "MtimeWish.findByThreeUrl", query = "SELECT m FROM MtimeWish m WHERE m.threeUrl = :threeUrl"),
    @NamedQuery(name = "MtimeWish.findByFour", query = "SELECT m FROM MtimeWish m WHERE m.four = :four"),
    @NamedQuery(name = "MtimeWish.findByFourUrl", query = "SELECT m FROM MtimeWish m WHERE m.fourUrl = :fourUrl"),
    @NamedQuery(name = "MtimeWish.findByFive", query = "SELECT m FROM MtimeWish m WHERE m.five = :five"),
    @NamedQuery(name = "MtimeWish.findByFiveUrl", query = "SELECT m FROM MtimeWish m WHERE m.fiveUrl = :fiveUrl"),
    @NamedQuery(name = "MtimeWish.findBySix", query = "SELECT m FROM MtimeWish m WHERE m.six = :six"),
    @NamedQuery(name = "MtimeWish.findBySixUrl", query = "SELECT m FROM MtimeWish m WHERE m.sixUrl = :sixUrl"),
    @NamedQuery(name = "MtimeWish.findBySeven", query = "SELECT m FROM MtimeWish m WHERE m.seven = :seven"),
    @NamedQuery(name = "MtimeWish.findBySevenUrl", query = "SELECT m FROM MtimeWish m WHERE m.sevenUrl = :sevenUrl"),
    @NamedQuery(name = "MtimeWish.findByEight", query = "SELECT m FROM MtimeWish m WHERE m.eight = :eight"),
    @NamedQuery(name = "MtimeWish.findByEightUrl", query = "SELECT m FROM MtimeWish m WHERE m.eightUrl = :eightUrl"),
    @NamedQuery(name = "MtimeWish.findByNine", query = "SELECT m FROM MtimeWish m WHERE m.nine = :nine"),
    @NamedQuery(name = "MtimeWish.findByNineUrl", query = "SELECT m FROM MtimeWish m WHERE m.nineUrl = :nineUrl"),
    @NamedQuery(name = "MtimeWish.findByTen", query = "SELECT m FROM MtimeWish m WHERE m.ten = :ten"),
    @NamedQuery(name = "MtimeWish.findByTenUrl", query = "SELECT m FROM MtimeWish m WHERE m.tenUrl = :tenUrl")})
public class MtimeWish implements Serializable {

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
    @Column(name = "six")
    private String six;
    @Column(name = "six_url")
    private String sixUrl;
    @Column(name = "seven")
    private String seven;
    @Column(name = "seven_url")
    private String sevenUrl;
    @Column(name = "eight")
    private String eight;
    @Column(name = "eight_url")
    private String eightUrl;
    @Column(name = "nine")
    private String nine;
    @Column(name = "nine_url")
    private String nineUrl;
    @Column(name = "ten")
    private String ten;
    @Column(name = "ten_url")
    private String tenUrl;
    @JoinColumn(name = "uid", referencedColumnName = "uid", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private MtimeUser mtimeUser;

    public MtimeWish() {
    }

    public MtimeWish(String uid) {
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

    public String getSix() {
        return six;
    }

    public void setSix(String six) {
        this.six = six;
    }

    public String getSixUrl() {
        return sixUrl;
    }

    public void setSixUrl(String sixUrl) {
        this.sixUrl = sixUrl;
    }

    public String getSeven() {
        return seven;
    }

    public void setSeven(String seven) {
        this.seven = seven;
    }

    public String getSevenUrl() {
        return sevenUrl;
    }

    public void setSevenUrl(String sevenUrl) {
        this.sevenUrl = sevenUrl;
    }

    public String getEight() {
        return eight;
    }

    public void setEight(String eight) {
        this.eight = eight;
    }

    public String getEightUrl() {
        return eightUrl;
    }

    public void setEightUrl(String eightUrl) {
        this.eightUrl = eightUrl;
    }

    public String getNine() {
        return nine;
    }

    public void setNine(String nine) {
        this.nine = nine;
    }

    public String getNineUrl() {
        return nineUrl;
    }

    public void setNineUrl(String nineUrl) {
        this.nineUrl = nineUrl;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getTenUrl() {
        return tenUrl;
    }

    public void setTenUrl(String tenUrl) {
        this.tenUrl = tenUrl;
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
        if (!(object instanceof MtimeWish)) {
            return false;
        }
        MtimeWish other = (MtimeWish) object;
        if ((this.uid == null && other.uid != null) || (this.uid != null && !this.uid.equals(other.uid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "team.beatles.mtime.entity.MtimeWish[ uid=" + uid + " ]";
    }
    
}
