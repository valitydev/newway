package dev.vality.newway.handler.dominant.impl;

import dev.vality.damsel.domain.TermSetHierarchyObject;
import dev.vality.newway.dao.dominant.iface.DomainObjectDao;
import dev.vality.newway.dao.dominant.impl.TermSetHierarchyDaoImpl;
import dev.vality.newway.domain.tables.pojos.TermSetHierarchy;
import dev.vality.newway.handler.dominant.AbstractDominantHandler;
import dev.vality.newway.util.JsonUtil;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class TermSetHierarchyHandler
        extends AbstractDominantHandler<TermSetHierarchyObject, TermSetHierarchy, Integer> {

    private final TermSetHierarchyDaoImpl termSetHierarchyDao;

    public TermSetHierarchyHandler(TermSetHierarchyDaoImpl termSetHierarchyDao) {
        this.termSetHierarchyDao = termSetHierarchyDao;
    }

    @Override
    protected DomainObjectDao<TermSetHierarchy, Integer> getDomainObjectDao() {
        return termSetHierarchyDao;
    }

    @Override
    protected TermSetHierarchyObject getTargetObject() {
        return getDomainObject().getTermSetHierarchy();
    }

    @Override
    protected Integer getTargetObjectRefId() {
        return getTargetObject().getRef().getId();
    }

    @Override
    protected boolean acceptDomainObject() {
        return getDomainObject().isSetTermSetHierarchy();
    }

    @Override
    public TermSetHierarchy convertToDatabaseObject(TermSetHierarchyObject termSetHierarchyObject, Long versionId,
                                                    boolean current) {
        TermSetHierarchy termSetHierarchy = new TermSetHierarchy();
        termSetHierarchy.setVersionId(versionId);
        termSetHierarchy.setTermSetHierarchyRefId(getTargetObjectRefId());
        dev.vality.damsel.domain.TermSetHierarchy data = termSetHierarchyObject.getData();
        termSetHierarchy.setName(data.getName());
        termSetHierarchy.setDescription(data.getDescription());
        if (data.isSetParentTerms()) {
            termSetHierarchy.setParentTermsRefId(data.getParentTerms().getId());
        }
        termSetHierarchy.setTermSetsJson(JsonUtil.objectToJsonString(
                data.getTermSets().stream().map(JsonUtil::thriftBaseToJsonNode).collect(Collectors.toList())));
        termSetHierarchy.setCurrent(current);
        return termSetHierarchy;
    }
}
