package com.jaenyeong.study_actualspringdatajpa.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Item extends BaseTimeEntity implements Persistable<Long> {
    @Id
    private Long id;

    public Item(final Long id) {
        this.id = id;
    }

    @Override
    public boolean isNew() {
        return getCreatedDate() == null;
    }
}
