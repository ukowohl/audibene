package de.ukowohl.audibene.service.support;

import de.ukowohl.audibene.utils.domain.EntitySupplier;
import de.ukowohl.audibene.utils.domain.EntityUpdater;
import de.ukowohl.audibene.utils.Mappers;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

@Validated
@Transactional
@RequiredArgsConstructor
public abstract class CrudServiceSupport<E, ID extends Serializable> implements CrudService<E, ID> {

    protected abstract JpaRepository<E, ID> getRepository();

    protected abstract Supplier<EntityNotFoundException> notFoundById(ID id);

    @NotNull
    @Override
    public <T> Page<T> findAll(@NotNull Pageable pageable, @NotNull Function<E, T> mapper) {
        return Mappers.map(getRepository().findAll(pageable), mapper);
    }

    @NotNull
    @Override
    public <T> T getById(@NotNull ID id, @NotNull Function<E, T> mapper) {
        return Optional.ofNullable(getRepository().getOne(id)).map(mapper).orElseThrow(notFoundById(id));
    }

    @NotNull
    @Override
    public <T> Optional<T> findOne(@NotNull ID id, @NotNull Function<E, T> mapper) {
        return Optional.ofNullable(getRepository().getOne(id)).map(mapper);
    }

    @NotNull
    @Override
    public <T> T create(@NotNull EntitySupplier<E> supplier, @NotNull Function<E, T> mapper) {
        return Mappers.nullSafe(getRepository().saveAndFlush(supplier.get()), mapper);
    }


    @NotNull
    @Override
    public <T> T update(@NotNull ID id, @NotNull EntityUpdater<E> updater, @NotNull Function<E, T> mapper) {
        E existing = getById(id, Function.identity());
        updater.accept(existing);
        return Mappers.nullSafe(getRepository().saveAndFlush(existing), mapper);
    }

    @Override
    public void delete(@NotNull ID id) {
        getRepository().delete(id);
    }
}
