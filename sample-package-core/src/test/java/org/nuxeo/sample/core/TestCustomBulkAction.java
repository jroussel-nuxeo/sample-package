package org.nuxeo.sample.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.nuxeo.ecm.core.bulk.message.BulkStatus.State.COMPLETED;
import static org.nuxeo.sample.core.CustomBulkAction.ACTION_NAME;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.bulk.BulkService;
import org.nuxeo.ecm.core.bulk.CoreBulkFeature;
import org.nuxeo.ecm.core.bulk.message.BulkCommand;
import org.nuxeo.ecm.core.bulk.message.BulkStatus;
import org.nuxeo.ecm.core.test.CoreFeature;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.TransactionalFeature;

import javax.inject.Inject;

@RunWith(FeaturesRunner.class)
@Features({ CoreFeature.class, CoreBulkFeature.class })
@Deploy({ "org.nuxeo.sample.core.sample-package-core" })
public class TestCustomBulkAction {

    @Inject
    protected TransactionalFeature txFeature;

    @Inject
    protected BulkService service;

    @Inject
    protected CoreSession session;

    @Test
    public void testAction() {

        // Change NXQL to retrieve the expected test documents
        String nxql = "SELECT * FROM Document WHERE ecm:isTrashed = 0";

        BulkCommand command = new BulkCommand.Builder(ACTION_NAME, nxql,
        session.getPrincipal().getName()).repository(session.getRepositoryName()).build();
        String commandId = service.submit(command);

        txFeature.nextTransaction();

        BulkStatus status = service.getStatus(commandId);
        assertNotNull(status);
        assertEquals(COMPLETED, status.getState());

        // Validate documents have been modified as expected(e.g. they are no longer retrieved by the NXQL if a flag metadata has been changed)
        assertEquals(0, session.query(nxql).size());
    }
}
