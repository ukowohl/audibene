package de.ukowohl.audibene.service.support;

import de.ukowohl.audibene.utils.domain.EntitySupplier;
import de.ukowohl.audibene.utils.domain.EntityUpdater;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Optional;
import java.util.function.Function;

@Validated
public interface CrudService<E, ID extends Serializable> {

    @NotNull
    <T> Page<T> findAll(@NotNull Pageable pageable, @NotNull Function<E, T> mapper);

    @NotNull
    <T> T getById(@NotNull ID id, @NotNull Function<E, T> mapper);

    @NotNull
    <T> Optional<T> findOne(@NotNull ID id, @NotNull Function<E, T> mapper);

    @NotNull
    <T> T create(@NotNull EntitySupplier<E> supplier, @NotNull Function<E, T> updater);

    @NotNull
    <T> T update(@NotNull ID id, @NotNull EntityUpdater<E> updater, @NotNull Function<E, T> mapper);

    void delete(@NotNull ID id);
}
