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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "mtime_user")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MtimeUser.findAll", query = "SELECT m FROM MtimeUser m"),
    @NamedQuery(name = "MtimeUser.findByUid", query = "SELECT m FROM MtimeUser m WHERE m.uid = :uid"),
    @NamedQuery(name = "MtimeUser.findByName", query = "SELECT m FROM MtimeUser m WHERE m.name = :name"),
    @NamedQuery(name = "MtimeUser.findBySource", query = "SELECT m FROM MtimeUser m WHERE m.source = :source"),
    @NamedQuery(name = "MtimeUser.findBySex", query = "SELECT m FROM MtimeUser m WHERE m.sex = :sex"),
    @NamedQuery(name = "MtimeUser.findByConstellation", query = "SELECT m FROM MtimeUser m WHERE m.constellation = :constellation"),
    @NamedQuery(name = "MtimeUser.findByAge", query = "SELECT m FROM MtimeUser m WHERE m.age = :age"),
    @NamedQuery(name = "MtimeUser.findByArea", query = "SELECT m FROM MtimeUser m WHERE m.area = :area"),
    @NamedQuery(name = "MtimeUser.findByEducationJob", query = "SELECT m FROM MtimeUser m WHERE m.educationJob = :educationJob"),
    @NamedQuery(name = "MtimeUser.findByTags", query = "SELECT m FROM MtimeUser m WHERE m.tags = :tags"),
    @NamedQuery(name = "MtimeUser.findByBirthday", query = "SELECT m FROM MtimeUser m WHERE m.birthday = :birthday")})
public class MtimeUser implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "uid")
    private String uid;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Column(name = "source")
    private int source;
    @Column(name = "sex")
    private String sex;
    @Column(name = "constellation")
    private String constellation;
    @Column(name = "age")
    private Integer age;
    @Column(name = "area")
    private String area;
    @Column(name = "education_job")
    private String educationJob;
    @Column(name = "tags")
    private String tags;
    @Column(name = "birthday")
    private String birthday;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mtimeUser")
    private Collection<MtimeComment> mtimeCommentCollection;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "mtimeUser")
    private MtimeWish mtimeWish;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "mtimeUser")
    private MtimeCollect mtimeCollect;

    public MtimeUser() {
    }

    public MtimeUser(String uid) {
        this.uid = uid;
    }

    public MtimeUser(String uid, String name, int source) {
        this.uid = uid;
        this.name = name;
        this.source = source;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getEducationJob() {
        return educationJob;
    }

    public void setEducationJob(String educationJob) {
        this.educationJob = educationJob;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    @XmlTransient
    public Collection<MtimeComment> getMtimeCommentCollection() {
        return mtimeCommentCollection;
    }

    public void setMtimeCommentCollection(Collection<MtimeComment> mtimeCommentCollection) {
        this.mtimeCommentCollection = mtimeCommentCollection;
    }

    public MtimeWish getMtimeWish() {
        return mtimeWish;
    }

    public void setMtimeWish(MtimeWish mtimeWish) {
        this.mtimeWish = mtimeWish;
    }

    public MtimeCollect getMtimeCollect() {
        return mtimeCollect;
    }

    public void setMtimeCollect(MtimeCollect mtimeCollect) {
        this.mtimeCollect = mtimeCollect;
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
        if (!(object instanceof MtimeUser)) {
            return false;
        }
        MtimeUser other = (MtimeUser) object;
        if ((this.uid == null && other.uid != null) || (this.uid != null && !this.uid.equals(other.uid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "team.beatles.mtime.entity.MtimeUser[ uid=" + uid + " ]";
    }
    
}
