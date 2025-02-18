package org.gbif.checklistbank.nub.lookup;

import org.gbif.checklistbank.config.ClbConfiguration;
import org.gbif.nub.lookup.straight.IdLookup;
import org.gbif.nub.lookup.straight.IdLookupImpl;
import org.junit.Ignore;
import org.junit.Test;

import java.util.UUID;

/**
 *
 */
@Ignore("A manual test class")
public class NubMatchServiceTest {

  @Test
  public void matchDataset() throws Exception {
    ClbConfiguration clb = new ClbConfiguration();
    clb.serverName = "pg1.gbif.org";
    clb.databaseName = "prod_checklistbank";
    clb.user = "clb";
    clb.password = "%BBJu2MgstXJ";
    IdLookup lookup = IdLookupImpl.temp().load(clb, false);
    NubMatchService service = new NubMatchService(clb, lookup, null, null);
    service.matchDataset(UUID.fromString("38f91564-30ff-47b0-aa6b-cf3b59f0fca7"));
  }

}