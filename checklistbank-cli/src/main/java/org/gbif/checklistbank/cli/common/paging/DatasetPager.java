package org.gbif.checklistbank.cli.common.paging;

import org.gbif.api.model.common.paging.PagingRequest;
import org.gbif.api.model.common.paging.PagingResponse;
import org.gbif.api.model.registry.Dataset;
import org.gbif.api.service.registry.DatasetService;
import org.gbif.api.vocabulary.DatasetType;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Pages through all datasets filtering by type only.
 */
public class DatasetPager extends DatasetBasePager {
    private static final Logger LOG = LoggerFactory.getLogger(DatasetPager.class);

    private final DatasetService ds;
    private final DatasetType type;

    public DatasetPager(DatasetService ds, @Nullable DatasetType type) {
        super(type);
        this.ds = ds;
        this.type = type;
    }

    @Override
    PagingResponse<Dataset> nextPage(PagingRequest page) {
        if (type == null) {
            return ds.list(page);
        } else {
            return ds.listByType(type, page);
        }
    }

}
