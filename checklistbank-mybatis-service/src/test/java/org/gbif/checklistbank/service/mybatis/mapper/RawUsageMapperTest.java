package org.gbif.checklistbank.service.mybatis.mapper;

import org.gbif.checklistbank.model.RawUsage;

import java.util.Date;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 *
 */
public class RawUsageMapperTest extends MapperITBase<RawUsageMapper> {

    public RawUsageMapperTest() {
        super(RawUsageMapper.class, true);
    }

    @Test
    public void crudTest() {
        final RawUsage raw = new RawUsage();
        raw.setUsageKey(usageKey);
        raw.setJson("{'me':'markus'}");
        raw.setDatasetKey(datasetKey);
        raw.setLastCrawled(new Date());

        assertNull(mapper.get(usageKey));
        mapper.insert(raw);

        // this is taken from dataset_metrics and not stored in each raw usage so null it for comparison
        raw.setLastCrawled(null);
        assertEquals(raw, mapper.get(usageKey));
        mapper.delete(usageKey);
        assertNull(mapper.get(usageKey));
    }
}