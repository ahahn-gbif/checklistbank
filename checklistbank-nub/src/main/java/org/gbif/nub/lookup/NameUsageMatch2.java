package org.gbif.nub.lookup;

import com.google.common.collect.Lists;
import org.gbif.api.model.checklistbank.NameUsageMatch;
import org.gbif.api.vocabulary.TaxonomicStatus;
import org.gbif.checklistbank.model.RankedName;

import java.util.List;
import java.util.Objects;

/**
 * NameUsageMatch for API v2
 * See https://github.com/gbif/checklistbank/issues/49
 */
public class NameUsageMatch2 {
  private boolean synonym;
  private RankedName usage;
  private RankedName acceptedUsage;
  private Nomenclature nomenclature;
  private List<RankedName> classification = Lists.newArrayList();
  private Diagnostics diagnostics = new Diagnostics();

  public static class Nomenclature {
    private String source;
    private String id;

    public String getSource() {
      return source;
    }

    public void setSource(String source) {
      this.source = source;
    }

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Nomenclature that = (Nomenclature) o;
      return Objects.equals(source, that.source) &&
          Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
      return Objects.hash(source, id);
    }
  }

  public static class Diagnostics {
    private NameUsageMatch.MatchType matchType;
    private Integer confidence;
    private TaxonomicStatus status;
    private List<String> lineage = Lists.newArrayList();
    private List<NameUsageMatch2> alternatives = Lists.newArrayList();
    private String note;

    public NameUsageMatch.MatchType getMatchType() {
      return matchType;
    }

    public void setMatchType(NameUsageMatch.MatchType matchType) {
      this.matchType = matchType;
    }

    public Integer getConfidence() {
      return confidence;
    }

    public void setConfidence(Integer confidence) {
      this.confidence = confidence;
    }

    public TaxonomicStatus getStatus() {
      return status;
    }

    public void setStatus(TaxonomicStatus status) {
      this.status = status;
    }

    public List<String> getLineage() {
      return lineage;
    }

    public void setLineage(List<String> lineage) {
      this.lineage = lineage;
    }

    public List<NameUsageMatch2> getAlternatives() {
      return alternatives;
    }

    public void setAlternatives(List<NameUsageMatch2> alternatives) {
      this.alternatives = alternatives;
    }

    public String getNote() {
      return note;
    }

    public void setNote(String note) {
      this.note = note;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Diagnostics that = (Diagnostics) o;
      return status == that.status &&
          matchType == that.matchType &&
          Objects.equals(confidence, that.confidence) &&
          Objects.equals(lineage, that.lineage) &&
          Objects.equals(alternatives, that.alternatives) &&
          Objects.equals(note, that.note);
    }

    @Override
    public int hashCode() {
      return Objects.hash(status, matchType, confidence, lineage, alternatives, note);
    }
  }

  public boolean isSynonym() {
    return synonym;
  }

  public void setSynonym(boolean synonym) {
    this.synonym = synonym;
  }

  public RankedName getUsage() {
    return usage;
  }

  public void setUsage(RankedName usage) {
    this.usage = usage;
  }

  public RankedName getAcceptedUsage() {
    return acceptedUsage;
  }

  public void setAcceptedUsage(RankedName acceptedUsage) {
    this.acceptedUsage = acceptedUsage;
  }

  public Object getNomenclature() {
    return nomenclature;
  }

  /**
   * the classification includes the accepted taxon concept view
   */
  public List<RankedName> getClassification() {
    return classification;
  }

  public void setClassification(List<RankedName> classification) {
    this.classification = classification;
  }

  public Diagnostics getDiagnostics() {
    return diagnostics;
  }

  public void setDiagnostics(Diagnostics diagnostics) {
    this.diagnostics = diagnostics;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    NameUsageMatch2 that = (NameUsageMatch2) o;
    return synonym == that.synonym &&
        Objects.equals(usage, that.usage) &&
        Objects.equals(acceptedUsage, that.acceptedUsage) &&
        Objects.equals(nomenclature, that.nomenclature) &&
        Objects.equals(classification, that.classification) &&
        Objects.equals(diagnostics, that.diagnostics);
  }

  @Override
  public int hashCode() {
    return Objects.hash(synonym, usage, acceptedUsage, nomenclature, classification, diagnostics);
  }
}
