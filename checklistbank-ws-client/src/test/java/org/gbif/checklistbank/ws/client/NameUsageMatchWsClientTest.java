package org.gbif.checklistbank.ws.client;

import javax.ws.rs.core.UriBuilderException;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NameUsageMatchWsClientTest {

  /**
   * Test http://dev.gbif.org/issues/browse/POR-2688
   * We expect a client handler or uniform interface exception cause we did not setup the entire jersey stack properly.
   * We only want to make sure there is no UriBuilderException thrown
   */
  @Test
  public void testCurlyBrackets() {
    try {
      WebResource api = Client.create().resource("http://api.gbif.org/v1/species/match");
      WebResource  q = api.queryParam("name", "Nereis southerni %7B%7Bnowrap Abdel-Moez & Humphries, 1955");
      assertEquals(
        "http://api.gbif.org/v1/species/match?name=Nereis+southerni+%7B%7Bnowrap+Abdel-Moez+%26+Humphries,+1955",
        q.toString());

      NameUsageMatchWsClient client = new NameUsageMatchWsClient(api);

      client.match("Nereis southerni {{nowrap Abdel-Moez & Humphries, 1955", null, null, false, false);
    } catch (RuntimeException e) {
      if (e instanceof UriBuilderException) {
        throw e;
      }
      System.out.println(e.toString());
    }
  }

}
