package com.lec.domain;

import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public class BoardListeners {

//    @PostLoad
//    public void postLoad(Board board) {
//        System.out.println("post load: {}" +  board);
//    }
    
    @PrePersist
    public void prePersist(Board board) {
        System.out.println("pre persist: {}" + board);
    }
    
    @PostPersist
    public void postPersist(Board board) {
        System.out.println("post persist: {}" + board);
    }
    
    @PreUpdate
    public void preUpdate(Board board) {
        System.out.println("pre update: {}" + board);
    }
    
    @PostUpdate
    public void postUpdate(Board board) {
        System.out.println("post update: {}" +  board);
    }
    
    @PreRemove
    public void preRemove(Board board) {
        System.out.println("pre remove: {}" +  board);
    }
    
    @PostRemove
    public void postRemove(Board board) {
        System.out.println("post remove: {}" +  board);
    }
}

