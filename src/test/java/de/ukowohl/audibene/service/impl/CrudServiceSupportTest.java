package de.ukowohl.audibene.service.impl;


import de.ukowohl.audibene.utils.domain.EntityUpdater;
import de.ukowohl.audibene.service.support.CrudService;
import lombok.Getter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.EntityNotFoundException;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Collections.singletonList;
import static lombok.AccessLevel.PROTECTED;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Getter(PROTECTED)
@RunWith(MockitoJUnitRunner.class)
public abstract class CrudServiceSupportTest<S extends CrudService<E, ID>, R extends JpaRepository<E, ID>, E, ID extends Serializable> {

    @Mock
    protected E entity;
    @Mock
    protected E modifiedEntity;
    @Mock
    protected List<E> entities;
    @Mock
    protected Page<E> page;
    @Mock
    protected Function<E, ID> mapper;
    @Mock
    protected Pageable pageable;
    @Mock
    private EntityUpdater<E> updater;

    protected abstract ID getId();

    protected abstract S getService();

    protected abstract R getRepository();

    @Before
    public void setUpProperties() throws Exception {
        entity = getEntity();
        modifiedEntity = getModifiedEntity();
        entities = getEntities();
        page = getPage();
        mapper = getMapper();
        updater = getUpdater();
        pageable = getPageable();

        when(getRepository().findOne(getId())).thenReturn(getEntity());
    }

    @Test
    public void shouldFindAllAsPageWithMapper() throws Exception {
        Pageable pageable = new PageRequest(0, 1);
        PageImpl<E> page = new PageImpl<>(singletonList(entity), pageable, 42);

        when(getRepository().findAll(pageable)).thenReturn(page);
        when(mapper.apply(entity)).thenReturn(getId());

        Page<ID> actual = getService().findAll(pageable, mapper);

        assertThat(actual, is(new PageImpl<>(singletonList(getId()), pageable, 42)));
    }


    @Test
    public void shouldFindByIdWithMapper() throws Exception {
        when(getRepository().getOne(getId())).thenReturn(entity);
        when(mapper.apply(entity)).thenReturn(getId());

        Optional<ID> actual = getService().findOne(getId(), mapper);

        assertThat(actual, is(Optional.of(getId())));
    }

    @Test
    public void shouldFindByIdWithMapperWhenNotFound() throws Exception {
        when(getRepository().findOne(getId())).thenReturn(null);

        Optional<ID> actual = getService().findOne(getId(), mapper);

        assertThat(actual, is(Optional.empty()));
    }

    @Test
    public void shouldGetByIdWithMapper() throws Exception {
        when(getRepository().getOne(getId())).thenReturn(entity);
        when(mapper.apply(entity)).thenReturn(getId());

        ID actual = getService().getById(getId(), mapper);

        assertThat(actual, is(getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldGetByIdWithMapperWhenNotFound() throws Exception {
        when(getRepository().findOne(getId())).thenReturn(null);

        getService().getById(getId(), mapper);
    }

    @Test
    public void shouldCreateBySupplierWithMapper() throws Exception {
        when(getRepository().saveAndFlush(entity)).thenReturn(entity);
        when(mapper.apply(entity)).thenReturn(getId());

        ID actual = getService().create(() -> entity, mapper);

        assertThat(actual, is(getId()));
        verify(getRepository()).saveAndFlush(entity);
    }

    @Test
    public void shouldDeleteById() throws Exception {
        getService().delete(getId());

        verify(getRepository()).delete(getId());
    }

    @Test
    public void shouldUpdateOne() throws Exception {
        when(getRepository().saveAndFlush(any())).thenReturn(returnsFirstArg());
        when(getRepository().getOne(getId())).thenReturn(entity);
        getService().update(getId(), updater, mapper);

        verify(updater).accept(entity);
        verify(getRepository()).saveAndFlush(entity);
    }
}