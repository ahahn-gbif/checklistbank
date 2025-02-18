package org.gbif.checklistbank.nub.source;

import org.gbif.api.model.registry.Dataset;
import org.gbif.checklistbank.config.ClbConfiguration;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * A nub source which is backed by postgres checklistbank usages of a given datasetKey
 */
public class ClbSource extends NubSource {
  private static final Logger LOG = LoggerFactory.getLogger(ClbSource.class);
  private static final UUID PATCH_DATASET_KEY = UUID.fromString("daacce49-b206-469b-8dc2-2257719f3afa");
  private final ClbConfiguration clb;

  public ClbSource(ClbConfiguration clb, UUID key, String name) {
    super(key, name.replaceAll("\\s", " "), false);
    this.clb = clb;
  }

  public ClbSource(ClbConfiguration clb, Dataset dataset) {
    this(clb, dataset.getKey(), dataset.getTitle());
    // we allow suprageneric homonyms to be created when found in the backbone patch dataset!
    supragenericHomonymSource = PATCH_DATASET_KEY.equals(dataset.getKey());
  }

  @Override
  public void initNeo(NeoUsageWriter writer) throws Exception {
    try (BaseConnection c = (BaseConnection) clb.connect()) {
      final CopyManager cm = new CopyManager(c);
      cm.copyOut("COPY ("
          + "SELECT u.id, u.parent_fk, u.basionym_fk, u.rank,"
          + " coalesce(u.status, CASE WHEN (u.is_synonym) THEN 'SYNONYM'::taxonomic_status ELSE 'ACCEPTED'::taxonomic_status END),"
          + " u.nom_status, n.scientific_name, c.citation"
          + " FROM name_usage u JOIN name n ON u.name_fk=n.id LEFT JOIN citation c ON u.name_published_in_fk=c.id"
          + " WHERE u.dataset_key = '" + key + "')"
          + " TO STDOUT WITH NULL ''", writer);
    }
  }
}
