package org.nuxeo.sample.core;

import static org.nuxeo.ecm.core.bulk.BulkServiceImpl.STATUS_STREAM;
import static org.nuxeo.lib.stream.computation.AbstractComputation.INPUT_1;
import static org.nuxeo.lib.stream.computation.AbstractComputation.OUTPUT_1;

import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.bulk.action.computation.AbstractBulkComputation;
import org.nuxeo.lib.stream.computation.Topology;
import org.nuxeo.runtime.stream.StreamProcessorTopology;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CustomBulkAction implements StreamProcessorTopology {
  public static final String ACTION_NAME = "custombulkaction";

  @Override
  public Topology getTopology(Map<String, String> map) {
    return Topology.builder()
    .addComputation(CustomBulkActionComputation::new,
    Arrays.asList(INPUT_1 + ":" + ACTION_NAME, OUTPUT_1 + ":" + STATUS_STREAM))
    .build();
    }

  public static class CustomBulkActionComputation extends AbstractBulkComputation {

    public CustomBulkActionComputation() {
      super("bulk/" + ACTION_NAME);
    }

    @Override
    protected void compute(CoreSession session, List<String> ids, Map<String, Serializable> properties) {
      // TODO: implement code to execute against document ids
    }
  }
}
