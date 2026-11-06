package org.jqassistant.plugin.giif.report.api.impl.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@JsonPropertyOrder({ "$schema", "version", "runs" })
public class GiifReport {

    private List<Rule> rules;

    private List<Issue> issues;