package de.ukowohl.audibene.utils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface Mappers {

    static <E, T> List<T> map(Collection<E> values, Function<E, T> mapper) {
        return values.stream().map(mapper).collect(Collectors.toList());
    }

    static <E, T> Page<T> map(Page<E> page, Function<E, T> mapper) {
        List<T> content = map(page.getContent(), mapper);
        PageRequest pageable = new PageRequest(page.getNumber(), page.getSize(), page.getSort());
        return new PageImpl<>(content, pageable, page.getTotalElements());
    }

    static <E, T> T nullSafe(E value, Function<E, T> mapper) {
        return Optional.ofNullable(value).map(mapper).orElse(null);
    }
}
