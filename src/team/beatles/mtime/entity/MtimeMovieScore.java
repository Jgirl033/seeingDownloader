/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package team.beatles.mtime.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author admin
 */
@Entity
public class MtimeMovieScore implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public MtimeMovieScore() {
    }
    
    
    private String mid;
    private double rating;
    private int evaluation_number;
    private double book_office;

    public MtimeMovieScore(String mid) {
        this.mid = mid;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getEvaluation_number() {
        return evaluation_number;
    }

    public void setEvaluation_number(int evaluation_number) {
        this.evaluation_number = evaluation_number;
    }

    public double getBook_office() {
        return book_office;
    }

    public void setBook_office(double book_office) {
        this.book_office = book_office;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(object instanceof MtimeMovieScore)) {
            return false;
        }
        MtimeMovieScore other = (MtimeMovieScore) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "team.beatles.mtime.entity.MtimeMovieScore[ id=" + id + " ]";
    }
    
    
    
}
