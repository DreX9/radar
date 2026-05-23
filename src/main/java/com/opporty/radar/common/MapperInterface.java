package com.opporty.radar.common;

public interface MapperInterface<E, DW, DV> {
    DV toDt(E entity);

    E toEntity(DW dto);
}
