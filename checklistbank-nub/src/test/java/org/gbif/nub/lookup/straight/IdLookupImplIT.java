package org.gbif.nub.lookup.straight;

import org.gbif.api.vocabulary.Kingdom;
import org.gbif.api.vocabulary.Rank;
import org.gbif.checklistbank.config.ClbConfiguration;
import org.gbif.checklistbank.service.mybatis.postgres.ClbDbTestRule;
import org.gbif.nub.lookup.NubMatchingTestModule;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import com.google.common.base.Joiner;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import static org.gbif.api.vocabulary.Kingdom.*;
import static org.gbif.api.vocabulary.Rank.*;
import static org.gbif.api.vocabulary.TaxonomicStatus.*;

public class IdLookupImplIT {

  private IdLookup matcher;
  private static final Joiner COMMA_JOINER = Joiner.on(", ").skipNulls();

  @Rule
  public ClbDbTestRule dbSetup = ClbDbTestRule.squirrels();

  @Test
  public void testMatching() throws IOException, InterruptedException {
    matcher = NubMatchingTestModule.provideLookup();

    assertMatch("Abies", "", "", GENUS, PLANTAE, 1);

    assertNoMatch("Abies alba", null, null, SPECIES, ANIMALIA);
    assertNoMatch("Abies alba", null, null, GENUS, PLANTAE);
    assertNoMatch("Abies alba", null, null, SUBSPECIES, PLANTAE);
    assertMatch("Abies alba", null, null, SPECIES, PLANTAE, 2);
    assertMatch("Abies alba", "", "", SPECIES, PLANTAE, 2);
    assertMatch("Abies alba", "", "1768", SPECIES, PLANTAE, 2);
    assertMatch("Abies alba", "Mill.", "", SPECIES, PLANTAE, 2);
    assertMatch("Abies alba", "Miller", "", SPECIES, PLANTAE, 2);
    assertMatch("Abies alba", "Mill.", "1800", SPECIES, INCERTAE_SEDIS, 2);
    assertMatch("Abies alba", "nothing but a year", "1768", SPECIES, INCERTAE_SEDIS, 2);
    assertNoMatch("Abies alba", "DC", "", SPECIES, PLANTAE);
    assertNoMatch("Abies alba", "DeCandole", "1770", SPECIES, PLANTAE);
    assertNoMatch("Abies alba", "Linnaeus", "", SPECIES, PLANTAE);
    assertNoMatch("Abies alba", "L.", "1989", SPECIES, PLANTAE);
  }

  @Test
  public void testSquirrels() throws IOException, SQLException {
    ClbConfiguration cfg = new ClbConfiguration();

    Properties props = dbSetup.getProperties();
    cfg.databaseName = props.getProperty("checklistbank.db.dataSource.databaseName");
    cfg.serverName = props.getProperty("checklistbank.db.dataSource.serverName");
    cfg.user = props.getProperty("checklistbank.db.dataSource.user");
    cfg.password = props.getProperty("checklistbank.db.dataSource.password");

    IdLookup l = IdLookupImpl.temp().load(cfg, true);
    assertEquals(2, l.size());
    assertEquals(1, l.match("Animalia", KINGDOM, ANIMALIA).getKey());

    assertEquals(10, l.match("Rodentia", ORDER, ANIMALIA).getKey());
    assertNull(l.match("Rodentia", FAMILY, ANIMALIA));
    assertNull(l.match("Rodentia", ORDER, PLANTAE));
    assertNull(l.match("Rodenti", ORDER, ANIMALIA));

    assertEquals(10, l.match("Rodentia", "Bowdich", "1821", ORDER , ACCEPTED, ANIMALIA).getKey());
    assertEquals(10, l.match("Rodentia", "Bowdich", "1221", ORDER , ACCEPTED, ANIMALIA).getKey());
    assertEquals(10, l.match("Rodentia", "Bowdich", null, ORDER , ACCEPTED, ANIMALIA).getKey());
    assertEquals(10, l.match("Rodentia", null, "1821", ORDER , ACCEPTED, ANIMALIA).getKey());
    assertEquals(10, l.match("Rodentia", "Bow.", null, ORDER , ACCEPTED, ANIMALIA).getKey());
    assertEquals(10, l.match("Rodentia", "Bow", "1821", ORDER , ACCEPTED, ANIMALIA).getKey());
    assertEquals(10, l.match("Rodentia", "B", "1821", ORDER , ACCEPTED, ANIMALIA).getKey());
    assertNull(l.match("Rodentia", "Mill.", "1823", ORDER , ACCEPTED, ANIMALIA));
  }

  private LookupUsage assertMatch(String name, String authorship, String year, Rank rank, Kingdom kingdom, Integer expectedKey) {
    LookupUsage m = matcher.match(name, authorship, year, rank, ACCEPTED ,kingdom);
    if (expectedKey == null && m == null) {
      // all fine, as expected!
    } else if (m == null) {
      fail("\n" + name + " not matching, but expecting " + expectedKey);
    } else {
      System.out.println("\n" + name + " matches " + m.getCanonical() + " " + m.getAuthorship() + " [" + COMMA_JOINER.join(m.getKingdom(), m.getRank(), m.getKey()) + "]");
      if (expectedKey != null && m.getKey() != expectedKey) {
        printAltMatches(name);
      }
      assertEquals(expectedKey, (Integer)m.getKey());
    }
    return m;
  }

  private void printAltMatches(String name) {
    System.out.println("\nALTERNATIVES for " + name);
    for (LookupUsage m : matcher.match(name)) {
      System.out.println("\n" + m.getCanonical() + " " + m.getAuthorship() + " [" + COMMA_JOINER.join(m.getKingdom(), m.getRank(), m.getKey()) + "]");
    }
  }

  private void assertNoMatch(String name, String authorship, String year, Rank rank, Kingdom kingdom) {
    assertMatch(name, authorship, year, rank, kingdom, null);
  }

}
