package org.gbif.checklistbank.cli.common.paging;

import org.gbif.api.model.common.paging.PagingRequest;
import org.gbif.api.model.common.paging.PagingResponse;
import org.gbif.api.model.registry.Dataset;
import org.gbif.api.service.registry.InstallationService;
import org.gbif.api.vocabulary.DatasetType;

import java.util.UUID;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Iterates over all datasets hosted by a given installation.
 */
public class InstallationPager extends DatasetBasePager {
    private static final Logger LOG = LoggerFactory.getLogger(InstallationPager.class);

    private final InstallationService service;
    private final UUID installationKey;

    public InstallationPager(InstallationService service, UUID installationKey, @Nullable DatasetType type) {
        super(type);
        this.service = service;
        this.installationKey = installationKey;
    }

    @Override
    PagingResponse<Dataset> nextPage(PagingRequest page) {
        return service.getHostedDatasets(installationKey, page);
    }

}
