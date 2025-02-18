package org.gbif.checklistbank.service.mybatis;

import org.gbif.api.exception.UnparsableException;
import org.gbif.api.model.checklistbank.ParsedName;
import org.gbif.api.service.checklistbank.NameParser;
import org.gbif.api.vocabulary.NamePart;
import org.gbif.api.vocabulary.NameType;
import org.gbif.api.vocabulary.Rank;
import org.gbif.checklistbank.service.ParsedNameService;
import org.gbif.nameparser.NameParserGbifV1;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParsedNameServiceMyBatisIT extends MyBatisServiceITBase<ParsedNameService> {

  private NameParser parser = new NameParserGbifV1();

  public ParsedNameServiceMyBatisIT() {
    super(ParsedNameService.class);
  }

  @Test
  public void testCreateOrGet() throws Exception {
    ParsedName pn = new ParsedName();
    pn.setScientificName("Abies alba Mill.");
    pn.setGenusOrAbove("Abies");
    pn.setAuthorship("Mill.");
    pn.setSpecificEpithet("alba");
    pn.setType(NameType.SCIENTIFIC);
    assertNull(pn.getKey());

    ParsedName pn2 = service.createOrGet(pn, true);
    assertNotNull(pn2.getKey());
    assertEquals("Abies alba Mill.", pn2.getScientificName());
    assertEquals("Abies alba", pn2.canonicalName());
    assertEquals("Abies", pn2.getGenusOrAbove());
    assertEquals("alba", pn2.getSpecificEpithet());
    assertEquals("Mill.", pn2.getAuthorship());

    pn = service.createOrGet(parse("Abies alba Mill."), true);
    assertEquals("Abies alba Mill.", pn.getScientificName());
    assertEquals("Abies alba", pn.canonicalName());
    assertEquals("Abies", pn.getGenusOrAbove());
    assertEquals("alba", pn.getSpecificEpithet());
    assertEquals("Mill.", pn.getAuthorship());

    pn = service.createOrGet(parse("Abies sp."), true);
    assertEquals("Abies sp.", pn.getScientificName());
    assertEquals("Abies spec.", pn.canonicalName());
    assertEquals("Abies", pn.getGenusOrAbove());
    assertEquals(Rank.SPECIES, pn.getRank());
    assertNull(pn.getSpecificEpithet());

    pn = service.createOrGet(parse("×Abies Mill."), true);
    assertEquals("×Abies Mill.", pn.getScientificName());
    assertEquals("Abies", pn.canonicalName());
    assertEquals("Abies", pn.getGenusOrAbove());
    assertNull(pn.getRank());
    assertNull(pn.getSpecificEpithet());
    assertEquals(NamePart.GENERIC, pn.getNotho());

    pn = service.createOrGet(parse("? hostilis Gravenhorst, 1829"), true);
    assertEquals("? hostilis Gravenhorst, 1829", pn.getScientificName());
    assertEquals("? hostilis", pn.canonicalName());
    assertEquals("?", pn.getGenusOrAbove());
    assertEquals(Rank.SPECIES, pn.getRank());
    assertEquals("hostilis", pn.getSpecificEpithet());
  }

  private ParsedName parse(String x) {
    try {
      return parser.parse(x, null);
    } catch (UnparsableException e) {
      ParsedName pn = new ParsedName();
      pn.setScientificName(x);
      pn.setType(e.type);
      return pn;
    }
  }

  @Test
  public void testOrphaned() throws Exception {
    assertEquals(1, service.deleteOrphaned());
  }

  @Test
  public void testReparse() throws Exception {
    assertEquals(5, service.reparseAll());
  }
}